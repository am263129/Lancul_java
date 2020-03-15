package com.arabian.lancul.UI.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Activity.LoginActivity;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class signinFragment extends Fragment {

    private View view;
    Button signin;
    EditText edt_email, edt_password;
    boolean hide = false;
    ImageView eye;
    String TAG = "Sign in";
    ProgressDialog loading;
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
        loading = new ProgressDialog(LoginActivity.getInstance());
        loading.setTitle("Connecting to server...");

    }


    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void login() {

            loading.show();
            String username = edt_email.getText().toString();
            String password = edt_password.getText().toString();
            FirebaseApp.initializeApp(LoginActivity.getInstance());
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(LoginActivity.getInstance(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loading.dismiss();
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Global.current_user_email = user.getEmail();
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                                LoginActivity.getInstance().finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                loading.dismiss();
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.getInstance(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



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
