package com.arabian.lancul;



import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arabian.lancul.UI.Fragment.RetaurantFragment;
import com.arabian.meowbottomnavigation.MeowBottomNavigation;


/**
 * Created by 1HE on 2020-02-02.
 */

public class MainActivity extends AppCompatActivity {

    private final static int ID_HOME = 1;
    private final static int ID_EXPLORE = 2;
    private final static int ID_MESSAGE = 3;
    private final static int ID_NOTIFICATION = 4;
    private final static int ID_ACCOUNT = 5;

    FrameLayout main_frame;
    HorizontalScrollView flags;

    public  static  MainActivity self;
    TextView label_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;
        main_frame = findViewById(R.id.main_frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        label_toolbar = findViewById(R.id.label_tab);
        flags = findViewById(R.id.flags);

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_tab1));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_EXPLORE, R.drawable.ic_tab2));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_tab3));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.ic_tab4));
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
                        loadFragment(new RetaurantFragment());
                        break;
                    case ID_EXPLORE:
                        label_toolbar.setText("Experiences");
                        name = "EXPLORE";
                        break;
                    case ID_MESSAGE:
                        label_toolbar.setText("Friends");
                        flags.setVisibility(View.VISIBLE);
                        name = "MESSAGE";
                        break;
                    case ID_NOTIFICATION:
                        label_toolbar.setText("Restaurants");
                        name = "NOTIFICATION";
                        break;
                    case ID_ACCOUNT:
                        label_toolbar.setText("My Profile");
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
}
