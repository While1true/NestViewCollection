package com.ck.hello.nestrefreshlib.View.Adpater.Base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Interface.BaseStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.ShowStateInterface;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.StateHandlerInterface;

import java.util.List;

/**
 * Created by ck on 2017/9/10.
 */

public abstract class BaseAdapter<T, E> extends RecyclerView.Adapter implements ShowStateInterface<E> {
    //全局id记录者
    protected static Recorder globalrecorder;
    //实例id记录
    protected Recorder recorder;
    protected StateEnum showstate = StateEnum.TYPE_ITEM;
    //数据集合
    protected List<T> list;

    //showstate传递的数据
    private E e = null;
    //状态onindBView
    protected StateHandlerInterface stateHandler;

    private int height = 0;

    public void setCount(int count) {
        this.count = count;
    }

    private int count = 0;

    /**
     * 设置数据构造
     *
     * @param list
     */
    public BaseAdapter(List<T> list) {
        this.list = list;
        init();
    }

    public BaseAdapter(int count) {
        this.count = count;
        init();
    }

    public BaseAdapter() {
        init();
    }

    private void init() {
        recorder = globalrecorder;
        if (recorder == null)
            recorder = new Recorder.Builder().build();
        try {
            stateHandler = recorder.getClazz().newInstance();
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
    public StateEnum getShowstate() {
        return showstate;
    }

    /**
     * 设置bean
     *
     * @param t
     */
    public void setBeanList(List<T> t) {
        this.list = t;
        showstate = StateEnum.TYPE_ITEM;
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
        showstate = StateEnum.TYPE_ITEM;
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
     *
     * @param type
     * @return
     */
    public boolean isfullspan(int type) {
        if (type < 4)
            return true;
        return false;
    }

    /**
     * @param handler 设置状态布局的处理器
     * @return
     */
    public BaseAdapter setStateHandler(StateHandlerInterface handler) {
        if (stateHandler.getStateClickListener() != null)
            handler.setStateClickListener(stateHandler.getStateClickListener());
        this.stateHandler = handler;
        return this;
    }

    /**
     * 设置状态布局的监听
     *
     * @param listener
     * @return
     */
    public BaseAdapter setStateListener(BaseStateListener listener) {
        stateHandler.setStateClickListener(listener);
        return this;
    }

    /**
     * 设置布局layout
     *
     * @param builder
     */
    public BaseAdapter setStateLayout(Recorder.Builder builder) {
        recorder = builder.build();
        return this;
    }

    /**
     * 传递数据给 StateHandler并切换显示状态
     *
     * @param showstate
     * @param e
     */
    public void showState(StateEnum showstate, E e) {
        showStateNotNotify(showstate, e);
        notifyDataSetChanged();
    }

    public void showEmpty() {
        showState(StateEnum.SHOW_EMPTY, null);
    }

    public void showStateNotNotify(StateEnum showstate, E e) {
        if (this.showstate != showstate)
            stateHandler.switchState(showstate);
        this.showstate = showstate;
        this.e = e;
    }

    public void ShowError() {
        showState(StateEnum.SHOW_ERROR, null);
    }

    public void showItem() {
        showState(StateEnum.TYPE_ITEM, null);
    }

    public void showLoading() {
        showState(StateEnum.SHOW_LOADING, null);
    }

    public void showNomore() {
        showState(StateEnum.SHOW_NOMORE, null);
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
                    return setIfGridLayoutManagerSpan(itemViewType, position, spanCount);
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        stateHandler.destory();
        stateHandler = null;
        if (list != null) list.clear();
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
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (showstate) {
            case SHOW_EMPTY:
                if (height != 0)
                    holder.itemView.getLayoutParams().height = height;
                stateHandler.BindEmptyHolder((SimpleViewHolder) holder, e);
                return;
            case SHOW_LOADING:
                if (height != 0)
                    holder.itemView.getLayoutParams().height = height;
                stateHandler.BindLoadingHolder((SimpleViewHolder) holder, e);
                return;
            case SHOW_ERROR:
                if (height != 0)
                    holder.itemView.getLayoutParams().height = height;
                stateHandler.BindErrorHolder((SimpleViewHolder) holder, e);
                return;
            case SHOW_NOMORE:
                if (position == getItemCount() - 1) {
                    stateHandler.BindNomoreHolder((SimpleViewHolder) holder, e);
                    return;
                }
                break;
        }
        onBindView((SimpleViewHolder) holder, list == null ? null : list.get(position), position);
    }

    /**
     * bindview 先拦截设置状态onCreateViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (StateEnum.SHOW_EMPTY.ordinal() == viewType)
            return SimpleViewHolder.createViewHolder(InflateView(recorder.getEmptyres(), parent));
        else if (StateEnum.SHOW_LOADING.ordinal() == viewType)
            return SimpleViewHolder.createViewHolder(InflateView(recorder.getLoadingres(), parent));
        else if (StateEnum.SHOW_ERROR.ordinal() == viewType)
            return SimpleViewHolder.createViewHolder(InflateView(recorder.getErrorres(), parent));
        else if (StateEnum.SHOW_NOMORE.ordinal() == viewType)
            return SimpleViewHolder.createViewHolder(InflateView(recorder.getNomore(), parent));
        else return onCreateHolder(parent, viewType);
    }

    /**
     * item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (showstate.ordinal()<3) {
            return 1;
        }
        if (showstate == StateEnum.SHOW_NOMORE)
            return getCount() + 1;
        return getCount();
    }

    private int getCount() {
        return list == null ? count : list.size();
    }

    /**
     * item type
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (showstate) {
            case SHOW_EMPTY:
                return StateEnum.SHOW_EMPTY.ordinal();
            case SHOW_LOADING:
                return StateEnum.SHOW_LOADING.ordinal();
            case SHOW_ERROR:
                return StateEnum.SHOW_ERROR.ordinal();
            case SHOW_NOMORE:
                if (position < getItemCount() - 1)
                    return getType(position);
                else
                    return StateEnum.SHOW_NOMORE.ordinal();

        }
        return getType(position);
    }

    protected abstract int getType(int positon);

    protected abstract SimpleViewHolder onCreateHolder(ViewGroup parent, int viewType);

    protected abstract boolean setIfStaggedLayoutManagerFullspan(int itemViewType);

    protected abstract void onBindView(SimpleViewHolder holder, T t, int positon);

    protected abstract int setIfGridLayoutManagerSpan(int viewtype, int position, int spanCount);
}
