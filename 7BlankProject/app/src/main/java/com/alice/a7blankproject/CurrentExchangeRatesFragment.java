package com.alice.a7blankproject;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.alice.a7blankproject.Model.CbrDataManager;
import com.alice.a7blankproject.Model.CurrencyInfo;
import com.alice.a7blankproject.Model.News;
import com.alice.a7blankproject.Util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
            Set<String> preferredCurrencies = mPreferences.getStringSet("currencies", new HashSet<String>());

            List<CurrencyInfo> currencyInfoList = CbrDataManager.getExchangeRatesByDate(new Date(), preferredCurrencies);
            CurrencyInfo[] currencyInfoArray = new CurrencyInfo[currencyInfoList.size()];
            currencyInfoList.toArray(currencyInfoArray);
            return currencyInfoArray;
        }

        @Override
        protected void onPostExecute(CurrencyInfo[] currencyInfoArray) {
            Toast.makeText(getActivity(), "Данные обновлены", Toast.LENGTH_SHORT).show();
            mCurrencies = currencyInfoArray;
            setListAdapter(new CurrencyInfoAdapter(currencyInfoArray, getActivity()));
        }
    }
}