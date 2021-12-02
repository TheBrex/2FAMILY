package com.example.a2family;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    //firebase inititialization
    private FirebaseAuth mAuth;
    private EditText editName, editSurname , editAddress, editPassword, editPasswordConfirm, editEmail;
    private  TextView backButton, signUpButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //setta i parametri della window in full screen

        setContentView(R.layout.activity_sign_up);

        //firebase inititialization
        mAuth = FirebaseAuth.getInstance();



        //initialize variables registration form
        this.editName= (EditText) findViewById(R.id.name_signUp);
        this.editSurname= (EditText) findViewById(R.id.surname);
        this.editAddress= (EditText) findViewById(R.id.address);
        this.editPassword= (EditText) findViewById(R.id.password_signUp);
        this.editPasswordConfirm= (EditText) findViewById(R.id.password_signUpConfirm);
        this.editEmail= (EditText) findViewById(R.id.email);
        this.backButton = (TextView) findViewById(R.id.backLogin);
        this.signUpButton = (TextView) findViewById(R.id.signup);
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

        //trim() get name inside form and remove all spaces inside
        String name= editName.getText().toString().trim();
        String surname = editSurname.getText().toString().trim();
        String address=editAddress.getText().toString().trim();
        String password= editPassword.getText().toString().trim();
        String passwordConfirm=editPasswordConfirm.getText().toString().trim();
        String email= editEmail.getText().toString().trim();

        //check field validity
        int error;
        error=checkField(name, editName);
        error=checkField(surname, editSurname);
        error=checkField(address, editAddress);
        int emailError=checkEmail(email, editEmail);
        int pswError=checkPassword(password,passwordConfirm, editPassword, editPasswordConfirm);

        //controlla che email o password non abbiano generato errori per poi procedere alla registrazione
        if(emailError == 1 && pswError==1) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        User user = new User(name, surname, address, email);
                        Toast.makeText(SignUpActivity.this, "La registrazione è andata a buon fine", Toast.LENGTH_LONG).show();
                        //se la registrazione va buon fine ritorna nella pagina di login
                        finish();

                    } else {
                        Toast.makeText(SignUpActivity.this, "La registrazione NON è andata a buon fine", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    //method for checking emptyness of a field
    private int checkField(String field, EditText xField){

        if(field.isEmpty()){
            xField.setError("Il campo è Richiesto ! ");
            xField.requestFocus();
            return 0;
        }
        else{
            return 1;
        }

    }

    //method for checking email format
    private int checkEmail(String field, EditText xField){

        checkField(field, xField);

        if(! Patterns.EMAIL_ADDRESS.matcher(field).matches()){
            xField.setError("Formato email non corretto ! ");
            xField.requestFocus();
            return 0;
        }
        else{
            return 1;
        }
    }

    //method for checking password format
    private  int checkPassword(String field, String field1, EditText xField, EditText yField){

        //controlla che i campi non siano vuoti
        checkField(field, xField);
        checkField(field1, yField);

        //controlla che la lunghezza della password sia di almeno 6 caratter
        if(field.length() < 6 ){
            xField.setError("Lunghezza minima 6 caratteri ! ");
            xField.requestFocus();
            return 0;
        }
        //controlla che la stringa in conferma password sia uguale a quella su password
        if(!field1.equals(field)){
            yField.setError("Le password non sono uguali !");
            yField.requestFocus();
            return 0;
        }
        else{
            return 1;
        }
    }


}