package com.potapp.cyberhelper.data.models.components;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.potapp.cyberhelper.data.models.mainSpec;

import java.io.Serializable;
import java.util.List;

@Entity
public abstract class Component implements Serializable {

    @PrimaryKey
    private int product_code;

    private String producer;
    private String model;
    private int price;
    private String refLink;

    private long lastUpdate;

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRefLink() {
        return refLink;
    }

    public void setRefLink(String refLink) {
        this.refLink = refLink;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public abstract mainSpec getMainSpec1();
    public abstract mainSpec getMainSpec2();
    public abstract mainSpec getMainSpec3();

    public abstract List<String[]> specifications();

    public String getName()
    {
        return producer + " " + model;
    }
}
