package com.example.babar.foobu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private Button cancel, update;
    private EditText fname, mname, lname, phone;
    private ImageView image;
    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userid, str_fname, str_mname, str_lname, str_phone, str_image, userSex;
    private Uri resultUri;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**INITIALIZATIONS**/
        userSex = getIntent().getExtras().getString("userSex");
        fname = findViewById(R.id.set_fname);
        mname = findViewById(R.id.set_mname);
        lname = findViewById(R.id.set_lname);
        phone = findViewById(R.id.set_phone);
        cancel = findViewById(R.id.set_cancel);
        update = findViewById(R.id.set_update);
        image = findViewById(R.id.set_image);
        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(userid);

        getUserInfo();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(Settings.this);
                dialog.setMessage("Updating...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(false);
                dialog.show();
                saveUserInformation();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

    }

    private void getUserInfo() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Firstname") != null){
                        str_fname = map.get("Firstname").toString();
                        fname.setText(str_fname);
                    }
                    if(map.get("Midname") != null){
                        str_mname = map.get("Midname").toString();
                        mname.setText(str_mname);
                    }
                    if(map.get("Lastname") != null){
                        str_lname = map.get("Lastname").toString();
                        lname.setText(str_lname);
                    }
                    if(map.get("Phone") != null){
                        str_phone = map.get("Phone").toString();
                        phone.setText(str_phone);
                    }
                    if(map.get("profileImageUrl") != null){
                        str_image = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(str_image).into(image);
//                        Picasso.with(getApplicationContext())
//                        .load(str_image)
//                        .into(image);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation() {
        str_fname = fname.getText().toString();
        str_mname = mname.getText().toString();
        str_lname = lname.getText().toString();
        str_phone = phone.getText().toString();

        Map userInfo = new HashMap<>();
        userInfo.put("Firstname", str_fname);
        userInfo.put("Midname", str_mname);
        userInfo.put("Lastname", str_lname);
        userInfo.put("Phone", str_phone);
        databaseReference.updateChildren(userInfo);

        //GET IMAGE AND CONVERT SOME SHITS
        if(resultUri != null){
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userid);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Map userInfo = new HashMap<>();
                    userInfo.put("profileImageUrl", downloadUrl.toString());
                    databaseReference.updateChildren(userInfo);
                    finish();
                    return;
                }
            });
        } else {
            dialog.dismiss();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageuri = data.getData();
            resultUri = imageuri;
            image.setImageURI(resultUri);

        }
    }
}
