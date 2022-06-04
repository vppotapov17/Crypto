package com.potapp.cyberhelper.screens.splash;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.potapp.cyberhelper.data.models.User;
import com.potapp.cyberhelper.data.models.components.Bp;
import com.potapp.cyberhelper.data.models.components.Case;
import com.potapp.cyberhelper.data.models.components.Cooler;
import com.potapp.cyberhelper.data.models.components.Cpu;
import com.potapp.cyberhelper.data.models.components.Gpu;
import com.potapp.cyberhelper.data.models.components.Hdd;
import com.potapp.cyberhelper.data.models.components.Mb;
import com.potapp.cyberhelper.data.models.components.Ozu;
import com.potapp.cyberhelper.data.models.components.Ssd;
import com.potapp.cyberhelper.data.room.dbs.DB_BPS;
import com.potapp.cyberhelper.data.room.dbs.DB_CASES;
import com.potapp.cyberhelper.data.room.dbs.DB_COOLERS;
import com.potapp.cyberhelper.data.room.dbs.DB_HDDS;
import com.potapp.cyberhelper.data.room.dbs.DB_MOTHERBOARDS;
import com.potapp.cyberhelper.data.room.dbs.DB_OZUS;
import com.potapp.cyberhelper.data.room.dbs.DB_PROCESSORS;
import com.potapp.cyberhelper.data.room.dbs.DB_SSDS;
import com.potapp.cyberhelper.data.room.dbs.DB_VIDEOCARDS;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SplashViewModel extends AndroidViewModel {

    User current_user;                                                                              // текущий пользователь
    int configurationListSize;                                                                      // количество сохраненных конфигураций в базе

    MutableLiveData<Boolean[]> liveData;
    Boolean array[] = new Boolean[]{false, false};


    public SplashViewModel(@NonNull Application app){
        super(app);


        liveData = new MutableLiveData<>();
        liveData.setValue(array);

        doAuth();

        new Thread(() -> loadData()).start();
    }

    // авторизация пользователя
    private void doAuth(){
        MobileAds.initialize(getApplication().getApplicationContext(), initializationStatus -> {
            Log.d("AAA", "Success");
        });

        // первый запуск приложения
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signInAnonymously();
            current_user = null;
            array[0] = true;
            liveData.postValue(array);
        }
        // анонимный пользователь
        else if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
        {
            current_user = null;
            array[0] = true;
            liveData.postValue(array);
        }
        // авторизованный пользователь
        else
        {
            // получаем данные пользователя и сохраняем в объект User
            current_user = new User();
            current_user.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());              // UID

            // остальные данные (имя, рейтинг и пр.)
            FirebaseFirestore.getInstance().collection("users")
                    .document(current_user.getUID())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists())
                                {
                                    current_user.setName(doc.getData().get("username").toString());
                                    current_user.setRating(Double.parseDouble(doc.getData().get("rating").toString()));
                                    array[0] = true;
                                    liveData.postValue(array);
                                }
                            }
                        }
                    });
        }
    }


    // загрузка информации о комплектующих в базу данных Room
    void loadData(){

        Context context = getApplication().getApplicationContext();

        DB_MOTHERBOARDS db_motherboards = Room.databaseBuilder(context, DB_MOTHERBOARDS.class, "MOTHERBOARDS").build();
        DB_PROCESSORS db_processors = Room.databaseBuilder(context, DB_PROCESSORS.class, "PROCESSORS").build();
        DB_VIDEOCARDS db_videocards = Room.databaseBuilder(context, DB_VIDEOCARDS.class, "VIDEOCARDS").build();
        DB_OZUS db_ozus = Room.databaseBuilder(context, DB_OZUS.class, "OZUS").build();
        DB_BPS db_bps = Room.databaseBuilder(context, DB_BPS.class, "BPS").build();
        DB_CASES db_CASES = Room.databaseBuilder(context, DB_CASES.class, "CASES").build();
        DB_COOLERS db_coolers = Room.databaseBuilder(context, DB_COOLERS.class, "COOLERS").build();
        DB_HDDS db_hdds = Room.databaseBuilder(context, DB_HDDS.class, "HDDS").build();
        DB_SSDS db_ssds = Room.databaseBuilder(context, DB_SSDS.class, "SSDS").build();


        Cpu CPU1 = new Cpu();

        CPU1.setProduct_code(1068556);
        CPU1.setProducer("AMD");
        CPU1.setModel("Ryzen 5 2600");
        CPU1.setPrice(10660);
        CPU1.setSocket("AM4");
        CPU1.setBaseFrequency(3.4);
        CPU1.setTurboFrequency(3.9);
        CPU1.setCores(6);
        CPU1.setStreams(12);
        CPU1.setCache(16);
        CPU1.setTechprocess(12);
        CPU1.setPciE_version(3.0);
        CPU1.setFamily("Pinnacle Ridge");
        CPU1.setTdp(65);
        CPU1.setOzuType("DDR4");
        CPU1.setMaxOzuFrequency(2933);
        CPU1.setOzuChannels(2);
        CPU1.setGraphics("VEGA 11");
        CPU1.setGraphics_frequency("1500");
        CPU1.setRelease_date("Апрель 2018");
        CPU1.setRefLink("https://www.citilink.ru/product/processor-amd-ryzen-5-2600-socketam4-oem-yd2600bbm6iaf-1068556/");

        CPU1.setGeekbench_multi(5351);
        CPU1.setGeekbench_single(977);

        CPU1.calcCapacity(context);
        CPU1.calcRatio(context);


        Cpu CPU2 = new Cpu();
        CPU2.setProduct_code(1640623);
        CPU2.setProducer("Intel");
        CPU2.setModel("Core i5 12400");
        CPU2.setPrice(19290);
        CPU2.setSocket("LGA 1700");
        CPU2.setBaseFrequency(2.5);
        CPU2.setTurboFrequency(4.4);
        CPU2.setCores(6);
        CPU2.setStreams(12);
        CPU2.setCache(18);
        CPU2.setTechprocess(10);
        CPU2.setPciE_version(5.0);
        CPU2.setFamily("Alder Lake");
        CPU2.setTdp(65);
        CPU2.setMaxOzuFrequency(4800);
        CPU2.setOzuType("DDR4/DDR5");
        CPU2.setOzuChannels(2);
        CPU2.setRelease_date("Январь 2022");
        CPU2.setRefLink("https://www.citilink.ru/product/processor-intel-original-core-i5-12400-soc-1700-bx8071512400-s-rl4v-2-1640623/");

        CPU2.setGeekbench_multi(9451);
        CPU2.setGeekbench_single(1647);

        CPU2.calcCapacity(context);
        CPU2.calcRatio(context);

        db_processors.getMyDao().addCPU(CPU1);
        db_processors.getMyDao().addCPU(CPU2);


        Mb MB = new Mb();


        MB.setProduct_code(1120710);
        MB.setProducer("Asrock");
        MB.setModel("A320M DVS R4.0");
        MB.setSocket("AM4");
        MB.setChipset("A320");
        MB.setPrice(3620);
        MB.setSata3(6);
        MB.setFormFactor("mATX");
        MB.setOzuType("DDR4");

        Mb MB2 = new Mb();
        MB2.setProduct_code(1669365);
        MB2.setProducer("Asus");
        MB2.setModel("ROG STRIX B660-F GAMING WIFI");
        MB2.setSocket("LGA1700");
        MB2.setChipset("B660");
        MB2.setPrice(23690);
        MB2.setFormFactor("ATX");
        MB2.setOzuType("DDR4");

        db_motherboards.getMyDao().addMB(MB);
        db_motherboards.getMyDao().addMB(MB2);


        Gpu GPU = new Gpu();

        GPU.setProduct_code(1658278);
        GPU.setProducer("Palit");
        GPU.setModel("GeForce RTX 3050");
        GPU.setPrice(43990);
        GPU.setRefLink("https://www.citilink.ru/product/videokarta-palit-pci-e-pa-rtx3050-dual-nv-rtx3050-8192mb-128-gddr6-155-1658278/properties/");

        GPU.setBaseFrequency(1552);
        GPU.setTurboFrequency(1777);
        GPU.setTechprocess(8);
        GPU.setPciE_version(4.0);
        GPU.setEnergyConsumption(140);

        GPU.setMemorySize(8);
        GPU.setMemoryType("GDDR6");
        GPU.setMemoryFrequency(14000);
        GPU.setBitDepth(128);

        GPU.setMaxResoultion("7680x4320");
        GPU.setRayTracing(true);
        GPU.setOptionalPower("8pin");

        db_videocards.getMyDao().addGPU(GPU);

        GPU.setProduct_code(1542015);
        GPU.setProducer("Palit");
        GPU.setModel("GeForce RTX 3060Ti");
        GPU.setPrice(85990);
        GPU.setRefLink("https://www.citilink.ru/product/videokarta-palit-pci-e-4-0-pa-rtx3060ti-dual-8g-v1-lhr-nv-rtx3060ti-81-1542015/?text=3060+ti+pal");

        GPU.setBaseFrequency(1410);
        GPU.setTurboFrequency(1665);
        GPU.setTechprocess(8);
        GPU.setPciE_version(4.0);
        GPU.setEnergyConsumption(200);

        GPU.setMemorySize(8);
        GPU.setMemoryType("GDDR6");
        GPU.setMemoryFrequency(14000);
        GPU.setBitDepth(256);

        GPU.setMaxResoultion("7680x4320");
        GPU.setRayTracing(true);
        GPU.setOptionalPower("8pin");

        db_videocards.getMyDao().addGPU(GPU);


        Ozu OZU = new Ozu();

        OZU.setProduct_code(1622267);
        OZU.setProducer("Corsair");
        OZU.setModel("Fury Renegade");
        OZU.setPrice(6090);
        OZU.setRefLink("https://www.citilink.ru/product/pamyat-ddr4-2x8gb-3200mhz-corsair-cmk16gx4m2e3200c16-vengeance-lpx-rtl-1622267/");
        OZU.setType("DDR4");

        OZU.setCapacity(16);
        OZU.setModulesQuantity(2);
        OZU.setFrequency(3200);
        OZU.setLatency("CL19");

        OZU.setECC(true);
        OZU.setBacklight(false);
        OZU.setRadiator(true);

        db_ozus.getMyDao().addOZU(OZU);

        OZU.setProduct_code(1622267);
        OZU.setProducer("AMD");
        OZU.setModel("Radeon R7 Performance Series");
        OZU.setPrice(2390);
        OZU.setRefLink("https://www.citilink.ru/product/modul-pamyati-amd-radeon-r7-performance-series-r748g2400u2s-uo-ddr4-8g-1007258/");
        OZU.setType("DDR4");

        OZU.setCapacity(8);
        OZU.setModulesQuantity(1);
        OZU.setFrequency(2400);
        OZU.setLatency("CL16");

        OZU.setECC(true);
        OZU.setBacklight(false);
        OZU.setRadiator(false);

        db_ozus.getMyDao().addOZU(OZU);


        Bp BP = new Bp();

        BP.setProducer("Thermaltake");
        BP.setProduct_code(365400);
        BP.setModel("TR2 S");
        BP.setCapacity(650);
        BP.setEfficiency(86);
        BP.setActivePFC(true);
        BP.setCertificate("Bronze");
        BP.setPrice((int)Math.random() % 26000 + 1000);

        for (int i = 0; i < 100; ++i) db_bps.getMyDao().addBP(BP);


        Case PCCASE = new Case();
        PCCASE.setProduct_code(1076328);
        PCCASE.setProducer("Aerocool");
        PCCASE.setModel("Cylon Mini");
        PCCASE.setPrice((int)Math.random() % 26000 + 1500);

        db_CASES.getMyDao().addPCCASE(PCCASE);


        Cooler COOLER = new Cooler();

        COOLER.setProduct_code(1161762);
        COOLER.setProducer("DeepCool");
        COOLER.setModel("Gammax 400 Blue Basic");
        COOLER.setPrice((int)Math.random() % 6000 + 150);
        COOLER.setTdp(150);

        ArrayList<Integer> noise = new ArrayList<>();
        noise.add(23);
        noise.add(27);

        COOLER.setNoiseLevel(noise);

        ArrayList<Integer> rspeed = new ArrayList<>();
        rspeed.add(1800);
        rspeed.add(2500);
        COOLER.setRotationSpeed(rspeed);

        db_coolers.getMyDao().addCOOLER(COOLER);



        Hdd HDD = new Hdd();

        HDD.setProduct_code(692114);
        HDD.setProducer("WD");
        HDD.setModel("Caviar Blue");
        HDD.setPrice((int) Math.random() % 50000 + 2000);

        db_hdds.getMyDao().addHDD(HDD);


        Ssd SSD25 = new Ssd();
        Ssd M2 = new Ssd();

        SSD25.setProduct_code(420251);
        SSD25.setProducer("Kingston");
        SSD25.setModel("A400");
        SSD25.setPrice((int)Math.random() % 30000 + 1500);
        SSD25.setFormFactor("2.5");

        M2.setProduct_code(1529537);
        M2.setProducer("Kingston");
        M2.setModel("KC2500");
        M2.setPrice((int)Math.random() % 50000 + 1500);
        M2.setFormFactor("M2");

        db_ssds.getMyDao().addSSD(SSD25);
        db_ssds.getMyDao().addSSD(M2);

        array[1] = true;
        liveData.postValue(array);
    }

}
