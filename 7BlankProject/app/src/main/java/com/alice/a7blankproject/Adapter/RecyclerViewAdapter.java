package com.alice.a7blankproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alice.a7blankproject.model.ExchangeRateByDate;
import com.alice.a7blankproject.util.TimeUtils;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ListItemViewHolder> {

    private List<ExchangeRateByDate> mItems;
    private SparseBooleanArray mSelectedItems;

    public RecyclerViewAdapter(List<ExchangeRateByDate> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        mItems = modelData;
        mSelectedItems = new SparseBooleanArray();
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(com.alice.a7blankproject.R.layout.fragment_exchange_rate_list_item, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        ExchangeRateByDate model = mItems.get(position);
        viewHolder.date.setText(TimeUtils.formatDate(model.getDate(), TimeUtils.DATE_PATTERN_SHORT_DATE));
        viewHolder.exchangeRate.setText(Double.toString(model.getExchangeRate()));
        viewHolder.itemView.setActivated(mSelectedItems.get(position, false));
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