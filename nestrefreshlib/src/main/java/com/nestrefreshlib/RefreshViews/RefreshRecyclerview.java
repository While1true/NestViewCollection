package com.nestrefreshlib.RefreshViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler;

/**
 * Created by 不听话的好孩子 on 2018/5/9.
 */

public class RefreshRecyclerview extends RefreshLayout {
    public RefreshRecyclerview(@NonNull Context context) {
        super(context);
    }

    public RefreshRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RefreshAdapterHandler setInnerRefresh(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration... decorations) {
        if (decorations != null) {
            for (RecyclerView.ItemDecoration decoration : decorations) {
                ((RecyclerView)getmScroll()).addItemDecoration(decoration);
            }
        }
        RefreshAdapterHandler refreshAdapterHandler = new RefreshAdapterHandler();
        refreshAdapterHandler.attachRefreshLayout(this, adapter, manager);
        return refreshAdapterHandler;
    }

    public void setLinearVirtical(RecyclerView.Adapter adapter) {
        setRecyclerviewProperity(adapter, new LinearLayoutManager(getContext()));
    }

    public void setLinearHorizotal(RecyclerView.Adapter adapter) {
        setRecyclerviewProperity(adapter, new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    public void setLinearVirticalWithDecorate(RecyclerView.Adapter adapter) {
        setRecyclerviewProperity(adapter, new LinearLayoutManager(getContext()), new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
    }

    public void setRecyclerviewProperity(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration... decorations) {
        setRecyclerviewProperity(adapter, manager, null, decorations);
    }

    public void setRecyclerviewProperity(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemAnimator animator, RecyclerView.ItemDecoration... decorations) {
        RecyclerView recyclerView = getmScroll();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (animator != null) {
            recyclerView.setItemAnimator(animator);
        }
        if (decorations != null) {
            for (RecyclerView.ItemDecoration decoration : decorations) {
                recyclerView.addItemDecoration(decoration);
            }
        }
    }

}
