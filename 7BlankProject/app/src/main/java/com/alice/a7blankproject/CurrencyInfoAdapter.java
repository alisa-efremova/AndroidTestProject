package com.alice.a7blankproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alice.a7blankproject.Model.CurrencyInfo;

class CurrencyInfoAdapter extends ArrayAdapter<CurrencyInfo> {

    private LayoutInflater mInflater;
    private CurrencyInfo[] mCurrencies;

    CurrencyInfoAdapter(CurrencyInfo[] list, Context context) {
        super(context, R.layout.fragment_current_exchange_rates, list);
        mInflater = LayoutInflater.from(context);
        mCurrencies = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(R.layout.fragment_current_exchange_rates, parent, false);
            holder = new ViewHolder();
            holder.nameView = (TextView) row.findViewById(R.id.currency_name);
            holder.exchangeRateView = (TextView) row.findViewById(R.id.exchange_rate);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        CurrencyInfo currencyInfo = mCurrencies[position];

        holder.nameView.setText(currencyInfo.getName());
        holder.exchangeRateView.setText(Double.toString(currencyInfo.getExchangeRate()));

        return row;
    }

    class ViewHolder {
        public TextView nameView;
        public TextView exchangeRateView;
    }
}