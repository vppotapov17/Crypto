package com.potapp.cyberhelper.data.models.components;
// 1% производительности = 264,34
// формула расчёта производительности: производительность (%) = Passmark_test / 264,34;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.potapp.cyberhelper.data.models.fpsTest;
import com.potapp.cyberhelper.data.models.mainSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Gpu extends Component{

    // основные характеристики

    private double pciE_version;                                                                    // версия PCI-Express
    private int baseFrequency;                                                                      // базовая частота ГП, МГц
    private int turboFrequency;                                                                     // турбо-частота ГП, МГц
    private int techprocess;                                                                        // техпроцесс, нм
    private String maxResoultion;                                                                   // максимальное разрешение
    private int energyConsumption;                                                                  // максимальное энергопотребление

    private String optionalPower;                                                                   // доп. питание
    private boolean rayTracing;                                                                     // поддержка трассировки лучей

    // видеопамять

    private String memoryType;                                                                      // тип
    private int memorySize;                                                                         // объём, ГБ
    private int memoryFrequency;                                                                    // частота, МГц
    private int bitDepth;                                                                           // разрядность шины, бит

    @Ignore
    private HashMap<String, String> otherSpecifications;                                            // остальные характеристики

    @Embedded
    private ArrayList<fpsTest> fpsTests;                                                            // тесты видеокарты в играх
    private String date;                                                                            // дата релиза

    //----------------------------------------------------------------------------------------------
    // get-методы
    //----------------------------------------------------------------------------------------------

    public double getPciE_version() {
        return pciE_version;
    }

    public int getBaseFrequency() {
        return baseFrequency;
    }

    public int getTurboFrequency() {
        return turboFrequency;
    }

    public HashMap<String, String> getOtherSpecifications() {
        return otherSpecifications;
    }

    public int getTechprocess() {
        return techprocess;
    }

    public String getMaxResoultion() {
        return maxResoultion;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public String getOptionalPower() {
        return optionalPower;
    }

    public boolean isRayTracing() {
        return rayTracing;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public int getMemoryFrequency() {
        return memoryFrequency;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public ArrayList<fpsTest> getFpsTests() {
        return fpsTests;
    }

    public String getDate() {
        return date;
    }


    //----------------------------------------------------------------------------------------------
    // set-методы
    //----------------------------------------------------------------------------------------------\

    public void setPciE_version(double pciE_version) {
        this.pciE_version = pciE_version;
    }

    public void setBaseFrequency(int baseFrequency) {
        this.baseFrequency = baseFrequency;
    }

    public void setTurboFrequency(int turboFrequency) {
        this.turboFrequency = turboFrequency;
    }

    public void setOtherSpecifications(HashMap<String, String> otherSpecifications) {
        this.otherSpecifications = otherSpecifications;
    }

    public void setTechprocess(int techprocess) {
        this.techprocess = techprocess;
    }

    public void setMaxResoultion(String maxResoultion) {
        this.maxResoultion = maxResoultion;
    }

    public void setEnergyConsumption(int energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public void setOptionalPower(String optionalPower) {
        this.optionalPower = optionalPower;
    }

    public void setRayTracing(boolean rayTracing) {
        this.rayTracing = rayTracing;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public void setMemoryFrequency(int memoryFrequency) {
        this.memoryFrequency = memoryFrequency;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public void setFpsTests(ArrayList<fpsTest> fpsTests) {
        this.fpsTests = fpsTests;
    }

    public void setDate(String date) {
        this.date = date;
    }


    //----------------------------------------------------------------------------------------------
    // дополнительные методы
    //----------------------------------------------------------------------------------------------

    public int getAverageFPS()
    {
        return 100;
    }

    public int getRatio()
    {
        return 100;
    }

    //----------------------------------------------------------------------------------------------
    // переопределённые методы класса Component
    //----------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Средний FPS");
        spec.setSpecValue(getAverageFPS() + "");

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Цена/качество");
        spec.setSpecValue(getRatio() + " %");

        return spec;
    }

    @Override
    public mainSpec getMainSpec3() {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Дата релиза");
        spec.setSpecValue(getDate() + " %");

        return spec;

    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specs = new ArrayList<>();

        specs.add(new String[]{"Основные характеристики", ""});
        specs.add(new String[]{"Производительность", getAverageFPS() + ""});
        specs.add(new String[]{"Цена/качество", getRatio() + "%"});
        specs.add(new String[]{"Видеочипсет", getModel()});
        if (baseFrequency != 0) specs.add(new String[]{"Частота", baseFrequency + " МГц"});
        if (techprocess != 0) specs.add(new String[]{"Техпроцесс", techprocess + " нм"});
        if (pciE_version != 0) specs.add(new String[]{"Интерфейс", "PCI-E " + pciE_version});
        if (energyConsumption != 0) specs.add(new String[]{"Энергопотребление", energyConsumption + " Вт"});

        specs.add(new String[]{"Видеопамять", ""});
        if (memorySize != 0) specs.add(new String[]{"Объём", memorySize + " Гб"});
        if (memoryType != null) specs.add(new String[]{"Тип", memoryType});
        if (memoryFrequency != 0) specs.add(new String[]{"Частота", memoryFrequency + "МГц"});
        if (bitDepth != 0) specs.add(new String[]{"Разрядность шины", bitDepth + " bit"});

        specs.add(new String[]{"Прочее", ""});
        if (maxResoultion != null) specs.add(new String[]{"Максимальное разрешение", maxResoultion});

        if (rayTracing) specs.add(new String[]{"Трассировка лучей", "есть"});
        else specs.add(new String[]{"Трассировка лучей", "ytn"});

        if (optionalPower != null) specs.add(new String[]{"Доп. питание", optionalPower});


        return specs;
    }
}
