package com.nestrefreshlib.RefreshViews.RefreshWrap;


import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.AdapterHelper.DefaultRefreshHeaderAndFooterWrap;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.AdapterHelper.Base.AdapterRefreshInterface;
import com.nestrefreshlib.RefreshViews.RefreshWrap.Base.RefreshHanderBase;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 * 调整recyclerview第一个，和最后一个Item的高度
 */

public class RefreshAdapterHandler extends RefreshHanderBase {
    private TextView mHeadertextView;
    private ProgressBar mHeaderPrgress;
    private RefreshLayout.State currentState;
    RefreshLayout layout;
    private View header;
    private View footer;
    private int footerHeight;
    private boolean isvirtical;


    @Override
    public void onPullHeader(View view, int scrolls) {
        super.onPullHeader(view, scrolls);
        resetFooter();
        if (header != null && scrolls != 0) {
            if (isvirtical) {
                header.getLayoutParams().height = scrolls;
            } else {
                header.getLayoutParams().width = scrolls;
            }
            header.requestLayout();
        }

        if (mHeadertextView != null) {
            if (mHeadertextView != null && scrolls > layout.getAttrsUtils().getmHeaderRefreshPosition()) {
                mHeadertextView.setText(title[1]);
            } else {
                mHeadertextView.setText(title[0]);
            }
        }

    }

    @Override
    public void onPullFooter(View view, int scrolls) {
        super.onPullFooter(view, scrolls);
        restHeader();
        if (!calculatIsFull(layout)) {
            if (isvirtical) {
                layout.scrollTo(0, scrolls);
            } else {
                layout.scrollTo(scrolls, 0);
            }
        } else {
            if (footer != null) {
                if (footerHeight == 0) {
                    footerHeight = isvirtical ? footer.getLayoutParams().height : footer.getLayoutParams().width;
                }
                if (isvirtical) {
                    footer.getLayoutParams().height = footerHeight + scrolls;
                } else {
                    footer.getLayoutParams().width = footerHeight + scrolls;
                }
                footer.requestLayout();
            }
        }
    }

    private void restHeader() {
        if (header == null)
            return;
        ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
        int height = isvirtical ? layoutParams.height : layoutParams.width;
        if (height != 1) {
            if (isvirtical) {
                layoutParams.height = 1;
            } else {
                layoutParams.width = 1;
            }
        }
    }

    private void resetFooter() {
        if (footer == null)
            return;
        ViewGroup.LayoutParams layoutParams = footer.getLayoutParams();
        if (footerHeight == 0) {
            footerHeight = isvirtical ? layoutParams.height : layoutParams.width;
        }
        int height = isvirtical ? layoutParams.height : layoutParams.width;
        if (height != footerHeight) {
            if (isvirtical) {
                layoutParams.height = footerHeight;
            } else {
                layoutParams.width = footerHeight;
            }
        }
    }

    public boolean calculatIsFull(RefreshLayout layout) {
        if (footer != null) {
            return isvirtical ? (footer.getBottom() >= layout.getHeight()) : (footer.getRight() >= layout.getLeft());
        }
        return true;
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
            if (adapter instanceof AdapterRefreshInterface) {
                header = ((AdapterRefreshInterface) adapter).getHeader();
                footer = ((AdapterRefreshInterface) adapter).getFooter();
            } else {
                throw new UnsupportedOperationException("不支持非继承于AdapterRefreshInterface 的wrap");
            }

        } else {

            header = layout.findViewById(R.id.header);
            footer = layout.findViewById(R.id.footer);
        }

        if (header != null) {
            layout.getAttrsUtils().setmHeaderRefreshPosition(dp2px(layout, 65));
            if (layout.getAttrsUtils().getOrentation() == RefreshLayout.Orentation.VERTICAL) {
                header.getLayoutParams().height = 1;
            } else {
                header.getLayoutParams().width = 1;
            }
            header.requestLayout();
        }

        if (header != null) {
            mHeadertextView = header.findViewById(R.id.textView);
            mHeaderPrgress = header.findViewById(R.id.progressBar);
        }
        this.header = header;
        this.footer = footer;
        isvirtical = layout.getAttrsUtils().getOrentation() == RefreshLayout.Orentation.VERTICAL;
    }

    @Override
    protected boolean handleAdapter() {
        return true;
    }

    public void attachRefreshLayout(RefreshLayout layout, RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager) {
        if (layout.getmScroll() instanceof RecyclerView) {
            View mFooter = layout.getmFooter();
            View mHeader = layout.getmHeader();
            layout.removeView(mHeader);
            layout.removeView(mFooter);

            RecyclerView recyclerView = (RecyclerView) layout.getmScroll();
            recyclerView.setLayoutManager(manager);
            if (!(adapter instanceof AdapterRefreshInterface)) {
                adapter = new DefaultRefreshHeaderAndFooterWrap(adapter).attachDefaultHeaderFooterView(layout);
            }
            recyclerView.setAdapter(adapter);
            layout.setRefreshHandler(this);
        } else {
            throw new UnsupportedOperationException("子view必须是recyclerview才能支持");
        }
    }

    public void startLoading(CharSequence charSequence) {
        TextView inFooterView = layout.findInFooterView(R.id.textView);
        ProgressBar progressBar = layout.findInFooterView(R.id.progressBar);
        if (inFooterView != null) {
            inFooterView.setText(charSequence);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stopLoading(CharSequence charSequence) {
        TextView inFooterView = layout.findInFooterView(R.id.textView);
        ProgressBar progressBar = layout.findInFooterView(R.id.progressBar);
        if (inFooterView != null) {
            inFooterView.setText(charSequence);
        }
        progressBar.setVisibility(View.GONE);
    }
}