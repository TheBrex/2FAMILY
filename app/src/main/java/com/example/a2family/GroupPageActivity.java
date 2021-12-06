package com.example.a2family;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class GroupPageActivity extends AppCompatActivity {

    private ListView member;
    private TextView logOut;

    // creating a variable for our
    // Firebase Database.
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // creating a variable for our Database
    // Reference for Firebase.
    private DatabaseReference databaseReference=firebaseDatabase.getReference().getRoot();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        this.logOut=(TextView) findViewById(R.id.logout_group);
        this.member =(ListView) findViewById(R.id.member_list);
        this.progressBar=(ProgressBar) findViewById(R.id.loading_logoutgroup);

        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String userId = preferences.getString("userId", "defaultvalue");
        String familyId = preferences.getString("familyId", "defaultvalue");
        System.out.println(familyId);
        //recupero l'id della famiglia
        //String familyId = getIntent().getStringExtra("familyId");

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void signOut() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signOut();
        progressBar.setVisibility(View.GONE);
        //vado alla pagina di login
        Intent loginPage=new Intent(GroupPageActivity.this, LoginActivity.class);
        startActivity(loginPage);
        finish();
    }
}