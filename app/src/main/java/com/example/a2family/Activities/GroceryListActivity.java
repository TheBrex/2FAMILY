package com.example.a2family.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2family.Adapters.ProductAdapter;
import com.example.a2family.Adapters.TaskAdapter;
import com.example.a2family.Classes.Product;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroceryListActivity extends BaseActivity {

    private RecyclerView rvProducts;
    private ArrayList<Product> products = new ArrayList<>();
    private ProductAdapter adapter;
    private EditText productName;
    private EditText quantityDialog;
    private ImageView insertProduct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        this.rvProducts=(RecyclerView) findViewById(R.id.grocery);
        this.productName = (EditText) findViewById(R.id.product_name);
        this.insertProduct = (ImageView) findViewById(R.id.confirm_product);
        this.quantityDialog = (EditText) findViewById(R.id.quantity);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProducts.setLayoutManager(linearLayoutManager);

        adapter=new ProductAdapter(products, this);

        //richiamo i fragment per i submenu
        bottMenu();
        bottoMenu();


        updateGroceryList();

        //listener sul button aggiungi prodotto
        insertProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getto le stringhe corrispondenti al prodotto e alla quantità
                String d = productName.getText().toString();
                String q = quantityDialog.getText().toString().trim();
                //se la quantità è effettivamente un numero
                if(q.length()>0 && q.matches("-?(0|[1-9]\\d*)")) {
                    //getta la quantityìà
                    int quantity = Integer.parseInt(q);
                    //crea un prodotto con i valori inseriti
                    Product p = new Product(d, quantity);
                    if (d != null && d.length() > 0 && quantity > 0) {
                        addProduct(p);
                    } else {
                        Toast.makeText(GroceryListActivity.this, "Inserisci un prodotto e una quantità", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    insertProduct.requestFocus();
                }
            }
        });



    }

    private void updateGroceryList() {
        //this.adapter=new ProductAdapter(this, this.products);
        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("groceryList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product p = snapshot.getValue(Product.class);
                products.add(p);
                adapter.notifyDataSetChanged();
                rvProducts.setAdapter(adapter);
                rvProducts.scrollToPosition(rvProducts.getAdapter().getItemCount()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product p = snapshot.getValue(Product.class);
                products.set(products.indexOf(p), p);
                adapter.notifyDataSetChanged();
                rvProducts.setAdapter(adapter);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product p = snapshot.getValue(Product.class);
                products.remove(p);
                adapter.notifyDataSetChanged();
                rvProducts.setAdapter(adapter);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rvProducts.setAdapter(adapter);

    }

    private void addProduct(Product product) {

        firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("groceryList").push().setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    quantityDialog.getText().clear();
                    productName.getText().clear();
                    Toast.makeText(GroceryListActivity.this, "Prodotto aggiunto con successo", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(GroceryListActivity.this, "Aggiunta prodotto fallita", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void removeProduct(Product p){
        Query q = BaseActivity.firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("groceryList").orderByChild("unique").equalTo(p.getUnique());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot product: snapshot.getChildren()) {
                    product.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void buyProduct(Product p){

        if(!(p.isBought())) {
            Query q = BaseActivity.firebaseDatabase.getReference().child("Families").child(getFamilyIdFromFile()).child("groceryList").orderByChild("unique").equalTo(p.getUnique());
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot product : snapshot.getChildren()) {
                        p.setBought(true);
                        product.getRef().setValue(p);
                        Toast.makeText(GroceryListActivity.this, "Prodotto acquistato", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }




}