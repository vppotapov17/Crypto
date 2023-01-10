package com.potapp.cyberhelper.models.components;
// 1% производительности = 264,34
// формула расчёта производительности: производительность (%) = Passmark_test / 264,34;

import android.util.Log;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.firebase.database.DataSnapshot;
import com.potapp.cyberhelper.models.fpsTest;
import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Gpu extends Component{

    // основные характеристики

    private String GpProducer;                                                                      // производитель ГП
    private String GpModel;                                                                         // модель ГП

    private String gpInterface;                                                                     // интерфейс
    private int gpFrequency;                                                                        // базовая частота ГП, МГц
    private int gpBoostFrequency;                                                                     // турбо-частота ГП, МГц
    private int techprocess;                                                                        // техпроцесс, нм
    private String maxResoultion;                                                                   // максимальное разрешение

    private String coolingSystem;                                                                   // система охлаждения

    // разъёмы

    private int Dvi;                                                                                // кол-во Dvi
    private int hdmi;                                                                               // кол-во Hdmi
    private String hdmiVer;                                                                         // версия Hdmi
    private int displayPort;                                                                        // кол-во DisplayPort
    private String displayPortVer;                                                                  // версия DisplayPort

    private String optionalPower;                                                                   // доп. питание


    // видеопамять

    private String ozuType;                                                                         // тип
    private int ozuSize;                                                                            // объём, ГБ
    private int ozuFrequency;                                                                       // частота, МГц
    private int bitDepth;                                                                           // разрядность шины, бит

    private int capacity;                                                                           // производительность
    private int ratio;                                                                              // цена/качество

    // технологии
    private boolean dlss;                                                                           // dlss
    private boolean rayTracing;                                                                     // поддержка трассировки лучей

    // прочее
    private int bench;                                                                              // результаты 3dMark
    private int length;                                                                             // длина
    private String date;                                                                            // дата релиза

    private String heatPipes;                                                                       // тепловые трубки
    private int maxTdp;                                                                             // максимальное тепловыделение
    private int minBp;                                                                              // минимальная мощность БП
    private int monitors;                                                                           // количество мониторов
    private boolean OCEdition;                                                                      // OverClock Edition
    private int RTCores;                                                                            // количество RT ядер
    private int tensorCores;                                                                        // количество тензорных ядер
    private int rasterizeBlocks;                                                                    // количество блоков растеризации
    private int textureBlocks;                                                                      // количество блоков текстурирования

    //----------------------------------------------------------------------------------------------
    // get-методы
    //----------------------------------------------------------------------------------------------

    public String getGpProducer() {
        return GpProducer;
    }

    public String getGpModel() {
        return GpModel;
    }

    public String getGpInterface() {
        return gpInterface;
    }

    public int getGpFrequency() {
        return gpFrequency;
    }

    public int getGpBoostFrequency() {
        return gpBoostFrequency;
    }

    public int getTechprocess() {
        return techprocess;
    }

    public String getMaxResoultion() {
        return maxResoultion;
    }

    public String getCoolingSystem() {
        return coolingSystem;
    }

    public int getDvi() {
        return Dvi;
    }

    public int getHdmi() {
        return hdmi;
    }

    public String getHdmiVer() {
        return hdmiVer;
    }

    public int getDisplayPort() {
        return displayPort;
    }

    public String getDisplayPortVer() {
        return displayPortVer;
    }

    public String getOptionalPower() {
        return optionalPower;
    }

    public String getOzuType() {
        return ozuType;
    }

    public int getOzuSize() {
        return ozuSize;
    }

    public int getOzuFrequency() {
        return ozuFrequency;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRatio() {
        return ratio;
    }

    public boolean isDlss() {
        return dlss;
    }

    public boolean isRayTracing() {
        return rayTracing;
    }

    public int getBench() {
        return bench;
    }

    public int getLength() {
        return length;
    }

    public String getDate() {
        return date;
    }

    public String getHeatPipes() {
        return heatPipes;
    }

    public int getMaxTdp() {
        return maxTdp;
    }

    public int getMinBp() {
        return minBp;
    }

    public int getMonitors() {
        return monitors;
    }

    public boolean isOCEdition() {
        return OCEdition;
    }

    public int getRTCores() {
        return RTCores;
    }

    public int getTensorCores() {
        return tensorCores;
    }

    public int getRasterizeBlocks() {
        return rasterizeBlocks;
    }

    public int getTextureBlocks() {
        return textureBlocks;
    }


    //----------------------------------------------------------------------------------------------
    // set-методы
    //----------------------------------------------------------------------------------------------\

    public void setGpProducer(String gpProducer) {
        GpProducer = gpProducer;
    }

    public void setGpModel(String gpModel) {
        GpModel = gpModel;
    }

    public void setGpInterface(String gpInterface) {
        this.gpInterface = gpInterface;
    }

    public void setGpFrequency(int gpFrequency) {
        this.gpFrequency = gpFrequency;
    }

    public void setGpBoostFrequency(int gpBoostFrequency) {
        this.gpBoostFrequency = gpBoostFrequency;
    }

    public void setTechprocess(int techprocess) {
        this.techprocess = techprocess;
    }

    public void setMaxResoultion(String maxResoultion) {
        this.maxResoultion = maxResoultion;
    }

    public void setCoolingSystem(String coolingSystem) {
        this.coolingSystem = coolingSystem;
    }

    public void setDvi(int dvi) {
        Dvi = dvi;
    }

    public void setHdmi(int hdmi) {
        this.hdmi = hdmi;
    }

    public void setHdmiVer(String hdmiVer) {
        this.hdmiVer = hdmiVer;
    }

    public void setDisplayPort(int displayPort) {
        this.displayPort = displayPort;
    }

    public void setDisplayPortVer(String displayPortVer) {
        this.displayPortVer = displayPortVer;
    }

    public void setOptionalPower(String optionalPower) {
        this.optionalPower = optionalPower;
    }

    public void setOzuType(String ozuType) {
        this.ozuType = ozuType;
    }

    public void setOzuSize(int ozuSize) {
        this.ozuSize = ozuSize;
    }

    public void setOzuFrequency(int ozuFrequency) {
        this.ozuFrequency = ozuFrequency;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public void setDlss(boolean dlss) {
        this.dlss = dlss;
    }

    public void setRayTracing(boolean rayTracing) {
        this.rayTracing = rayTracing;
    }

    public void setBench(int bench) {
        this.bench = bench;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeatPipes(String heatPipes) {
        this.heatPipes = heatPipes;
    }

    public void setMaxTdp(int maxTdp) {
        this.maxTdp = maxTdp;
    }

    public void setMinBp(int minBp) {
        this.minBp = minBp;
    }

    public void setMonitors(int monitors) {
        this.monitors = monitors;
    }

    public void setOCEdition(boolean OCEdition) {
        this.OCEdition = OCEdition;
    }

    public void setRTCores(int RTCores) {
        this.RTCores = RTCores;
    }

    public void setTensorCores(int tensorCores) {
        this.tensorCores = tensorCores;
    }

    public void setRasterizeBlocks(int rasterizeBlocks) {
        this.rasterizeBlocks = rasterizeBlocks;
    }

    public void setTextureBlocks(int textureBlocks) {
        this.textureBlocks = textureBlocks;
    }


    //----------------------------------------------------------------------------------------------
    // дополнительные методы
    //----------------------------------------------------------------------------------------------

    public int getAverageFPS()
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
        spec.setSpecValue(ratio + " %");

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
        specs.add(new String[]{"Производитель", getProducer()});
        specs.add(new String[]{"Модель", getModel()});

        specs.add(new String[]{"Производительность", getAverageFPS() + ""});
        specs.add(new String[]{"Цена/качество", ratio + "%"});
        specs.add(new String[]{"Модель ГП", getGpModel()});
        if (gpFrequency != 0) specs.add(new String[]{"Частота", gpFrequency + " МГц"});
        if (techprocess != 0) specs.add(new String[]{"Техпроцесс", techprocess + " нм"});
        if (gpInterface != null) specs.add(new String[]{"Интерфейс", gpInterface});
        if (maxTdp != 0) specs.add(new String[]{"Тепловыделение", maxTdp + " Вт"});

        specs.add(new String[]{"Видеопамять", ""});
        if (ozuSize != 0) specs.add(new String[]{"Объём", ozuSize + " Гб"});
        if (ozuType != null) specs.add(new String[]{"Тип", ozuType});
        if (ozuFrequency != 0) specs.add(new String[]{"Частота", ozuFrequency + "МГц"});
        if (bitDepth != 0) specs.add(new String[]{"Разрядность шины", bitDepth + " bit"});

        specs.add(new String[]{"Прочее", ""});
        if (maxResoultion != null) specs.add(new String[]{"Максимальное разрешение", maxResoultion});

        if (rayTracing) specs.add(new String[]{"Трассировка лучей", "есть"});
        else specs.add(new String[]{"Трассировка лучей", "ytn"});

        if (optionalPower != null) specs.add(new String[]{"Доп. питание", optionalPower});


        return specs;
    }

    public static Gpu createFromSnapshot(DataSnapshot snap){
        Gpu gpu = new Gpu();

        gpu.setProduct_code(Integer.parseInt(snap.getKey()));
        gpu.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
        gpu.setProducer(snap.child("Producer").getValue().toString());
        gpu.setModel(snap.child("Model").getValue().toString());
        gpu.setRefLink(snap.child("Url").getValue().toString());
        gpu.setPictureLink(snap.child("Picture").getValue().toString());

        gpu.setGpProducer(snap.child("GpProducer").getValue().toString());
        gpu.setGpModel(snap.child("GpModel").getValue().toString());
        gpu.setBench(Integer.parseInt(snap.child("Bench").getValue().toString()));
        gpu.setBitDepth(Integer.parseInt(snap.child("BitDepth").getValue().toString()));

        try {
            if (snap.child("Interface").getValue() != null) gpu.setGpInterface(snap.child("Interface").getValue().toString());
        }
        catch (NullPointerException e){}

        try {
            gpu.setGpFrequency(Integer.parseInt(snap.child("GpFrequency").getValue().toString()));
        }catch (Exception e){
            Log.d("AAA", snap.getKey());
        }
        try {
            gpu.setGpBoostFrequency(Integer.parseInt(snap.child("GpBoostFrequency").getValue().toString()));
        }catch (NullPointerException e){}

        try {
            gpu.setTechprocess(Integer.parseInt(snap.child("Techprocess").getValue().toString()));
        }catch (NullPointerException e){
            Log.d("AAA", snap.getKey());
        }

        try {
            gpu.setMaxResoultion(snap.child("MaxResolution").getValue().toString());
        }catch (NullPointerException e){}
        try {
            gpu.setCoolingSystem(snap.child("CoolingSystem").getValue().toString());
        }catch (NullPointerException e){}
        try {
            gpu.setDvi(Integer.parseInt(snap.child("Dvi").getValue().toString()));
        }catch (NullPointerException e){}
        try {
            gpu.setHdmi(Integer.parseInt(snap.child("Hdmi").getValue().toString()));
        }catch (NullPointerException e){}
        try {
            gpu.setHdmiVer(snap.child("HdmiVer").getValue().toString());
        }catch (NullPointerException e){}
        try {
            gpu.setDisplayPort(Integer.parseInt(snap.child("DisplayPort").getValue().toString()));
        }catch (NullPointerException e){}
        try {
            gpu.setDisplayPortVer(snap.child("DisplayPortVer").getValue().toString());
        }catch (NullPointerException e){}
        try {
            gpu.setOptionalPower(snap.child("OptionalPower").getValue().toString());
        }catch (NullPointerException e){}

        gpu.setOzuType(snap.child("OzuType").getValue().toString());
        gpu.setOzuSize(Integer.parseInt(snap.child("OzuSize").getValue().toString()));
        gpu.setOzuFrequency(Integer.parseInt(snap.child("OzuFrequency").getValue().toString()));

        try {
            gpu.setMaxTdp(Integer.parseInt(snap.child("MaxTdp").getValue().toString()));
        }catch (NullPointerException e){
            Log.d("AAA", snap.getKey());
        }

        try {
            if (snap.child("DLSS").getValue().toString().equals("есть")) gpu.setDlss(true);
            else gpu.setDlss(false);
        }catch (NullPointerException e){}

        try {
            if (snap.child("RayTracing").getValue().toString().equals("есть"))
                gpu.setRayTracing(true);
            else gpu.setRayTracing(false);
        }catch (NullPointerException e){}

        try {
            gpu.setLength((int)Double.parseDouble(snap.child("Length").getValue().toString()));
        }catch (NullPointerException e){}
        try {
            gpu.setDate(snap.child("Date").getValue().toString());
        }catch (NullPointerException e){}
        try {
            gpu.setHeatPipes(snap.child("HeatPipes").getValue().toString());
        }catch (NullPointerException e){}
        try {
            gpu.setMinBp(Integer.parseInt(snap.child("MinBp").getValue().toString()));
        }catch (NullPointerException e){}
        try {
            gpu.setMonitors(Integer.parseInt(snap.child("Monitors").getValue().toString()));
        }catch (NullPointerException e){}

        if (snap.child("OverClockEdition").getValue().toString().equals("есть")) gpu.setOCEdition(true);
        else gpu.setOCEdition(false);

        try {
            gpu.setRTCores(Integer.parseInt(snap.child("RTCores").getValue().toString()));
        }catch (NullPointerException e){}

        try {
            gpu.setTensorCores(Integer.parseInt(snap.child("TensorCores").getValue().toString()));
        }catch (NullPointerException e){}

        try {
            gpu.setRasterizeBlocks(Integer.parseInt(snap.child("RasterizeBlocks").getValue().toString()));
        }catch (NullPointerException e){}

        try {
            gpu.setTextureBlocks(Integer.parseInt(snap.child("TextureBlocks").getValue().toString()));
        }catch (NullPointerException e){}

        return gpu;
    }


    public HashMap<String, String> toFirebase(){
        HashMap<String, String> map = new HashMap<>();
        return map;
    }

}
