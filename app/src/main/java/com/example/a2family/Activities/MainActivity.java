package com.example.a2family.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a2family.Classes.Family;
import com.example.a2family.Classes.User;
import com.example.a2family.Interfaces.HelperInterface;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends BaseActivity implements HelperInterface {

    private EditText editFamilyCode;
    private EditText editFamilyNumber;
    private Button joinButton;
    private Button createButton;
    private TextView logout;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //richiama la funzione redirect che verifica se l'utente fa già parte di un
        //gruppo famiglia, se lo è lo riporta nella pagina del gruppo
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
                joinGroup();

            }
        });

        //listener sul bottone "creagruppo"
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });

        //listener sulla textview Logout
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }


    private void joinGroup() {

        String familycode = editFamilyCode.getText().toString().trim();
        String userKey=mAuth.getCurrentUser().getUid();


        if((checkField(familycode, editFamilyCode))==0){
            Log.d("Error", "campi vuoti");
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseDatabase.getReference().child("Users").child(userKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        User u = task.getResult().getValue(User.class);
                        firebaseDatabase.getReference().child("Families").child(familycode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                //controlla che esista effetivamente un gruppo con quel codice
                                if (task.getResult().getValue() == null) {
                                    Toast.makeText(MainActivity.this, "Non esiste un gruppo con questo codice", Toast.LENGTH_LONG).show();
                                    editFamilyCode.requestFocus();
                                }
                                //se esiste
                                else {
                                    //creo l'oggetto famiglia corrispondente al child nel database con il codice "familycode"
                                    Family f = task.getResult().getValue(Family.class);
                                    //TODO: quando prelevo la famiglia non salva la chat - FIXARE (completed)
                                    //aggiungo il nuovo membro all'Oggetto famiglia f
                                    //se l'inserimento va a buon fine
                                    if (f.addMember(u, userKey) == 1) {

                                        firebaseDatabase.getReference().child("Families").child(familycode).setValue(f).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this, "Ti sei unito al gruppo", Toast.LENGTH_LONG).show();

                                                    //TODO: 3) creare un nuovo elemento nel db che tiene traccia dell'id dell'utente e del gruppo a cui appartiene (COMPLETED)
                                                    firebaseDatabase.getReference().child("TrackFamily").child(userKey).child("Family").setValue(familycode);

                                                    /*
                                                    TODO:salvare l'id famiglia dell'utente nel file Setting per non dover
                                                     passare ogni volta per il database ( COMPLETED )
                                                     */
                                                    //salvo nel file Settings una voce che memorizza l'id dell gruppo famiglia di cui l'utente fa parte
                                                    //in questo modo non devo ricercare ogni volta il gruppo a cui appartiene l'utente
                                                    putFamilyIdIntoFile(familycode);

                                                /*
                                                TODO: reindirizzare l'utente nella pagina del gruppo famiglia dopo essersi unito ( COMPLETED )
                                               */
                                                    //creo l'oggetto intent che mi porta alla pagina principale del gruppo
                                                    Intent groupPage = new Intent(MainActivity.this, GroupPageActivity.class);
                                                    //passo il codice famiglia alla nuova activity che sto lanciando, in questo modo non c'è bisogno
                                                    //di rileggerlo dal file
                                                    groupPage.putExtra("familyId", familycode);
                                                    startActivity(groupPage,ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                                    finish();

                                                } else {
                                                    Toast.makeText(MainActivity.this, "Non è stato possibile unirsi al gruppo", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });


                                    } else {
                                        Toast.makeText(MainActivity.this, "Non è stato possibile unirsi al gruppo", Toast.LENGTH_LONG).show();
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Non è stato possibile unirsi al gruppo", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void createGroup() {
        //getta il numero di componenti massimo decisi per la creazione del gruppo e lo parsa ad intero
        String familyMember = editFamilyNumber.getText().toString().trim();

        //se il campo non è vuoto
        if (checkField(familyMember, editFamilyNumber) == 1) {

            Integer intFamilyMember;
            try {
                //estrapola il valore intero dalla stringa se questa è un numero
                intFamilyMember = Integer.parseInt(familyMember);
            } catch (Exception e) {
                //nel caso in cui non sia un numero genera eccezzione e richiede un numero di componenti valido
                editFamilyNumber.requestFocus();
                editFamilyNumber.setError("Inserisci un numero di componenti valido! ");
                return;
            }

            //controlla che il numero di componenti inseriti sia maggiore di 0
            if (intFamilyMember <= 0 || intFamilyMember == null ) {
                editFamilyNumber.requestFocus();
                editFamilyNumber.setError("Inserisci un numero di componenti maggiore di 0 ! ");
                return;
            }
            //chiave che identiifica l'utente loggato
            String userKey = mAuth.getCurrentUser().getUid();
            //getta lo user loggato attraverso l'id di autenticazione
            //il listener ForSingleEvent cerca l'utente nella cache

            progressBar.setVisibility(View.VISIBLE);

            firebaseDatabase.getReference().child("Users").child(userKey).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        //crea un'istanza della classe User con l'oggetto snapshot trovato a cui corrisponde la userKey
                        User u = task.getResult().getValue(User.class);

                        // TODO: inserire utente nella famiglia e creare gruppo sul database (COMPLETED)
                        //crea un oggetto famiglia il cui nome rappresentato dal cognome del creatore, e il numero di conmponenti inseriti
                        //in fase di creazione
                        Family f = new Family(u.getSurname(), intFamilyMember);
                        //aggiungo l'utente che sta creando la famiglia al gruppo
                        if (f.addMember(u, mAuth.getCurrentUser().getUid()) == 1) {
                            //metto il riferimento alla voce Families nel DB
                            databaseReference = firebaseDatabase.getReference("Families");
                            //salvo la chiave univoca che viene generata attraverso il push sul database senza effettivamente modificare il database
                            String familyCode = databaseReference.push().getKey();
                            //ora inserisco il gruppo famiglia con l'id ricavato attraverso push().getKey() che genera un ID univoco con il timestamp e setValue setta i campi
                            databaseReference.child(familyCode).setValue(f).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //messaggio che mi comunica che la creazione è andata a buon fine
                                        Toast.makeText(MainActivity.this, "Creazione Gruppo completata", Toast.LENGTH_LONG).show();
                                        //TODO: creare un nuovo elemento nel db che tiene traccia dell'id dell'utente e del gruppo a cui appartiene ( COMPLETED )
                                        databaseReference = firebaseDatabase.getReference().child("TrackFamily");
                                        databaseReference.child(userKey).child("Family").setValue(familyCode);
                                        //salvo l'id del gruppo all'interno del file Settings
                                        putFamilyIdIntoFile(familyCode);

                                        /*
                                         TODO: reindirizzare l'utente nella pagina del gruppo famiglia ( COMPLETED )
                                        */
                                        //creo l'oggetto intent che mi porta alla pagina principale del gruppo
                                        Intent groupPage = new Intent(MainActivity.this, GroupPageActivity.class);
                                        //passo il codice famiglia alla nuova activity che sto lanciando, in questo modo non c'è bisogno
                                        //di rileggerlo dal file nella prossima activity
                                        groupPage.putExtra("familyId", familyCode);
                                        groupPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(groupPage, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                        finish();

                                    } else {
                                        //messaggio che mi comunica che la creazione NON è andata a buon fine
                                        Toast.makeText(MainActivity.this, "Creazione Gruppo fallita, Riprovare", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Creazione Gruppo fallita, Riprovare", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Creazione Gruppo fallita, Riprovare", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });


        }
    }


}

