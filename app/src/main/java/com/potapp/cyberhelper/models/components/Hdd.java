package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;

import com.google.firebase.database.DataSnapshot;
import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Hdd extends Component{


    // ---------------------------------------------------------------------------------------------
    // поля класса
    // ---------------------------------------------------------------------------------------------

    // основные характеристики

    private String family;                                                                          // линейка

    private String formFactor;                                                                      // форм-фактор
    private double capacity;                                                                        // объём
    private int bufferMemory;                                                                       // буферная память
    private int rotationSpeed;                                                                      // скорость вращения шпинделя

    // ---------------------------------------------------------------------------------------------
    // set-методы
    // ---------------------------------------------------------------------------------------------


    public void setFamily(String family) {
        this.family = family;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setBufferMemory(int bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    // ---------------------------------------------------------------------------------------------
    // get-методы
    // ---------------------------------------------------------------------------------------------


    public String getFamily() {
        return family;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public double getCapacity() {
        return capacity;
    }

    public int getBufferMemory() {
        return bufferMemory;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }


    // ---------------------------------------------------------------------------------------------
    // переопределенные методы класса Component
    // ---------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Объём");
        spec.setSpecValue(capacity + " ТБ");

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Буферная память");
        spec.setSpecValue(bufferMemory + " МБ");

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Скорость вращения");
        spec.setSpecValue(rotationSpeed + " об/мин");

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Характеристики", ""});
        if (getProducer() != null) specs.add(new String[]{"Бренд", getProducer()});
        if (family != null) specs.add(new String[]{"Линейка", family});
        if (getModel() != null) specs.add(new String[]{"Модель", getModel()});
        if (formFactor != null) specs.add(new String[]{"Форм-фактор", formFactor});
        if (capacity != 0) specs.add(new String[]{"Объём", capacity + " ТБ"});
        if (bufferMemory != 0) specs.add(new String[]{"Буферная память", bufferMemory + " МБ"});
        if (rotationSpeed != 0) specs.add(new String[]{"Скорость вращения", rotationSpeed + " об/мин"});

        return specs;
    }

    public static Hdd createFromSnapshot(DataSnapshot snap){
        Hdd hdd = new Hdd();

        hdd.setProduct_code(Integer.parseInt(snap.getKey()));
        hdd.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
        hdd.setProducer(snap.child("Producer").getValue().toString());
        hdd.setModel(snap.child("Model").getValue().toString());
        try {
            hdd.setFamily(snap.child("Family").getValue().toString());
        }
        catch (NullPointerException e){}

        hdd.setRefLink(snap.child("Url").getValue().toString());
        hdd.setPictureLink(snap.child("Picture").getValue().toString());

        hdd.setCapacity(Double.parseDouble(snap.child("Capacity").getValue().toString()));
        hdd.setFormFactor(snap.child("FormFactor").getValue().toString());
        hdd.setBufferMemory(Integer.parseInt(snap.child("Buffer").getValue().toString()));
        hdd.setRotationSpeed(Integer.parseInt(snap.child("Speed").getValue().toString()));

        return hdd;
    }


    public HashMap<String, String> toFirebase(){
        HashMap<String, String> map = new HashMap<>();
        return map;
    }
}
