package com.ck.hello.nestrefreshlib.View.RefreshViews.HeadWrap;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by vange on 2017/9/1.
 */

public abstract class RefreshWrapBase {
    //头尾布局View
    protected View viewLayout;

    //获取父布局头尾布局等
    private WrapInterface parent;

    //是头布局还是尾布局
    protected boolean header;

    public RefreshWrapBase(WrapInterface parent, boolean header) {
        this.header = header;
        this.parent = parent;
        viewLayout = LayoutInflater.from(parent.getContext()).inflate(getLayout(), header?parent.getHeaderLayout():parent.getFootLayout(), false);
        initViews();
        addRefreshtoParent();
    }

    public abstract int getLayout();

    /**
     * 拉动过程
     *
     * @param pull
     */
    public abstract void onPull(int pull);

    /**
     * 处于刷新状态下调用
     */
    public abstract void onRefresh();

    /**
     * 刷新完整调用
     */
    public abstract void onComplete();

    /**
     * 进行一些组件的初始化
     */
    public abstract void initViews();

    /**
     * 高度
     * @return
     */
    public abstract int getHeight();
    /**
     * 做一些销毁操作
     */
    public void OnDetachFromWindow() {
        viewLayout = null;
        parent = null;
        parent = null;
    }


    /**
     * 将布局添加到父ViewGroup
     *
     * @param
     */
    protected void addRefreshtoParent() {
        final LinearLayout wrapParent = header ? getHeaderWrapParent() : getfooterWrapParent();
        wrapParent.removeAllViews();
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getHeight());
        params.gravity = Gravity.CENTER_VERTICAL;
        wrapParent.setOrientation(LinearLayout.HORIZONTAL);
        if (header) {
            params.topMargin = -getHeight();
        }
        wrapParent.setLayoutParams(params);
        wrapParent.addView(viewLayout);
    }


    /**
     * 头跟布局
     *
     * @param
     * @return
     */
    protected LinearLayout getHeaderWrapParent() {
        return parent.getHeaderLayout();
    }

    /**
     * 尾跟布局
     *
     * @param
     * @return
     */
    protected LinearLayout getfooterWrapParent() {
        return parent.getFootLayout();
    }
}
