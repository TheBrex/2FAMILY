package com.example.a2family.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a2family.Classes.User;
import com.example.a2family.Fragment.ExitFragment;
import com.example.a2family.Fragment.NavigationFragment;
import com.example.a2family.Interfaces.HelperInterface;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity implements HelperInterface {
    // creating a variable for our
    // Firebase Database.
    protected FirebaseAuth mAuth=FirebaseAuth.getInstance();
    protected static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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

    public void bottoMenu() {
        //Crea il bottom menu e avvia il fragment nel momento in cui vine cliccata l'incona del submenu
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_app_bar);

        //NavigationView exitView=(NavigationView) findViewById(R.id.navigationexitview);
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
                        break;
                    case R.id.account:
                        if(!(BaseActivity.this instanceof AccountActivity)){
                            Intent account = new Intent(BaseActivity.this, AccountActivity.class);
                            startActivity(account, ActivityOptions.makeSceneTransitionAnimation(BaseActivity.this).toBundle());
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }

        });
    }


    public void signOut() {
        mAuth.signOut();

        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();

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
        putUserNameIntoFile(ID);


    }

    public String getFamilyIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getString("familyId", "defaultvalue");
    }

    public String getUserIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getString("userId", "defaultvalue");
    }

    public void removeUserIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        preferences.edit().remove("userId").apply();
    }

    public void removeFamilyIdFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        preferences.edit().remove("familyId").apply();
    }

    public void putUserNameIntoFile(String ID){
        firebaseDatabase.getReference().child("Users").child(ID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue()!=null) {
                        User u = task.getResult().getValue(User.class);
                        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", u.getName());
                        editor.apply();
                    }
                }
            }
        });

    }

    public String getUsernameFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getString("username", "defaultvalue");
    }
    public void putSurnameIntoFile(String surname) {
        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userSurname",surname);
        editor.apply();
    }
    public String getSurnameFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getString("userSurname", "defaultvalue");
    }


    public void putEmailIntoFile(String email){
        SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userEmail",email);
        editor.apply();
    }

    public void putAddressIntoFile(String address) {
            SharedPreferences preferences = getSharedPreferences("Settings",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userAddress",address);
            editor.apply();
    }
    public String getEmailFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getString("userEmail", "defaultvalue");
    }
    public String getAddressFromFile(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getString("userAddress", "defaultvalue");
    }



    public void getUser(String UID) {
        User[] u = {new User()};
        firebaseDatabase.getReference().child("Users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    u[0] = new User(task.getResult().getValue(User.class));
                    putUserIdIntoFile(mAuth.getCurrentUser().getUid());
                    putEmailIntoFile(u[0].getEmail());
                    putAddressIntoFile(u[0].getAddress());
                    putUserNameIntoFile(u[0].getName());
                    putSurnameIntoFile(u[0].getSurname());
                }
            }
        });
    }
}
