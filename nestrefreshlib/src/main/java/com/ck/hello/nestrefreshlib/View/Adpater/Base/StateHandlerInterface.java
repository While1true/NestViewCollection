package com.ck.hello.nestrefreshlib.View.Adpater.Base;

/**
 * Created by ck on 2017/9/9.
 */

public interface StateHandlerInterface<T> {
    StateHandlerInterface setStateClickListener(BaseStateListener listener);
    BaseStateListener getStateClickListener();
    void BindEmptyHolder(SimpleViewHolder holder,T t);
    void BindErrorHolder(SimpleViewHolder holder,T t);
    void BindLoadingHolder(SimpleViewHolder holder,T t);
    void BindNomoreHolder(SimpleViewHolder holder,T t);
    void destory();

    void switchState();
}
