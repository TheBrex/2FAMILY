package com.example.a2family.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.example.a2family.Classes.User;
import com.example.a2family.Fragment.ExitFragment;
import com.example.a2family.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class AccountActivity extends BaseActivity {

    private TextView nameSurname;
    private TextView email;
    private TextView address;
    private TextView changePassword;
    private TextView deleteAccount;
    private User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        bottoMenu();
        bottMenu();

        getUser(getUserIdFromFile());

        this.nameSurname= (TextView) findViewById(R.id.name_surname);
        this.email=(TextView) findViewById(R.id.profile_email);
        this.address=(TextView) findViewById(R.id.address);
        this.changePassword=(TextView) findViewById(R.id.change_password);
        this.deleteAccount=(TextView) findViewById(R.id.delete_account);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
                //AccountActivity.this.signOut();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });


    }

    private void deleteAccount() {

        new AlertDialog.Builder(this)
                .setTitle("Eliminazione Account")
                .setMessage("Sei sicuro di voler eliminare definivamente il tuo account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        FragmentManager manager = getSupportFragmentManager();
                        ExitFragment bottomNavFragment = new ExitFragment("DeleteAccount",null);
                        //TODO: fixare -- getActivity() nel fragment ritorna null
                        bottomNavFragment.exitGroup();
                        AccountActivity.this.signOut();

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void changePassword() {
        new AlertDialog.Builder(this)
                .setTitle("Cambio Password")
                .setMessage("Sei sicuro di voler cambiare la password? Se confermi verrà inviata una mail e effettuato il logout")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail());
                        AccountActivity.this.signOut();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void initializeData() {
        this.nameSurname.setText(me.getName().substring(0,1).toUpperCase()+ me.getName().substring(1).toLowerCase()+" "+me.getSurname().substring(0,1).toUpperCase()+me.getSurname().substring(1).toLowerCase());
        this.email.setText(me.getEmail());
        this.address.setText(me.getAddress());
    }

    private void getUser(String UID){
        User[] u = {new User()};
        firebaseDatabase.getReference().child("Users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    u[0] = new User(task.getResult().getValue(User.class));
                    me = new User(u[0]);
                    initializeData();
                }
            }
        });

    }



}