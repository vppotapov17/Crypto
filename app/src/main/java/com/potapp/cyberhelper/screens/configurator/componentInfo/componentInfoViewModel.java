package com.potapp.cyberhelper.screens.configurator.componentInfo;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;

import com.potapp.cyberhelper.models.Configuration;
import com.potapp.cyberhelper.models.components.Component;
import com.potapp.cyberhelper.models.components.*;

public class componentInfoViewModel extends AndroidViewModel {

    FragmentManager fm;
    Component select_component;
    Configuration current_configuration;

    public componentInfoViewModel(@NonNull Application app, FragmentManager fm, Component select_component, Configuration current_configuration)
    {
        super(app);
        this.fm = fm;
        this.select_component = select_component;
        this.current_configuration = current_configuration;
    }

    // добавлен ли уже в сборку просматриваемый компонент
    boolean viewAddedComponent() {
        boolean viewAddedComponent = false;
        if (select_component instanceof Mb)
            viewAddedComponent = select_component == current_configuration.mMb;
        else if (select_component instanceof Cpu)
            viewAddedComponent = select_component == current_configuration.mCpu;
        else if (select_component instanceof Gpu)
            viewAddedComponent = select_component == current_configuration.mGpu;
        else if (select_component instanceof Ozu)
            viewAddedComponent = select_component == current_configuration.mOzu;
        else if (select_component instanceof Bp)
            viewAddedComponent = select_component == current_configuration.mBp;

        return viewAddedComponent;
    }

    void onBackClick()
    {
        fm.popBackStack();
    }

    void onAddButtonClick()
    {
        Toast.makeText(getApplication(), current_configuration.addComponent(select_component, getApplication()), Toast.LENGTH_SHORT).show();
        fm.popBackStack();
        fm.popBackStack();
    }

    void onDeleteButtonClick(){
        Toast.makeText(getApplication(), current_configuration.deleteComponent(select_component, getApplication()), Toast.LENGTH_SHORT).show();
        fm.popBackStack();
    }

    void onBuyButtonClick(){
        Uri uriUrl = Uri.parse(select_component.getRefLink());
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        getApplication().startActivity(launchBrowser);
    }

    String getImageUrl()
    {
        return "https://items.s1.citilink.ru/" + select_component.getProduct_code() + "_v01_b.jpg";
    }
}
