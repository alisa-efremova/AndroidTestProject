package com.alice.a7blankproject;

public class CurrencyInfo {
    private String code;
    private String name;
    private double exchangeRate;

    public CurrencyInfo(String code, String name, double exchangeRate) {
        this.code = code;
        this.name = name;
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
}
