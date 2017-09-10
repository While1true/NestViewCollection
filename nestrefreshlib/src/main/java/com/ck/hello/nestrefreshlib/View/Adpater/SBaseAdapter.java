package com.ck.hello.nestrefreshlib.View.Adpater;///*

/* Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */


import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseAdapterRecord;
        import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;

        import java.util.List;


/**
 * Created by S0005 on 2017/4/17.
 * SHOW_EMPTY:为空时  SHOW_LOADING：加载  SHOW_ERROR：网络错误 SHOW_NOMORE：无更多
 */

public abstract class SBaseAdapter<T, E> extends BaseAdapterRecord<T, E> {

    private int layoutid;

    public SBaseAdapter(List<T> list, int layoutid) {
        super(list);
        this.layoutid = layoutid;
    }

    @Override
    protected int getType(int positon) {
        return TYPE_ITEM;
    }

    protected SimpleViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return SimpleViewHolder.createViewHolder(parent.getContext(), InflateView(layoutid, parent));
    }

    @Override
    protected boolean setIfStaggedLayoutManagerFullspan(int itemViewType) {
        return isfullspan(itemViewType);
    }


    protected abstract void onBindView(SimpleViewHolder holder, T item, int position);

    @Override
    protected int setIfGridLayoutManagerSpan(int itemViewType, int position, int spanCount) {
        return (isfullspan(itemViewType) ? spanCount : 1);
    }
}
