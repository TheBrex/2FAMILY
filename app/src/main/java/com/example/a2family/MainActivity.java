package com.example.a2family;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2family.Families.Family;
import com.example.a2family.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements FieldChecker {

    private EditText editFamilyCode;
    private EditText editFamilyNumber;
    private Button joinButton;
    private Button createButton;
    private TextView logout;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().getRoot();

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

        /*
        TODO: inizializzare un oggetto UniqueGenerator e inserirci dentro tutti gli id dei gruppi già
         esistenti nel database per non creare un nuovo gruppo con un id uguale a uno di quelli esistenti
         ...
         ...
         ...

        Family f = new Family("Rossi", 4);
        User user1= new User("Mario", "Rossi", "mariorossi@email.com","via");
        User user2=new User("Marioe", "Rosdsfsi", "mariordsfssi@email.com","eweia");
        f.addMember(user1, mAuth.getCurrentUser().getUid());
        f.addMember(user2,"5445ty45rgetw5gt");
        databaseReference = firebaseDatabase.getReference("Families");
        databaseReference.push().setValue(f);
        databaseReference.child("Families").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */
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

                createGroup();
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

    private void createGroup() {
        //getta il numero di componenti massimo decisi per la creazione del gruppo e lo parsa ad intero
        String familyMember = editFamilyNumber.getText().toString().trim();

        //se il campo non è vuoto
        if (checkField(familyMember, editFamilyNumber) == 1) {
            int intFamilyMember = Integer.parseInt(familyMember);

            //chiave che identiifica l'utente loggato
            String userKey = mAuth.getCurrentUser().getUid();
            //getta lo user loggato attraverso l'id di autenticazione
            //il listener ForSingleEvent cerca l'utente nella cache

            progressBar.setVisibility(View.VISIBLE);

            databaseReference.child("Users").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //crea un'istanza della classe User con l'oggetto snapshot trovato a cui corrisponde la userKey
                    User u = snapshot.getValue(User.class);
                    System.out.println(u.toString());

                    // TODO: inserire utente nella famiglia e creare gruppo sul database
                    //crea un oggetto famiglia il cui nome rappresentato dal cognome del creatore, e il numero di conmponenti inseriti
                    //in fase di creazione
                    Family f = new Family(u.getSurname(), intFamilyMember );
                    //aggiungo l'utente che sta creando la famiglia al gruppo
                    f.addMember(u,mAuth.getCurrentUser().getUid());
                    //metto il riferimento alla voce Families nel DB
                    databaseReference = firebaseDatabase.getReference("Families");
                    //inserisco il gruppo famiglia con la funzione push() che genera un idUnivoco per il gruppo e setValue setta i campi
                    databaseReference.push().setValue(f).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Creazione Gruppo completata", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                /*
                                 TODO: reindirizzare l'utente nella pagina del gruppo famiglia
                                  ...
                                  ...
                                  ...
                                */
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            //Family f = new Family(mAuth.getCurrentUser());

            //firebaseDatabase.getReference("familyGroups").setValue()

        }
    }
}

