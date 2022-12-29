package com.potapp.cyberhelper.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("current_user", (Serializable) null);

        vm.getLiveData().observe(this, code->{
            if (code == 1) {
                startActivity(intent);
            }
            else if (code == -1)
            {
                Toast.makeText(getApplicationContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
