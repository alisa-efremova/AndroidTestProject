package com.alice.a7blankproject.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.alice.a7blankproject.adapter.CurrencyInfoAdapter;
import com.alice.a7blankproject.activity.ExchangeRateDynamicsActivity;
import com.alice.a7blankproject.model.CbrDataManager;
import com.alice.a7blankproject.model.CurrencyInfo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CurrentExchangeRatesFragment extends ListFragment {

    SharedPreferences mPreferences;
    CurrencyInfo[] mCurrencies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        PreferenceManager.setDefaultValues(getActivity(), com.alice.a7blankproject.R.xml.preferences, false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CurrencyInfo currencyInfo = mCurrencies[position];
        Intent intent = new Intent(getActivity(), ExchangeRateDynamicsActivity.class);
        intent.putExtra(ExchangeRateDynamicsActivity.CURRENCY_CODE, currencyInfo.getCode());
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