package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;

import com.google.firebase.database.DataSnapshot;
import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class Case extends Component{

    // основные характеристики

    private String formFactor;                                                                      // форм-фактор
    private String bpPosition;                                                                      // расположение БП

    // разъёмы и отсеки

    private int sections525;                                                                        // количество отсеков 5.25
    private int sections35;                                                                         // количество отсеков 3.5
    private int sections25;                                                                         // количество отсеков 2.5
    private int slotsQuantity;                                                                      // количество слотов расширения
    private int usb20;                                                                              // количество разъёмов USB 2.0 на передней панели
    private int usb30;                                                                              // количество разъёмов USB 3.0 на передней панели
    private String audio;                                                                           // аудиоразъемы

    // вентиляторы в комплекте

    private String fanFront;                                                                        // вентиляторы на передней панели
    private String fanBack;                                                                         // вентиляторы на задней панели
    private String fanOpp;                                                                          // возможность установки вентиляторов

    // размеры, вес

    private int gpu_maxLength;                                                                      // максимальная длина видеокарты
    private String weight;                                                                          // вес
    private String sizes;                                                                           // размеры
    private String material;                                                                        // материал
    private String walls;                                                                           // толщина стенок


    // прочее

    private boolean SVO;                                                                            // возможность установки СВО
    private String color;                                                                           // цвет
    private boolean transpSidePanel;                                                                // прозрачная боковая панель


    //----------------------------------------------------------------------------------------------
    // set-методы
    //----------------------------------------------------------------------------------------------


    public void setSections525(int sections525) {
        this.sections525 = sections525;
    }

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setBpPosition(String bpPosition) {
        this.bpPosition = bpPosition;
    }

    public void setSections35(int sections35) {
        this.sections35 = sections35;
    }

    public void setSections25(int sections25) {
        this.sections25 = sections25;
    }

    public void setSlotsQuantity(int slotsQuantity) {
        this.slotsQuantity = slotsQuantity;
    }

    public void setUsb20(int usb20) {
        this.usb20 = usb20;
    }

    public void setUsb30(int usb30) {
        this.usb30 = usb30;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void setFanFront(String fanFront) {
        this.fanFront = fanFront;
    }

    public void setFanBack(String fanBack) {
        this.fanBack = fanBack;
    }

    public void setFanOpp(String fanOpp) {
        this.fanOpp = fanOpp;
    }

    public void setGpu_maxLength(int gpu_maxLength) {
        this.gpu_maxLength = gpu_maxLength;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setSizes(String sizes) {
        this.sizes = sizes;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setWalls(String walls) {
        this.walls = walls;
    }

    public void setSVO(boolean SVO) {
        this.SVO = SVO;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTranspSidePanel(boolean transpSidePanel) {
        this.transpSidePanel = transpSidePanel;
    }


    //----------------------------------------------------------------------------------------------
    // get-методы
    //----------------------------------------------------------------------------------------------


    public int getSections525() {
        return sections525;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public String getBpPosition() {
        return bpPosition;
    }

    public int getSections35() {
        return sections35;
    }

    public int getSections25() {
        return sections25;
    }

    public int getSlotsQuantity() {
        return slotsQuantity;
    }

    public int getUsb20() {
        return usb20;
    }

    public int getUsb30() {
        return usb30;
    }

    public String getAudio() {
        return audio;
    }

    public String getFanFront() {
        return fanFront;
    }

    public String getFanBack() {
        return fanBack;
    }

    public String getFanOpp() {
        return fanOpp;
    }

    public int getGpu_maxLength() {
        return gpu_maxLength;
    }

    public String getWeight() {
        return weight;
    }

    public String getSizes() {
        return sizes;
    }

    public String getMaterial() {
        return material;
    }

    public String getWalls() {
        return walls;
    }

    public boolean isSVO() {
        return SVO;
    }

    public String getColor() {
        return color;
    }

    public boolean isTranspSidePanel() {
        return transpSidePanel;
    }


    //----------------------------------------------------------------------------------------------
    // переопределённые методы класса Component
    //----------------------------------------------------------------------------------------------

    @Override
    public mainSpec getMainSpec1()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Форм-фактор");
        spec.setSpecValue(formFactor);

        return spec;
    }

    @Override
    public mainSpec getMainSpec2()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Расположение БП");
        spec.setSpecValue(bpPosition);

        return spec;
    }

    @Override
    public mainSpec getMainSpec3()
    {
        mainSpec spec = new mainSpec();

        spec.setSpecTitle("Вес");
        spec.setSpecValue(weight + " кг");

        return spec;
    }

    @Override
    public List<String[]> specifications()
    {
        List<String[]> specifications = new ArrayList<>();

        String str[];

        str = new String[]{"Основные харатеристики", ""};
        specifications.add(str);

        if (formFactor != null) {
            str = new String[]{"Форм-фактор", formFactor};
            specifications.add(str);
        }
        if (bpPosition != null)
        {
            str = new String[]{"Расположение БП", bpPosition};
            specifications.add(str);
        }
        if (material != null){
            str = new String[]{"Материал", material};
            specifications.add(str);
        }
        if (color != null){
            str = new String[]{"Цвет", color};
            specifications.add(str);
        }

        str = new String[]{"Разъёмы и отсеки", ""};
        specifications.add(str);
        if (sections525 != 0)
        {
            str = new String[]{"Отсеков 5.25″", sections525 + ""};
            specifications.add(str);
        }
        if (sections35 != 0)
        {
            str = new String[]{"Отсеков 3.5″", sections35 + ""};
            specifications.add(str);
        }
        if (sections25 != 0)
        {
            str = new String[]{"Отсеков 2.5″", sections25 + ""};
            specifications.add(str);
        }
        if (slotsQuantity != 0)
        {
            str = new String[]{"Слотов расширения", slotsQuantity + ""};
            specifications.add(str);
        }
        if (usb20 != 0)
        {
            str = new String[]{"Разъёмов USB 2.0", usb20 + ""};
            specifications.add(str);
        }
        if (usb30 != 0)
        {
            str = new String[]{"Разъёмов USB 3.0", usb30 + ""};
            specifications.add(str);
        }
        if (audio != null){
            str = new String[]{"Аудио-разъёмы", audio + ""};
            specifications.add(str);
        }

        str = new String[]{"Вентиляторы", ""};
        specifications.add(str);
        if (fanFront != null)
        {
            str = new String[]{"Передняя панель", fanFront};
            specifications.add(str);
        }
        if (fanBack != null)
        {
            str = new String[]{"Задняя панель", fanBack};
            specifications.add(str);
        }
        if (fanOpp != null){
            str = new String[]{"Возможность установки", fanOpp};
            specifications.add(str);
        }

        str = new String[]{"Размеры и вес", ""};
        specifications.add(str);

        if (gpu_maxLength != 0)
        {
            str = new String[]{"Максимальная длина видеокарты", gpu_maxLength + " мм"};
            specifications.add(str);
        }
        if (sizes != null)
        {
            str = new String[]{"Размеры", sizes};
            specifications.add(str);
        }
        if (walls != null)
        {
            str = new String[]{"Толщина стенок", walls};
            specifications.add(str);
        }
        if (weight != null)
        {
            str = new String[]{"Вес", weight};
            specifications.add(str);
        }

        str = new String[]{"Особенности", ""};
        specifications.add(str);

        if (gpu_maxLength != 0)
        {
            str = new String[]{"Максимальная длина видеокарты", gpu_maxLength + " мм"};
            specifications.add(str);
        }
        if (SVO)
            str = new String[]{"Возможность установки СВО", "есть"};
        else str = new String[]{"Возможность установки СВО", "нет"};
        specifications.add(str);

        if (transpSidePanel)
            str = new String[]{"Прозрачная боковая панель", "да"};
        else str = new String[]{"Прозрачная боковая панель", "нет"};
        specifications.add(str);


        return specifications;

    }

    public static Case createFromSnapshot(DataSnapshot snap){
        Case caseItem = new Case();

        caseItem.setProduct_code(Integer.parseInt(snap.getKey()));
        caseItem.setPrice(Integer.parseInt(snap.child("Price").getValue().toString()));
        caseItem.setProducer(snap.child("Producer").getValue().toString());
        caseItem.setModel(snap.child("Model").getValue().toString());

        caseItem.setRefLink(snap.child("Url").getValue().toString());
        caseItem.setPictureLink(snap.child("Picture").getValue().toString());

        if (snap.child("Size").exists()) caseItem.setFormFactor(snap.child("Size").getValue().toString());
        if (snap.child("BpLocation").exists()) caseItem.setBpPosition(snap.child("BpLocation").getValue().toString());
        if (snap.child("Sections525").exists()) caseItem.setSections525(Integer.parseInt(snap.child("Sections525").getValue().toString()));
        if (snap.child("Hdd35").exists()) caseItem.setSections35(Integer.parseInt(snap.child("Hdd35").getValue().toString()));
        if (snap.child("Sections25").exists()) caseItem.setSections25(Integer.parseInt(snap.child("Sections25").getValue().toString()));
        if (snap.child("SlotsQuantity").exists()) caseItem.setSlotsQuantity(Integer.parseInt(snap.child("SlotsQuantity").getValue().toString()));
        if (snap.child("Usb20").exists()) caseItem.setUsb20(Integer.parseInt(snap.child("Usb20").getValue().toString()));
        if (snap.child("Usb30").exists()) caseItem.setUsb30(Integer.parseInt(snap.child("Usb30").getValue().toString()));
        if (snap.child("Audio").exists()) caseItem.setAudio(snap.child("Audio").getValue().toString());

        if (snap.child("FanFront").exists())  caseItem.setFanFront(snap.child("FanFront").getValue().toString());
        if (snap.child("FanBack").exists()) caseItem.setFanBack(snap.child("FanBack").getValue().toString());
        if (snap.child("FanOpp").exists()) caseItem.setFanOpp(snap.child("FanOpp").getValue().toString());

        if (snap.child("GpuLength").exists()) caseItem.setGpu_maxLength(Integer.parseInt(snap.child("GpuLength").getValue().toString()));
        if (snap.child("Weight").exists()) caseItem.setWeight(snap.child("Weight").getValue().toString());
        if (snap.child("Sizes").exists()) caseItem.setSizes(snap.child("Sizes").getValue().toString());
        if (snap.child("Material").exists()) caseItem.setMaterial(snap.child("Material").getValue().toString());
        if (snap.child("Walls").exists()) caseItem.setWalls(snap.child("Walls").getValue().toString());

        if (snap.child("SVO").exists()){
            if (snap.child("SVO").getValue().toString().equals("есть")) caseItem.setSVO(true);
            else caseItem.setSVO(false);
        }
        else {
            caseItem.setSVO(false);
        }

        if (snap.child("Color").exists()) caseItem.setColor(snap.child("Color").getValue().toString());
        if (snap.child("TranspSidePanel").exists()){
            if (snap.child("TranspSidePanel").getValue().toString().equals("есть")) caseItem.setTranspSidePanel(true);
            else caseItem.setTranspSidePanel(false);
        }
        else {
            caseItem.setTranspSidePanel(false);
        }

        return caseItem;
    }

    public HashMap<String, String> toFirebase(){
        HashMap<String, String> map = new HashMap<>();
        return map;
    }
}
