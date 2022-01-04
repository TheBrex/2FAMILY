package com.example.a2family.Classes;

import java.util.Date;

public class Event {
    private long milliseconds;
    private String eventDescription;
    private int hour;
    private int minute;


    public Event() {
    }

    public Event(long milliseconds, String eventDescription, int hour, int minute) {
        this.milliseconds = milliseconds;
        this.eventDescription = eventDescription;
        this.hour = hour;
        this.minute = minute;
    }

    public Long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
