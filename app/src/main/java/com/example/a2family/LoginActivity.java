package com.example.a2family;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextView signUp;
    private EditText username;
    private EditText psw;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.loginButton= (Button) findViewById(R.id.login);
        signUp=(TextView) findViewById(R.id.singUP);
        username = (EditText) findViewById(R.id.username);
        psw= (EditText) findViewById(R.id.password);
        progressBar=(ProgressBar) findViewById(R.id.loading);

        mAuth = FirebaseAuth.getInstance();


        // listener on LOGIN button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        //listener on REGISTRATI text
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_signUp_page = new Intent(LoginActivity.this, SignUpActivity.class); //volontà di aprire la pagina di registrazione
                startActivity(open_signUp_page); //lancia l'activity
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!= null){
            //utente già loggato
        }
    }
}