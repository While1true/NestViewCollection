package com.nestrefreshlib.RefreshViews.AdapterHelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nestrefreshlib.R;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class RefreshHeaderAndFooterAdapterWrap extends BaseHeaderAndFooterAdapterWrap {
    public RefreshHeaderAndFooterAdapterWrap(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    public RefreshHeaderAndFooterAdapterWrap addHeaderLayout(int layoutid, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        headers.add(inflate);
        return this;
    }

    public RefreshHeaderAndFooterAdapterWrap addfooterLayout(int layoutid, ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        footers.add(inflate);
        return this;
    }

    public RefreshHeaderAndFooterAdapterWrap attachView(ViewGroup parent){
        addHeaderLayout(R.layout.header_layout,parent);
        addfooterLayout(R.layout.footer_layout,parent);
        return this;
    }

    public View getHeader(){
        return headers.get(0);
    }
    public View getFooter(){
        return footers.get(0);
    }
}
