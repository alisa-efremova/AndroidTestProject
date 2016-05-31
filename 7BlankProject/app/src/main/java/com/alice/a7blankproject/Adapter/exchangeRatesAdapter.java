package com.alice.a7blankproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alice.a7blankproject.model.ExchangeRateByDate;
import com.alice.a7blankproject.util.TimeUtils;

import java.util.List;

public class ExchangeRatesAdapter extends RecyclerView.Adapter<ExchangeRatesAdapter.ListItemViewHolder> {

    private List<ExchangeRateByDate> mItems;

    public ExchangeRatesAdapter(List<ExchangeRateByDate> exchangeRatesList) {
        if (exchangeRatesList == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        mItems = exchangeRatesList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(com.alice.a7blankproject.R.layout.list_item_exchange_rate, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        ExchangeRateByDate model = mItems.get(position);
        viewHolder.date.setText(TimeUtils.formatDate(model.getDate(), TimeUtils.DATE_PATTERN_SHORT_DATE));
        viewHolder.exchangeRate.setText(Double.toString(model.getExchangeRate()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView exchangeRate;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            date         = (TextView) itemView.findViewById(com.alice.a7blankproject.R.id.date);
            exchangeRate = (TextView) itemView.findViewById(com.alice.a7blankproject.R.id.exchange_rate);
        }
    }
}