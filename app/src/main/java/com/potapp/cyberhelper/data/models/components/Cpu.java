package com.potapp.cyberhelper.data.models.components;


import android.content.Context;

import androidx.room.Entity;
import androidx.room.Room;

import com.potapp.cyberhelper.data.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

import com.potapp.cyberhelper.data.room.dbs.DB_PROCESSORS;

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

    // память
    private int maxOzuFrequency;                                                                    // максимальная частота
    private int ozuChannels;                                                                        // количество каналов
    private String ozuType;                                                                         // тип памяти

    // прочее
    private double pciE_version;                                                                    // версия PCI-Express
    private String graphics;                                                                        // встроенная графика
    private String graphics_frequency;                                                              // частота графического ядра
    private String release_date;                                                                    // дата релиза

    // бенчмарки
    private int geekbench_multi;                                                                    // тест в Geekbench 5 (Multi)
    private int geekbench_single;                                                                   // тест в Geekbench 5 (Single)

    private double capacity;
    private double ratio;


    // ---------------------------------------------------------------------------------------------
    // сеттеры
    // ---------------------------------------------------------------------------------------------

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

    // ---------------------------------------------------------------------------------------------
    // геттеры
    // ---------------------------------------------------------------------------------------------

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

    // ---------------------------------------------------------------------------------------------
    // дополнительные методы
    // ---------------------------------------------------------------------------------------------

    // производительность процессора в %
    public void calcCapacity(final Context context)
    {
        final double[] best_capacity = {1};

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                double current_capacity;

                for (Cpu CPU : Room.databaseBuilder(context, DB_PROCESSORS.class, "PROCESSORS").build().getMyDao().getCPU())
                {
                    current_capacity = CPU.getGeekbench_multi() + CPU.getGeekbench_single();
                    if (current_capacity > best_capacity[0]) best_capacity[0] = current_capacity;
                }
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double i = (geekbench_multi + geekbench_single) / best_capacity[0] * 100;
        capacity = Math.round(i * 10) / 10;
    }
    // ---------------------------------------------------------------------------------------------

    // соотношение цена/качество в %
    public void calcRatio(final Context context)
    {
        final double[] best_ratio = {1};

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                double current_ratio;

                for (Cpu CPU : Room.databaseBuilder(context, DB_PROCESSORS.class, "PROCESSORS").build().getMyDao().getCPU())
                {
                    current_ratio = CPU.getPrice() / CPU.getCapacity();
                    if (current_ratio > best_ratio[0]) best_ratio[0] = current_ratio;
                }
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double i = (getPrice() / getCapacity()) / best_ratio[0] * 100;
        ratio = Math.round(i * 10) / 10;
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

        spec.setSpecTitle("Дата релиза");
        spec.setSpecValue(getRelease_date());

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Основные характеристики", ""});

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
