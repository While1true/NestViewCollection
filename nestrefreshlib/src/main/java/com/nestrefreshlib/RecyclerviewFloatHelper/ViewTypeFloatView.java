package com.nestrefreshlib.RecyclerviewFloatHelper;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 适用于单item
 * Created by 不听话的好孩子 on 2018/4/27.
 */
public class ViewTypeFloatView extends RecyclerView.OnScrollListener implements RecyclerviewFloatHelper.FloatInterface, Runnable {
    RecyclerView.Adapter adapter;
    RecyclerviewFloatHelper.OnFloatClickListener listener;
    ViewGroup container;
    RecyclerView recyclerView;
    int viewtype = -1;
    int currentfloatposition = -1;
    int rollbackSize = 5;
    boolean crashmove = true;
    List<Integer> arrays = new ArrayList<Integer>();
    private RecyclerView.ViewHolder viewHolder;
    private int firstvisable;
    //0:正在获取position 1:可以暂时使用
    private int type;
    private int itemCount;

    public void setOnFloatClickListener(RecyclerviewFloatHelper.OnFloatClickListener listener) {
        this.listener = listener;
    }

    public void setCrashmove(boolean crashmove) {
        this.crashmove = crashmove;
    }

    public ViewTypeFloatView(ViewGroup container, int viewtype) {
        this.viewtype = viewtype;
        this.container = container;
    }

    public ViewTypeFloatView(ViewGroup container, int... floatposition) {
        Arrays.sort(floatposition);
        this.container = container;
        for (int i : floatposition) {
            arrays.add(i);
        }
        type = 1;
    }

    public ViewTypeFloatView(ViewGroup container, int viewtype, int rollbackSize) {
        this.viewtype = viewtype;
        this.container = container;
        this.rollbackSize = rollbackSize;
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
    public void attachRecyclerview(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(this);
        adapter = recyclerView.getAdapter();
        itemCount = adapter.getItemCount();

        if (type == 0) {
            /**
             * 启动线程计算位置
             */
            new Thread(this).start();
        } else {
            firstvisable = arrays.get(0);
            viewtype = adapter.getItemViewType(firstvisable);
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
         * {@link type=0 定了itemtype 1 定了position}
         * 启动子线程，一次计算100万内的type position 分段1000个记录一个标记位置用于优化查找
         * 超过计算位置，重启线程计算
         */
        int currentnearestposition = -1;
        if (type == 0) {
            return;
        } else {
            if (firstcompletevisable > arrays.get(arrays.size() - 1)) {
                /**
                 * 继续计算
                 */
                new Thread(this).start();
                type = 0;
                return;
            }
            int mark = arrays.size() - 1;
            int size = marks.size();
            for (int i = 0; i < size; i++) {
                if (marks.valueAt(i) > firstcompletevisable) {
                    mark = marks.keyAt(i);
                    break;
                }
            }
            for (int i1 = mark; i1 >= 0; i1--) {
                Integer integer = arrays.get(i1);
                if (integer < firstcompletevisable) {
                    currentnearestposition = integer;
                    break;
                }
            }
        }


        /**
         * 如果没有要悬浮的了，并且有悬浮过就隐藏掉
         */
        if (currentnearestposition == -1 && viewHolder != null) {
            viewHolder.itemView.setVisibility(View.GONE);
            currentfloatposition = -1;
            return;
        }


        /**
         * {@link crashmove}从一个悬浮view过渡到另一个的移动动画
         */
        if (crashmove && viewHolder != null) {
            View childAt = recyclerView.getChildAt(firstcompletevisable == getFirstvisable(recyclerView) ? 0 : 1);
            if (firstcompletevisable != -1 && adapter.getItemViewType(firstcompletevisable) == viewtype && childAt.getTop() <= viewHolder.itemView.getHeight()) {

                int height = viewHolder.itemView.getHeight();
                int top = height - childAt.getTop();
                if (top >= 0 && top <= height) {
                    viewHolder.itemView.setTranslationY(-top);
                }
            } else {
                viewHolder.itemView.setTranslationY(0);
            }
        }

        /**
         * 如果当前悬浮和需要悬浮的相等，不必做改动
         */
        if (currentnearestposition == currentfloatposition) {
            return;
        }

        if (currentfloatposition == firstcompletevisable && firstcompletevisable == getFirstvisable(recyclerView)) {
            return;
        }

        /**
         * 如果没有创建过，创建
         * 创建过，绑定新的数据
         */
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

    public int getFirstcompletevisable(RecyclerView recyclerView) {
        return RecyclerviewFloatHelper.getFirstcompletevisable(recyclerView);
    }

    public int getFirstvisable(RecyclerView recyclerView) {
        return RecyclerviewFloatHelper.getFirstvisable(recyclerView);
    }

    int MAX_PERIOD = 1000000;
    SparseArray<Integer> marks = new SparseArray<>();
    int mark = 0;

    @Override
    public void run() {
        System.out.println("所有type位置开始部分获取");
        int size = arrays.size();
        int i = 0;
        if (size > 0) {
            i = arrays.get(size - 1);
        }
        int end = MAX_PERIOD + i;

        for (; i < end && i < itemCount; i++) {
            if (adapter.getItemViewType(i) == viewtype) {
                arrays.add(i);
                if (i / 1000 == mark) {
                    marks.put(arrays.size() - 1, i);
                    mark++;
                }
            }
        }
        System.out.println("共" + mark + "个标记位置");
        System.out.println("所有type位置获取完毕");
        type = 1;
    }
}
