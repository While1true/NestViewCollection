package com.nestrefreshlib.State.Interface;


/**
 * Created by ck on 2017/9/10.
 */

public interface ShowStateInterface<T> {

    void showState(StateEnum showstate, T t);

    void showEmpty();

    void ShowError();

    void showItem();

    void showLoading();

    void showNomore();
}
