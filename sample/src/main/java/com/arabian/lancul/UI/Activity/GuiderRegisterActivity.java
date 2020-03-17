package com.arabian.lancul.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.arabian.lancul.R;

import java.util.zip.Inflater;

public class GuiderRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText guider_firstname,
            guider_lastname,
            guider_address,
            guider_email,
            guider_phone,
            guider_birthday,
            guider_bio;
    HorizontalScrollView guider_language;
    ImageView flag_international,
            flag_america,
            flag_saudi,
            flag_brazil,
            flag_japan,
            flag_spain,
            flag_germany,
            flag_france;
    Button btn_signup, btn_cancel;
    ImageView guider_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guider_register);
        init_view();
        init_action();
    }

    private void init_action() {
        guider_language.setOnClickListener(this);
    }

    private void init_view() {
        guider_firstname = findViewById(R.id.edt_guider_firstname);
        guider_lastname = findViewById(R.id.edt_guider_lastname);
        guider_address = findViewById(R.id.edt_guider_address);
        guider_email = findViewById(R.id.edt_guider_email);
        guider_phone = findViewById(R.id.edt_guider_phone);
        guider_birthday = findViewById(R.id.edt_guider_birthday);
        guider_bio = findViewById(R.id.edt_guider_bio);
        flag_international = findViewById(R.id.flag_international);
        flag_america = findViewById(R.id.flag_america);
        flag_saudi = findViewById(R.id.flag_saudi);
        flag_brazil = findViewById(R.id.flag_brazil);
        flag_japan = findViewById(R.id.flag_japan);
        flag_spain = findViewById(R.id.flag_spain);
        flag_germany = findViewById(R.id.flag_germany);
        flag_france = findViewById(R.id.flag_france);
        btn_signup = findViewById(R.id.btn_guider_signup);
        btn_cancel = findViewById(R.id.btn_guider_cancel);
        guider_language = findViewById(R.id.area_language);
        guider_photo = findViewById(R.id.img_guider_photo);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.area_language:
                show_language_selecter();
                break;
            case R.id.btn_guider_signup:
                break;
            case R.id.btn_guider_cancel:
                finish();
                break;

        }
    }

    private void show_language_selecter() {
//        Inflater inflater  = new Inflater();
//        inflater.inflate(R.layout.)
    }
}
