package com.arabian.lancul.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class InviteGuiderActivity extends AppCompatActivity {

    ImageView guider_photo;
    TextView guider_bio;
    Guider guider;
    Button btn_send;
    EditText invite_message;
    String TAG = "Invite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_guider);
        Intent intent = getIntent();
        guider = Global.array_guider.get(Integer.parseInt(intent.getStringExtra("Index")));
        init_view();
    }

    private void init_view() {
        guider_photo = findViewById(R.id.invite_guider_photo);
        guider_bio = findViewById(R.id.invite_guider_bio);
        if(!guider.getImageURL().equals(""))
        {
            Glide.with(InviteGuiderActivity.this).load(guider.getImageURL()).into(guider_photo);
        }
        guider_bio.setText(guider.getBio().toString());
        btn_send = findViewById(R.id.btn_send_invite);
        invite_message = findViewById(R.id.edt_invoice);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_invite();
            }
        });
    }

    private void send_invite() {
        String message = invite_message.getText().toString();
        String date = Global.getToday();

        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> Invite = new HashMap<>();
        Invite.put("invite_content", message);
        Invite.put("invite_date", date);
        Invite.put("invite_sender_name", Global.my_name);
        Invite.put("invite_sender_email", Global.current_user_email);
        Invite.put("invite_status","New");
//        Invite.put("user_type", "client");
        db.collection("guiders").document(guider.getEmail()).collection("invite").document(Global.current_user_email)
                .set(Invite)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "upload user data:success");
                        upgrade_my_data(guider.getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed");
                    }
                });

    }


    private void upgrade_my_data(String guider_email) {
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> Invite = new HashMap<>();
        List<String> linked_guiders = Global.my_user_data.getLinked_guiders();
        linked_guiders.add(guider_email);
        Invite.put("user_linked_guiders",linked_guiders);
        db.collection("users").document(Global.my_email)
                .update(Invite)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "upload user data:success");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed");
                    }
                });

    }
}
