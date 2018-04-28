package com.nestrefreshlib.RecyclerviewFloatHelper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 不听话的好孩子 on 2018/4/27.
 */
public class ViewTypeFloatView extends RecyclerView.OnScrollListener implements RecyclerviewFloatHelper.FloatInterface {
    RecyclerView.Adapter adapter;
    RecyclerviewFloatHelper.OnFloatClickListener listener;
    ViewGroup container;
    int viewtype = -1;
    int currentfloatposition = -1;
    int rollbackSzie = 4;
    private RecyclerView.ViewHolder viewHolder;
    int firstfloatitemposition = -1;

    public void setOnFloatClickListener(RecyclerviewFloatHelper.OnFloatClickListener listener) {
        this.listener = listener;
    }

    public ViewTypeFloatView(ViewGroup container, int viewtype) {
        this.viewtype = viewtype;
        this.container = container;
    }

    public ViewTypeFloatView(ViewGroup container, int viewtype, int rollbackSzie) {
        this.viewtype = viewtype;
        this.container = container;
        this.rollbackSzie = rollbackSzie;
    }

    @Override
    public void attachRecyclerview(RecyclerView recyclerView) {
        recyclerView.removeOnScrollListener(this);
        recyclerView.addOnScrollListener(this);
        adapter = recyclerView.getAdapter();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int firstcompletevisable = getFirstcompletevisable(recyclerView);

        /**
         * 找到里的最近的viewtype的position
         */
        int currentnearestposition = -1;
        for (int i = firstcompletevisable - 1; i >= 0 && i > firstcompletevisable - rollbackSzie; i--) {
            if (adapter.getItemViewType(i) == viewtype) {
                currentnearestposition = i;
                if (firstfloatitemposition == -1) {
                    firstfloatitemposition = i;
                }
                break;
            }
        }
        if (currentnearestposition == currentfloatposition) {
            if (viewHolder != null) {
                if (firstcompletevisable != -1 && adapter.getItemViewType(firstcompletevisable) == viewtype) {
                    View childAt = recyclerView.getChildAt(1);
                    int top = viewHolder.itemView.getHeight() - childAt.getTop();
                    if (top > 0 && top < viewHolder.itemView.getHeight())
                        viewHolder.itemView.setTranslationY(-top);
                } else {
                    viewHolder.itemView.setTranslationY(0);
                }
            }
            return;
        }
        if (currentnearestposition == -1) {
            if (firstcompletevisable <= firstfloatitemposition && viewHolder != null) {
                viewHolder.itemView.setVisibility(View.GONE);
                currentfloatposition = -1;
            }
        } else {
            if (viewHolder == null) {
                viewHolder = adapter.createViewHolder(recyclerView, viewtype);
                container.addView(viewHolder.itemView);
            } else {
                viewHolder.itemView.setVisibility(View.VISIBLE);
            }
            currentfloatposition = currentnearestposition;
            if (listener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v, currentfloatposition);
                    }
                });
            }
            adapter.onBindViewHolder(viewHolder, currentfloatposition);
        }
    }

    public int getFirstcompletevisable(RecyclerView recyclerView) {
        return RecyclerviewFloatHelper.getFirstcompletevisable(recyclerView);
    }
}
