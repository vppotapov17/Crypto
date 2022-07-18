package com.potapp.cyberhelper.adapters.ConfiguratorAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.configurator.componentInfo.componentInfoFragment;
import com.potapp.cyberhelper.models.components.*;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class componentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Component> componentList;
    FragmentManager fm;
    Configuration current_configuration;
    Component current_component;

    public componentListAdapter(List<Component> componentList, FragmentManager fm, Configuration current_configuration){
        this.componentList = componentList;
        this.fm = fm;
        this.current_configuration = current_configuration;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int index){

        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_one_button, parent, false )){};

        return holder;
    }

    @Override
    public void onBindViewHolder (@NonNull final RecyclerView.ViewHolder holder, int index){

        CardView cardView = holder.itemView.findViewById(R.id.cardview);

        final ImageView image = holder.itemView.findViewById(R.id.image);                           // изображение процессора
        TextView title = holder.itemView.findViewById(R.id.name);                                   // название процессора

        final MaterialButton selectButton = holder.itemView.findViewById(R.id.button1);             // кнопка выбора

        // спецификация 1
        TextView spec1_title = holder.itemView.findViewById(R.id.spec1_title);
        TextView spec1_value = holder.itemView.findViewById(R.id.spec1_value);

        // спецификация 2
        TextView spec2_title = holder.itemView.findViewById(R.id.spec2_title);
        TextView spec2_value = holder.itemView.findViewById(R.id.spec2_value);

        // спецификация 3
        TextView spec3_title = holder.itemView.findViewById(R.id.spec3_title);
        TextView spec3_value = holder.itemView.findViewById(R.id.spec3_value);

        TextView price = holder.itemView.findViewById(R.id.fullPrice);                              // стоимость процессора

        current_component = componentList.get(index);


        // загрузка изображения в ImageView с сайта Citilink.ru

        Glide
            .with(image.getContext())
            .load("https://items.s1.citilink.ru/" + current_component.getProduct_code() + "_v01_b.jpg")
            .into(image);



        // изменение внешнего вида кнопки выбора и обработка нажатия
        selectButton.setRippleColorResource(R.color.MainColorOrange);
        selectButton.setTextColor(holder.itemView.getResources().getColor(R.color.MainColorOrange));
        selectButton.setStrokeColorResource(R.color.MainColorOrange);
        selectButton.setIcon(holder.itemView.getResources().getDrawable(R.drawable.ic_add1));
        selectButton.setIconTintResource(R.color.MainColorOrange);
        selectButton.setText("ДОБАВИТЬ");
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(image.getContext(), current_configuration.addComponent(componentList.get(index), image.getContext()), Toast.LENGTH_SHORT).show();
                fm.popBackStack();
            }
        });

        // заполнение текстовых полей
        title.setText(current_component.getName());                                                 // название компонента (производитель + модель)


        spec1_title.setText(current_component.getMainSpec1().getSpecTitle());
        spec1_value.setText(current_component.getMainSpec1().getSpecValue());

        spec2_title.setText(current_component.getMainSpec2().getSpecTitle());
        spec2_value.setText(current_component.getMainSpec2().getSpecValue());

        spec3_title.setText(current_component.getMainSpec3().getSpecTitle());
        spec3_value.setText(current_component.getMainSpec3().getSpecValue());

        price.setText(current_component.getPrice() + " ₽");

        cardView.setOnClickListener(view -> {

            loadFragment(componentInfoFragment.newInstance(componentList.get(index), current_configuration));
        });
    }

    @Override
    public int getItemCount(){
        return componentList.size();
    }

    void loadFragment(Fragment fragment){
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

}
