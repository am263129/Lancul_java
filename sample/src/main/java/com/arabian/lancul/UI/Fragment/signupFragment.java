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


public class signupFragment extends Fragment {

    EditText edt_name, edt_email, edt_password, edt_confirm;
    ImageView btn_showpass;
    Button btn_signup;
    boolean hide = false;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_signup, container, false);

        init_view();
        init_actions();




        return view;
    }

    private void init_view() {
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        edt_confirm = view.findViewById(R.id.edt_confirm_pass);
        btn_signup = view.findViewById(R.id.btn_signup);
        btn_showpass = view.findViewById(R.id.btn_show_pass);
    }
    private void init_actions() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isValidEmail(edt_email.getText().toString()) || edt_password.getText().toString().equals("") || !edt_password.getText().toString().equals(edt_confirm.getText().toString())){
                    Toast.makeText(getContext(), R.string.Error_Login, Toast.LENGTH_LONG).show();
                }
                else {
                    signup();
                }
            }
        });
        btn_showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHidePass();
            }
        });
    }

    private void signup() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public void ShowHidePass() {

        if (hide){
            edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edt_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btn_showpass.setImageResource(R.drawable.show_pass);
        }
        else{
            edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edt_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btn_showpass.setImageResource(R.drawable.hide_pass);
        }
        hide = !hide;

    }


}
