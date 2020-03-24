package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Adapter.ChatAdapter;
import com.arabian.lancul.UI.Object.Chat;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chat_list;
    private EditText message;
    ImageButton send;
    String TAG = "-chat-";
    FirebaseFirestore db;
    String chat_id = "00001";
    Integer partner_index;
    String chat_document = "";
    ProgressDialog loading;
    RelativeLayout peding, chat;
    boolean is_pending = false;
    private FirebaseFirestore mDb;
    private ListenerRegistration mChatMessageEventListener, mUserListEventListener;
    private ChatAdapter mChatMessageRecyclerAdapter;
    private Set<String> mMessageIds = new HashSet<>();
    private ArrayList<Chat> mMessages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        try{
            is_pending = intent.getBooleanExtra("pending",false);
        }
        catch (Exception E){
            Log.e(TAG, "No pending");
        }

        partner_index = intent.getIntExtra("partner_index", 0);
        if(Global.user_mode) {
            chat_document = Global.my_email + ":" + Global.array_guider.get(partner_index).getEmail();
            Global.partner_photo = Global.array_guider.get(partner_index).getImageURL();
        }
        else{
            chat_document = Global.my_clients.get(partner_index).getEmail() + ":" + Global.my_email;
            Global.partner_photo = Global.my_clients.get(partner_index).getPhoto();
        }
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        db = FirebaseFirestore.getInstance();

//        get_chat_id();
        init_view();
        initChatroomRecyclerView();
        init_action();
    }


    private void joinChatroom(){


        DocumentReference joinChatroomRef = mDb
                .collection("chats")
                .document(chat_document)
                .collection("private_chat")
                .document(FirebaseAuth.getInstance().getUid());

        Log.d(TAG, "joinChatroom: user: " + Global.my_email);
        joinChatroomRef.set(Global.my_user_data); // Don't care about listening for completion.
    }

    private void insertNewMessage(){
        String chat_message = message.getText().toString();

        if(!chat_message.equals("")){
            chat_message = chat_message.replaceAll(System.getProperty("line.separator"), "");

            DocumentReference newMessageDoc = mDb
                    .collection("chats")
                    .document(chat_document)
                    .collection("private_chat")
                    .document();
            Chat newChatMessage = new Chat();
            newChatMessage.setChat_content(chat_message);
            newChatMessage.setChat_id(newMessageDoc.getId());
            newChatMessage.setChat_sender(Global.my_email);
            newMessageDoc.set(newChatMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        clearMessage();
                    }else{
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, "Failed to send message.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void initChatroomRecyclerView(){
        mChatMessageRecyclerAdapter = new ChatAdapter(this,mMessages);
        chat_list.setAdapter(mChatMessageRecyclerAdapter);
        chat_list.setLayoutManager(new LinearLayoutManager(this));

        chat_list.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    chat_list.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mMessages.size() > 0){
                                chat_list.smoothScrollToPosition(
                                        chat_list.getAdapter().getItemCount() - 1);
                            }

                        }
                    }, 100);
                }
            }
        });

    }


    private void getChatMessages(){

        CollectionReference messagesRef = mDb
                .collection("chats")
                .document(chat_document)
                .collection("private_chat");

        mChatMessageEventListener = messagesRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "onEvent: Listen failed.", e);
                            return;
                        }

                        if(queryDocumentSnapshots != null){
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                                Chat message = doc.toObject(Chat.class);
                                if(!mMessageIds.contains(message.getChat_id())){
                                    mMessageIds.add(message.getChat_id());
                                    mMessages.add(message);
                                    chat_list.smoothScrollToPosition(mMessages.size() - 1);
                                }

                            }
                            mChatMessageRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }


    private void clearMessage(){
        message.setText("");
    }



    private void get_chat_id() {

        db.collection("chats").document(chat_document).collection("private_chat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            if(count == 0){

                                chat_id = "00001";
                            }
                            else {
                                chat_id = String.valueOf(count + 1);
                                Integer limit = chat_id.length();
                                if (chat_id.length()<5)
                                    for (int i = 0; i < 5 - limit; i ++){
                                        chat_id = "0" + chat_id;
                                    }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void init_view() {
        chat_list = findViewById(R.id.chat_list);
        message = findViewById(R.id.chat_text);
        send = findViewById(R.id.btn_send);
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setTitle("Please wait...");
        peding = findViewById(R.id.pending);
        chat = findViewById(R.id.chat_active);
        if (is_pending){
            peding.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
        }
        else
        {
            chat.setVisibility(View.VISIBLE);
            peding.setVisibility(View.GONE);
        }
        mDb = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);

    }
    private void init_action() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNewMessage();
//                get_chat_id();
//                String chat_message = message.getText().toString();
//                HashMap<String, Object> chat = new HashMap<>();
//                chat.put("Chat_Content", chat_message);
//                chat.put("Chat_Sent_Date", Global.getToday());
//                chat.put("Chat_Id", chat_id);
//                chat.put("Chat_Notification", "yet");
//                chat.put("Chat_Sender", Global.my_name);
//                chat.put("Chat_Status", "Unread");
//                chat.put("Chat_Type", "Text");
//                db.collection("chats").document(chat_document).collection("private_chat").document(chat_id)
//                        .set(chat)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.e(TAG, "success");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e(TAG,"Failed");
//                            }
//                        });
//
//                message.setText("");
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

    @Override
    protected void onResume() {
        super.onResume();
        getChatMessages();
    }

}
