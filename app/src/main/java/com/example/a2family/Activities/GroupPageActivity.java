package com.example.a2family.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2family.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.Objects;


public class GroupPageActivity extends BaseActivity {

    private ListView member;
    private TextView logOut;
    private TextView family;
    private ImageView copy;

    private ListView listView;
    private ArrayList<String> memberList=new ArrayList<>();
    //array che contiene gli item della listview
    private ArrayAdapter<String> adapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        //richiamo i fragment per i submenu
        bottMenu();
        bottoMenu();

        //String userId = getUserIdFromFile();
        String familyId = getFamilyIdFromFile();
        System.out.println(familyId);
        //recupero l'id della famiglia
        //String familyId = getIntent().getStringExtra("familyId");

        this.member =(ListView) findViewById(R.id.member_list);
        this.progressBar=(ProgressBar) findViewById(R.id.loading_logoutgroup);
        this.listView= (ListView) findViewById(R.id.member_list);
        this.family = (TextView) findViewById(R.id.familyId);
        this.family.setText(familyId);
        this.copy=(ImageView) findViewById(R.id.copyCode);

        //inizializzo l'array di stringhe dove gli elementi hanno il layot definito in list_item.xml
        initializeList(familyId);

        //imposto il listener sull'immagine per procedere a copiare il codice famiglia per condividerlo piu facilmente
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Family Code", familyId);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(GroupPageActivity.this, "Family ID copiato negli appunti", Toast.LENGTH_LONG).show();
            }
        });


    }

    //inizializza la lista dei membri attraverso un listener sulla lista dei membri della famiglia con Id : familyID
    private void initializeList(String familyId) {
        this.adapter=new ArrayAdapter<String>(this, R.layout.list_item, R.id.member_name ,this.memberList);
        databaseReference=firebaseDatabase.getReference("Families").child(familyId).child("members");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("name").getValue(String.class).toUpperCase();
                String surname = Objects.requireNonNull(snapshot.child("surname").getValue(String.class)).toUpperCase();
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