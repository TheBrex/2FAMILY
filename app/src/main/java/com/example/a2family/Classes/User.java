package com.example.a2family.Classes;

import android.location.Location;

public class User {

    private String name, surname, address, email;
    private Position position;


    public User(String name, String surname, String address, String email) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.position = new Position(0,0);

    }

    public User() {
    }

    public User(User o){
        this.name=o.name;
        this.surname=o.surname;
        this.address=o.address;
        this.email=o.email;
        this.position=o.position;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", position=" + position +
                '}';
    }

}
