package com.nestrefreshlib.RefreshViews.RefreshWrap.Base;

import android.view.View;

import com.nestrefreshlib.RefreshViews.RefreshLayout;

import java.lang.ref.WeakReference;

/**
 * Created by 不听话的好孩子 on 2018/2/9.
 * base
 */

public class RefreshHanderBase extends RefreshLayout.BaseRefreshHeaderAndFooterHandler<String> {
    protected String[] title;

    @Override
    public void onPullHeader(View view, int scrolls) {

    }

    @Override
    public void onPullFooter(View view, int scrolls) {

    }

    @Override
    public void OnStateChange(RefreshLayout.State state) {

    }

    @Override
    protected void initView(RefreshLayout layout) {
        super.initView(layout);
        View header = layout.getmHeader();
        View footer = layout.getmFooter();
        String[] tempVertical = {"下拉刷新", "释放刷新", "正在刷新中", "上拉加载", "释放加载", "正在加载中", "刷新完成", "加载完成"};
        String[] tempHorizontal = {"右拉刷新", "释放刷新", "正在刷新中", "左拉加载", "释放加载", "正在加载中", "刷新完成", "加载完成"};
        title = (layout.getAttrsUtils().getOrentation() == RefreshLayout.Orentation.VERTICAL) ?
                tempVertical : tempHorizontal;
        handleview(layout, header, footer);
    }

    protected void handleview(RefreshLayout layout, View header, View footer) {
    }
}
