package com.potapp.cyberhelper.screens.configurator.componentInfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.models.components.Component;

public class componentInfoFactory extends ViewModelProvider.AndroidViewModelFactory {

    Application app;
    FragmentManager fm;
    Component select_component;
    Configuration current_configuration;

    public componentInfoFactory(@NonNull Application app, FragmentManager fm, Component select_component, Configuration current_configuration){
        super(app);
        this.app = app;
        this.fm = fm;
        this.select_component = select_component;
        this.current_configuration = current_configuration;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new componentInfoViewModel(app, fm, select_component, current_configuration);
    }
}
