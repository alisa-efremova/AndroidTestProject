package com.alice.a7blankproject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alice.a7blankproject.Model.CbrDataManager;
import com.alice.a7blankproject.Model.ExchangeRateByDate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExchangeRateFragment extends Fragment {

    private String mCurrencyCode = "USD";
    private View mView;
    SharedPreferences mPreferences;

    public ExchangeRateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mCurrencyCode = extras.getString(CurrentExchangeRatesFragment.CURRENCY_CODE);
        }

        //todo: move to activity?
        String title = getResources().getString(R.string.exchange_rate_history_title, mCurrencyCode);
        TextView textView = (TextView) getActivity().findViewById(R.id.historyTitle);
        textView.setText(title);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_exchange_rate_list, container, false);
        new LoadCurrentExchangeRatesTask().execute();
        return mView;
    }

    class LoadCurrentExchangeRatesTask extends AsyncTask<String, Void, List<ExchangeRateByDate>> {
        @Override
        protected List<ExchangeRateByDate> doInBackground(String... path) {
            List<String> currencies = Arrays.asList(getResources().getStringArray(R.array.currencies));
            CbrDataManager cbr = new CbrDataManager(currencies);
            int period = Integer.valueOf(mPreferences.getString("period", "7"));
            return cbr.getExchangeRatesByDateInterval(mCurrencyCode, new Date(), period);
        }

        @Override
        protected void onPostExecute(List<ExchangeRateByDate> exchangeRateInfo) {
            Toast.makeText(getActivity(), "Данные обновлены (период)", Toast.LENGTH_SHORT).show();
            Collections.reverse(exchangeRateInfo);

            if (mView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) mView;
                recyclerView.setAdapter(new RecyclerViewAdapter(exchangeRateInfo));
            }
        }
    }
}
