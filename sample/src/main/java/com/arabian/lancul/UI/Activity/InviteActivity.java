package com.arabian.lancul.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arabian.lancul.R;

public class InviteActivity extends AppCompatActivity {

    Button share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        share = (Button)findViewById(R.id.btn_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }
    private void share() {
        final String appPackageName=getApplication().getPackageName();
        String shareBody = "Download "+getString(R.string.app_name)+" From :  "+"http://play.google.com/store/apps/details?id=" + appPackageName;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name));
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
    }
}
