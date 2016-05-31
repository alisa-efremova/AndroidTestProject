package com.alice.a7blankproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alice.a7blankproject.model.CurrencyInfo;

public class CurrencyInfoAdapter extends ArrayAdapter<CurrencyInfo> {

    private LayoutInflater mInflater;
    private CurrencyInfo[] mCurrencies;

    public CurrencyInfoAdapter(CurrencyInfo[] list, Context context) {
        super(context, com.alice.a7blankproject.R.layout.list_item_current_exchange_rate, list);
        mInflater = LayoutInflater.from(context);
        mCurrencies = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View itemView = convertView;
        if (itemView == null) {
            itemView = mInflater.inflate(com.alice.a7blankproject.R.layout.list_item_current_exchange_rate, parent, false);
            holder = new ViewHolder();
            holder.nameView         = (TextView) itemView.findViewById(com.alice.a7blankproject.R.id.currency_name);
            holder.exchangeRateView = (TextView) itemView.findViewById(com.alice.a7blankproject.R.id.exchange_rate);
            itemView.setTag(holder);
        }
        else {
            holder = (ViewHolder) itemView.getTag();
        }

        CurrencyInfo currencyInfo = mCurrencies[position];

        holder.nameView.setText(currencyInfo.getName());
        holder.exchangeRateView.setText(Double.toString(currencyInfo.getExchangeRate()));

        return itemView;
    }

    class ViewHolder {
        public TextView nameView;
        public TextView exchangeRateView;
    }
}