package com.example.a2family.Classes;

import java.util.Calendar;
import java.util.Objects;

public class TaskToDo {
   private String title;
   private String description;
   private Boolean done;
   private String whoComplete;
   private String unique ;

    public TaskToDo() {
    }

    public TaskToDo(String title, String description) {
        this.title = title;
        this.description = description;
        this.done = done;
        this.whoComplete = "";
        this.unique=""+Calendar.getInstance().getTimeInMillis()+title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getWhoComplete() {
        return whoComplete;
    }

    public void setWhoComplete(String whoComplete) {
        this.whoComplete = whoComplete;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskToDo)) return false;
        TaskToDo taskToDo = (TaskToDo) o;
        return Objects.equals(unique, taskToDo.unique);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unique);
    }
}
