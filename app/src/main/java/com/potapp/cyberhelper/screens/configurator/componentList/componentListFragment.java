package com.potapp.cyberhelper.screens.configurator.componentList;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.adapters.ConfiguratorAdapters.componentListAdapter;
import com.potapp.cyberhelper.data.models.components.Mb;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.MaterialToolbar;

public class componentListFragment extends Fragment {

    componentListViewModel viewModel;

    LinearLayoutManager llm;
    int positionIndex;

    Configuration current_configuration;
    Class component_class;

    AlertDialog filterDialog;

    private AdView adView;

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


        viewModel = new componentListViewModel(getActivity().getApplication(), getLayoutInflater(), current_configuration, component_class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.configurator_list_component, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();



        // инициализация элементов интерфейса
        ProgressBar progressBar = getActivity().findViewById(R.id.progressBar);
        RecyclerView rv = getActivity().findViewById(R.id.rv);
        ImageButton filterButton = getActivity().findViewById(R.id.filter_button);
        MaterialToolbar toolbar = getActivity().findViewById(R.id.toolbar);


        // тулбар
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(view -> getFragmentManager().popBackStack());

        // список
        llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);


        // подиска на обновление ViewModel
        viewModel.listLiveData.observe(this, components -> {
            if (filterDialog == null)
            {
                if (component_class == Mb.class) filterDialog = viewModel.filterDialog();
            }

            rv.setAdapter(new componentListAdapter(components, getFragmentManager(), current_configuration));
            progressBar.setVisibility(View.INVISIBLE);
        });

        filterButton.setOnClickListener(view ->{
            try {
                filterDialog.show();
            }
            catch (Exception e){}
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