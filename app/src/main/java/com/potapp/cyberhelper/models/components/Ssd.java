package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;

import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Ssd extends Component {

    // основные характеристики

    private String family;
    private int capacity;                                                                           // объём
    private String formFactor;                                                                      // форм-фактор
    private String interFace;                                                                       // интерфейс подключения
    private int maxSpeed_write;                                                                     // максимальная скорость записи
    private int maxSpeed_read;                                                                      // максимальная скорость чтения
    private String storageType;                                                                     // тип памяти
    private int TBW;                                                                                // ресурс TBW

    // ---------------------------------------------------------------------------------------------
    // сеттеры
    // ---------------------------------------------------------------------------------------------


    public void setFamily(String family) {
        this.family = family;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setInterFace(String interFace) {
        this.interFace = interFace;
    }

    public void setMaxSpeed_write(int maxSpeed_write) {
        this.maxSpeed_write = maxSpeed_write;
    }

    public void setMaxSpeed_read(int maxSpeed_read) {
        this.maxSpeed_read = maxSpeed_read;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setTBW(int TBW) {
        this.TBW = TBW;
    }

    // ---------------------------------------------------------------------------------------------
    // геттеры
    // ---------------------------------------------------------------------------------------------


    public String getFamily() {
        return family;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public String getInterFace() {
        return interFace;
    }

    public int getMaxSpeed_write() {
        return maxSpeed_write;
    }

    public int getMaxSpeed_read() {
        return maxSpeed_read;
    }

    public String getStorageType() {
        return storageType;
    }

    public int getTBW() {
        return TBW;
    }

    // ---------------------------------------------------------------------------------------------
    // переопределенные методы класса Component
    // ---------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Объём");
        spec.setSpecValue(capacity + " Гб");

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Скорость чтения");
        spec.setSpecValue(maxSpeed_read + " МБ/с");

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Скорость записи");
        spec.setSpecValue(maxSpeed_write + " МБ/с");

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specifications = new ArrayList<>();

        String str[];

        str = new String[]{"Характеристики", ""};
        specifications.add(str);
        if (getProducer() != null) specifications.add(new String[]{"Бренд", getProducer()});
        if (getFamily() != null) specifications.add(new String[]{"Линейка", getFamily()});
        if (getModel() != null) specifications.add(new String[]{"Модель", getModel()});
        if (capacity != 0) {
            str = new String[]{"Объём", capacity + " Гб"};
            specifications.add(str);
        }
        if (formFactor != null) {
            str = new String[]{"Форм-фактор", formFactor + ""};
            specifications.add(str);
        }
        if (interFace != null) {
            str = new String[]{"Интерфейс", interFace + ""};
            specifications.add(str);
        }
        if (maxSpeed_read != 0) {
            str = new String[]{"Скорость чтения", maxSpeed_read + " МБ/с"};
            specifications.add(str);
        }
        if (maxSpeed_write != 0) {
            str = new String[]{"Скорость записи", maxSpeed_write + " МБ/с"};
            specifications.add(str);
        }
        if (storageType != null) {
            str = new String[]{"Тип памяти", storageType + ""};
            specifications.add(str);
        }
        if (TBW != 0) {
            str = new String[]{"Ресурс TBW", TBW + " ТБ"};
            specifications.add(str);
        }

        return specifications;
    }

}
