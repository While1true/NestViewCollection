package com.nestrefreshlib.State;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.nestrefreshlib.State.Interface.StateEnum;
import com.nestrefreshlib.State.Interface.BaseStateListener;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.State.Interface.StateHandlerInterface;
import com.nestrefreshlib.SLoading;
import com.nestrefreshlib.R;

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
    public void BindEmptyHolder(Holder holder, String s) {

        TextView tv = holder.getView(R.id.tv);
        if (tv != null && s != null)
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
    public void BindErrorHolder(Holder holder, String s) {
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
    public void BindLoadingHolder(Holder holder, String s) {
        sLoading = holder.getView(R.id.sloading);
        if (sLoading != null)
            sLoading.startAnimator();
    }

    @Override
    public void BindNomoreHolder(final Holder holder, String s) {
        if (s != null) {
            ((TextView) holder.getView(R.id.tv_nomore)).setText(s);
        }


    }


    public class DecelerateAccelerateInterpolator implements Interpolator {
        public DecelerateAccelerateInterpolator() {
        }

        public float getInterpolation(float t) {
            float x = 2.0f * t - 1.0f;
            return 0.5f * (x * x * x + 1.0f);
        }
    }

    /**
     * 销毁时调用
     */
    public void destory() {
        listener = null;
        if (sLoading != null) {
            sLoading.stopAnimator();
            sLoading = null;
        }
    }

    /**
     * 切换状态时调用，关闭一些动画
     */
    @Override
    public void switchState(StateEnum state) {
        if (sLoading != null && state != StateEnum.SHOW_LOADING)
            sLoading.stopAnimator();
    }
}
