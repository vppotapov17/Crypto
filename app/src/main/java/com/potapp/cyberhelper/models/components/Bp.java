package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Bp extends Component{

    // ---------------------------------------------------------------------------------------------
    // поля класса
    // ---------------------------------------------------------------------------------------------


    // основные характеристики

    private int capacity;                                                                           // мощность
    private int capacity12v;                                                                        // мощность по 12В линии
    private String certificate;                                                                     // сертификат 80 Plus
    private boolean activePFC;                                                                      // наличие активного PFC
    private int efficiency;                                                                         // КПД
    private int MTBF;                                                                               // время наработки на отказ

    // разъёмы

    private String cpuPower;                                                                        // питание процессора и мат. платы
    private int length24pin;                                                                        // длина линии (24pin), мм
    private String gpuPower;                                                                        // питание видеокарты
    private int sata_ConnectorsQuantity;                                                            // количество разъёмов SATA
    private int molex_ConnectorsQuantity;                                                           // количество разъёмов Molex

    @Ignore
    private List<String[]> otherSpecifications;                                                     // остальные характеристики


    // ---------------------------------------------------------------------------------------------
    // set-методы
    // ---------------------------------------------------------------------------------------------


    public void setCapacity(int capacity) {                                                   // мощность
        this.capacity = capacity;
    }

    public void setCapacity12v(int capacity12v) {                                           // мощность по линии 12В
        this.capacity12v = capacity12v;
    }

    public void setCertificate(String certificate) {                                          // сертификат
        this.certificate = certificate;
    }

    public void setActivePFC(boolean activePFC) {                                             // наличие активного PFC
        this.activePFC = activePFC;
    }

    public void setEfficiency(int efficiency) {                                                     // КПД
        this.efficiency = efficiency;
    }

    public void setMTBF(int MTBF) {                                                           // время наработки на отказ
        this.MTBF = MTBF;
    }

    public void setCpuPower(String cpuPower) {                                                  // питание процессора и мат. платы
        this.cpuPower = cpuPower;
    }

    public void setLength24pin(int length24pin) {                                       // длина линии питания мат. платы (24pin)
        this.length24pin = length24pin;
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

    public void setOtherSpecifications(List<String[]> otherSpecifications) {
        this.otherSpecifications = otherSpecifications;
    }

    // ---------------------------------------------------------------------------------------------
    // get-методы
    // ---------------------------------------------------------------------------------------------


    public int getCapacity() {                                                                   // мощность
        return capacity;
    }

    public int getCapacity12v() {                                                               // мощность по линии 12В
        return capacity12v;
    }

    public String getCertificate() {                                                             // сертификат
        return certificate;
    }

    public boolean isActivePFC() {                                                               // наличие активного PFC
        return activePFC;
    }

    public int getEfficiency() {                                                                    // КПД
        return efficiency;
    }

    public int getMTBF() {                                                                       // время наработки на отказ
        return MTBF;
    }

    public String getCpuPower() {                                                                 // питание процессора и мат. платы
        return cpuPower;
    }

    public int getLength24pin() {                                                             // длина линии питания мат. платы (24pin)
        return length24pin;
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

    public List<String[]> getOtherSpecifications() {
        return otherSpecifications;
    }

    // ---------------------------------------------------------------------------------------------
    // дополнительные методы
    // ---------------------------------------------------------------------------------------------

    public int getDurability() {                                                                    // надёжность

        switch (getProducer())
        {
            case "Seasonic":
            case "BeQuiet":
            case "SuperFlower":
            case "Enermax":

                return 100;

            case "FSP":
            case "DeepCool":
            case "Chieftec":
            case "Thermaltake":
            case "Corsair":
            case "CoolerMaster":
            case "Gigabyte":

                return 80;

            case "Zalman":
            case "Cougar":

                return  60;

            case "Aerocool":
            case "Hiper":
            case "Xilence":

                return 40;

            default:

                return 20;
        }
    }

    // ---------------------------------------------------------------------------------------------
    // переопределённые методы класса Component
    // ---------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();
        spec.setSpecTitle("Надёжность");
        spec.setSpecValue(getDurability() + " %");

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
        spec.setSpecTitle("Сертификат 80Plus");
        spec.setSpecValue(certificate);

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Основные характеристики", ""});
        specs.add(new String[]{"Надёжность", getDurability() + " %"});
        if (capacity != 0) specs.add(new String[]{"Номинальная мощность", capacity + " Вт"});
        if (capacity12v != 0) specs.add(new String[]{"Мощность по линии 12В", capacity12v + " Вт"});
        if (certificate != null) specs.add(new String[]{"Сертификат 80Plus", certificate});

        if (activePFC) specs.add(new String[]{"Активный PFC", "есть"});
        else specs.add(new String[]{"Активный PFC", "нет"});

        if (efficiency != 0) specs.add(new String[]{"КПД", efficiency + " %"});
        if (MTBF != 0) specs.add(new String[]{"Время наработки на отказ", MTBF + " ч"});

        specs.add(new String[]{"Разъёмы", ""});
        if (cpuPower != null) specs.add(new String[]{"Питание процессора и материнской платы", cpuPower});
        if (length24pin != 0) specs.add(new String[]{"Длина линии 24pin", length24pin + " мм"});

        if (gpuPower != null) specs.add(new String[]{"Питание видеокарты", gpuPower});
        else specs.add(new String[]{"Питание видеокарты", "нет"});

        specs.add(new String[]{"Количество разъёмов SATA3", sata_ConnectorsQuantity + ""});
        specs.add(new String[]{"Количество разъёмов MOLEX", molex_ConnectorsQuantity + ""});

        //specs.addAll(otherSpecifications);

        return specs;
    }

}
