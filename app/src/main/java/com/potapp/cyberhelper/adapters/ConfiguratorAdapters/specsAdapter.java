package com.potapp.cyberhelper.adapters.ConfiguratorAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.R;

import java.util.List;

// адаптер поддерживает два вида разметок
// 1) заголовок подраздела спецификаций
// 2) сама спецификация, состоящая из заголовка (title) и значения (value)

// в адаптер передается 1 аргумент - список (List), содержащий массивы строк - элементы списка
// все массивы строк в списке должны содержать ровно по 2 элемента; если элемент является заголовком подраздела, то второй элемент должен быть равен ""
// массивы строк в List идут в том порядке, в котором они должны быть отображены на экране

public class specsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // список характеристик
    // каждая характеристика - это массив строк
    // элемент с индексом 0 - название
    // элемент с индексом 1 - значение
    // элемент с индексом 2 - подсказка (ее может не быть)

    List<String[]> specifications;

    boolean isGpu;                                                                                  // просмотр характеристик видеокарты

    public specsAdapter(List<String[]> specifications, boolean isGpu){
        this.specifications = specifications;
        this.isGpu = isGpu;
    }

    // определение типа загружаемой разметки
    @Override
    public int getItemViewType(int position)
    {
        if (isGpu && position == getItemCount() - 1) return 2;                                      // кнопка просмотра FPS для видеокарт

        for (String[] str : specifications){
            if (str[1] == null) Log.e("AAA", str[0]);
            if (str[1].equals("") && position == specifications.indexOf(str)) return 0;             // разметка заголовка подраздела
        }



        return 1;                                                                                   // разметка спецификации
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){

        View current_view = null;

        switch (viewType)
        {
            case 0: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discussions_rv_title_specifications, parent, false); break;
            case 1: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.configurator_rv_specification, parent, false); break;
            case 2: current_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.configurator_rv_button_view_fps, parent, false); break;
        }

        return new RecyclerView.ViewHolder(current_view)
        {};
    }


    @Override
    public void onBindViewHolder (@NonNull final RecyclerView.ViewHolder holder, int position){

        if (getItemViewType(position) != 2) {

            String[] current_specification = specifications.get(position);


            switch (getItemViewType(position)) {
                case 0:                                                                                 // разметка заголовка спецификаций

                    TextView section_title = holder.itemView.findViewById(R.id.title);

                    section_title.setText(current_specification[0]);

                    break;

                case 1:                                                                                 // разметка спецификации

                    TextView title = holder.itemView.findViewById(R.id.spec_title);
                    TextView value = holder.itemView.findViewById(R.id.spec_value);

//                    try{
//                        specLayout.setTooltipText(current_value[2]);
//                    }
//                    catch (IndexOutOfBoundsException exception)
//                    {}

                    title.setText(current_specification[0]);
                    value.setText(current_specification[1]);

                    break;
            }
        }
        else {}
    }

    @Override
    public int getItemCount(){
        int count = specifications.size();
        if (isGpu) count++;
        return count;
    }

}
