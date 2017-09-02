package com.ck.hello.nestrefreshlib.View.RefreshViews.HeadWrap;

import android.util.TypedValue;

/**
 * Created by ck on 2017/9/2.
 */

public class EmptyRefreshWrap extends RefreshWrapBase {
    public EmptyRefreshWrap(WrapInterface parent, boolean header) {
        super(parent, header);
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public void onPull(int pull) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public int getHeight() {
        return dp2px(50) ;
    }

}
