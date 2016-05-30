package com.alice.a7blankproject.Model;

public class CurrencyInfo {
    private String code;
    private String name;
    private double exchangeRate;

    public CurrencyInfo(String code, String name, double exchangeRate) {
        this.code = code.trim();
        this.name = name.trim();
        this.exchangeRate = exchangeRate;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public double getExchangeRate() {
        return this.exchangeRate;
    }

    @Override
    public String toString() {
        return getCode() + " ("
                + getName() + "): "
                + getExchangeRate();
    }
}
