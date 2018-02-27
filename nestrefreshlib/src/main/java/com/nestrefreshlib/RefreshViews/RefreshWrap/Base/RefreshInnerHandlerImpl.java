package com.nestrefreshlib.RefreshViews.RefreshWrap.Base;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.nestrefreshlib.RefreshViews.AdapterHelper.RefreshHeaderAndFooterAdapterWrap;
import com.nestrefreshlib.RefreshViews.RefreshLayout;

/**
 * Created by 不听话的好孩子 on 2018/2/9.
 */

public class RefreshInnerHandlerImpl extends RefreshOuterHanderImpl {
    protected View header;
    protected View footer;
    @Override
    public void onPullHeader(View view, int scrolls) {
        if (header != null) {
            if (scrolls == 0)
                scrolls = 1;
            System.out.println(scrolls);
            header.getLayoutParams().height = scrolls;
            header.requestLayout();
        }
    }

    @Override
    public void onPullFooter(View view, int scrolls) {
        if (footer != null) {
            if (scrolls == 0)
                scrolls = 1;
            footer.getLayoutParams().height = scrolls;
            footer.requestLayout();
        }
    }

    public static int dp2px(View view, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, view.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static void setInnerRecyclerviewAdapter(RefreshLayout refreshLayout, RefreshLayout.BaseRefreshHeaderAndFooterHandler handler, RecyclerView.Adapter adapter) {
        refreshLayout.setRefreshWrap(handler);
        RecyclerView recyclerView = refreshLayout.getmScroll();
        if (adapter instanceof RefreshHeaderAndFooterAdapterWrap)
            recyclerView.setAdapter(adapter);
        else
            recyclerView.setAdapter(new RefreshHeaderAndFooterAdapterWrap(adapter).attachView(recyclerView));
    }

    @Override
    protected void handleview(RefreshLayout layout, View header, View footer) {
        View view = layout.getmScroll();
        if (view instanceof RecyclerView) {
            RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
            if (adapter instanceof RefreshHeaderAndFooterAdapterWrap) {
                this.header = ((RefreshHeaderAndFooterAdapterWrap) adapter).getHeader();
                this.footer = ((RefreshHeaderAndFooterAdapterWrap) adapter).getFooter();
            } else {
                throw new UnsupportedOperationException("不支持非继承于RefreshHeaderAndFooterWrap 的wrap");
            }

        } else {

            this.header = layout.findViewById(com.nestrefreshlib.R.id.header);
            this.footer = layout.findViewById(com.nestrefreshlib.R.id.footer);
        }

        if (this.header != null) {
            layout.getAttrsUtils().setmHeaderRefreshPosition(dp2px(layout, 65));
            this.header.getLayoutParams().height = 1;
            this.header.requestLayout();
        }

        if (this.footer != null) {
            layout.getAttrsUtils().setmFooterRefreshPosition(dp2px(layout, 50));
            this.footer.getLayoutParams().height = 1;
            this.footer.requestLayout();
        }
        header=this.header;
        footer=this.footer;

    }

    @Override
    public boolean isOutHeaderAndFooter() {
        return false;
    }

}
