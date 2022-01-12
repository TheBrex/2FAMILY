package com.example.a2family.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a2family.Activities.CalendarActivity;
import com.example.a2family.Activities.ToDoActivity;
import com.example.a2family.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class DeleteEventFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected FirebaseAuth mAuth=FirebaseAuth.getInstance();
    protected FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // creating a variable for our Database
    // Reference for Firebase.
    protected DatabaseReference databaseReference=firebaseDatabase.getReference().getRoot();


    public DeleteEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteEventFragment newInstance(String param1, String param2) {
        DeleteEventFragment fragment = new DeleteEventFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete, container, false);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.deleteeventview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //poichè il fragment è utilizzato sia per eliminare gli eventi che i task, ogni qual volta che viene premuto l'item
                //che conferma l'eliminazione devo controllare se l'activity su cui è creato il fragment appartiene o a CalendarActivity oppure a ToDOActivity
                //e richiamare metodi differenti
                switch (id){
                    case R.id.delete_event:
                        if(getActivity() instanceof CalendarActivity)
                            deleteEvent();
                        if(getActivity() instanceof ToDoActivity)
                            ((ToDoActivity)getActivity()).deleteTask(DeleteEventFragment.this.getArguments().getString("unique"));

                        if (navigationView.isShown()) {
                            dismiss();
                        }
                        break;
                }
                return false;
            }
        });
        return view;
    }

    private void deleteEvent() {
        //prende i valori passati tramite Bundle dalla CalendarActivity
        String itemToDelete = DeleteEventFragment.this.getArguments().getString("item_toDelete");
        String familyID = DeleteEventFragment.this.getArguments().getString("familyId");
        //trasformo le stringe corrispondenti a ore e minuti in interi
        int hour=Integer.parseInt(DeleteEventFragment.this.getArguments().getString("hour"));
        int minute=Integer.parseInt(DeleteEventFragment.this.getArguments().getString("minute"));
        //eseguo una query sul database per individuare l'evento da eliminare attraverso la stringa che rappresenta i millisecondi di un determinato giorno
        Query q = firebaseDatabase.getReference().child("Families").child(familyID).child("events").child(String.valueOf(CalendarActivity.c.getTimeInMillis())).orderByChild("eventDescription").equalTo(itemToDelete);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot event: snapshot.getChildren()) {
                    //scorro tra i possibili risultati ed elimino quello che corrisponde
                    if(hour==(event.child("hour").getValue(Integer.class)) && minute==(event.child("minute").getValue(Integer.class))) {
                        event.getRef().removeValue();
                        Toast.makeText(getContext(), "Eliminazione completata", Toast.LENGTH_LONG).show();
                    }
                    else{
                       Toast.makeText(getContext(), "C'è stato un problema in fase di eliminazione", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}