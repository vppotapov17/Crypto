package com.potapp.cyberhelper.screens.configurator.creatingConfiguration;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;
import com.potapp.cyberhelper.models.Configuration;

public class creatingConfigurationViewModel extends AndroidViewModel {
    private Configuration current_configuration;

    public creatingConfigurationViewModel(@NonNull Application application, Configuration current_configuration) {
        super(application);

        this.current_configuration = current_configuration;

    }

    boolean onDoneButtonClick(){
        // если обязательные комплектующие не добавлены, отображается соответствующее сообщение
        if (current_configuration.isReady())
        {
            current_configuration.isReady = true;
            Thread t = new Thread(() -> Room.databaseBuilder(getApplication().getApplicationContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS")
                    .build().getMyDao().updateConfiguration(current_configuration));
            t.start();

            return true;
        }
        return false;
    }

    public Configuration getCurrent_configuration(){
        return current_configuration;
    }
}
