package com.nestrefreshlib.RefreshViews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.AdapterHelper.Base.BaseAdapterWrapper;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class DefaultNomoreHeaderAndFooterWrap extends BaseAdapterWrapper {
    private boolean isnomore=false;

    public DefaultNomoreHeaderAndFooterWrap(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    public DefaultNomoreHeaderAndFooterWrap addHeaderView(int layoutid, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        addHeaderView(inflate);
        return this;
    }

    public DefaultNomoreHeaderAndFooterWrap addFooterView(int layoutid, ViewGroup parent) {
        View footers = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        addFooterView(footers);
        return this;
    }

    public DefaultNomoreHeaderAndFooterWrap attachDefaultHeaderFooterView(ViewGroup parent) {
        addFooterView(R.layout.footer_layout, parent);
        showNoMore("没有更多了");
        return this;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()-(isnomore?0:1);
    }

    public boolean isIsnomore() {
        return isnomore;
    }

    public void setIsnomore(boolean isnomore) {
        this.isnomore = isnomore;
    }

    public void showNoMore(String text) {
        TextView textView = getFooter().findViewById(R.id.textView);
        textView.setText(text);
    }
}

