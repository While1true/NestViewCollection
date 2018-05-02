package com.nestrefreshlib.Adpater.Impliment;///*

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.nestrefreshlib.Adpater.Base.BaseAdapter;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.Adpater.Base.Holder;

import java.util.List;

public class SAdapter extends BaseAdapter implements LifecycleObserver {
    public SAdapter(List<?> list) {
        super(list);
    }

    public SAdapter(int count) {
        super(count);
    }

    public SAdapter() {
        super();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return Holder.createViewHolder(InflateView(Holdersid.get(viewType).getLayout(), parent));
    }

    public <T extends SAdapter>T addLifeOwener(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
        return (T) this;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Override
    protected void onDestory() {
        super.onDestory();
        Holdersid.clear();
    }

    protected int getType(int position) {
        return getMutilType(list == null ? null : list.get(position), position);
    }


    @Override
    protected void onBindView(Holder holder, Object t, int positon) {
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
    public int setIfGridLayoutManagerSpan(int itemViewType, int position, int spanCount) {
        return Holdersid.get(itemViewType).gridSpanSize(list == null ? null : list.get(position), position);
    }

    /**
     * StaggedLayoutManager 是否全屏时调用
     *
     * @param itemViewType
     * @return
     */
    @Override
    public boolean setIfStaggedLayoutManagerFullspan(int itemViewType) {
        return Holdersid.get(itemViewType).isfull();
    }

    SparseArray<ItemHolder> Holdersid = new SparseArray<>(3);

    public <T extends SAdapter>T addType(int layoutid, ItemHolder<?> itemholder) {
        Holdersid.put(Holdersid.size(), itemholder.setLayout(layoutid));
        return (T) this;
    }

    public <T extends SAdapter>T addType(ItemHolder<?> itemholder) {
        if (itemholder.getLayout() == 0) {
            throw new NullPointerException("layoutid not defined");
        }
        Holdersid.put(Holdersid.size(), itemholder);
        return (T) this;
    }

    protected int getMutilType(Object item, int position) {

        for (int i = 0; i < Holdersid.size(); i++) {
            if (Holdersid.valueAt(i).istype(item, position)) {
                return Holdersid.keyAt(i);
            }
        }

        return -1;
    }

    public void differUpdate(List<? extends DifferCallback.differ> newlist) {
        DifferCallback callback = new DifferCallback(list, newlist);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback,false);
        list=newlist;
        diffResult.dispatchUpdatesTo(this);
    }

    public static class DifferCallback<T extends DifferCallback.differ> extends DiffUtil.Callback {
        List<T> oldList;
        List<T> newList;

        public DifferCallback(List<T> oldList, List<T> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        public interface differ {
            String firstCondition();

            String secondCondition();
        }

        @Override
        public int getOldListSize() {
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).firstCondition().equals(newList.get(newItemPosition).firstCondition());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).secondCondition().equals(newList.get(newItemPosition).secondCondition());
        }
    }
}
