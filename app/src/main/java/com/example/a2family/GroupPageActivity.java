package com.example.a2family;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GroupPageActivity extends AppCompatActivity {

    protected ListView member;

    // creating a variable for our
    // Firebase Database.
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    // creating a variable for our Database
    // Reference for Firebase.
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        this.member =(ListView) findViewById(R.id.member_list);
        //recupero l'id della famiglia
        String familyId = getIntent().getStringExtra("familyId");


    }
}