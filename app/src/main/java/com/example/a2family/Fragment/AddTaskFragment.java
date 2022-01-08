package com.example.a2family.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.a2family.Activities.ToDoActivity;
import com.example.a2family.Classes.TaskToDo;
import com.example.a2family.R;

public class AddTaskFragment extends DialogFragment {

    private EditText title;
    private EditText description;
    private Button addTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.add_task_dialog, container, false);

        this.title=view.findViewById(R.id.title_task);
        this.description=view.findViewById(R.id.description_task);
        this.addTask=view.findViewById(R.id.add_task);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof ToDoActivity){
                    ((ToDoActivity)getActivity()).addTask(new TaskToDo(title.getText().toString(), description.getText().toString(), ((ToDoActivity) getActivity()).myUsername, ((ToDoActivity) getActivity()).myID));
                    dismiss();
                }
            }
        });

        return view;
    }
}


