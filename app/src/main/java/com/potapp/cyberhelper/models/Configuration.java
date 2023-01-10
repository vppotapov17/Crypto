package com.potapp.cyberhelper.models;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Room;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.database.dbs.DB_COMPONENTS;
import com.potapp.cyberhelper.models.components.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;
import com.squareup.okhttp.internal.DiskLruCache;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.subjects.PublishSubject;

@Entity
public class Configuration implements Serializable {

    public Configuration(){
        //currentPriceSubject = PublishSubject.create();
    }

    //@Ignore
    //private PublishSubject<Integer> currentPriceSubject;

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


//    public PublishSubject<Integer> getCurrentPriceSubject(){
//        return null;
//    }

    public int getFullPrice()
    {
        int price = 0;

        if (mCpu != null) price += mCpu.getPrice();
        if (mMb != null) price += mMb.getPrice();
        if (mOzu != null) price += mOzu.getPrice() * mOzu.getItemQuantity();
        if (mGpu != null) price += mGpu.getPrice();
        if (mBp != null) price += mBp.getPrice();
        if (mHdd != null) price += mHdd.getPrice() * mHdd.getItemQuantity();
        if (mSsd_25 != null) price += mSsd_25.getPrice() * mSsd_25.getItemQuantity();
        if (mSsd_m2 != null) price += mSsd_m2.getPrice() * mSsd_m2.getItemQuantity();


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
        else if (component instanceof Hdd){
            Hdd HDD = (Hdd) component;
            if (mSsd_25 != null){
                if (mSsd_25.getItemQuantity() == mMb.getSata3()) mSsd_25.setItemQuantity(mSsd_25.getItemQuantity() - 1);
            }
            mHdd = HDD;
        }
        else if (component instanceof Ssd) {
            Ssd SSD = (Ssd) component;

            if (SSD.getFormFactor().equals("2.5")){

                if (mHdd != null){
                    if (mHdd.getItemQuantity() == mMb.getSata3()) mHdd.setItemQuantity(mHdd.getItemQuantity() - 1);
                }

                mSsd_25 = SSD;
            }
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

        //currentPriceSubject.onNext(getFullPrice());

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

        //currentPriceSubject.onNext(getFullPrice());

        return toastString;
    }

    // проверка конфигуарции на наличие обязательных комплектующих
    public boolean isReady()
    {
        boolean isReady = true;

        if (mCpu == null) isReady = false;
        if (mGpu == null) isReady = false;
        if (mMb == null) isReady = false;
        if (mBp == null) isReady = false;
        if (mOzu == null) isReady = false;
        if (mCooler == null) isReady = false;

        boolean isStorage = false;                                                                  // наличие хотя бы одного накопителя

        if (mHdd != null) isStorage = true;
        if (mSsd_25 != null) isStorage = true;
        if (mSsd_m2 != null) isStorage = true;

        if (!isStorage) isReady = false;

        return isReady;
    }

    // конвертация сборки для хранения в Firebase
    public HashMap<String, HashMap<String, String>> convertToFirebase() {
        HashMap<String, HashMap<String, String>> map = new HashMap<>();

        map.put("cpu", mCpu.toFirebase());
        map.put("mb", mMb.toFirebase());
        map.put("ozu", mOzu.toFirebase());
        map.put("bp", mBp.toFirebase());
        map.put("case", mCase.toFirebase());
        map.put("cooler", mCooler.toFirebase());

        if (mHdd != null) map.put("hdd", mHdd.toFirebase());
        if (mSsd_25 != null) map.put("ssd25", mSsd_25.toFirebase());
        if (mSsd_m2 != null) map.put("ssdM2", mSsd_m2.toFirebase());

        return map;
    }


    public static Configuration createFromSnapshot(DataSnapshot snapshot){
        Configuration configuration = new Configuration();
        configuration.mCpu = Cpu.createFromSnapshot(snapshot.child("cpu"), false);
        configuration.mMb = Mb.createFromSnapshot(snapshot.child("mb"));
        configuration.mGpu = Gpu.createFromSnapshot(snapshot.child("gpu"));
        configuration.mBp = Bp.createFromSnapshot(snapshot.child("bp"));
        configuration.mOzu = Ozu.createFromSnapshot(snapshot.child("ozu"));
        configuration.mCase = Case.createFromSnapshot(snapshot.child("case"));
        configuration.mCooler = Cooler.createFromSnapshot(snapshot.child("cooler"));
        configuration.mHdd = Hdd.createFromSnapshot(snapshot.child("hdd"));
        configuration.mSsd_25 = Ssd.createFromSnapshot(snapshot.child("ssdM2"));
        configuration.mSsd_m2 = Ssd.createFromSnapshot(snapshot.child("ssd25"));

        return configuration;
    }

    // обратное преобразование из Firebase в Configuration
    public static Configuration CreateFromFirebase(HashMap<String, String> map, DB_COMPONENTS db_components)
    {
        Configuration configuration = new Configuration();

        // имя
        if (map.get("name") != null) configuration.name = map.get("name");

        // материнская плата
        if (map.get("mb") != null)
        {
            boolean found = false;
            int mbCode = Integer.parseInt(map.get("mb"));
            // поиск материнской платы с соответствующим кодом в БД Room
            for (Mb MB: db_components.getMotherboardsDao().getMB())
            {
                if (MB.getProduct_code() == mbCode) {
                    configuration.mMb = MB;
                    found = true;
                    break;
                }
                if (!found) return null;
            }
        }

        // процессор
        if (map.get("cpu") != null)
        {
            // поиск процессора с соответствующим кодом в БД Room
            for (Cpu CPU: db_components.getProcessorsDao().getCPU())
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
            for (Gpu GPU: db_components.getVideocardsDao().getGPU())
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
            for (Ozu OZU: db_components.getOzusDao().getOZU())
            {
                if (OZU.getProduct_code() == Integer.parseInt(map.get("ozuCode"))) {
                    configuration.mOzu = OZU;
                    configuration.mOzu.setItemQuantity(Integer.parseInt(map.get("ozuQuantity")));
                    break;
                }
            }
        }

        // блок питания
        if (map.get("bp") != null)
        {
            // поиск БП с соответствующим кодом в БД Room
            for (Bp BP: db_components.getBpsDao().getBP())
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
            for (Case PCCASE: db_components.getCasesDao().getCASE())
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
            for (Cooler COOLER: db_components.getCoolersDao().getCOOLER())
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
            for (Hdd HDD: db_components.getHddsDao().getHDD())
            {
                if (HDD.getProduct_code() == Integer.parseInt(map.get("hddCode"))) {
                    configuration.mHdd = HDD;
                    configuration.mHdd.setItemQuantity(Integer.parseInt(map.get("hddQuantity")));
                    break;
                }
            }
        }

        // SSD 2.5
        if (map.get("ssd25Code") != null)
        {
            // поиск SSD 2.5 с соответствующим кодом в БД Room
            for (Ssd SSD: db_components.getSsdsDao().getSSD())
            {
                if (SSD.getProduct_code() == Integer.parseInt(map.get("ssd25Code"))) {
                    configuration.mSsd_25 = SSD;
                    configuration.mSsd_25.setItemQuantity(Integer.parseInt(map.get("ssd25Quantity")));
                    break;
                }
            }
        }

        // SSD M2
        if (map.get("ssdM2Code") != null)
        {
            // поиск SSD M2 с соответствующим кодом в БД Room
            for (Ssd SSD: db_components.getSsdsDao().getSSD())
            {
                if (SSD.getProduct_code() == Integer.parseInt(map.get("ssdM2Code"))) {
                    configuration.mSsd_m2 = SSD;
                    configuration.mSsd_25.setItemQuantity(Integer.parseInt(map.get("ssdM2Quantity")));
                    break;
                }
            }
        }


        configuration.isReady = true;
        return configuration;
    }
}
