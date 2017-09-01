package com.ck.hello.nestrefreshlib.View.RefreshViews.HeadWrap;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by vange on 2017/9/1.
 */

public abstract class RefreshWrapBase {
    protected View viewLayout;

    private WrapInterface parent;

    protected int height;

    public RefreshWrapBase(WrapInterface parent){
        this.parent=parent;
        viewLayout = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent.getParentView(), false);
    }
    public abstract int getLayout();

    /**
     * 拉动过程
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
    public void OnDetachFromWindow(){
        viewLayout=null;
        parent=null;
        parent=null;
    }



    /**
     * 头跟布局
     * @param
     * @return
     */
    protected LinearLayout getHeaderWrapParent(){
        return parent.getHeaderLayout();
    }

    /**
     * 尾跟布局
     * @param
     * @return
     */
    protected LinearLayout getfooterWrapParent(){
        return parent.getFootLayout();
    }
}
