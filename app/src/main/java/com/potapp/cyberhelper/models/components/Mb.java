package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;

import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Mb extends Component {


    // основные характеристики

    private String socket;                                          // сокет
    private String chipset;                                         // чипсет
    private String formFactor;                                      // форм-фактор
    private String power;                                           // питание процессора

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

    // разъёмы на задней панели

    private String ps2;
    private int usb20;
    private int usb30;
    private int usb31;
    private int usbC;
    private int displayPort;
    private int vga;
    private int hdmi;

    // прочее

    private String sound;                                           // звук
    private String network;                                         // сетевой интерфейс


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

    public void setPower(String power) {
        this.power = power;
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

    public void setPs2(String ps2) {
        this.ps2 = ps2;
    }

    public void setUsb20(int usb20) {
        this.usb20 = usb20;
    }

    public void setUsb30(int usb30) {
        this.usb30 = usb30;
    }

    public void setUsb31(int usb31) {
        this.usb31 = usb31;
    }

    public void setUsbC(int usbC) {
        this.usbC = usbC;
    }

    public void setDisplayPort(int displayPort) {
        this.displayPort = displayPort;
    }

    public void setVga(int vga) {
        this.vga = vga;
    }

    public void setHdmi(int hdmi) {
        this.hdmi = hdmi;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setNetwork(String network) {
        this.network = network;
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

    public String getPower() {
        return power;
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

    public String getPs2() {
        return ps2;
    }

    public int getUsb20() {
        return usb20;
    }

    public int getUsb30() {
        return usb30;
    }

    public int getUsb31() {
        return usb31;
    }

    public int getUsbC() {
        return usbC;
    }

    public int getDisplayPort() {
        return displayPort;
    }

    public int getVga() {
        return vga;
    }

    public int getHdmi() {
        return hdmi;
    }

    public String getSound() {
        return sound;
    }

    public String getNetwork() {
        return network;
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
        if (power != null) specs.add(new String[]{"Питание", power});

        specs.add(new String[]{"Память", ""});
        if (ozuType != null) specs.add(new String[]{"Тип", ozuType});
        if (ozuSlotsQuantity != 0) specs.add(new String[]{"Количество слотов", ozuSlotsQuantity + ""});
        if (maxOzuSize != 0) specs.add(new String[]{"Максимальный объём", maxOzuSize + " Гб"});
        if (ozuFrequencySpec != 0) specs.add(new String[]{"Частотная спецификация", ozuFrequencySpec + " МГц"});

        specs.add(new String[]{"Слоты расширения", ""});
        if (pciE_x1 != 0) specs.add(new String[]{"PCI-Express x1", pciE_x1 + ""});
        if (pciEv3_x16 != 0) specs.add(new String[]{"PCI-Express 3.0 x16", pciEv3_x16 + ""});
        if (pciEv4_x16 != 0) specs.add(new String[]{"PCI-Express 4.0 x16", pciEv4_x16 + ""});

        specs.add(new String[]{"Дисковые контроллеры", ""});
        if (sata3 != 0) specs.add(new String[]{"SATA3", sata3 + " шт."});
        if (m2 != 0) specs.add(new String[]{"M2", m2 + " шт."});

        specs.add(new String[]{"Разъёмы", ""});
        if (ps2 != null) specs.add(new String[]{"PS/2", ps2});
        if (usb20 != 0) specs.add(new String[]{"USB 2.0", usb20 + " шт."});
        if (usb30 != 0) specs.add(new String[]{"USB 3.0", usb30 + " шт."});
        if (usb31 != 0) specs.add(new String[]{"USB 3.1", usb31 + " шт."});
        if (usbC != 0) specs.add(new String[]{"USB Type-C", usbC + " шт."});
        if (displayPort != 0) specs.add(new String[]{"Display Port", displayPort + " шт."});
        if (vga != 0) specs.add(new String[]{"VGA", vga + " шт."});
        if (hdmi != 0) specs.add(new String[]{"HDMI", hdmi + " шт."});

        specs.add(new String[]{"Прочее", ""});
        if (sound != null) specs.add(new String[]{"Звук", sound});
        if (network != null) specs.add(new String[]{"Сетевой интерфейс", network});

        return specs;
    }

}