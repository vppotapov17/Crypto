package com.potapp.cyberhelper.screens.configurator.creatingConfiguration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.ConfiguratorAdapters.creatingConfigurationAdapter;
import com.google.android.material.button.MaterialButton;

import com.potapp.cyberhelper.data.room.dbs.DB_CONFIGURATIONS;

public class creatingConfigurationFragment extends Fragment {

    LinearLayoutManager llm;
    int positionIndex;

    Configuration current_configuration;                                                            // текущая конфигурация

    public creatingConfigurationFragment() {}


    public static creatingConfigurationFragment newInstance(Configuration current_configuration) {

        creatingConfigurationFragment fragment = new creatingConfigurationFragment();

        Bundle args = new Bundle();
        args.putSerializable("current_configuration", current_configuration);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        current_configuration = (Configuration) getArguments().getSerializable(("current_configuration"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.configurator_creating_configuration, container, false);
    }
    
    @Override
    public void onStart(){
        super.onStart();

        // элементы интерфейса
        TextView confName = getActivity().findViewById(R.id.confName);                              // имя сборки
        TextView fullPrice = getActivity().findViewById(R.id.fullPrice);                            // общая стоимость сборки

        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);

        // название конфигурации

        confName.setText(current_configuration.name);

        // общая стоимость конфигурации

        fullPrice.setText(current_configuration.getFullPrice() + " ₽");

        // кнопка "ИЗМЕНИТЬ/УДАЛИТЬ"
        final MaterialButton button = getActivity().findViewById(R.id.button);

        final creatingConfigurationAdapter creatingConfigurationAdapter = new creatingConfigurationAdapter(current_configuration, getActivity().getSupportFragmentManager(), fullPrice);

        // обработка списка
        final RecyclerView rv = getActivity().findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rv.setAdapter(creatingConfigurationAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 500);


        // обработка нажатия на кнопку "ГОТОВО"
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // если обязательные комплектующие не добавлены, отображается соответствующее сообщение
                if (isConfig_Ready(current_configuration))
                {
                    current_configuration.isReady = true;
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Room.databaseBuilder(getContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS").build().getMyDao().updateConfiguration(current_configuration);
                        }
                    });
                    t.start();

                    Toast.makeText(getContext(), "Сборка завершена!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else Toast.makeText(getActivity().getApplicationContext(), "Отсутствуют обязательные комплектующие", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // проверка конфигуарции на наличие обязательных комплектующих
    public boolean isConfig_Ready(Configuration configuration)
    {
        boolean isReady = true;

        if (configuration.mCpu == null) isReady = false;
        if (configuration.mGpu == null) isReady = false;
        if (configuration.mMb == null) isReady = false;
        if (configuration.mBp == null) isReady = false;
        if (configuration.mOzu == null) isReady = false;
        if (configuration.mCooler == null) isReady = false;

        boolean isStorage = false;                                                                  // наличие хотя бы одного накопителя

        if (configuration.mHdd != null) isStorage = true;
        if (configuration.mSsd_25 != null) isStorage = true;
        if (configuration.mSsd_m2 != null) isStorage = true;

        if (!isStorage) isReady = false;

        return isReady;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        positionIndex = llm.findFirstVisibleItemPosition();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (positionIndex != -1)
            llm.scrollToPosition(positionIndex);
    }

}