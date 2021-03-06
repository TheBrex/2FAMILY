package com.example.a2family.Fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a2family.Activities.AccountActivity;
import com.example.a2family.Activities.BaseActivity;
import com.example.a2family.Activities.GroupPageActivity;
import com.example.a2family.Activities.MainActivity;
import com.example.a2family.Activities.MapsActivity;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExitFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // creating a variable for our
    // Firebase Database.
    protected FirebaseAuth mAuth=FirebaseAuth.getInstance();
    protected FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // creating a variable for our Database
    // Reference for Firebase.
    protected DatabaseReference databaseReference=firebaseDatabase.getReference().getRoot();
    protected Activity activity;


    public ExitFragment(Object o,   Object o1) {

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExitFragment newInstance(String param1, String param2) {
        ExitFragment fragment = new ExitFragment(null, null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    public void passActivity(@NonNull Activity activity) {
        this.activity = (Activity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exit, container, false);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.navigationexitview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.exit_group:
                        //metodo che passa l'activity al fragment da cui questo ?? stato generato
                        ExitFragment.this.passActivity(getActivity());
                        exitGroup();
                        break;
                    case R.id.exit_account:
                        Activity activity = getActivity();
                        if(activity instanceof BaseActivity){
                            ((BaseActivity)activity).signOut();
                        }
                        break;
                }
                return false;
            }
        });
        return view;
    }

    public void exitGroup() {
        //Activity activity = getActivity();
        if(activity instanceof BaseActivity ){
            //prendo lo user id e il familyId che riguardano l'utente che vuole uscire dal gruppo
            String familyId=((BaseActivity)activity).getFamilyIdFromFile();
            String userId=((BaseActivity)activity).getUserIdFromFile();

            /*
           TODO: decrementare il campo actualnumbercomponents, se ?? 0 rimuovere il gruppo famiglia
            dentro una transazione perch?? potrebbero uscire due persone nello stesso momento e non devo avere inconsistenza
            nel momento in cui devo capire se i membri del gruppo sono diventati 0 per poterlo eliminare ( COMPLETED )
            */
            //avvio una transazione per garantire consistenza dei dati
            databaseReference=firebaseDatabase.getReference().child("Families").child(familyId).child("actualNumberComponents");
            databaseReference.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    if (currentData.getValue() != null) {
                        //leggo il numero attuale dei componenti
                        int familyComponents = Integer.parseInt(currentData.getValue().toString());
                        //rimuovo l'utente dai membri della famiglia nel db
                        //System.out.println(familyComponents);
                        databaseReference.getParent().child("members").child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(activity, "Sei uscito dal Gruppo", Toast.LENGTH_LONG).show();
                            }
                        });
                        //se il numero dei componenti tolto quello corrente che sta uscendo ?? = a 0 allora il gruppo famiglia va eliminato
                        if (familyComponents - 1 <= 0) {

                            databaseReference.getParent().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("Info", "Eliminazione gruppo completata");
                                    //TODO : rimuovere il riferimento dell'utente da "trackFamily" e rimuove l'id dal file ( COMPLETED )
                                    //rimuovo anche il riferimento che tiene traccia del gruppo famiglia a cui appartiene uno User
                                    firebaseDatabase.getReference().child("TrackFamily").child(userId).getRef().removeValue();
                                    //rimuovo l'id famiglia dal file
                                    ((BaseActivity)activity).removeFamilyIdFromFile();

                                }
                            });
                        }
                        //se i membri sono > 0 aggiorno il valore dei componenti attuali
                        else {
                            Log.d("Info", "Rimozione utente completata");
                            //rimuovo id famiglia dal file
                            ((BaseActivity)activity).removeFamilyIdFromFile();
                            //rimuovo l'utente dal gruppo famiglia
                            firebaseDatabase.getReference().child("TrackFamily").child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    System.out.println("eliminato");
                                }
                            });
                            int fc= familyComponents-1;
                            //setto il valore nuovo dei componenti del gruppo
                            System.out.println(databaseReference.get().toString());
                            //aggiorno il nodo interessato dalla transazione con il nuovo numero di componenti
                            currentData.setValue(fc);

                        }
                    }
                    return Transaction.success(currentData);
                }
                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        //avvio intanto la mainActivity
                        ((BaseActivity)activity).removeFamilyIdFromFile();
                        //se stavo condividendo la posizione blocco la condivisione
                        MapsActivity.stopLocationUpdates();
                        Intent mainPage = new Intent(activity, MainActivity.class);
                        //termino tutte le activity tranne quella che sto lanciando
                        mainPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainPage, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                        activity.finish();
                }
            });
        }
    }
}