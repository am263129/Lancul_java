package com.arabian.lancul;



import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arabian.lancul.UI.Fragment.ExperienceFragment;
import com.arabian.lancul.UI.Fragment.HomeFragment;
import com.arabian.lancul.UI.Fragment.LivechatFragment;
import com.arabian.lancul.UI.Fragment.ProfileFragment;
import com.arabian.lancul.UI.Fragment.RetaurantFragment;
import com.arabian.lancul.UI.Util.Global;
import com.arabian.meowbottomnavigation.MeowBottomNavigation;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;

        init_data();
        main_frame = findViewById(R.id.main_frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        label_toolbar = findViewById(R.id.label_tab);

        button_logout = findViewById(R.id.button_logout);

        flags = findViewById(R.id.flags);

        bottomNavigation = findViewById(R.id.bottomNavigation);

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
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Do you want to logout?");
                // alert.setMessage("Message");

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Your action here
                    }
                });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

                alert.show();


            }

        });
    }

    private void init_data() {
//        GoogleCredentials credentials = null;
//        try {
//            credentials = GoogleCredentials.getApplicationDefault();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(credentials)
//                .build();
//        FirebaseApp.initializeApp(options);
//
//        Firestore db = FirestoreClient.getFirestore();
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
