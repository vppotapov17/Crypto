package com.potapp.cyberhelper.data.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import com.potapp.cyberhelper.data.models.components.*;

import java.io.Serializable;
import java.util.HashMap;

import com.potapp.cyberhelper.data.room.dbs.DB_BPS;
import com.potapp.cyberhelper.data.room.dbs.DB_CONFIGURATIONS;
import com.potapp.cyberhelper.data.room.dbs.DB_COOLERS;
import com.potapp.cyberhelper.data.room.dbs.DB_HDDS;
import com.potapp.cyberhelper.data.room.dbs.DB_MOTHERBOARDS;
import com.potapp.cyberhelper.data.room.dbs.DB_OZUS;
import com.potapp.cyberhelper.data.room.dbs.DB_CASES;
import com.potapp.cyberhelper.data.room.dbs.DB_PROCESSORS;
import com.potapp.cyberhelper.data.room.dbs.DB_SSDS;
import com.potapp.cyberhelper.data.room.dbs.DB_VIDEOCARDS;

@Entity
public class Configuration implements Serializable {

    public Configuration(){}

    //поля для компонентов
    @PrimaryKey
    @NonNull
    public String name;
    @Embedded (prefix = "cpu")
    public Cpu mCpu;
    @Embedded (prefix = "mb")
    public Mb mMb;
    @Embedded (prefix = "ozu")
    public Ozu mOzu;
    @Embedded (prefix = "gpu")
    public Gpu mGpu;
    @Embedded (prefix = "bp")
    public Bp mBp;
    @Embedded (prefix = "hdd")
    public Hdd mHdd;
    @Embedded (prefix = "ssd_25")
    public Ssd mSsd_25;
    @Embedded (prefix = "ssd_m2")
    public Ssd mSsd_m2;
    @Embedded (prefix = "cooler")
    public Cooler mCooler;
    @Embedded (prefix = "pcCase")
    public Case mCase;

    public boolean isReady;

    public int getFullPrice()
    {
        int price = 0;

        if (mCpu != null) price += mCpu.getPrice();
        if (mMb != null) price += mMb.getPrice();
        if (mOzu != null) price += mOzu.getPrice();
        if (mGpu != null) price += mGpu.getPrice();
        if (mBp != null) price += mBp.getPrice();
        if (mHdd != null) price += mHdd.getPrice() * mHdd.getItem_quantity();
        if (mSsd_25 != null) price += mSsd_25.getPrice() * mSsd_25.getItem_quantity();
        if (mSsd_m2 != null) price += mSsd_m2.getPrice() * mSsd_m2.getItem_quantity();


        if (mCooler != null) price += mCooler.getPrice();
        if (mCase != null) price += mCase.getPrice();

        return price;
    }

    public String addComponent(Component component, final Context context)
    {
        String toastString = component.getName() + " добавлен в сборку!";

        if (component instanceof Mb)
        {
            mMb = (Mb) component;
            toastString = component.getName() + " добавлена в сборку!";
        }
        else if (component instanceof Cpu) mCpu = (Cpu) component;
        else if (component instanceof Gpu)
        {
            mGpu = (Gpu) component;
            toastString = component.getName() + " добавлена в сборку!";
        }
        else if (component instanceof Ozu)
        {
            mOzu = (Ozu) component;
            toastString = component.getName() + " добавлена в сборку!";
        }
        else if (component instanceof Bp) mBp = (Bp) component;
        else if (component instanceof Cooler) mCooler = (Cooler) component;
        else if (component instanceof Case) mCase = (Case) component;
        else if (component instanceof Hdd) mHdd = (Hdd) component;
        else if (component instanceof Ssd) {
            Ssd SSD = (Ssd) component;

            if (SSD.getFormFactor().equals("2.5")) mSsd_25 = SSD;
            else mSsd_m2 = SSD;
        }

        final Configuration configuration = this;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Room.databaseBuilder(context, DB_CONFIGURATIONS.class, "CONFIGURATIONS").build().getMyDao().updateConfiguration(configuration);
            }
        });
        t.start();


        return toastString;
    }

    public String deleteComponent(Component component, final Context context)
    {
        String toastString = component.getName() + " удалён из сборки!";

        if (component instanceof Mb)
        {
            mMb = null;
            mOzu = null;
            mHdd = null;
            mSsd_25 = null;
            mSsd_m2 = null;
            toastString = component.getName() + " удалена из сборки!";
        }
        else if (component instanceof Cpu) mCpu = null;
        else if (component instanceof Gpu)
        {
            mGpu = null;
            toastString = component.getName() + " удалена из сборки!";
        }
        else if (component instanceof Ozu)
        {
            mOzu = null;
            toastString = component.getName() + " удалена из сборки";
        }
        else if (component instanceof Bp) mBp = null;
        else if (component instanceof Cooler) mCooler = null;
        else if (component instanceof Case) mCase = null;
        else if (component instanceof Hdd) mHdd = null;
        else if (component instanceof Ssd)
        {
            Ssd SSD = (Ssd) component;

            if (SSD.getFormFactor().equals("2.5")) mSsd_25 = null;
            else mSsd_m2 = null;
        }

        final Configuration configuration = this;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Room.databaseBuilder(context, DB_CONFIGURATIONS.class, "CONFIGURATIONS").build().getMyDao().updateConfiguration(configuration);
            }
        });
        t.start();

        return toastString;
    }

    // конвертация сборки для хранения в Firebase
    public HashMap<String, String> convertToFirebase() {
        HashMap<String, String> map = new HashMap<>();

        map.put("name", name);
        map.put("mb", mMb.getProduct_code() + "");
        map.put("cpu", mCpu.getProduct_code() + "");
        map.put("gpu", mGpu.getProduct_code() + "");

        map.put("ozuQuantity", mOzu.getItem_quantity() + "");
        map.put("ozuCode", mOzu.getProduct_code() + "");

        map.put("bp", mBp.getProduct_code() + "");
        map.put("cooler", mCooler.getProduct_code() + "");
        map.put("pccase", mCase.getProduct_code() + "");

        if (mHdd != null)
        {
            map.put("hddQuantity", mHdd.getItem_quantity() + "");
            map.put("hddCode", mHdd.getProduct_code() + "");
        }

        if (mSsd_25 != null)
        {
            map.put("ssd25Quantity", mSsd_25.getItem_quantity() + "");
            map.put("ssd25Code", mSsd_25.getProduct_code() + "");
        }

        if (mSsd_m2 != null)
        {
            map.put("ssdM2Quantity", mSsd_m2.getItem_quantity() + "");
            map.put("ssdM2Code", mSsd_m2.getProduct_code() + "");
        }


        return map;
    }

    // обратное преобразование из Firebase в Configuration
    public static Configuration CreateFromFirebase(HashMap<String, String> map, Context context)
    {
        Configuration configuration = new Configuration();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                // имя
                if (map.get("name") != null) configuration.name = map.get("name");

                // материнская плата
                if (map.get("mb") != null)
                {
                    // поиск материнской платы с соответствующим кодом в БД Room
                    for (Mb MB: Room.databaseBuilder(context, DB_MOTHERBOARDS.class, "MOTHERBOARDS").build().getMyDao().getMB())
                    {
                        if (MB.getProduct_code() == Integer.parseInt(map.get("mb"))) {
                            configuration.mMb = MB;
                            break;
                        }
                    }
                }

                // процессор
                if (map.get("cpu") != null)
                {
                    // поиск процессора с соответствующим кодом в БД Room
                    for (Cpu CPU: Room.databaseBuilder(context, DB_PROCESSORS.class, "PROCESSORS").build().getMyDao().getCPU())
                    {
                        if (CPU.getProduct_code() == Integer.parseInt(map.get("cpu"))) {
                            configuration.mCpu = CPU;
                            break;
                        }
                    }
                }

                // видеокарта
                if (map.get("gpu") != null)
                {
                    // поиск видеокарты с соответствующим кодом в БД Room
                    for (Gpu GPU: Room.databaseBuilder(context, DB_VIDEOCARDS.class, "VIDEOCARDS").build().getMyDao().getGPU())
                    {
                        if (GPU.getProduct_code() == Integer.parseInt(map.get("gpu"))) {
                            configuration.mGpu = GPU;
                            break;
                        }
                    }
                }

                // оперативная память
                if (map.get("ozuCode") != null)
                {
                    // поиск ОЗУ с соответствующим кодом в БД Room
                    for (Ozu OZU: Room.databaseBuilder(context, DB_OZUS.class, "OZUS").build().getMyDao().getOZU())
                    {
                        if (OZU.getProduct_code() == Integer.parseInt(map.get("ozuCode"))) {
                            configuration.mOzu = OZU;
                            configuration.mOzu.setItem_quantity(Integer.parseInt(map.get("ozuQuantity")));
                            break;
                        }
                    }
                }

                // блок питания
                if (map.get("bp") != null)
                {
                    // поиск БП с соответствующим кодом в БД Room
                    for (Bp BP: Room.databaseBuilder(context, DB_BPS.class, "BPS").build().getMyDao().getBP())
                    {
                        if (BP.getProduct_code() == Integer.parseInt(map.get("bp"))) {
                            configuration.mBp = BP;
                            break;
                        }
                    }
                }

                // корпус
                if (map.get("pccase") != null)
                {
                    // поиск корпуса с соответствующим кодом в БД Room
                    for (Case PCCASE: Room.databaseBuilder(context, DB_CASES.class, "PCCASES").build().getMyDao().getPCCASE())
                    {
                        if (PCCASE.getProduct_code() == Integer.parseInt(map.get("pccase"))) {
                            configuration.mCase = PCCASE;
                            break;
                        }
                    }
                }

                // кулер
                if (map.get("cooler") != null)
                {
                    // поиск кулера с соответствующим кодом в БД Room
                    for (Cooler COOLER: Room.databaseBuilder(context, DB_COOLERS.class, "COOLERS").build().getMyDao().getCOOLER())
                    {
                        if (COOLER.getProduct_code() == Integer.parseInt(map.get("cooler"))) {
                            configuration.mCooler = COOLER;
                            break;
                        }
                    }
                }

                // жёсткий диск
                if (map.get("hddCode") != null)
                {
                    // поиск HDD с соответствующим кодом в БД Room
                    for (Hdd HDD: Room.databaseBuilder(context, DB_HDDS.class, "HDDS").build().getMyDao().getHDD())
                    {
                        if (HDD.getProduct_code() == Integer.parseInt(map.get("hddCode"))) {
                            configuration.mHdd = HDD;
                            configuration.mHdd.setItem_quantity(Integer.parseInt(map.get("hddQuantity")));
                            break;
                        }
                    }
                }

                // SSD 2.5
                if (map.get("ssd25Code") != null)
                {
                    // поиск SSD 2.5 с соответствующим кодом в БД Room
                    for (Ssd SSD: Room.databaseBuilder(context, DB_SSDS.class, "SSDS").build().getMyDao().getSSD())
                    {
                        if (SSD.getProduct_code() == Integer.parseInt(map.get("ssd25Code"))) {
                            configuration.mSsd_25 = SSD;
                            configuration.mSsd_25.setItem_quantity(Integer.parseInt(map.get("ssd25Quantity")));
                            break;
                        }
                    }
                }

                // SSD M2
                if (map.get("ssdM2Code") != null)
                {
                    // поиск SSD M2 с соответствующим кодом в БД Room
                    for (Ssd SSD: Room.databaseBuilder(context, DB_SSDS.class, "SSDS").build().getMyDao().getSSD())
                    {
                        if (SSD.getProduct_code() == Integer.parseInt(map.get("ssdM2Code"))) {
                            configuration.mSsd_m2 = SSD;
                            configuration.mSsd_25.setItem_quantity(Integer.parseInt(map.get("ssdM2Quantity")));
                            break;
                        }
                    }
                }
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        configuration.isReady = true;
        return configuration;
    }
}
