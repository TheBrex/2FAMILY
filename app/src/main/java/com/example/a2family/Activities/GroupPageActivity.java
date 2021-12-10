package com.example.a2family.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a2family.Fragment.ExitFragment;
import com.example.a2family.Fragment.NavigationFragment;
import com.example.a2family.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class GroupPageActivity extends BaseActivity {

    private ListView member;
    private TextView logOut;


    private ListView listView;
    private ArrayList<String> memberList=new ArrayList<>();
    //array che contiene gli item della listview
    private ArrayAdapter<String> adapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        bottMenu();
        exitMenu();


        String userId = getUserIdFromFile();
        String familyId = getFamilyIdFromFile();
        System.out.println(familyId);
        //recupero l'id della famiglia
        //String familyId = getIntent().getStringExtra("familyId");

        this.logOut=(TextView) findViewById(R.id.logout_group);
        this.member =(ListView) findViewById(R.id.member_list);
        this.progressBar=(ProgressBar) findViewById(R.id.loading_logoutgroup);
        this.listView= (ListView) findViewById(R.id.member_list);
        //inizializzo l'array di stringhe dove gli elementi hanno il layot definito in list_item.xml

        initializeList(familyId);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }


    private void initializeList(String familyId) {
        this.adapter=new ArrayAdapter<String>(this, R.layout.list_item, R.id.member_name ,this.memberList);
        databaseReference=firebaseDatabase.getReference("Families").child(familyId).child("members");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("name").getValue(String.class).toUpperCase();
                String surname = snapshot.child("surname").getValue(String.class).toUpperCase();
                memberList.add(name+" "+surname);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class).toUpperCase();
                String surname = snapshot.child("surname").getValue(String.class).toUpperCase();
                adapter.remove(name+" "+ surname);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setAdapter(adapter);
    }






}