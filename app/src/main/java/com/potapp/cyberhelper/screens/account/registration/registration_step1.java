package com.potapp.cyberhelper.screens.account.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.account.RegistrationFragment;

public class registration_step1 extends Fragment {

    public registration_step1(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.account_registration_step1, container, false);
    }

    public RegistrationFragment newInstance(Bundle args){
        return new RegistrationFragment();
    }

}
