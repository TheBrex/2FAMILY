package com.example.a2family;

import android.util.Patterns;
import android.widget.EditText;

public interface HelperInterface {


    //il metodo controlla che i campi non siano vuoti, se vuoti ritorna 0, 1 altrimenti
    default int checkField(String field, EditText xField){

        if(field.isEmpty()){
            xField.setError("Il campo è Richiesto ! ");
            xField.requestFocus();
            return 0;
        }
        else{
            return 1;
        }

    }

    //metodo controlla il formato dell'email, se scorretto ritorna 0, 1 altrimenti
    default int checkEmail(String field, EditText xField){

        checkField(field, xField);

        if(! Patterns.EMAIL_ADDRESS.matcher(field).matches()){
            xField.setError("Formato email non corretto ! ");
            xField.requestFocus();
            return 0;
        }
        else{
            return 1;
        }
    }

    //metodo controlla che la password sia lunga almeno 6 char, e i due campi password
    //contengano la stessa stringa, ritorna 0 se non rispettano le condizioni, 1 altrimenti
    default int checkPassword(String field, String field1, EditText xField, EditText yField){

        //controlla che i campi non siano vuoti
        checkField(field, xField);
        checkField(field1, yField);

        //controlla che la lunghezza della password sia di almeno 6 caratter
        if(field.length() < 6 ){
            xField.setError("Lunghezza minima 6 caratteri ! ");
            xField.requestFocus();
            return 0;
        }
        //controlla che la stringa in conferma password sia uguale a quella su password
        if(!field1.equals(field)){
            yField.setError("Le password non sono uguali !");
            yField.requestFocus();
            return 0;
        }
        else{
            return 1;
        }
    }


}
