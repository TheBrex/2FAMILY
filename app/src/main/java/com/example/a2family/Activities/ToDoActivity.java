package com.example.a2family.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2family.Adapters.TaskAdapter;
import com.example.a2family.Classes.TaskToDo;
import com.example.a2family.Fragment.AddTaskFragment;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToDoActivity extends BaseActivity {


    private ArrayList<TaskToDo> taskToDos =new ArrayList<>();
    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private FloatingActionButton addEvent;
    public String myUsername;
    public String myID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        myUsername=getUsernameFromFile();
        myID = getUserIdFromFile();

        //richiamo i fragment per i submenu
        bottMenu();
        bottoMenu();

        this.rvTasks=(RecyclerView) findViewById(R.id.tasksList);
        this.addEvent=(FloatingActionButton) findViewById(R.id.addeventbutton);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTasks.setLayoutManager(linearLayoutManager);
        adapter=new TaskAdapter(taskToDos, this);



        updateTaskList();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTaskFragment newFragment = new AddTaskFragment();
                newFragment.show(getSupportFragmentManager(),"AddTask");
            }
        });

    }

    private void updateTaskList() {
        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("tasksToDo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaskToDo t = snapshot.getValue(TaskToDo.class);
                taskToDos.add(t);
                if(taskToDos.size()>0){
                    adapter = new TaskAdapter(taskToDos, ToDoActivity.this);
                    rvTasks.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    rvTasks.scrollToPosition(rvTasks.getAdapter().getItemCount()-1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TaskToDo p = snapshot.getValue(TaskToDo.class);
                taskToDos.set(taskToDos.indexOf(p), p);
                rvTasks.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                TaskToDo t = snapshot.getValue(TaskToDo.class);
                taskToDos.remove(t);
                rvTasks.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addTask(TaskToDo task) {

        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("tasksToDo").push().setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ToDoActivity.this, "Hai aggiunto una commissione", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void completeTaskStatus(TaskToDo t){
        t.setDone(true);
        t.setWhoComplete(getUsernameFromFile());
        t.setWhoCompleteID(getUserIdFromFile());
        modifyTask(t);
    }
    public void uncompleteTaskStatus(TaskToDo t){
        t.setDone(false);
        t.setWhoComplete("");
        t.setWhoCompleteID("");
        modifyTask(t);
    }

    private void modifyTask(TaskToDo t){
        Query q = firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("tasksToDo").orderByChild("unique").equalTo(t.getUnique());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot task: snapshot.getChildren()) {
                    task.getRef().setValue(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteTask(String unique){
        Query q = firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("tasksToDo").orderByChild("unique").equalTo(unique);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot task: snapshot.getChildren()) {
                    task.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}