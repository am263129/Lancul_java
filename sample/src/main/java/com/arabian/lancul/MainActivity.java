package com.arabian.lancul;



import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Object.Res_Exp;
import com.arabian.lancul.UI.Util.Global;
import com.arabian.meowbottomnavigation.MeowBottomNavigation;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



/**
 * Created by 1HE on 2020-02-02.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int ID_HOME = 1;
    private final static int ID_EXPERIENCE = 2;
    private final static int ID_MESSAGE = 3;
    private final static int ID_RESTAURANT = 4;
    private final static int ID_ACCOUNT = 5;

    public String LANGUAGE_En = "English";
    public String LANGUAGE_Ge = "Deutsch";
    public String LANGUAGE_Fr = "Français";
    public String LANGUAGE_Ar = "العربية";
    public String LANGUAGE_Sp = "Español";
    public String LANGUAGE_Ja = "日本語";
    public String LANGUAGE_Po = "Português";

    FrameLayout main_frame;
    HorizontalScrollView flags;

    public  static  MainActivity self;
    private TextView label_toolbar;
    private Button button_logout;
    private MeowBottomNavigation bottomNavigation;
    private String TAG =  "MainActivity";
    private ImageView filter_en,filter_ja, filter_sa, filter_pt, filter_sp, filter_ge, filter_fr;
    private RelativeLayout selected_en, selected_ja, selected_sa, selected_pt, selected_sp, selected_ge, selected_fr;
    private boolean en = false, ja = false, sa = false , pt = false, sp = false, ge = false, fr = false;
    private List<String> filter_list = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;
        init_view();
        init_action();
        get_current_location();



    }

    private void get_current_location() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                           Upload_my_location(location);
                        }
                    }
                });
    }

    private void Upload_my_location(Location location) {
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();
        List<Double> my_location = new ArrayList<>();
        my_location.clear();
        my_location.add(lat);
        my_location.add(lon);
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> status = new HashMap<>();
        status.put("user_location", my_location);
        db.collection("users").document(Global.my_email).update(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "upload user data:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed");
                    }
                });

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
        selected_en = (RelativeLayout)findViewById(R.id.f_en);
        selected_fr = (RelativeLayout)findViewById(R.id.f_fr);
        selected_ge = (RelativeLayout)findViewById(R.id.f_ge);
        selected_ja = (RelativeLayout)findViewById(R.id.f_ja);
        selected_pt = (RelativeLayout)findViewById(R.id.f_pt);
        selected_sa = (RelativeLayout)findViewById(R.id.f_sa);
        selected_sp = (RelativeLayout)findViewById(R.id.f_sp);
        filter_en = findViewById(R.id.selected_en);
        filter_fr = findViewById(R.id.selected_fr);
        filter_ge = findViewById(R.id.selected_ge);
        filter_ja = findViewById(R.id.selected_ja);
        filter_pt = findViewById(R.id.selected_pt);
        filter_sa = findViewById(R.id.selected_sa);
        filter_sp = findViewById(R.id.selected_sp);
        selected_pt.setOnClickListener(this);
        selected_en.setOnClickListener(this);
        selected_ja.setOnClickListener(this);
        selected_fr.setOnClickListener(this);
        selected_ge.setOnClickListener(this);
        selected_sa.setOnClickListener(this);
        selected_sp.setOnClickListener(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void init_action(){
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_tab1));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_EXPERIENCE, R.drawable.ic_tab2));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_tab3));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_RESTAURANT, R.drawable.ic_tab4));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT, R.drawable.ic_tab5));

//        bottomNavigation.setCount(ID_MESSAGE,"1");

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
                        label_toolbar.setText(getString(R.string.toolbar_label_saudiarabia));
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new HomeFragment());
                        break;
                    case ID_EXPERIENCE:
                        label_toolbar.setText(getString(R.string.toolbar_label_experiences));
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new ExperienceFragment());
                        name = "EXPLORE";
                        break;
                    case ID_MESSAGE:
                        label_toolbar.setText(getString(R.string.toolbar_label_friends));
                        flags.setVisibility(View.VISIBLE);
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new LivechatFragment());
                        name = "MESSAGE";
                        break;
                    case ID_RESTAURANT:
                        label_toolbar.setText(getString(R.string.toolbar_label_restaurants));
                        button_logout.setVisibility(View.GONE);
                        name = "NOTIFICATION";
                        loadFragment(new RetaurantFragment());
                        break;
                    case ID_ACCOUNT:
                        label_toolbar.setText(getString(R.string.toolbar_label_my_profile));
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
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Button okButton = (Button) dialog.findViewById(R.id.btn_logout);
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
//        LoginActivity.get_user();
//        LoginActivity.get_guider();
//        LoginActivity.get_chat();
//        LoginActivity.get_feedback();
        if(Global.go_profile){
            Global.go_profile = false;
            bottomNavigation.show(ID_ACCOUNT,true);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.f_en:
                en = !en;
                filter_en.setVisibility(View.INVISIBLE);
                filter_list.remove(LANGUAGE_En);
                if(en)
                {
                    filter_list.add(LANGUAGE_En);
                    filter_en.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.f_ja:
                ja = !ja;
                filter_ja.setVisibility(View.INVISIBLE);
                filter_list.remove(LANGUAGE_Ja);
                if(ja)
                {
                    filter_list.add(LANGUAGE_Ja);
                    filter_ja.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f_sa:
                sa = !sa;
                filter_sa.setVisibility(View.INVISIBLE);
                filter_list.remove(LANGUAGE_Ar);
                if(sa)
                {
                    filter_list.add(LANGUAGE_Ar);
                    filter_sa.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f_fr:
                fr = !fr;
                filter_fr.setVisibility(View.INVISIBLE);
                filter_list.remove(LANGUAGE_Fr);
                if(fr)
                {
                    filter_list.add(LANGUAGE_Fr);
                    filter_fr.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f_ge:
                ge = !ge;
                filter_ge.setVisibility(View.INVISIBLE);
                filter_list.remove(LANGUAGE_Ge);
                if(ge)
                {
                    filter_list.add(LANGUAGE_Ge);
                    filter_ge.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f_pt:
                pt = !pt;
                filter_pt.setVisibility(View.INVISIBLE);
                filter_list.remove(LANGUAGE_Po);
                if(pt)
                {
                    filter_list.add(LANGUAGE_Po);
                    filter_pt.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f_sp:
                sp = !sp;
                filter_sp.setVisibility(View.INVISIBLE);
                filter_list.remove(LANGUAGE_Sp);
                if(sp)
                {
                    filter_list.add(LANGUAGE_Sp);
                    filter_sp.setVisibility(View.VISIBLE);
                }
                break;
        }

        LivechatFragment.filter_guiders(this,filter_list);
    }


}
