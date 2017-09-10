package com.ck.hello.nestrefreshlib.View.Adpater;///*

/* Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */


import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseAdapterRecord;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateHandlerInterface;

import java.util.List;


/**
 * Created by S0005 on 2017/4/17.
 * SHOW_EMPTY:为空时  SHOW_LOADING：加载  SHOW_ERROR：网络错误 SHOW_NOMORE：无更多
 */

public abstract class SBaseAdapter<T> extends BaseAdapterRecord<T, Object> {

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
        return SimpleViewHolder.createViewHolder(InflateView(layoutid, parent));
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


    @Override
    public SBaseAdapter<T> setStateHandler(StateHandlerInterface handler) {
        return (SBaseAdapter<T>) super.setStateHandler(handler);
    }

    @Override
    public SBaseAdapter<T> setStateListener(BaseStateListener listener) {
        return (SBaseAdapter<T>) super.setStateListener(listener);
    }

    @Override
    public SBaseAdapter<T> setStateLayout(Recorder.Builder builder) {
        return (SBaseAdapter<T>) super.setStateLayout(builder);
    }
}
