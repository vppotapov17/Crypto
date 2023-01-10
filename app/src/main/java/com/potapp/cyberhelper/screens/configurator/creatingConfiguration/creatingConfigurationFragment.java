package com.potapp.cyberhelper.screens.configurator.creatingConfiguration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.ConfiguratorAdapters.creatingConfigurationAdapter;
import com.google.android.material.button.MaterialButton;

import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;

public class creatingConfigurationFragment extends Fragment {

    creatingConfigurationViewModel viewModel;
    LinearLayoutManager llm;
    int positionIndex;

    Configuration current_configuration;                                                            // текущая конфигурация

    public creatingConfigurationFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        current_configuration = (Configuration) getArguments().getSerializable("current_configuration");

        creatingConfigurationFactory factory = new creatingConfigurationFactory(getActivity().getApplication(), current_configuration);
        viewModel = ViewModelProviders.of(this, factory).get(creatingConfigurationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.configurator_creating_configuration, container, false);
    }
    
    @Override
    public void onResume(){
        super.onResume();

        // элементы интерфейса
        TextView confName = getView().findViewById(R.id.confName);                              // имя сборки
        TextView fullPrice = getView().findViewById(R.id.fullPrice);                            // общая стоимость сборки
        MaterialButton button = getView().findViewById(R.id.button);                            // кнопка "ГОТОВО"
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);

        // общая стоимость сборки
        fullPrice.setText(current_configuration.getFullPrice() + " ₽");

        // имя сборки
        confName.setText(current_configuration.name);

        // обработка списка
        final RecyclerView rv = getView().findViewById(R.id.rv);

        llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        new Handler().postDelayed(()->{
            Log.d("AAA", current_configuration.name);
            rv.setAdapter(new creatingConfigurationAdapter(viewModel.getCurrent_configuration(), NavHostFragment.findNavController(this), getContext(), fullPrice));
            progressBar.setVisibility(View.INVISIBLE);

            if (rv.getAdapter() != null) Log.d("AAA", "Adapter is set");
            }, 200);



        // обработка нажатия на кнопку "ГОТОВО"
        button.setOnClickListener(view->{
            if (viewModel.onDoneButtonClick())
            {
                Toast.makeText(getContext(), "Сборка завершена!", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
            else Toast.makeText(getContext(), "Отсутствуют обязательные комплектующие!", Toast.LENGTH_SHORT).show();
        });


        if (positionIndex != -1)
            llm.scrollToPosition(positionIndex);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        positionIndex = llm.findFirstVisibleItemPosition();
    }
}