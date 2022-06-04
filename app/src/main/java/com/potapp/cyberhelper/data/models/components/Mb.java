package com.potapp.cyberhelper.data.models.components;

import androidx.room.Entity;

import com.potapp.cyberhelper.data.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Mb extends Component {

    // основные характеристики

    private String socket;                                          // сокет
    private String chipset;                                         // чипсет
    private String formFactor;                                      // форм-фактор
    private int cpuPower;                                           // питание процессора

    // память

    private String ozuType;                                         // тип памяти
    private int ozuSlotsQuantity;                                   // количество слотов
    private int maxOzuSize;                                         // максимальный объём
    private int ozuFrequencySpec;                                   // частотная спецификация

    // слоты расширения

    private int pciE_x1;                                            // количество слотов PCI-E x1
    private int pciEv3_x16;                                         // количество слотов PCI-E 3.0 x16
    private int pciEv4_x16;                                         // количество слотов PCI-E 4.0 x16

    // дисковые контроллеры

    private int sata3;                                              // количество разъёмов SATA3
    private int m2;                                                 // количество разъёмов M2
    private boolean RAID_support;                                   // поддержка SATA RAID

//    private HashMap<String, String> otherSpecifications;            // остальные характеристики


    // ---------------------------------------------------------------------------------------------
    // сеттеры
    // ---------------------------------------------------------------------------------------------


    public void setSocket(String socket) {
        this.socket = socket;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setCpuPower(int cpuPower) {
        this.cpuPower = cpuPower;
    }

    public void setOzuType(String ozuType) {
        this.ozuType = ozuType;
    }

    public void setOzuSlotsQuantity(int ozuSlotsQuantity) {
        this.ozuSlotsQuantity = ozuSlotsQuantity;
    }

    public void setMaxOzuSize(int maxOzuSize) {
        this.maxOzuSize = maxOzuSize;
    }

    public void setOzuFrequencySpec(int ozuFrequencySpec) {
        this.ozuFrequencySpec = ozuFrequencySpec;
    }

    public void setPciE_x1(int pciE_x1) {
        this.pciE_x1 = pciE_x1;
    }

    public void setPciEv3_x16(int pciEv3_x16) {
        this.pciEv3_x16 = pciEv3_x16;
    }

    public void setPciEv4_x16(int pciEv4_x16) {
        this.pciEv4_x16 = pciEv4_x16;
    }

    public void setSata3(int sata3) {
        this.sata3 = sata3;
    }

    public void setM2(int m2) {
        this.m2 = m2;
    }

    public void setRAID_support(boolean RAID_support) {
        this.RAID_support = RAID_support;
    }


    // ---------------------------------------------------------------------------------------------
    // геттеры
    // ---------------------------------------------------------------------------------------------

    public String getSocket() {
        return socket;
    }

    public String getChipset() {
        return chipset;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public int getCpuPower() {
        return cpuPower;
    }

    public String getOzuType() {
        return ozuType;
    }

    public int getOzuSlotsQuantity() {
        return ozuSlotsQuantity;
    }

    public int getMaxOzuSize() {
        return maxOzuSize;
    }

    public int getOzuFrequencySpec() {
        return ozuFrequencySpec;
    }

    public int getPciE_x1() {
        return pciE_x1;
    }

    public int getPciEv3_x16() {
        return pciEv3_x16;
    }

    public int getPciEv4_x16() {
        return pciEv4_x16;
    }

    public int getSata3() {
        return sata3;
    }

    public int getM2() {
        return m2;
    }

    public boolean isRAID_support() {
        return RAID_support;
    }

    // ---------------------------------------------------------------------------------------------
    // переопределённые методы класса Component
    // ---------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1() {
        mainSpec spec = new mainSpec();
        spec.setSpecTitle("Сокет");
        spec.setSpecValue(socket);

        return spec;
    }

    @Override
    public mainSpec getMainSpec2() {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Чипсет");
        spec.setSpecValue(chipset);

        return spec;
    }

    @Override
    public mainSpec getMainSpec3() {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Форм-фактор");
        spec.setSpecValue(formFactor);

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Основные характеристики", ""});
        if (socket != null) specs.add(new String[]{"Сокет", socket});
        if (chipset != null) specs.add(new String[]{"Чипсет", chipset});
        if (formFactor != null) specs.add(new String[]{"Форм-фактор", formFactor});
        if (cpuPower != 0) specs.add(new String[]{"Питание процессора", cpuPower + " pin"});

        specs.add(new String[]{"Память", ""});
        if (ozuSlotsQuantity != 0) specs.add(new String[]{"Количество слотов", ozuSlotsQuantity + ""});
        if (maxOzuSize != 0) specs.add(new String[]{"Максимальный объём", maxOzuSize + " Гб"});
        if (ozuFrequencySpec != 0) specs.add(new String[]{"Частотная спецификация", ozuFrequencySpec + " МГц"});

        specs.add(new String[]{"Слоты расширения", ""});
        if (pciE_x1 != 0) specs.add(new String[]{"PCI-Express x1", pciE_x1 + ""});
        if (pciEv3_x16 != 0) specs.add(new String[]{"PCI-Express 3.0 x16", pciEv3_x16 + ""});
        if (pciEv4_x16 != 0) specs.add(new String[]{"PCI-Express 4.0 x16", pciEv4_x16 + ""});

        specs.add(new String[]{"Дисковые контроллеры", ""});
        if (sata3 != 0) specs.add(new String[]{"Разъёмов SATA3", sata3 + ""});
        if (m2 != 0) specs.add(new String[]{"Разъёмов M2", m2 + ""});

        if (RAID_support) specs.add(new String[]{"Поддержка SATA RAID", "есть"});
        else specs.add(new String[]{"Поддержка SATA RAID", "нет"});

        return specs;
    }

}