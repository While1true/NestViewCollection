package com.ck.hello.nestrefreshlib.View.Adpater.Base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ck on 2017/9/9.
 */

public interface StateInterface<T> {
    StateInterface  setStateClickListener(StateClickListener listener);
    StateClickListener  getStateClickListener();
    void BindEmptyHolder(RecyclerView.ViewHolder holder,T t);
    void BindErrorHolder(RecyclerView.ViewHolder holder,T t);
    void BindLoadingHolder(RecyclerView.ViewHolder holder,T t);
    void BindNomoreHolder(RecyclerView.ViewHolder holder,T t);
    void destory();
}
