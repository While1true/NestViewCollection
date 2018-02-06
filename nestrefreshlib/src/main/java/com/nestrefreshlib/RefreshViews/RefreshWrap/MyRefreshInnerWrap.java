package com.nestrefreshlib.RefreshViews.RefreshWrap;


import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.AdapterHelper.RefreshHeaderAndFooterWrap;
import com.nestrefreshlib.RefreshViews.RefreshLayout;

import java.lang.ref.WeakReference;


/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class MyRefreshInnerWrap extends RefreshLayout.BaseRefreshWrap<String> {
    View header;
    View footer;
    private TextView mHeadertextView;
    private ProgressBar mHeaderPrgress;
    private TextView mfootertextView;
    private ProgressBar mfootPrgress;
    private WeakReference<RefreshLayout> layoutWeakReference;

    public RefreshLayout getRefreshLayout() {
        return layoutWeakReference.get();
    }

    private RefreshLayout.State currentState;
    String[] title;

    @Override
    public void onPullHeader(View view, int scrolls) {
        if (header != null) {
            if (scrolls == 0)
                scrolls = 1;
            System.out.println(scrolls);
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
            if (mHeadertextView != null && scrolls > getRefreshLayout().getmHeaderRefreshPosition()) {
                mHeadertextView.setText(title[1]);
            } else {
                mHeadertextView.setText(title[0]);
            }
        }
    }

    @Override
    public void onPullFooter(View view, int scrolls) {
        if (footer != null) {
            if (scrolls == 0)
                scrolls = 1;
            System.out.println(scrolls + " onPullFooter");
            footer.getLayoutParams().height = scrolls;
            footer.requestLayout();
        }

        /**
         * 完成状态时不要改变字
         */
        if (currentState == RefreshLayout.State.LOADINGCOMPLETE || currentState == RefreshLayout.State.LOADING) {
            return;
        }
        if (mfootertextView != null) {
            if (mfootertextView != null && scrolls > getRefreshLayout().getmFooterRefreshPosition()) {
                mfootertextView.setText(title[4]);
            } else {
                mfootertextView.setText(title[3]);
            }
        }
    }

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
                if (mfootPrgress != null) {
                    mfootPrgress.setVisibility(View.VISIBLE);
                    mfootertextView.setText(title[5]);
                }
                break;
            case REFRESHING:
                if (mHeaderPrgress != null) {
                    mHeaderPrgress.setVisibility(View.VISIBLE);
                    mHeadertextView.setText(title[2]);
                }
                break;
            case LOADINGCOMPLETE:
                if (mfootPrgress != null) {
                    mfootPrgress.setVisibility(View.INVISIBLE);
                    mfootertextView.setText(data == null ? title[7] : data);
                }
                break;
            case IDEL:
                break;
            case PULL_HEADER:
                break;
            case PULL_FOOTER:
                break;
        }

    }

    @Override
    protected void initView(RefreshLayout layout) {
        super.initView(layout);
        layoutWeakReference = new WeakReference<RefreshLayout>(layout);
        View view = layout.getmScroll();
        if (view instanceof RecyclerView) {
            RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
            if (adapter instanceof RefreshHeaderAndFooterWrap) {
                header = ((RefreshHeaderAndFooterWrap) adapter).getHeader();
                footer = ((RefreshHeaderAndFooterWrap) adapter).getFooter();
            } else {
                throw new UnsupportedOperationException("不支持非继承于RefreshHeaderAndFooterWrap 的wrap");
            }

        } else {

            header = layout.findViewById(R.id.header);
            footer = layout.findViewById(R.id.footer);
        }
        handleview(layout, header, footer);
    }

    public static int dp2px(View view, float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, view.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static void setInnerRecyclerviewAdapter(RefreshLayout refreshLayout, RecyclerView.Adapter adapter) {
        refreshLayout.setRefreshWrap(new MyRefreshInnerWrap());
        RecyclerView recyclerView = refreshLayout.getmScroll();
        recyclerView.setAdapter(new RefreshHeaderAndFooterWrap(adapter).attachView(recyclerView));
    }

    private void handleview(RefreshLayout layout, View header, View footer) {
        if (header != null) {
            layout.setmHeaderRefreshPosition(dp2px(layout, 65));
            header.getLayoutParams().height = 1;
            header.requestLayout();
            mHeadertextView = header.findViewById(R.id.textView);
            mHeaderPrgress = header.findViewById(R.id.progressBar);
        }

        if (footer != null) {
            layout.setmFooterRefreshPosition(dp2px(layout, 50));
            footer.getLayoutParams().height = 1;
            footer.requestLayout();
            mfootertextView = footer.findViewById(R.id.textView);
            mfootPrgress = footer.findViewById(R.id.progressBar);
        }
        String[] tempVertical = {"下拉刷新", "释放刷新", "正在刷新中", "上拉加载", "释放加载", "正在加载中", "刷新完成", "加载完成"};
        String[] tempHorizontal = {"右拉刷新", "释放刷新", "正在刷新中", "左拉加载", "释放加载", "正在加载中", "刷新完成", "加载完成"};
        title = (layout.getOrentation() == RefreshLayout.Orentation.VERTICAL) ?
                tempVertical : tempHorizontal;
    }

    @Override
    public boolean isOutHeaderAndFooter() {
        return false;
    }
}
