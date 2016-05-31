package com.alice.a7blankproject.model;

import com.alice.a7blankproject.util.TimeUtils;

import java.util.Date;

public class ExchangeRateByDate implements Comparable {
    private Date mDate;
    private double mExchangeRate;

    public ExchangeRateByDate(Date date, double exchangeRate) {
        mDate         = date;
        mExchangeRate = exchangeRate;
    }

    public Date getDate() {
        return mDate;
    }

    public double getExchangeRate() {
        return mExchangeRate;
    }

    public int compareTo(Object compareToObject)
    {
        return ((ExchangeRateByDate) compareToObject).getDate().compareTo(mDate);
    }

    @Override
    public String toString() {
        return TimeUtils.formatDate(getDate(), TimeUtils.DATE_PATTERN_SHORT_DATE) + " " + getExchangeRate();
    }
}
