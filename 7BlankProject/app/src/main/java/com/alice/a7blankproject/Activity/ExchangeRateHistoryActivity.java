package com.alice.a7blankproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExchangeRateHistoryActivity extends AppCompatActivity {
    public static final String CURRENCY_CODE = "CurrencyCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.alice.a7blankproject.R.layout.activity_exchange_rate_history);
    }
}
