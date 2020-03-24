package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiderRegisterActivity extends AppCompatActivity implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    private static final String TAG = "GuiderRegister";
    EditText guider_firstname,
            guider_lastname,
            guider_address,
            guider_email,
            guider_password,
            guider_phone,
            guider_birthday,
            guider_bio;

    CardView guider_language;
    ImageView
            flag_america,
            flag_saudi,
            flag_brazil,
            flag_japan,
            flag_spain,
            flag_germany,
            flag_france;
    Button btn_signup, btn_cancel,btn_language;
    ImageView guider_photo, password_switcher;

    CheckBox chk_america, chk_germany,chk_spain, chk_arabia ,chk_portugal, chk_japan, chk_france;

    ArrayList<String> skill_language ;
    public String LANGUAGE_En = "English";
    public String LANGUAGE_Ge = "Deutsch";
    public String LANGUAGE_Fr = "Français";
    public String LANGUAGE_Ar = "العربية";
    public String LANGUAGE_Sp = "Español";
    public String LANGUAGE_Ja = "日本語";
    public String LANGUAGE_Po = "Português";

    boolean hide = false;
    ProgressDialog loading;

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
        guider_password = findViewById(R.id.edt_guider_password);
        guider_phone = findViewById(R.id.edt_guider_phone);
        guider_birthday = findViewById(R.id.edt_guider_birthday);
        guider_bio = findViewById(R.id.edt_guider_bio);
        btn_language = findViewById(R.id.btn_language);
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
        password_switcher = findViewById(R.id.btn_switch_password_show);
        btn_language.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        password_switcher.setOnClickListener(this);
        skill_language = new ArrayList<>();
        loading = new ProgressDialog(this);
        loading.setTitle("Signing up...");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_language:
                show_language_selecter();
                break;
            case R.id.btn_guider_signup:
                if(validate())
                {
                    sign_up();
                }
                break;
            case R.id.btn_guider_cancel:
                finish();
                break;
            case R.id.btn_switch_password_show:
                ShowHidePass();
                break;
        }
    }

    public void ShowHidePass() {

        if (hide){
            guider_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password_switcher.setImageResource(R.drawable.show_pass);
        }
        else{
            guider_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password_switcher.setImageResource(R.drawable.hide_pass);
        }
        hide = !hide;

    }
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean validate() {
        boolean valid = true;
        if(guider_firstname.getText().toString().length()==0)
        {
            guider_firstname.setError("Please input First Name");
            valid = false;
        }
        if(guider_lastname.getText().toString().length()==0)
        {
            guider_lastname.setError("Please input Last Name");
            valid = false;
        }
        if(guider_email.getText().toString().length() == 0 || !isValidEmail(guider_email.getText().toString()))
        {
            guider_email.setError("Please input correct Email Address");
            valid = false;
        }
        if(guider_phone.getText().toString().length()==0)
        {
            guider_phone.setError("Please input Phone Number");
            valid = false;
        }
        if (skill_language.size() == 0)
        {
            Toast.makeText(this,"Sorry. You should have at least two language skill to be Guider.",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(guider_bio.getText().toString().length()<20){
            Toast.makeText(this,"Please input at least 30 letters for bio", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
    private void sign_up() {
        loading.show();
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = guider_email.getText().toString();
        String password = guider_password.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.getInstance(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Global.my_email = user.getEmail();
                            Global.my_name = guider_firstname + " " + guider_lastname;
                            upload_data();

                        } else {
                            loading.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(GuiderRegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    private void upload_data(){
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put("guider_email", guider_email.getText().toString());
        user.put("guider_firstname", guider_firstname.getText().toString());
        user.put("guider_lastname", guider_lastname.getText().toString());
        user.put("guider_phone", guider_phone.getText().toString());
        user.put("guider_birthday", guider_birthday.getText().toString());
        user.put("guider_address", guider_address.getText().toString());
        user.put("guider_bio", guider_bio.getText().toString());
        user.put("guider_languages", skill_language);
        user.put("guider_photo", "");
        user.put("guider_status", "online");
        user.put("guider_available", false);
        user.put("guider_verified", false);
        user.put("guider_rating", "New");
        db.collection("guiders").document(guider_email.getText().toString())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(GuiderRegisterActivity.this, GuiderActivity.class);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "upload user data:success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Failed");
                    }
                });

    }

    private void show_language_selecter() {
        final Dialog dialog= new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.dialog_language_selector, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.setContentView(dialog_view);
        Rect displayRectangle = new Rect();
        Window full_window = this.getWindow();
        full_window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog_view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));

        chk_america = dialog_view.findViewById(R.id.chk_america);
        chk_germany = dialog_view.findViewById(R.id.chk_germany);
        chk_spain = dialog_view.findViewById(R.id.chk_spain);
        chk_arabia = dialog_view.findViewById(R.id.chk_arabia);
        chk_portugal = dialog_view.findViewById(R.id.chk_portugal);
        chk_japan = dialog_view.findViewById(R.id.chk_japan);
        chk_france = dialog_view.findViewById(R.id.chk_france);
        chk_america.setOnCheckedChangeListener(this);
        chk_spain.setOnCheckedChangeListener(this);
        chk_portugal.setOnCheckedChangeListener(this);
        chk_japan.setOnCheckedChangeListener(this);
        chk_germany.setOnCheckedChangeListener(this);
        chk_france.setOnCheckedChangeListener(this);
        chk_arabia.setOnCheckedChangeListener(this);

        Button ok = dialog_view.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_languages();
                dialog.dismiss();
            }
        });


        if(skill_language != null) {
            for (int i = 0; i < skill_language.size(); i++){
                if(skill_language.get(i).equals(LANGUAGE_En)){
                    chk_america.setChecked(true);
                }
                if(skill_language.get(i).equals(LANGUAGE_Ar)){
                    chk_arabia.setChecked(true);
                }
                if(skill_language.get(i).equals(LANGUAGE_Fr)){
                    chk_france.setChecked(true);
                }
                if(skill_language.get(i).equals(LANGUAGE_Ja)){
                    chk_japan.setChecked(true);
                }
                if(skill_language.get(i).equals(LANGUAGE_Sp)){
                    chk_spain.setChecked(true);
                }
                if(skill_language.get(i).equals(LANGUAGE_Po)){
                    chk_portugal.setChecked(true);
                }
                if(skill_language.get(i).equals(LANGUAGE_Ge)){
                    chk_germany.setChecked(true);
                }
            }
        }
        dialog.show();
    }

    private void save_languages() {
        skill_language.clear();
        if(chk_america.isChecked()){
            skill_language.add(LANGUAGE_En);
        }
        if(chk_arabia.isChecked()){
            skill_language.add(LANGUAGE_Ar);
        }
        if(chk_france.isChecked()){
            skill_language.add(LANGUAGE_Fr);
        }
        if (chk_germany.isChecked()){
            skill_language.add(LANGUAGE_Ge);
        }
        if (chk_japan.isChecked()){
            skill_language.add(LANGUAGE_Ja);
        }
        if (chk_portugal.isChecked()){
            skill_language.add(LANGUAGE_Po);
        }
        if(chk_spain.isChecked()){
            skill_language.add(LANGUAGE_Sp);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(chk_america.isChecked()){
            flag_america.setVisibility(View.VISIBLE);
        }
        else{
            flag_america.setVisibility(View.GONE);
        }
        if(chk_arabia.isChecked()){
            flag_saudi.setVisibility(View.VISIBLE);
        }
        else{
            flag_saudi.setVisibility(View.GONE);
        }
        if(chk_france.isChecked()){
            flag_france.setVisibility(View.VISIBLE);
        }
        else{
            flag_france.setVisibility(View.GONE);
        }
        if (chk_germany.isChecked()){
            flag_germany.setVisibility(View.VISIBLE);
        }
        else{
            flag_germany.setVisibility(View.GONE);
        }
        if (chk_japan.isChecked()){
            flag_japan.setVisibility(View.VISIBLE);
        }
        else
        {
            flag_japan.setVisibility(View.GONE);
        }
        if (chk_portugal.isChecked()){
            flag_brazil.setVisibility(View.VISIBLE);
        }
        else
        {
            flag_brazil.setVisibility(View.GONE);
        }
        if(chk_spain.isChecked()){
            flag_spain.setVisibility(View.VISIBLE);
        }
        else{
            flag_spain.setVisibility(View.GONE);
        }
    }
}
