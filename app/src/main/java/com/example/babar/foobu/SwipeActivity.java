package com.example.babar.foobu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class SwipeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    //private int i;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        logout = findViewById(R.id.b_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getInstance().signOut();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
            }
        });

        auth = FirebaseAuth.getInstance();

        checkUserSex();

            al = new ArrayList<>();
            //al.add("php");
            //al.add("c");
            //al.add("python");
            //al.add("java");
            //al.add("html");
            //al.add("c++");
            //al.add("css");
            //al.add("javascript");

            arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );

            SwipeFlingAdapterView flingContainer =  findViewById(R.id.frame);

            flingContainer.setAdapter(arrayAdapter);
            flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {
                    // this is the simplest way to delete an object from the Adapter (/AdapterView)
                    Log.d("LIST", "removed object!");
                    al.remove(0);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLeftCardExit(Object dataObject) {
                    //Do something on the left!
                    //You also have access to the original object.
                    //If you want to use it just cast it (String) dataObject
                    Toast.makeText(SwipeActivity.this, "Left!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRightCardExit(Object dataObject) {
                    Toast.makeText(SwipeActivity.this, "Right!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdapterAboutToEmpty(int itemsInAdapter) {
                    // Ask for more data here
                    //al.add("XML ".concat(String.valueOf(i)));
                    //arrayAdapter.notifyDataSetChanged();
                    //Log.d("LIST", "notified");
                    //i++;
                }

                @Override
                public void onScroll(float scrollProgressPercent) {
                   }
            });


            // Optionally add an OnItemClickListener
            flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
                @Override
                public void onItemClicked(int itemPosition, Object dataObject) {
                    Toast.makeText(SwipeActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                }
            });

        }

        private String userSex;
        private String oppositeUserSex;

        public void checkUserSex(){
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference maleDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");
            maleDB.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getKey().equals(user.getUid())){
                        userSex = "Male";
                        oppositeUserSex = "Female";
                        getOppositeSexUsers();
                    }

                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference femaleDB = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
            femaleDB.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.getKey().equals(user.getUid())){
                        userSex = "Female";
                        oppositeUserSex = "Male";
                        getOppositeSexUsers();
                    }

                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void getOppositeSexUsers(){
            DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserSex);
            oppositeSexDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.exists()){
                        al.add(dataSnapshot.child("Birthdate").getValue().toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

}
