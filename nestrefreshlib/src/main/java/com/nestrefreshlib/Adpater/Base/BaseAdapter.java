package com.nestrefreshlib.Adpater.Base;

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

public abstract class BaseAdapter extends RecyclerView.Adapter {
    //数据集合
    protected List list;

    public void setCount(int count) {
        this.count = count;
    }

    private int count = 0;

    /**
     * 设置数据构造
     *
     * @param list
     */
    public BaseAdapter(List list) {
        this.list = list;
    }

    public BaseAdapter(int count) {
        this.count = count;
    }

    public BaseAdapter() {

    }

    /**
     * 获取数据bean
     *
     * @return
     */
    public <T> List<T> getBeanlist() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
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

    protected void onDestory() {
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
        onBindView((Holder) holder, list == null ? null : list.get(position), position);
    }

    /**
     * item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
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
        return getType(position);
    }

    protected abstract int getType(int positon);

    protected abstract boolean setIfStaggedLayoutManagerFullspan(int itemViewType);

    protected abstract void onBindView(Holder holder, Object t, int positon);

    protected abstract int setIfGridLayoutManagerSpan(int viewtype, int position, int spanCount);
}
