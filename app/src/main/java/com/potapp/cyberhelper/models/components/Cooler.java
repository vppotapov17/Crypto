package com.potapp.cyberhelper.models.components;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.firebase.database.DataSnapshot;
import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Cooler extends Component{

    // основные характеристики
    private int tdp;                                                                                // рассеиваемая мощность
    private String coolingType;                                                                     // тип охлаждения
    private int fanQuantity;                                                                        // количество вентиляторов
    private int fanSize;                                                                            // размер вентилятора, мм

    private String airFlow;                                                                         // воздушный поток, CFM
    private String noiseLevel;                                                                      // уровень шума, дБ
    private String rotationSpeed;                                                                   // скорость вращения, об/мин

    // совместимость с сокетами
    private boolean AM4;
    private boolean lga1200;
    private boolean lga1700;

    // конструкция
    private String heatPipes;                                                                       // тепловые трубки
    private String heatPipes_material;                                                              // материал тепловых трубок
    private String radiatorMaterial;                                                                // материал радиатора

    // прочее
    private String power;                                                                           // питание
    private String backlight;                                                                       // цвет подсветки (если есть)
    private boolean paste;                                                                          // термопаста в комплекте
    private String weight;                                                                          // вес
    // ---------------------------------------------------------------------------------------------
    // get-методы
    // ---------------------------------------------------------------------------------------------


    public boolean isAM4() {
        return AM4;
    }

    public boolean isLga1200() {
        return lga1200;
    }

    public boolean isLga1700() {
        return lga1700;
    }

    public int getTdp() {
        return tdp;
    }

    public String getCoolingType() {
        return coolingType;
    }

    public int getFanQuantity() {
        return fanQuantity;
    }

    public int getFanSize() {
        return fanSize;
    }

    public String getAirFlow() {
        return airFlow;
    }

    public String getNoiseLevel() {
        return noiseLevel;
    }

    public String getRotationSpeed() {
        return rotationSpeed;
    }

    public String getHeatPipes() {
        return heatPipes;
    }

    public String getHeatPipes_material() {
        return heatPipes_material;
    }

    public String getRadiatorMaterial() {
        return radiatorMaterial;
    }

    public String getPower() {
        return power;
    }

    public String getBacklight() {
        return backlight;
    }

    public boolean isPaste() {
        return paste;
    }

    public String getWeight() {
        return weight;
    }

    // ---------------------------------------------------------------------------------------------
    // set-методы
    // ---------------------------------------------------------------------------------------------


    public void setAM4(boolean AM4) {
        this.AM4 = AM4;
    }

    public void setLga1200(boolean lga1200) {
        this.lga1200 = lga1200;
    }

    public void setLga1700(boolean lga1700) {
        this.lga1700 = lga1700;
    }

    public void setTdp(int tdp) {
        this.tdp = tdp;
    }

    public void setCoolingType(String coolingType) {
        this.coolingType = coolingType;
    }

    public void setFanQuantity(int fanQuantity) {
        this.fanQuantity = fanQuantity;
    }

    public void setFanSize(int fanSize) {
        this.fanSize = fanSize;
    }

    public void setAirFlow(String airFlow) {
        this.airFlow = airFlow;
    }

    public void setNoiseLevel(String noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public void setRotationSpeed(String rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setHeatPipes(String heatPipes) {
        this.heatPipes = heatPipes;
    }

    public void setHeatPipes_material(String heatPipes_material) {
        this.heatPipes_material = heatPipes_material;
    }

    public void setRadiatorMaterial(String radiatorMaterial) {
        this.radiatorMaterial = radiatorMaterial;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setBacklight(String backlight) {
        this.backlight = backlight;
    }

    public void setPaste(boolean paste) {
        this.paste = paste;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    // ---------------------------------------------------------------------------------------------
    // переопределённые методы класса Component
    // ---------------------------------------------------------------------------------------------


    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Рассеиваемая мощность");
        spec.setSpecValue(tdp + " Вт");

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Уровень шума");
        spec.setSpecValue(noiseLevel);

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Скорость");

        spec.setSpecValue(rotationSpeed);

        return spec;
    }

    @Override
    public List<String[]> specifications() {

        List<String[]> specifications = new ArrayList<>();

        String str[];

        str = new String[]{"Основные характеристики", ""};
        specifications.add(str);
        if (tdp != 0) {
            str = new String[]{"Рассеиваемая мощность", tdp + " Вт"};
            specifications.add(str);
        }
        if (coolingType != null) {
            str = new String[]{"Тип охлаждения", coolingType};
            specifications.add(str);
        }
        if (fanQuantity != 0) {
            str = new String[]{"Количество вентиляторов", fanQuantity + ""};
            specifications.add(str);
        }
        if (fanSize != 0) {
            str = new String[]{"Размер вентилятора", fanSize + " мм"};
            specifications.add(str);
        }
        if (airFlow != null) {
            str = new String[]{"Воздушный поток", airFlow};
            specifications.add(str);
        }
        if (noiseLevel != null) {
            str = new String[]{"Уровень шума", noiseLevel};
            specifications.add(str);
        }
        if (rotationSpeed != null) {
            str = new String[]{"Скорость вращения", rotationSpeed};
            specifications.add(str);
        }

        str = new String[]{"Поддержка сокетов", ""};
        specifications.add(str);

        if (AM4 == true) str = new String[]{"AM4", "да"};
        else str = new String[]{"AM4", "нет"};
        specifications.add(str);

        if (lga1200 == true) str = new String[]{"LGA 1200", "да"};
        else str = new String[]{"LGA 1200", "нет"};
        specifications.add(str);

        if (lga1700 == true) str = new String[]{"LGA 1700", "да"};
        else str = new String[]{"LGA 1700", "нет"};
        specifications.add(str);

        str = new String[]{"Конструкция", ""};
        specifications.add(str);

        if (heatPipes != null)
            str = new String[]{"Тепловые трубки", heatPipes};
        else str = new String[]{"Тепловые трубки", "нет"};
        specifications.add(str);

        if (heatPipes_material != null) {
            str = new String[]{"Материал тепловых трубок", heatPipes_material};
            specifications.add(str);
        }
        if (radiatorMaterial != null) {
            str = new String[]{"Материал радиатора", radiatorMaterial};
            specifications.add(str);
        }

        str = new String[]{"Прочее", ""};
        specifications.add(str);

        if (power != null) {
            str = new String[]{"Питание", power};
            specifications.add(str);
        }
        if (backlight != null)
            str = new String[]{"Подсветка", backlight };
        else str = new String[]{"Подсветка", "нет"};
        specifications.add(str);

        return specifications;
    }

    // ---------------------------------------------------------------------------------------------
    // дополнительные методы
    // ---------------------------------------------------------------------------------------------

    private String ListToString(ArrayList<Integer> list)
    {
        String value = null;
        if (list != null) {
            if (list.get(0) != list.get(1)) value = list.get(0) + " - " + list.get(1);
            else value = list.get(0) + "";
        }

        return value;
    }

    public static Cooler createFromSnapshot(DataSnapshot snap){
        Cooler cooler = new Cooler();

        cooler.setProduct_code(Integer.parseInt(snap.getKey()));
        cooler.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
        cooler.setProducer(snap.child("Producer").getValue().toString());
        cooler.setModel(snap.child("Model").getValue().toString());

        cooler.setRefLink(snap.child("Url").getValue().toString());
        cooler.setPictureLink(snap.child("Picture").getValue().toString());

        cooler.setBacklight(snap.child("Backlight").getValue().toString());
        cooler.setTdp(Integer.parseInt(snap.child("MaxTdp").getValue().toString()));
        cooler.setFanQuantity(Integer.parseInt(snap.child("FanQuantity").getValue().toString()));
        try {
            cooler.setFanSize(Integer.parseInt(snap.child("FanSize").getValue().toString()));
        }
        catch (NumberFormatException e){
            Double d = Double.parseDouble(snap.child("FanSize").getValue().toString());
            cooler.setFanSize(d.intValue());
        }
        cooler.setRotationSpeed(snap.child("FanSpeed").getValue().toString());
        try {
            cooler.setHeatPipes(snap.child("HeatPipes").getValue().toString());
        }catch (NullPointerException e){}
        try {
            cooler.setHeatPipes_material(snap.child("HeatPipesMaterial").getValue().toString());
        }catch (NullPointerException e){}

        cooler.setNoiseLevel(snap.child("Noise").getValue().toString());

        if (snap.child("Paste").getValue().toString().equals("есть"))cooler.setPaste(true);
        else cooler.setPaste(false);

        cooler.setPower(snap.child("Power").getValue().toString());
        try {
            cooler.setRadiatorMaterial(snap.child("RadiatorMaterial").getValue().toString());
        }catch (NullPointerException e){}

        DataSnapshot sockets = snap.child("Sockets");
        if (sockets.child("AM4").getValue().equals("да")) cooler.setAM4(true);
        else cooler.setAM4(false);

        if (sockets.child("LGA 1200").getValue().equals("да")) cooler.setLga1200(true);
        else cooler.setLga1200(false);

        if (sockets.child("LGA 1700").getValue().equals("да")) cooler.setLga1700(true);
        else cooler.setLga1700(false);

        return cooler;
    }


    public HashMap<String, String> toFirebase(){
        HashMap<String, String> map = new HashMap<>();
        return map;
    }
}
