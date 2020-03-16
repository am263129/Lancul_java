package com.arabian.lancul.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
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
    MeowBottomNavigation bottomNavigation;
    String TAG =  "GuiderActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider);
    }
}
