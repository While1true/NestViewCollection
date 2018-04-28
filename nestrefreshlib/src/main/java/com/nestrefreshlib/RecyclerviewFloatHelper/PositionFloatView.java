package com.nestrefreshlib.RecyclerviewFloatHelper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/4/27.
 */

public class PositionFloatView extends RecyclerView.OnScrollListener implements RecyclerviewFloatHelper.FloatInterface {
    private final List<int[]> list;
    RecyclerView.Adapter adapter;
    RecyclerviewFloatHelper.OnFloatClickListener listener;
    ViewGroup container;
    int currentfloatposition = -1;
    int[] floatposition;
    private RecyclerView.ViewHolder viewHolder;

    public void setOnFloatClickListener(RecyclerviewFloatHelper.OnFloatClickListener listener) {
        this.listener = listener;
    }

    public PositionFloatView(ViewGroup container, int... floatposition) {
        Arrays.sort(floatposition);
        this.floatposition = floatposition;
        this.container = container;
        list = Arrays.asList(floatposition);
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
        for (int i = floatposition.length - 1; i >= 0; i--) {
            if (floatposition[i] < firstcompletevisable) {
                currentnearestposition = floatposition[i];
                break;
            }
        }
        if (currentnearestposition == currentfloatposition) {
            if (viewHolder != null) {
                if (firstcompletevisable != -1 && list.contains(firstcompletevisable)) {
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
        if (viewHolder != null) {
            container.removeView(viewHolder.itemView);
            currentfloatposition = -1;
        }
        if (currentnearestposition == -1) {
            return;
        }
        currentfloatposition = currentnearestposition;
        viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(currentfloatposition));
        if (listener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, currentfloatposition);
                }
            });
        }
        container.addView(viewHolder.itemView);
        adapter.onBindViewHolder(viewHolder, currentfloatposition);
    }
    public int getFirstcompletevisable(RecyclerView recyclerView) {
        return RecyclerviewFloatHelper.getFirstcompletevisable(recyclerView);
    }
}
