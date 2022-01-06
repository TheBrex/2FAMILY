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

public class ProductAdapter extends BaseAdapter {

    ArrayList<Product> arrayList;
    Context context;

    protected FirebaseAuth mAuth=FirebaseAuth.getInstance();
    protected FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // creating a variable for our Database
    // Reference for Firebase.
    protected DatabaseReference databaseReference=firebaseDatabase.getReference().getRoot();


    public ProductAdapter(Context context, ArrayList<Product> products){

        this.arrayList=products;
        this.context=context;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.list_products, null);

            TextView number = convertView.findViewById(R.id.number);
            number.setText(position+1+".");

            TextView product = convertView.findViewById(R.id.name);
            product.setText(arrayList.get(position).getDescription());

            TextView productquantity = convertView.findViewById(R.id.itemquantity);
            productquantity.setText("Quantità: "+ String.valueOf(arrayList.get(position).getQuantity()));

            ImageView bought = convertView.findViewById(R.id.bought);
            if(arrayList.get(position).isBought()){
                bought.setVisibility(View.INVISIBLE);
            }

            ImageView remove = convertView.findViewById(R.id.remove);

            bought.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof GroceryListActivity) {
                        ((GroceryListActivity) context).buyProduct(arrayList.get(position));
                    }
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof GroceryListActivity){
                         ((GroceryListActivity) context).removeProduct(arrayList.get(position));
                    }
                }
            });

        }
        return convertView;

    }





}
