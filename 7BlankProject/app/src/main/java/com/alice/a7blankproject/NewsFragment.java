package com.alice.a7blankproject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alice.a7blankproject.Model.CbrDataManager;
import com.alice.a7blankproject.Model.News;
import com.alice.a7blankproject.Util.TimeUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NewsFragment extends Fragment {

    private View mView;
    private SharedPreferences mPreferences;

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_news_list, container, false);
        new LoadNewsTask().execute();
        return mView;
    }

    class LoadNewsTask extends AsyncTask<String, Void, List<News>> {
        @Override
        protected List<News> doInBackground(String... path) {
            int period = 7;//Integer.valueOf(mPreferences.getString("period", "7"));
            List<News> news = CbrDataManager.getNews(TimeUtils.addToDate(new Date(), -period), new Date());
            return news;
        }

        @Override
        protected void onPostExecute(List<News> news) {
            Toast.makeText(getActivity(), "Данные обновлены (период)", Toast.LENGTH_SHORT).show();

            if (mView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) mView;
                recyclerView.setAdapter(new NewsAdapter(news, getActivity()));
            }
        }
    }
}
