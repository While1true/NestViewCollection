package com.nestrefreshlib.RefreshViews.RefreshWrap.Base;

import android.support.v7.widget.RecyclerView;

import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler;

public  interface RecyclerviewRefreshInterface {
    void setRecyclerviewProperity(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration... decorations);

    RefreshAdapterHandler setInnerRefresh(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration... decorations);

    void setLinearVirtical(RecyclerView.Adapter adapter);

    void setLinearHorizotal(RecyclerView.Adapter adapter);

    void setLinearVirticalWithDecorate(RecyclerView.Adapter adapter);

    void setRecyclerviewProperity(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemAnimator animator, RecyclerView.ItemDecoration... decorations);

    void showNomore();

    void showNomore(boolean nomore, CharSequence sequence);

    void setCanFooter(boolean canFooter);

    void setCanHeader(boolean canFooter);

    void setElasetic();

    void setOverscroll();

    void setOrentation(RefreshLayout.Orentation orentation);

}
