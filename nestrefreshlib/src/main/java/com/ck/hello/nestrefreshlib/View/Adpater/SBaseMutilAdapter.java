package com.ck.hello.nestrefreshlib.View.Adpater;///*

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateInterface;

import java.util.ArrayList;
import java.util.List;

/* Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */


/**
 * Created by S0005 on 2017/4/17.
 * SHOW_EMPTY:为空时  SHOW_LOADING：加载  SHOW_ERROR：网络错误 SHOW_NOMORE：无更多
 */

/**
 * @param <T> data的泛型
 * @param <E> 传给状态布局的泛型
 *            如果StateHandler写死就不用管E
 */
public class SBaseMutilAdapter<T, E> extends RecyclerView.Adapter {
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

    private E e = null;
    StateInterface StateHandler;

    public static final int TYPE_ITEM = 30000000;
    private int height = 0;


    /**
     * 设置布局layout
     *
     * @param emptyres
     * @param loadingres
     * @param errorres
     * @param nomore
     * @param
     */
    public SBaseMutilAdapter<T, E> setStateLayout(int emptyres, int loadingres, int errorres, int nomore) {
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

    /**
     * 设置数据构造
     *
     * @param list
     */
    public SBaseMutilAdapter(List<T> list) {
        if (recorder == null)
            recorder = new Recorder.Builder().build();
        this.list = list;
        StateHandler = new StateHandler();
    }

    /**
     * @param handler 设置状态布局的处理器
     * @return
     */
    public SBaseMutilAdapter<T, E> setStateHandler(StateInterface handler) {
        if (StateHandler.getStateClickListener() != null)
            handler.setStateClickListener(StateHandler.getStateClickListener());
        this.StateHandler = handler;
        return this;
    }

    /**
     * 设置状态布局的监听
     *
     * @param listener
     * @return
     */
    public SBaseMutilAdapter<T, E> setStateListener(BaseStateListener listener) {
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

                    return (itemViewType>=0&&itemViewType<Holdersid.size())?
                            (Holdersid.get(itemViewType).isfull()?spanCount:1):(isfullspan(itemViewType)?spanCount:1);
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
         ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan((itemViewType>=0&&itemViewType<Holdersid.size())?
                 Holdersid.get(itemViewType).isfull():isfullspan(itemViewType));

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
        StateHandler = null;
        Holdersid.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SHOW_EMPTY:
                return SimpleViewHolder.createViewHolder(parent.getContext(), InflateView(recorder.getEmptyres(), parent));
            case SHOW_LOADING:
                return SimpleViewHolder.createViewHolder(parent.getContext(), InflateView(recorder.getLoadingres(), parent));
            case SHOW_ERROR:
                return SimpleViewHolder.createViewHolder(parent.getContext(), InflateView(recorder.getErrorres(), parent));
            case SHOW_NOMORE:
                return SimpleViewHolder.createViewHolder(parent.getContext(), InflateView(recorder.getNomore(), parent));
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
        for (int i = 0; i < Holdersid.size(); i++) {
            if (Holdersid.get(i).istype(list.get(position), position)) {
                Holdersid.get(i).onBind((SimpleViewHolder) holder, list.get(position), position);
                break;
            }
        }

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
        if (Holdersid.size() == 0)
            return TYPE_ITEM;
        else return getMutilType(list.get(position), position);
    }

    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        SimpleViewHolder viewHolder = SimpleViewHolder.createViewHolder(parent.getContext(), InflateView(Holdersid.get(viewType).getLayout(), parent));
        return viewHolder;
    }

    ArrayList<ITEMHOLDER> Holdersid = new ArrayList<>(3);

    public SBaseMutilAdapter<T, E> addType(int layoutid, ITEMHOLDER<T> itemholder) {
        Holdersid.add(itemholder.setLayout(layoutid));
        return this;
    }

    protected int getMutilType(T item, int position) {
        Log.i("Debug", "getMutilType: " + position + "\n");
        for (int i = 0; i < Holdersid.size(); i++) {
            Log.i("DEBUG", "getMutilType: " + Holdersid.get(i).istype(item, position));
            if (Holdersid.get(i).istype(item, position)) {
                return i;
            }
        }

        return TYPE_ITEM;
    }

    public static abstract class ITEMHOLDER<T> {
        private int layout;

        protected boolean isfull() {
            return false;
        }

        public abstract void onBind(SimpleViewHolder holder, T item, int position);

        public abstract boolean istype(T item, int position);

        public int getLayout() {
            return layout;
        }

        public ITEMHOLDER setLayout(int layout) {
            this.layout = layout;
            return this;
        }
    }
}
