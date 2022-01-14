package com.example.a2family.Activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.a2family.Classes.Family;
import com.example.a2family.Classes.User;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class AccountActivity extends BaseActivity {

    private TextView nameSurname;
    private TextView email;
    private TextView address;
    private TextView changePassword;
    private TextView numberComponents;
    private TextView available;
    private TextView showNumberComponents;
    private LinearLayout changeMaxNumberComponents;
    private int maxNumberComponents;
    private int actualNumberComponents;

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
        this.numberComponents=(TextView) findViewById(R.id.components);
        this.available = (TextView) findViewById(R.id.available);
        this.showNumberComponents=(TextView) findViewById(R.id.showNumberComponents);
        this.changeMaxNumberComponents=(LinearLayout) findViewById(R.id.change_numberComponents);


        initializeData();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
                //AccountActivity.this.signOut();
            }
        });

        showNumberComponents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNumber();
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

    private void changeNumber() {

        EditText components = new EditText(AccountActivity.this);
        components.setInputType(InputType.TYPE_CLASS_NUMBER);
        components.setHint("Nuovo Numero Membri");

        new AlertDialog.Builder(this)
                .setTitle("Aumenta Capienza Gruppo")
                .setMessage("Inserisci il nuovo numero di componenti per il gruppo")
                .setView(components)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String c = components.getText().toString().trim();
                        initializeData();
                        if(c.matches("-?(0|[1-9]\\d*)") && Integer.parseInt(c)>=actualNumberComponents) {
                            setNewNumber(Integer.parseInt(c));
                        }
                        else{
                            components.requestFocus();
                            components.setError("Inserisci un numero maggiore rispetto a quello dei membri attuali");
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void setNewNumber(int n) {
        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("maxNumberComponents").setValue(n).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AccountActivity.this, "Modifica completata", Toast.LENGTH_SHORT).show();
                    initializeData();
                }
            }
        });
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

        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Family f = task.getResult().getValue(Family.class);
                    maxNumberComponents = f.getMaxNumberComponents();
                    actualNumberComponents = f.getActualNumberComponents();
                    numberComponents.setText(String.valueOf(f.getActualNumberComponents()));
                    available.setText(String.valueOf(f.getMaxNumberComponents() - f.getActualNumberComponents()));
                    if(f.getCreatorID().equals(getUserIdFromFile())){
                        changeMaxNumberComponents.setVisibility(View.VISIBLE);
                    }
                }
            }
        });




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