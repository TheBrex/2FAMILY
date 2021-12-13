package com.example.a2family.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2family.Fragment.ExitFragment;
import com.example.a2family.Fragment.NavigationFragment;
import com.example.a2family.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    // creating a variable for our
    // Firebase Database.
    protected FirebaseAuth mAuth=FirebaseAuth.getInstance();
    protected FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // creating a variable for our Database
    // Reference for Firebase.
    protected DatabaseReference databaseReference=firebaseDatabase.getReference().getRoot();


    public void bottMenu(){
        //Crea il bottom menu e avvia il fragment nel momento in cui vine cliccata l'incona del submenu
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        //NavigationView navigationView=(NavigationView) findViewById(R.id.navigationview);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationFragment bottomNavFragment = new NavigationFragment();
                bottomNavFragment.show(getSupportFragmentManager(), "TAG");
            }
        });

    }

    public void exitMenu() {
        //Crea il bottom menu e avvia il fragment nel momento in cui vine cliccata l'incona del submenu
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_app_bar);

        NavigationView exitView=(NavigationView) findViewById(R.id.navigationexitview);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        ExitFragment bottomNavFragment = new ExitFragment(null, null);
                        bottomNavFragment.show(getSupportFragmentManager(), "TAG");
                        break;
                    case R.id.home:
                        //se non mi trovo nell'activity principale e faccio tap su HOME, termina l'activity corrente
                        if(!(BaseActivity.this instanceof GroupPageActivity)){
                            finish();
                            //TODO aggiungere animazione
                        }
                }
                return false;
            }

        });
    }

    public void signOut() {
        mAuth.signOut();

        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();

        //vado alla pagina di login
        Intent loginPage=new Intent(BaseActivity.this, LoginActivity.class);
        startActivity(loginPage, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
        Toast.makeText(this,"Logout avvenuto con successo", Toast.LENGTH_LONG).show();
    }

    public void putFamilyIdIntoFile(String ID){
        //salvo l'id dell'utente loggato nel file Settings accessibile esclusivamente dall'app
        //in questo modo non c'è la necessità di passare per il database per recuperarlo
        //salvo la stringa corrispondente all'id della famiglia trovato nel db nel fire "Settings"
        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("familyId",ID);
        editor.apply();
    }

    public void putUserIdIntoFile(String ID){
        //salvo l'id dell'utente loggato nel file Settings accessibile esclusivamente dall'app
        //in questo modo non c'è la necessità di passare per il database
        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId",mAuth.getCurrentUser().getUid());
        editor.apply();
    }

    public String getFamilyIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String familyId = preferences.getString("familyId", "defaultvalue");
        return familyId;
    }

    public String getUserIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String userId = preferences.getString("userId", "defaultvalue");
        return userId;
    }

    public void removeUserIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        preferences.edit().remove("userId").apply();
    }

    public void removeFamilyIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        preferences.edit().remove("familyId").apply();
    }





}
