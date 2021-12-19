package com.example.a2family.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2family.Classes.Message;
import com.example.a2family.R;

import java.util.ArrayList;

// Provide a direct reference to each of the views within a data item
// Used to cache the views within the item layout for fast access
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Store a member variable for the messages
    private ArrayList<Message> messageArrayList;
    private Context context;
    private String userID;

    //utilizzate per assegnare il layout ad un oggetto messaggio
    //nel caso lo abbia inviato l'utente o meno
    private static final int VIEW_ME = 0;
    private static final int VIEW_YOU = 1;

    public MessageAdapter(ArrayList<Message> messageArrayList, String userID) {
        this.messageArrayList = messageArrayList;
        this.userID = userID;
    }

    @NonNull
    @Override
    // Usually involves inflating a layout from XML and returning the holder
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ME) {
            return new ViewHolderMe(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false));
        }
        if (viewType == VIEW_YOU) {
            return new ViewHolderYou(LayoutInflater.from(parent.getContext()).inflate(R.layout.your_message, parent, false));
        }
        return null;
    }

    //metodo che viene richiamato dal LayoutManager per popolare le View con i dati corrispondenti agli oggetti dell'arraylist
    // Involves populating data into the item through holder

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderMe) {
            ViewHolderMe viewHolder = (ViewHolderMe) holder;
            viewHolder.messageText.setText(String.format("%s", messageArrayList.get(position).getMessageText().trim()));
            viewHolder.time.setText(String.format("%s", messageArrayList.get(position).getTimestamp()));
            viewHolder.itemView.setTag(viewHolder);

        } else if (holder instanceof ViewHolderYou) {
            ViewHolderYou viewHolder = (ViewHolderYou) holder;
            viewHolder.username.setText(String.format("%s", messageArrayList.get(position).getMessageUserName()));
            viewHolder.messageText.setText(String.format("%s", messageArrayList.get(position).getMessageText()));
            viewHolder.time.setText(String.format("%s", messageArrayList.get(position).getTimestamp()));
            viewHolder.itemView.setTag(viewHolder);

            viewHolder.itemView.setTag(viewHolder);
        }
    }

    public void update(ArrayList<Message> updatedList){
        messageArrayList=updatedList;
        notifyDataSetChanged();
    }

    //dato l'indice dell'oggetto message memorizzato nell'arraylist ritorna 0 se è un messaggio dell'utente, 1 altrimenti
    @Override
    public int getItemViewType(int position) {
        if(messageArrayList.get(position).getMessageUserID().equals(userID)){
            return VIEW_ME;
        }else{
            return VIEW_YOU;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ViewHolderMe extends RecyclerView.ViewHolder {

        private TextView messageText;
        private TextView time;

        public ViewHolderMe(final View itemView) {
            super(itemView);
            messageText=(TextView) itemView.findViewById(R.id.message_body);
            time =(TextView) itemView.findViewById(R.id.time);
        }

        public TextView getMessageText() {
            return messageText;
        }

        public void setMessageText(TextView messageText) {
            this.messageText = messageText;
        }
    }

    public class ViewHolderYou extends RecyclerView.ViewHolder {

        private TextView username;
        private TextView messageText;
        private TextView time;
        private View avatar;

        public ViewHolderYou(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.user);
            messageText=(TextView) itemView.findViewById(R.id.message_body);
            time =(TextView) itemView.findViewById(R.id.time);
            avatar = (View) itemView.findViewById(R.id.avatar);
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }

        public TextView getMessageText() {
            return messageText;
        }

        public void setMessageText(TextView messageText) {
            this.messageText = messageText;
        }

        public View getAvatar() {
            return avatar;
        }

        public void setAvatar(View avatar) {
            this.avatar = avatar;
        }
    }
}
