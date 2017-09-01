package com.ck.hello.nestrefreshlib.View.RefreshViews.HeadWrap;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.R;

/**
 * Created by vange on 2017/9/1.
 */

public class DefaultRefreshWrap extends RefreshWrapBase {
    private static String[] pulldown = {"下拉刷新", "释放刷新", "正在刷新", "刷新完成"};
    private static String[] pullup = {"上拉加载", "释放加载", "正在加载", "加载完成"};

    ImageView progress;
    TextView headTitle;


    private boolean header;
    private RotateAnimation animation;

    /**
     * 构造调用intViews初始化组件
     * @param parent
     * @param header
     */
    public DefaultRefreshWrap(WrapInterface parent, boolean header) {
        super(parent);
        this.header = header;
        initViews();
    }

    @Override
    public int getLayout() {
        return R.layout.pull_to_refreshlayout;
    }

    @Override
    public void onPull(int pull) {
        if (Math.abs(pull) >= getHeight()) {
            String headerstr = this.header ? pulldown[1] : pullup[1];
            if (headerstr != headTitle.getText().toString())
                headTitle.setText(headerstr);
        } else {
            String footstr = header ? pulldown[0] : pullup[0];
            if (footstr != headTitle.getText().toString())
                headTitle.setText(footstr);
        }
        progress.setRotation(pull);
    }

    @Override
    public void onRefresh() {
        startRotate();
        headTitle.setText(header ? pulldown[2] : pullup[2]);

    }

    @Override
    public void onComplete() {
        if (animation != null)
            animation.cancel();
        headTitle.setText(header ? pulldown[3] : pullup[3]);
    }

    @Override
    public void initViews() {
        LinearLayout wrapParent = header ? getHeaderWrapParent() : getfooterWrapParent();
        progress = (ImageView) viewLayout.findViewById(R.id.pull_to_refresh_image);
        headTitle = (TextView) viewLayout.findViewById(R.id.pull_to_refresh_text);
        String title = header ? pulldown[0] : pullup[0];
        if (title != headTitle.getText().toString())
            headTitle.setText(title);

        wrapParent.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight());
        params.gravity = Gravity.CENTER_VERTICAL;
        wrapParent.setOrientation(LinearLayout.HORIZONTAL);

        if (header)
            params.topMargin = -getHeight();

        wrapParent.setLayoutParams(params);
        wrapParent.addView(viewLayout);
    }

    @Override
    public int getHeight() {
        if (height == 0)
            height = dp2px(55);
        return height;
    }

    @Override
    public void OnDetachFromWindow() {
        progress = null;
        headTitle = null;
        animation.cancel();
        animation = null;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, viewLayout.getResources().getDisplayMetrics());
    }

    /**
     * 下拉progress的动画
     */
    private void startRotate() {
        if (animation == null) {
            animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(430);
            animation.setRepeatCount(-1);
        } else {
            animation.cancel();
        }

        progress.startAnimation(animation);
    }
}
