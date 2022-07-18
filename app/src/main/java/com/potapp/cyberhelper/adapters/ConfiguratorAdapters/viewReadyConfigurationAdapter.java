package com.potapp.cyberhelper.adapters.ConfiguratorAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.models.components.Ssd;
import com.potapp.cyberhelper.screens.configurator.componentInfo.componentInfoFragment;
import com.potapp.cyberhelper.models.components.Component;
import com.potapp.cyberhelper.models.components.Hdd;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

// адаптер для отображения комплектующих готовой конфигурации
public class viewReadyConfigurationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Configuration current_configuration;                                                            // текущая конфигурация
    FragmentManager fm;                                                                             // FragmentManager для запуска фрагментов
    TextView fullPrice;                                                                             // текстовое поле для отображения общей стоимости сборки

    Context context;

    // позиции заголовков комплектующих в списке
    final int pt_mb = 0, pt_cpu = 2, pt_gpu = 4, pt_ozu = 6, pt_bp = 8,
            pt_cooler = 10, pt_pccase = 12, pt_storage1 = 14, pt_storage2 = 16, pt_storage3 = 18;

    // позиции CardView в списке
    final int p_mb = pt_mb + 1, p_cpu = pt_cpu + 1, p_gpu = pt_gpu + 1, p_ozu = pt_ozu + 1,
            p_bp = pt_bp + 1, p_cooler = pt_cooler + 1, p_pccase = pt_pccase + 1,
            p_storage1 = pt_storage1 + 1, p_storage2 = pt_storage2 + 1, p_storage3 = pt_storage3 + 1;

    // конструктор класса (производит инициализацию полей)
    public viewReadyConfigurationAdapter(Configuration current_configuration, FragmentManager fm, TextView fullPrice, Context context)
    {
        this.current_configuration = current_configuration;
        this.fm = fm;
        this.fullPrice = fullPrice;
        this.context = context;
    }

    // определение типа загружаемой разметки
    @Override
    public int getItemViewType(int position)
    {
        if (position % 2 == 0 || position == 0)
        {
            return 0;                                                                               // заголовок компонента
        }
        else
        {
            switch (position)
            {
                case p_ozu:
                case p_storage1:
                case p_storage2:
                case p_storage3:
                    return 2;                                                                       // CardView с указанием количества
                default:
                    return 1;                                                                       // CardView без указания количества
            }
        }
    }

    // загрузка соответствующей разметки
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = null;

        switch (viewType)
        {
            case 0: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.configurator_rv_title_components, parent, false); break;
            case 1: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_two_button, parent, false); break;
            case 2: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_two_button_quantity, parent, false); break;
        }

        return new RecyclerView.ViewHolder(itemView){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        // установка значений в зависимости от типа разметки
        int viewType = getItemViewType(position);

        // заголовок компонента
        if (viewType == 0)
        {
            TextView title = holder.itemView.findViewById(R.id.recycler_title);

            Resources res = holder.itemView.getResources();

            String current_title = null;                                                            // текст заголовка
            int current_drawable;

            // определение текста и изображения заголовка в зависимости от позиции
            switch (position) {

                case pt_mb:                                                                         // материнская плата
                    current_title = res.getString(R.string.MB);
                    break;

                case pt_cpu:                                                                        // процессор
                    current_title = res.getString(R.string.CPU);
                    break;

                case pt_gpu:                                                                        // видеокарта
                    current_title = res.getString(R.string.GPU);
                    break;

                case pt_ozu:                                                                        // оперативная память
                    current_title = res.getString(R.string.OZU);
                    break;

                case pt_bp:                                                                         // блок питания
                    current_title = res.getString(R.string.BP);
                    break;

                case pt_cooler:                                                                     // охлаждение
                    current_title = res.getString(R.string.COOLER);
                    break;

                case pt_pccase:                                                                     // корпус
                    current_title = res.getString(R.string.PCCASE);
                    break;

                case pt_storage1:                                                                   // накопитель 1

                    // определение вида накопителя на 1-ой позиции
                    Component current_component = getStorageList().get(0);

                    if (current_component instanceof Hdd)
                        current_title = res.getString(R.string.HDD);
                    else if (current_component instanceof Ssd)
                    {
                        current_title = res.getString(R.string.SSD);
                        if (((Ssd) current_component).getFormFactor().equals("2.5"))
                            current_title += " 2.5″";
                        else current_title += " M2";
                    }

                    break;

                case pt_storage2:                                                                   // накопитель 2

                    // определение вида компонента на 2-ой позиции
                    current_component = getStorageList().get(1);

                    if (current_component instanceof Ssd)
                    {
                        current_title = res.getString(R.string.SSD);
                        if (((Ssd) current_component).getFormFactor().equals("2.5")) current_title += " 2.5″";
                        else current_title += " M2";
                    }

                    break;

                case pt_storage3:                                                                   // накопитель 3

                    current_title = res.getString(R.string.SSD) + " M2";

                    break;
            }

            title.setText(current_title);
        }
        // CardView с указанием и без указания количества
        if (viewType == 1 || viewType == 2)
        {
            final Component current_component = getCurrentComponent(position);                      // текущий компонент, из которого будет производиться заполнение полей

            // инициализация элементов интерфейса
            CardView cardView = holder.itemView.findViewById(R.id.cardview);

            ImageView image = holder.itemView.findViewById(R.id.image);

            TextView name = holder.itemView.findViewById(R.id.name);
            TextView price = holder.itemView.findViewById(R.id.fullPrice);
            

            TextView spec1_title = holder.itemView.findViewById(R.id.spec1_title);
            TextView spec2_title = holder.itemView.findViewById(R.id.spec2_title);
            TextView spec3_title = holder.itemView.findViewById(R.id.spec3_title);

            TextView spec1_value = holder.itemView.findViewById(R.id.spec1_value);
            TextView spec2_value = holder.itemView.findViewById(R.id.spec2_value);
            TextView spec3_value = holder.itemView.findViewById(R.id.spec3_value);

            MaterialButton button1 = holder.itemView.findViewById(R.id.button1);
            MaterialButton button2 = holder.itemView.findViewById(R.id.button2);

            // установка значений

            // изображение компонента
            Glide
                    .with(holder.itemView.getContext())
                    .load(imageLink(current_component.getProduct_code()))
                    .into(image);

            name.setText(current_component.getName());                                              // название компонента
            price.setText(current_component.getPrice() + " ₽");                                       // стоимость компонента

            // основные характеристики
            spec1_title.setText(current_component.getMainSpec1().getSpecTitle());
            spec1_value.setText(current_component.getMainSpec1().getSpecValue());
            spec2_title.setText(current_component.getMainSpec2().getSpecTitle());
            spec2_value.setText(current_component.getMainSpec2().getSpecValue());
            spec3_title.setText(current_component.getMainSpec3().getSpecTitle());
            spec3_value.setText(current_component.getMainSpec3().getSpecValue());

            // кнопка "купить"
            button1.setText("КУПИТЬ");
            button1.setIconResource(R.drawable.ic_buy);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            // кнопка "поделиться"
            button2.setText("ПОДЕЛИТЬСЯ");
            button2.setIconResource(R.drawable.ic_outline_share);
            button2.setIconTintResource(R.color.blue);
            button2.setTextColor(cardView.getResources().getColor(R.color.blue));

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            cardView.setClickable(true);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(componentInfoFragment.newInstance(current_component, current_configuration));
                }
            });
        }
        // дополнительные элементы в карточке с указанием количества
        if (viewType == 2)
        {
            ImageButton QMinus = holder.itemView.findViewById(R.id.QMinus);
            ImageButton QPlus = holder.itemView.findViewById(R.id.QPlus);
            TextView QPrice = holder.itemView.findViewById(R.id.price);

            QMinus.setClickable(false);
            QPlus.setClickable(false);

            final TextView QValue = holder.itemView.findViewById(R.id.QValue);


        }
    }

    @Override
    public int getItemCount()
    {
        return 14 + getStorageList().size() * 2;
    }

    // ---------------------------------------------------------------------------------------------
    // дополнительные методы
    // ---------------------------------------------------------------------------------------------

    List<Component> getStorageList()
    {
        List <Component> list = new ArrayList<>();
        if (current_configuration.mHdd != null) list.add(current_configuration.mHdd);
        if (current_configuration.mSsd_25 != null) list.add(current_configuration.mSsd_25);
        if (current_configuration.mSsd_m2 != null) list.add(current_configuration.mSsd_m2);

        return list;
    }

    // определение текущего компонента в зависимости от позиции

    private Component getCurrentComponent(int positioin)
    {
        Component current_component = null;

        switch (positioin)
        {
            case p_mb: current_component = current_configuration.mMb; break;
            case p_cpu: current_component = current_configuration.mCpu; break;
            case p_gpu: current_component = current_configuration.mGpu; break;
            case p_ozu: current_component = current_configuration.mOzu; break;
            case p_bp: current_component = current_configuration.mBp; break;
            case p_cooler: current_component = current_configuration.mCooler; break;
            case p_pccase: current_component = current_configuration.mCase; break;
            case p_storage1: current_component = getStorageList().get(0); break;
            case p_storage2: current_component = getStorageList().get(1); break;
            case p_storage3: current_component = getStorageList().get(2); break;
        }

        return current_component;
    }

    // генерирование ссылки на изображение комплектующего

    String imageLink(int product_code)
    {
        return "https://items.s1.citilink.ru/" + product_code + "_v01_b.jpg";
    }

    // загрузка фрагмента
    private void loadFragment(Fragment fragment)
    {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // переход по ссылке
    public void goToUrl(String url){
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }

}
