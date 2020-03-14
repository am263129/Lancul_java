package com.arabian.lancul.UI.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.InviteActivity;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    View view;
    LinearLayout btn_favorite, btn_help, btn_feedback, btn_invite_friend, btn_rate_app, btn_about_us, btn_privacy_policy;
    ImageView btn_setting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profile, container, false);

        btn_favorite = view.findViewById(R.id.btn_favorite);
        btn_help = view.findViewById(R.id.btn_help);
        btn_feedback = view.findViewById(R.id.btn_feedback);
        btn_invite_friend = view.findViewById(R.id.btn_invitefriend);
        btn_rate_app = view.findViewById(R.id.btn_rate_app);
        btn_about_us = view.findViewById(R.id.btn_about_us);
        btn_privacy_policy = view.findViewById(R.id.btn_privacy_policy);
        btn_setting = view.findViewById(R.id.btn_setting);

        btn_favorite.setOnClickListener(this);
        btn_help.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);
        btn_invite_friend.setOnClickListener(this);
        btn_rate_app.setOnClickListener(this);
        btn_about_us.setOnClickListener(this);
        btn_privacy_policy.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent =  new Intent(MainActivity.getInstance(), InviteActivity.class);
        startActivity(intent);

        switch (view.getId()){
            case R.id.btn_setting:
                
                break;

        }
    }
}
