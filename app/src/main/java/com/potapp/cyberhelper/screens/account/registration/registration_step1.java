package com.potapp.cyberhelper.screens.account.registration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    @Override
    public void onResume(){
        super.onResume();

        Log.d("AAA", "Step1 Resume");
        NavController controller = NavHostFragment.findNavController(this);


        FloatingActionButton fab = getActivity().findViewById(R.id.nextButton1);

        fab.setClickable(true);
        fab.setOnClickListener(view -> {
            Log.d("AAA", "Кнопка нажата");
            controller.navigate(R.id.action_registrationFragmentStep1_to_registration_step2);
        });
    }
}
