package com.ck.hello.nestrefreshlib.View.Adpater.Impliment;///*

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.BaseStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.StateHandlerInterface;

import java.util.ArrayList;

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

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Holdersid.clear();
    }

    protected int getType(int position) {
        if (Holdersid.size() == 0)
            return TYPE_ITEM;
        else return getMutilType(list.get(position), position);
    }


    @Override
    protected void onBindView(SimpleViewHolder holder, T t, int positon) {
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
        return (itemViewType >= 0 && itemViewType < Holdersid.size()) ?
                (Holdersid.get(itemViewType).gridSpanSize(list.get(position), position)) : (isfullspan(itemViewType) ? spanCount : 1);
    }

    protected SimpleViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        if (viewType >= 0 && viewType < Holdersid.size())
            return SimpleViewHolder.createViewHolder(InflateView(Holdersid.get(viewType).getLayout(), parent));
        //add type未处理完的数据空显示
        return SimpleViewHolder.createViewHolder(new View(parent.getContext()));
    }

    /**
     * StaggedLayoutManager 是否全屏时调用
     *
     * @param itemViewType
     * @return
     */
    @Override
    protected boolean setIfStaggedLayoutManagerFullspan(int itemViewType) {
        return (itemViewType >= 0 && itemViewType < Holdersid.size()) ?
                Holdersid.get(itemViewType).isfull() : isfullspan(itemViewType);
    }

    ArrayList<ItemHolder> Holdersid = new ArrayList<>(3);

    public SAdapter addType(int layoutid, ItemHolder itemholder) {
        Holdersid.add(itemholder.setLayout(layoutid));
        return this;
    }

    protected int getMutilType(T item, int position) {
        for (int i = 0; i < Holdersid.size(); i++) {
            if (Holdersid.get(i).istype(item, position)) {
                return i;
            }
        }

        return TYPE_ITEM;
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
