package com.example.a2family.Families;

import com.example.a2family.Users.User;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Family {


    private String name;
    //numero massimo dei componenti
    private int maxNumberComponents;
    //numero attuale dei componenti
    private int actualNumberComponents;
    //collection di utenti che compongono la famiglia
    private HashMap<String,User> members = new HashMap<>();



    public Family(String name, int maxNumberComponents) {

        this.name = name;
        this.actualNumberComponents=0;
        this.maxNumberComponents=maxNumberComponents;
    }

    public Family() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNumberComponents() {
        return maxNumberComponents;
    }

    public void setMaxNumberComponents(int maxNumberComponents) {
        this.maxNumberComponents = maxNumberComponents;
    }

    public int getActualNumberComponents() {
        return actualNumberComponents;
    }

    public void setActualNumberComponents(int actualNumberComponents) {
        this.actualNumberComponents = actualNumberComponents;
    }

    public int addMember(User u, String key){
        //controllo che ci sia ancora spazio nel gruppo, se c'è posso aggiungerlo
        if(this.actualNumberComponents + 1 <= maxNumberComponents){
            this.members.put(key, u);
            this.actualNumberComponents+=1;
            return 1;
        }
        else{
            //se non c'è spazio ritorno 0
            return 0;
        }
    }

    public int removeMember(User u, String key){
        if(members.remove(key, u)) {
            //rimuovo l'utente u e decremento di 1 i componenti
            this.actualNumberComponents-=1;
            return 1;
        }
        else{
            return 0;
        }
    }

    public HashMap<String, User> getMembers() {
        return members;
    }

    public void setMembers(HashMap<String, User> members) {
        this.members = members;
    }
}

