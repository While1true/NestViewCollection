package com.nestrefreshlib.RefreshViews.AdapterHelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.AdapterHelper.Base.BaseAdapterWrapper;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class DefaultRefreshHeaderAndFooterWrap extends BaseAdapterWrapper {
    public DefaultRefreshHeaderAndFooterWrap(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    public DefaultRefreshHeaderAndFooterWrap addHeaderView(int layoutid, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        addHeaderView(inflate);
        return this;
    }

    public DefaultRefreshHeaderAndFooterWrap addFooterView(int layoutid, ViewGroup parent) {
        View footers = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        addFooterView(footers);
        return this;
    }

    public DefaultRefreshHeaderAndFooterWrap attachDefaultHeaderFooterView(ViewGroup parent) {
        addHeaderView(R.layout.header_layout, parent);
        addFooterView(R.layout.footer_layout, parent);
        return this;
    }
}

