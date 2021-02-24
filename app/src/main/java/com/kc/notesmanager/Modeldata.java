package com.kc.notesmanager;

import android.graphics.Bitmap;

public class Modeldata  {
    String ID;

    Bitmap image;
    String title;
    String subject;
    String notes;
    String location;
    String date;
    String time;

    public Modeldata(String ID,Bitmap image, String title, String subject, String location, String date, String time, String notes) {

        this.image=image;
        this.title = title;
        this.subject = subject;
        this.notes = notes;
        this.location = location;
        this.date = date;
        this.time = time;
        this.ID=ID;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
