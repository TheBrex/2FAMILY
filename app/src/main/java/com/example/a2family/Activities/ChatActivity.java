package com.example.a2family.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2family.Adapters.MessageAdapter;
import com.example.a2family.Classes.Message;
import com.example.a2family.Classes.User;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnSuccessListener;
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

        adapter = new MessageAdapter(messages,getUserIdFromFile());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = sendTextMessage.getText().toString().trim();
                if(!(message.isEmpty())) {
                    send_message(message);
                }
            }
        });


    }


    private void retriveMessages() {
        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot message : snapshot.getChildren()){
                    Message m = new Message();
                    m = snapshot.getValue(Message.class);
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
    }

    private void send_message(String message) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        firebaseDatabase.getReference().child("Users").child(getUserIdFromFile()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = null;

                name = snapshot.getValue(String.class);
                Message m = new Message(getUserIdFromFile(), name , message, ts);
                firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("chat").push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        messages.add(m);
                        adapter.notifyItemRangeChanged(messages.size(), 1);
                        rvMessages.scrollToPosition(rvMessages.getAdapter().getItemCount()-1);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}