package com.example.a2family.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2family.Activities.ToDoActivity;
import com.example.a2family.Classes.TaskToDo;
import com.example.a2family.Fragment.DeleteEventFragment;
import com.example.a2family.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    private ArrayList<TaskToDo> taskToDoArrayList;
    private Context mContex;
    protected FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public TaskAdapter(ArrayList<TaskToDo> taskToDoArrayList, Context mContex) {
        this.taskToDoArrayList = taskToDoArrayList;
        this.mContex = mContex;
    }


    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tasks, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(v);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        holder.itemView.setTag(taskToDoArrayList.get(position));

        TaskToDo t = taskToDoArrayList.get(position);
        holder.title.setText(t.getTitle());
        holder.description.setText(t.getDescription());
        holder.created_by.setText("Creato da: "+t.getCreatedBy().substring(0,1).toUpperCase() + t.getCreatedBy().substring(1).toLowerCase());


        if(t.getWhoComplete()!=null && !(t.getWhoComplete().equals("")) ) {
            holder.doneBy.setVisibility(View.VISIBLE);
            holder.doneBy.setText("Completato da  " + t.getWhoComplete().substring(0, 1).toUpperCase() + t.getWhoComplete().substring(1).toLowerCase());
            holder.star.setImageResource(R.drawable.ic_baseline_star_24);
            holder.status.setImageResource(R.drawable.ic_baseline_done_all_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tramite questo if solo il creatore del task può eliminarlo dalla lista dopo eventualmente averne verificato l'esito
                if(taskToDoArrayList.get(holder.getAdapterPosition()).getCreatedByID().equals(((ToDoActivity)mContex).getUserIdFromFile())) {
                    DeleteEventFragment deleteEventFragment = new DeleteEventFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("unique", taskToDoArrayList.get(holder.getAdapterPosition()).getUnique());
                    deleteEventFragment.setArguments(bundle);
                    deleteEventFragment.show(((AppCompatActivity) mContex).getSupportFragmentManager(), "TAG");
                }
            }
        });


        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tramite questo if vado a dare la possibilità di contrassegnare un attività come svolta o meno, a seconda se quella attività non sia stata svolta ancora da nessuno
                //oppure da me stesso, in questo modo non posso cambiare lo status di un'attività che non ha svolto l'utente loggato
                if(taskToDoArrayList.get(holder.getAdapterPosition()).getWhoCompleteID().equals(((ToDoActivity)mContex).getUserIdFromFile()) ||
                        taskToDoArrayList.get(holder.getAdapterPosition()).getWhoComplete().equals("")) {
                    if (v.getTag().equals("emptyStar")) {
                        holder.star.setTag("star");
                        holder.star.setImageResource(R.drawable.ic_baseline_star_24);
                        holder.changeStatus(holder.status);

                        holder.doneBy.setVisibility(View.VISIBLE);
                        if (mContex instanceof ToDoActivity) {
                            ((ToDoActivity) mContex).completeTaskStatus(taskToDoArrayList.get(holder.getAdapterPosition()));
                        }
                    } else {
                        holder.star.setTag("emptyStar");
                        holder.star.setImageResource(R.drawable.ic_baseline_star_border_24);
                        holder.changeStatus(holder.status);


                        holder.doneBy.setVisibility(View.INVISIBLE);
                        if (mContex instanceof ToDoActivity) {
                            ((ToDoActivity) mContex).uncompleteTaskStatus(taskToDoArrayList.get(holder.getAdapterPosition()));
                        }
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return taskToDoArrayList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView doneBy;
        private ImageView status;
        private ImageView star;
        private TextView created_by;



        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.task);
            this.description=(TextView) itemView.findViewById(R.id.task_description);
            this.doneBy=(TextView) itemView.findViewById(R.id.done_by);
            this.status=(ImageView) itemView.findViewById(R.id.status);
            this.star = (ImageView) itemView.findViewById(R.id.star);
            this.created_by=(TextView) itemView.findViewById(R.id.created_by);

        }


        private void changeStatus(ImageView status){
            if(status.getTag().equals("attention")){
                status.setTag("complete");
                status.setImageResource(R.drawable.ic_baseline_done_all_24);
            }
            else{
                status.setTag("attention");
                status.setImageResource(R.drawable.ic_baseline_report_problem_24);
            }
        }



    }
}
