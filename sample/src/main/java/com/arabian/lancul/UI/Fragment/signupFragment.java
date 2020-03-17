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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class signupFragment extends Fragment {

    EditText edt_name, edt_email, edt_password, edt_confirm;
    ImageView btn_showpass;
    Button btn_signup;
    boolean hide = false;
    private View view;
    String TAG = "sign up";
    ProgressDialog loading;
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
        loading = new ProgressDialog(LoginActivity.getInstance());
        loading.setTitle("Signing up...");
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
        loading.show();
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.getInstance(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Global.current_user_email = user.getEmail();
                            upload_data();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                            LoginActivity.getInstance().finish();
                        } else {
                            loading.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.getInstance(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void upload_data() {
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put("user_email", edt_email.getText().toString());
        user.put("user_name", edt_name.getText().toString());
        user.put("user_photo", "");
        user.put("user_status", "online");
        user.put("user_type", "client");
        db.collection("users").document(edt_email.getText().toString())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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
            edt_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btn_showpass.setImageResource(R.drawable.hide_pass);
        }
        hide = !hide;

    }


}
