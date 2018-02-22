package com.example.babar.foobu;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button reg_button;
    private EditText reg_email, reg_pass, reg_firstname, reg_midname, reg_lastname, reg_pass_confirm;
    public static EditText reg_birthdate;
    private RadioGroup gender;
    public static String hold_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**************INITIALIZATIONS****************/
        reg_button = findViewById(R.id.reg_button);
        reg_email = findViewById(R.id.reg_email);
        reg_pass = findViewById(R.id.reg_pass);
        reg_pass_confirm = findViewById(R.id.reg_repeat_pass);
        reg_firstname = findViewById(R.id.reg_firstname);
        reg_midname = findViewById(R.id.reg_midname);
        reg_lastname = findViewById(R.id.reg_lastname);
        reg_birthdate = findViewById(R.id.reg_birthdate);
        gender = findViewById(R.id.gender);



        auth = FirebaseAuth.getInstance();
        final ProgressDialog dialog = new ProgressDialog(RegistrationActivity.this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){     //means user is logged in therefore go to next activity
                    Intent intent = new Intent(RegistrationActivity.this, SwipeActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_hold = reg_email.getText().toString();
                final String pass_hold = reg_pass.getText().toString();

                int genderid = gender.getCheckedRadioButtonId();
                final RadioButton gender = findViewById(genderid);
                if(gender.getText() == null){
                    return;
                }

                dialog.setMessage("Registering details...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(false);
                dialog.show();

                auth.createUserWithEmailAndPassword(email_hold, pass_hold)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!reg_pass_confirm.getText().equals(reg_pass.getText()) && !task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                String userId = auth.getCurrentUser().getUid();
                                DatabaseReference dbase = FirebaseDatabase.getInstance().getReference("Users/" +gender.getText().toString() + "/"+userId);        //below is error; registering account but not names on dbase
                                dbase.child("Firstname").setValue(reg_firstname.getText().toString());
                                dbase.child("Midname").setValue(reg_midname.getText().toString());
                                dbase.child("Lastname").setValue(reg_lastname.getText().toString());
                                dbase.child("Birthdate").setValue(hold_date);
                            } catch(Exception e){
                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                                Log.d("registration_error", e.toString());
                            }
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    public void birthdate(View v){              //executed when user clicks bdate edittext
        DialogFragment newFragment = new DatePicker();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:         //when user pressed back, possible to execute func here
//                Toast.makeText(getApplicationContext(), "TEST BACK", Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
