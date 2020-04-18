package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Adapter.ChatAdapter;
import com.arabian.lancul.UI.Object.Chat;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Object.Invite;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;
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
    RelativeLayout peding, chat, content;

    boolean is_pending = false;
    private FirebaseFirestore mDb;
    private ListenerRegistration mChatMessageEventListener, mUserListEventListener;
    private ChatAdapter mChatMessageRecyclerAdapter;
    private Set<String> mMessageIds = new HashSet<>();
    private ArrayList<Chat> mMessages = new ArrayList<>();
    private ListenerRegistration mAcceptEventListener;
    private ImageView partner_photo,rate_partner;
    private TextView partner_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init_view();
        get_rateable();
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
            content.setBackgroundResource(R.drawable.gradient_background);
            Glide.with(ChatActivity.this).load(Global.array_guider.get(partner_index).getImageURL()).into(partner_photo);
            partner_name.setText(Global.array_guider.get(partner_index).getName());
        }
        else{
            chat_document = Global.my_clients.get(partner_index).getEmail() + ":" + Global.my_email;
            Global.partner_photo = Global.my_clients.get(partner_index).getPhoto();
            content.setBackgroundResource(R.drawable.gradient_background_guider);
            Glide.with(ChatActivity.this).load(Global.array_client.get(partner_index).getPhoto()).into(partner_photo);
            partner_name.setText(Global.array_client.get(partner_index).getName());
        }
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        db = FirebaseFirestore.getInstance();

//        get_chat_id();

        initChatroomRecyclerView();
        init_action();
    }

    private void get_rateable() {

    }

    private void init_chatable() {
        String document_id = "";
        String subdocument_id = "";
        if(Global.user_mode) {
            document_id = Global.array_guider.get(partner_index).getEmail();
            subdocument_id = Global.my_email;
        }
        else{
            document_id = Global.my_email;
            subdocument_id = Global.my_clients.get(partner_index).getEmail();
        }
        mDb = FirebaseFirestore.getInstance();
        DocumentReference messagesRef = mDb
                .collection("guiders")
                .document(document_id)
                .collection("invite").document(subdocument_id);

        mAcceptEventListener = messagesRef.
        addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.get("invite_status"));
                    if(snapshot.get("invite_status").equals("Accepted")){
                        enable_chat();
                    }
                    else{
                        disable_chat();
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void enable_chat(){
        getSupportActionBar().show();
        chat.setVisibility(View.VISIBLE);
        peding.setVisibility(View.GONE);
    }
    private void disable_chat(){
        getSupportActionBar().hide();
        peding.setVisibility(View.VISIBLE);
        chat.setVisibility(View.GONE);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        content = findViewById(R.id.content_chat);
        chat_list = findViewById(R.id.chat_list);
        message = findViewById(R.id.chat_text);
        send = findViewById(R.id.btn_send);
        loading = new ProgressDialog(this);
        partner_photo = findViewById(R.id.partner_photo);
        partner_name = findViewById(R.id.partner_name);
        rate_partner = findViewById(R.id.btn_rate);
        loading.setCancelable(false);
        loading.setTitle(getString(R.string.progress_wait));
        peding = findViewById(R.id.pending);
        chat = findViewById(R.id.chat_active);
        mDb = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDb.setFirestoreSettings(settings);


    }

    private void show_accept_dialog() {
        final Dialog dialog= new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.dialog_rate_partner, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.setContentView(dialog_view);
        Rect displayRectangle = new Rect();
        Window full_window = this.getWindow();
        full_window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog_view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));

        final RatingBar rating = dialog_view.findViewById(R.id.rating);
        final EditText feedback = dialog_view.findViewById(R.id.edt_feedback);


        Button accept = dialog_view.findViewById(R.id.btn_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_feedback(rating.getRating(),feedback.getText().toString());
                dialog.dismiss();
            }
        });
        Button cancel = dialog_view.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void upload_feedback(float rating, String toString) {
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

        rate_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_accept_dialog();
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
        init_chatable();
    }

}
