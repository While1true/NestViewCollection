package com.nestrefreshlib.RefreshViews.RefreshWrap;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshWrap.Base.RefreshHanderBase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 不听话的好孩子 on 2018/5/9.
 */

public class RefreshHandlerImpl extends RefreshHanderBase implements Runnable {
    private TextView refreshText;
    private TextView refreshTime;
    private ImageView icon;
    private RefreshLayout.State currentState;
    private TextView mfootertextView;
    private ProgressBar mfootPrgress;
    RefreshLayout layout;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private boolean canswitch = true;

    public RefreshHandlerImpl() {
    }

    public void onPullHeader(View view, int scrolls) {
        if (this.refreshText != null) {
            if (this.refreshText != null && scrolls > layout.getAttrsUtils().getmHeaderRefreshPosition()) {
                this.refreshText.setText(this.title[1]);
                if (canswitch && this.icon.getRotation() != 180.0F) {
                    canswitch = false;
                    this.icon.animate().rotation(180.0F).withEndAction(this).start();
                }
            } else {
                this.refreshText.setText(this.title[0]);
                if (canswitch && this.icon.getRotation() != 0.0F) {
                    canswitch = false;
                    this.icon.animate().rotation(0.0F).withEndAction(this).start();
                }
            }
        }

    }

    public void onPullFooter(View view, int scrolls) {
        if (this.mfootertextView != null) {
            if (this.mfootertextView != null && scrolls > layout.getAttrsUtils().getmFooterRefreshPosition()) {
                this.mfootertextView.setText(this.title[4]);
            } else {
                this.mfootertextView.setText(this.title[3]);
            }
        }

    }

    public void OnStateChange(RefreshLayout.State state) {
        this.currentState = state;
        switch (state) {
            case REFRESHCOMPLETE:
                if (this.refreshText != null) {
                    this.refreshText.setText(this.data == null ? this.title[6] : (String) this.data);
                    this.refreshTime.setText(this.getCurrentTime(this.refreshText.getContext()));
                }
                break;
            case LOADING:
                if (mfootPrgress != null) {
                    mfootPrgress.setVisibility(View.VISIBLE);
                    mfootertextView.setText(title[5]);
                }
                break;
            case REFRESHING:
                if (this.refreshText != null) {
                    this.refreshText.setVisibility(View.VISIBLE);
                    this.refreshText.setText(this.title[2]);
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

    protected void handleview(RefreshLayout layout, View header, View footer) {
        this.layout = layout;
        if (header != null) {
            this.refreshText = (TextView) header.findViewById(com.nestrefreshlib.R.id.refreshText);
            this.icon = (ImageView) header.findViewById(com.nestrefreshlib.R.id.icon);
            this.refreshTime = (TextView) header.findViewById(com.nestrefreshlib.R.id.refreshTime);
            if (this.refreshTime != null) {
                String simpleName = this.refreshTime.getContext().getClass().getSimpleName();
                String timelast = this.refreshTime.getContext().getSharedPreferences("RefreshHandlerImpl", 0).getString(simpleName, "");
                this.refreshTime.setText(timelast);
            }
        }

        if (footer != null) {
            this.mfootertextView = (TextView) footer.findViewById(com.nestrefreshlib.R.id.textView);
            this.mfootPrgress = (ProgressBar) footer.findViewById(com.nestrefreshlib.R.id.progressBar);
        }

    }

    public String getCurrentTime(Context context) {
        String time = "上次刷新时间：" + this.simpleDateFormat.format(new Date());
        String simpleName = context.getClass().getSimpleName();
        context.getSharedPreferences("RefreshHandlerImpl", 0).edit().putString(simpleName, time).commit();
        return time;
    }

    @Override
    public void run() {
        canswitch = true;
    }
}
