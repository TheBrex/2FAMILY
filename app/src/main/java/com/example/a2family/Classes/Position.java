package com.example.a2family.Classes;

public class Position {

    protected double latidude;
    protected double longitude;

    public Position() {
    }

    public Position(double latidude, double longitude) {
        this.latidude = latidude;
        this.longitude = longitude;
    }

    public void setLatidude(float latidude) {
        this.latidude = latidude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double getLatidude() {
        return latidude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Position{" +
                "latidude=" + latidude +
                ", longitude=" + longitude +
                '}';
    }
}
