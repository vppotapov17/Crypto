package com.potapp.cyberhelper.adapters.ConfiguratorAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.potapp.cyberhelper.database.dbs.DB_CONFIGURATIONS;
import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.screens.configurator.componentInfo.componentInfoFragment;
import com.potapp.cyberhelper.models.components.Component;
import com.potapp.cyberhelper.models.components.*;
import com.potapp.cyberhelper.screens.configurator.componentList.*;
import com.google.android.material.button.MaterialButton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;


// адаптер для отображения комплектующих конфигурации в режиме создания
public class creatingConfigurationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    Configuration current_configuration;                                                            // текущая конфигурация
    FragmentManager fm;                                                                             // FragmentManager для запуска соответствующего фрагмента
    NavController navController;

    TextView fullPrice;

    // позиции заголовков комплектующих в списке
    final int pt_mb = 0, pt_cpu = 2, pt_gpu = 4, pt_ozu = 6, pt_bp = 8,
            pt_cooler = 10, pt_pccase = 12, pt_hdd = 14, pt_ssd_25 = 16, pt_ssd_m2 = 18;

    // позиции CardView в списке
    final int p_mb = pt_mb + 1, p_cpu = pt_cpu + 1, p_gpu = pt_gpu + 1, p_ozu = pt_ozu + 1,
            p_bp = pt_bp + 1, p_cooler = pt_cooler + 1, p_pccase = pt_pccase + 1,
            p_hdd = pt_hdd + 1, p_ssd_25 = pt_ssd_25 + 1, p_ssd_m2 = pt_ssd_m2 + 1;

    // конструктор класса (производит инициализацию полей)
    public creatingConfigurationAdapter(Configuration current_configuration, NavController navController, Context context, TextView fullPrice)
    {
        this.navController = navController;
        this.context = context;
        this.current_configuration = current_configuration;
        this.fm = fm;
        this.fullPrice = fullPrice;

    }

    // определение типа загружаемой разметки
    @Override
    public int getItemViewType(int position)
    {
        // 0 - заголовок компонента
        // 1 - добавление комплектующего
        // 2 - карточка без указания количества товаров
        // 3 - карточка с указанием количества товаров

        if (position % 2 == 0 || position == 0) return 0;
        else
        {
            switch (position)
            {
                case p_mb:
                    if (current_configuration.mMb == null) return 1;
                    return 2;
                case p_cpu:
                    if (current_configuration.mCpu == null) return 1;
                    return 2;
                case p_gpu:
                    if (current_configuration.mGpu == null) return 1;
                    return 2;
                case p_ozu:
                    if (current_configuration.mOzu == null) return 1;
                    return 3;
                case p_bp:
                    if (current_configuration.mBp == null) return 1;
                    return 2;
                case p_cooler:
                    if (current_configuration.mCooler == null) return 1;
                    return 2;
                case p_pccase:
                    if (current_configuration.mCase == null) return 1;
                    return 2;
                case p_hdd:
                    if (current_configuration.mHdd == null) return 1;
                    return 3;
                case p_ssd_25:
                    if (current_configuration.mSsd_25 == null) return 1;
                    return 3;
                default:
                    if (current_configuration.mSsd_m2 == null) return 1;
                    return 3;
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
            // заголовок комплектующего
            case 0: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.configurator_rv_title_components, parent, false); break;
            // добавление нового комплектующего
            case 1: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.configurator_rv_add_component, parent, false); break;
            // CardView без указания количества товаров
            case 2: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_two_button, parent, false); break;
            // CardView с указанием количества товаров
            case 3: itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_two_button_quantity, parent, false); break;
        }

        return new RecyclerView.ViewHolder(itemView){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        // установка значений элементов интерфейса в зависимости от типа разметки

        int viewType = getItemViewType(position);

        // заголовок комплектующего

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

                case pt_hdd:                                                                        // жесткий диск
                    current_title = res.getString(R.string.HDD);
                    break;

                case pt_ssd_25:                                                                     // SSD 2.5
                    current_title = res.getString(R.string.SSD) + " 2.5″";
                    break;

                case pt_ssd_m2:                                                                     // SSD M2
                    current_title = res.getString(R.string.SSD) + " M2";
                    break;
            }

            title.setText(current_title);
        }
        // добавление нового комплектующего
        else if (viewType == 1)
        {
            final Class componentType;                                                              // тип добавляемого компонента
            final String toastText;                                                                 // сообщение, если отсутствует мат.плата или процессор

            final CardView cardView = holder.itemView.findViewById(R.id.cardview);

            // определение загружаемого фрагмента
            switch (position) {
                case p_mb:                                                                          // материнская плата

                    componentType = Mb.class;
                    toastText = null;

                    break;

                case p_cpu:                                                                         // процессор

                    componentType = Cpu.class;
                    toastText = null;

                    break;

                case p_gpu:                                                                         // видеокарта

                    componentType = Gpu.class;
                    toastText = null;

                    break;

                case p_ozu:                                                                         // оперативная память

                    if (current_configuration.mMb == null) {
                        componentType = null;
                        toastText = "Сначала выберите материнскую плату!";
                    } else {
                        componentType = Ozu.class;
                        toastText = null;
                    }

                    break;

                case p_bp:                                                                          // блок питания

                    componentType = Bp.class;
                    toastText = null;

                    break;

                case p_cooler:                                                                      // охлаждение

                    if (current_configuration.mMb == null) {
                        componentType = null;
                        toastText = "Сначала выберите материнскую плату!";
                    } else {
                        componentType = Cooler.class;
                        toastText = null;
                    }

                    break;

                case p_pccase:                                                                      // корпус

                    componentType = Case.class;
                    toastText = null;

                    break;

                case p_hdd:                                                                         // жесткий диск

                    if (current_configuration.mMb == null)
                    {
                        componentType = null;
                        toastText = "Сначала выберите материнскую плату!" ;
                    }
                    else
                    {
                        componentType = Hdd.class;
                        toastText = null;
                    }

                    break;

                case p_ssd_25:                                                                      // SSD 2.5

                    if (current_configuration.mMb == null)
                    {
                        componentType = null;
                        toastText = "Сначала выберите материнскую плату!" ;
                    }
                    else
                    {
                        componentType = Ssd.class;
                        toastText = null;
                    }

                    break;

                case p_ssd_m2:                                                                      // SSD M2

                    if (current_configuration.mMb == null)
                    {
                        componentType = null;
                        toastText = "Сначала выберите материнскую плату!" ;
                    }
                    else
                    {
                        // передаём класс Component (значит что добавляем SSD M2)
                        componentType = Component.class;
                        toastText = null;
                    }

                    break;

                default:

                    componentType = null;
                    toastText = null;
            }

            // обработка нажатия на CardView
            cardView.setClickable(true);
            cardView.setOnClickListener(view -> {
                if (componentType == null)
                    Toast.makeText(cardView.getContext(), toastText, Toast.LENGTH_SHORT).show();
                else {
                    Bundle args = new Bundle();
                    args.putSerializable("current_configuration", current_configuration);
                    args.putSerializable("component_class", componentType);
                    navController.navigate(R.id.action_creatingConfigurationFragment_to_componentListFragment, args);
                }
            });
        }
        // CardView с указанием и без указания количества
        else if (viewType == 2 || viewType == 3)
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
            final MaterialButton button2 = holder.itemView.findViewById(R.id.button2);

            // установка значений

            // изображение компонента
            Glide
                    .with(holder.itemView.getContext())
                    .load(imageLink(current_component.getProduct_code()))
                    .into(image);

            name.setText(current_component.getName());                                              // название компонента
            price.setText(current_component.getItemQuantity() * current_component.getPrice() + " ₽");                                     // стоимость компонента

            // основные характеристики
            spec1_title.setText(current_component.getMainSpec1().getSpecTitle());
            spec1_value.setText(current_component.getMainSpec1().getSpecValue());
            spec2_title.setText(current_component.getMainSpec2().getSpecTitle());
            spec2_value.setText(current_component.getMainSpec2().getSpecValue());
            spec3_title.setText(current_component.getMainSpec3().getSpecTitle());
            spec3_value.setText(current_component.getMainSpec3().getSpecValue());

            // кнопка "изменить"
            button1.setText("ИЗМЕНИТЬ");
            button1.setIconResource(R.drawable.ic_change);

            button1.setOnClickListener(view -> {
                Bundle args = new Bundle();
                args.putSerializable("current_configuration", current_configuration);
                args.putSerializable("component_class", current_component.getClass());
                navController.navigate(R.id.action_creatingConfigurationFragment_to_componentListFragment, args);

            });

            // кнопка "удалить"
            button2.setText("УДАЛИТЬ");
            button2.setIconResource(R.drawable.ic_outline_delete_sweep);

            button2.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), current_configuration.deleteComponent(current_component, button2.getContext()), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                updateFullPrice();
            });

            cardView.setClickable(true);
            cardView.setOnClickListener(view -> {
                Bundle args = new Bundle();
                args.putSerializable("select_component", current_component);
                args.putSerializable("current_configuration", current_configuration);
                navController.navigate(R.id.action_creatingConfigurationFragment_to_componentInfoFragment, args);
            });
        }

        // дополнительные элементы в CardView с указанием количества
        if (viewType == 3)
        {
            Component current_component = getCurrentComponent(position);



            // элементы интерфейса
            ImageButton QMinus = holder.itemView.findViewById(R.id.QMinus);
            ImageButton QPlus = holder.itemView.findViewById(R.id.QPlus);
            TextView QValue = holder.itemView.findViewById(R.id.QValue);

            TextView price = holder.itemView.findViewById(R.id.fullPrice);



            // подписка на изменение количества
            PublishSubject<Integer> QSubject = PublishSubject.create();


            QSubject.subscribe(integer -> {
                int QMax = getMaxItemsQuantity(current_component);

                Log.d("MaxItems", "CurrentValue: " + integer);

                if (integer > 0 && integer <= QMax) {
                    if (current_component == current_configuration.mOzu || current_component == current_configuration.mSsd_m2){
                        if (integer + 1 > QMax) QPlus.setColorFilter(R.color.colorAccentOptional);
                        else QPlus.setColorFilter(null);
                        if (integer - 1 < 1) QMinus.setColorFilter(R.color.colorAccentOptional);
                        else QMinus.setColorFilter(null);
                    }


                    QValue.setText(integer + "");

                    current_component.setItemQuantity(integer);
                    price.setText(integer * current_component.getPrice() + " ₽");
                    updateFullPrice();

                    // обновляем конфигурацию в базе данных
                    DB_CONFIGURATIONS db_configurations = Room.databaseBuilder(context, DB_CONFIGURATIONS.class, "CONFIGURATIONS").build();

                    Runnable runnable = () -> {
                        db_configurations.getMyDao().updateConfiguration(current_configuration);
                    };

                    Observable.fromRunnable(runnable)
                            .subscribeOn(Schedulers.io())
                            .subscribe();

                }
            });

            QSubject.onNext(current_component.getItemQuantity());

            QValue.setText(current_component.getItemQuantity() + "");

            QMinus.setOnClickListener(view -> {
                int Q = Integer.parseInt(QValue.getText().toString());
                QSubject.onNext(Q - 1);
            });
            QPlus.setOnClickListener(view -> {
                int Q = Integer.parseInt(QValue.getText().toString());
                QSubject.onNext(Q + 1);
            });
        }
    }

    // максимально возможное количество комплектов
    private int getMaxItemsQuantity(Component current_component){

        // ОЗУ
        if (current_component instanceof Ozu)
        {
            int maxItems1 = current_configuration.mMb.getOzuSlotsQuantity() / ((Ozu) current_component).getModulesQuantity();
            int maxItems2 = current_configuration.mMb.getMaxOzuSize() / ((Ozu) current_component).getSingleCapacity();

            if (maxItems1 < maxItems2) return maxItems1;
            Log.d("AAA", "OZU");
            return maxItems2;

        }
        // hdd
        else if (current_component instanceof Hdd){

            int q = 0;

            if (current_configuration.mSsd_25 != null) q += current_configuration.mSsd_25.getItemQuantity();
            return current_configuration.mMb.getSata3() - q;
        }
        // ssd
        else
        {
            // ssd 2.5
            if (((Ssd) current_component).getFormFactor().equals("2.5")){
                int q = 0;

                if (current_configuration.mHdd != null) q += current_configuration.mHdd.getItemQuantity();
                return current_configuration.mMb.getSata3() - q;
            }
            // ssd M2
            else {
                return current_configuration.mMb.getM2();
            }
        }
    }

    @Override
    public int getItemCount()
    {
        int count = 20;
        if (current_configuration.mMb != null)
            if (current_configuration.mMb.getM2() == 0) count -= 2;

        return count;
    }


    // ---------------------------------------------------------------------------------------------
    // дополнительные методы
    // ---------------------------------------------------------------------------------------------

    // обновление общей стоимости сборки
    private void updateFullPrice(){
        fullPrice.setText(current_configuration.getFullPrice() + " ₽");
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
            case p_hdd: current_component = current_configuration.mHdd; break;
            case p_ssd_25: current_component = current_configuration.mSsd_25; break;
            case p_ssd_m2: current_component = current_configuration.mSsd_m2; break;
        }

        return current_component;
    }

    // генерирование ссылки на изображение комплектующего

    String imageLink(int product_code)
    {
        return "https://items.s1.citilink.ru/" + product_code + "_v01_b.jpg";
    }

}
