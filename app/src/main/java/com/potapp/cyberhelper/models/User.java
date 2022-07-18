package com.potapp.cyberhelper.models;

import java.io.Serializable;

public class User implements Serializable {
    private String UID;
    private String name;
    private double rating;
    private Configuration configuration;

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

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
