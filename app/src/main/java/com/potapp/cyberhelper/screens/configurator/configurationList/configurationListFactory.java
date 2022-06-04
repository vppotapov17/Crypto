package com.potapp.cyberhelper.screens.configurator.configurationList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class configurationListFactory extends ViewModelProvider.AndroidViewModelFactory {
    Application app;
    FragmentManager fm;

    public configurationListFactory(@NonNull Application app, FragmentManager fm){
        super(app);
        this.app = app;
        this.fm = fm;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new configurationListViewModel(app, fm);
    }

}
