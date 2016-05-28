package com.alice.a7blankproject;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;
import java.util.Set;

public class CurrentExchangeRatesFragment extends ListFragment {
    public static final String CURRENCY_CODE = "CurrencyCode";

    SharedPreferences mPreferences;
    CurrencyInfo[] mCurrencies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CurrencyInfo currencyInfo = mCurrencies[position];
        Intent intent = new Intent(getActivity(), ExchangeRateHistoryActivity.class);
        intent.putExtra(CURRENCY_CODE, currencyInfo.getCode());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadCurrentExchangeRatesTask().execute();
    }

    class LoadCurrentExchangeRatesTask extends AsyncTask<String, Void, CurrencyInfo[]> {
        @Override
        protected CurrencyInfo[] doInBackground(String... path) {
            Set<String> preferredCurrencies = mPreferences.getStringSet("currencies", null);
            return CbrDataManager.getExchangeRatesByDate(new Date(), preferredCurrencies);
        }

        @Override
        protected void onPostExecute(CurrencyInfo[] currencyInfoArray) {
            Toast.makeText(getActivity(), "Данные обновлены", Toast.LENGTH_SHORT).show();
            mCurrencies = currencyInfoArray;
            setListAdapter(new CurrencyInfoAdapter(currencyInfoArray, getActivity()));
        }
    }
}