package com.example.a2family.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2family.Adapters.MessageAdapter;
import com.example.a2family.Classes.Message;
import com.example.a2family.Classes.User;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends BaseActivity {

    private ArrayList<Message> messages;
    private ImageButton send;
    private EditText sendTextMessage;
    private RecyclerView rvMessages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //richiamo i fragment per i submenu
        bottMenu();
        bottoMenu();

        this.send =(ImageButton) findViewById(R.id.send);
        this.sendTextMessage=(EditText) findViewById(R.id.text_message);
        this.messages = new ArrayList<>();

        rvMessages = (RecyclerView) findViewById(R.id.rv_chat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(linearLayoutManager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = sendTextMessage.getText().toString().trim();
                if(!(message.isEmpty())) {
                    send_message(message);
                }
            }
        });

        retriveMessages();

    }


    private void retriveMessages() {
        String familyID = getFamilyIdFromFile();
        firebaseDatabase.getReference().child("Families").child(familyID).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot message : snapshot.getChildren()){
                    Message m = message.getValue(Message.class);
                    messages.add(m);
                }
                if( messages.size()>0){
                    adapter = new MessageAdapter(messages, getUserIdFromFile());
                    rvMessages.setAdapter(adapter);
                    rvMessages.scrollToPosition(rvMessages.getAdapter().getItemCount()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseDatabase.getReference().child("Families").child(familyID).child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message m = snapshot.getValue(Message.class);
                messages.add(m);
                if( messages.size()>0){
                    adapter = new MessageAdapter(messages, getUserIdFromFile());
                    rvMessages.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    rvMessages.scrollToPosition(rvMessages.getAdapter().getItemCount()-1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void send_message(String message) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        sendTextMessage.getText().clear();
        sendTextMessage.setHint("Message");
        firebaseDatabase.getReference().child("Users").child(getUserIdFromFile()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = null;

                name = snapshot.getValue(String.class);
                Message m = new Message(getUserIdFromFile(), name , message, ts);
                firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("chat").push().setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}