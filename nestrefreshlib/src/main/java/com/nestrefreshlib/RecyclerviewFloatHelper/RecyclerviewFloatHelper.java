package com.nestrefreshlib.RecyclerviewFloatHelper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by 不听话的好孩子 on 2018/4/27.
 */

public class RecyclerviewFloatHelper {
    private FloatInterface floatInterface;

    public RecyclerviewFloatHelper(FloatInterface floatInterface) {
        this.floatInterface = floatInterface;
    }

    public void setOnFloatClickListener(OnFloatClickListener listener) {
        floatInterface.setOnFloatClickListener(listener);
    }

    public FloatInterface getFloatInterface() {
        return floatInterface;
    }

    public void attachRecyclerview(RecyclerView recyclerView) {
        floatInterface.attachRecyclerview(recyclerView);
    }

    public interface FloatInterface {
        void detachRecyclerview();
        void attachRecyclerview(RecyclerView recyclerView);

        void setOnFloatClickListener(OnFloatClickListener listener);
    }

    public interface OnFloatClickListener {
        void onClick(View v, int position);
    }
    private static int[] completevisables;

    public static int getFirstcompletevisable(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int firstcompletevisable = -1;
        if (layoutManager instanceof LinearLayoutManager) {
            firstcompletevisable = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (completevisables == null) {
                completevisables = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            }
            ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(completevisables);
            firstcompletevisable = completevisables[0];
        }
        return firstcompletevisable;
    }
    public static int getFirstvisable(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int firstvisable = -1;
        if (layoutManager instanceof LinearLayoutManager) {
            firstvisable = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (completevisables == null) {
                completevisables = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            }
            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(completevisables);
            firstvisable = completevisables[0];
        }
        return firstvisable;
    }

    }

