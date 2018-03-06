package com.nestrefreshlib.RefreshViews.AdapterHelper.Base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nestrefreshlib.Adpater.Impliment.SAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class BaseHeaderAndFooterAdapterWrap extends RecyclerView.Adapter implements AdapterRefreshInterface {
    protected RecyclerView.Adapter adapter;

    protected List<View> headers = new ArrayList<>();
    protected List<View> footers = new ArrayList<>();
    private static int HEADERBASE = 99999999;
    private static int FOOTERBASE = -99999999;

    public BaseHeaderAndFooterAdapterWrap(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        adapter.registerAdapterDataObserver(observer);
    }
    RecyclerView.AdapterDataObserver observer=new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        }

    };

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        adapter.unregisterAdapterDataObserver(observer);
    }
    public BaseHeaderAndFooterAdapterWrap addHeader(View Header) {
        headers.add(Header);
        return this;
    }

    public BaseHeaderAndFooterAdapterWrap addFooter(View footer) {
        footers.add(footer);
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= HEADERBASE) {
            int position = viewType - HEADERBASE;
            return new RecyclerView.ViewHolder(headers.get(position)) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        } else if (viewType <= FOOTERBASE) {
            int position = FOOTERBASE - viewType;
            return new RecyclerView.ViewHolder(footers.get(position)) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (adapter instanceof SAdapter) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager manager = (GridLayoutManager) layoutManager;
                final int spanCount = manager.getSpanCount();
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    public int getSpanSize(int position) {
                        int itemViewType = getItemViewType(position);
                        if (itemViewType <= FOOTERBASE || itemViewType >= HEADERBASE) {
                            return spanCount;
                        }
                        return ((SAdapter) adapter).setIfGridLayoutManagerSpan(itemViewType, position - headers.size(), spanCount);
                    }
                });
            }
        } else {
            adapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if(adapter instanceof SAdapter){
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if(params instanceof android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) {
                int itemViewType = holder.getItemViewType();
                if (itemViewType <= FOOTERBASE || itemViewType >= HEADERBASE) {
                    ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams)params).setFullSpan(true);
                }else {
                    ((android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(((SAdapter) adapter).setIfStaggedLayoutManagerFullspan(itemViewType));
                }
            }
        }else{
            adapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < headers.size()) {
            onBindHeader(headers.get(position), position);
        } else if (position < headers.size() + adapter.getItemCount()) {
            adapter.onBindViewHolder(holder, position - headers.size());
        } else {
            onBindHeader(footers.get(position - headers.size() - adapter.getItemCount()), position);
        }
    }

    public void onBindHeader(View view, int position) {
    }

    public void onBindFooter(View view, int position) {
    }

    public View getHeader(){

        return headers.get(0);
    }

    public View getFooter(){

        return footers.get(0);
    }

    @Override
    public RecyclerView.Adapter getWrapAdapter() {
        return adapter;
    }

    @Override
    public int getItemCount() {
        return headers.size() + adapter.getItemCount() + footers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headers.size()) {
            return HEADERBASE + position;
        } else if (position < headers.size() + adapter.getItemCount()) {
            return adapter.getItemViewType(position - headers.size());
        } else {
            return FOOTERBASE - position+headers.size()+adapter.getItemCount();
        }
    }
}
