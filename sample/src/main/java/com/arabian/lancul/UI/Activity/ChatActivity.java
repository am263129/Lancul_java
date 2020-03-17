package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    RecyclerView list;
    private EditText message;
    ImageButton send;
    String TAG = "-chat-";
    FirebaseFirestore db;
    String chat_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseApp.initializeApp(LoginActivity.getInstance());
        db = FirebaseFirestore.getInstance();
        get_chat_id();
        init_view();
        init_action();
    }

    private void get_chat_id() {
        db.collection("chats").document(Global.my_name + "&" + Global.chat_guider_name).collection("chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer size = task.getResult().size();
                            Log.e(TAG,String.valueOf(size));
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
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
                message.setText("");
                String chat_message = message.getText().toString();
                HashMap<String, Object> chat = new HashMap<>();
                chat.put("Chat_Content", chat_message);
                chat.put("Chat_Sent_Date", Global.getToday());
                chat.put("Chat_Id", Global.getToday());
                chat.put("Chat_Notification", "yet");
                chat.put("Chat_Sender", Global.my_name);
                chat.put("Chat_Status", "Unread");
                chat.put("Chat_Type", "Text");
                db.collection("chats").document(Global.my_name + " & " + Global.chat_guider_name).collection(chat_id).document(Global.getToday())
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


        final DocumentReference docRef = db.collection("chats").document(Global.my_name + " & " + Global.chat_guider_name);
        docRef . addSnapshotListener ( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
}
