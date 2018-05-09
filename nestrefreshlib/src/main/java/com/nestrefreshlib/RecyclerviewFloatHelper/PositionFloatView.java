package com.nestrefreshlib.RecyclerviewFloatHelper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 适用于多item
 * Created by 不听话的好孩子 on 2018/4/27.
 */

public class PositionFloatView extends RecyclerView.OnScrollListener implements RecyclerviewFloatHelper.FloatInterface {
    private final List<Integer> list = new ArrayList<>();
    RecyclerView.Adapter adapter;
    RecyclerviewFloatHelper.OnFloatClickListener listener;
    RecyclerView recyclerView;
    ViewGroup container;
    int currentfloatposition = -1;
    int[] floatposition;
    int translationx = 0;
    boolean crashmove = true;
    private RecyclerView.ViewHolder viewHolder;

    public void setOnFloatClickListener(RecyclerviewFloatHelper.OnFloatClickListener listener) {
        this.listener = listener;
    }

    public void setCrashmove(boolean crashmove) {
        this.crashmove = crashmove;
    }

    public PositionFloatView(ViewGroup container, int... floatposition) {
        Arrays.sort(floatposition);
        this.floatposition = floatposition;
        this.container = container;
        for (int i : this.floatposition) {
            list.add(i);
        }
    }

    @Override
    public void attachRecyclerview(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(this);
        adapter = recyclerView.getAdapter();
    }

    @Override
    public void detachRecyclerview() {
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(this);
        }
        if (viewHolder != null) {
            container.removeView(viewHolder.itemView);
        }
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
        if (crashmove && viewHolder != null) {
            View childAt=null;
            for (int i = 0; i < 3; i++) {
                View child = recyclerView.getChildAt(i);
                int childAdapterPosition = recyclerView.getChildAdapterPosition(child);
                if(list.contains(childAdapterPosition)&&childAdapterPosition>=getFirstcompletevisable(recyclerView)){
                    childAt=child;
                    break;
                }
            }
                if (childAt!=null&&firstcompletevisable != -1&& childAt.getTop() <= viewHolder.itemView.getHeight()) {
                    int height = viewHolder.itemView.getHeight();
                    int top = height - childAt.getTop();
                    if (top >= 0 && top <= height) {
                        translationx = -top;
                        viewHolder.itemView.setTranslationY(-top);
                    }
                } else {
                    translationx = 0;
                    viewHolder.itemView.setTranslationY(0);
                }
        }
        if (currentnearestposition == -1) {
            if (viewHolder != null) {
                container.removeView(viewHolder.itemView);
                viewHolder = null;
                currentfloatposition = -1;
            }
            return;
        } else {
            if (currentnearestposition == currentfloatposition || (currentfloatposition == firstcompletevisable && firstcompletevisable == getFirstvisable(recyclerView))) {
                return;
            } else {
                if (viewHolder != null) {
                    container.removeView(viewHolder.itemView);
                    viewHolder = null;
                    currentfloatposition = -1;
                }
            }
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
        viewHolder.itemView.setTranslationY(translationx);
        container.addView(viewHolder.itemView);
        adapter.onBindViewHolder(viewHolder, currentfloatposition);
    }

    public int getFirstcompletevisable(RecyclerView recyclerView) {
        return RecyclerviewFloatHelper.getFirstcompletevisable(recyclerView);
    }

    public int getFirstvisable(RecyclerView recyclerView) {
        return RecyclerviewFloatHelper.getFirstvisable(recyclerView);
    }
}
