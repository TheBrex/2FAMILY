package com.example.a2family.Activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.a2family.Classes.User;
import com.example.a2family.R;

public class AccountActivity extends BaseActivity {

    private TextView nameSurname;
    private TextView email;
    private TextView address;
    private TextView changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        bottoMenu();
        bottMenu();


        this.nameSurname= (TextView) findViewById(R.id.name_surname);
        this.email=(TextView) findViewById(R.id.profile_email);
        this.address=(TextView) findViewById(R.id.address);
        this.changePassword=(TextView) findViewById(R.id.change_password);

        initializeData();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
                //AccountActivity.this.signOut();
            }
        });

        /*
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAccount();
            }
        });
        */


    }

    private void changePassword() {
        //crea un nuovo dialog al click del cambio password
        new AlertDialog.Builder(this)
                .setTitle("Cambio Password")
                .setMessage("Sei sicuro di voler cambiare la password? Se confermi verrà inviata una mail e effettuato il logout")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //una volta cliccato su yes verrà inviata una mail all'utente attualmente loggato per il reset della password
                        mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail());
                        //logout
                        AccountActivity.this.signOut();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void initializeData() {
        //inizializza i campi nel layout della pagina del profilo utente
        String name = getUsernameFromFile();
        String surname = getSurnameFromFile();
        this.nameSurname.setText(name.substring(0,1).toUpperCase()+ name.substring(1).toLowerCase()+" "+surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase());
        this.email.setText(getEmailFromFile());
        this.address.setText(getAddressFromFile().toUpperCase());
    }




    /*
    private void deleteAccount() {

        new AlertDialog.Builder(this)
                .setTitle("Eliminazione Account")
                .setMessage("Sei sicuro di voler eliminare definivamente il tuo account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        ExitFragment bottomNavFragment = new ExitFragment("DeleteAccount",null);
                        //TODO: fixare -- getActivity() nel fragment ritorna null
                        bottomNavFragment.passActivity(AccountActivity.this);
                        Bundle b = new Bundle();
                        b.putString("userkey", getUserIdFromFile());
                        bottomNavFragment.setArguments(b);
                        bottomNavFragment.exitGroup();

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }



    public void removeUserFromDB(String userId) {
        System.out.println("ciao");
        firebaseDatabase.getReference().child("Users").child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
                    mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAuth.getCurrentUser().delete();
                            AccountActivity.this.signOut();
                        }
                    });

                }
            }
        });
    }


     */



}