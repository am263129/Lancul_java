package com.arabian.lancul.UI.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.EditProfileActivity;
import com.arabian.lancul.UI.Activity.GuiderActivity;
import com.arabian.lancul.UI.Activity.InviteActivity;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    View view;
    LinearLayout btn_favorite, btn_help, btn_feedback, btn_invite_friend, btn_rate_app, btn_about_us, btn_privacy_policy;
    ImageView btn_setting;
    TextView my_email;

    public static ImageView my_photo;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        my_email = view.findViewById(R.id.label_my_email);
        btn_favorite = view.findViewById(R.id.btn_favorite);
        btn_help = view.findViewById(R.id.btn_help);
        btn_feedback = view.findViewById(R.id.btn_feedback);
        btn_invite_friend = view.findViewById(R.id.btn_invitefriend);
        btn_rate_app = view.findViewById(R.id.btn_rate_app);
        btn_about_us = view.findViewById(R.id.btn_about_us);
        btn_privacy_policy = view.findViewById(R.id.btn_privacy_policy);
        btn_setting = view.findViewById(R.id.btn_setting);
        my_photo = view.findViewById(R.id.circle_image_view_profile_nav_header);
        my_email.setText(Global.my_email);
        btn_favorite.setOnClickListener(this);
        btn_help.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);
        btn_invite_friend.setOnClickListener(this);
        btn_rate_app.setOnClickListener(this);
        btn_about_us.setOnClickListener(this);
        btn_privacy_policy.setOnClickListener(this);
        btn_setting.setOnClickListener(this);

        if(Global.iamguider) {
            if(!Global.my_guider_data.getImageURL().equals(""))
                Glide.with(getActivity()).load(Global.my_guider_data.getImageURL()).into(my_photo);
        }
        else if(!Global.my_user_data.getPhoto().equals("")){
            Glide.with(getActivity()).load(Global.my_user_data.getPhoto()).into(my_photo);
        }
        return view;
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        Intent viewIntent;
        Context context;
        if(Global.iamguider){
            context = GuiderActivity.getInstance();
        }
        else{
            context = MainActivity.getInstance();
        }
        switch (view.getId()){
            case R.id.btn_rate_app:
                rate_app();
                break;
            case R.id.btn_setting:
                intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_about_us:
                viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://lancul.net"));
                startActivity(viewIntent);
                break;
            case R.id.btn_privacy_policy:
                viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://lancul.net/privacy-policy"));
                startActivity(viewIntent);
                break;
            default:
                intent =  new Intent(context, InviteActivity.class);
                startActivity(intent);
                break;


        }

    }

    private void rate_app() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + MainActivity.getInstance().getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.getInstance().getPackageName())));
        }
    }


}
