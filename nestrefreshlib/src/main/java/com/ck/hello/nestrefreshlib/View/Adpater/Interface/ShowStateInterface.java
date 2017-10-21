package com.ck.hello.nestrefreshlib.View.Adpater.Interface;


/**
 * Created by ck on 2017/9/10.
 */

public interface ShowStateInterface<E> {

    void showState(int showstate, E e);

    void showEmpty();

    void ShowError();

    void showItem();

    void showLoading();

    void showNomore();
}
