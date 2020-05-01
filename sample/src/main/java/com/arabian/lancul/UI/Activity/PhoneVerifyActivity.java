package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Util.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import es.dmoral.toasty.Toasty;

public class PhoneVerifyActivity extends AppCompatActivity {

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public String TAG = "Phone_Verify";
    public String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseAuth mAuth;
    private String selected_country_code;
    CountryCodePicker ccp;

    EditText phone_number;
    Button btn_send;
    String phoneNumber;
    ProgressDialog signning;
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        phone_number = findViewById(R.id.edt_number);
        btn_send = findViewById(R.id.btn_send);
        signning = new ProgressDialog(PhoneVerifyActivity.this);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPhoneNumberValid(phone_number.getText().toString(),ccp.getSelectedCountryNameCode())){
                    phoneNumber =ccp.getSelectedCountryCodeWithPlus() +  phone_number.getText().toString();
//                    phoneNumber = "+40273647894";
                    send_sms();
                    signning.setTitle("Sending...");
                    signning.show();
                }
                else{
                    Toasty.error(PhoneVerifyActivity.this,"Please Input Valid Phone Number",Toasty.LENGTH_LONG).show();
                }
            }
        });



        ccp = findViewById(R.id.ccp);


        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                signning.dismiss();
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                signning.dismiss();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                signning.dismiss();
                mVerificationId = verificationId;
                mResendToken = token;
                show_input_code_dialog();
            }
        };



    }

    private void show_input_code_dialog() {
        final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialog_view = inflater.inflate(R.layout.dialog_input_verifycode, null);
        dialog.setContentView(dialog_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Rect displayRectangle = new Rect();
        Window full_window = this.getWindow();
        full_window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog_view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        final EditText verification_code = dialog_view.findViewById(R.id.edt_verification_code);
        final EditText my_name = dialog_view.findViewById(R.id.edt_name);
        Button verify = dialog_view.findViewById(R.id.btn_verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verification_code.getText().toString());
                signInWithPhoneAuthCredential(credential);
                name = my_name.getText().toString();
                signning.setTitle("Signing In..");
                signning.show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean isPhoneNumberValid(String phoneNumber, String countryCode)
    {
        //NOTE: This should probably be a member variable.
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try
        {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, countryCode);
            return phoneUtil.isValidNumber(numberProto);
        }
        catch (NumberParseException e)
        {
            Log.e("NumberParseException: ",e.toString());
        }

        return false;
    }

    private void send_sms() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            signning.dismiss();
                            Toasty.success(PhoneVerifyActivity.this,"Success in Verification",Toasty.LENGTH_LONG).show();
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(PhoneVerifyActivity.this, MainActivity.class);
                            Global.my_email = phoneNumber;
                            upload_data();
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            signning.dismiss();
                            Toasty.error(PhoneVerifyActivity.this,"Verification Failure, invalid code",Toasty.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void upload_data() {
        Client client = new Client(name,phoneNumber,"","",null, new ArrayList<Double>());
        Global.my_user_data = client;
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put("user_email", phoneNumber);
        user.put("user_name", name);
        user.put("user_photo", "");
        user.put("user_status", "online");
        user.put("user_type", "client");
        ArrayList<String> linked_gudider = new ArrayList<>();
        user.put("user_linked_guiders", linked_gudider);
        db.collection("users").document(phoneNumber)
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
    public void onCountryPickerClick(View view) {
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                selected_country_code = ccp.getSelectedCountryCodeWithPlus();
            }
        });
    }

}
