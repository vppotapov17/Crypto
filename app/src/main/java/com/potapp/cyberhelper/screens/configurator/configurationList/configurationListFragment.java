package com.potapp.cyberhelper.screens.configurator.configurationList;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.ConfiguratorAdapters.configurationListAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class configurationListFragment extends Fragment {

    configurationListViewModel vm;                                                                   // ViewModel

    public configurationListFragment() {}

    public static configurationListFragment newInstance() {
        return new configurationListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(this, new configurationListFactory(getActivity().getApplication(), getFragmentManager()))
                .get(configurationListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.configurator_list_configuration, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();


        // инициализация элементов интерфейса
        TextView createFirstConfiguration = getActivity().findViewById(R.id.createFirstConfiguration);  // сообщение об отсутсвии конфигураций
        RecyclerView rv = getActivity().findViewById(R.id.rv);                                      // recyclerView
        ShimmerFrameLayout shimmer = getActivity().findViewById(R.id.shimmer);                      // shimmer-загрузка
        FloatingActionButton add_conf_button = getActivity().findViewById(R.id.add_conf_button);    // кнопка создания новой конфигурации


        if (rv.getAdapter() == null) {
            rv.setVisibility(View.INVISIBLE);                                                       // скрываем RecyclerView
            createFirstConfiguration.setVisibility(View.INVISIBLE);                                 // скрываем сообщение об отсутствии конфигураций
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmerAnimation();                                                        // запускаем shimmer-анимацию
        }

        // подписываемся на изменения лайвдата
        vm.getLiveData().observe(this, s -> {
            // останавливаем и скрываем shimmer
            shimmer.stopShimmerAnimation();
            shimmer.setVisibility(View.INVISIBLE);

            if (rv.getAdapter() == null) {
                if (s.size() == 0) {
                    createFirstConfiguration.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.VISIBLE);
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv.setAdapter(new configurationListAdapter(s, getFragmentManager(), rv, createFirstConfiguration));
                }
            }

        });

        // диалоговое окно

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_configuration_name, null);
        builder.setView(view);

        // инициалиазция элементов интерфейса диалогового окна
        EditText insertName = view.findViewById(R.id.insertName);
        Button ok_action = view.findViewById(R.id.ok);
        Button cancel_action = view.findViewById(R.id.cancel);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        insertName.requestFocus();
        // нажатие на Enter
        insertName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                vm.CreateConfiguration(dialog, insertName);
                return true;
            }
            return false;
        });

        // нажатие на "ОК"
        ok_action.setOnClickListener(view1 -> vm.CreateConfiguration(dialog, insertName));

        // нажатия на "Отмена"
        cancel_action.setOnClickListener(view1 -> dialog.dismiss());

        // нажатие на кнопку создания конфигураций
        add_conf_button.setOnClickListener(view12 -> dialog.show());
    }
}







