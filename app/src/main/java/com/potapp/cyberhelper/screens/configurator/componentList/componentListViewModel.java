package com.potapp.cyberhelper.screens.configurator.componentList;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.database.dbs.DB_COMPONENTS;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.models.componentFilters.MbFilter;
import com.potapp.cyberhelper.models.components.*;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class componentListViewModel extends AndroidViewModel {

    private static final String COMPONENTS_FEED_URL = "https://feeds.advcake.com/feed/download/bdd07c79189c41ac56bd8ff2d2eeb6dc";

    LayoutInflater inflater;

    DB_COMPONENTS db_components;                                                                    // база данных комплектующих

    MbFilter currentFilter_mb;

    Configuration current_configuration;                                                            // текущая конфигурация
    Class component_class;                                                                          // класс текущего компонента
    private MutableLiveData<List<Component>> componentListLiveData;
    private MutableLiveData<List<NativeAd>> adListLiveData;

    List<Component> baseComponentList;                                                              // базовый список компонентов (без фильтров)
    List<Component> filterComponentList;                                                            // список компонентов с учётом фильтров

    public componentListViewModel(@NonNull Application app, LayoutInflater inflater, Configuration current_configuration, Class component_class) {
        super(app);

        // база данных комплектующих
        db_components = Room.databaseBuilder(getApplication().getApplicationContext(), DB_COMPONENTS.class, "COMPONENTS").build();


        this.inflater = inflater;
        this.currentFilter_mb = new MbFilter();
        this.current_configuration = current_configuration;
        this.component_class = component_class;


        baseComponentList = new ArrayList<>();
        filterComponentList = new ArrayList<>();


        componentListLiveData = new MutableLiveData<>();
        adListLiveData = new MutableLiveData<>();

        Callable<Boolean> isDataExist;

        // заполнение базового списка компонентами текущего типа
        Callable<List<Component>> getComponentList;


        if (component_class == Mb.class)
        {
            isDataExist = ()-> !db_components.getMotherboardsDao().getMB().isEmpty();

            getComponentList = () -> {
                List<Mb> mbs = db_components.getMotherboardsDao().getMB();
                for (Mb MB : mbs)
                {
                    if (isMb_correct(MB)) baseComponentList.add(MB);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else if (component_class == Cpu.class)
        {
            isDataExist = ()-> !db_components.getProcessorsDao().getCPU().isEmpty();

            getComponentList = ()->{
                List<Cpu> cpus = db_components.getProcessorsDao().getCPU();

                for (Cpu CPU : cpus)
                {
                    if (isCpu_correct(CPU))
                        baseComponentList.add(CPU);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else if (component_class == Gpu.class)
        {
            isDataExist = ()-> !db_components.getVideocardsDao().getGPU().isEmpty();

            getComponentList = ()->{
                List<Gpu> gpus = db_components.getVideocardsDao().getGPU();
                for (Gpu GPU : gpus)
                {
                    if (isGpu_correct(GPU)) baseComponentList.add(GPU);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else if (component_class == Ozu.class)
        {
            isDataExist = ()-> !db_components.getOzusDao().getOZU().isEmpty();

            getComponentList = ()->{
                List<Ozu> ozus = db_components.getOzusDao().getOZU();
                for (Ozu OZU : ozus)
                {
                    if (isOzu_correct(OZU)) baseComponentList.add(OZU);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else if (component_class == Bp.class)
        {
            isDataExist = ()-> !db_components.getBpsDao().getBP().isEmpty();

            getComponentList = ()->{
                List<Bp> bps = db_components.getBpsDao().getBP();

                for (Bp BP : bps) {
                    if (isBp_correct(BP)) baseComponentList.add(BP);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else if (component_class == Case.class)
        {
            isDataExist = ()-> !db_components.getCasesDao().getCASE().isEmpty();

            getComponentList = ()-> {
                List<Case> cases = db_components.getCasesDao().getCASE();
                for (Case CASE : cases) {
                    if (isPccase_correct(CASE)) baseComponentList.add(CASE);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };

        }
        else if (component_class == Cooler.class)
        {
            isDataExist = ()-> !db_components.getCoolersDao().getCOOLER().isEmpty();

            getComponentList = ()->{
                List<Cooler> coolers = db_components.getCoolersDao().getCOOLER();
                for (Cooler COOLER : coolers)
                {
                    if (isCooler_correct(COOLER))
                        baseComponentList.add(COOLER);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else if (component_class == Hdd.class){
            isDataExist = ()-> !db_components.getHddsDao().getHDD().isEmpty();

            getComponentList = ()->{
                List<Hdd> hdds = db_components.getHddsDao().getHDD();
                for (Hdd HDD : hdds)
                {
                    if (isHdd_correct(HDD)) baseComponentList.add(HDD);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else if (component_class == Ssd.class){
            isDataExist = ()-> !db_components.getSsdsDao().getSSD().isEmpty();

            getComponentList = ()->{
                List<Ssd> ssds = db_components.getSsdsDao().getSSD();
                for (Ssd SSD : ssds)
                {
                    if (isSsd25_correct(SSD)) baseComponentList.add(SSD);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }

        // SSD M2
        else if (component_class == Component.class) {
            isDataExist = ()-> !db_components.getSsdsDao().getSSD().isEmpty();

            getComponentList = ()->{
                List<Ssd> ssds = db_components.getSsdsDao().getSSD();
                for (Ssd SSD : ssds)
                {
                    if (isSsdM2_correct(SSD)) baseComponentList.add(SSD);
                }

                filterComponentList.addAll(baseComponentList);

                return filterComponentList;
            };
        }
        else {
            isDataExist = ()-> true;

            getComponentList = ()->{
                filterComponentList.clear();
                return filterComponentList;
            };
        }

        Observable.fromCallable(isDataExist)
                .subscribeOn(Schedulers.io())
                .subscribe((aBoolean -> {
                    if (aBoolean) {
                        Observable.fromCallable(getComponentList)
                                .subscribeOn(Schedulers.computation())
                                .subscribe((components -> {
                                    componentListLiveData.postValue(components);
                                }));
                    }
                    else {
                        loadDataFromFirebase(getComponentList);
                    }
                }));


    }

    private void loadDataFromFirebase(Callable<List<Component>> getComponentList){
        if (component_class == Mb.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/Motherboards").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Mb> mbList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){

                        Mb mb = new Mb();

                        mb.setProduct_code(Integer.parseInt(snap.getKey()));
                        mb.setProducer(snap.child("Producer").getValue().toString());
                        mb.setModel(snap.child("Model").getValue().toString());
                        try {
                            mb.setSocket(snap.child("Socket").getValue().toString());
                        }
                        catch (Exception e){
                            Log.d("AAA", mb.getProduct_code() + "");
                        }
                        mb.setRefLink(snap.child("Url").getValue().toString());
                        mb.setPictureLink(snap.child("Picture").getValue().toString());
                        mb.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        mb.setChipset(snap.child("Chipset").getValue().toString());
                        mb.setOzuType(snap.child("OzuType").getValue().toString());
                        mb.setOzuSlotsQuantity(Integer.parseInt(snap.child("OzuSlots").getValue().toString()));
                        mb.setMaxOzuSize(Integer.parseInt(snap.child("MaxOzuSize").getValue().toString()));
                        mb.setOzuFrequencySpec(Integer.parseInt(snap.child("OzuFrequencySpec").getValue().toString()));
                        mb.setPciE_x1(Integer.parseInt(snap.child("PciE_x1").getValue().toString()));
                        mb.setPciEv3_x16(Integer.parseInt(snap.child("PciEv3_x16").getValue().toString()));
                        mb.setPciEv4_x16(Integer.parseInt(snap.child("PciEv4_x16").getValue().toString()));
                        mb.setSata3(Integer.parseInt(snap.child("Sata3").getValue().toString()));
                        mb.setM2(Integer.parseInt(snap.child("M2").getValue().toString()));
                        try {
                            mb.setPs2(snap.child("PS2").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        mb.setUsb20(Integer.parseInt(snap.child("Usb20").getValue().toString()));
                        mb.setUsb30(Integer.parseInt(snap.child("Usb30").getValue().toString()));
                        mb.setUsb31(Integer.parseInt(snap.child("Usb31").getValue().toString()));
                        mb.setUsbC(Integer.parseInt(snap.child("UsbC").getValue().toString()));
                        mb.setDisplayPort(Integer.parseInt(snap.child("DisplayPort").getValue().toString()));
                        mb.setVga(Integer.parseInt(snap.child("VGA").getValue().toString()));
                        mb.setHdmi(Integer.parseInt(snap.child("HDMI").getValue().toString()));
                        mb.setNetwork(snap.child("Network").getValue().toString());
                        mb.setSound(snap.child("Sound").getValue().toString());
                        mb.setFormFactor(snap.child("FormFactor").getValue().toString());

                        try {
                            mb.setPower(snap.child("Power").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        mbList.add(mb);
                    }

                    Completable addMBsToRoom = Completable.fromRunnable(()->{
                        for (Mb MB : mbList)
                            db_components.getMotherboardsDao().addMB(MB);


                    });

                    addMBsToRoom
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                componentListLiveData.postValue(components);
                            });
                        });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    componentListLiveData.postValue(null);
                }
            });
        }
        else if (component_class == Cpu.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/CPUs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Cpu> cpuList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){

                        Cpu cpu = new Cpu();

                        cpu.setProduct_code(Integer.parseInt(snap.getKey()));
                        cpu.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        cpu.setProducer(snap.child("Producer").getValue().toString());
                        cpu.setModel(snap.child("Model").getValue().toString());
                        cpu.setSocket(snap.child("Socket").getValue().toString());
                        cpu.setRefLink(snap.child("Url").getValue().toString());
                        cpu.setPictureLink(snap.child("Picture").getValue().toString());
                        cpu.setCores(Integer.parseInt(snap.child("Cores").getValue().toString()));
                        cpu.setStreams(Integer.parseInt(snap.child("Streams").getValue().toString()));
                        cpu.setBaseFrequency(Double.parseDouble(snap.child("BaseFrequency").getValue().toString()));
                        cpu.setTurboFrequency(Double.parseDouble(snap.child("TurboFrequency").getValue().toString()));
                        cpu.setCache(Integer.parseInt(snap.child("Cache").getValue().toString()));
                        cpu.setTechprocess(Integer.parseInt(snap.child("Techprocess").getValue().toString()));
                        cpu.setMultiplier(snap.child("Multiplier").getValue().toString());
                        cpu.setTdp(Integer.parseInt(snap.child("Tdp").getValue().toString()));
                        cpu.setOzuType(snap.child("OzuType").getValue().toString());
                        cpu.setMaxOzuFrequency(Integer.parseInt(snap.child("MaxOzuFrequency").getValue().toString()));
                        cpu.setOzuChannels(Integer.parseInt(snap.child("OzuChannels").getValue().toString()));
                        cpu.setPciE_version(Double.parseDouble(snap.child("PciE_version").getValue().toString()));

                        if (!snap.child("Graphics").getValue().toString().equals("null") && snap.child("Grpahics").getValue() != null) {
                            cpu.setGraphics(snap.child("Graphics").getValue().toString());
                            cpu.setGraphics_frequency(snap.child("GraphicsFrequency").getValue().toString());
                        }

                        try {
                            cpu.setFamily(snap.child("Family").getValue().toString());
                        }
                        catch (Exception e){
                        }
                        cpu.setGeekbench_multi(Integer.parseInt(snap.child("Geekbench_multi").getValue().toString()));
                        cpu.setGeekbench_single(Integer.parseInt(snap.child("Geekbench_single").getValue().toString()));
                        cpu.setShipment(snap.child("Shipment").getValue().toString());
                        cpuList.add(cpu);
                    }

                    Completable addCpusToRoom = Completable.fromRunnable(()->{


                        // определение лучшей производительности, лучшего соотношения ц/к
                        double maxCapacity = 0;
                        double maxRatio = 0;

                        for (Cpu cpu : cpuList){

                            double capacity = cpu.getGeekbench_multi() + cpu.getGeekbench_single();
                            double ratio = capacity / cpu.getPrice();

                            if (capacity > maxCapacity) maxCapacity = capacity;
                            if (ratio > maxRatio) maxRatio = ratio;
                        }

                        for (Cpu cpu : cpuList) {

                            double capacity = cpu.getGeekbench_multi() + cpu.getGeekbench_single();
                            double ratio = capacity / cpu.getPrice();

                            capacity = (capacity / maxCapacity) * 100;
                            capacity = (double) Math.round(capacity * 10) / 10;

                            ratio = ratio / maxRatio * 100;


                            ratio = Math.round(ratio * 10);
                            ratio /= 10;

                            cpu.setCapacity(capacity);
                            cpu.setRatio(ratio);

                            db_components.getProcessorsDao().addCPU(cpu);
                        }
                    });

                    addCpusToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    componentListLiveData.postValue(null);
                }
            });
        }
        else if (component_class == Gpu.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/GPUs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Gpu> gpuList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){

                        Gpu gpu = new Gpu();

                        gpu.setProduct_code(Integer.parseInt(snap.getKey()));
                        gpu.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        gpu.setProducer(snap.child("Producer").getValue().toString());
                        gpu.setModel(snap.child("Model").getValue().toString());
                        gpu.setRefLink(snap.child("Url").getValue().toString());
                        gpu.setPictureLink(snap.child("Picture").getValue().toString());

                        gpu.setGpProducer(snap.child("GpProducer").getValue().toString());
                        gpu.setGpModel(snap.child("GpModel").getValue().toString());
                        gpu.setBench(Integer.parseInt(snap.child("Bench").getValue().toString()));
                        gpu.setBitDepth(Integer.parseInt(snap.child("BitDepth").getValue().toString()));

                        try {
                            if (snap.child("Interface").getValue() != null) gpu.setGpInterface(snap.child("Interface").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        try {
                            gpu.setGpFrequency(Integer.parseInt(snap.child("GpFrequency").getValue().toString()));
                        }catch (Exception e){
                            Log.d("AAA", snap.getKey());
                        }
                        try {
                            gpu.setGpBoostFrequency(Integer.parseInt(snap.child("GpBoostFrequency").getValue().toString()));
                        }catch (NullPointerException e){}

                        try {
                            gpu.setTechprocess(Integer.parseInt(snap.child("Techprocess").getValue().toString()));
                        }catch (NullPointerException e){
                            Log.d("AAA", snap.getKey());
                        }

                        try {
                            gpu.setMaxResoultion(snap.child("MaxResolution").getValue().toString());
                        }catch (NullPointerException e){}
                        try {
                            gpu.setCoolingSystem(snap.child("CoolingSystem").getValue().toString());
                        }catch (NullPointerException e){}
                        try {
                            gpu.setDvi(Integer.parseInt(snap.child("Dvi").getValue().toString()));
                        }catch (NullPointerException e){}
                        try {
                            gpu.setHdmi(Integer.parseInt(snap.child("Hdmi").getValue().toString()));
                        }catch (NullPointerException e){}
                        try {
                            gpu.setHdmiVer(snap.child("HdmiVer").getValue().toString());
                        }catch (NullPointerException e){}
                        try {
                            gpu.setDisplayPort(Integer.parseInt(snap.child("DisplayPort").getValue().toString()));
                        }catch (NullPointerException e){}
                        try {
                            gpu.setDisplayPortVer(snap.child("DisplayPortVer").getValue().toString());
                        }catch (NullPointerException e){}
                        try {
                            gpu.setOptionalPower(snap.child("OptionalPower").getValue().toString());
                        }catch (NullPointerException e){}

                        gpu.setOzuType(snap.child("OzuType").getValue().toString());
                        gpu.setOzuSize(Integer.parseInt(snap.child("OzuSize").getValue().toString()));
                        gpu.setOzuFrequency(Integer.parseInt(snap.child("OzuFrequency").getValue().toString()));

                        try {
                            gpu.setMaxTdp(Integer.parseInt(snap.child("MaxTdp").getValue().toString()));
                        }catch (NullPointerException e){
                            Log.d("AAA", snap.getKey());
                        }

                        try {
                            if (snap.child("DLSS").getValue().toString().equals("есть")) gpu.setDlss(true);
                            else gpu.setDlss(false);
                        }catch (NullPointerException e){}

                        try {
                            if (snap.child("RayTracing").getValue().toString().equals("есть"))
                                gpu.setRayTracing(true);
                            else gpu.setRayTracing(false);
                        }catch (NullPointerException e){}

                        try {
                            gpu.setLength((int)Double.parseDouble(snap.child("Length").getValue().toString()));
                        }catch (NullPointerException e){}
                        try {
                            gpu.setDate(snap.child("Date").getValue().toString());
                        }catch (NullPointerException e){}
                        try {
                            gpu.setHeatPipes(snap.child("HeatPipes").getValue().toString());
                        }catch (NullPointerException e){}
                        try {
                            gpu.setMinBp(Integer.parseInt(snap.child("MinBp").getValue().toString()));
                        }catch (NullPointerException e){}
                        try {
                            gpu.setMonitors(Integer.parseInt(snap.child("Monitors").getValue().toString()));
                        }catch (NullPointerException e){}

                        if (snap.child("OverClockEdition").getValue().toString().equals("есть")) gpu.setOCEdition(true);
                        else gpu.setOCEdition(false);

                        try {
                            gpu.setRTCores(Integer.parseInt(snap.child("RTCores").getValue().toString()));
                        }catch (NullPointerException e){}

                        try {
                            gpu.setTensorCores(Integer.parseInt(snap.child("TensorCores").getValue().toString()));
                        }catch (NullPointerException e){}

                        try {
                            gpu.setRasterizeBlocks(Integer.parseInt(snap.child("RasterizeBlocks").getValue().toString()));
                        }catch (NullPointerException e){}

                        try {
                            gpu.setTextureBlocks(Integer.parseInt(snap.child("TextureBlocks").getValue().toString()));
                        }catch (NullPointerException e){}

                        gpuList.add(gpu);
                    }

                    Completable addGpusToRoom = Completable.fromRunnable(()->{

                        for (Gpu gpu : gpuList){

                            db_components.getVideocardsDao().addGPU(gpu);
                        }
                    });

                    addGpusToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    componentListLiveData.postValue(null);
                }
            });
        }
        else if (component_class == Ozu.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/OZUs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Ozu> ozuList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){

                        Ozu ozu = new Ozu();

                        ozu.setProduct_code(Integer.parseInt(snap.getKey()));
                        ozu.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        ozu.setProducer(snap.child("Producer").getValue().toString());
                        ozu.setModel(snap.child("Model").getValue().toString());
                        try {
                            ozu.setFamily(snap.child("Family").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        ozu.setRefLink(snap.child("Url").getValue().toString());
                        ozu.setPictureLink(snap.child("Picture").getValue().toString());

                        ozu.setType(snap.child("Type").getValue().toString());
                        ozu.setSingleCapacity(Integer.parseInt(snap.child("SingleCapacity").getValue().toString()));
                        ozu.setModulesQuantity(Integer.parseInt(snap.child("ModulesQuantity").getValue().toString()));
                        ozu.setFrequency(Integer.parseInt(snap.child("Frequency").getValue().toString()));

                        try {
                            ozu.setLatency(snap.child("Latency").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        if (snap.child("ECC").getValue().toString().equals("есть")) ozu.setECC(true);
                        else ozu.setECC(false);

                        if (snap.child("Radiator").getValue().toString().equals("есть")) ozu.setRadiator(true);
                        else ozu.setRadiator(false);

                        ozuList.add(ozu);
                    }

                    Completable addOzusToRoom = Completable.fromRunnable(()->{

                        for (Ozu ozu : ozuList){
                            db_components.getOzusDao().addOZU(ozu);
                        }
                    });

                    addOzusToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    componentListLiveData.postValue(null);
                }
            });
        }
        else if (component_class == Bp.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/BPs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Bp> bpList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){

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

                        bpList.add(bp);
                    }

                    Completable addBPsToRoom = Completable.fromRunnable(()->{

                        for (Bp bp : bpList){
                            db_components.getBpsDao().addBP(bp);
                        }
                    });

                    addBPsToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    componentListLiveData.postValue(null);
                }
            });
        }
        else if (component_class == Case.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/Cases").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Case> caseList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Case caseItem = new Case();

                        caseItem.setProduct_code(Integer.parseInt(snap.getKey()));
                        caseItem.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        caseItem.setProducer(snap.child("Producer").getValue().toString());
                        caseItem.setModel(snap.child("Model").getValue().toString());

                        caseItem.setRefLink(snap.child("Url").getValue().toString());
                        caseItem.setPictureLink(snap.child("Picture").getValue().toString());

                        if (snap.child("Size").exists()) caseItem.setFormFactor(snap.child("Size").getValue().toString());
                        if (snap.child("BpLocation").exists()) caseItem.setBpPosition(snap.child("BpLocation").getValue().toString());
                        if (snap.child("Sections525").exists()) caseItem.setSections525(Integer.parseInt(snap.child("Sections525").getValue().toString()));
                        if (snap.child("Hdd35").exists()) caseItem.setSections35(Integer.parseInt(snap.child("Hdd35").getValue().toString()));
                        if (snap.child("Sections25").exists()) caseItem.setSections25(Integer.parseInt(snap.child("Sections25").getValue().toString()));
                        if (snap.child("SlotsQuantity").exists()) caseItem.setSlotsQuantity(Integer.parseInt(snap.child("SlotsQuantity").getValue().toString()));
                        if (snap.child("Usb20").exists()) caseItem.setUsb20(Integer.parseInt(snap.child("Usb20").getValue().toString()));
                        if (snap.child("Usb30").exists()) caseItem.setUsb30(Integer.parseInt(snap.child("Usb30").getValue().toString()));
                        if (snap.child("Audio").exists()) caseItem.setAudio(snap.child("Audio").getValue().toString());

                        if (snap.child("FanFront").exists())  caseItem.setFanFront(snap.child("FanFront").getValue().toString());
                        if (snap.child("FanBack").exists()) caseItem.setFanBack(snap.child("FanBack").getValue().toString());
                        if (snap.child("FanOpp").exists()) caseItem.setFanOpp(snap.child("FanOpp").getValue().toString());

                        if (snap.child("GpuLength").exists()) caseItem.setGpu_maxLength(Integer.parseInt(snap.child("GpuLength").getValue().toString()));
                        if (snap.child("Weight").exists()) caseItem.setWeight(snap.child("Weight").getValue().toString());
                        if (snap.child("Sizes").exists()) caseItem.setSizes(snap.child("Sizes").getValue().toString());
                        if (snap.child("Material").exists()) caseItem.setMaterial(snap.child("Material").getValue().toString());
                        if (snap.child("Walls").exists()) caseItem.setWalls(snap.child("Walls").getValue().toString());

                        if (snap.child("SVO").exists()){
                            if (snap.child("SVO").getValue().toString().equals("есть")) caseItem.setSVO(true);
                            else caseItem.setSVO(false);
                        }
                        else {
                            caseItem.setSVO(false);
                        }

                        if (snap.child("Color").exists()) caseItem.setColor(snap.child("Color").getValue().toString());
                        if (snap.child("TranspSidePanel").exists()){
                            if (snap.child("TranspSidePanel").getValue().toString().equals("есть")) caseItem.setTranspSidePanel(true);
                            else caseItem.setTranspSidePanel(false);
                        }
                        else {
                            caseItem.setTranspSidePanel(false);
                        }

                        caseList.add(caseItem);
                    }

                    Log.d("AAA", caseList.size() + "");
                    Completable addCasesToRoom = Completable.fromRunnable(()->{

                        for (Case case1 : caseList){
                            db_components.getCasesDao().addCASE(case1);
                        }
                    });

                    addCasesToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    componentListLiveData.postValue(null);
                }
            });
        }
        else if (component_class == Cooler.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/Coolers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Cooler> coolerList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){

                        Cooler cooler = new Cooler();

                        cooler.setProduct_code(Integer.parseInt(snap.getKey()));
                        cooler.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        cooler.setProducer(snap.child("Producer").getValue().toString());
                        cooler.setModel(snap.child("Model").getValue().toString());

                        cooler.setRefLink(snap.child("Url").getValue().toString());
                        cooler.setPictureLink(snap.child("Picture").getValue().toString());

                        cooler.setBacklight(snap.child("Backlight").getValue().toString());
                        cooler.setTdp(Integer.parseInt(snap.child("MaxTdp").getValue().toString()));
                        cooler.setFanQuantity(Integer.parseInt(snap.child("FanQuantity").getValue().toString()));
                        try {
                            cooler.setFanSize(Integer.parseInt(snap.child("FanSize").getValue().toString()));
                        }
                        catch (NumberFormatException e){
                            Double d = Double.parseDouble(snap.child("FanSize").getValue().toString());
                            cooler.setFanSize(d.intValue());
                        }
                        cooler.setRotationSpeed(snap.child("FanSpeed").getValue().toString());
                        try {
                            cooler.setHeatPipes(snap.child("HeatPipes").getValue().toString());
                        }catch (NullPointerException e){}
                        try {
                            cooler.setHeatPipes_material(snap.child("HeatPipesMaterial").getValue().toString());
                        }catch (NullPointerException e){}

                        cooler.setNoiseLevel(snap.child("Noise").getValue().toString());

                        if (snap.child("Paste").getValue().toString().equals("есть"))cooler.setPaste(true);
                        else cooler.setPaste(false);

                        cooler.setPower(snap.child("Power").getValue().toString());
                        try {
                            cooler.setRadiatorMaterial(snap.child("RadiatorMaterial").getValue().toString());
                        }catch (NullPointerException e){}

                        DataSnapshot sockets = snap.child("Sockets");
                        if (sockets.child("AM4").getValue().equals("да")) cooler.setAM4(true);
                        else cooler.setAM4(false);

                        if (sockets.child("LGA 1200").getValue().equals("да")) cooler.setLga1200(true);
                        else cooler.setLga1200(false);

                        if (sockets.child("LGA 1700").getValue().equals("да")) cooler.setLga1700(true);
                        else cooler.setLga1700(false);

                        coolerList.add(cooler);
                    }

                    Completable addCoolersToRoom = Completable.fromRunnable(()->{

                        for (Cooler cooler : coolerList){
                            db_components.getCoolersDao().addCOOLER(cooler);
                        }
                    });

                    addCoolersToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    componentListLiveData.postValue(null);
                }
            });
        }
        else if (component_class == Hdd.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/HDDs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Hdd> hddList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Hdd hdd = new Hdd();

                        hdd.setProduct_code(Integer.parseInt(snap.getKey()));
                        hdd.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        hdd.setProducer(snap.child("Producer").getValue().toString());
                        hdd.setModel(snap.child("Model").getValue().toString());
                        try {
                            hdd.setFamily(snap.child("Family").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        hdd.setRefLink(snap.child("Url").getValue().toString());
                        hdd.setPictureLink(snap.child("Picture").getValue().toString());

                        hdd.setCapacity(Double.parseDouble(snap.child("Capacity").getValue().toString()));
                        hdd.setFormFactor(snap.child("FormFactor").getValue().toString());
                        hdd.setBufferMemory(Integer.parseInt(snap.child("Buffer").getValue().toString()));
                        hdd.setRotationSpeed(Integer.parseInt(snap.child("Speed").getValue().toString()));

                        hddList.add(hdd);
                    }

                    Completable addHDDsToRoom = Completable.fromRunnable(()->{

                        for (Hdd hdd : hddList){
                            db_components.getHddsDao().addHDD(hdd);
                        }
                    });

                    addHDDsToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (component_class == Ssd.class || component_class == Component.class){
            FirebaseDatabase.getInstance().getReference("Data/Components/SSDs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Ssd> ssdList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Ssd ssd = new Ssd();

                        ssd.setProduct_code(Integer.parseInt(snap.getKey()));
                        ssd.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
                        ssd.setProducer(snap.child("Producer").getValue().toString());
                        ssd.setModel(snap.child("Model").getValue().toString());
                        try {
                            ssd.setFamily(snap.child("Family").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        ssd.setRefLink(snap.child("Url").getValue().toString());
                        ssd.setPictureLink(snap.child("Picture").getValue().toString());

                        ssd.setCapacity(Integer.parseInt(snap.child("Capacity").getValue().toString()));
                        ssd.setFormFactor(snap.child("FormFactor").getValue().toString());
                        ssd.setMaxSpeed_read(Integer.parseInt(snap.child("ReadSpeed").getValue().toString()));
                        ssd.setMaxSpeed_write(Integer.parseInt(snap.child("WriteSpeed").getValue().toString()));

                        try {
                            ssd.setTBW((int)Double.parseDouble(snap.child("TBW").getValue().toString()));
                        }
                        catch (NullPointerException e){}
                        try {
                            ssd.setStorageType(snap.child("StorageType").getValue().toString());
                        }
                        catch (NullPointerException e){}
                        try {
                            ssd.setInterFace(snap.child("Interface").getValue().toString());
                        }
                        catch (NullPointerException e){}

                        ssdList.add(ssd);
                    }

                    Completable addSSDsToRoom = Completable.fromRunnable(()->{

                        for (Ssd ssd : ssdList){
                            db_components.getSsdsDao().addSSD(ssd);
                        }
                    });

                    addSSDsToRoom
                            .subscribeOn(Schedulers.io())
                            .subscribe(() -> {
                                Observable.fromCallable(getComponentList).subscribeOn(Schedulers.io()).subscribe(components -> {
                                    componentListLiveData.postValue(components);
                                });
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private boolean isMb_correct(Mb MB){
        boolean isCorrect = true;

        // материнские платы, не соответствующие выбранному процессору
        if (current_configuration.mCpu != null)
            if (!MB.getSocket().equals(current_configuration.mCpu.getSocket())) isCorrect = false;

        // материнские платы, не соответствующие выбранному корпусу
        if (current_configuration.mCase != null)
            if (!MB.getFormFactor().equals(current_configuration.mCase.getFormFactor())) isCorrect = false;

        // материнская плата, которая уже выбрана
        if (current_configuration.mMb != null)
            if (current_configuration.mMb.getProduct_code() == MB.getProduct_code()) isCorrect = false;

        return isCorrect;
    }

    private boolean isCpu_correct(Cpu CPU){
        boolean isCorrect = true;

        // соответствие выбранной материнской плате
        if (current_configuration.mMb != null)
        {
            if (!current_configuration.mMb.getSocket().equals(CPU.getSocket())) isCorrect = false;
        }

        // если данный процессор уже добавлен в сборку
        if (current_configuration.mCpu != null)
        {
            if (current_configuration.mCpu.getProduct_code() == CPU.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isGpu_correct(Gpu GPU){
        boolean isCorrect = true;

        // данная видеокарта уже добавлена в сборку
        if (current_configuration.mGpu != null)
        {
            if (current_configuration.mGpu.getProduct_code() == GPU.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isOzu_correct(Ozu OZU) {
        boolean isCorrect = true;

        // проверка на соответствие типов памяти
        if (!current_configuration.mMb.getOzuType().equals(OZU.getType())) {
            isCorrect = false;
            Log.d("AAA", "Несоотв. тип " + OZU.getType());
        }
        // проверка на количество модулей
        if (current_configuration.mMb.getOzuSlotsQuantity() < OZU.getModulesQuantity()){
            isCorrect = false;
            Log.d("AAA", "Несоотв. кол-во " + OZU.getModulesQuantity());
        }

        // данная память уже добавлена в сборку
        if (current_configuration.mOzu != null)
        {
            if (current_configuration.mOzu.getProduct_code() == OZU.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isBp_correct(Bp BP)
    {
        boolean isCorrect = true;

        // данный БП уже добавлен в сборку
        if (current_configuration.mBp != null)
        {
            if (current_configuration.mBp.getProduct_code() == BP.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isPccase_correct(Case PCCASE)
    {
        boolean isCorrect = true;

        // данный корпус уже добавлен в сборку
        if (current_configuration.mCase != null)
        {
            if (current_configuration.mCase.getProduct_code() == PCCASE.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isCooler_correct(Cooler COOLER)
    {
        boolean isCorrect = false;

        // совместимость с сокетом
        if (current_configuration.mMb != null){
            if (current_configuration.mMb.getSocket().equals("AM4")){
                if (COOLER.isAM4()) {
                    isCorrect = true;
                    Log.d("AAA", "AM4");
                }

            }
            else if (current_configuration.mMb.getSocket().equals("LGA 1200")){
                if (COOLER.isLga1200()) isCorrect = true;
                Log.d("AAA", "1200");
            }
            else if (current_configuration.mMb.getSocket().equals("LGA 1700")){
                if (COOLER.isLga1700()) isCorrect = true;
                Log.d("AAA", "1700");
            }
        }

        // данный кулер уже добавлен в сборку
        if (current_configuration.mCooler != null)
        {
            if (current_configuration.mCooler.getProduct_code() == COOLER.getProduct_code()) isCorrect = false;
        }

        Log.d("AAA", isCorrect + "");
        return isCorrect;
    }

    private boolean isHdd_correct(Hdd HDD)
    {
        boolean isCorrect = true;

        // данный ЖД уже добавлен в сборку
        if (current_configuration.mHdd != null)
        {
            if (current_configuration.mHdd.getProduct_code() == HDD.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isSsd25_correct(Ssd SSD)
    {
        boolean isCorrect = true;

        // проверка на форм-фактор
        if (!SSD.getFormFactor().equals("2.5")) isCorrect = false;

        // данный SSD 2.5" уже добавлен в сборку
        if (current_configuration.mSsd_25 != null)
        {
            if (current_configuration.mSsd_25.getProduct_code() == SSD.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isSsdM2_correct(Ssd SSD)
    {
        boolean isCorrect = true;

        // проверка на форм-фактор
        if (!SSD.getFormFactor().equals("M.2 2280")) isCorrect = false;

        // данный SSD 2.5" уже добавлен в сборку
        if (current_configuration.mSsd_m2 != null)
        {
            if (current_configuration.mSsd_m2.getProduct_code() == SSD.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    // обновление цены на товар
    private int updatePrice(int productCode){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            URL url = new URL(COMPONENTS_FEED_URL);
            parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));


            while (parser.getEventType() != XmlPullParser.END_DOCUMENT){

                if (parser.getEventType() == XmlPullParser.START_TAG){
                    if (parser.getName().equals("offer")){

                        int code = Integer.parseInt(parser.getAttributeValue(0));

                        if (code == productCode){
                            while (true){
                                if (parser.getEventType() == XmlPullParser.START_TAG){
                                    if (parser.getName().equals("price")){
                                        parser.next();
                                        return Integer.parseInt(parser.getText());
                                    }
                                }
                                if (parser.getEventType() == XmlPullParser.END_TAG){
                                    if (parser.getName().equals("offer")) break;
                                }
                                parser.next();
                            }
                        }
                    }
                }

                parser.next();
            }

            return 0;
        }
        catch (XmlPullParserException | IOException e){
            Log.d("AAA", "Ошибка при обновлении цен");
            return -1;
        }
    }

    HashMap<String, List<String>> getMbFilters(){

        HashMap<String, List<String>> filters = new HashMap<>();

        // сокет
        List<String> availableSockets = new ArrayList<>();
        if (current_configuration.mCpu == null){
            for (Component mb : baseComponentList){
                Mb MB = (Mb)mb;
                if (!availableSockets.contains(MB.getSocket())) availableSockets.add(MB.getSocket());
            }
        }
        else availableSockets.add(current_configuration.mCpu.getSocket());
        filters.put("Сокет", availableSockets);

        // форм-фактор
        List<String> availableFormFactors = new ArrayList<>();
        for (Component component : baseComponentList)
        {
            Mb MB = (Mb) component;
            if (!availableFormFactors.contains(MB.getFormFactor())) availableFormFactors.add(MB.getFormFactor());
        }
        filters.put("Форм-фактор", availableFormFactors);

        // тип памяти
        List<String> availableOzuTypes = new ArrayList<>();
        for (Component component : baseComponentList)
        {
            Mb MB = (Mb) component;
            if (!availableOzuTypes.contains(MB.getOzuType())) availableOzuTypes.add(MB.getOzuType());
        }
        filters.put("Тип памяти", availableOzuTypes);

        // количество слотов памяти
        List<String> availableOzuSlotsQuantities = new ArrayList<>();
        for (Component component : baseComponentList)
        {
            Mb MB = (Mb)component;
            String ozuSlotsQuantity = MB.getOzuSlotsQuantity() + "";
            if (!availableOzuSlotsQuantities.contains(ozuSlotsQuantity)) availableOzuSlotsQuantities.add(ozuSlotsQuantity);
        }
        filters.put("Количество слотов памяти", availableOzuSlotsQuantities);

        // количество M2
        List<String> availableM2Quantities = new ArrayList<>();
        for (Component component : baseComponentList)
        {
            Mb MB = (Mb) component;
            String M2Quantity = MB.getM2() + "";
            if (!availableM2Quantities.contains(M2Quantity)) availableM2Quantities.add(M2Quantity);
        }
        filters.put("Разъёмы M2", availableM2Quantities);

        // количество SATA
        List<String> availableSataQuantities = new ArrayList<>();
        for (Component component : baseComponentList){
            Mb MB = (Mb) component;
            String SataQuantity = MB.getSata3() + "";
            if (!availableSataQuantities.contains(SataQuantity)) availableSataQuantities.add(SataQuantity);
        }
        filters.put("Разъёмы SATA3", availableSataQuantities);

        return filters;
    }

    List<String> getChipsets(String socket){
        List<String> chipsets = new ArrayList<>();
        for (Component component : baseComponentList){
            Mb MB = (Mb)component;
            if (MB.getSocket().equals(socket)) {
                if (!chipsets.contains(MB.getChipset())) chipsets.add(MB.getChipset());
            }
        }

        return chipsets;
    }


    // применение фильтров для материнских плат
    public void applyMbFilters(HashMap<String, List<String>> filter){

        filterComponentList.clear();

        for (Component component : baseComponentList){
            Mb mb = (Mb)component;

            boolean mSocket = false;

            if (filter.containsKey("Socket")){
                for (String socket : filter.get("Socket")){
                    if (mb.getSocket().equals(socket)) mSocket = true;
                }
            } else mSocket = true;


            boolean mChipset = false;
            if (filter.containsKey("Chipset")){
                for (String chipset : filter.get("Chipset")){
                    if (mb.getChipset().equals(chipset)) mChipset = true;
                }
            } else mChipset = true;


            boolean mFormFactor = false;
            if (filter.containsKey("FormFactor")){
                for (String formFactor : filter.get("FormFactor")){
                    if (mb.getFormFactor().equals(formFactor)) mFormFactor = true;
                }
            } else mFormFactor = true;

            boolean mOzuType = false;
            if (filter.containsKey("OzuType")){
                Log.d("AAA", filter.get("OzuType").toString());
                for (String ozuType : filter.get("OzuType")){
                    if (mb.getOzuType().equals(ozuType)) mOzuType = true;
                }
            } else mOzuType = true;

            boolean mSlotsQuantity = false;
            if (filter.containsKey("OzuSlotsQuantity")){
                for (String ozuSlotsQuantity : filter.get("OzuSlotsQuantity")){
                    if (mb.getOzuSlotsQuantity() == Integer.parseInt(ozuSlotsQuantity)) mSlotsQuantity = true;
                }
            } else mSlotsQuantity = true;

            boolean mM2Quantity = false;
            if (filter.containsKey("M2Quantity")){
                for (String m2Quantity : filter.get("M2Quantity")){
                    if (mb.getM2() == Integer.parseInt(m2Quantity)) mM2Quantity = true;
                }
            } else mM2Quantity = true;

            boolean mSataQuantity = false;
            if (filter.containsKey("SataQuantity")){
                for (String sataQuantity : filter.get("SataQuantity")){
                    if (mb.getSata3() == Integer.parseInt(sataQuantity)) mSataQuantity = true;
                }
            } else mSataQuantity = true;


            if (mSocket && mChipset && mFormFactor  && mSlotsQuantity && mOzuType && mM2Quantity && mSataQuantity) {
                filterComponentList.add(mb);
            }


        }

        componentListLiveData.setValue(filterComponentList);
    }

    protected void loadNativeAdList(Context context, int size){
        List<NativeAd> adList = new ArrayList<>();

        int maxAdNumber;
        if (size > 30) maxAdNumber = size / 30;
        else maxAdNumber = 1;

        for (int i = 1; i <= maxAdNumber; i++){

            final NativeAdLoader nativeAdLoader = new NativeAdLoader(context);
            final NativeAdRequestConfiguration nativeAdRequestConfiguration =
                    new NativeAdRequestConfiguration.Builder("R-M-1648144-2").build();
            nativeAdLoader.loadAd(nativeAdRequestConfiguration);

            int finalI = i;
            nativeAdLoader.setNativeAdLoadListener(new NativeAdLoadListener() {
                @Override
                public void onAdLoaded(@NonNull final NativeAd nativeAd) {
                    adList.add(nativeAd);
                    if (adList.size() == maxAdNumber){
                        Log.d("AAA", "Size = " + size);
                        Log.d("AAA", "finalI = " + finalI);
                        Log.d("AAA", "AdSize = " + adList.size());
                        adListLiveData.setValue(adList);
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull final AdRequestError error) {
                    Log.d("AAA", "Ошибка");
                    adListLiveData.setValue(adList);
                }
            });
        }
    }

    protected LiveData<List<Component>> getComponentListLiveData(){
        return componentListLiveData;
    }

    protected LiveData<List<NativeAd>> getAdListLiveData(){
        return adListLiveData;
    }

    public String getCurrentTitle(){

        String titleString = "";

        if (component_class == Mb.class) titleString = "Материнская плата";
        else if (component_class == Cpu.class) titleString = "Процессор";
        else if (component_class == Gpu.class) titleString = "Видеокарта";
        else if (component_class == Ozu.class) titleString = "Оперативная память";
        else if (component_class == Bp.class) titleString = "Блок питания";
        else if (component_class == Case.class) titleString = "Корпус";
        else if (component_class == Cooler.class) titleString = "Охлаждение";
        else if (component_class == Hdd.class) titleString = "Жёсткий диск";
        else if (component_class == Ssd.class) titleString = "SSD-диск 2.5";
        else if (component_class == Component.class) titleString = "SSD-диск M2";

        return titleString;
    }
}
