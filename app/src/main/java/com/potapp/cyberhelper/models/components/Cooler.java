package com.potapp.cyberhelper.models.components;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cooler extends Component{

    // основные характеристики
    private int tdp;                                                                                // рассеиваемая мощность
    private String coolingType;                                                                     // тип охлаждения
    private int fanQuantity;                                                                        // количество вентиляторов
    private int fanSize;                                                                            // размер вентилятора, мм
    // списки из двух элементов - нижней и верхней границы значений
    @Embedded
    private ArrayList<Integer> airFlow;                                                             // воздушный поток, CFM
    @Embedded
    private ArrayList<Integer> noiseLevel;                                                          // уровень шума, дБ
    @Embedded
    private ArrayList<Integer> rotationSpeed;                                                       // скорость вращения, об/мин
    @Embedded
    private ArrayList<String> supportSockets;                                                            // совместимость с сокетами

    // конструкция
    private String heatPipes;                                                                       // тепловые трубки
    private String heatPipes_material;                                                              // материал тепловых трубок
    private String radiatorMaterial;                                                                // материал радиатора

    // прочее
    private int power;                                                                              // питание
    private String backlight;                                                                       // цвет подсветки (если есть)

    // ---------------------------------------------------------------------------------------------
    // get-методы
    // ---------------------------------------------------------------------------------------------


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

    public ArrayList<Integer> getAirFlow() {
        return airFlow;
    }

    public ArrayList<Integer> getNoiseLevel() {
        return noiseLevel;
    }

    public ArrayList<Integer> getRotationSpeed() {
        return rotationSpeed;
    }

    public ArrayList<String> getSupportSockets() {
        return supportSockets;
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

    public int getPower() {
        return power;
    }

    public String getBacklight() {
        return backlight;
    }

    // ---------------------------------------------------------------------------------------------
    // set-методы
    // ---------------------------------------------------------------------------------------------


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

    public void setAirFlow(ArrayList<Integer> airFlow) {
        this.airFlow = airFlow;
    }

    public void setNoiseLevel(ArrayList<Integer> noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public void setRotationSpeed(ArrayList<Integer> rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setSupportSockets(ArrayList<String> supportSockets) {
        this.supportSockets = supportSockets;
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

    public void setPower(int power) {
        this.power = power;
    }

    public void setBacklight(String backlight) {
        this.backlight = backlight;
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
        spec.setSpecValue(ListToString(noiseLevel) + " дБ");

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Скорость вращения");

        spec.setSpecValue(ListToString(rotationSpeed) + " об/мин");

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
            str = new String[]{"Воздушный поток", ListToString(airFlow) + " cfm"};
            specifications.add(str);
        }
        if (noiseLevel != null) {
            str = new String[]{"Уровень шума", ListToString(noiseLevel) + " дБ"};
            specifications.add(str);
        }
        if (rotationSpeed != null) {
            str = new String[]{"Скорость вращения", ListToString(rotationSpeed) + " об/мин"};
            specifications.add(str);
        }

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

        if (power != 0) {
            str = new String[]{"Питание", power + " pin"};
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
}
