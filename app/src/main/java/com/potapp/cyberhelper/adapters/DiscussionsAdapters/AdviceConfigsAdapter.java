package com.potapp.cyberhelper.adapters.DiscussionsAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.models.components.Ozu;

import java.util.List;

public class AdviceConfigsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Configuration> configurationList;

    public AdviceConfigsAdapter(List<Configuration> configurationList)
    {
        this.configurationList = configurationList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_card_without_button, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position)
    {
        TextView name = holder.itemView.findViewById(R.id.name);
        name.setText(configurationList.get(position).name);

        TextView spec1_title = holder.itemView.findViewById(R.id.spec1_title);
        TextView spec2_title = holder.itemView.findViewById(R.id.spec2_title);
        TextView spec3_title = holder.itemView.findViewById(R.id.spec3_title);

        spec1_title.setText("Процессор");
        spec2_title.setText("Видеокарта");
        spec3_title.setText("ОЗУ");


        TextView spec1_value = holder.itemView.findViewById(R.id.spec1_value);
        TextView spec2_value = holder.itemView.findViewById(R.id.spec2_value);
        TextView spec3_value = holder.itemView.findViewById(R.id.spec3_value);

        TextView fullPrice = holder.itemView.findViewById(R.id.fullPrice);

        Configuration current_configuration = configurationList.get(position);

        spec1_value.setText(current_configuration.mCpu.getModel());
        spec2_value.setText(current_configuration.mGpu.getModel());

        Ozu OZU = current_configuration.mOzu;
        spec3_value.setText(OZU.getItemQuantity() * OZU.getModulesQuantity() + "x" + OZU.getCapacity() / OZU.getModulesQuantity() + " Гб");

        fullPrice.setText(current_configuration.getFullPrice() + "");
    }

    @Override
    public int getItemCount(){
        return configurationList.size();
    }

}
