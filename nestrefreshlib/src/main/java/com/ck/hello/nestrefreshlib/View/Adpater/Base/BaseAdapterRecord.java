package com.ck.hello.nestrefreshlib.View.Adpater.Base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * Created by ck on 2017/9/10.
 */

public abstract class BaseAdapterRecord<T, E> extends RecyclerView.Adapter implements ShowStateInterface<E> {
    //全局id记录者
    protected static Recorder globalrecorder;
    //实例id记录
    protected Recorder recorder;
    //状态码
    public static final int SHOW_EMPTY = -100, SHOW_LOADING = -200, SHOW_ERROR = -300, SHOW_NOMORE = -400, TYPE_ITEM = 30000000;
    protected int showstate = SHOW_LOADING;
    //数据集合
    protected List<T> list;

    //showstate传递的数据
    private E e = null;
    //状态onindBView
    protected StateHandlerInterface StateHandler;

    private int height = 0;
    /**
     * 设置数据构造
     *
     * @param list
     */
    public BaseAdapterRecord(List<T> list) {
        recorder=globalrecorder;
        if (recorder == null)
            recorder = new Recorder.Builder().build();
        this.list = list;
        try {
            StateHandler =recorder.getClazz().newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
    /**
     * 全局初始化状态 layout id
     *
     * @param globalRecordera
     */
    public static void init(Recorder globalRecordera) {
        globalrecorder = globalRecordera;
    }


    /**
     * 当前显示状态
     *
     * @return
     */
    public int getShowstate() {
        return showstate;
    }

    /**
     * 设置bean
     *
     * @param t
     */
    public void setBeanList(List<T> t) {
        this.list = t;
        showstate = TYPE_ITEM;
    }

    /**
     * 添加数据bean
     *
     * @param t
     */
    public void addBeanList(List<T> t) {
        if (this.list == null)
            this.list = t;
        else
            this.list.addAll(t);
        showstate = TYPE_ITEM;
    }

    /**
     * 获取数据bean
     *
     * @return
     */
    public List<T> getBeanlist() {
        return list;
    }

    /**
     * 是否全屏显示
     * @param type
     * @return
     */
    public boolean isfullspan(int type) {
        if (type == SHOW_EMPTY || type == SHOW_ERROR || type == SHOW_LOADING || type == SHOW_NOMORE)
            return true;
        return false;
    }

    /**
     * @param handler 设置状态布局的处理器
     * @return
     */
    public <R extends BaseAdapterRecord<T,E>>R setStateHandler(StateHandlerInterface handler) {
        if (StateHandler.getStateClickListener() != null)
            handler.setStateClickListener(StateHandler.getStateClickListener());
        this.StateHandler = handler;
        return (R)this;
    }

    /**
     * 设置状态布局的监听
     *
     * @param listener
     * @return
     */
    public <R extends BaseAdapterRecord<T,E>>R setStateListener(BaseStateListener listener) {
        StateHandler.setStateClickListener(listener);
        return  (R)this;
    }

    /**
     * 设置布局layout
     *
     * @param builder
     */
    public <R extends BaseAdapterRecord<T,E>>R setStateLayout(Recorder.Builder builder) {
        recorder = builder.build();
        return (R)this;
    }

    /**
     * 传递数据给 StateHandler并切换显示状态
     * @param showstate
     * @param e
     */
    public void showState(int showstate, E e) {
        if (this.showstate != showstate)
            StateHandler.switchState();
        this.showstate = showstate;
        this.e = e;
        notifyDataSetChanged();
    }

    public void showEmpty() {
        showState(BaseAdapterRecord.SHOW_EMPTY, null);
    }

    public void ShowError() {
        showState(BaseAdapterRecord.SHOW_ERROR, null);
    }

    public void showItem() {
        showState(BaseAdapterRecord.TYPE_ITEM, null);
    }

    public void showLoading() {
        showState(BaseAdapterRecord.SHOW_LOADING, null);
    }

    public void showNomore() {
        showState(BaseAdapterRecord.SHOW_NOMORE, null);
    }


    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            final int spanCount = manager.getSpanCount();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemViewType = getItemViewType(position);
                    return setIfGridLayoutManagerSpan(itemViewType,position,spanCount) ;
                }
            });
        }
    }
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        StateHandler.destory();
        StateHandler = null;
        if(list!=null)list.clear();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            int itemViewType = holder.getItemViewType();
            ((StaggeredGridLayoutManager.LayoutParams) params)
                    .setFullSpan(setIfStaggedLayoutManagerFullspan(itemViewType));
        }
    }



    public View InflateView(int layout, ViewGroup parent) {

        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    /**
     * bindview 先拦截设置状态布局
     * @param holder
     * @param position
     */
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
        onBindView((SimpleViewHolder) holder, list.get(position), position);
    }
    /**
     * bindview 先拦截设置状态onCreateViewHolder
     */
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
        return onCreateHolder(parent, viewType);
    }

    /**
     * item数量
     * @return
     */
    @Override
    public int getItemCount() {
        if (showstate == SHOW_EMPTY || showstate == SHOW_LOADING || showstate == SHOW_ERROR) {
            return 1;
        }
        if (showstate == SHOW_NOMORE)
            return getCount() + 1;
        return getCount();
    }
    private int getCount() {
        return list == null ? 0 : list.size();
    }
    /**
     * item type
     * @param position
     * @return
     */
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

    protected abstract int getType(int positon);

    protected abstract SimpleViewHolder onCreateHolder(ViewGroup parent, int viewType);
    protected abstract boolean  setIfStaggedLayoutManagerFullspan(int itemViewType) ;
    protected abstract void onBindView(SimpleViewHolder holder, T t, int positon);
    protected abstract int setIfGridLayoutManagerSpan(int viewtype,int position,int spanCount);
}
