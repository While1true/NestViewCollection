package com.ck.hello.nestrefreshlib.View.Adpater.Impliment;

import android.view.View;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.R;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.BaseStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.StateHandlerInterface;
import com.ck.hello.nestrefreshlib.View.Adpater.SLoading;

/**
 * Created by ck on 2017/9/9.
 */

public class DefaultStateHandler implements StateHandlerInterface<String> {
    private DefaultStateListener listener;
    private SLoading sLoading;

    public DefaultStateHandler setStateClickListener(BaseStateListener listener) {
        this.listener = (DefaultStateListener) listener;
        return this;
    }

    @Override
    public BaseStateListener getStateClickListener() {
        return listener;
    }

    @Override
    public void BindEmptyHolder(SimpleViewHolder holder, String s) {

        TextView tv =holder.getView(R.id.tv);
        if (tv != null&&s!=null)
            tv.setText(s);
        if (tv != null && listener != null)
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.showEmpty(v.getContext());
                }
            });
    }

    @Override
    public void BindErrorHolder(SimpleViewHolder holder, String s) {
        View reloadbt = holder.itemView.findViewById(R.id.reload);
        if (reloadbt != null && listener != null)
            reloadbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.netError(v.getContext());
                }
            });
    }

    @Override
    public void BindLoadingHolder(SimpleViewHolder holder, String s) {
        if (sLoading == null) {
            sLoading = holder.getView(R.id.sloading);
        }
        if (sLoading != null)
            sLoading.startAnimator();
    }

    @Override
    public void BindNomoreHolder(SimpleViewHolder holder, String s) {
        if(s!=null)
        ((TextView) holder.getView(R.id.tv_nomore)).setText(s);
    }


    /**
     * 销毁时调用
     */
    public void destory() {
        listener=null;
        if (sLoading != null) {
            sLoading.stopAnimator();
            sLoading = null;
        }
    }

    /**
     * 切换状态时调用，关闭一些动画
     */
    @Override
    public void switchState(int state) {
        if (sLoading != null&&state!=SAdapter.SHOW_LOADING)
            sLoading.stopAnimator();
    }
}
