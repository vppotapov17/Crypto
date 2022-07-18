package com.potapp.cyberhelper.screens.configurator.viewReadyConfiguration;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.MainActivity;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.ConfiguratorAdapters.viewReadyConfigurationAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;
import com.potapp.cyberhelper.database.daos.DAO_CONFIGURATIONS;

public class viewReadyConfigurationFragment extends Fragment {

    Configuration current_configuration;                                                            // текущая конфигурация

    DAO_CONFIGURATIONS dao_configurations;                                                          // база созданных сборок

    public viewReadyConfigurationFragment() {

    }

    public static viewReadyConfigurationFragment newInstance(Configuration current_configuration) {

        viewReadyConfigurationFragment fragment = new viewReadyConfigurationFragment();

        Bundle args = new Bundle();
        args.putSerializable("current_configuration", current_configuration);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao_configurations = Room.databaseBuilder(getContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS").build().getMyDao();
        current_configuration = (Configuration) getArguments().getSerializable(("current_configuration"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.configurator_view_ready_configuration, container, false);
    }
    
    @Override
    public void onResume(){
        super.onResume();

        // название конфигурации
        TextView confName = getActivity().findViewById(R.id.confName);
        confName.setText(current_configuration.name);

        // общая стоимость конфигурации
        TextView fullPrice = getActivity().findViewById(R.id.fullPrice);
        fullPrice.setText(current_configuration.getFullPrice() + " ₽");

        // кнопка назад
        ImageButton back = getActivity().findViewById(R.id.back);
        back.setOnClickListener(view -> getFragmentManager().popBackStack());


        final viewReadyConfigurationAdapter viewReadyConfigurationAdapter = new viewReadyConfigurationAdapter(current_configuration, getActivity().getSupportFragmentManager(), fullPrice, getContext());

        // обработка списка
        final RecyclerView rv = getActivity().findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(viewReadyConfigurationAdapter);
    }
}