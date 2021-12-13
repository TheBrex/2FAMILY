package com.example.a2family.Classes;

public class Position {

    protected float latidude;
    protected float longitude;

    public Position() {
    }

    public Position(float latidude, float longitude) {
        this.latidude = latidude;
        this.longitude = longitude;
    }

    public void setLatidude(float latidude) {
        this.latidude = latidude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatidude() {
        return latidude;
    }

    public float getLongitude() {
        return longitude;
    }


}
