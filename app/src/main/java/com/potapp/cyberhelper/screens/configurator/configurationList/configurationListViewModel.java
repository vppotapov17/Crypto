package com.potapp.cyberhelper.screens.configurator.configurationList;

import android.app.AlertDialog;
import android.app.Application;
import android.os.Handler;
import android.telecom.Call;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;
import com.potapp.cyberhelper.screens.configurator.creatingConfiguration.creatingConfigurationFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class configurationListViewModel extends AndroidViewModel {

    FragmentManager fm;

    private DB_CONFIGURATIONS db_configurations;

    private MutableLiveData<List<Configuration>> liveData;                                          // лайвдата со списком конфигураций
    private List<Configuration> configurationList;

    public configurationListViewModel(Application app, FragmentManager fm) {
        super(app);

        this.fm = fm;
        liveData = new MutableLiveData<>();
        db_configurations = Room.databaseBuilder(getApplication().getApplicationContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS").build();
        configurationList = new ArrayList<>();
        Callable<List<Configuration>> getConfigurationList = () -> db_configurations.getMyDao().getConfiguration();

        Observable.fromCallable(getConfigurationList)
                .subscribeOn(Schedulers.io())
                .delay(800, TimeUnit.MILLISECONDS)
                .subscribe(configurations -> {
                    configurationList.clear();
                    configurationList.addAll(configurations);
                    liveData.postValue(configurationList);
                });
    }


    // создание новой конфигурации
    void CreateConfiguration(AlertDialog dialog, EditText insertName){
        String confName = insertName.getText().toString();                                          // имя конфигурации, введённое с клавиатуры

        // защита от ввода некорректных данных
        if(confName.isEmpty()) insertName.setError("Введите имя сборки");
        // проверка на наличие сборки с таким именем
        else {
            Callable<Boolean> isConfigurationExists = () -> {
                boolean isExist = false;

                for (Configuration configuration : configurationList)
                {
                    if (confName.equals(configuration.name)) isExist = true;
                }
                return isExist;
            };

            Observable.fromCallable(isConfigurationExists)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                       if (aBoolean) insertName.setError("Эта сборка уже существует");
                       else {
                           final Configuration current_configuration = new Configuration();                        // создание нового объекта класса Configuration
                           current_configuration.name = (insertName.getText().toString());                         // инициализация поля "Name" (имя конфигурации)

                           current_configuration.isReady = false;
                           configurationList.add(current_configuration);
                           liveData.setValue(configurationList);

                           Callable<Boolean> addConfigurationToDb = ()->{
                               db_configurations.getMyDao().addConfiguration(current_configuration);
                               return true;
                           };

                           Observable.fromCallable(addConfigurationToDb)
                                   .subscribeOn(Schedulers.io())
                                   .subscribe(aBoolean1 -> {

                                   });

                           FragmentTransaction ft = fm.beginTransaction();
                           Fragment ConfigurationMenu = creatingConfigurationFragment.newInstance(current_configuration);

                           ft.addToBackStack(null);
                           ft.replace(R.id.fragment_container, ConfigurationMenu);
                           dialog.dismiss();
                           ft.commit();
                       }
                    });
        }
    }

    public LiveData<List<Configuration>> getLiveData(){
        return liveData;
    }
}
