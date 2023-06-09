package com.potapp.cyberhelper.screens.configurator.creatingConfiguration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.potapp.cyberhelper.models.Configuration;

public class creatingConfigurationFactory implements ViewModelProvider.Factory {

    private Configuration current_configuration;
    private Application application;

    public creatingConfigurationFactory(Application application, Configuration current_configuration) {
        this.application = application;
        this.current_configuration = current_configuration;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new creatingConfigurationViewModel(application, current_configuration);
    }
}
