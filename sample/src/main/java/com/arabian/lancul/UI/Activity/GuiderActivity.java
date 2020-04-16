package com.arabian.lancul.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Fragment.ExperienceFragment;
import com.arabian.lancul.UI.Fragment.HomeFragment;
import com.arabian.lancul.UI.Fragment.InviteFragment;
import com.arabian.lancul.UI.Fragment.LivechatFragment;
import com.arabian.lancul.UI.Fragment.ProfileFragment;
import com.arabian.lancul.UI.Fragment.RetaurantFragment;
import com.arabian.lancul.UI.Fragment.SoonFragment;
import com.arabian.lancul.UI.Fragment.chatFragment_guider;
import com.arabian.lancul.UI.Util.Global;
import com.arabian.meowbottomnavigation.MeowBottomNavigation;

public class GuiderActivity extends AppCompatActivity {


    private final static int ID_CHAT = 1;
    private final static int ID_INVITE = 2;
    private final static int ID_MY_FEEDBACK = 3;
    private final static int ID_NEW_FEED = 4;
    private final static int ID_PROFILE = 5;

    FrameLayout main_frame;

    public  static GuiderActivity self;
    TextView label_toolbar;
    Button button_logout;
    HorizontalScrollView flags;
    RelativeLayout search_field;
    MeowBottomNavigation bottomNavigation;
    String TAG =  "GuiderActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider);
        self = this;
        init_view();
        init_action();
    }



    private void init_view(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        main_frame = findViewById(R.id.main_frame_guider);
        label_toolbar = findViewById(R.id.label_tab);
        button_logout = findViewById(R.id.button_logout);
        flags = findViewById(R.id.flags);
        search_field = findViewById(R.id.search_field);
        bottomNavigation = findViewById(R.id.bottomNavigation_guider);

    }

    private void init_action(){
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_CHAT, R.drawable.ic_chat));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_INVITE, R.drawable.ic_invite));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MY_FEEDBACK, R.drawable.ic_feedback));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NEW_FEED, R.drawable.ic_new_feed));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_tab5));

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
                search_field.setVisibility(View.GONE);
                switch (item.getId()) {
                    case ID_CHAT:
                        name = "HOME";
                        label_toolbar.setText("Users");
                        button_logout.setVisibility(View.GONE);
                        search_field.setVisibility(View.VISIBLE);
                        loadFragment(new chatFragment_guider());
                        break;
                    case ID_INVITE:
                        label_toolbar.setText("Invite");
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new InviteFragment());
                        name = "EXPLORE";
                        break;
                    case ID_MY_FEEDBACK:
                        label_toolbar.setText("My Feedback");
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new SoonFragment());
                        name = "MESSAGE";
                        break;
                    case ID_NEW_FEED:
                        label_toolbar.setText("New Feed");
                        button_logout.setVisibility(View.GONE);
                        loadFragment(new SoonFragment());
                        name = "NOTIFICATION";
                        break;
                    case ID_PROFILE:
                        label_toolbar.setText("My Profile");
                        button_logout.setVisibility(View.VISIBLE);
                        loadFragment(new SoonFragment());
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


        bottomNavigation.show(ID_CHAT,true);


        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(GuiderActivity.this);
                dialog.setContentView(R.layout.modal);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Button okButton = (Button) dialog.findViewById(R.id.btn_logout);
                Button cancelButton = (Button) dialog.findViewById(R.id.btn_cancel);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = new Intent(GuiderActivity.getInstance(), LoginActivity.class);
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

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_guider, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    public static GuiderActivity getInstance(){
        return self;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Global.go_profile){
            Global.go_profile = false;
            bottomNavigation.show(ID_CHAT,true);
        }
    }
}
