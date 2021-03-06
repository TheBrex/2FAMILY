package com.example.a2family.Classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;

public class Family {

    private String name;
    private String creatorID;
    //numero massimo dei componenti
    private int maxNumberComponents;
    //numero attuale dei componenti
    private int actualNumberComponents;
    //collection di utenti che compongono la famiglia
    private HashMap<String,User> members = new HashMap<>();
    private HashMap<String,Message> chat = new HashMap<>();

    private HashMap<String, Product> groceryList = new HashMap<>();
    private HashMap<String, TaskToDo> tasksToDo = new HashMap();
    private HashMap<String, HashMap<String,Event> > events = new HashMap<>();


    public Family(String name, int maxNumberComponents, String creatorID) {

        this.name = name;
        this.actualNumberComponents=0;
        this.maxNumberComponents=maxNumberComponents;
        this.creatorID = creatorID;
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

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    public HashMap<String, Message> getChat() {
        return chat;
    }

    public void setChat(HashMap<String, Message> chat) {
        this.chat = chat;
    }

    public HashMap<String, HashMap<String, Event>> getEvents() {
        return events;
    }

    public void setEvents(HashMap<String, HashMap<String, Event>> events) {
        this.events = events;
    }

    public HashMap<String, Product> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(HashMap<String, Product> groceryList) {
        this.groceryList = groceryList;
    }

    public HashMap<String, TaskToDo> getTasksToDo() {
        return tasksToDo;
    }

    public void setTasksToDo(HashMap<String, TaskToDo> tasksToDo) {
        this.tasksToDo = tasksToDo;
    }
}

