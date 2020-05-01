package com.arabian.lancul.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.arabian.lancul.UI.Fragment.ProfileFragment;
import com.arabian.lancul.UI.Object.Guider;
import com.arabian.lancul.UI.Util.Global;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class EditProfileActivity extends AppCompatActivity {

    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView userPhoto;
    EditText user_name, user_password, confirm_password, user_email;
    Button btn_save;
    private String TAG = "Editprofile";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init_view();
        init_action();

    }

    private void init_action() {
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validation
                if(validataion()) {
                    if(!user_password.getText().toString().equals(""))
                    {
                        reset_password(user_password.getText().toString());
                    }
                    uploadImage();

                }
            }
        });
    }

    private void refresh_my_data() {
        if(Global.iamguider) {
            for (int i = 0; i < Global.array_guider.size(); i++) {
                if (Global.array_guider.get(i).getEmail().toString().equals(Global.my_email)) {
                    Global.my_guider_data = Global.array_guider.get(i);
                    break;
                }
            }
        }
        else{
            for(int i = 0; i < Global.array_client.size(); i++){
                if(Global.array_client.get(i).getEmail().equals(Global.my_email)){
                    Global.my_user_data = Global.array_client.get(i);
                    break;
                }
            }
        }
        if(Global.iamguider) {
            if(!Global.my_guider_data.getImageURL().equals(""))
                Glide.with(context).load(Global.my_guider_data.getImageURL()).into(ProfileFragment.my_photo);
        }
        else if(!Global.my_user_data.getPhoto().equals("")){
            Glide.with(context).load(Global.my_user_data.getPhoto()).into(ProfileFragment.my_photo);
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public boolean validataion(){
        boolean valid = true;
        if(user_name.getText().toString().length()==0)
        {
            user_name.setError("Please input Valid Name");
            valid = false;
        }
        if(user_email.getText().toString().length() == 0 || !isValidEmail(user_email.getText().toString()))
        {
            user_email.setError("Please input correct Email Address");
            valid = false;
        }
        if(user_password.getText().toString().length() < 6 && !user_password.getText().toString().equals(""))
        {
            user_password.setError("Please should be at least 6 letters");
            valid = false;
        }

        if(!user_password.getText().toString().equals(confirm_password.getText().toString())){
            confirm_password.setError("Password doesn't match");
            valid = false;
        }
        return valid;
    }

    private void init_view() {
        if(Global.iamguider){
            context = GuiderActivity.getInstance();
        }
        else{
            context = MainActivity.getInstance();
        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userPhoto = findViewById(R.id.user_photo);
        btn_save = findViewById(R.id.btn_save);
        user_name = findViewById(R.id.edt_username);
        user_password = findViewById(R.id.edt_password);
        user_email = findViewById(R.id.edt_email);
        confirm_password = findViewById(R.id.edt_confim_password);
        user_name.setText(Global.my_name);
        user_email.setText(Global.my_email);
        if(Global.iamguider) {
            if(!Global.my_guider_data.getImageURL().equals(""))
            Glide.with(EditProfileActivity.this).load(Global.my_guider_data.getImageURL()).into(userPhoto);
        }
        else if(!Global.my_user_data.getPhoto().equals("")){
            Glide.with(EditProfileActivity.this).load(Global.my_user_data.getPhoto()).into(userPhoto);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Global.go_profile = true;
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                userPhoto.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }




    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.progress_uploading));
            progressDialog.show();

            // Defining the child of storageReference
            final String path = "images/" + Global.my_email + "_profile_.jpg";
            final StorageReference ref = storageReference.child(path);
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                  @Override
                                                                                  public void onSuccess(Uri uri) {
                                                                                      Uri downloadUrl = uri;
                                                                                      Log.e("KKK", downloadUrl.toString());
                                                                                      Upload_data(downloadUrl.toString());
                                                                                  }
                                                                              });
                                    progressDialog.dismiss();
                                    Toasty.success(context,EditProfileActivity.this.
                                                    getString(R.string.toast_upload_image),
                                                    Toasty.LENGTH_SHORT)
                                            .show();


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toasty.error
                                    (context,EditProfileActivity.this.
                                            getString(R.string.toast_failed) + e.getMessage(),
                                            Toasty.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });




        }
    }

    private void Upload_data(String photo) {
        if (Global.iamguider){
            FirebaseApp.initializeApp(LoginActivity.getInstance());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference myRef = db.collection("guiders").document(Global.my_email);

            myRef
                    .update("guider_photo", photo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            Toasty.success(EditProfileActivity.this,"Success in update data",Toasty.LENGTH_LONG).show();
//                            LoginActivity.get_guider();
//                            LoginActivity.get_user();
                            refresh_my_data();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                            Toasty.error(EditProfileActivity.this,"Error update data",Toasty.LENGTH_LONG).show();
                        }
                    });

        }
        else{
            FirebaseApp.initializeApp(LoginActivity.getInstance());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference myRef = db.collection("users").document(Global.my_email);

            myRef
                    .update("user_photo", photo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            Toasty.success(EditProfileActivity.this,"Success in update data",Toasty.LENGTH_LONG).show();
//                            LoginActivity.get_guider();
//                            LoginActivity.get_user();
                            refresh_my_data();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                            Toasty.error(EditProfileActivity.this,"Error update data",Toasty.LENGTH_LONG).show();
                        }
                    });
        }

    }

    private void reset_password(String newPassword){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");

                        }
                    }
                });
    }

}
