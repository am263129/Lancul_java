package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    RecyclerView list;
    private EditText message;
    ImageButton send;
    String TAG = "-chat-";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init_view();
        init_action();



    }

    private void init_view() {
        list = findViewById(R.id.chat_list);
        message = findViewById(R.id.chat_text);
        send = findViewById(R.id.btn_send);

    }
    private void init_action() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chat_message = message.getText().toString();
                FirebaseApp.initializeApp(LoginActivity.getInstance());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                HashMap<String, Object> chat = new HashMap<>();
                chat.put("Chat_Content", chat_message);
                chat.put("Chat_Sent_Date", Global.getToday());
                chat.put("Chat_Id", Global.getToday());
                chat.put("Chat_Notification", "yet");
                chat.put("Chat_Sender", Global.my_name);
                chat.put("Chat_Status", "Unread");
                chat.put("Chat_Type", "Text");
                db.collection("chats").document(Global.my_name + " & " + Global.chat_guider_name)
                        .set(chat)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e(TAG, "success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,"Failed");
                            }
                        });
            }
        });
    }
}
