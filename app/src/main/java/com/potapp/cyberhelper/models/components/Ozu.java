package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;

import com.google.firebase.database.DataSnapshot;
import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Ozu extends Component {

    private String family;                                                                                  // линейка

    // количество и объём
    private int modulesQuantity;                                                                    // количество модулей в комплекте
    private int singleCapacity;                                                                     // объём одного модуля

    // спецификации
    private String type;                                                                            // тип
    private int frequency;                                                                          // частота
    private String latency;                                                                         // латентность
    private boolean ECC;                                                                            // поддержка ECC
    private boolean radiator;                                                                       // наличие радиатора


    // ---------------------------------------------------------------------------------------------
    // сеттеры
    // ---------------------------------------------------------------------------------------------


    public void setFamily(String family) {
        this.family = family;
    }

    public void setModulesQuantity(int modulesQuantity) {
        this.modulesQuantity = modulesQuantity;
    }

    public void setSingleCapacity(int singleCapacity) {
        this.singleCapacity = singleCapacity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public void setECC(boolean ECC) {
        this.ECC = ECC;
    }

    public void setRadiator(boolean radiator) {
        this.radiator = radiator;
    }

    // ---------------------------------------------------------------------------------------------
    // геттеры
    // ---------------------------------------------------------------------------------------------


    public String getFamily() {
        return family;
    }

    public int getModulesQuantity() {
        return modulesQuantity;
    }

    public int getSingleCapacity() {
        return singleCapacity;
    }

    public String getType() {
        return type;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getLatency() {
        return latency;
    }

    public boolean isECC() {
        return ECC;
    }

    public boolean isRadiator() {
        return radiator;
    }

    // ---------------------------------------------------------------------------------------------
    // переопределенные методы класса Component
    // ---------------------------------------------------------------------------------------------

    @Override
    public String getName(){
        String name = getProducer();
        if (getFamily() != null) name += " " + getFamily();
        name += " " + modulesQuantity + "x" + singleCapacity + " Гб";
        return name;
    }

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Тип");
        spec.setSpecValue(type);

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Объём");
        spec.setSpecValue(modulesQuantity + "x" + singleCapacity + " Гб");

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Частота");
        spec.setSpecValue(frequency + " МГц");

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Основные характеристики", ""});
        if (getProducer() != null) specs.add(new String[]{"Бренд", getProducer()});
        if (getFamily() != null) specs.add(new String[]{"Линейка", getFamily()});
        if (getModel() != null) specs.add(new String[]{"Модель", getModel()});
        specs.add(new String[]{"Общий объём", (singleCapacity * modulesQuantity) + " Гб"});
        specs.add(new String[]{"Модулей в комплекте", modulesQuantity + ""});
        specs.add(new String[]{"Частота", frequency + " МГц"});
        specs.add(new String[]{"Латентность", latency + ""});


        specs.add(new String[]{"Особенности", ""});
        if (ECC) specs.add(new String[]{"Поддержка ECC", "есть"});
        else specs.add(new String[]{"Поддержка ECC", "нет"});

        if (radiator) specs.add(new String[]{"Наличие радиатора", "есть"});
        else specs.add(new String[]{"Наличие радиатора", "нет"});

        return specs;
    }


    public HashMap<String, String> toFirebase(){
        HashMap<String, String> map = new HashMap<>();

        map.put("Code", getProduct_code() + "");
        map.put("Price", getProduct_code() + "");
        map.put("Producer", getProduct_code() + "");
        map.put("Model", getProduct_code() + "");
        map.put("Url", getProduct_code() + "");
        map.put("Picture", getProduct_code() + "");
        map.put("Frequency", getProduct_code() + "");
        map.put("ModulesQuantity", getProduct_code() + "");
        map.put("SingleCapacity", getProduct_code() + "");
        map.put("Type", getProduct_code() + "");

        if (isECC()) map.put("ECC", "есть");
        else map.put("ECC", "нет");

        if (radiator) map.put("Radiator", "есть");
        else map.put("Radiator", "нет");

        return map;
    }

    public static Ozu createFromSnapshot(DataSnapshot snap){
        Ozu ozu = new Ozu();

        ozu.setProduct_code(Integer.parseInt(snap.getKey()));
        ozu.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
        ozu.setProducer(snap.child("Producer").getValue().toString());
        ozu.setModel(snap.child("Model").getValue().toString());
        try {
            ozu.setFamily(snap.child("Family").getValue().toString());
        }
        catch (NullPointerException e){}

        ozu.setRefLink(snap.child("Url").getValue().toString());
        ozu.setPictureLink(snap.child("Picture").getValue().toString());

        ozu.setType(snap.child("Type").getValue().toString());
        ozu.setSingleCapacity(Integer.parseInt(snap.child("SingleCapacity").getValue().toString()));
        ozu.setModulesQuantity(Integer.parseInt(snap.child("ModulesQuantity").getValue().toString()));
        ozu.setFrequency(Integer.parseInt(snap.child("Frequency").getValue().toString()));

        try {
            ozu.setLatency(snap.child("Latency").getValue().toString());
        }
        catch (NullPointerException e){}

        if (snap.child("ECC").getValue().toString().equals("есть")) ozu.setECC(true);
        else ozu.setECC(false);

        if (snap.child("Radiator").getValue().toString().equals("есть")) ozu.setRadiator(true);
        else ozu.setRadiator(false);

        return ozu;
    }


}
