package com.nestrefreshlib.RefreshViews;

/**
 * Created by 不听话的好孩子 on 2018/2/9.
 */

public abstract class RefreshListener extends RefreshLayout.Callback {
    @Override
    public void call(RefreshLayout.State t) {
        if (t == RefreshLayout.State.REFRESHING) {
            Refreshing();
        } else if (t == RefreshLayout.State.LOADING) {
            Loading();
        }
    }

    public abstract void Refreshing();

    public abstract void Loading();

}
