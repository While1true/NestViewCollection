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
    boolean crashmove = true;
    List<Integer> arrays = new ArrayList<Integer>();
    private RecyclerView.ViewHolder viewHolder;
    //0:正在计算 position 1:可以暂时使用 2 不用计算
    private int type;
    private Thread thread = new Thread(this);
    ;

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
        if (arrays.size() > 0)
            type = 2;
    }

    @Override
    public void detachRecyclerview() {
        try {
            thread.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (type == 2) {
            int one = arrays.get(0);
            viewtype = adapter.getItemViewType(one);
        } else {
            if (adapter.getItemCount() < MAX_PERIOD / 10) {
                thread.run();
            } else {
                /**
                 * 启动线程计算位置
                 */
                thread.start();
            }
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
         * {@link type}
         * 启动子线程，一次计算10万内的type position 分段100个记录一个标记位置用于优化查找
         * 超过计算位置，重启线程计算
         */
        int currentnearestposition = -1;
        if (type == 0) {
            return;
        } else if (type == 2) {
            /**
             * 找到里的最近的viewtype的position
             */
            for (int i = arrays.size() - 1; i >= 0; i--) {
                if (arrays.get(i) < firstcompletevisable) {
                    currentnearestposition = arrays.get(i);
                    break;
                }
            }
        } else {
            if (firstcompletevisable > calculated) {
                /**
                 * 继续计算
                 */
                thread.run();
            }
            int mark = -1;
            int size = marks.size();
            for (int i = 0; i < size; i++) {
                if (marks.valueAt(i) > firstcompletevisable) {
                    mark = marks.keyAt(i);
                    break;
                }
            }
            if (mark == -1) {
                mark = arrays.size() - 1;
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
            View childAt=null;
            for (int i = 0; i < 3; i++) {
                View child = recyclerView.getChildAt(i);
                int childAdapterPosition = recyclerView.getChildAdapterPosition(child);
                if(arrays.contains(childAdapterPosition)&&childAdapterPosition>=getFirstcompletevisable(recyclerView)){
                    childAt=child;
                    break;
                }
            }
            if (childAt != null && firstcompletevisable != -1 && childAt.getTop() <= viewHolder.itemView.getHeight()) {

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

    int MAX_PERIOD = 100000;
    int calculated = -1;
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
        int itemCount = adapter.getItemCount();
        for (; i < end && i < itemCount; i++) {
            calculated = i;
            if (adapter.getItemViewType(i) == viewtype) {
                arrays.add(i);
                if (i / 100 == mark) {
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
