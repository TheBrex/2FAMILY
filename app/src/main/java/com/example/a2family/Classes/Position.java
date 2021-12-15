package com.example.a2family.Classes;

public class Position {

    protected double latitude;
    protected double longitude;

    public Position() {
    }

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Position{" +
                "latidude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
