package com.potapp.cyberhelper.screens.configurator.componentInfo;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.ConfiguratorAdapters.specsAdapter;
import com.potapp.cyberhelper.data.models.components.*;
import com.nambimobile.widgets.efab.FabOption;

public class componentInfoFragment extends Fragment {

    componentInfoViewModel viewModel;

    Component select_component;
    Configuration current_configuration;

    public componentInfoFragment(){}

    public static componentInfoFragment newInstance(Component select_component, Configuration current_configuration) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("select_component", select_component);
        bundle.putSerializable("current_configuration", current_configuration);

        componentInfoFragment fragment = new componentInfoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        select_component = (Component) this.getArguments().getSerializable("select_component");
        current_configuration = (Configuration)this.getArguments().getSerializable("current_configuration");

        viewModel = new ViewModelProvider(getActivity(), new componentInfoFactory(getActivity().getApplication(), getFragmentManager(),
                select_component, current_configuration)).get(componentInfoViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;

        if (viewModel.viewAddedComponent())
            view = inflater.inflate(R.layout.configurator_info_added_component, container, false);
        else view = inflater.inflate(R.layout.configurator_info_component, container, false);

        return view;
    }


    public void onResume(){
        super.onResume();


        // инициализация элементов интерфейса
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);                                 // тулбар
        ImageView component_image = getActivity().findViewById(R.id.photo_staticInfo);              // изображение компонента
        TextView component_price = getActivity().findViewById(R.id.price);                          // стоимость компонента

        FabOption buy_button = getView().findViewById(R.id.buy_button);                             // кнопка "Купить"
        FabOption add_button = getView().findViewById(R.id.add_button);                             // кнопка "Добавить"

        RecyclerView rv = getActivity().findViewById(R.id.rv);                                      // список характеристик


        // тулбар
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(select_component.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.onBackClick();
            }
        });


        // изображение компонента
        Glide
                .with(getContext())
                .load(viewModel.getImageUrl())
                .error(R.drawable.ic_cpu)
                .into(component_image);


        // стоимость компонента
        component_price.setText(select_component.getPrice() + " ₽");


        // обработка списка
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new specsAdapter(select_component.specifications(), select_component instanceof Gpu));


        // обработка нажатия на кнопку "добавить"
        if (!viewModel.viewAddedComponent()) {
            add_button.setClickable(true);
            add_button.setOnClickListener(view -> {
                viewModel.onAddButtonClick();
            });
        }


        // обработка нажатия на кнопку "Купить"
        buy_button.setClickable(true);
        buy_button.setOnClickListener(view -> {
            viewModel.onBuyButtonClick();
        });
    }

}