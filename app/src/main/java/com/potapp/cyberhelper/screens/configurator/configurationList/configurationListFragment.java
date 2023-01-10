package com.potapp.cyberhelper.screens.configurator.configurationList;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
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
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.template.HorizontalOffset;
import com.yandex.mobile.ads.nativeads.template.NativeBannerView;
import com.yandex.mobile.ads.nativeads.template.appearance.BannerAppearance;
import com.yandex.mobile.ads.nativeads.template.appearance.ButtonAppearance;
import com.yandex.mobile.ads.nativeads.template.appearance.NativeTemplateAppearance;
import com.yandex.mobile.ads.nativeads.template.appearance.TextAppearance;


public class configurationListFragment extends Fragment {

    configurationListViewModel vm;                                                                   // ViewModel

    public configurationListFragment() {}

    public static configurationListFragment newInstance() {
        return new configurationListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configurationListFactory factory = new configurationListFactory(getActivity().getApplication(), getActivity().getSupportFragmentManager());


        vm = ViewModelProviders.of(this, factory).get(configurationListViewModel.class);

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
        TextView createFirstConfiguration = getView().findViewById(R.id.createFirstConfiguration);  // сообщение об отсутсвии конфигураций
        RecyclerView rv = getView().findViewById(R.id.rv_list_conf);                                      // recyclerView
        ShimmerFrameLayout shimmer = getView().findViewById(R.id.shimmer_list_conf);                      // shimmer-загрузка
        FloatingActionButton add_conf_button = getView().findViewById(R.id.add_conf_button);    // кнопка создания новой конфигурации

        createFirstConfiguration.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.INVISIBLE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();


        new Handler().postDelayed(()->{
            // подписываемся на изменения лайвдата
            vm.getLiveData().observe(this, s -> {
                Log.d("AAA", "Livedata updated");
                // останавливаем и скрываем shimmer
                shimmer.stopShimmerAnimation();
                shimmer.setVisibility(View.INVISIBLE);


                if (s.size() == 0) {
                    Log.d("AAA", "Конфигураций нет");
                    createFirstConfiguration.setVisibility(View.VISIBLE);
                } else {
                    Log.d("AAA", "Конфигурации есть");
                    rv.setVisibility(View.VISIBLE);
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv.setAdapter(new configurationListAdapter(s, getActivity().getSupportFragmentManager(), rv, createFirstConfiguration));
                }

            });
        }, 400);




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







