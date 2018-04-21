package com.nestrefreshlib.RefreshViews.AdapterHelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nestrefreshlib.R;
import com.nestrefreshlib.swipe.SwipeAdapterWrapper;

/**
 * Created by ck on 2018/4/21.
 */

public class DefaultRefreshWrapAdapter extends SwipeAdapterWrapper {
    public DefaultRefreshWrapAdapter(Context context, RecyclerView.Adapter adapter) {
        super(context, adapter);
    }

    public DefaultRefreshWrapAdapter addHeaderView(int layoutid, ViewGroup parent) {
        View inflate =mInflater.inflate(layoutid, parent, false);
        addHeaderView(inflate);
        return this;
    }

    public DefaultRefreshWrapAdapter addFooterView(int layoutid, ViewGroup parent) {
        View footers = mInflater.inflate(layoutid, parent, false);
        addFooterView(footers);
        return this;
    }

    public DefaultRefreshWrapAdapter attachDefaultHeaderFooterView(ViewGroup viewGroup) {
        addHeaderView(R.layout.header_layout, viewGroup);
        addFooterView(R.layout.footer_layout, viewGroup);
        return this;
    }
}
