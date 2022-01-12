package com.example.a2family.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2family.Activities.GroceryListActivity;
import com.example.a2family.Classes.Product;
import com.example.a2family.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    ArrayList<Product> productArrayList;
    Context context;
    protected FirebaseAuth mAuth=FirebaseAuth.getInstance();
    protected FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // creating a variable for our Database
    // Reference for Firebase.
    protected DatabaseReference databaseReference=firebaseDatabase.getReference().getRoot();

    public ProductAdapter(ArrayList<Product> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products, parent, false);
        ProductAdapter.ProductViewHolder productViewHolder = new ProductAdapter.ProductViewHolder(v);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.itemView.setTag(productArrayList.get(position));
        Product p = productArrayList.get(position);
        holder.number.setText(String.valueOf(holder.getAdapterPosition()+1));
        holder.name.setText(p.getDescription());
        holder.quantity.setText("Quantità: "+String.valueOf(p.getQuantity()));
        holder.remove.setImageResource(R.drawable.ic_baseline_close_24);

        //se il prodotto è stato acquistato allora setta nasconde l'icona utile per contrassegnare un elemento come acquistato
        if(p.isBought()){
            holder.bought.setVisibility(View.INVISIBLE);
        }
        else{
            holder.bought.setVisibility(View.VISIBLE);
        }

        //quando viene cliccata l'icona per contrassegnare il prodotto come comprato
        holder.bought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof GroceryListActivity){
                    //richiama il metodo che modifica lo stato di un prodotto nel DB estrapolando l'oggetto tramite la sua posizione all'interno dell'arraylist
                    ((GroceryListActivity)context).buyProduct(productArrayList.get(holder.getAdapterPosition()));
                    //modifica in locale la visualizzazione
                    holder.bought();
                }
            }
        });

        //quando viene cliccata l'icona per rimuovere il prodotto
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof GroceryListActivity){
                    //richiama il metodo per la rimozione del prodotto nel BD estrapolando l'oggetto tramite la sua posizione all'interno dell'arraylist
                    ((GroceryListActivity)context).removeProduct(productArrayList.get(holder.getAdapterPosition()));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }



    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView name;
        private TextView quantity;
        private ImageView bought;
        private ImageView remove;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.number=(TextView) itemView.findViewById(R.id.number);
            this.name=(TextView) itemView.findViewById(R.id.name);
            this.quantity=(TextView) itemView.findViewById(R.id.itemquantity);
            this.bought=(ImageView) itemView.findViewById(R.id.bought);
            this.remove=(ImageView) itemView.findViewById(R.id.remove);
        }

        private void bought(){
            this.bought.setVisibility(View.INVISIBLE);
        }


    }




}
