package com.potapp.cyberhelper.screens.readyConfigurations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.potapp.cyberhelper.R;

public class MainReadyConfigurations extends Fragment {



    public MainReadyConfigurations() {

    }


    public static MainReadyConfigurations newInstance() {
       return new MainReadyConfigurations();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.readyconfiguration_fragment, container, false);
    }
}