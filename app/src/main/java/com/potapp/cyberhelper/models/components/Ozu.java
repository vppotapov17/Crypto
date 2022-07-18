package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;

import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Ozu extends Component {


    // количество и объём
    private int modulesQuantity;                                                                    // количество модулей в комплекте
    private int capacity;                                                                           // общий объём

    // спецификации
    private String type;                                                                            // тип
    private int frequency;                                                                          // частота
    private String latency;                                                                         // латентность
    private boolean ECC;                                                                            // поддержка ECC
    private boolean radiator;                                                                       // наличие радиатора
    private boolean backlight;                                                                      // наличие подсветки

    // ---------------------------------------------------------------------------------------------
    // сеттеры
    // ---------------------------------------------------------------------------------------------


    public void setModulesQuantity(int modulesQuantity) {
        this.modulesQuantity = modulesQuantity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public void setBacklight(boolean backlight) {
        this.backlight = backlight;
    }

    // ---------------------------------------------------------------------------------------------
    // геттеры
    // ---------------------------------------------------------------------------------------------


    public int getModulesQuantity() {
        return modulesQuantity;
    }

    public int getCapacity() {
        return capacity;
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

    public boolean isBacklight() {
        return backlight;
    }

    // ---------------------------------------------------------------------------------------------
    // переопределенные методы класса Component
    // ---------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Общий объём");
        spec.setSpecValue(capacity + " Гб");

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Модулей в комплекте");
        spec.setSpecValue(modulesQuantity + "");

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
        specs.add(new String[]{"Общий объём", capacity + " Гб"});
        specs.add(new String[]{"Модулей в комплекте", modulesQuantity + ""});
        specs.add(new String[]{"Частота", frequency + " МГц"});
        specs.add(new String[]{"Латентность", latency + ""});

        if (ECC) specs.add(new String[]{"Поддержка ECC", "есть"});
        else specs.add(new String[]{"Поддержка ECC", "нет"});

        specs.add(new String[]{"Особенности", ""});
        if (backlight) specs.add(new String[]{"Наличие подсветки", "есть"});
        else specs.add(new String[]{"Наличие подсветки", "нет"});

        if (radiator) specs.add(new String[]{"Наличие радиатора", "есть"});
        else specs.add(new String[]{"Наличие радиатора", "нет"});

        return specs;
    }
}
