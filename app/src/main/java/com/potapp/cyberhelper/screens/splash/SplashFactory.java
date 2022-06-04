package com.potapp.cyberhelper.screens.splash;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class SplashFactory extends ViewModelProvider.AndroidViewModelFactory {

    public SplashFactory(@NonNull Application application) {
        super(application);
    }
}
