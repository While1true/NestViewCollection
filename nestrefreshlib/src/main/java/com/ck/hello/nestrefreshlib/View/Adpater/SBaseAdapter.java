package com.ck.hello.nestrefreshlib.View.Adpater;///*

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
/* Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */


import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateInterface;

import java.util.List;


/**
 * Created by S0005 on 2017/4/17.
 * SHOW_EMPTY:为空时  SHOW_LOADING：加载  SHOW_ERROR：网络错误 SHOW_NOMORE：无更多
 */

public abstract class SBaseAdapter<T, E> extends RecyclerView.Adapter {
    /**
     * 存储全局布局id
     */
    private static Recorder recorder;

    public static void init(Recorder recorder1) {
        recorder = recorder1;
    }

    public static final int SHOW_EMPTY = -100, SHOW_LOADING = -200, SHOW_ERROR = -300, SHOW_NOMORE = -400;
    protected int showstate = SHOW_LOADING;

    protected List<T> list;


    public static final int TYPE_ITEM = 30000000;
    private int height = 0;

    private int layoutid;
    private E e = null;
    StateInterface StateHandler;


    //是否全屏
    private boolean full = true;

    /**
     * 设置布局
     *
     * @param emptyres
     * @param loadingres
     * @param errorres
     * @param nomore
     * @param
     */
    public SBaseAdapter<T, E> setStateLayout(int emptyres, int loadingres, int errorres, int nomore) {
        Recorder.Builder builder = new Recorder.Builder();
        if (emptyres != 0)
            builder.setEmptyRes(emptyres);
        if (loadingres != 0)
            builder.setLoadingRes(loadingres);
        if (errorres != 0)
            builder.setErrorRes(errorres);
        if (nomore != 0)
            builder.setNomoreRes(nomore);
        recorder = builder.build();
        return this;
    }

    public SBaseAdapter(List<T> list, int layoutid) {
        if(recorder==null)
            recorder=new Recorder.Builder().build();
        this.list = list;
        this.layoutid = layoutid;
        StateHandler = new StateHandler();
    }

    public SBaseAdapter<T, E> setStateHandler(StateInterface handler) {
        if (StateHandler.getStateClickListener() != null)
            handler.setStateClickListener(StateHandler.getStateClickListener());
        this.StateHandler = handler;
        return this;
    }

    public SBaseAdapter<T, E> setStateListener(BaseStateListener listener) {
        StateHandler.setStateClickListener(listener);
        return this;
    }


    public int getShowstate() {
        return showstate;
    }

    public void setBeanList(List<T> t) {
        this.list = t;
        showstate = TYPE_ITEM;
    }

    public List<T> getBeanlist() {
        return list;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            final int spanCount = manager.getSpanCount();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    return (isfullspan(itemViewType)?spanCount:1);
                }
            });
        }
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (height == 0)
                    height = recyclerView.getMeasuredHeight();
//                    notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if(params instanceof StaggeredGridLayoutManager.LayoutParams){
            int itemViewType = holder.getItemViewType();
            ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(isfullspan(itemViewType));

        }
    }
    public boolean isfullspan(int type){

        if(type==SHOW_EMPTY||type==SHOW_ERROR||type==SHOW_LOADING||type==SHOW_NOMORE)
            return true;
        return false;
    }
    public void showState(int showstate, E e) {
        if (this.showstate != showstate)
            StateHandler.switchState();
        this.showstate = showstate;
        this.e = e;
        notifyDataSetChanged();
    }

    public void showEmpty() {
        showState(SBaseMutilAdapter.SHOW_EMPTY, null);
    }
    public void setShowError() {
        showState(SBaseMutilAdapter.SHOW_ERROR, null);
    }
    public void showItem() {
        showState(SBaseMutilAdapter.TYPE_ITEM, null);
    }
    public void showLoading() {
        showState(SBaseMutilAdapter.SHOW_LOADING, null);
    }
    public void setShowNomore() {
        showState(SBaseMutilAdapter.SHOW_NOMORE, null);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        StateHandler.destory();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SHOW_EMPTY:
                return SimpleViewHolder.createViewHolder(parent.getContext(),InflateView(recorder.getEmptyres(), parent));
            case SHOW_LOADING:
                return SimpleViewHolder.createViewHolder(parent.getContext(),InflateView(recorder.getLoadingres(), parent));
            case SHOW_ERROR:
                return SimpleViewHolder.createViewHolder(parent.getContext(),InflateView(recorder.getErrorres(), parent));
            case SHOW_NOMORE:
                return SimpleViewHolder.createViewHolder(parent.getContext(),InflateView(recorder.getNomore(), parent));
        }
        return onCreate(parent, viewType);
    }

    public View InflateView(int layout, ViewGroup parent) {

        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (showstate) {
            case SHOW_EMPTY:
                if (height != 0)
                    holder.itemView.getLayoutParams().height = height;
                StateHandler.BindEmptyHolder((SimpleViewHolder) holder, e);
                return;
            case SHOW_LOADING:
                if (height != 0)
                    holder.itemView.getLayoutParams().height = height;
                StateHandler.BindLoadingHolder((SimpleViewHolder) holder, e);

                return;
            case SHOW_ERROR:
                if (height != 0)
                    holder.itemView.getLayoutParams().height = height;

                StateHandler.BindErrorHolder((SimpleViewHolder) holder, e);
                return;
            case SHOW_NOMORE:
                if (position == getItemCount() - 1) {
                    StateHandler.BindNomoreHolder((SimpleViewHolder) holder, e);
                    return;
                }
                break;
        }
            onBind((SimpleViewHolder) holder, list.get(position), position);

    }

    @Override
    public int getItemCount() {
        if (showstate == SHOW_EMPTY || showstate == SHOW_LOADING || showstate == SHOW_ERROR) {
            return 1;
        }
        if (showstate == SHOW_NOMORE)
            return getCount() + 1;
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        switch (showstate) {
            case SHOW_EMPTY:
                return SHOW_EMPTY;
            case SHOW_LOADING:
                return SHOW_LOADING;
            case SHOW_ERROR:
                return SHOW_ERROR;
            case SHOW_NOMORE:
                if (position < getItemCount() - 1)
                    return getType(position);
                else
                    return SHOW_NOMORE;

        }
        return getType(position);
    }


    private int getCount() {
        return list == null ? 0 : list.size();
    }

    protected int getType(int position) {
        return TYPE_ITEM;
    }

    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return SimpleViewHolder.createViewHolder(parent.getContext(), InflateView(layoutid, parent));
    }

    protected abstract void onBind(SimpleViewHolder holder, T item, int position);
}
