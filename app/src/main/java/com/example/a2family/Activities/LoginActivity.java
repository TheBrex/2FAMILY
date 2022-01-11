package com.example.a2family.Activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a2family.Fragment.ExitFragment;
import com.example.a2family.Interfaces.HelperInterface;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity implements HelperInterface {

    private Button loginButton;
    private TextView editSignUp;
    private EditText editEmail;
    private EditText editPsw;
    private ProgressBar progressBar;
    private TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //se l'utente è loggato lo riporta nella pagina di creazione o unione ad un gruppo

        mAuth = FirebaseAuth.getInstance();
        //istanza del database
        firebaseDatabase=FirebaseDatabase.getInstance();
        
        //metodo che controlla se l'utente è gia' autenticato, se lo è
        //lancia la MainActvity
        onStart();
        setContentView(R.layout.activity_login);
        
        //setta i campi con gli oggetti di riferimento nel layout XML attraverso l'id
        loginButton= (Button) findViewById(R.id.login);
        editSignUp=(TextView) findViewById(R.id.singUP);
        editEmail = (EditText) findViewById(R.id.email);
        editPsw= (EditText) findViewById(R.id.password);
        progressBar=(ProgressBar) findViewById(R.id.loading);
        forgotPassword=(TextView) findViewById(R.id.forgotpassword);

        // listener on LOGIN button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //listener on REGISTRATI text
        editSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_signUp_page = new Intent(LoginActivity.this, SignUpActivity.class); //volontà di aprire la pagina di registrazione
                startActivity(open_signUp_page, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle()); //lancia l'activity

            }
        });
        
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        
    }

    private void resetPassword() {


        EditText email = new EditText(LoginActivity.this);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new AlertDialog.Builder(this)
                .setTitle("Password dimenticata?")
                .setMessage("Inserisci la mail per resettare la password del tuo account")
                .setView(email)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String txt= email.getText().toString().trim();
                        mAuth.sendPasswordResetEmail(txt).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "E' stata inviata un' email per il reset della password", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "C'è stato un problema nell'invio dell' email", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }})
                .setNegativeButton(android.R.string.no, null).show();

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
                        if(mAuth.getCurrentUser().isEmailVerified()) {
                            //salvo l'id dell'utente loggato nel file Settings accessibile esclusivamente dall'app
                            //in questo modo non c'è la necessità di passare per il database
                            getUser(mAuth.getCurrentUser().getUid());
                            //putUserIdIntoFile(mAuth.getCurrentUser().getUid());
                            //putEmailIntoFile(mAuth.getCurrentUser().getEmail());

                            //TODO: se l'utente è già in un gruppo famiglia, redirectarlo alla pagina del gruppo ( COMPLETED )
                            //TODO: fix Trackfamily "Family" value different from familyId when create familygroup ( COMPLETED )
                            //TODO: fix check if trackFamily exist ( COMPLETED )
                            String uID = mAuth.getCurrentUser().getUid();
                            firebaseDatabase.getReference("TrackFamily").child(uID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    //se la voce esiste
                                    if (task.isSuccessful()) {
                                        //se esiste un child con l'id dell'utente
                                        if (!(task.getResult().getValue() == null)) {
                                            //salvo l'id dell'utente loggato nel file Settings accessibile esclusivamente dall'app
                                            //in questo modo non c'è la necessità di passare per il database per recuperarlo
                                            //salvo la stringa corrispondente all'id della famiglia trovato nel db nel fire "Settings"
                                            String familyId = task.getResult().child("Family").getValue(String.class);
                                            putFamilyIdIntoFile(familyId);

                                            //reindirizzo nella pagina del gruppo
                                            Intent groupPage = new Intent(LoginActivity.this, GroupPageActivity.class);
                                            startActivity(groupPage, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());

                                        } else {
                                            //la voce TrackFamily non esiste ancora oppure non è stato trovato l'id dell'utente in "TrackFamily"
                                            //significa che l'utente non appartiene ancora ad un gruppo famiglia
                                            Intent mainPage = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(mainPage, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                                        }
                                        finish();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(LoginActivity.this, "Controlla la tua casella email", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                    else{
                        //se il login Fallisce riporta un messaggio d'errore
                        Toast.makeText(LoginActivity.this, "Login Fallito, Riprovare", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        String userId = getUserIdFromFile();
        String familyId = getFamilyIdFromFile();

        if (!(familyId.equals("defaultvalue")) && !(userId.equals("defaultvalue"))) {
            Intent groupPage = new Intent(LoginActivity.this, GroupPageActivity.class);
            //se l'utente fa gia parte di un gruppo passo il suo valore alla nuova activity
            groupPage.putExtra("familyId", familyId);
            startActivity(groupPage,ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
            finish();
        }
        else if(!userId.equals("defaultvalue") ) {
            Intent groupPage = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(groupPage, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
            finish();
        }

    }


}