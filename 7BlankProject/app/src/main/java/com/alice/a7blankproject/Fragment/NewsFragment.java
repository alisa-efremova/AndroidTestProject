package com.alice.a7blankproject.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alice.a7blankproject.adapter.NewsAdapter;
import com.alice.a7blankproject.model.CbrDataManager;
import com.alice.a7blankproject.model.News;
import com.alice.a7blankproject.util.TimeUtils;

import java.util.Date;
import java.util.List;

public class NewsFragment extends Fragment {

    private View mView;

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(com.alice.a7blankproject.R.layout.fragment_news_list, container, false);
        new LoadNewsTask().execute();
        return mView;
    }

    class LoadNewsTask extends AsyncTask<String, Void, List<News>> {
        @Override
        protected List<News> doInBackground(String... path) {
            Date startDate = TimeUtils.resetTimePartOfDate(new Date());
            Date endDate = TimeUtils.addToDate(startDate, 1);
            return CbrDataManager.getNews(startDate, endDate);
        }

        @Override
        protected void onPostExecute(List<News> news) {
            Toast.makeText(getActivity(), "Новости обновлены", Toast.LENGTH_SHORT).show();

            if (mView instanceof RecyclerView) {
                RecyclerView recyclerView = (RecyclerView) mView;
                recyclerView.setAdapter(new NewsAdapter(news, getActivity()));
            }
        }
    }
}
