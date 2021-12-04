package com.example.a2family;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText editFamilyCode;
    private EditText editFamilyNumber;
    private Button joinButton;
    private Button createButton;
    private TextView logout;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setta i campi con gli oggetti di riferimento nel layout XML attraverso l'id
        this.editFamilyCode=(EditText) findViewById(R.id.family_code);
        this.editFamilyNumber=(EditText) findViewById(R.id.max_component);
        this.joinButton=(Button) findViewById(R.id.join_group);
        this.createButton=(Button) findViewById(R.id.create_group);
        this.logout=(TextView) findViewById(R.id.logout);
        this.progressBar=(ProgressBar) findViewById(R.id.loading_logout);


        //listener sul bottone "unisciti"
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //listener sul bottone "creagruppo"
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //listener sulla textview Logout
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signOut();
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }
}

