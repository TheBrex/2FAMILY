package com.example.a2family.Activities;

import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.DataSnapshot;

public class AccountActivity extends BaseActivity {

    private TextView nameSurname;
    private TextView email;
    private TextView address;
    private TextView changePassword;
    private User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        bottoMenu();
        bottMenu();


        this.nameSurname= (TextView) findViewById(R.id.name_surname);
        this.email=(TextView) findViewById(R.id.profile_email);
        this.address=(TextView) findViewById(R.id.address);
        this.changePassword=(TextView) findViewById(R.id.change_password);

        initializeData();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
                //AccountActivity.this.signOut();
            }
        });

        /*
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAccount();
            }
        });
        */


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
        String name = getUsernameFromFile();
        String surname = getSurnameFromFile();
        this.nameSurname.setText(name.substring(0,1).toUpperCase()+ name.substring(1).toLowerCase()+" "+surname.substring(0,1).toUpperCase()+surname.substring(1).toLowerCase());
        this.email.setText(getEmailFromFile());
        this.address.setText(getAddressFromFile().toUpperCase());
    }




        /*
    private void deleteAccount() {

        new AlertDialog.Builder(this)
                .setTitle("Eliminazione Account")
                .setMessage("Sei sicuro di voler eliminare definivamente il tuo account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        ExitFragment bottomNavFragment = new ExitFragment("DeleteAccount",null);
                        //TODO: fixare -- getActivity() nel fragment ritorna null
                        bottomNavFragment.passActivity(AccountActivity.this);
                        Bundle b = new Bundle();
                        b.putString("userkey", getUserIdFromFile());
                        bottomNavFragment.setArguments(b);
                        bottomNavFragment.exitGroup();

                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }



    public void removeUserFromDB(String userId) {
        System.out.println("ciao");
        firebaseDatabase.getReference().child("Users").child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
                    mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mAuth.getCurrentUser().delete();
                            AccountActivity.this.signOut();
                        }
                    });

                }
            }
        });
    }


     */



}