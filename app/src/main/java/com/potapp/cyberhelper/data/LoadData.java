package com.potapp.cyberhelper.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.data.models.components.*;
import com.potapp.cyberhelper.data.room.dbs.DB_BPS;
import com.potapp.cyberhelper.data.room.dbs.DB_CASES;
import com.potapp.cyberhelper.data.room.dbs.DB_COOLERS;
import com.potapp.cyberhelper.data.room.dbs.DB_HDDS;
import com.potapp.cyberhelper.data.room.dbs.DB_MOTHERBOARDS;
import com.potapp.cyberhelper.data.room.dbs.DB_OZUS;
import com.potapp.cyberhelper.data.room.dbs.DB_PROCESSORS;
import com.potapp.cyberhelper.data.room.dbs.DB_SSDS;
import com.potapp.cyberhelper.data.room.dbs.DB_VIDEOCARDS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class LoadData extends AsyncTask<Void, Void, Void> {

    Context context;
    Activity splashScreen;


    // DB комплектующих
    private DB_MOTHERBOARDS db_motherboards;
    private DB_PROCESSORS db_processors;
    private DB_VIDEOCARDS db_videocards;
    private DB_OZUS db_ozus;
    private DB_BPS db_bps;
    private DB_CASES db_CASES;
    private DB_COOLERS db_coolers;
    private DB_HDDS db_hdds;
    private DB_SSDS db_ssds;

    // база данных Firestore
    FirebaseFirestore firestore;

    public LoadData(Activity splashScreen){

        this.splashScreen = splashScreen;
        this.context = splashScreen.getApplicationContext();

        firestore = FirebaseFirestore.getInstance();

        db_motherboards = Room.databaseBuilder(this.context, DB_MOTHERBOARDS.class, "MOTHERBOARDS").build();
        db_processors = Room.databaseBuilder(this.context, DB_PROCESSORS.class, "PROCESSORS").build();
        db_videocards = Room.databaseBuilder(this.context, DB_VIDEOCARDS.class, "VIDEOCARDS").build();
        db_ozus = Room.databaseBuilder(this.context, DB_OZUS.class, "OZUS").build();
        db_bps = Room.databaseBuilder(this.context, DB_BPS.class, "BPS").build();
        db_CASES = Room.databaseBuilder(this.context, DB_CASES.class, "PCCASES").build();
        db_coolers = Room.databaseBuilder(this.context, DB_COOLERS.class, "COOLERS").build();
        db_hdds = Room.databaseBuilder(this.context, DB_HDDS.class, "HDDS").build();
        db_ssds = Room.databaseBuilder(this.context, DB_SSDS.class, "SSDS").build();
    }


    @Override
    protected Void doInBackground(Void... params) {

        // проверяем, нужно ли обновлять данные


        Log.d("UpdateDBStatus", "Обновление успешно!");
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        Intent intent = new Intent(splashScreen, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void getBenchmarks(final Cpu CPU) {

        firestore.collection("Processors Benchmarks")
                .document(CPU.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                CPU.setGeekbench_multi(Integer.valueOf(doc.getData().get("Multi").toString()));
                                CPU.setGeekbench_single(Integer.valueOf(doc.getData().get("Single").toString()));
                                CPU.setLastUpdate(new Date().getTime() / 86400000);
                                db_processors.getMyDao().addCPU(CPU);
                            }
                            else Log.d(TAG, "Нет такого документа");
                        }
                        else {
                            Log.d(TAG, "Ошибка");
                        }
                    }
                });
    }

    // получить список URL-ов

    private List<String> getUrlList(String page)
    {
        List<String> urls = new ArrayList<>();

        Document docUrls;

        for (int i = 1; true; ++i){
            String current_page = page + "?p=" + i;         // создание ссылки на текущую страницу

            try {
                docUrls = Jsoup
                        .connect(current_page)
                        .userAgent("Chrome/4.0.249.0")
                        .get();
            } catch (IOException e) {
                break;
            }

            try {
                Elements elements = docUrls.select("div.ProductCardHorizontal__image-block > a");

                for (Element element : elements) {
                    urls.add(element.attr("href"));
                }
            }
            catch (Exception e){

            }

        }

        urls.size();
        return urls;
    }

}