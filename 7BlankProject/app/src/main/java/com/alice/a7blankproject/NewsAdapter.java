package com.alice.a7blankproject;

        import android.support.v7.widget.RecyclerView;
        import android.util.SparseBooleanArray;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.alice.a7blankproject.Model.News;
        import com.alice.a7blankproject.Util.TimeUtils;

        import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ListItemViewHolder> {

    private List<News> mItems;
    private SparseBooleanArray mSelectedItems;

    NewsAdapter(List<News> modelData) {
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
                inflate(R.layout.fragment_news_list_item, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        News model = mItems.get(position);
        viewHolder.date.setText(TimeUtils.formatDate(model.getDate(), TimeUtils.DATE_PATTERN_SHORT_DATETIME));
        viewHolder.title.setText(model.getTitle());
        viewHolder.itemView.setActivated(mSelectedItems.get(position, false));
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