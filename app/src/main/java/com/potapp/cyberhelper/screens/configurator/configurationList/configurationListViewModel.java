package com.potapp.cyberhelper.screens.configurator.configurationList;

import android.app.AlertDialog;
import android.app.Application;
import android.os.Handler;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.data.room.dbs.DB_CONFIGURATIONS;
import com.potapp.cyberhelper.screens.configurator.creatingConfiguration.creatingConfigurationFragment;

import java.util.List;

public class configurationListViewModel extends AndroidViewModel {

    FragmentManager fm;


    MutableLiveData<List<Configuration>> liveData;                                                  // лайвдата со списком конфигураций

    List<Configuration> configurationList;                                                          // список сохранённых конфигураций

    public configurationListViewModel(Application app, FragmentManager fm){
        super(app);

        this.fm = fm;

        liveData = new MutableLiveData<>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                configurationList = Room.databaseBuilder(getApplication().getApplicationContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS").build()
                        .getMyDao().getConfiguration();

                liveData.postValue(configurationList);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t.start();
            }
        }, 600);

    }

    // создание новой конфигурации
    void CreateConfiguration(AlertDialog dialog, EditText insertName){
        String confName = insertName.getText().toString();                                          // имя конфигурации, введённое с клавиатуры

        // защита от ввода некорректных данных
        if(confName.isEmpty()) insertName.setError("Введите имя сборки");
        else if(isConfigurationExist(confName)) insertName.setError("Эта сборка уже существует");
        else
        {
            final Configuration current_configuration = new Configuration();                        // создание нового объекта класса Configuration
            current_configuration.name = (insertName.getText().toString());                         // инициализация поля "Name" (имя конфигурации)

            current_configuration.isReady = false;

            configurationList.add(current_configuration);

            // запись конфигурации в базу данных
            new Thread(() -> Room.databaseBuilder(getApplication().getApplicationContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS")
                    .build().getMyDao().addConfiguration(current_configuration)).start();

            FragmentTransaction ft = fm.beginTransaction();
            Fragment ConfigurationMenu = creatingConfigurationFragment.newInstance(current_configuration);

            ft.addToBackStack(null);
            ft.replace(R.id.fragment_container, ConfigurationMenu);
            dialog.dismiss();
            ft.commit();
        }
    }

    // проверка существования конфигурации по имени
    public boolean isConfigurationExist(String name){

        boolean isExist = false;

        for(int i = 0; i < configurationList.size(); ++i)
        {
            if(name.equals(configurationList.get(i).name)) isExist = true;
        }

        return isExist;
    }
}
