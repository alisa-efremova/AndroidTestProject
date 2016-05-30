package com.alice.a7blankproject.Model;

import com.alice.a7blankproject.Util.TimeUtils;

import java.util.Date;

public class News {
    private Date mDate;
    private String mTitle;
    private String mUrl;

    public News(Date date, String title, String url) {
        mDate = date;
        mTitle = title.trim();
        mUrl = url.trim();
    }

    public Date getDate() {
        return mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return CbrDataManager.NEWS_URL + mUrl;
    }

    @Override
    public String toString() {
        return TimeUtils.formatDate(getDate(), TimeUtils.DATE_PATTERN_SHORT_DATE) + " "
                + getTitle() + " ("
                + getUrl() + ")";
    }
}
