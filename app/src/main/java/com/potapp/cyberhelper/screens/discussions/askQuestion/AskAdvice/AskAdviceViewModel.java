package com.potapp.cyberhelper.screens.discussions.askQuestion.AskAdvice;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;
import com.potapp.cyberhelper.models.Configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AskAdviceViewModel extends AndroidViewModel {

    private Integer publishedQuestionsQuantity;
    private MutableLiveData<List<Configuration>> configurationListLiveData;

    public AskAdviceViewModel(@NonNull Application application) {
        super(application);

        configurationListLiveData = new MutableLiveData<>();

        // получение списка готовых сборок из базы данных
        DB_CONFIGURATIONS db_configurations = Room.databaseBuilder(application.getApplicationContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS").build();

        Callable<List<Configuration>> getReadyConfigurations = () -> {
            List<Configuration> configurations = new ArrayList<>();
            for (Configuration configuration : db_configurations.getMyDao().getConfiguration())
            {
                if (configuration.isReady) configurations.add(configuration);
            }
            return configurations;
        };

        Observable.fromCallable(getReadyConfigurations)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configurations -> {
                    configurationListLiveData.setValue(configurations);
                });

        // количество опубликованных вопросов
        FirebaseDatabase.getInstance().getReference("Data/Questions/Advice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                publishedQuestionsQuantity = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // добавление в базу информации о вопросе
    public void publishQuestion(String authorUID, String text, Configuration configuration){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Data/Questions/Advice/" + (publishedQuestionsQuantity + 1));

        reference.child("AuthorUID").setValue(authorUID);
        reference.child("Date").setValue(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
        reference.child("Text").setValue(text);

        // добавление в базу информации о конфигурации
        reference.child("Configuration").child(configuration.name).setValue(configuration.convertToFirebase());
    }

    public LiveData<List<Configuration>> getConfigurationListLiveData(){
        return configurationListLiveData;
    }


}
