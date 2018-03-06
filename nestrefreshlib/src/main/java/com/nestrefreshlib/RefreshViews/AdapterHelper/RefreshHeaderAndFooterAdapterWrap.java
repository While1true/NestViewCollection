package com.nestrefreshlib.RefreshViews.AdapterHelper;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.AdapterHelper.Base.BaseHeaderAndFooterAdapterWrap;

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
        attachView(R.layout.header_layout,R.layout.footer_layout,parent);
        return this;
    }
    public RefreshHeaderAndFooterAdapterWrap attachView(@LayoutRes int header, @LayoutRes int footer,ViewGroup parent){
        addHeaderLayout(header,parent);
        addfooterLayout(footer,parent);
        return this;
    }

    public Holder getHeaderHolder(){
        return new Holder(getHeader());
    }
    public Holder getfooterHolder(){
        return new Holder(getFooter());
    }
}
