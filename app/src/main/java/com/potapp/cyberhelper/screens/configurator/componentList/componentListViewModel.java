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
                                    Log.d("AAA", "List LiveData updated");
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
                    Completable addMBsToRoom = Completable.fromRunnable(()->{
                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getMotherboardsDao().addMB(Mb.createFromSnapshot(snap));
                        }
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
                        cpuList.add(Cpu.createFromSnapshot(snap, true));
                    }

                    Completable addCpusToRoom = Completable.fromRunnable(()->{
                        Cpu.setCapacities(cpuList);
                        for (Cpu cpu : cpuList){
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

                    Completable addGpusToRoom = Completable.fromRunnable(()->{
                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getVideocardsDao().addGPU(Gpu.createFromSnapshot(snap));
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
                    Completable addOzusToRoom = Completable.fromRunnable(()->{
                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getOzusDao().addOZU(Ozu.createFromSnapshot(snap));
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
                    Completable addBPsToRoom = Completable.fromRunnable(()->{
                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getBpsDao().addBP(Bp.createFromSnapshot(snap));
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

                    Completable addCasesToRoom = Completable.fromRunnable(()->{

                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getCasesDao().addCASE(Case.createFromSnapshot(snap));
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

                    Completable addCoolersToRoom = Completable.fromRunnable(()->{

                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getCoolersDao().addCOOLER(Cooler.createFromSnapshot(snap));
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

                    Completable addHDDsToRoom = Completable.fromRunnable(()->{

                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getHddsDao().addHDD(Hdd.createFromSnapshot(snap));
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

                    Completable addSSDsToRoom = Completable.fromRunnable(()->{

                        for (DataSnapshot snap : snapshot.getChildren()){
                            db_components.getSsdsDao().addSSD(Ssd.createFromSnapshot(snap));
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
