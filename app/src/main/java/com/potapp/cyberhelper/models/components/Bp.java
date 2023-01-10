package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.firebase.database.DataSnapshot;
import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Entity
public class Bp extends Component{

    // ---------------------------------------------------------------------------------------------
    // поля класса
    // ---------------------------------------------------------------------------------------------


    // основные характеристики

    private String family;                                                                          // линейка

    private int capacity;                                                                           // мощность
    private String certificate;                                                                     // сертификат 80 Plus
    private boolean activePFC;                                                                      // наличие активного PFC
    private int fanSize;                                                                            // размер вентилятора
    private String color;                                                                           // цвет

    // особенности
    private boolean detachableCables;                                                               // отсоединяющиеся кабели
    private boolean rgbFan;                                                                         // подсветка вентилятора

    // разъёмы

    private String cpuPower;                                                                        // питание процессора и мат. платы
    private String gpuPower;                                                                        // питание видеокарты
    private int sata_ConnectorsQuantity;                                                            // количество разъёмов SATA
    private int molex_ConnectorsQuantity;                                                           // количество разъёмов Molex


    // ---------------------------------------------------------------------------------------------
    // set-методы
    // ---------------------------------------------------------------------------------------------


    public void setCapacity(int capacity) {                                                   // мощность
        this.capacity = capacity;
    }

    public void setCertificate(String certificate) {                                          // сертификат
        this.certificate = certificate;
    }

    public void setActivePFC(boolean activePFC) {                                             // наличие активного PFC
        this.activePFC = activePFC;
    }


    public void setCpuPower(String cpuPower) {                                                  // питание процессора и мат. платы
        this.cpuPower = cpuPower;
    }

    public void setGpuPower(String gpuPower) {                                                // питание видеокарты
        this.gpuPower = gpuPower;
    }

    public void setSata_ConnectorsQuantity(int sata_ConnectorsQuantity) {                           // количество разъемов SATA
        this.sata_ConnectorsQuantity = sata_ConnectorsQuantity;
    }

    public void setMolex_ConnectorsQuantity(int molex_ConnectorsQuantity) {                         // количество разъемов MOLEX
        this.molex_ConnectorsQuantity = molex_ConnectorsQuantity;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setFanSize(int fanSize) {
        this.fanSize = fanSize;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDetachableCables(boolean detachableCables) {
        this.detachableCables = detachableCables;
    }

    public void setRgbFan(boolean rgbFan) {
        this.rgbFan = rgbFan;
    }

    // ---------------------------------------------------------------------------------------------
    // get-методы
    // ---------------------------------------------------------------------------------------------


    public int getCapacity() {                                                                   // мощность
        return capacity;
    }


    public String getCertificate() {                                                             // сертификат
        return certificate;
    }

    public boolean isActivePFC() {                                                               // наличие активного PFC
        return activePFC;
    }

    public String getCpuPower() {                                                                 // питание процессора и мат. платы
        return cpuPower;
    }

    public String getGpuPower() {                                                                // питание видеокарты
        return gpuPower;
    }

    public int getSata_ConnectorsQuantity() {                                                    // количество разъемов SATA
        return sata_ConnectorsQuantity;
    }

    public int getMolex_ConnectorsQuantity() {                                                   // количество разъемов MOLEX
        return molex_ConnectorsQuantity;
    }

    public String getFamily() {
        return family;
    }

    public int getFanSize() {
        return fanSize;
    }

    public String getColor() {
        return color;
    }

    public boolean isDetachableCables() {
        return detachableCables;
    }

    public boolean isRgbFan() {
        return rgbFan;
    }

    // ---------------------------------------------------------------------------------------------
    // дополнительные методы
    // ---------------------------------------------------------------------------------------------

    public String getDurability() {                                                                 // надёжность

        switch (getProducer().toUpperCase(Locale.ROOT).replaceAll(" ", ""))
        {
            case "SEASONIC":
            case "BEQUIET":
            case "SUPERFLOWER":
            case "ENERMAX":

                return "очень высокая";

            case "FSP":
            case "DEEPCOOL":
            case "CHIEFTEC":
            case "THERMALTAKE":
            case "CORSAIR":
            case "COOLERMASTER":
            case "GIGABYTE":

                return "высокая";

            case "ZALMAN":
            case "COUGAR":

                return  "средняя";

            case "AEROCOOL":
            case "HIPER":
            case "XILENCE":

                return "низкая";

            default:

                return "очень низкая";
        }
    }

    // ---------------------------------------------------------------------------------------------
    // переопределённые методы класса Component
    // ---------------------------------------------------------------------------------------------


    @Override
    public String getName() {
        String name = getProducer();
        if (family != null) name += " " + getFamily();
        name += " " + getModel();
        return name;
    }

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();
        spec.setSpecTitle("Надёжность");
        spec.setSpecValue(getDurability());

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();
        spec.setSpecTitle("Номинальная мощность");
        spec.setSpecValue(getCapacity() + " Вт");

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();
        spec.setSpecTitle("Сертификат");
        spec.setSpecValue(certificate);

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Основные характеристики", ""});

        specs.add(new String[]{"Бренд", getProducer()});
        if (family != null) specs.add(new String[]{"Линейка", getFamily()});
        specs.add(new String[]{"Модель", getModel()});
        if (capacity != 0) specs.add(new String[]{"Номинальная мощность", capacity + " Вт"});
        if (certificate != null) specs.add(new String[]{"Сертификат", certificate});

        if (activePFC) specs.add(new String[]{"Активный PFC", "есть"});
        else specs.add(new String[]{"Активный PFC", "нет"});

        if (fanSize != 0) specs.add(new String[]{"Размер вентилятора", fanSize + " мм"});
        if (color != null) specs.add(new String[]{"Цвет", color});

        specs.add(new String[]{"Разъёмы", ""});
        if (cpuPower != null) specs.add(new String[]{"Питание процессора и материнской платы", cpuPower});

        if (gpuPower != null) specs.add(new String[]{"Питание видеокарты", gpuPower});
        else specs.add(new String[]{"Питание видеокарты", "нет"});

        specs.add(new String[]{"Количество разъёмов SATA3", sata_ConnectorsQuantity + ""});
        specs.add(new String[]{"Количество разъёмов MOLEX", molex_ConnectorsQuantity + ""});

        specs.add(new String[]{"Особенности", ""});
        if (detachableCables) specs.add(new String[]{"Отсоединяющиеся кабели", "есть"});
        else specs.add(new String[]{"Отсоединяющиеся кабели", "нет"});

        if (rgbFan) specs.add(new String[]{"Подсветка вентилятора", "есть"});
        else specs.add(new String[]{"Подсветка вентилятора", "нет"});


        //specs.addAll(otherSpecifications);

        return specs;
    }


    public static Bp createFromSnapshot(DataSnapshot snap){
        Bp bp = new Bp();

        bp.setProduct_code(Integer.parseInt(snap.getKey()));
        bp.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
        bp.setProducer(snap.child("Producer").getValue().toString());
        bp.setModel(snap.child("Model").getValue().toString());
        try {
            bp.setFamily(snap.child("Family").getValue().toString());
        }
        catch (NullPointerException e){}

        bp.setRefLink(snap.child("Url").getValue().toString());
        bp.setPictureLink(snap.child("Picture").getValue().toString());

        if (snap.child("ActivePFC").getValue().toString().equals("есть"))
            bp.setActivePFC(true);
        else bp.setActivePFC(false);

        if (snap.child("Cables").getValue().toString().equals("есть"))
            bp.setDetachableCables(true);
        else bp.setDetachableCables(false);

        bp.setCapacity(Integer.parseInt(snap.child("Capacity").getValue().toString()));
        bp.setCertificate(snap.child("Certificate").getValue().toString());
        bp.setColor(snap.child("Color").getValue().toString());
        bp.setCpuPower(snap.child("CpuPower").getValue().toString());

        try {
            bp.setFanSize(Integer.parseInt(snap.child("FanSize").getValue().toString()));
        }
        catch (NullPointerException e){}
        bp.setGpuPower(snap.child("GpuPower").getValue().toString());
        bp.setMolex_ConnectorsQuantity(Integer.parseInt(snap.child("Molex").getValue().toString()));
        bp.setSata_ConnectorsQuantity(Integer.parseInt(snap.child("Sata").getValue().toString()));

        if (snap.child("RgbFan").getValue().toString().equals("есть"))
            bp.setRgbFan(true);
        else bp.setRgbFan(false);

        return bp;
    }


    public HashMap<String, String> toFirebase(){
        HashMap<String, String> map = new HashMap<>();
        return map;
    }
}
