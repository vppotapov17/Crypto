package com.potapp.cyberhelper.models.components;

import androidx.room.Entity;

import com.potapp.cyberhelper.models.mainSpec;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Case extends Component{

    // основные характеристики

    private String formFactor;                                                                      // форм-фактор
    private String bpPosition;                                                                      // расположение БП

    // разъёмы и отсеки

    private int compQuantity35;                                                                     // количество отсеков 3.5
    private int compQuantity25;                                                                     // количество отсеков 2.5
    private int slotsQuantity;                                                                      // количество слотов расширения
    private int usb20_connQuantity;                                                                 // количество разъёмов USB 2.0 на передней панели
    private int usb30_connQuantity;                                                                 // количество разъёмов USB 3.0 на передней панели

    // возможность установки вентиляторов

    private String fanOpp_Front;                                                                    // на передней панели
    private String fanOpp_Back;                                                                     // на задней панели
    private String fanOpp_Bottom;                                                                   // на нижней панели
    private String fanOpp_Top;                                                                      // на верхней панели

    // вентиляторы в комплекте

    private String fanFront;                                                                        // вентиляторы на передней панели
    private String fanBack;                                                                         // вентиляторы на задней панели
    private String fanBottom;                                                                       // вентиляторы на нижней панели
    private String fanTop;                                                                          // вентиляторы на верхней панели

    // размеры, вес

    private int gpu_maxLength;                                                                      // максимальная длина видеокарты
    private int bp_maxLength;                                                                       // максимальная длина БП
    private int cooler_maxHigh;                                                                     // максимальная высота кулера
    private int weight;                                                                             // вес

    // прочее

    private boolean SVO;                                                                            // возможность установки СВО


    //----------------------------------------------------------------------------------------------
    // set-методы
    //----------------------------------------------------------------------------------------------

    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    public void setBpPosition(String bpPosition) {
        this.bpPosition = bpPosition;
    }

    public void setCompQuantity35(int compQuantity35) {
        this.compQuantity35 = compQuantity35;
    }

    public void setCompQuantity25(int compQuantity25) {
        this.compQuantity25 = compQuantity25;
    }

    public void setSlotsQuantity(int slotsQuantity) {
        this.slotsQuantity = slotsQuantity;
    }

    public void setUsb20_connQuantity(int usb20_connQuantity) {
        this.usb20_connQuantity = usb20_connQuantity;
    }

    public void setUsb30_connQuantity(int usb30_connQuantity) {
        this.usb30_connQuantity = usb30_connQuantity;
    }

    public void setFanOpp_Front(String fanOpp_Front) {
        this.fanOpp_Front = fanOpp_Front;
    }

    public void setFanOpp_Back(String fanOpp_Back) {
        this.fanOpp_Back = fanOpp_Back;
    }

    public void setFanOpp_Bottom(String fanOpp_Bottom) {
        this.fanOpp_Bottom = fanOpp_Bottom;
    }

    public void setFanOpp_Top(String fanOpp_Top) {
        this.fanOpp_Top = fanOpp_Top;
    }

    public void setFanFront(String fanFront) {
        this.fanFront = fanFront;
    }

    public void setFanBack(String fanBack) {
        this.fanBack = fanBack;
    }

    public void setFanBottom(String fanBottom) {
        this.fanBottom = fanBottom;
    }

    public void setFanTop(String fanTop) {
        this.fanTop = fanTop;
    }

    public void setGpu_maxLength(int gpu_maxLength) {
        this.gpu_maxLength = gpu_maxLength;
    }

    public void setBp_maxLength(int bp_maxLength) {
        this.bp_maxLength = bp_maxLength;
    }

    public void setCooler_maxHigh(int cooler_maxHigh) {
        this.cooler_maxHigh = cooler_maxHigh;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setSVO(boolean SVO) {
        this.SVO = SVO;
    }

    //----------------------------------------------------------------------------------------------
    // get-методы
    //----------------------------------------------------------------------------------------------


    public String getFormFactor() {
        return formFactor;
    }

    public String getBpPosition() {
        return bpPosition;
    }

    public int getCompQuantity35() {
        return compQuantity35;
    }

    public int getCompQuantity25() {
        return compQuantity25;
    }

    public int getSlotsQuantity() {
        return slotsQuantity;
    }

    public int getUsb20_connQuantity() {
        return usb20_connQuantity;
    }

    public int getUsb30_connQuantity() {
        return usb30_connQuantity;
    }

    public String getFanOpp_Front() {
        return fanOpp_Front;
    }

    public String getFanOpp_Back() {
        return fanOpp_Back;
    }

    public String getFanOpp_Bottom() {
        return fanOpp_Bottom;
    }

    public String getFanOpp_Top() {
        return fanOpp_Top;
    }

    public String getFanFront() {
        return fanFront;
    }

    public String getFanBack() {
        return fanBack;
    }

    public String getFanBottom() {
        return fanBottom;
    }

    public String getFanTop() {
        return fanTop;
    }

    public int getGpu_maxLength() {
        return gpu_maxLength;
    }

    public int getBp_maxLength() {
        return bp_maxLength;
    }

    public int getCooler_maxHigh() {
        return cooler_maxHigh;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isSVO() {
        return SVO;
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
        if (weight != 0)
        {
            str = new String[]{"Вес", weight + ""};
            specifications.add(str);
        }

        str = new String[]{"Разъёмы и отсеки", ""};
        specifications.add(str);
        if (compQuantity35 != 0)
        {
            str = new String[]{"Отсеков 3.5″", compQuantity35 + ""};
            specifications.add(str);
        }
        if (compQuantity25 != 0)
        {
            str = new String[]{"Отсеков 2.5″", compQuantity25 + ""};
            specifications.add(str);
        }
        if (slotsQuantity != 0)
        {
            str = new String[]{"Слотов расширения", slotsQuantity + ""};
            specifications.add(str);
        }
        if (usb20_connQuantity != 0)
        {
            str = new String[]{"Разъёмов USB 2.0", usb20_connQuantity + ""};
            specifications.add(str);
        }
        if (usb30_connQuantity != 0)
        {
            str = new String[]{"Разъёмов USB 3.0", usb30_connQuantity + ""};
            specifications.add(str);
        }

        str = new String[]{"Установка вентиляторов", ""};
        specifications.add(str);
        if (fanOpp_Front != null)
        {
            str = new String[]{"Передняя панель", fanOpp_Front + ""};
            specifications.add(str);
        }
        if (fanOpp_Back != null)
        {
            str = new String[]{"Задняя панель", fanOpp_Back + ""};
            specifications.add(str);
        }
        if (fanOpp_Bottom != null)
        {
            str = new String[]{"Нижняя панель", fanOpp_Bottom + ""};
            specifications.add(str);
        }
        if (fanOpp_Top != null)
        {
            str = new String[]{"Верхняя панель", fanOpp_Top + ""};
            specifications.add(str);
        }

        str = new String[]{"Вентиляторы в комплекте", ""};
        specifications.add(str);
        if (fanFront != null)
        {
            str = new String[]{"Передняя панель", fanFront + ""};
            specifications.add(str);
        }
        if (fanBack != null)
        {
            str = new String[]{"Задняя панель", fanBack + ""};
            specifications.add(str);
        }
        if (fanOpp_Bottom != null)
        {
            str = new String[]{"Нижняя панель", fanOpp_Bottom + ""};
            specifications.add(str);
        }
        if (fanOpp_Top != null)
        {
            str = new String[]{"Верхняя панель", fanOpp_Top + ""};
            specifications.add(str);
        }

        str = new String[]{"Прочее", ""};
        specifications.add(str);

        if (gpu_maxLength != 0)
        {
            str = new String[]{"Максимальная длина видеокарты", gpu_maxLength + " мм"};
            specifications.add(str);
        }
        if (bp_maxLength != 0)
        {
            str = new String[]{"Максимальная длина БП", bp_maxLength + " мм"};
            specifications.add(str);
        }
        if (cooler_maxHigh != 0)
        {
            str = new String[]{"Максимальная высота кулера", cooler_maxHigh + " мм"};
            specifications.add(str);
        }
        if (SVO)
            str = new String[]{"Возможность установки СВО", "есть"};
        else str = new String[]{"Возможность установки СВО", "нет"};
        specifications.add(str);

        return specifications;

    }
}
