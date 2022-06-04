package com.potapp.cyberhelper.data.models;

import java.io.Serializable;

public class User implements Serializable {
    private String UID;
    private String name;
    private double rating;

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
