package com.nestrefreshlib.Adpater.Impliment;///*

import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.nestrefreshlib.Adpater.Base.BaseAdapter;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.Adpater.Base.StateEnum;
import com.nestrefreshlib.Adpater.Interface.BaseStateListener;
import com.nestrefreshlib.Adpater.Base.Recorder;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Interface.StateHandlerInterface;

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
 * @paam  Object 传给状态布局的泛型
 *            如果StateHandler写死就不用管E
 */
public class SAdapter<T> extends BaseAdapter<T, Object> {
    public SAdapter(List<T> list) {
        super(list);
    }

    public SAdapter(int count) {
        super(count);
    }

    public SAdapter() {
        super();
    }

    //    @Override
//    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView);
//        onDestory();
//    }


    @Override
    protected void onDestory() {
        super.onDestory();
        Holdersid.clear();
    }

    protected int getType(int position) {
        if (Holdersid.size() == 0)
            return StateEnum.TYPE_ITEM.ordinal();
        else return getMutilType(list==null?null:list.get(position), position);
    }


    @Override
    protected void onBindView(Holder holder, T t, int positon) {
        Holdersid.get(holder.getItemViewType()).onBind(holder, t, positon);
    }

    /**
     * 如果是GridLayoutManager 返回给getspanLookup
     *
     * @param itemViewType
     * @param position
     * @return
     */
    @Override
    protected int setIfGridLayoutManagerSpan(int itemViewType, int position, int spanCount) {
        return (itemViewType>=StateEnum.values().length) ?
                (Holdersid.get(itemViewType).gridSpanSize(list==null?null:list.get(position), position)) : (isfullspan(itemViewType) ? spanCount : 1);
    }

    protected Holder onCreateHolder(ViewGroup parent, int viewType) {
            return Holder.createViewHolder(InflateView(Holdersid.get(viewType).getLayout(), parent));
    }

    /**
     * StaggedLayoutManager 是否全屏时调用
     *
     * @param itemViewType
     * @return
     */
    @Override
    protected boolean setIfStaggedLayoutManagerFullspan(int itemViewType) {
        return (itemViewType>=StateEnum.values().length) ?
                Holdersid.get(itemViewType).isfull() : isfullspan(itemViewType);
    }

    SparseArray<ItemHolder> Holdersid = new SparseArray<>(3);

    public SAdapter addType(int layoutid, ItemHolder itemholder) {
        Holdersid.put(StateEnum.values().length+Holdersid.size(),itemholder.setLayout(layoutid));
        return this;
    }
    public SAdapter addType(ItemHolder itemholder) {
        if(itemholder.getLayout()==0){
            throw new NullPointerException("layoutid not defined");
        }
        Holdersid.put(StateEnum.values().length+Holdersid.size(),itemholder);
        return this;
    }
    protected int getMutilType(T item, int position) {

        for (int i = 0; i < Holdersid.size(); i++) {
            if (Holdersid.valueAt(i).istype(item, position)) {
                return Holdersid.keyAt(i);
            }
        }

        return StateEnum.TYPE_ITEM.ordinal();
    }



    @Override
    public SAdapter<T> setStateHandler(StateHandlerInterface handler) {
        return (SAdapter<T>) super.setStateHandler(handler);
    }

    @Override
    public SAdapter<T> setStateListener(BaseStateListener listener) {
        return (SAdapter<T>) super.setStateListener(listener);
    }

    @Override
    public SAdapter<T> setStateLayout(Recorder.Builder builder) {
        return (SAdapter<T>) super.setStateLayout(builder);
    }
}
