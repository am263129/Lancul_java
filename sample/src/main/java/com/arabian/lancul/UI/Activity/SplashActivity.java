package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.arabian.lancul.R;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Object.Feedback;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Object.Invite;
import com.arabian.lancul.UI.Object.Res_Exp;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.arabian.lancul.UI.Util.Global.array_chat_ids;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = "Splash Activity";
    private static boolean ready_data_experience = false;
    private static boolean ready_data_restaurant = false;
    private static boolean ready_guider = false;
    private static boolean ready_user = false;
    private static boolean ready_feedback = false;
    private static boolean ready_chat = false;
    private ListenerRegistration mChatMessageEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init_data();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume(){
        super.onResume();
        get_chat();
        get_guider();
        get_user();

    }
    private void init_data() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiences")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Global.array_experience.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.get("name").toString();
                                String location = document.get("location").toString();
                                String photo = document.get("imageUrl").toString();
                                Float rating = Float.parseFloat(document.get("rating").toString());

                                Res_Exp experience = new Res_Exp(name,location,photo,rating);
                                Global.array_experience.add(experience);

                            }
                            ready_data_experience = true;
                            check_loading();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //get Restaurant data
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Global.array_restaurant.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.get("name").toString();
                                String location = document.get("location").toString();
                                String photo = document.get("imageUrl").toString();
                                Float rating = Float.parseFloat(document.get("rating").toString());

                                Res_Exp restaurant = new Res_Exp(name,location,photo,rating);
                                Global.array_restaurant.add(restaurant);

                            }
                            ready_data_restaurant = true;
                            check_loading();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
    public void get_guider(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("guiders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Global.array_guider.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                try {
                                    String bio = document.get("guider_bio").toString();
                                    String imageUrl = document.get("guider_photo").toString();
                                    String phone = document.get("guider_phone").toString();
                                    String email = document.get("guider_email").toString();
                                    boolean is_available = Boolean.parseBoolean(document.get("guider_available").toString());
                                    String name = document.get("guider_firstname").toString() + " " + document.get("guider_lastname").toString();
                                    Float rating = 0f;
                                    boolean new_guider = false;
                                    if (document.get("guider_rating").toString().equals("New")) {
                                        new_guider = true;
                                    } else {
                                        rating = Float.parseFloat(document.get("guider_rating").toString());
                                    }
                                    boolean verified = Boolean.parseBoolean(document.get("guider_verified").toString());

                                    List<String> languages = (List<String>) document.get("guider_languages");
                                    List<Double> location = new ArrayList<>();
                                    try {
                                        location = (List<Double>) document.get("guider_location");
                                    }
                                    catch (Exception E){
                                        Log.e(TAG, E.toString());
                                    }
                                    String address = "";
                                    String birthday = "";
                                    try {
                                        address = document.get("guider_address").toString();
                                        birthday = document.get("guider_birthday").toString();
                                    }
                                    catch (Exception E){
                                        Log.e(TAG,"No address and birthday");
                                    }
                                    String status = document.get("guider_status").toString();

                                    Guider guider = new Guider(bio, imageUrl, name, rating, is_available, verified, languages, phone, email, address, birthday, status, new_guider,location);
                                    Global.array_guider.add(guider);
                                }
                                catch (Exception E){
                                    Log.e(TAG, E.toString());
                                }

                            }
                            get_feedback();
                            ready_guider = true;
                            check_loading();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void get_user(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference messagesRef = db
                .collection("users");

        mChatMessageEventListener = messagesRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "onEvent: Listen failed.", e);
                            return;
                        }
                        Global.array_client.clear();
                        if(queryDocumentSnapshots != null){
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = "",email = "",pole = "",status = "",photo = "";
                                if(document.get("user_name") == null) continue;
                                name = document.get("user_name").toString();
                                email = document.get("user_email").toString();
                                pole = document.get("user_type").toString();
                                status = document.get("user_status").toString();
                                photo = document.get("user_photo").toString();
                                List<String> linked_guiders = (List<String>) document.get("user_linked_guiders");
                                List<Double> location = new ArrayList<>();
                                Log.e("name",name+":"+email);
                                try{
                                    location = (List<Double>) document.get("user_location");
                                }
                                catch (Exception E){
                                    Log.e(TAG, "No_location info");
                                }
                                if(pole.equals("client")){
                                    Client client = new Client(name, email, status, photo, linked_guiders, location);
                                    Global.array_client.add(client);
                                }

                            }

                        }
                        ready_user = true;
                        check_loading();
                    }
                });
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            Global.array_client.clear();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                String name = document.get("user_name").toString();
//                                String email = document.get("user_email").toString();
//                                String pole = document.get("user_type").toString();
//                                String status = document.get("user_status").toString();
//                                String photo = document.get("user_photo").toString();
//                                List<String> linked_guiders = (List<String>) document.get("user_linked_guiders");
//                                List<Double> location = new ArrayList<>();
//                                try{
//                                    location = (List<Double>) document.get("user_location");
//                                }
//                                catch (Exception E){
//                                    Log.e(TAG, "No_location info");
//                                }
//                                if(pole.equals("client")){
//                                    Client client = new Client(name, email, status, photo, linked_guiders, location);
//                                    Global.array_client.add(client);
//                                }
//                            }
//                            ready_user = true;
//                            check_loading();
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
    }
    public void get_feedback() {
        for (int  i = 0; i < Global.array_guider.size(); i ++){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final int finalI = i;
            db.collection("guiders").document(Global.array_guider.get(i).getEmail()).collection("feedback")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                String temp = Global.array_guider.get(finalI).getEmail();
                                ArrayList<Feedback> feedbacks = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    try {
                                        Feedback feedback = document.toObject(Feedback.class);
                                        feedbacks.add(feedback);
                                    }
                                    catch (Exception E){
                                        Log.e(TAG, E.toString());
                                    }
                                }

                                Global.array_guider.get(finalI).setFeedbacks(feedbacks);

                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        ready_feedback = true;
        check_loading();
    }
    public void get_chat(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            array_chat_ids.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                array_chat_ids.add(document.getId());
                            }
//                            for (int i = 0; i< array_chat_ids.size(); i++){
//                                if(array_chat_ids.get(i).toLowerCase().contains(Global.my_name)){
//
//                                }
//                            }

                            ready_chat = true;
                            check_loading();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    private void check_loading(){
        if(ready_chat && ready_data_experience && ready_data_restaurant && ready_feedback && ready_guider && ready_user ){
            ready_chat = ready_data_experience = ready_data_restaurant = ready_feedback = ready_guider = ready_user = false;
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
            return;
    }

}
