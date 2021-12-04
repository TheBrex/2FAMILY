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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements FieldChecker {

    private Button loginButton;
    private TextView editSignUp;
    private EditText editEmail;
    private EditText editPsw;
    private ProgressBar progressBar;

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
        setContentView(R.layout.activity_login);
        
        mAuth = FirebaseAuth.getInstance();
        //istanza del database
        firebaseDatabase=FirebaseDatabase.getInstance();
        
        //metodo che controlla se l'utente è gia' autenticato, se lo è
        //lancia la MainActvity
        onStart();
        
        
        //setta i campi con gli oggetti di riferimento nel layout XML attraverso l'id
        loginButton= (Button) findViewById(R.id.login);
        editSignUp=(TextView) findViewById(R.id.singUP);
        editEmail = (EditText) findViewById(R.id.email);
        editPsw= (EditText) findViewById(R.id.password);
        progressBar=(ProgressBar) findViewById(R.id.loading);

        


        // listener on LOGIN button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                login();
            }
        });

        //listener on REGISTRATI text
        editSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_signUp_page = new Intent(LoginActivity.this, SignUpActivity.class); //volontà di aprire la pagina di registrazione
                startActivity(open_signUp_page); //lancia l'activity


            }
        });
    }


    private void login() {
        //inizializza le variabili con le stringhe nei campi del form di login
        String email= this.editEmail.getText().toString().trim();
        String password=this.editPsw.getText().toString().trim();

        //inizializza le istanze del database
        this.firebaseDatabase=FirebaseDatabase.getInstance();
        this.mAuth=FirebaseAuth.getInstance();

        //controlla i formati di email e password
        int emailError=checkEmail(email, editEmail );
        int error=checkField(password, editPsw);

        if(error==1 && emailError==1){

            progressBar.setVisibility(View.VISIBLE);
            //effettua il login con le credenziali inserite
            //listener che definisce l'esito del login
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //controlla se il task va a buon fine
                    if(task.isSuccessful()){
                        //se il Login funziona vai alla MainActivity
                        Intent mainPage = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainPage);
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        //se il login Fallisce riporta un messaggio d'errore
                        Toast.makeText(LoginActivity.this, "Login Fallito, Riprovare", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!= null){
            Intent mainPage = new Intent(LoginActivity.this, MainActivity.class); //volontà di aprire la pagina di registrazione
            startActivity(mainPage); //lancia l'activity
        }
    }

}