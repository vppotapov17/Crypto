package com.potapp.cyberhelper.screens.configurator.componentList;

import android.app.Application;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.potapp.cyberhelper.models.Configuration;

public class componentListFactory extends ViewModelProvider.AndroidViewModelFactory {

    Application app;
    LayoutInflater inflater;

    Configuration current_configuration;
    Class component_class;


    public componentListFactory(@NonNull Application app, LayoutInflater inflater, Configuration current_configuration,
                                Class component_class) {
        super(app);

        this.app = app;
        this.inflater = inflater;

        this.current_configuration = current_configuration;
        this.component_class = component_class;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new componentListViewModel(app, inflater, current_configuration, component_class);
    }
}
