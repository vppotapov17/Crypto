package com.potapp.cyberhelper.screens.configurator.componentInfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.models.components.Component;

public class componentInfoFactory implements ViewModelProvider.Factory {

    Application app;
    NavController navController;
    Component select_component;
    Configuration current_configuration;

    public componentInfoFactory(Application app, NavController navController, Component select_component, Configuration current_configuration){
        this.app = app;
        this.navController = navController;
        this.select_component = select_component;
        this.current_configuration = current_configuration;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new componentInfoViewModel(app, navController, select_component, current_configuration);
    }
}
