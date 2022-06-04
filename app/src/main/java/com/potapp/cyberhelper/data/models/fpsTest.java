package com.potapp.cyberhelper.data.models;

// класс для хранения результатов FPS в играх
public class fpsTest {

    String gameName;
    private int valueLow;
    private int valueMedium;
    private int valueHigh;
    private int valueUltra;


    public fpsTest(String gameName, int valueLow, int valueMedium, int valueHigh, int valueUltra)
    {
        this.gameName = gameName;
        this.valueLow = valueLow;
        this.valueMedium = valueMedium;
        this.valueHigh = valueHigh;
        this.valueUltra = valueUltra;
    }

    //----------------------------------------------------------------------------------------------
    // get-методы
    //----------------------------------------------------------------------------------------------

    public String getGameName() {
        return gameName;
    }

    public int getValueLow() {
        return valueLow;
    }

    public int getValueMedium() {
        return valueMedium;
    }

    public int getValueHigh() {
        return valueHigh;
    }

    public int getValueUltra() {
        return valueUltra;
    }

    //----------------------------------------------------------------------------------------------
    // set-методы
    //----------------------------------------------------------------------------------------------

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setValueLow(int valueLow) {
        this.valueLow = valueLow;
    }

    public void setValueMedium(int valueMedium) {
        this.valueMedium = valueMedium;
    }

    public void setValueHigh(int valueHigh) {
        this.valueHigh = valueHigh;
    }

    public void setValueUltra(int valueUltra) {
        this.valueUltra = valueUltra;
    }
}
