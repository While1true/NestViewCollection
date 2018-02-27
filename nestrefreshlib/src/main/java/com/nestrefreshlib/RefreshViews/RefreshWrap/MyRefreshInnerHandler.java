package com.nestrefreshlib.RefreshViews.RefreshWrap;


import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshWrap.Base.RefreshInnerHandlerImpl;


/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class MyRefreshInnerHandler extends RefreshInnerHandlerImpl {
    private TextView mHeadertextView;
    private ProgressBar mHeaderPrgress;
    private TextView mfootertextView;
    private ProgressBar mfootPrgress;
    private RefreshLayout.State currentState;

    @Override
    public void onPullHeader(View view, int scrolls) {
       super.onPullHeader(view,scrolls);
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
     super.onPullFooter(view,scrolls);

        /**
         * 完成状态时不要改变字
         */
        if (currentState == RefreshLayout.State.LOADINGCOMPLETE || currentState == RefreshLayout.State.LOADING) {
            return;
        }
        if (mfootertextView != null) {
            if (mfootertextView != null && scrolls > getRefreshLayout().getAttrsUtils().getmFooterRefreshPosition()) {
                mfootertextView.setText(title[4]);
            } else {
                mfootertextView.setText(title[3]);
            }
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
    protected void handleview(RefreshLayout layout, View header, View footer) {
        super.handleview(layout,header,footer);
        if (this.header != null) {
            mHeadertextView = this.header.findViewById(R.id.textView);
            mHeaderPrgress = this.header.findViewById(R.id.progressBar);
        }

        if (this.footer != null) {
            mfootertextView = this.footer.findViewById(R.id.textView);
            mfootPrgress = this.footer.findViewById(R.id.progressBar);
        }
    }


}
