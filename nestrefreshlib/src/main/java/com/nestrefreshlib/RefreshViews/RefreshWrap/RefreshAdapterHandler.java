package com.nestrefreshlib.RefreshViews.RefreshWrap;


import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.AdapterHelper.AdapterScrollListener;
import com.nestrefreshlib.RefreshViews.AdapterHelper.Base.BaseHeaderAndFooterAdapterWrap;
import com.nestrefreshlib.RefreshViews.AdapterHelper.RefreshHeaderAndFooterAdapterWrap;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshWrap.Base.RefreshHanderBase;


/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class RefreshAdapterHandler extends RefreshHanderBase {
    private TextView mHeadertextView;
    private ProgressBar mHeaderPrgress;
    private RefreshLayout.State currentState;
    RefreshLayout layout;
    private View header;


    @Override
    public void onPullHeader(View view, int scrolls) {
        super.onPullHeader(view, scrolls);
        if (header != null && scrolls != 0) {
            header.getLayoutParams().height = scrolls;
            header.requestLayout();
        }
        /**
         * 完成状态时不要改变字
         */
        if (currentState == RefreshLayout.State.REFRESHCOMPLETE || currentState == RefreshLayout.State.REFRESHING) {
            return;
        }
        if (mHeadertextView != null) {
            if (mHeadertextView != null && scrolls > getRefreshLayout().getAttrsUtils().getmHeaderRefreshPosition()) {
                mHeadertextView.setText(title[1]);
            } else {
                mHeadertextView.setText(title[0]);
            }
        }

    }

    @Override
    public void onPullFooter(View view, int scrolls) {
        super.onPullFooter(view, scrolls);
        if (layout.getAttrsUtils().getOrentation() == RefreshLayout.Orentation.VERTICAL) {
            layout.scrollTo(0, scrolls);
        } else {
            layout.scrollTo(scrolls, 0);
        }
    }

    @Override
    public void OnStateChange(RefreshLayout.State state) {
        currentState = state;
        switch (state) {
            case REFRESHCOMPLETE:
                if (mHeaderPrgress != null) {
                    mHeaderPrgress.setVisibility(View.INVISIBLE);
                    mHeadertextView.setText(data == null ? title[6] : data);
                }
                break;
            case LOADING:
                break;
            case REFRESHING:
                if (mHeaderPrgress != null) {
                    mHeaderPrgress.setVisibility(View.VISIBLE);
                    mHeadertextView.setText(title[2]);
                }
                break;
            case LOADINGCOMPLETE:
                break;
            case IDEL:
                break;
            case PULL_HEADER:
                break;
            case PULL_FOOTER:
                break;
        }

    }

    public static int dp2px(View view, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, view.getResources().getDisplayMetrics()) + 0.5f);
    }

    @Override
    protected void handleview(RefreshLayout layout, View header, View footer) {
        this.layout = layout;
        View view = layout.getmScroll();
        if (view instanceof RecyclerView) {
            RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
            if (adapter instanceof RefreshHeaderAndFooterAdapterWrap) {
                header = ((RefreshHeaderAndFooterAdapterWrap) adapter).getHeader();
                footer = ((RefreshHeaderAndFooterAdapterWrap) adapter).getFooter();
            } else {
                throw new UnsupportedOperationException("不支持非继承于RefreshHeaderAndFooterWrap 的wrap");
            }

        } else {

            header = layout.findViewById(com.nestrefreshlib.R.id.header);
        }

        if (header != null) {
            layout.getAttrsUtils().setmHeaderRefreshPosition(dp2px(layout, 65));
            header.getLayoutParams().height = 1;
            header.requestLayout();
        }

        if (header != null) {
            mHeadertextView = header.findViewById(R.id.textView);
            mHeaderPrgress = header.findViewById(R.id.progressBar);
        }
        this.header = header;

    }


    public <T extends BaseHeaderAndFooterAdapterWrap>T attachRefreshLayout(RefreshLayout layout, RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager) {
        if (layout.getmScroll() instanceof RecyclerView) {
            View mFooter = layout.getmFooter();
            View mHeader = layout.getmHeader();
            layout.removeView(mHeader);
            layout.removeView(mFooter);
            mHeader = null;
            mFooter = null;

            ((RecyclerView) layout.getmScroll()).addOnScrollListener(new AdapterScrollListener(layout));
            ((RecyclerView) layout.getmScroll()).setLayoutManager(manager);
            if (!(adapter instanceof BaseHeaderAndFooterAdapterWrap)) {
                adapter = new RefreshHeaderAndFooterAdapterWrap(adapter).attachView(layout);
            }
            ((RecyclerView) layout.getmScroll()).setAdapter(adapter);
            layout.setRefreshHandler(this);
        } else {
            throw new UnsupportedOperationException("子view必须是recyclerview才能支持");
        }

        return (T) adapter;
    }
}
