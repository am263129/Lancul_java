package com.arabian.lancul;



import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arabian.lancul.UI.Activity.LoginActivity;
import com.arabian.lancul.UI.Fragment.ExperienceFragment;
import com.arabian.lancul.UI.Fragment.HomeFragment;
import com.arabian.lancul.UI.Fragment.LivechatFragment;
import com.arabian.lancul.UI.Fragment.ProfileFragment;
import com.arabian.lancul.UI.Fragment.RetaurantFragment;
import com.arabian.lancul.UI.Fragment.signinFragment;
import com.arabian.lancul.UI.Object.Res_Exp;
import com.arabian.lancul.UI.Util.Global;
import com.arabian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;


/**
 * Created by 1HE on 2020-02-02.
 */

public class MainActivity extends AppCompatActivity {

    private final static int ID_HOME = 1;
    private final static int ID_EXPERIENCE = 2;
    private final static int ID_MESSAGE = 3;
    private final static int ID_RESTAURANT = 4;
    private final static int ID_ACCOUNT = 5;

    FrameLayout main_frame;
    HorizontalScrollView flags;

    public  static  MainActivity self;
    TextView label_toolbar;
    Button button_logout;
    MeowBottomNavigation bottomNavigation;
    String TAG =  "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;
        init_data();
        init_view();
        init_action();



    }

    private void init_view(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        main_frame = findViewById(R.id.main_frame);
        label_toolbar = findViewById(R.id.label_tab);
        button_logout = findViewById(R.id.button_logout);
        flags = findViewById(R.id.flags);
        bottomNavigation = findViewById(R.id.bottomNavigation);

    }

    private void init_action(){
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_tab1));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_EXPERIENCE, R.drawable.ic_tab2));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_tab3));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_RESTAURANT, R.drawable.ic_tab4));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT, R.drawable.ic_tab5));


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                String name;
                flags.setVisibility(View.GONE);
                switch (item.getId()) {
                    case ID_HOME:
                        name = "HOME";
                        label_toolbar.setText("Saudi Arabia");
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new HomeFragment());
                        break;
                    case ID_EXPERIENCE:
                        label_toolbar.setText("Experiences");
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new ExperienceFragment());
                        name = "EXPLORE";
                        break;
                    case ID_MESSAGE:
                        label_toolbar.setText("Friends");
                        flags.setVisibility(View.VISIBLE);
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new LivechatFragment());
                        name = "MESSAGE";
                        break;
                    case ID_RESTAURANT:
                        label_toolbar.setText("Restaurants");
                        button_logout.setVisibility(View.GONE);
                        name = "NOTIFICATION";
                        loadFragment(new RetaurantFragment());
                        break;
                    case ID_ACCOUNT:
                        label_toolbar.setText("My Profile");
                        button_logout.setVisibility(View.VISIBLE);
                        loadFragment(new ProfileFragment());
                        name = "ACCOUNT";
                        break;
                    default:
                        name = "";
                }

            }
        });


        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });


        bottomNavigation.show(ID_HOME,true);


        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.modal);

                Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
                Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = new Intent(MainActivity.getInstance(), LoginActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }

        });

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
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    public static MainActivity getInstance(){
        return self;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Global.go_profile){
            Global.go_profile = false;
            bottomNavigation.show(ID_ACCOUNT,true);
        }
    }
}
