package com.arabian.lancul.UI.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Fragment.signinFragment;
import com.arabian.lancul.UI.Fragment.signupFragment;
import com.arabian.lancul.UI.Object.Client;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Object.Res_Exp;
import com.arabian.lancul.UI.Object.Feedback;
import com.arabian.lancul.UI.Util.Global;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.arabian.lancul.UI.Util.Global.array_chat_ids;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,  GoogleApiClient.OnConnectionFailedListener {
    TextSwitcher switcher;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private static String TAG = "Login Activity";
    public static LoginActivity self;
    public ImageView btn_guider_mode, btn_signin_google;
    private LinearLayout main_window,user_window, guider_window;
    private TextView label_mode,btn_forgot_password_guider;
    private Button btn_login;
    private TextView btn_register;
    private ProgressDialog loading;
    private EditText guider_email, guider_password;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private Integer RC_SIGN_IN = 9002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        self = this;
        init_view();
        init_action();
        init_data();

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume(){
        super.onResume();
        get_chat();
        get_guider();
        get_user();
        check_mode();
        get_feedback();
    }



    private void init_view() {
        FirebaseApp.initializeApp(this);
        viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setOffscreenPageLimit(100);
        main_window = findViewById(R.id.main_back);
        user_window = findViewById(R.id.user_window);
        guider_window = findViewById(R.id.guider_window);
        label_mode = findViewById(R.id.label_mode);
        btn_login = findViewById(R.id.btn_sign_in_guider);
        btn_register = findViewById(R.id.btn_sign_up_guider);
        guider_email = findViewById(R.id.guider_email);
        guider_password = findViewById(R.id.guider_password);
        btn_signin_google = findViewById(R.id.btn_signin_google);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new signinFragment());
        adapter.addFragment(new signupFragment());
        btn_guider_mode = findViewById(R.id.btn_im_guider);
        btn_forgot_password_guider = findViewById(R.id.btn_forgot_password_guider);

        loading = new ProgressDialog(LoginActivity.getInstance());
        loading.setTitle(LoginActivity.this.getString(R.string.progress_sign_in));

        GoogleSignIn();
    }
    private void init_action() {
        btn_guider_mode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        final BubbleNavigationConstraintView bubbleNavigationLinearView = findViewById(R.id.top_navigation_constraint);
        bubbleNavigationLinearView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationLinearView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position, true);
            }
        });

        btn_signin_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_with_google();
            }
        });
        btn_forgot_password_guider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isValidEmail(guider_email.getText().toString()) || guider_email.getText().toString().equals("")){
                    Toasty.error(LoginActivity.this, R.string.Error_Login, Toasty.LENGTH_LONG).show();
                }
                else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(guider_email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toasty.success(LoginActivity.this, getString(R.string.toast_send_email), Toasty.LENGTH_LONG).show();
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                }
            }
        });

    }
    private void init_data() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("experiences")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Global.array_experience.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.get("name").toString();
                                String location = document.get("location").toString();
                                String photo = document.get("imageUrl").toString();
                                Float rating = Float.parseFloat(document.get("rating").toString());

                                Res_Exp experience = new Res_Exp(name,location,photo,rating);
                                Global.array_experience.add(experience);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //get Restaurant data
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Global.array_restaurant.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.get("name").toString();
                                String location = document.get("location").toString();
                                String photo = document.get("imageUrl").toString();
                                Float rating = Float.parseFloat(document.get("rating").toString());

                                Res_Exp restaurant = new Res_Exp(name,location,photo,rating);
                                Global.array_restaurant.add(restaurant);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    public static void get_guider(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("guiders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Global.array_guider.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                try {
                                    String bio = document.get("guider_bio").toString();
                                    String imageUrl = document.get("guider_photo").toString();
                                    String phone = document.get("guider_phone").toString();
                                    String email = document.get("guider_email").toString();
                                    boolean is_available = Boolean.parseBoolean(document.get("guider_available").toString());
                                    String name = document.get("guider_firstname").toString() + " " + document.get("guider_lastname").toString();
                                    Float rating = 0f;
                                    boolean new_guider = false;
                                    if (document.get("guider_rating").toString().equals("New")) {
                                        new_guider = true;
                                    } else {
                                        rating = Float.parseFloat(document.get("guider_rating").toString());
                                    }
                                    boolean verified = Boolean.parseBoolean(document.get("guider_verified").toString());

                                    List<String> languages = (List<String>) document.get("guider_languages");

                                    String address = "";
                                    String birthday = "";
                                    try {
                                        address = document.get("guider_address").toString();
                                        birthday = document.get("guider_birthday").toString();
                                    }
                                    catch (Exception E){
                                        Log.e(TAG,"No address and birthday");
                                    }
                                    String status = document.get("guider_status").toString();

                                    Guider guider = new Guider(bio, imageUrl, name, rating, is_available, verified, languages, phone, email, address, birthday, status, new_guider);
                                    Global.array_guider.add(guider);
                                }
                                catch (Exception E){
                                    Log.e(TAG, E.toString());
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static void get_user(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Global.array_client.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.get("user_name").toString();
                                String email = document.get("user_email").toString();
                                String pole = document.get("user_type").toString();
                                String status = document.get("user_status").toString();
                                String photo = document.get("user_photo").toString();
                                List<String> linked_guiders = (List<String>) document.get("user_linked_guiders");
                                if(pole.equals("client")){
                                    Client client = new Client(name, email, status, photo, linked_guiders);
                                    Global.array_client.add(client);
                                }



                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static void get_feedback() {
        for (int  i = 0; i < Global.array_guider.size(); i ++){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final int finalI = i;
            db.collection("guiders").document(Global.array_guider.get(i).getEmail()).collection("feedback")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                String temp = Global.array_guider.get(finalI).getEmail();
                                ArrayList<Feedback> feedbacks = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    try {
                                        Feedback feedback = document.toObject(Feedback.class);
                                        feedbacks.add(feedback);
                                    }
                                    catch (Exception E){
                                        Log.e(TAG, E.toString());
                                    }
                                }

                                Global.array_guider.get(finalI).setFeedbacks(feedbacks);
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }
    public static void get_chat(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            array_chat_ids.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                array_chat_ids.add(document.getId());
                            }
//                            for (int i = 0; i< array_chat_ids.size(); i++){
//                                if(array_chat_ids.get(i).toLowerCase().contains(Global.my_name)){
//
//                                }
//                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void login() {

        loading.show();
        String username = guider_email.getText().toString();
        String password = guider_password.getText().toString();
        FirebaseApp.initializeApp(LoginActivity.getInstance());
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(LoginActivity.getInstance(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent;
                            loading.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Global.my_email = user.getEmail();

                            for (int i = 0; i < Global.array_guider.size(); i ++){
                                if (Global.array_guider.get(i).getEmail().toString().equals(Global.my_email)){
                                    Global.iamguider = true;
                                    Global.my_name = Global.array_guider.get(i).getName();
                                    Global.my_email = Global.array_guider.get(i).getEmail();
                                    Global.my_guider_data = Global.array_guider.get(i);
                                }
                            }
                            if (!Global.iamguider){
                                Toasty.info(LoginActivity.this,LoginActivity.this.getString(R.string.toast_enjoy_user),Toasty.LENGTH_SHORT).show();
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                intent = new Intent(LoginActivity.this, GuiderActivity.class);
                                startActivity(intent);
                                LoginActivity.getInstance().finish();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            loading.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toasty.error(LoginActivity.getInstance(), LoginActivity.this.getString(R.string.toast_authentication_failed),
                                    Toasty.LENGTH_SHORT).show();
                        }

                    }
                });



    }

    private void login_with_google(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void GoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("AIzaSyBi4E-nXQAwWh8ewnuV8vIRvBjOi06vlg8")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_in_guider:
                if(!isValidEmail(guider_email.getText().toString()) || guider_password.getText().toString().equals("")){
                    Toasty.error(LoginActivity.this, R.string.Error_Login, Toasty.LENGTH_LONG).show();
                }
                else
                    login();
                break;
            case R.id.btn_sign_up_guider:

                Intent intent = new Intent(this,GuiderRegisterActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_im_guider:
                Global.user_mode = !Global.user_mode;
                check_mode();


                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void check_mode() {

        if(!Global.user_mode){
            main_window.setBackgroundResource(R.drawable.gradient_background_guider);
            guider_window.setVisibility(View.VISIBLE);
            user_window.setVisibility(View.GONE);
            btn_guider_mode.setBackgroundResource(R.drawable.ico_usermode);
            label_mode.setText(getString(R.string.to_usermode));
            label_mode.setTextColor(ContextCompat.getColor(this,R.color.orange));


        }
        else{
            main_window.setBackgroundResource(R.drawable.gradient_background);
            guider_window.setVisibility(View.GONE);
            user_window.setVisibility(View.VISIBLE);
            btn_guider_mode.setBackgroundResource(R.drawable.ico_guider);
            label_mode.setText(getString(R.string.to_guidemode));
            label_mode.setTextColor(ContextCompat.getColor(this,R.color.guider_orange));
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    public static LoginActivity getInstance(){
        return self;
    }




    private void getResultGoogle(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            String photo = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg" ;
            if (acct.getPhotoUrl()!=null){
                photo =  acct.getPhotoUrl().toString();
            }

            signupFragment signupFragment = new signupFragment();
            signupFragment.signup(acct.getId().toString(),acct.getId());
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
        else {

        }
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getResultGoogle(result);
        }
    }
}
