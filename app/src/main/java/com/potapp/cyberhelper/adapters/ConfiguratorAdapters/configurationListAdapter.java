package com.potapp.cyberhelper.adapters.ConfiguratorAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.potapp.cyberhelper.screens.configurator.creatingConfiguration.creatingConfigurationFragment;
import com.potapp.cyberhelper.R;

import java.util.ArrayList;
import java.util.List;

import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.screens.configurator.viewReadyConfiguration.viewReadyConfigurationFragment;
import com.google.android.material.button.MaterialButton;

import com.potapp.cyberhelper.database.daos.DAO_CONFIGURATIONS;
import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;

public class configurationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    DAO_CONFIGURATIONS dao_configurations;                                                          // база созданных сборок

    private List<Configuration> configurationList;
    private FragmentManager fm;
    private RecyclerView rv;
    private TextView no_configuration;

    // список готовых конфигураций
    List<Configuration> getReadyConfigurations()
    {
        List<Configuration> readyConfigurations = new ArrayList<>();

        for (Configuration configuration : configurationList)
            if (configuration.isReady) readyConfigurations.add(configuration);

        return readyConfigurations;
    }

    // список незавершённых конфигураций
    List<Configuration> getNotReadyConfigurations()
    {
        List<Configuration> notReadyConfigurations = new ArrayList<>();

        for (Configuration configuration : configurationList)
            if (!configuration.isReady) notReadyConfigurations.add(configuration);

        return notReadyConfigurations;
    }

    public configurationListAdapter(List<Configuration> configurationList, FragmentManager fm, RecyclerView rv, TextView no_configuration)
    {
        this.fm = fm;
        this.configurationList = configurationList;
        this.rv = rv;
        this.no_configuration = no_configuration;

        this.dao_configurations = Room.databaseBuilder(rv.getContext(), DB_CONFIGURATIONS.class, "CONFIGURATIONS").build().getMyDao();


    }

    @Override
    public int getItemViewType(int position)
    {

        if (getReadyConfigurations().size() != 0 && getNotReadyConfigurations().size() != 0)
        {
            if (position == 0 || position == getNotReadyConfigurations().size() + 1) return 0;

            return 1;
        }
        else
        {
            if (position == 0) return 0;
            return 1;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View currentView = null;                                                                    // текущая разметка

        switch (viewType)
        {
            case 0: currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.configurator_rv_title_configuration_list, parent, false); break;
            case 1: currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_one_button, parent, false); break;
        }

        return new RecyclerView.ViewHolder(currentView) {};
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position)
    {
        int viewType = getItemViewType(position);

        // заголовок
        if (viewType == 0)
        {
            TextView title = holder.itemView.findViewById(R.id.title);
            if (getReadyConfigurations().size() != 0 && getNotReadyConfigurations().size() == 0)
            {
                if (position == 0) title.setText("Завершённые");
            }
            else {
                if (position == 0) title.setText("Продолжить сборку");
                else title.setText("Завершённые");
            }
        }
        // основная разметка
        else
        {
            // определение текущей конфигурации
            final Configuration current_configuration;

            // существуют и завершённые, и незавершённые сборки
            if (getNotReadyConfigurations().size() != 0 && getReadyConfigurations().size() != 0) {
                // незавершённые сборки
                if (position < getNotReadyConfigurations().size() + 1)
                    current_configuration = getNotReadyConfigurations().get(position - 1);
                // завершённые сборки
                else
                    current_configuration = getReadyConfigurations().get(position - getNotReadyConfigurations().size() - 2);
            }
            // существуют только завершённые сборки
            else if (getNotReadyConfigurations().size() == 0) current_configuration = getReadyConfigurations().get(position - 1);
            // существуют только незавершённые сборки
            else current_configuration = getNotReadyConfigurations().get(position - 1);


            // определение элементов интерфейса
            CardView cardView = holder.itemView.findViewById(R.id.cardview);

            TextView spec_cpu = holder.itemView.findViewById(R.id.spec1_value);
            TextView spec_gpu = holder.itemView.findViewById(R.id.spec2_value);
            TextView spec_ozu = holder.itemView.findViewById(R.id.spec3_value);

            TextView name = holder.itemView.findViewById(R.id.name);
            TextView price = holder.itemView.findViewById(R.id.fullPrice);

            MaterialButton delete = holder.itemView.findViewById(R.id.button1);

            ImageView image = holder.itemView.findViewById(R.id.image);

            // обработка нажатия на карточку
            cardView.setClickable(true);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (current_configuration.isReady)
                        loadFragment(viewReadyConfigurationFragment.newInstance(current_configuration));
                    else loadFragment(creatingConfigurationFragment.newInstance(current_configuration));
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            dao_configurations.removeConfiguration(current_configuration);
                        }
                    });
                    t.start();

                    configurationList.remove(current_configuration);

                    if (configurationList.size() == 0)
                    {
                        rv.setVisibility(View.INVISIBLE);
                        no_configuration.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        rv.setVisibility(View.VISIBLE);
                        no_configuration.setVisibility(View.INVISIBLE);
                    }

                    notifyDataSetChanged();
                }
            });



            if (current_configuration.mCpu != null) spec_cpu.setText(current_configuration.mCpu.getModel());
            else spec_cpu.setText("н/д");


            if (current_configuration.mGpu != null)
            {
                String gpuModel = current_configuration.mGpu.getModel();
                gpuModel = gpuModel.substring(gpuModel.indexOf(" ") + 1);
                spec_gpu.setText(gpuModel);
            }
            else spec_gpu.setText("н/д");

            if (current_configuration.mOzu != null) spec_ozu.setText(current_configuration.mOzu.getModulesQuantity() + "x" +
                    current_configuration.mOzu.getCapacity() / current_configuration.mOzu.getModulesQuantity() + "Гб");
            else spec_ozu.setText("н/д");


            name.setText(current_configuration.name);
            price.setText(current_configuration.getFullPrice() + " ₽");

            if (current_configuration.mCase != null) {
                Glide
                        .with(image.getContext())
                        .load("https://items.s1.citilink.ru/" + current_configuration.mCase.getProduct_code() + "_v01_b.jpg")
                        .into(image);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        int count = 0;
        if (getNotReadyConfigurations().size() != 0)
        {
            count ++;
            count += getNotReadyConfigurations().size();
        }
        if (getReadyConfigurations().size() != 0)
        {
            count ++;
            count += getReadyConfigurations().size();
        }

        return count;
    }

    // загрузка фрагмента
    void loadFragment(Fragment fragment)
    {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
