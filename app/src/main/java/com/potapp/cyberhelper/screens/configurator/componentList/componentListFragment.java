package com.potapp.cyberhelper.screens.configurator.componentList;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.ConfiguratorAdapters.componentListAdapter;
import com.potapp.cyberhelper.models.components.Component;
import com.potapp.cyberhelper.models.components.Mb;
import com.google.android.material.appbar.MaterialToolbar;
import com.yandex.mobile.ads.nativeads.NativeAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class componentListFragment extends Fragment {

    componentListViewModel viewModel;

    LinearLayoutManager llm;
    int positionIndex;

    Configuration current_configuration;
    Class component_class;

    AlertDialog filterDialog;

    HashMap<String, List<String>> savedFilter;
    List<NativeAd> adList;

    public componentListFragment() {
        // Required empty public constructor
    }

    public static componentListFragment newInstance(Configuration current_configuration, Class component_class) {
        componentListFragment fragment = new componentListFragment();
        Bundle args = new Bundle();

        args.putSerializable("current_configuration", current_configuration);
        args.putSerializable("component_class", component_class);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            this.current_configuration = (Configuration) getArguments().getSerializable("current_configuration");
            this.component_class = (Class)getArguments().getSerializable("component_class");
        }

        componentListFactory factory = new componentListFactory(getActivity().getApplication(), getLayoutInflater(), current_configuration, component_class);
        viewModel = ViewModelProviders.of(this, factory).get(componentListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("AAA", "OnCreateView");

        if (savedFilter != null) Log.d("AAA", savedFilter.toString());

        return inflater.inflate(R.layout.configurator_list_component, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("AAA", "OnStart");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("AAA", "OnResume");

        // инициализация элементов интерфейса
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);

        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);
        RecyclerView rv = getActivity().findViewById(R.id.rv);
        ImageButton filterButton = getActivity().findViewById(R.id.filter_button);
        MaterialToolbar toolbar = getActivity().findViewById(R.id.toolbar);

        ConstraintLayout no_components = getActivity().findViewById(R.id.no_components);

        TextView title = getActivity().findViewById(R.id.title);

        title.setText(viewModel.getCurrentTitle());



        // тулбар
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view1 -> getFragmentManager().popBackStack());

        // список
        llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);


        // подиска на обновление ViewModel
        viewModel.getComponentListLiveData().observe(this, components -> {
            new Handler().postDelayed(() -> {


                if (components.size() != 0){
                    no_components.setVisibility(View.INVISIBLE);
                    rv.setVisibility(View.VISIBLE);

                    if (adList == null){
                        viewModel.getAdListLiveData().observe(this, nativeAds -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            adList = nativeAds;
                            rv.setAdapter(new componentListAdapter(components, nativeAds, getFragmentManager(), current_configuration));
                        });
                        viewModel.loadNativeAdList(getContext(), components.size());
                    }
                    else{
                        rv.setAdapter(new componentListAdapter(components, adList, getFragmentManager(), current_configuration));
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    no_components.setVisibility(View.VISIBLE);
                }


            }, 150);
        });



        LinearLayout filterLayout = getActivity().findViewById(R.id.filterLayout);

        // фильтры
        // материнская плата
        new Handler().postDelayed(()->{

            View filterView;

            if (component_class == Mb.class){

                filterView = getLayoutInflater().inflate(R.layout.dialog_filter_mb, filterLayout, false);
                if (filterLayout.getChildCount() == 0) filterLayout.addView(filterView);

                // получаем фильтры из ViewModel
                HashMap<String, List<String>> filters = viewModel.getMbFilters();

                // сокет
                ChipGroup socketChipGroup = filterView.findViewById(R.id.socketChipGroup);
                socketChipGroup.removeAllViews();

                List<String> sockets = filters.get("Сокет");

                if (sockets.size() == 1){
                    Chip chip = new Chip(getContext());
                    chip.setText(sockets.get(0));
                    chip.setChecked(true);
                    chip.setCheckable(false);
                    chip.setId(View.generateViewId());
                    socketChipGroup.addView(chip);
                }
                else
                {
                    for(String socket : sockets){
                        Chip chip = new Chip(getContext());
                        chip.setText(socket);
                        socketChipGroup.addView(chip);
                    }
                }

                // чипсет
                ChipGroup chipsetChipGroup = filterView.findViewById(R.id.chipsetChipGroup);
                TextView chipsetTitle = filterView.findViewById(R.id.chipsetTitle);

                if (sockets.size() == 1){
                    // показываем ChipGroup с чипсетами
                    chipsetChipGroup.setVisibility(View.VISIBLE);
                    chipsetTitle.setVisibility(View.VISIBLE);

                    Chip chip = new Chip(getContext());
                    chip.setText(sockets.get(0));
                    List<String> chipsets = viewModel.getChipsets(sockets.get(0));
                    chipsetChipGroup.removeAllViews();

                    if (chipsets.size() == 1){
                        Chip chip1 = new Chip(getContext());
                        chip1.setText(sockets.get(0));
                        chip1.setChecked(true);
                        chip1.setCheckable(false);
                        chipsetChipGroup.addView(chip1);
                    }
                    else
                    {
                        for(String chipset : chipsets){
                            Chip chip1 = new Chip(getContext());
                            chip1.setText(chipset);
                            chipsetChipGroup.addView(chip1);
                        }
                    }
                }

                socketChipGroup.setOnCheckedChangeListener((group, checkedId) -> {

                    chipsetChipGroup.removeAllViews();

                    // если сокет не выбран, скрываем выбор чипсета
                    if (checkedId == View.NO_ID){
                        chipsetChipGroup.setVisibility(View.GONE);
                        chipsetTitle.setVisibility(View.GONE);
                    }
                    else {
                        // показываем ChipGroup с чипсетами


                        chipsetChipGroup.setVisibility(View.VISIBLE);
                        chipsetTitle.setVisibility(View.VISIBLE);

                        Chip chip = socketChipGroup.findViewById(checkedId);
                        List<String> chipsets = viewModel.getChipsets(chip.getText().toString());

                        if (chipsets.size() == 1){
                            Chip chip1 = new Chip(getContext());
                            chip1.setText(sockets.get(0));
                            chip1.setChecked(true);
                            chip1.setCheckable(false);
                            chipsetChipGroup.addView(chip1);
                        }
                        else
                        {
                            for(String chipset : chipsets){
                                Chip chip1 = new Chip(getContext());
                                chip1.setText(chipset);
                                chipsetChipGroup.addView(chip1);
                            }
                        }
                    }
                });

                // форм-фактор
                ChipGroup formFactorChipGroup = filterView.findViewById(R.id.formFactorChipGroup);
                formFactorChipGroup.removeAllViews();

                List<String> formFactors = filters.get("Форм-фактор");

                if (formFactors.size() == 1){
                    Chip chip = new Chip(getContext());
                    chip.setText(formFactors.get(0));
                    chip.setChecked(true);
                    chip.setCheckable(false);
                    formFactorChipGroup.addView(chip);
                }
                else
                {
                    for(String formFactor : formFactors){
                        Chip chip = new Chip(getContext());
                        chip.setText(formFactor);
                        formFactorChipGroup.addView(chip);
                    }
                }

                // тип памяти
                ChipGroup ozuTypeChipGroup = filterView.findViewById(R.id.ozuTypeChipGroup);
                ozuTypeChipGroup.removeAllViews();

                List<String> ozuTypes = filters.get("Тип памяти");

                if (ozuTypes.size() == 1){
                    Chip chip = new Chip(getContext());
                    chip.setText(ozuTypes.get(0));
                    chip.setChecked(true);
                    chip.setCheckable(false);
                    ozuTypeChipGroup.addView(chip);
                }
                else
                {
                    for(String ozuType : ozuTypes){
                        Chip chip = new Chip(getContext());
                        chip.setText(ozuType);
                        ozuTypeChipGroup.addView(chip);
                    }
                }

                // количество слотов памяти
                ChipGroup ozuSlotsQuantityChipGroup = filterView.findViewById(R.id.ozuSlotsQuantityChipGroup);
                ozuSlotsQuantityChipGroup.removeAllViews();

                List<String> ozuSlotsQuantities = filters.get("Количество слотов памяти");

                if (ozuSlotsQuantities.size() == 1){
                    Chip chip = new Chip(getContext());
                    chip.setText(ozuSlotsQuantities.get(0));
                    chip.setChecked(true);
                    chip.setCheckable(false);
                    ozuSlotsQuantityChipGroup.addView(chip);
                }
                else
                {
                    for(String ozuSlotsQuantity : ozuSlotsQuantities){
                        Chip chip = new Chip(getContext());
                        chip.setText(ozuSlotsQuantity);
                        ozuSlotsQuantityChipGroup.addView(chip);
                    }
                }


                // разъёмы M2
                ChipGroup m2QuantityGroup = filterView.findViewById(R.id.M2QuantityChipGroup);
                m2QuantityGroup.removeAllViews();

                List<String> m2Quantities = filters.get("Разъёмы M2");

                if (m2Quantities.size() == 1){
                    Chip chip = new Chip(getContext());
                    chip.setText(m2Quantities.get(0));
                    chip.setChecked(true);
                    chip.setCheckable(false);
                    m2QuantityGroup.addView(chip);
                }
                else
                {
                    for(String m2Quantity : m2Quantities){
                        Chip chip = new Chip(getContext());
                        chip.setText(m2Quantity);
                        m2QuantityGroup.addView(chip);
                    }
                }

                // разъёмы SATA3
                ChipGroup sataQuantityGroup = filterView.findViewById(R.id.SataQuantityChipGroup);
                sataQuantityGroup.removeAllViews();

                List<String> sataQuantities = filters.get("Разъёмы SATA3");

                if (sataQuantities.size() == 1){
                    Chip chip = new Chip(getContext());
                    chip.setText(sataQuantities.get(0));
                    chip.setChecked(true);
                    chip.setCheckable(false);
                    sataQuantityGroup.addView(chip);
                }
                else
                {
                    for(String sataQuantity : sataQuantities){
                        Chip chip = new Chip(getContext());
                        chip.setText(sataQuantity);
                        sataQuantityGroup.addView(chip);
                    }
                }

                // восстановление сохраненного фильтра
                if (savedFilter != null){
                    if (savedFilter.containsKey("Socket")){
                        for (String socket : savedFilter.get("Socket")){
                            for (int i = 0; i < socketChipGroup.getChildCount(); i++){
                                Chip c = (Chip)socketChipGroup.getChildAt(i);
                                if (socket.equals(c.getText().toString())) c.setChecked(true);
                            }
                        }
                    }
                    if (savedFilter.containsKey("Chipset")){
                        for (String chipset : savedFilter.get("Chipset")){
                            for (int i = 0; i < chipsetChipGroup.getChildCount(); i++){
                                Chip c = (Chip)chipsetChipGroup.getChildAt(i);
                                if (chipset.equals(c.getText().toString())) c.setChecked(true);
                            }
                        }
                    }
                    if (savedFilter.containsKey("FormFactor")){
                        for (String formFactor : savedFilter.get("FormFactor")){
                            for (int i = 0; i < formFactorChipGroup.getChildCount(); i++){
                                Chip c = (Chip)formFactorChipGroup.getChildAt(i);
                                if (formFactor.equals(c.getText().toString())) c.setChecked(true);
                            }
                        }
                    }

                    if (savedFilter.containsKey("OzuType")){
                        for (String ozuType : savedFilter.get("OzuType")){
                            for (int i = 0; i < ozuTypeChipGroup.getChildCount(); i++){
                                Chip c = (Chip)ozuTypeChipGroup.getChildAt(i);
                                if (ozuType.equals(c.getText().toString())) c.setChecked(true);
                            }
                        }
                    }
                    if (savedFilter.containsKey("OzuSlotsQuantity")){
                        for (String ozuSlotsQuantity : savedFilter.get("OzuSlotsQuantity")){
                            for (int i = 0; i < ozuSlotsQuantityChipGroup.getChildCount(); i++){
                                Chip c = (Chip)ozuSlotsQuantityChipGroup.getChildAt(i);
                                if (ozuSlotsQuantity.equals(c.getText().toString())) c.setChecked(true);
                            }
                        }
                    }
                    if (savedFilter.containsKey("M2Quantity")){
                        for (String m2Quantity : savedFilter.get("M2Quantity")){
                            for (int i = 0; i < m2QuantityGroup.getChildCount(); i++){
                                Chip c = (Chip)m2QuantityGroup.getChildAt(i);
                                if (m2Quantity.equals(c.getText().toString())) c.setChecked(true);
                            }
                        }
                    }
                    if (savedFilter.containsKey("SataQuantity")){
                        for (String sataQuantity : savedFilter.get("SataQuantity")){
                            for (int i = 0; i < sataQuantityGroup.getChildCount(); i++){
                                Chip c = (Chip)sataQuantityGroup.getChildAt(i);
                                if (sataQuantity.equals(c.getText().toString())) c.setChecked(true);
                            }
                        }
                    }

                    savedFilter = null;
                }

            }
            else filterView = null;

            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {
                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    rv.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    if (component_class == Mb.class) viewModel.applyMbFilters(readMbFilters(filterView));
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });

        }, 300);

        filterButton.setOnClickListener(view1 ->{
            try {
                drawerLayout.openDrawer(GravityCompat.END);

            }
            catch (Exception e){}
        });

        if (positionIndex != -1)
            llm.scrollToPosition(positionIndex);

    }

    HashMap<String, List<String>> readMbFilters(View view){

        HashMap<String, List<String>> filters = new HashMap<>();

        List<String> socketFilter = new ArrayList<>();
        ChipGroup socketGroup = view.findViewById(R.id.socketChipGroup);
        if (socketGroup != null){
            for (Integer id : socketGroup.getCheckedChipIds()){
                Chip c = view.findViewById(id);
                socketFilter.add(c.getText().toString());
            }

            if (!socketFilter.isEmpty()) filters.put("Socket", socketFilter);
        }

        List<String> chipsetFilter = new ArrayList<>();
        ChipGroup chipsetGroup = view.findViewById(R.id.chipsetChipGroup);
        if (chipsetGroup != null) {
            for (Integer id : chipsetGroup.getCheckedChipIds()) {
                Chip c = view.findViewById(id);
                chipsetFilter.add(c.getText().toString());
            }

            if (!chipsetFilter.isEmpty()) filters.put("Chipset", chipsetFilter);
        }

        List<String> formFactorFilter = new ArrayList<>();
        ChipGroup formFactorGroup = view.findViewById(R.id.formFactorChipGroup);
        if (chipsetGroup != null) {
            for (Integer id : formFactorGroup.getCheckedChipIds()) {
                Chip c = view.findViewById(id);
                formFactorFilter.add(c.getText().toString());
            }

            if (!formFactorFilter.isEmpty()) filters.put("FormFactor", formFactorFilter);
        }

        List<String> ozuTypeFilter = new ArrayList<>();
        ChipGroup ozuTypeGroup = view.findViewById(R.id.ozuTypeChipGroup);
        if (ozuTypeGroup != null) {
            for (Integer id : ozuTypeGroup.getCheckedChipIds()) {
                Chip c = view.findViewById(id);
                ozuTypeFilter.add(c.getText().toString());
            }

            if (!ozuTypeFilter.isEmpty()) filters.put("OzuType", ozuTypeFilter);
        }

        List<String> ozuSlotsQuantityFilter = new ArrayList<>();
        ChipGroup ozuSlotsQuantityGroup = view.findViewById(R.id.ozuSlotsQuantityChipGroup);
        if (ozuSlotsQuantityGroup != null) {
            for (Integer id : ozuSlotsQuantityGroup.getCheckedChipIds()) {
                Chip c = view.findViewById(id);
                ozuSlotsQuantityFilter.add(c.getText().toString());
            }

            if (!ozuSlotsQuantityFilter.isEmpty()) filters.put("OzuSlotsQuantity", ozuSlotsQuantityFilter);
        }

        List<String> m2QuantityFilter = new ArrayList<>();
        ChipGroup m2QuantityGroup = view.findViewById(R.id.M2QuantityChipGroup);
        if (m2QuantityGroup != null) {
            for (Integer id : m2QuantityGroup.getCheckedChipIds()) {
                Chip c = view.findViewById(id);
                m2QuantityFilter.add(c.getText().toString());
            }

            if (!m2QuantityFilter.isEmpty()) filters.put("M2Quantity", m2QuantityFilter);
        }

        List<String> sataQuantityFilter = new ArrayList<>();
        ChipGroup sataQuantityGroup = view.findViewById(R.id.SataQuantityChipGroup);
        if (sataQuantityGroup != null) {
            for (Integer id : sataQuantityGroup.getCheckedChipIds()) {
                Chip c = view.findViewById(id);
                sataQuantityFilter.add(c.getText().toString());
            }

            if (!sataQuantityFilter.isEmpty()) filters.put("SataQuantity", sataQuantityFilter);
        }

        return filters;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("AAA", "OnPause");
        positionIndex = llm.findFirstVisibleItemPosition();
    }

    @Override
    public void onStop(){
        super.onStop();
        LinearLayout ll = getActivity().findViewById(R.id.filterLayout);

        if (component_class == Mb.class) savedFilter = readMbFilters(ll.getChildAt(0));

        Log.d("AAA", "OnStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("AAA", "OnDestroy");
    }

    @Override
    public void onDetach(){
        super.onDetach();
        Log.d("AAA", "OnDetach");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();


        Log.d("AAA", "OnDestroyView");
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d("AAA", "OnAttach");
    }
}