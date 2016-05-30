package com.alice.a7blankproject;

        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
        import android.util.SparseBooleanArray;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.TextView;

        import com.alice.a7blankproject.Model.News;
        import com.alice.a7blankproject.Util.TimeUtils;

        import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ListItemViewHolder> {

    private List<News> mItems;
    private SparseBooleanArray mSelectedItems;
    private Context mContext;

    NewsAdapter(List<News> modelData, Context context) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        mItems = modelData;
        mSelectedItems = new SparseBooleanArray();
        mContext = context;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_news_list_item, viewGroup, false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder viewHolder, final int position) {
        News model = mItems.get(position);
        viewHolder.date.setText(TimeUtils.formatDate(model.getDate(), TimeUtils.DATE_PATTERN_SHORT_DATETIME));
        viewHolder.title.setText(model.getTitle());
        viewHolder.itemView.setActivated(mSelectedItems.get(position, false));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News model = mItems.get(position);
                Intent intent = new Intent(mContext, NewsDetailsActivity.class);
                intent.putExtra(NewsDetailsFragment.EXTRA_PARAM_URL, model.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView title;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            date  = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}