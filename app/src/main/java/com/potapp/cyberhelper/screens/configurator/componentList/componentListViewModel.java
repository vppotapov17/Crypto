package com.potapp.cyberhelper.screens.configurator.componentList;

import android.app.AlertDialog;
import android.app.Application;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.potapp.cyberhelper.R;
import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.data.models.componentFilters.MbFilter;
import com.potapp.cyberhelper.data.models.components.*;
import com.potapp.cyberhelper.data.room.dbs.*;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class componentListViewModel extends AndroidViewModel {

    LayoutInflater inflater;

    MbFilter currentFilter_mb;

    Configuration current_configuration;                                                            // текущая конфигурация
    Class component_class;                                                                          // класс текущего компонента
    MutableLiveData<List<Component>> listLiveData;
    Thread t;

    List<Component> baseComponentList;                                                              // базовый список компонентов (без фильтров)
    List<Component> filterComponentList;                                                            // список компонентов с учётом фильтров


    public componentListViewModel(@NonNull Application app, LayoutInflater inflater, Configuration current_configuration, Class component_class) {
        super(app);

        this.inflater = inflater;

        this.currentFilter_mb = new MbFilter();

        this.current_configuration = current_configuration;
        this.component_class = component_class;


        baseComponentList = new ArrayList<>();
        filterComponentList = new ArrayList<>();


        listLiveData = new MutableLiveData<>();



        t = null;

        // заполнение базового списка компонентами текущего типа
        if (component_class == Mb.class)
        {
            t = new Thread(() -> {
                List<Mb> mbs = Room.databaseBuilder(app.getApplicationContext(), DB_MOTHERBOARDS.class, "MOTHERBOARDS").build().getMyDao().getMB();
                for (Mb MB : mbs)
                {
                    if (isMb_correct(MB)) baseComponentList.add(MB);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);

            });
        }
        else if (component_class == Cpu.class){
            t = new Thread(() -> {
                List<Cpu> cpus = Room.databaseBuilder(app.getApplicationContext(), DB_PROCESSORS.class, "PROCESSORS").build().getMyDao().getCPU();
                for (Cpu CPU : cpus)
                {
                    if (isCpu_correct(CPU)) baseComponentList.add(CPU);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }
        else if (component_class == Gpu.class)
        {
            t = new Thread(()->{
               List<Gpu> gpus = Room.databaseBuilder(app.getApplicationContext(), DB_VIDEOCARDS.class, "VIDEOCARDS").build().getMyDao().getGPU();
               for (Gpu GPU : gpus)
               {
                   if (isGpu_correct(GPU)) baseComponentList.add(GPU);
               }

               filterComponentList.addAll(baseComponentList);
               listLiveData.postValue(filterComponentList);
            });
        }
        else if (component_class == Ozu.class){
            t = new Thread(() -> {
                List<Ozu> ozus = Room.databaseBuilder(app.getApplicationContext(), DB_OZUS.class, "OZUS").build().getMyDao().getOZU();
                for (Ozu OZU : ozus)
                {
                    if (isOzu_correct(OZU)) baseComponentList.add(OZU);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }
        else if (component_class == Bp.class){
            t = new Thread(() -> {

                List<Bp> bps = Room.databaseBuilder(app.getApplicationContext(), DB_BPS.class, "BPS").build().getMyDao().getBP();

                for (Bp BP : bps) {
                    if (isBp_correct(BP)) baseComponentList.add(BP);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }
        else if (component_class == Case.class)
        {
            t = new Thread(() -> {
                List<Case> pccases = Room.databaseBuilder(app.getApplicationContext(), DB_CASES.class, "PCCASES").build().getMyDao().getPCCASE();
                for (Case PCCASE : pccases)
                {
                    if (isPccase_correct(PCCASE)) baseComponentList.add(PCCASE);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }
        else if (component_class == Cooler.class){
            t = new Thread(() -> {
                List<Cooler> coolers = Room.databaseBuilder(app.getApplicationContext(), DB_COOLERS.class, "COOLERS").build().getMyDao().getCOOLER();
                for (Cooler COOLER : coolers)
                {
                    if (isCooler_correct(COOLER)) baseComponentList.add(COOLER);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }
        else if (component_class == Hdd.class){
            t = new Thread(() -> {
                List<Hdd> hdds = Room.databaseBuilder(app.getApplicationContext(), DB_HDDS.class, "HDDS").build().getMyDao().getHDD();
                for (Hdd HDD : hdds)
                {
                    if (isHdd_correct(HDD)) baseComponentList.add(HDD);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }
        else if (component_class == Ssd.class){
            t = new Thread(() -> {
                List<Ssd> ssds = Room.databaseBuilder(app.getApplicationContext(), DB_SSDS.class, "SSDS").build().getMyDao().getSSD();
                for (Ssd SSD : ssds)
                {
                    if (isSsd25_correct(SSD)) baseComponentList.add(SSD);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }

        // SSD M2
        else if (component_class == Component.class) {
            t = new Thread(() -> {
                List<Ssd> ssds = Room.databaseBuilder(app.getApplicationContext(), DB_SSDS.class, "SSDS").build().getMyDao().getSSD();
                for (Ssd SSD : ssds)
                {
                    if (isSsdM2_correct(SSD)) baseComponentList.add(SSD);
                }

                filterComponentList.addAll(baseComponentList);
                listLiveData.postValue(filterComponentList);
            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t.start();
            }
        }, 200);


    }


    private boolean isMb_correct(Mb MB){
        boolean isCorrect = true;

        // материнские платы, не соответствующие выбранному процессору
        if (current_configuration.mCpu != null)
            if (!MB.getSocket().equals(current_configuration.mCpu.getSocket())) isCorrect = false;

        // материнские платы, не соответствующие выбранному корпусу
        if (current_configuration.mCase != null)
            if (!MB.getFormFactor().equals(current_configuration.mCase.getFormFactor())) isCorrect = false;

        // материнская плата, которая уже выбрана
        if (current_configuration.mMb != null)
            if (current_configuration.mMb.getProduct_code() == MB.getProduct_code()) isCorrect = false;

        return isCorrect;
    }

    private boolean isCpu_correct(Cpu CPU){
        boolean isCorrect = true;

        // соответствие выбранной материнской плате
        if (current_configuration.mMb != null)
        {
            if (!current_configuration.mMb.getSocket().equals(CPU.getSocket())) isCorrect = false;
        }

        // если данный процессор уже добавлен в сборку
        if (current_configuration.mCpu != null)
        {
            if (current_configuration.mCpu.getProduct_code() == CPU.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isGpu_correct(Gpu GPU){
        boolean isCorrect = true;

        // данная видеокарта уже добавлена в сборку
        if (current_configuration.mGpu != null)
        {
            if (current_configuration.mGpu.getProduct_code() == GPU.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isOzu_correct(Ozu OZU) {
        boolean isCorrect = true;

        // проверка на соответствие типов памяти
        if (!current_configuration.mMb.getOzuType().equals(OZU.getType())) isCorrect = false;
        // проверка на количество модулей
        if (current_configuration.mMb.getOzuSlotsQuantity() > OZU.getModulesQuantity()) isCorrect = false;

        // данная память уже добавлена в сборку
        if (current_configuration.mOzu != null)
        {
            if (current_configuration.mOzu.getProduct_code() == OZU.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isBp_correct(Bp BP)
    {
        boolean isCorrect = true;

        // данный БП уже добавлен в сборку
        if (current_configuration.mBp != null)
        {
            if (current_configuration.mBp.getProduct_code() == BP.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isPccase_correct(Case PCCASE)
    {
        boolean isCorrect = true;

        // данный корпус уже добавлен в сборку
        if (current_configuration.mCase != null)
        {
            if (current_configuration.mCase.getProduct_code() == PCCASE.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isCooler_correct(Cooler COOLER)
    {
        boolean isCorrect = false;

        // совместимость с сокетом
        if (current_configuration.mMb != null){
            for (String socket : COOLER.getSupportSockets())
            {
                if (socket.equals(current_configuration.mMb.getSocket())) {
                    isCorrect = true;
                    break;
                }
            }
        }

        // данный кулер уже добавлен в сборку
        if (current_configuration.mCooler != null)
        {
            if (current_configuration.mCooler.getProduct_code() == COOLER.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isHdd_correct(Hdd HDD)
    {
        boolean isCorrect = true;

        // данный ЖД уже добавлен в сборку
        if (current_configuration.mHdd != null)
        {
            if (current_configuration.mHdd.getProduct_code() == HDD.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isSsd25_correct(Ssd SSD)
    {
        boolean isCorrect = true;

        // проверка на форм-фактор
        if (!SSD.getFormFactor().equals("2.5")) isCorrect = false;

        // данный SSD 2.5" уже добавлен в сборку
        if (current_configuration.mSsd_25 != null)
        {
            if (current_configuration.mSsd_25.getProduct_code() == SSD.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    private boolean isSsdM2_correct(Ssd SSD)
    {
        boolean isCorrect = true;

        // проверка на форм-фактор
        if (!SSD.getFormFactor().equals("M2")) isCorrect = false;

        // данный SSD 2.5" уже добавлен в сборку
        if (current_configuration.mSsd_m2 != null)
        {
            if (current_configuration.mSsd_m2.getProduct_code() == SSD.getProduct_code()) isCorrect = false;
        }

        return isCorrect;
    }

    AlertDialog filterDialog(){

        View dialogView;
        AlertDialog.Builder builder;

        // фильтры для материнских плат
        if (component_class == Mb.class)
        {
            dialogView = inflater.inflate(R.layout.dialog_filter_mb, null);
            builder = new AlertDialog.Builder(dialogView.getContext());
            builder.setView(dialogView);

            // сокет и чипсет
            ChipGroup socketChipGroup = dialogView.findViewById(R.id.socketChipGroup);
            TextView chipsetTitle = dialogView.findViewById(R.id.chipsetTitle);
            ChipGroup chipsetChipGroup = dialogView.findViewById(R.id.chipsetChipGroup);

            // если процессор выбран, фильтр по сокету невозможен
            if (current_configuration.mCpu != null) {
                Chip chip = new Chip(dialogView.getContext());
                chip.setId(View.generateViewId());
                chip.setText(current_configuration.mCpu.getSocket());
                chip.setChecked(true);
                chip.setCheckable(false);

                socketChipGroup.addView(chip);

                // фильтр по чипсету
                String current_socket = current_configuration.mCpu.getSocket();
                // все доступные чипсеты
                List<String> availableChipsets = new ArrayList<>();
                for (Component component : baseComponentList)
                {
                    Mb MB = (Mb) component;
                    if (MB.getSocket().equals(current_socket))
                    {
                        if (!availableChipsets.contains(MB.getChipset())) availableChipsets.add(MB.getChipset());
                    }
                }

                // добавляем в ChipGroup доступные чипсеты
                chipsetChipGroup.removeAllViews();
                for (String filterValue : availableChipsets)
                {
                    Chip chip1 = new Chip(dialogView.getContext());
                    chip.setText(filterValue);

                    if (currentFilter_mb.getChipsets() != null)
                    {
                        if (currentFilter_mb.getChipsets().contains(filterValue)) chip1.setChecked(true);
                    }


                    chipsetChipGroup.addView(chip1);
                }
            }
            else {
                int currentFilterChipId = View.NO_ID;                                               // id чипа для текущего фильтра сокета

                // все доступные сокеты
                List<String> availableSockets = new ArrayList<>();
                for (Component component : baseComponentList)
                {
                    Mb MB = (Mb)component;
                    if (!availableSockets.contains(MB.getSocket())) availableSockets.add(MB.getSocket());
                }

                // добавляем в ChipGroup доступные сокеты
                for (String filterValue : availableSockets)
                {
                    Chip chip = new Chip(dialogView.getContext());
                    chip.setId(View.generateViewId());
                    chip.setText(filterValue);

                    if (filterValue.equals(currentFilter_mb.getSocket())) currentFilterChipId = chip.getId();

                    socketChipGroup.addView(chip);
                }



                socketChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        // если сокет не выбран, скрываем выбор чипсета
                        if (checkedId == View.NO_ID){
                            chipsetChipGroup.setVisibility(View.GONE);
                            chipsetTitle.setVisibility(View.GONE);
                        }
                        else {
                            // показываем ChipGroup с чипсетами
                            chipsetChipGroup.setVisibility(View.VISIBLE);
                            chipsetTitle.setVisibility(View.VISIBLE);

                            Chip chip1 = socketChipGroup.findViewById(checkedId);
                            String current_socket = chip1.getText().toString();

                            // все доступные чипсеты
                            List<String> availableChipsets = new ArrayList<>();
                            for (Component component : baseComponentList)
                            {
                                Mb MB = (Mb) component;
                                if (MB.getSocket().equals(current_socket))
                                {
                                    if (!availableChipsets.contains(MB.getChipset())) availableChipsets.add(MB.getChipset());
                                }
                            }

                            // добавляем в ChipGroup доступные чипсеты
                            chipsetChipGroup.removeAllViews();
                            for (String filterValue : availableChipsets)
                            {
                                Chip chip = new Chip(dialogView.getContext());
                                chip.setText(filterValue);

                                if (currentFilter_mb.getChipsets() != null)
                                {
                                    if (currentFilter_mb.getChipsets().contains(filterValue)) chip.setChecked(true);
                                }

                                chipsetChipGroup.addView(chip);
                            }
                        }
                    }
                });

                if (currentFilterChipId != View.NO_ID){
                    Chip chip = socketChipGroup.findViewById(currentFilterChipId);
                    chip.setChecked(true);
                }

                // пока сокет не выбран, чипсет выбирать нельзя
                if(socketChipGroup.getCheckedChipId() == View.NO_ID){
                    chipsetChipGroup.setVisibility(View.GONE);
                    chipsetTitle.setVisibility(View.GONE);
                }

            }

            // форм-фактор
            ChipGroup formFactorChipGroup = dialogView.findViewById(R.id.formFactorChipGroup);

            // все доступные форм-факторы
            List<String> availableFormFactors = new ArrayList<>();
            for (Component component : baseComponentList)
            {
                Mb MB = (Mb) component;
                if (!availableFormFactors.contains(MB.getFormFactor())) availableFormFactors.add(MB.getFormFactor());
            }

            formFactorChipGroup.removeAllViews();

            // добавляем в ChipGroup доступные форм-факторы
            for (String filterValue : availableFormFactors){
                Chip chip = new Chip(dialogView.getContext());
                chip.setText(filterValue);
                formFactorChipGroup.addView(chip);

                if (availableFormFactors.size() == 1) {
                    chip.setChecked(true);
                    chip.setCheckable(false);
                }
            }

            // тип памяти
            ChipGroup ozuTypeChipGroup = dialogView.findViewById(R.id.ozuTypeChipGroup);

            // все доступные типы памяти
            List<String> availableOzuTypes = new ArrayList<>();
            for (Component component : baseComponentList)
            {
                Mb MB = (Mb) component;
                if (!availableOzuTypes.contains(MB.getOzuType())) availableOzuTypes.add(MB.getOzuType());
            }

            ozuTypeChipGroup.removeAllViews();

            // добавляем в ChipGroup доступные типы памяти
            for (String filterValue : availableOzuTypes)
            {
                Chip chip = new Chip(dialogView.getContext());
                chip.setText(filterValue);
                ozuTypeChipGroup.addView(chip);

                if (availableOzuTypes.size() == 1) {
                    chip.setChecked(true);
                    chip.setCheckable(false);
                }
            }

            // количество слотов памяти

            ChipGroup ozuSlotsQuantityChipGroup = dialogView.findViewById(R.id.ozuSlotsQuantityChipGroup);

            // все доступные фильтры
            List<Integer> availableOzuSlotsQuantities = new ArrayList<>();
            for (Component component : baseComponentList)
            {
                Mb MB = (Mb)component;
                if (!availableOzuSlotsQuantities.contains(MB.getOzuSlotsQuantity())) availableOzuSlotsQuantities.add(MB.getOzuSlotsQuantity());
            }

            ozuSlotsQuantityChipGroup.removeAllViews();

            // добавляем в ChipGroup все доступные фильтры
            for (Integer filterValue : availableOzuSlotsQuantities){
                Chip chip = new Chip(dialogView.getContext());
                chip.setText(filterValue + "");
                ozuSlotsQuantityChipGroup.addView(chip);

                if (availableOzuSlotsQuantities.size() == 1)
                {
                    chip.setChecked(true);
                    chip.setCheckable(false);
                }
            }

            // количество разъёмов M2

            ChipGroup M2QuantityChipGroup = dialogView.findViewById(R.id.M2QuantityChipGroup);

            // все доступные фильтры
            List<Integer> availableM2Quantities = new ArrayList<>();
            for (Component component : baseComponentList)
            {
                Mb MB = (Mb) component;
                if (!availableM2Quantities.contains(MB.getM2())) availableM2Quantities.add(MB.getM2());
            }

            M2QuantityChipGroup.removeAllViews();

            // добавляем в ChipGroup все доступные фильтры
            for (Integer filterValue : availableM2Quantities){
                Chip chip = new Chip(dialogView.getContext());
                chip.setText(filterValue + "");
                M2QuantityChipGroup.addView(chip);

                if (availableM2Quantities.size() == 1)
                {
                    chip.setChecked(true);
                    chip.setCheckable(false);
                }
            }
        }
        // фильтры для процессоров
        else if (component_class == Cpu.class){
            dialogView = inflater.inflate(R.layout.dialog_filter_mb, null);
            builder = new AlertDialog.Builder(dialogView.getContext());
            builder.setView(dialogView);



        }
        else {
            builder = null;
            dialogView = null;
        }


        AlertDialog dialog = builder.create();

        MaterialButton applyFilters = dialogView.findViewById(R.id.apply);
        applyFilters.setOnClickListener(view ->{
            applyMbFilters(dialogView, dialog);

        });

        ImageButton closeButton = dialogView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(view ->{
            dialog.dismiss();
        });


        return dialog;
    }

    // применение фильтров для материнских плат
    private void applyMbFilters(View dialogView, AlertDialog dialog){

        // собираем все установленные фильтры

        // сокет
        ChipGroup socketGroup = dialogView.findViewById(R.id.socketChipGroup);
        Chip chip = socketGroup.findViewById(socketGroup.getCheckedChipId());
        if (chip != null) currentFilter_mb.setSocket(chip.getText().toString());
        else {
            currentFilter_mb.setSocket(null);
        }

        // чипсеты
        List<String> chipsets = new ArrayList<>();
        ChipGroup chipsetGroup = dialogView.findViewById(R.id.chipsetChipGroup);

        for (int id : chipsetGroup.getCheckedChipIds())
        {
            chip = chipsetGroup.findViewById(id);
            if (chip != null) chipsets.add(chip.getText().toString());
        }
        currentFilter_mb.setChipsets(chipsets);

        // форм-факторы
        List<String> formFactors = new ArrayList<>();
        ChipGroup formFactorGroup = dialogView.findViewById(R.id.formFactorChipGroup);

        for (int id : formFactorGroup.getCheckedChipIds())
        {
            chip = formFactorGroup.findViewById(id);
            if (chip != null) formFactors.add(chip.getText().toString());
        }
        currentFilter_mb.setFormFactors(formFactors);

        // типы памяти
        List<String> ozuTypes = new ArrayList<>();
        ChipGroup ozuTypeGroup = dialogView.findViewById(R.id.ozuTypeChipGroup);

        for (int id : ozuTypeGroup.getCheckedChipIds())
        {
            chip = ozuTypeGroup.findViewById(id);
            if (chip != null) ozuTypes.add(chip.getText().toString());
        }
        currentFilter_mb.setOzuTypes(ozuTypes);


        // количество слотов памяти
        List<Integer> ozuSlotsQuantities = new ArrayList<>();
        ChipGroup ozuSlotsQuantityGroup = dialogView.findViewById(R.id.ozuSlotsQuantityChipGroup);

        for (int id : ozuSlotsQuantityGroup.getCheckedChipIds())
        {
            chip = ozuSlotsQuantityGroup.findViewById(id);
            if (chip != null) ozuSlotsQuantities.add(Integer.parseInt(chip.getText().toString()));
        }
        currentFilter_mb.setOzuSlotsQuantities(ozuSlotsQuantities);

        // количество слотов M2
        List<Integer> M2Quantities = new ArrayList<>();
        ChipGroup M2QuantitiesGroup = dialogView.findViewById(R.id.M2QuantityChipGroup);

        for (int id : M2QuantitiesGroup.getCheckedChipIds())
        {
            chip = M2QuantitiesGroup.findViewById(id);
            if (chip != null) M2Quantities.add(Integer.parseInt(chip.getText().toString()));
        }
        currentFilter_mb.setM2Quantities(M2Quantities);

        // очищаем текущий список
        filterComponentList.removeAll(filterComponentList);

        for (Component component : baseComponentList)
        {
            Mb MB = (Mb)component;
            boolean componentCorrect = true;

            // соответствие сокета
            if (currentFilter_mb.getSocket() != null)
            {
                if (!MB.getSocket().equals(currentFilter_mb.getSocket())) componentCorrect = false;
            }

            // соответствие чипсета
            boolean chipsetMatch = false;
            if (currentFilter_mb.getChipsets().size() == 0) chipsetMatch = true;

            for (String s : currentFilter_mb.getChipsets())
            {
                if (MB.getChipset().equals(s)) chipsetMatch = true;
            }

            if (!chipsetMatch) componentCorrect = false;

            // соответствие форм-фактора
            boolean formFactorMatch = false;
            if (currentFilter_mb.getFormFactors().size() == 0) formFactorMatch = true;

            for (String s : currentFilter_mb.getFormFactors())
            {
                if (MB.getFormFactor().equals(s)) formFactorMatch = true;
            }

            if (!formFactorMatch) componentCorrect = false;

            // соответствие типа памяти
            boolean ozuTypeMatch = false;
            if (currentFilter_mb.getOzuTypes().size() == 0) ozuTypeMatch = true;

            for (String s : currentFilter_mb.getOzuTypes())
            {
                if (MB.getOzuType().equals(s)) ozuTypeMatch = true;
            }

            if (!ozuTypeMatch) componentCorrect = false;

            // соответствие количества слотов памяти
            boolean ozuQuantityMatch = false;
            if (currentFilter_mb.getOzuSlotsQuantities().size() == 0) ozuQuantityMatch = true;

            for (int s : currentFilter_mb.getOzuSlotsQuantities())
            {
                if (MB.getOzuSlotsQuantity() == s) ozuQuantityMatch = true;
            }

            if (!ozuQuantityMatch) componentCorrect = false;

            // соответствие количества слотов M2
            boolean M2QuantityMatch = false;
            if (currentFilter_mb.getM2Quantities().size() == 0) M2QuantityMatch = true;

            for (int s : currentFilter_mb.getM2Quantities())
            {
                if (MB.getM2() == s) M2QuantityMatch = true;
            }

            if (!M2QuantityMatch) componentCorrect = false;

            if (componentCorrect) filterComponentList.add(MB);
        }

        listLiveData.postValue(filterComponentList);
        dialog.dismiss();
    }
}
