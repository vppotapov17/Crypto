package com.potapp.cyberhelper.models.componentFilters;

import java.util.List;

public class MbFilter {

    private int priceFrom;
    private int priceTo;

    private String socket;
    private List<String> chipsets;
    private List<String> formFactors;
    private List<String> ozuTypes;
    private List<Integer> ozuSlotsQuantities;
    private List<Integer> M2Quantities;

    public int getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }

    public int getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public List<String> getChipsets() {
        return chipsets;
    }

    public void setChipsets(List<String> chipsets) {
        this.chipsets = chipsets;
    }

    public List<String> getFormFactors() {
        return formFactors;
    }

    public void setFormFactors(List<String> formFactors) {
        this.formFactors = formFactors;
    }

    public List<String> getOzuTypes() {
        return ozuTypes;
    }

    public void setOzuTypes(List<String> ozuTypes) {
        this.ozuTypes = ozuTypes;
    }

    public List<Integer> getOzuSlotsQuantities() {
        return ozuSlotsQuantities;
    }

    public void setOzuSlotsQuantities(List<Integer> ozuSlotsQuantities) {
        this.ozuSlotsQuantities = ozuSlotsQuantities;
    }

    public List<Integer> getM2Quantities() {
        return M2Quantities;
    }

    public void setM2Quantities(List<Integer> m2Quantities) {
        M2Quantities = m2Quantities;
    }
}
