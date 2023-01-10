package com.potapp.cyberhelper.adapters.ConfiguratorAdapters;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.configurator.componentInfo.componentInfoFragment;
import com.potapp.cyberhelper.models.components.*;
import com.google.android.material.button.MaterialButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class componentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Component> componentList;
    private List<NativeAd> adList;

    private int step;

    NavController navController;
    Configuration current_configuration;
    Component current_component;

    public componentListAdapter(List<Component> componentList, List<NativeAd> adList, NavController navController, Configuration current_configuration){
        this.navController = navController;
        this.current_configuration = current_configuration;

        this.componentList = new ArrayList<>();
        this.componentList.addAll(componentList);
        this.adList = adList;

        if (adList.size() != 0){
            step = this.componentList.size() / this.adList.size();
            if (step < 30) step = 20;

            for (int i = step - 1; i < this.componentList.size(); i += step){
                this.componentList.add(i, null);
            }
        }
    }

    @Override
    public int getItemViewType(int position){

        if (adList.size() != 0){
            for (int i = step - 1; i < componentList.size(); i += step)
            {
                if (position == i) return 1;
            }
            return 0;
        }

        return 0;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        RecyclerView.ViewHolder holder;
        if (viewType == 0){
            holder = new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_one_button, parent, false )){};
        }
        else holder = new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad_item, parent, false )){};


        return holder;
    }

    @Override
    public void onBindViewHolder (@NonNull final RecyclerView.ViewHolder holder, int position){


        if (getItemViewType(position) == 0 ){

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

            TextView price = holder.itemView.findViewById(R.id.fullPrice);                          // стоимость процессора


            current_component = componentList.get(position);


            // загрузка изображения в ImageView с сайта Citilink.ru

            try {
                Glide
                        .with(image.getContext())
                        .load(current_component.getPictureLink())
                        .error("https://items.s1.citilink.ru/" + current_component.getProduct_code() + "_v01_b.jpg")
                        .into(image);

            }
            catch (Exception e){
                Log.e("AAA", position + " - position");
            }



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

                    Toast.makeText(image.getContext(), current_configuration.addComponent(componentList.get(position), image.getContext()), Toast.LENGTH_SHORT).show();
                    navController.popBackStack();
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

                Bundle args = new Bundle();
                args.putSerializable("select_component", componentList.get(position));
                Log.d("AAA", componentList.get(position).getName());
                args.putSerializable("current_configuration", current_configuration);
                navController.navigate(R.id.action_componentListFragment_to_componentInfoFragment, args);
            });
        }
        else if (getItemViewType(position) == 1){

            Log.d("AAA", adList.size() + " - size");
            Log.d("AAA", position + " - position");
            Log.d("AAA", componentList.size() + " - totalSize");
            NativeAd current_ad = adList.get(position / step);

            NativeBannerView nativeBannerView = holder.itemView.findViewById(R.id.nativeBannerView);

            // внешний вид кнопки
            ButtonAppearance buttonAppearance = new ButtonAppearance.Builder()
                    .setTextAppearance(new TextAppearance.Builder().setTextColor(Color.WHITE).build())
                    .setBorderWidth(0f)
                    .setNormalColor(Color.parseColor("#FD5900"))
                    .setPressedColor(Color.parseColor("#EDFD762D")).build();

            // внешний вид рамки
            BannerAppearance bannerAppearance = new BannerAppearance.Builder()
                    .setImageMargins(new HorizontalOffset(0f, 32f))
                    .setBorderWidth(0f).build();


            final NativeTemplateAppearance nativeTemplateAppearance =
                    new NativeTemplateAppearance.Builder()
                            .withCallToActionAppearance(buttonAppearance).withBannerAppearance(bannerAppearance).build();


            nativeBannerView.applyAppearance(nativeTemplateAppearance);
            nativeBannerView.setAd(current_ad);

        }
    }

    @Override
    public int getItemCount(){
        return componentList.size();
    }
}
