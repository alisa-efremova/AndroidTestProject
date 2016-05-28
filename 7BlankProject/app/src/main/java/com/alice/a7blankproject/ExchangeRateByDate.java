package com.alice.a7blankproject;

import java.util.Date;

public class ExchangeRateByDate implements Comparable {
    private Date mDate;
    private String mExchangeRate;

    public ExchangeRateByDate(Date date, String exchangeRate) {
        mDate         = date;
        mExchangeRate = exchangeRate;
    }

    public Date getDate() {
        return mDate;
    }

    public String getExchangeRate() {
        return mExchangeRate;
    }

    public int compareTo(Object compareToObject)
    {
        return ((ExchangeRateByDate) compareToObject).getDate().compareTo(mDate);
    }
}
