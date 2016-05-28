package com.alice.a7blankproject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ExchangeRateFragment extends Fragment {

    private int mColumnCount = 2;
    private String mCurrencyCode = "USD";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exchange_rate_list, container, false);

        ArrayList<Model> demoData = new ArrayList<>();
        char c = 'A';
        for (byte i = 0; i < 20; i++) {
            Model model = new Model();
            model.name = c++;
            model.age = (byte) (20 + i);
            demoData.add(model);
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            recyclerView.setAdapter(new RecyclerViewAdapter(demoData));
        }
        return view;
    }
}
