package com.nestrefreshlib.RefreshViews.AdapterHelper;

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

public class DefaultNomoreFooterWrap extends BaseAdapterWrapper {
    public DefaultNomoreFooterWrap(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    public DefaultNomoreFooterWrap addHeaderView(int layoutid, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        addHeaderView(inflate);
        return this;
    }

    public DefaultNomoreFooterWrap addFooterView(int layoutid, ViewGroup parent) {
        View footers = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        addFooterView(footers);
        return this;
    }

    public DefaultNomoreFooterWrap attachDefaultHeaderFooterView(ViewGroup parent) {
        addFooterView(R.layout.footer_layout, parent);
        return this;
    }

    boolean nomore = false;

    @Override
    public int getItemCount() {
        return super.getItemCount()-(nomore?0:1);
    }

    public void setNomore(boolean nomore, CharSequence charSequence) {
        this.nomore = nomore;
        if (charSequence != null) {
            TextView foottext = getFooter().findViewById(R.id.textView);
            foottext.setText(charSequence);
        }
    }
}

