package com.arabian.lancul.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;


public class signinFragment extends Fragment {

    private View view;
    Button signin;
    EditText edt_email, edt_password;
    boolean hide = false;
    ImageView eye;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_signin, container, false);
        init_view();
        init_actions();


        return view;
    }

    private void init_actions() {
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHidePass();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isValidEmail(edt_email.getText().toString()) || edt_password.getText().toString().equals("")){
                    Toast.makeText(getContext(), R.string.Error_Login, Toast.LENGTH_LONG).show();
                }
                else {
                    login();
                }

            }
        });
    }

    private void init_view() {
        signin = view.findViewById(R.id.btn_signin);
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        eye = view.findViewById(R.id.btn_show_pass);
    }


    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void login() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }


    public void ShowHidePass() {

        if (hide){
            edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eye.setImageResource(R.drawable.show_pass);
        }
        else{
            edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eye.setImageResource(R.drawable.hide_pass);
        }
        hide = !hide;

    }


}
