package com.potapp.cyberhelper.screens.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;

import java.io.Serializable;

public class SplashActivity extends AppCompatActivity {

    SplashViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        vm = new ViewModelProvider(this, new SplashFactory(getApplication())).get(SplashViewModel.class);


    }

    @Override
    public void onResume()
    {
        super.onResume();


        vm.liveData.observe(this, s->{
            if (s[0] && s[1]){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("current_user", (Serializable) vm.current_user);
                startActivity(intent);
            }
        });
    }
}
