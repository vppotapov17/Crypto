package com.potapp.cyberhelper.models.components;


import android.content.Context;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Room;

import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cpu extends Component {

    // основные характеристики
    private String family;                                                                          // семейство процессоров
    private String socket;                                                                          // сокет
    public int cores;                                                                               // количество ядер
    private int streams;                                                                            // количество потоков
    private double baseFrequency;                                                                   // базовая частота
    private double turboFrequency;                                                                  // турбо-частота
    private int techprocess;                                                                        // техпроцесс
    private int tdp;                                                                                // тепловыделение
    private int cache;                                                                              // кэш L3
    private String shipment;                                                                        // тип поставки

    // память
    private int maxOzuFrequency;                                                                    // максимальная частота
    private int ozuChannels;                                                                        // количество каналов
    private String ozuType;                                                                         // тип памяти

    // прочее
    private double pciE_version;                                                                    // версия PCI-Express
    private String graphics;                                                                        // встроенная графика
    private String graphics_frequency;                                                              // частота графического ядра
    private String multiplier;                                                                      // множитель
    private String release_date;                                                                    // дата релиза

    // бенчмарки
    private int geekbench_multi;                                                                    // тест в Geekbench 5 (Multi)
    private int geekbench_single;                                                                   // тест в Geekbench 5 (Single)

    private double capacity;
    private double ratio;


    // ---------------------------------------------------------------------------------------------
    // сеттеры
    // ---------------------------------------------------------------------------------------------


    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public void setFamily(String family) {                                                  // семейство процессоров
        this.family = family;
    }

    public void setSocket(String socket) {                                                  // сокет
        this.socket = socket;
    }

    public void setCores(int cores) {                                                       // количество ядер
        this.cores = cores;
    }

    public void setStreams(int streams) {                                                   // количество потоков
        this.streams = streams;
    }

    public void setBaseFrequency(double baseFrequency) {                                    // базовая частота
        this.baseFrequency = baseFrequency;
    }

    public void setTurboFrequency(double turboFrequency) {                                  // турбо-частота
        this.turboFrequency = turboFrequency;
    }

    public void setTechprocess(int techprocess) {                                           // техпроцесс
        this.techprocess = techprocess;
    }

    public void setTdp(int tdp) {                                                           // тепловыделение
        this.tdp = tdp;
    }

    public void setCache(int cache) {                                                       // кэш L3
        this.cache = cache;
    }

    public void setMaxOzuFrequency(int maxOzuFrequency) {                                   // максимальная частота памяти
        this.maxOzuFrequency = maxOzuFrequency;
    }

    public void setOzuChannels(int ozuChannels) {                                           // количество каналов памяти
        this.ozuChannels = ozuChannels;
    }

    public void setOzuType(String ozuType) {
        this.ozuType = ozuType;
    }

    public void setPciE_version(double pciE_version) {                                      // версия PCI-E
        this.pciE_version = pciE_version;
    }

    public void setGraphics(String graphics) {                                              // встроенная графика
        this.graphics = graphics;
    }

    public void setGraphics_frequency(String graphics_frequency) {                          // частота встроенной графики
        this.graphics_frequency = graphics_frequency;
    }

    public void setRelease_date(String release_date) {                                      // дата релиза
        this.release_date = release_date;
    }

    public void setGeekbench_multi(int geekbench_multi) {                                   // тест в GeekBench (многоядерный)
        this.geekbench_multi = geekbench_multi;
    }

    public void setGeekbench_single(int geekbench_single) {                                 // тест в GeekBench (одноядерный)
        this.geekbench_single = geekbench_single;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setMultiplier(String multiplier) {
        this.multiplier = multiplier;
    }

    // ---------------------------------------------------------------------------------------------
    // геттеры
    // ---------------------------------------------------------------------------------------------


    public String getShipment() {
        return shipment;
    }

    public String getFamily() {                                                                 // семейство процессоров
        return family;
    }

    public String getSocket() {                                                                 // сокет
        return socket;
    }

    public int getCores() {                                                                     // количество ядер
        return cores;
    }

    public int getStreams() {                                                                   // количество потоков
        return streams;
    }

    public double getBaseFrequency() {                                                          // базовая частота
        return baseFrequency;
    }

    public double getTurboFrequency() {                                                         // турбо-частота
        return turboFrequency;
    }

    public int getTechprocess() {                                                               // техпроцесс
        return techprocess;
    }

    public int getTdp() {                                                                       // тепловыделение
        return tdp;
    }

    public int getCache() {                                                                     // кэш L3
        return cache;
    }

    public int getMaxOzuFrequency() {                                                           // максимальная частота памяти
        return maxOzuFrequency;
    }

    public int getOzuChannels() {                                                               // количество каналов памяти
        return ozuChannels;
    }

    public String getOzuType() {
        return ozuType;
    }

    public double getPciE_version() {                                                           // версия PCI-E
        return pciE_version;
    }

    public String getGraphics() {                                                               // встроенная графика
        return graphics;
    }

    public String getGraphics_frequency() {                                                     // частота встроенной графики
        return graphics_frequency;
    }

    public String getRelease_date() {                                                           // дата релиза
        return release_date;
    }

    public int getGeekbench_multi() {                                                           // тест в GeekBench (многоядерный)
        return geekbench_multi;
    }

    public int getGeekbench_single() {                                                          // тест в GeekBench (одноядерный)
        return geekbench_single;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getRatio() {
        return ratio;
    }

    public String getMultiplier() {
        return multiplier;
    }

    // ---------------------------------------------------------------------------------------------
    // переопределенные методы класса Component
    // ---------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Производительность");
        spec.setSpecValue(capacity + " %");

        Log.d("AAA", getCapacity() + "");
        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Цена/качество");
        spec.setSpecValue(ratio + " %");

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Тип поставки");
        spec.setSpecValue(getShipment());

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Основные характеристики", ""});

        if (getProducer() != null) specs.add(new String[]{"Бренд", getProducer()});
        if (getModel() != null) specs.add(new String[]{"Модель", getModel()});
        if (getFamily() != null) specs.add(new String[]{"Ядро", getFamily()});

        specs.add(new String[]{"Цена/качество", getRatio() + "%"});
        if (release_date != null) specs.add(new String[]{"Дата релиза", release_date});
        if (family != null) specs.add(new String[]{"Семейство", family});
        if (socket != null) specs.add(new String[]{"Сокет", socket});
        if (cores != 0) specs.add(new String[]{"Количество ядер", cores + ""});
        if (streams != 0) specs.add(new String[]{"Количество потоков", streams + ""});
        if (baseFrequency != 0) specs.add(new String[]{"Базовая частота", baseFrequency + " МГц"});
        if (turboFrequency != 0) specs.add(new String[]{"Турбо-частота", turboFrequency + " Мгц"});
        if (cache != 0) specs.add(new String[]{"Кэш L3", cache + " МБ"});
        if (techprocess != 0) specs.add(new String[]{"Техпроцесс", techprocess + " нм"});
        if (tdp != 0) specs.add(new String[]{"Тепловыделение", tdp + " Вт"});
        if (pciE_version != 0) specs.add(new String[]{"Версия PCI-E", pciE_version + ""});

        specs.add(new String[]{"Память", ""});
        if (ozuType != null) specs.add(new String[]{"Тип", ozuType});
        if (maxOzuFrequency != 0) specs.add(new String[]{"Макс. частота", maxOzuFrequency + " МГц"});
        if (ozuChannels != 0) specs.add(new String[]{"Количество каналов", ozuChannels + ""});


        if (graphics != null)
        {
            specs.add(new String[]{"Встроенная графика", ""});
            specs.add(new String[]{"Модель", graphics});
            specs.add(new String[]{"Частота", graphics_frequency + ""});
        }

        return specs;
    }
}
