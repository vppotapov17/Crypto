package com.potapp.cyberhelper.screens.splash;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.potapp.cyberhelper.database.dbs.DB_COMPONENTS;
import com.potapp.cyberhelper.models.components.Cpu;
import com.potapp.cyberhelper.models.components.Mb;
import com.potapp.cyberhelper.models.components.Ozu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashViewModel extends AndroidViewModel {

    private static final String COMPONENTS_FEED_URL = "https://feeds.advcake.com/feed/download/bdd07c79189c41ac56bd8ff2d2eeb6dc";

    private MutableLiveData<Integer> liveData;
    private int successLoadNumber;

    // БД Room
    private DB_COMPONENTS db_components;

    public LiveData<Integer> getLiveData(){
        return liveData;
    };

    public SplashViewModel(@NonNull Application app) {
        super(app);

        //addNewHDDs();
        //DBAnalitycs();
        successLoadNumber = 0;

        db_components = Room.databaseBuilder(getApplication().getApplicationContext(), DB_COMPONENTS.class, "COMPONENTS").build();
        liveData = new MutableLiveData<>();

        AnonymousAuth();
        //addNewProcessors();

    }

    void AnonymousAuth(){
        // авторизация анонимного пользователя
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener(authResult -> {
                liveData.postValue(1);
            }).addOnFailureListener(e ->{
                liveData.postValue(-1);
            }).addOnCanceledListener(()->{
                liveData.postValue(-1);
            });
        }
        else liveData.postValue(1);
    }

    // обновление цены на товар
    int updatePrice(int productCode){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            URL url = new URL("https://feeds.advcake.com/feed/download/81b61ee5f91c46cf2f046c2b0380bac8");
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

    void DBAnalitycs(){
        FirebaseDatabase.getInstance().getReference("Data/Components/HDDs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("HDDAnal", snapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void addNewProcessors(){

        Observable.fromRunnable(() -> {

        }).subscribeOn(Schedulers.io()).subscribe();


        Completable.fromRunnable(()-> {

            List<Cpu> feedCpuList = new ArrayList<>();

            Log.d("AAA", "Начался поиск новых процессоров");

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();

                URL url = new URL("https://feeds.advcake.com/feed/download/da1a1dc4f9deb21fa6457b1c5c6d6f1c");
                parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));


                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {

                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("offer")) {

                            Cpu cpu = new Cpu();
                            cpu.setProduct_code(Integer.parseInt(parser.getAttributeValue(0)));

                            while (true) {
                                if (parser.getEventType() == XmlPullParser.START_TAG) {
                                    if (parser.getName().equals("url")) {
                                        parser.next();
                                        cpu.setRefLink(parser.getText());
                                    } else if (parser.getName().equals("price")) {
                                        parser.next();
                                        cpu.setPrice(Integer.parseInt(parser.getText()));
                                    } else if (parser.getName().equals("picture")) {
                                        parser.next();
                                        cpu.setPictureLink(parser.getText());
                                    } else if (parser.getName().equals("name")) {
                                        parser.next();
                                        String name = parser.getText();
                                        name = name.substring(name.indexOf(" ") + 1, name.indexOf(","));
                                        cpu.setProducer(name.substring(0, name.indexOf(" ")));
                                        cpu.setModel(name.substring(name.indexOf(" ") + 1));
                                    }
                                }
                                if (parser.getEventType() == XmlPullParser.END_TAG) {
                                    if (parser.getName().equals("offer")) break;
                                }
                                parser.next();
                            }

                            feedCpuList.add(cpu);
                        }
                    }

                    parser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                Log.d("AAA", e.getMessage());
            }

            // какие процессоры уже есть в базе данных
            List<Integer> existsKey = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("Data/Components/CPUs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        existsKey.add(Integer.parseInt(snapshot1.getKey()));
                    }

                    Log.d("AAA", "Всего процессоров в фиде: " + feedCpuList.size());

                    Completable.fromRunnable(()->{
                        for (Cpu feedCpu : feedCpuList) {

                            if (existsKey.contains(feedCpu.getProduct_code())) {
                                continue;
                            }

                            Log.d("AAA", feedCpu.getName());

                            String url = feedCpu.getRefLink();

                            try {
                                Document d = Jsoup
                                        .connect(url + "/properties/")
                                        .userAgent("Chrome")
                                        .get();


                                Log.d("AAA", "Чтение характеристик ...");

                                Elements specs = d.select("div.Specifications__row");


                                for (Element spec : specs) {
                                    String specText = spec.select("div.Specifications__column_name").get(0).text();
                                    String specValue = spec.select("div.Specifications__column_value").get(0).text();

                                    if (specText.equals("Гнездо процессора")) {

                                        if (specValue.contains("Socket"))
                                            specValue = specValue.substring(6);
                                        feedCpu.setSocket(specValue);

                                    } else if (specText.equals("Ядро"))
                                        feedCpu.setFamily(specValue);
                                    else if (specText.equals("Количество ядер"))
                                        try {
                                            feedCpu.setCores(Integer.parseInt(specValue));
                                        } catch (Exception e) {
                                        }
                                    else if (specText.equals("Количество потоков"))
                                        try {
                                            feedCpu.setStreams(Integer.parseInt(specValue));
                                        } catch (Exception e) {
                                        }
                                    else if (specText.equals("Частота")) {
                                        String baseFreq = specValue.substring(0, specValue.indexOf(" ГГц"));
                                        feedCpu.setBaseFrequency(Double.parseDouble(baseFreq));

                                        try {
                                            String turboFreq = specValue.substring(specValue.indexOf(" и ") + 3, specValue.lastIndexOf(" ГГц "));
                                            feedCpu.setTurboFrequency(Double.parseDouble(turboFreq));
                                        } catch (Exception e) {
                                        }

                                    } else if (specText.equals("L3 кэш"))
                                        try {
                                            feedCpu.setCache(Integer.parseInt(specValue.substring(0, specValue.length() - 3)));
                                        } catch (Exception e) {
                                        }
                                    else if (specText.equals("Технологический процесс"))
                                        try {
                                            feedCpu.setTechprocess(Integer.parseInt(specValue.substring(0, specValue.length() - 3)));
                                        } catch (Exception e) {
                                        }
                                    else if (specText.equals("Множитель"))
                                        feedCpu.setMultiplier(specValue);
                                    else if (specText.equals("Тепловыделение"))
                                        try {
                                            feedCpu.setTdp(Integer.parseInt(specValue.substring(0, specValue.length() - 3)));
                                        } catch (Exception e) {
                                        }
                                    else if (specText.equals("Тип памяти"))
                                        feedCpu.setOzuType(specValue);
                                    else if (specText.equals("Поддержка частот памяти"))
                                        try {
                                            feedCpu.setMaxOzuFrequency(Integer.parseInt(specValue.substring(0, specValue.length() - 4)));
                                        } catch (Exception e) {
                                        }

                                    else if (specText.equals("Количество каналов памяти"))
                                        try {
                                            feedCpu.setOzuChannels(Integer.parseInt(specValue));
                                        } catch (Exception e) {
                                        }
                                    else if (specText.equals("Версия PCI Express"))
                                        try {
                                            feedCpu.setPciE_version(Double.parseDouble(specValue.substring(specValue.length() - 3)));
                                        } catch (Exception e) {
                                        }
                                    else if (specText.equals("Тип поставки")) {
                                        try {
                                            feedCpu.setShipment(specValue);
                                        } catch (Exception e) {
                                        }
                                    } else if (specText.equals("Модель графического ядра")) {
                                        feedCpu.setGraphics(specValue);
                                    } else if (specText.equals("Частота графического ядра"))
                                        feedCpu.setGraphics_frequency(specValue);
                                }

                                Log.d("AAA", "Парсинг результатов бенчмарков ...");

                                Document benchDoc = Jsoup
                                        .connect("https://browser.geekbench.com/processor-benchmarks/")
                                        .userAgent("Mozilla")
                                        .get();

                                Elements singleElements = benchDoc.select("div#single-core > div.table-wrapper > table > tbody > tr");


                                for (Element element : singleElements) {
                                    String name = element.select("td.name > a").get(0).text();
                                    String value = element.select("td.score").get(0).text();
                                    name = name.replace("-", " ");

                                    if (name.equals(feedCpu.getName())) {
                                        feedCpu.setGeekbench_single(Integer.parseInt(value));
                                    }
                                }

                                Elements multiElements = benchDoc.select("div#multi-core > div.table-wrapper > table > tbody > tr");

                                for (Element element : multiElements) {
                                    String name = element.select("td.name > a").get(0).text();
                                    String value = element.select("td.score").get(0).text();
                                    name = name.replace("-", " ");

                                    if (name.equals(feedCpu.getName())) {
                                        feedCpu.setGeekbench_multi(Integer.parseInt(value));
                                    }
                                }

                                Log.d("AAA", "Запись в базу данных ...");

                                HashMap<String, String> processor = new HashMap<>();
                                processor.put("Producer", feedCpu.getProducer());
                                processor.put("Model", feedCpu.getModel());
                                processor.put("Socket", feedCpu.getSocket());
                                processor.put("Cores", feedCpu.getCores() + "");
                                processor.put("Streams", feedCpu.getStreams() + "");
                                processor.put("BaseFrequency", feedCpu.getBaseFrequency() + "");
                                processor.put("TurboFrequency", feedCpu.getTurboFrequency() + "");
                                processor.put("Cache", feedCpu.getCache() + "");
                                processor.put("Techprocess", feedCpu.getTechprocess() + "");
                                processor.put("Multiplier", feedCpu.getMultiplier() + "");
                                processor.put("Tdp", feedCpu.getTdp() + "");
                                processor.put("OzuType", feedCpu.getOzuType() + "");
                                processor.put("MaxOzuFrequency", feedCpu.getMaxOzuFrequency() + "");
                                processor.put("OzuChannels", feedCpu.getOzuChannels() + "");
                                processor.put("PciE_version", feedCpu.getPciE_version() + "");
                                processor.put("Graphics", feedCpu.getGraphics() + "");
                                processor.put("GraphicsFrequency", feedCpu.getGraphics_frequency());
                                processor.put("Family", feedCpu.getFamily());
                                processor.put("Geekbench_single", feedCpu.getGeekbench_single() + "");
                                processor.put("Geekbench_multi", feedCpu.getGeekbench_multi() + "");
                                processor.put("Url", url);
                                processor.put("Picture", feedCpu.getPictureLink());
                                processor.put("Price", feedCpu.getPrice() + "");
                                processor.put("Shipment", feedCpu.getShipment());

                                // если указаны бенчмарки, добавляем в базу
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data/Components/CPUs/");
                                if (feedCpu.getGeekbench_single() != 0 && feedCpu.getGeekbench_multi() != 0) {
                                    reference.child(feedCpu.getProduct_code() + "").setValue(processor);
                                    Log.d("AAA", feedCpu.getName() + " успешно добавлен в базу!");
                                } else
                                    Log.d("AAA", feedCpu.getName() + " не добавлен в базу, т.к. не найдены результаты бенчмарков");


                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("AAA", e.getMessage());
                            }

                            try {
                                Thread.sleep(2500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).subscribeOn(Schedulers.io()).subscribe(()->{
                       Log.d("AAA", "Обновление процессоров завершено!");
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }).subscribeOn(Schedulers.io()).subscribe(()->{});
    }

    void addNewHDDs(){
        new Thread(()->{

            Log.d("AAA", "Start");

            int size = 0;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();

                URL url = new URL("https://feeds.advcake.com/feed/download/0b76b04e995b9d0aca74f35359a2376b");
                parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));


                while (parser.getEventType() != XmlPullParser.END_DOCUMENT){

                    if (parser.getEventType() == XmlPullParser.START_TAG){
                        if (parser.getName().equals("offer")){
                            HashMap<String, String> item = new HashMap<>();

                            String code = parser.getAttributeValue(0);

                            while (true){
                                if (parser.getEventType() == XmlPullParser.START_TAG){
                                    if (parser.getName().equals("url")){
                                        parser.next();
                                        item.put("Url", parser.getText());
                                    }
                                    else if (parser.getName().equals("price")){
                                        parser.next();
                                        item.put("Price", parser.getText());
                                    }
                                    else if (parser.getName().equals("picture")){
                                        parser.next();
                                        item.put("Picture", parser.getText());
                                    }
                                    else if (parser.getName().equals("description")){
                                        parser.next();
                                        String description = parser.getText();

                                        String formFactor = description.substring(description.indexOf("форм-фактор") + 12, description.indexOf("форм-фактор") + 15);
                                        item.put("FormFactor", formFactor);

                                        String capacity = description.substring(description.indexOf("объём") + 7, description.indexOf(";", description.indexOf("объём")));

                                        int value = 0;

                                        if (capacity.contains("ТБ")) value = Integer.parseInt(capacity.substring(0, capacity.indexOf("ТБ") - 1));
                                        else if (capacity.contains("ГБ")) {
                                            value = Integer.parseInt(capacity.substring(0, capacity.indexOf("ГБ") - 1));
                                            value /= 1000;
                                        }

                                        item.put("Capacity", value + "");

                                        String speed = description.substring(description.indexOf("скорость вращения шпинделя") + 27,
                                                description.indexOf(";", description.indexOf("скорость вращения шпинделя")));

                                        item.put("Speed", speed.substring(0, speed.indexOf("об/мин") - 1));

                                        String buffer = description.substring(description.indexOf("буферная память") + 16,
                                                description.length() - 3);

                                        item.put("Buffer", buffer);
                                    }
                                    else if (parser.getName().equals("vendor")){
                                        parser.next();
                                        item.put("Producer", parser.getText());
                                    }
                                    else if (parser.getName().equals("model")){
                                        parser.next();
                                        item.put("Model", parser.getText());
                                    }
                                    else if (parser.getName().equals("param")){
                                        if (parser.getAttributeValue(0).equals("Линейка")){
                                            parser.next();
                                            item.put("Family", parser.getText());
                                        }
                                    }
                                }
                                if (parser.getEventType() == XmlPullParser.END_TAG){
                                    if (parser.getName().equals("offer")) break;
                                }
                                parser.next();
                            }


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Data/Components/HDDs");
                            ref.child(code).setValue(item);

                        }
                    }

                    parser.next();
                }
                Log.d("AAA", "size: " + size);
            }
            catch (XmlPullParserException | IOException e){
                Log.d("AAA", e.getMessage());
            }

        }).start();
    }

    public void createDataBase() {

        new Thread(()->{
            try {

                List<String> urls = new ArrayList<>();
                List<String> pictures = new ArrayList<>();
                List<String> prices = new ArrayList<>();
                List<String> codes = new ArrayList<>();

                Log.d("AAA", "Start");

                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser parser = factory.newPullParser();

                    URL url = new URL("https://feeds.advcake.com/feed/download/71d01af912a86c82179bfd9d33fce290");
                    parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));


                    while (parser.getEventType() != XmlPullParser.END_DOCUMENT){

                        if (parser.getEventType() == XmlPullParser.START_TAG){
                            if (parser.getName().equals("offer")){
                                codes.add(parser.getAttributeValue(0));
                                while (true){
                                    if (parser.getEventType() == XmlPullParser.START_TAG){
                                        if (parser.getName().equals("url")){
                                            parser.next();
                                            urls.add(parser.getText());
                                        }
                                        else if (parser.getName().equals("price")){
                                            parser.next();
                                            prices.add(parser.getText());
                                        }
                                        else if (parser.getName().equals("picture")){
                                            parser.next();
                                            pictures.add(parser.getText());
                                        }

                                    }
                                    if (parser.getEventType() == XmlPullParser.END_TAG){
                                        if (parser.getName().equals("offer")) break;
                                    }
                                    parser.next();
                                }
                            }
                        }

                        parser.next();
                    }
                }
                catch (XmlPullParserException | IOException e){
                    Log.d("AAA", "Ошибка");
                }


                Log.d("AAA", "Найдено " + urls.size() + " материнских плат");

                List<Mb> mbList = db_components.getMotherboardsDao().getMB();
                for (int i = 0; i < urls.size(); i++) {


                    boolean f = false;
                    int q = Integer.parseInt(codes.get(i));
                    for (Mb MB : mbList){
                        if (q == MB.getProduct_code()) f = true;
                    }
                    if (f) continue;

                    String url = urls.get(i);
                    int current_index = urls.indexOf(url);

                    Log.d("AAA", "Матплата " + urls.indexOf(url));

                    Mb mb = new Mb();

                    Document d = Jsoup
                            .connect(url + "/properties/")
                            .userAgent("YaBrowser/22.7.5.940")
                            .get();

                    Log.d("AAA", "Получение данных о матплате ...");

                    String mbName = d.select("head > title").get(0).text();


                    mbName = mbName.substring(33, mbName.indexOf(","));

                    String mbBrand = mbName.substring(0, mbName.indexOf(" "));
                    String mbModel = mbName.substring(mbName.indexOf(" ") + 1);

                    mb.setProducer(mbBrand);
                    mb.setModel(mbModel);
                    mb.setProduct_code(Integer.parseInt(codes.get(current_index)));
                    mb.setPrice(Integer.parseInt(prices.get(current_index)));
                    mb.setRefLink(url);
                    mb.setPictureLink(pictures.get(current_index));

                    Log.d("AAA", "Имя: " + mb.getName());
                    Log.d("AAA", "Код товара: " + mb.getProduct_code());
                    Log.d("AAA", "Стоимость: " + mb.getPrice());
                    Log.d("AAA", "Ссылка реф: " + mb.getRefLink());
                    Log.d("AAA", "Изображение: " + mb.getPictureLink());


                    Log.d("AAA", "Чтение характеристик ...");

                    Elements specs = d.select("div.Specifications__row");


                    for (Element spec : specs) {
                        String specText = spec.select("div.Specifications__column_name").get(0).text();
                        String specValue = spec.select("div.Specifications__column_value").get(0).text();

                        if (specText.contains("Гнездо процессора")) {

                            if (specValue.contains("Socket")) specValue = specValue.substring(6);
                            mb.setSocket(specValue);

                        } else if (specText.contains("Чипсет")) {
                            mb.setChipset(specValue.substring(specValue.indexOf(" ") + 1));
                            Log.d("AAA", mb.getChipset());
                        } else if (specText.contains("Частотная спецификация памяти"))
                            try {
                                specValue = specValue.substring(0, specValue.indexOf(" "));
                                mb.setOzuFrequencySpec(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("Слотов памяти"))
                            try {
                                String ozuType = specText.substring(specText.length() - 4);
                                mb.setOzuType(ozuType);
                                mb.setOzuSlotsQuantity(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("Максимальный объем оперативной памяти")) {
                            try {
                                mb.setMaxOzuSize(Integer.parseInt(specValue.substring(0, specValue.indexOf(" "))));
                            } catch (Exception e) {
                            }

                        } else if (specText.contains("PCI-E x1")) {
                            Log.d("AAA", "PCIEX1 detected");
                            try {
                                mb.setPciE_x1(Integer.parseInt(specValue));
                                Log.d("AAA", mb.getPciE_x1() + "");

                            } catch (Exception e) {
                                Log.d("AAA", "PCI-E x1 не указано");
                            }
                        } else if (specText.contains("PCI-E 3.0 x16"))
                            try {
                                mb.setPciEv3_x16(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("PCI-E 4.0 x16"))
                            try {
                                mb.setPciEv4_x16(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("SATA3"))
                            try {
                                mb.setSata3(Integer.parseInt(specValue));
                                Log.d("AAA", mb.getSata3() + "");
                            } catch (Exception e) {
                                Log.d("AAA", "Sata3 не указано");
                            }
                        else if (specText.contains("M.2"))
                            try {
                                mb.setM2(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("Форм-фактор"))
                            try {
                                mb.setFormFactor(specValue);
                            } catch (Exception e) {
                            }

                        else if (specText.contains("PS/2"))
                            try {
                                mb.setPs2(specValue);
                            } catch (Exception e) {
                            }
                        else if (specText.contains("USB 2.0"))
                            try {
                                mb.setUsb20(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("USB 3.0")) {
                            try {
                                mb.setUsb30(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        } else if (specText.equals("Кол-во внешних USB 3.1")) {
                            try {
                                mb.setUsb31(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        } else if (specText.contains("Type-C")) {
                            try {
                                mb.setUsbC(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        } else if (specText.contains("Display Port"))
                            try {
                                mb.setDisplayPort(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("VGA"))
                            try {
                                mb.setVga(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("HDMI"))
                            try {
                                mb.setHdmi(Integer.parseInt(specValue));
                            } catch (Exception e) {
                            }
                        else if (specText.contains("Сетевой интерфейс"))
                            try {
                                mb.setNetwork(specValue);
                            } catch (Exception e) {
                            }
                        else if (specText.contains("Звук"))
                            try {
                                mb.setSound(specValue);
                            } catch (Exception e) {
                            }
                        else if (specText.contains("Питание материнской платы и процессора"))
                            try {
                                mb.setPower(specValue);
                            } catch (Exception e) {
                            }

                    }


                    Log.d("AAA", "Запись в базу данных ...");

                    HashMap<String, String> motherboard = new HashMap<>();
                    motherboard.put("Producer", mb.getProducer());
                    motherboard.put("Model", mb.getModel());
                    motherboard.put("Socket", mb.getSocket());
                    motherboard.put("Url", mb.getRefLink());
                    motherboard.put("Price", mb.getPrice() + "");
                    motherboard.put("Picture", mb.getPictureLink());

                    motherboard.put("Chipset", mb.getChipset());
                    motherboard.put("OzuType", mb.getOzuType());
                    motherboard.put("OzuSlots", mb.getOzuSlotsQuantity() + "");
                    motherboard.put("MaxOzuSize", mb.getMaxOzuSize() + "");
                    motherboard.put("OzuFrequencySpec", mb.getOzuFrequencySpec() + "");
                    motherboard.put("PciE_x1", mb.getPciE_x1() + "");
                    motherboard.put("PciEv3_x16", mb.getPciEv3_x16() + "");
                    motherboard.put("PciEv4_x16", mb.getPciEv4_x16() + "");
                    motherboard.put("Sata3", mb.getSata3() + "");
                    motherboard.put("M2", mb.getM2() + "");
                    motherboard.put("PS2", mb.getPs2());
                    motherboard.put("Usb20", mb.getUsb20() + "");
                    motherboard.put("Usb30", mb.getUsb30() + "");
                    motherboard.put("Usb31", mb.getUsb31() + "");
                    motherboard.put("UsbC", mb.getUsbC() + "");
                    motherboard.put("DisplayPort", mb.getDisplayPort() + "");
                    motherboard.put("VGA", mb.getVga() + "");
                    motherboard.put("HDMI", mb.getHdmi() + "");
                    motherboard.put("Network", mb.getNetwork());
                    motherboard.put("Sound", mb.getSound());
                    motherboard.put("FormFactor", mb.getFormFactor());
                    motherboard.put("Power", mb.getPower());


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data/Components/Motherboards");
                    reference.child(mb.getProduct_code() + "").setValue(motherboard);

                    Thread.sleep(1000);
                }

            } catch (IOException | InterruptedException e) {
                Log.d("AAA", e.getMessage());
            }
        }).start();

//        new Thread(()->{
//
//            Log.d("AAA", "Start");
//
//            int size = 0;
//
//            try {
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                factory.setNamespaceAware(true);
//                XmlPullParser parser = factory.newPullParser();
//
//                URL url = new URL("https://feeds.advcake.com/feed/download/950c35dc8e82b5175b7ac976aeb1792e");
//                parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));
//
//
//                while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
//
//                    if (parser.getEventType() == XmlPullParser.START_TAG){
//                        if (parser.getName().equals("offer")){
//                            HashMap<String, String> item = new HashMap<>();
//
//                            String code = parser.getAttributeValue(0);
//                            boolean inCorrect = false;
//
//                            while (true){
//                                if (parser.getEventType() == XmlPullParser.START_TAG){
//                                    if (parser.getName().equals("url")){
//                                        parser.next();
//                                        item.put("Url", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("price")){
//                                        parser.next();
//                                        item.put("Price", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("picture")){
//                                        parser.next();
//                                        item.put("Picture", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("name")){
//                                        parser.next();
//
//                                        // тип памяти
//                                        String name = parser.getText();
//                                        if (name.contains("SO-DIMM")) inCorrect = true;
//
//                                        if (name.contains("ECC")) item.put("ECC", "есть");
//                                        else item.put("ECC", "нет");
//
//                                        if (name.contains("DDR4")){
//                                            item.put("Type", "DDR4");
//                                        }
//                                        else if (name.contains("DDR5")){
//                                            item.put("Type", "DDR5");
//                                        }
//                                        // объём
//                                        String b = name.substring(name.indexOf(" - ") + 3, name.indexOf(","));
//                                        item.put("Frequency", b.substring(b.length() - 4));
//                                        b = b.substring(0, b.length() - 5);
//                                        if (b.contains("x")){
//                                            item.put("ModulesQuantity", b.substring(0, 1));
//                                            b = b.substring(3);
//                                        }
//                                        else {
//                                            item.put("ModulesQuantity", "1");
//                                        }
//                                        item.put("SingleCapacity", b.substring(0, b.indexOf("Г")));
//                                    }
//                                    else if (parser.getName().equals("description")){
//                                        parser.next();
//                                        String description = parser.getText();
//
//                                        if (description.contains("латентность")){
//                                            item.put("Latency", description.substring(description.indexOf("латентность") + 13,
//                                                    description.indexOf("латентность") + 17));
//                                        }
//                                        if (description.contains("радиатор")) item.put("Radiator", "есть");
//                                        else item.put("Radiator", "нет");
//                                    }
//                                    else if (parser.getName().equals("vendor")){
//                                        parser.next();
//                                        item.put("Producer", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("model")){
//                                        parser.next();
//                                        item.put("Model", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("param")){
//                                        if (parser.getAttributeValue(0).equals("Линейка")){
//                                            parser.next();
//                                            item.put("Family", parser.getText());
//                                        }
//                                    }
//                                }
//                                if (parser.getEventType() == XmlPullParser.END_TAG){
//                                    if (parser.getName().equals("offer")) break;
//                                }
//                                parser.next();
//                            }
//
//                            if (item.get("Type") != null && !inCorrect){
//                                FirebaseFirestore.getInstance().collection("OZUs").document(code).set(item);
//                                size++;
//                                Log.d("AAA", item.toString());
//                            }
//
//                        }
//                    }
//
//                    parser.next();
//                }
//                Log.d("AAA", "size: " + size);
//            }
//            catch (XmlPullParserException | IOException e){
//                Log.d("AAA", e.getMessage());
//            }
//
//        }).start();

//        new Thread(()->{
//
//            Log.d("AAA", "Start");
//
//            int size = 0;
//
//            try {
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                factory.setNamespaceAware(true);
//                XmlPullParser parser = factory.newPullParser();
//
//                URL url = new URL("https://feeds.advcake.com/feed/download/7d208f477a115ff6dec4a4e8e6919b5f");
//                parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));
//
//
//                while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
//
//                    if (parser.getEventType() == XmlPullParser.START_TAG){
//                        if (parser.getName().equals("offer")){
//                            HashMap<String, String> item = new HashMap<>();
//
//                            String code = parser.getAttributeValue(0);
//
//                            while (true){
//                                if (parser.getEventType() == XmlPullParser.START_TAG){
//                                    if (parser.getName().equals("url")){
//                                        parser.next();
//                                        item.put("Url", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("price")){
//                                        parser.next();
//                                        item.put("Price", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("picture")){
//                                        parser.next();
//                                        item.put("Picture", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("name")){
//                                        parser.next();
//                                        String name = parser.getText();
//                                        name = name.substring(0, name.indexOf(","));
//                                        name = name.substring(name.lastIndexOf(" ") + 1);
//
//                                        double value = 0;
//
//                                        if (name.contains("ГБ")) value = Double.parseDouble(name.replace("ГБ", ""));
//                                        else if (name.contains("ТБ")) {
//                                            value = Double.parseDouble(name.replace("ТБ", ""));
//                                            value *= 1000;
//                                        }
//
//                                        item.put("Capacity", (int) value + "");
//                                    }
//                                    else if (parser.getName().equals("description")){
//                                        parser.next();
//                                        String description = parser.getText();
//
//                                        String readSpeed = description.substring(description.indexOf("скорость чтения, до") + 21,
//                                                description.indexOf(";", description.indexOf("скорость чтения, до")) - 5);
//                                        item.put("ReadSpeed", readSpeed);
//
//                                        String writeSpeed = description.substring(description.indexOf("скорость записи, до") + 21,
//                                                description.indexOf(";", description.indexOf("скорость записи, до")) - 5);
//                                        item.put("WriteSpeed", writeSpeed);
//
//                                        String Interface = description.substring(description.indexOf("интерфейс") + 11,
//                                                description.indexOf(";", description.indexOf("интерфейс")));
//
//                                        item.put("Interface", Interface);
//                                        String formFactor = "";
//                                        try {
//                                            formFactor = description.substring(description.indexOf("форм-фактор") + 13,
//                                                    description.indexOf(";", description.indexOf("форм-фактор")));
//                                        }
//                                        catch (IndexOutOfBoundsException e){
//                                            formFactor = description.substring(description.indexOf("форм-фактор") + 13);
//                                        }
//
//
//                                        if (formFactor.contains("2.5")){
//                                            formFactor = formFactor.substring(0, formFactor.length() - 1);
//                                        }
//
//                                        item.put("FormFactor", formFactor);
//
//                                        String storageType = description.substring(description.indexOf("тип памяти") + 12,
//                                                description.indexOf(";", description.indexOf("тип памяти")));
//
//                                        item.put("StorageType", storageType);
//
//                                        if (description.contains("TBW")){
//                                            String TBW = description.substring(description.indexOf("TBW") + 5,
//                                                    description.indexOf("ТБ", description.indexOf("TBW")) - 1);
//
//                                            item.put("TBW", TBW);
//                                        }
//
//                                    }
//                                    else if (parser.getName().equals("vendor")){
//                                        parser.next();
//                                        item.put("Producer", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("model")){
//                                        parser.next();
//                                        item.put("Model", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("param")){
//                                        if (parser.getAttributeValue(0).equals("Линейка")){
//                                            parser.next();
//                                            item.put("Family", parser.getText());
//                                        }
//                                    }
//                                }
//                                if (parser.getEventType() == XmlPullParser.END_TAG){
//                                    if (parser.getName().equals("offer")) break;
//                                }
//                                parser.next();
//                            }
//
//
//                            FirebaseFirestore.getInstance().collection("SSDs").document(code).set(item);
//                            size++;
//                            Log.d("AAA", item.toString());
//
//                        }
//                    }
//
//                    parser.next();
//                }
//                Log.d("AAA", "size: " + size);
//            }
//            catch (XmlPullParserException | IOException e){
//                Log.d("AAA", e.getMessage());
//            }
//
//        }).start();

//        new Thread(()->{
//
//            Log.d("AAA", "Start");
//
//            int size = 0;
//
//            try {
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                factory.setNamespaceAware(true);
//                XmlPullParser parser = factory.newPullParser();
//
//                URL url = new URL("https://feeds.advcake.com/feed/download/743d4273566f529da2b071d4abded61e");
//                parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));
//
//
//                while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
//
//                    if (parser.getEventType() == XmlPullParser.START_TAG){
//                        if (parser.getName().equals("offer")){
//                            HashMap<String, String> item = new HashMap<>();
//
//                            String code = parser.getAttributeValue(0);
//
//                            while (true){
//                                if (parser.getEventType() == XmlPullParser.START_TAG){
//                                    if (parser.getName().equals("url")){
//                                        parser.next();
//                                        item.put("Url", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("price")){
//                                        parser.next();
//                                        item.put("Price", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("picture")){
//                                        parser.next();
//                                        item.put("Picture", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("description")){
//                                        parser.next();
//                                        String description = parser.getText();
//
//                                        if (description.contains("размер вентилятора")){
//                                            String fanSize = description.substring(description.indexOf("размер вентилятора") + 19,
//                                                    description.indexOf(";", description.indexOf("размер вентилятора")) - 2);
//                                            item.put("FanSize", fanSize);
//                                        }
//
//                                        String capacity = description.substring(description.indexOf("мощность") + 10,
//                                                description.indexOf(";", description.indexOf("мощность")) - 3);
//                                        item.put("Capacity", capacity);
//
//                                        if (description.contains("активный PFC")) item.put("ActivePFC", "есть");
//                                        else item.put("ActivePFC", "нет");
//
//                                        if (description.contains("стандарт 80 PLUS")){
//                                            String certificate = description.substring(description.indexOf("стандарт 80 PLUS") + 9,
//                                                    description.indexOf(";", description.indexOf("стандарт 80 PLUS")));
//                                            item.put("Certificate", certificate);
//                                        }
//                                        else item.put("Certificate", "нет");
//
//                                        String cpuPower = description.substring(description.indexOf("питание MB и CPU") + 18,
//                                                description.indexOf(";", description.indexOf("питание MB и CPU")));
//                                        item.put("CpuPower", cpuPower);
//
//                                        if (description.contains("питание видеокарты")){
//                                            String gpuPower = description.substring(description.indexOf("питание видеокарты") + 20,
//                                                    description.indexOf(";", description.indexOf("питание видеокарты")));
//                                            item.put("GpuPower", gpuPower);
//                                        }
//                                        else item.put("GpuPower", "отсутствует");
//
//                                        String molex = description.substring(description.indexOf("разъемы Molex") + 15,
//                                                description.indexOf(";", description.indexOf("разъемы Molex")) - 3);
//                                        item.put("Molex", molex);
//
//                                        String sata = description.substring(description.indexOf("разъемы SATA") + 14,
//                                                description.indexOf(";", description.indexOf("разъемы SATA")) - 3);
//                                        item.put("Sata", sata);
//
//                                        if (description.contains("отсоединяющиеся кабели")) item.put("Cables", "есть");
//                                        else item.put("Cables", "нет");
//
//                                        if (description.contains("подсветка вентилятора")) item.put("RgbFan", "есть");
//                                        else item.put("RgbFan", "нет");
//                                    }
//                                    else if (parser.getName().equals("vendor")){
//                                        parser.next();
//                                        item.put("Producer", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("model")){
//                                        parser.next();
//                                        item.put("Model", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("param")){
//                                        if (parser.getAttributeValue(0).equals("Линейка")){
//                                            parser.next();
//                                            item.put("Family", parser.getText());
//                                        }
//                                        else if (parser.getAttributeValue(0).equals("Цвет")){
//                                            parser.next();
//                                            item.put("Color", parser.getText());
//                                        }
//                                    }
//                                }
//                                if (parser.getEventType() == XmlPullParser.END_TAG){
//                                    if (parser.getName().equals("offer")) break;
//                                }
//                                parser.next();
//                            }
//
//
//                            FirebaseFirestore.getInstance().collection("BPs").document(code).set(item);
//                            size++;
//                            Log.d("AAA", item.toString());
//
//                        }
//                    }
//
//                    parser.next();
//                }
//                Log.d("AAA", "size: " + size);
//            }
//            catch (XmlPullParserException | IOException e){
//                Log.d("AAA", e.getMessage());
//            }
//
//        }).start();

//        Log.d("AAA", "Start");
//
//        List<String> urls = new ArrayList<>();
//        List<String> codes = new ArrayList<>();
//        int size = 0;
//
//        FirebaseFirestore.getInstance().collection("GPUs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
//                        urls.add(documentSnapshot.get("Url").toString());
//                        codes.add(documentSnapshot.getId());
//                    }
//                    for (int current_index = 0; current_index < urls.size(); current_index++){
//
//                        HashMap<String, String> item = new HashMap();
//                        String url = urls.get(current_index);
//                        String code = codes.get(current_index);
//
//                        Log.d("AAA", "Видеокарта " + urls.indexOf(url));
//
//                        new Thread(() -> {
//                            Document d = null;
//                            try {
//                                d = Jsoup
//                                        .connect(url + "properties/")
//                                        .userAgent("Chrome")
//                                        .get();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            Log.d("AAA", "Получение данных о видеокарте ...");
//
//                            Elements specs = d.select("div.Specifications__row");
//
//                            for (Element spec : specs){
//                                String specText = spec.select("div.Specifications__column_name").get(0).text();
//                                String specValue = spec.select("div.Specifications__column_value").get(0).text();
//
//                                if (specText.contains("Техпроцесс")){
//                                    specValue = specValue.substring(0, specValue.length() - 3);
//                                    item.put("Techprocess", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Разрядность шины")){
//                                    specValue = specValue.substring(0, specValue.length() - 4);
//                                    item.put("BitDepth", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Максимальное разрешение")){
//                                    item.put("MaxResolution", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Количество RT ядер")){
//                                    item.put("RTCores", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Количество тензорных ядер")){
//                                    item.put("TensorCores", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Число блоков растеризации")){
//                                    item.put("RasterizeBlocks", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Число текстурных блоков")){
//                                    item.put("TextureBlocks", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Поддержка технологий")){
//                                    item.put("Technologies", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Поддержка трассировки лучей")){
//                                    if (specValue.equals("есть")) item.put("RayTracing", specValue);
//                                    else item.put("RayTracing", "нет");
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Поддержка DLSS")){
//                                    if (specValue.equals("есть")) item.put("DLSS", specValue);
//                                    else item.put("DLSS", "нет");
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Разъемы DVI")){
//                                    item.put("Dvi", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Разъемов HDMI")){
//                                    item.put("Hdmi", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Версия разъема HDMI")){
//                                    item.put("HdmiVer", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Разъемов Display Port")){
//                                    item.put("DisplayPort", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Версия разъема Display Port")){
//                                    item.put("DisplayPortVer", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Количество поддерживаемых мониторов")){
//                                    item.put("Monitors", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Разъемы дополнительного питания")){
//                                    item.put("OptionalPower", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Максимальное энергопотребление")){
//                                    specValue = specValue.substring(0, specValue.length() - 3);
//                                    item.put("MaxTdp", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Система охлаждения")){
//                                    item.put("CoolingSystem", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Использование тепловых трубок")){
//                                    if (specValue.equals("есть")) item.put("HeatPipes", specValue);
//                                    else item.put("HeatPipes", "нет");
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Lite Hash Rate (LHR)")){
//                                    item.put("LHR", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Длина видеокарты")){
//                                    specValue = specValue.substring(0, specValue.length() - 3);
//                                    item.put("Length", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                                else if (specText.contains("Количество поддерживаемых мониторов")){
//                                    item.put("Monitors", specValue);
//                                    Log.d("AAA", specValue);
//                                }
//                            }
//
//                            FirebaseFirestore.getInstance()
//                                    .collection("GPUs")
//                                    .document(code)
//                                    .set(item, SetOptions.merge());
//
//                            Log.d("AAA", item.toString());
//                        }).start();
//
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//            }
//        });

//        new Thread(()->{
//
//            Log.d("AAA", "Start");
//
//            int size = 0;
//
//            try {
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                factory.setNamespaceAware(true);
//                XmlPullParser parser = factory.newPullParser();
//
//                URL url = new URL("https://feeds.advcake.com/feed/download/b66489d69c282aca8438c2f3d023d244");
//                parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));
//
//
//                while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
//
//                    if (parser.getEventType() == XmlPullParser.START_TAG){
//                        if (parser.getName().equals("offer")){
//                            HashMap<String, String> item = new HashMap<>();
//                            boolean isCorrect = true;
//                            String code = parser.getAttributeValue(0);
//
//                            while (true){
//                                if (parser.getEventType() == XmlPullParser.START_TAG){
//                                    if (parser.getName().equals("url")){
//                                        parser.next();
//                                        item.put("Url", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("price")){
//                                        parser.next();
//                                        item.put("Price", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("picture")){
//                                        parser.next();
//                                        item.put("Picture", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("description")){
//                                        parser.next();
//                                        String description = parser.getText();
//
//                                        if (description.contains("размер")){
//                                            String Size = description.substring(description.indexOf("размер") + 8,
//                                                    description.indexOf(";", description.indexOf("размер")));
//                                            item.put("Size", Size);
//                                        }
//                                        else isCorrect = false;
//
//                                        if (description.contains("USB")){
//
//                                            String usb;
//                                            try {
//                                                usb = description.substring(description.indexOf("USB"),
//                                                        description.indexOf(";", description.indexOf("USB")));
//                                            }
//                                            catch (Exception e){
//                                                usb = description.substring(description.indexOf("USB"));
//                                            }
//
//                                            if (usb.contains("USB 2.0")){
//                                                String usb20 = usb.substring(usb.indexOf("USB 2.0") + 9,
//                                                        usb.indexOf("шт", usb.indexOf("USB 2.0")));
//
//                                                item.put("Usb20", usb20);
//                                            }
//                                            if (usb.contains("USB 3.0")){
//                                                String usb30 = usb.substring(usb.indexOf("USB 3.0") + 9,
//                                                        usb.indexOf("шт", usb.indexOf("USB 3.0")));
//
//                                                item.put("Usb30", usb30);
//                                            }
//                                            if (usb.contains("USB 3.1")){
//                                                String usb31 = usb.substring(usb.indexOf("USB 3.1") + 9,
//                                                        usb.indexOf("шт", usb.indexOf("USB 3.1")));
//
//                                                item.put("Usb31", usb31);
//                                            }
//                                            if (usb.contains("аудио на передней панели")) item.put("Audio", "есть");
//                                            else item.put("Audio", "нет");
//                                        }
//
//                                        if (description.contains("внутренние 3.5")){
//                                            String hdd35 = description.substring(description.indexOf("внутренние 3.5") + 17,
//                                                    description.indexOf("шт", description.indexOf("внутренние 3.5")));
//                                            item.put("Hdd35", hdd35);
//                                        }
//                                        if (description.contains("прозрачная боковая панель")){
//                                            item.put("TranspSidePanel", "есть");
//                                        }
//                                        else item.put("TranspSidePanel", "нет");
//                                        if (description.contains("видеокарта длиной, до")){
//                                            String gpuLength = description.substring(description.indexOf("видеокарта длиной, до") + 23,
//                                                    description.indexOf("мм", description.indexOf("видеокарта длиной, до")) - 1);
//                                            item.put("GpuLength", gpuLength);
//                                        }
//                                        else {
//                                            isCorrect = false;
//                                        }
//
//                                    }
//                                    else if (parser.getName().equals("vendor")){
//                                        parser.next();
//                                        item.put("Producer", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("model")){
//                                        parser.next();
//                                        item.put("Model", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("param")){
//                                        if (parser.getAttributeValue(0).equals("Материал")){
//                                            parser.next();
//                                            item.put("Material", parser.getText());
//                                        }
//                                        else if (parser.getAttributeValue(0).equals("Цвет")){
//                                            parser.next();
//                                            item.put("Color", parser.getText());
//                                        }
//                                    }
//                                }
//                                if (parser.getEventType() == XmlPullParser.END_TAG){
//                                    if (parser.getName().equals("offer")) break;
//                                }
//                                parser.next();
//                            }
//
//
//                            if (isCorrect)
//                            {
//                                FirebaseFirestore.getInstance().collection("Cases").document(code).set(item);
//                                size++;
//                                Log.d("AAA", item.toString());
//                            }
//
//                        }
//                    }
//
//                    parser.next();
//                }
//                Log.d("AAA", "size: " + size);
//            }
//            catch (XmlPullParserException | IOException e){
//                Log.d("AAA", e.getMessage());
//            }
//
//        }).start();

//        new Thread(()->{
//
//            Log.d("AAA", "Start");
//
//            int size = 0;
//
//            try {
//                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                factory.setNamespaceAware(true);
//                XmlPullParser parser = factory.newPullParser();
//
//                URL url = new URL("https://feeds.advcake.com/feed/download/dd3487dc04c037bbfee64f047f627e06");
//                parser.setInput(new InputStreamReader(url.openConnection().getInputStream()));
//
//
//                while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
//
//                    if (parser.getEventType() == XmlPullParser.START_TAG){
//                        if (parser.getName().equals("offer")){
//                            HashMap<String, String> item = new HashMap<>();
//                            boolean isCorrect = true;
//                            String code = parser.getAttributeValue(0);
//
//                            while (true){
//                                if (parser.getEventType() == XmlPullParser.START_TAG){
//                                    if (parser.getName().equals("name")){
//                                        parser.next();
//                                        isCorrect = false;
//                                        if (parser.getText().contains("Устройство охлаждения(кулер)")) isCorrect = true;
//                                        if (parser.getText().contains("Система водяного охлаждения")) isCorrect = true;
//                                    }
//
//                                    else if (parser.getName().equals("url")){
//                                        parser.next();
//                                        item.put("Url", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("price")){
//                                        parser.next();
//                                        item.put("Price", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("picture")){
//                                        parser.next();
//                                        item.put("Picture", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("description")){
//                                        parser.next();
//                                        String description = parser.getText();
//                                        try {
//                                            String mainInfo = description.substring(description.indexOf("вентиляторов"),
//                                                    description.indexOf(";", description.indexOf("вентиляторов")));
//
//                                            item.put("FanQuantity", mainInfo.substring(15, mainInfo.indexOf("шт")));
//
//                                            mainInfo = mainInfo.substring(mainInfo.indexOf(",") + 2);
//
//                                            item.put("FanSize", mainInfo.substring(0, mainInfo.indexOf("мм") - 1));
//
//                                            item.put("RPM", mainInfo.substring(mainInfo.indexOf(",") + 2));
//
//                                            if (description.contains("радиатор")){
//                                                item.put("RadiatorMaterial", description.substring(description.indexOf("радиатор") + 11,
//                                                        description.indexOf(";", description.indexOf("радиатор"))));
//                                            }
//                                            if (description.contains("с тепловыми трубками")){
//                                                item.put("HeatPipes", "есть");
//                                            }
//                                            else item.put("HeatPipes", "нет");
//
//                                            if (description.contains("питание от МП")){
//                                                String power;
//                                                try {
//                                                    power = description.substring(description.indexOf("питание от МП") + 16,
//                                                            description.indexOf(";"));
//                                                }
//                                                catch (Exception e){
//                                                    power = description.substring(description.indexOf("питание от МП") + 16);
//                                                }
//                                                item.put("Power", power);
//                                            }
//                                        }
//                                        catch (Exception e){
//                                            isCorrect = false;
//                                        }
//                                    }
//                                    else if (parser.getName().equals("vendor")){
//                                        parser.next();
//                                        item.put("Producer", parser.getText());
//                                    }
//                                    else if (parser.getName().equals("model")){
//                                        parser.next();
//                                        item.put("Model", parser.getText());
//                                    }
//                                }
//                                if (parser.getEventType() == XmlPullParser.END_TAG){
//                                    if (parser.getName().equals("offer")) break;
//                                }
//                                parser.next();
//                            }
//
//
//                            if (isCorrect)
//                            {
//                                //FirebaseFirestore.getInstance().collection("Coolers").document(code).set(item);
//                                size++;
//                                Log.d("AAA", item.toString());
//                            }
//
//                        }
//                    }
//
//                    parser.next();
//                }
//                Log.d("AAA", "size: " + size);
//            }
//            catch (XmlPullParserException | IOException e){
//                Log.d("AAA", e.getMessage());
//            }
//
//        }).start();

    }

}
