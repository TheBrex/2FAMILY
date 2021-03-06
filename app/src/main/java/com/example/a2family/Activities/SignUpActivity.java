package com.example.a2family.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a2family.Classes.User;
import com.example.a2family.Interfaces.HelperInterface;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class SignUpActivity extends BaseActivity  {


    private EditText editName, editSurname , editAddress, editPassword, editPasswordConfirm, editEmail;
    private TextView backButton;
    private Button signUpButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);


        //inizializza campi con gli specifici elementi grafici definiti attraverso l'id nel file xml
        //del layout del'activity corrente
        this.editName= (EditText) findViewById(R.id.name_signUp);
        this.editSurname= (EditText) findViewById(R.id.surname);
        this.editAddress= (EditText) findViewById(R.id.address);
        this.editPassword= (EditText) findViewById(R.id.password_signUp);
        this.editPasswordConfirm= (EditText) findViewById(R.id.password_signUpConfirm);
        this.editEmail= (EditText) findViewById(R.id.email);
        this.backButton = (TextView) findViewById(R.id.backLogin);
        this.signUpButton = (Button) findViewById(R.id.signup);
        this.progressBar=(ProgressBar) findViewById(R.id.loading);


        //trigger event when back is tapped
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //trigger event when registration button is tapped
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });

    }

    private void signUpUser() {

        //trim() prende il valore nel campo del form e rimuove gli spazi nella stringa
        String name= editName.getText().toString().trim();
        String surname = editSurname.getText().toString().trim();
        String password= editPassword.getText().toString().trim();
        String passwordConfirm=editPasswordConfirm.getText().toString().trim();
        String address=editAddress.getText().toString();//la stringa indirizzo contiene eventualmente spazi
        String email= editEmail.getText().toString().trim();

        //controlla che i campi siano correttamente inseriti, in caso contrario ritorna errore
        int error;
        error=checkField(name, editName);
        error=checkField(surname, editSurname);
        error=checkField(address, editAddress);
        int emailError=checkEmail(email, editEmail);
        int pswError=checkPassword(password,passwordConfirm, editPassword, editPasswordConfirm);

        //controlla che email o password non abbiano generato errori specifici per poi procedere alla registrazione
        if(emailError == 1 && pswError==1) {
            progressBar.setVisibility(View.VISIBLE);

            //crea l'utente per l'autenticazione con email e password
            //Il listener onComplete ?? utlizzato per verificare l'esito dell'operazione e agire di conseguenza
            //Se l'operazione va a buon fine allora possiamo procedere ad inserire i dati utente nel realTime database
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //cotrollo che l'operazione abbia avuto esito positivo
                    if (task.isSuccessful()) {
                        //invio la mail per l'attivazione dell'account
                        sendEmail();
                        //creo un oggetto User con i dati inseriti in fase di registrazione
                        User user = new User(name, surname, address, email);

                        // riferimento a "Users" nel realTime database
                        databaseReference = firebaseDatabase.getReference("Users");
                        //estrapolo l'id univoco di autenticazione dell'utente e lo uso come "chiave" per effettuare l'inserimento in "Users"
                        String userKey= mAuth.getCurrentUser().getUid();

                        //inserisce nela database una nuova voce in "Users" con l'id Univoco usato per l'autenticazione, e tutti i suoi campi extra all'interno
                        //il listener onComplete ?? utilizzato per verificare l'esito dell'operazione e agire di conseguenza
                        databaseReference.child(userKey).setValue(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //se l'operazione va a buon fine
                                if(task.isSuccessful()){
                                    //mostra messaggio di avvenuta registrazione
                                    Toast.makeText(SignUpActivity.this, "La registrazione ?? andata a buon fine", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    //mostra messaggio di registrazione fallita
                                    Toast.makeText(SignUpActivity.this, "La registrazione NON ?? andata a buon fine", Toast.LENGTH_LONG).show();
                                }
                                //la progress bar(l'icona di caricamento) viene rimosso
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                        //se la registrazione va buon fine ritorna nella pagina di login con finish() termina l'activity corrente
                        finish();

                    } else {
                        //se la registrazione per l'autenticazione non va a buon fine mostra messaggio di errore
                        Toast.makeText(SignUpActivity.this, "La registrazione NON ?? andata a buon fine", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }


    private void sendEmail(){
        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "E' stata inviata una mail per confermare la registrazione ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "C'?? stato un problema nell'invio dell' email di conferma registrazione",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



}