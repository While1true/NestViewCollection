package com.nestrefreshlib.RefreshViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.nestrefreshlib.R;
import com.nestrefreshlib.RefreshViews.AdapterHelper.Base.AdapterRefreshInterface;
import com.nestrefreshlib.RefreshViews.RefreshWrap.Base.RefreshHanderBase;

import static java.lang.Math.signum;


/**
 * EVALUATEABLE -> overscroll 非overscroll
 * <p>
 * elastic_overscroll
 * <p>
 * overscroll
 * <p>
 * refresh
 * <p>
 * Created by 不听话的好孩子 on 2017/12/14.
 * <p>
 * 如果在代码中配置了 canheader canfooter=false 那么该布局不会被加载，再次设置也不会生效
 * 若果默认配置都是true，oncreat后再代码中都生效  默认false时不加载布局是为了神曲一些不必要的加载布局
 */

public class RefreshLayout extends FrameLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener, Runnable {
    public static final String TAG = "RefreshLayout";
    private NestedScrollingParentHelper helper;

    private View mHeader, mFooter, mScroll;

    /**
     * 滑动值
     */
    private int scrolls = 0;

    /**
     * 刷新状态
     */
    State state = State.IDEL;

    private ValueAnimator valueAnimatorHeader;
    private ValueAnimator valueAnimatorfooter;

    /**
     * 属性解析 保存类
     */
    private AttrsUtils attrsUtils;

    private boolean canhandle = true;

    /**
     * 方向
     */
    public enum Orentation {
        HORIZONTAL, VERTICAL
    }

    /**
     * 刷新状态
     * 正在刷新
     * 正在加载
     * 拉头部
     * 拉尾部
     * 闲置
     * 刷新完成位置
     * 加载完成位置
     */
    public enum State {
        REFRESHING, LOADING, REFRESHCOMPLETE, LOADINGCOMPLETE, PULL_HEADER, PULL_FOOTER, IDEL
    }

    public RefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrsUtils = new AttrsUtils();
        attrsUtils.ParseAttrs(context, attrs);
        try {
            if (baseRefreshHandler == null) {
                baseRefreshHandler = (BaseRefreshHeaderAndFooterHandler) AttrsUtils.builder.defaultRefreshHandler.newInstance();
            }
            if (baseRefreshHandler.handleAdapter()) {
                attrsUtils.EVALUATEABLE = true;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView(getContext());
    }

    private void initView(Context context) {
        helper = new NestedScrollingParentHelper(this);
        ViewCompat.setNestedScrollingEnabled(this, true);
        LayoutInflater inflater = LayoutInflater.from(context);
        if (getChildCount() > 0)
            mScroll = getChildAt(0);
        else {
            mScroll = inflater.inflate(attrsUtils.SCROLL_LAYOUT_ID, this, false);
            addView(mScroll);
        }
        if (!attrsUtils.OVERSCROLL && !attrsUtils.EVALUATEABLE) {
            if (attrsUtils.CANHEADER) {
                mHeader = inflater.inflate(attrsUtils.HEADER_LAYOUTID, this, false);
                addView(mHeader);
            }
            if (attrsUtils.CANFOOTR) {
                mFooter = inflater.inflate(attrsUtils.FOOTER_LAYOUTID, this, false);
                addView(mFooter);
            }
        }

        initAnimator();

    }

    private void initAnimator() {
        valueAnimatorHeader = ValueAnimator.ofInt();
        valueAnimatorHeader.setInterpolator(new DecelerateInterpolator());
        valueAnimatorHeader.addUpdateListener(this);
        valueAnimatorfooter = valueAnimatorHeader.clone();
    }

    public interface KeyBoardChangedListener {
        void changed(boolean visable,int range);
    }

    private KeyBoardChangedListener keyBoardlistener;

    public void setKeyBoardChangedListener(KeyBoardChangedListener keyBoardlistener) {
        this.keyBoardlistener = keyBoardlistener;
    }

    private int layoutbottom = -1;
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(keyBoardlistener!=null) {
            if (bottom - layoutbottom > 100) {
                keyBoardlistener.changed(false, layoutbottom - bottom);
            } else if (bottom - layoutbottom < -100) {
                keyBoardlistener.changed(true, layoutbottom - bottom);
            }
            layoutbottom = bottom;
        }

        if (top == bottom || left == right) {
            return;
        }
        LayoutParams layoutParams = (LayoutParams) mScroll.getLayoutParams();
        right = right - left - getPaddingRight();
        bottom = bottom - top - getPaddingBottom();
        top = getPaddingTop();
        left = getPaddingLeft();
        mScroll.layout(left + layoutParams.leftMargin, top + layoutParams.topMargin, right - layoutParams.rightMargin, bottom - layoutParams.bottomMargin);
        if (attrsUtils.orentation == Orentation.VERTICAL) {
            if (attrsUtils.OVERSCROLL || attrsUtils.EVALUATEABLE) {
                if (attrsUtils.mMaxHeaderScroll == -1) {
                    attrsUtils.mMaxHeaderScroll = getMeasuredHeight() / 2;
                }
                if (attrsUtils.mMaxFooterScroll == -1) {
                    attrsUtils.mMaxFooterScroll = attrsUtils.mMaxHeaderScroll;
                }
                if (attrsUtils.mHeaderRefreshPosition == -1) {
                    attrsUtils.mHeaderRefreshPosition = attrsUtils.mMaxHeaderScroll / 3;
                }
            } else {
                if (mHeader != null) {
                    LayoutParams headerParams = (LayoutParams) mScroll.getLayoutParams();

                    if (attrsUtils.mFlingmax == -1) {
                        attrsUtils.mFlingmax = mHeader.getMeasuredHeight() / 6;
                    }
                    if (attrsUtils.mMaxHeaderScroll == -1) {
                        attrsUtils.mMaxHeaderScroll = 4 * mHeader.getMeasuredHeight();
                    }
                    if (attrsUtils.mHeaderRefreshPosition == -1) {
                        attrsUtils.mHeaderRefreshPosition = mHeader.getMeasuredHeight() + headerParams.bottomMargin;
                    }

                    mHeader.layout(left + headerParams.leftMargin, top - mHeader.getMeasuredHeight() - headerParams.bottomMargin, right - headerParams.rightMargin, top - headerParams.bottomMargin);
                }
                if (mFooter != null) {
                    LayoutParams footerParams = (LayoutParams) mScroll.getLayoutParams();

                    if (attrsUtils.mMaxFooterScroll == -1) {
                        attrsUtils.mMaxFooterScroll = (int) (mFooter.getMeasuredHeight() * 1.5f);
                    }
                    if (attrsUtils.mFooterRefreshPosition == -1) {
                        attrsUtils.mFooterRefreshPosition = mFooter.getMeasuredHeight() + footerParams.bottomMargin;
                    }
                    mFooter.layout(left + footerParams.leftMargin, bottom + footerParams.topMargin, right - footerParams.rightMargin, bottom + mFooter.getMeasuredHeight() + footerParams.topMargin);
                }
            }

        } else {
            if (attrsUtils.OVERSCROLL || attrsUtils.EVALUATEABLE) {
                if (attrsUtils.mMaxHeaderScroll == -1) {
                    attrsUtils.mMaxHeaderScroll = getMeasuredWidth() / 2;
                }
                if (attrsUtils.mMaxFooterScroll == -1) {
                    attrsUtils.mMaxFooterScroll = attrsUtils.mMaxHeaderScroll;
                }
                if (attrsUtils.mHeaderRefreshPosition == -1) {
                    attrsUtils.mHeaderRefreshPosition = attrsUtils.mMaxHeaderScroll / 3;
                }
            } else {
                if (mHeader != null) {
                    LayoutParams headerParams = (LayoutParams) mScroll.getLayoutParams();

                    if (attrsUtils.mFlingmax == -1) {
                        attrsUtils.mFlingmax = mHeader.getMeasuredWidth() / 6;
                    }
                    if (attrsUtils.mMaxHeaderScroll == -1) {
                        attrsUtils.mMaxHeaderScroll = 4 * mHeader.getMeasuredWidth();
                    }
                    if (attrsUtils.mHeaderRefreshPosition == -1) {
                        attrsUtils.mHeaderRefreshPosition = mHeader.getMeasuredWidth() + headerParams.leftMargin;
                    }
                    mHeader.layout(left - mHeader.getMeasuredWidth() - headerParams.rightMargin, top + headerParams.topMargin, left - layoutParams.leftMargin - headerParams.rightMargin, bottom - headerParams.bottomMargin);
                }
                if (mFooter != null) {
                    LayoutParams footerParams = (LayoutParams) mScroll.getLayoutParams();

                    if (attrsUtils.mMaxFooterScroll == -1) {
                        attrsUtils.mMaxFooterScroll = (int) (mFooter.getMeasuredWidth() * 1.5f);
                    }
                    if (attrsUtils.mFooterRefreshPosition == -1) {
                        attrsUtils.mFooterRefreshPosition = mFooter.getMeasuredWidth() + footerParams.leftMargin;
                    }
                    mFooter.layout(right + footerParams.leftMargin, top + footerParams.topMargin, right + mFooter.getMeasuredWidth() + footerParams.leftMargin, bottom - footerParams.bottomMargin);
                }
            }

        }
        if (changed) {
            baseRefreshHandler.initView(this);
            baseRefreshHandler.isinit = true;
        }

    }

    private void aninatorTo(int from, int to) {
        if (from == to) {
            return;
        }
        ValueAnimator valueAnimator = getValueAnimator();
        valueAnimator.cancel();
        valueAnimator.setIntValues(from, to);
        valueAnimator.setDuration(180 + 150 * Math.abs(from - to) / attrsUtils.mMaxHeaderScroll);
        valueAnimator.start();
    }

    private ValueAnimator getValueAnimator() {
        ValueAnimator valueAnimator;
        if (scrolls < 0) {
            valueAnimator = valueAnimatorHeader;
        } else {
            valueAnimator = valueAnimatorfooter;
        }
        return valueAnimator;
    }


    public void NotifyCompleteRefresh0() {
        if (scrolls == 0 || state.ordinal() > 1) {
            return;
        }
        state = scrolls < 0 ? State.REFRESHCOMPLETE : State.LOADINGCOMPLETE;
        callbackState(state);

        postDelayed(this, attrsUtils.delayCompleteTime);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimatorHeader != null) {
            valueAnimatorHeader.cancel();
        }
        if (valueAnimatorfooter != null) {
            valueAnimatorfooter.cancel();
        }
    }

    @Override
    public void run() {
        aninatorTo(scrolls, 0);
    }

    /**
     * 刷新停留到配置位置，再归位
     */
    public void NotifyCompleteRefresh1(Object obj) {
        if (scrolls == 0 || state.ordinal() > 1) {
            return;
        }
        baseRefreshHandler.setData(obj);
        if (state == State.REFRESHING || state == State.LOADING) {
            state = state == State.REFRESHING ? State.REFRESHCOMPLETE : State.LOADINGCOMPLETE;
            int position = state == State.REFRESHING ? -attrsUtils.mHeaderRefreshCompletePosition : attrsUtils.mFooterRefreshPosition;
            if (position == 0) {
                callbackState(state);
            }
            aninatorTo(scrolls, position);
        } else {
            NotifyCompleteRefresh0();
        }
    }

    /**
     * 刷新停留到某个位置，再归位
     *
     * @param obj
     * @param position
     */
    public void NotifyCompleteRefresh1(int position, Object obj) {
        if (scrolls == 0 || state.ordinal() > 1) {
            return;
        }
        baseRefreshHandler.setData(obj);
        if (state == State.REFRESHING || state == State.LOADING) {
            state = state == State.REFRESHING ? State.REFRESHCOMPLETE : State.LOADINGCOMPLETE;
            if (position == 0) {
                callbackState(state);
            }
            aninatorTo(scrolls, state == State.REFRESHCOMPLETE ? -position : position);
        } else {
            NotifyCompleteRefresh0();
        }

    }

    /**
     * 设置动画到正在刷新
     */
    public void setRefreshing() {
        if (state != State.REFRESHING && state != State.LOADING) {
            state = State.REFRESHING;
            post(new Runnable() {
                @Override
                public void run() {
                    aninatorTo(scrolls, -attrsUtils.mHeaderRefreshPosition);
                }
            });

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!canhandle) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedFraction = animation.getAnimatedFraction();
        int animatedValue = (int) animation.getAnimatedValue();
        scrolls = animatedValue;
        doScroll(attrsUtils.orentation == Orentation.VERTICAL);
        /**
         * 刷新完成时
         * 状态已变成 State.REFRESHCOMPLETE State.LOADINGCOMPLETE
         */
        boolean isComplete = (state == State.REFRESHCOMPLETE) || (state == State.LOADINGCOMPLETE);
        if (isComplete) {
            callbackScroll(state == State.REFRESHCOMPLETE ? State.PULL_HEADER : State.PULL_FOOTER, animatedValue);
        } else {
            callbackScroll(state, animatedValue);
        }
        if (animatedFraction == 1) {
            if (animatedValue != 0) {
                if (!isComplete) {
                    canhandle = false;
                    if (animatedValue > 0) {
                        state = State.LOADING;
                    } else {
                        state = State.REFRESHING;
                    }
                } else {
                    NotifyCompleteRefresh0();
                }
            } else {
                canhandle = true;
                state = State.IDEL;
            }
            callbackState(state);
        }
    }

    private void callbackScroll(State state, int value) {
        if (callback != null) {
            callback.call(state, value);
        }

        if (state == State.PULL_HEADER) {
            baseRefreshHandler.onPullHeader(mHeader, -value);
        } else {
            baseRefreshHandler.onPullFooter(mFooter, value);
        }
    }

    private void callbackState(State state) {
        if (callback != null) {
            callback.call(state);
        }
        baseRefreshHandler.OnStateChange(state);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target) {
        helper.onStopNestedScroll(target);
        if (scrolls != 0 && (state.ordinal() > 3)) {
            changeState(scrolls, 0);
            int mRefreshPosition = scrolls > 0 ? attrsUtils.mFooterRefreshPosition : attrsUtils.mHeaderRefreshPosition;
            if (Math.abs(scrolls) >= mRefreshPosition && !attrsUtils.OVERSCROLL) {
                //上拉不停留，下拉停留到刷新位置
                if (scrolls > 0 && baseRefreshHandler.handleAdapter()) {
                    aninatorTo(scrolls, 0);
                } else {
                    aninatorTo(scrolls, (int) signum(scrolls) * mRefreshPosition);
                }
            } else {
                aninatorTo(scrolls, 0);
            }
        }
    }

    private void changeState(int scrolls, int dy) {
        State statex;
        if (scrolls == 0) {
            statex = dy > 0 ? State.PULL_FOOTER : State.PULL_HEADER;
        } else {
            statex = scrolls > 0 ? State.PULL_FOOTER : State.PULL_HEADER;
        }
        if (statex != state) {
            callbackState(statex);
        }
        this.state = statex;
    }

    private void checkBounds(int scrolltemp) {
        int maxheader;
        int maxfooter;
        if (attrsUtils.OVERSCROLL) {
            maxheader = scrolltemp <= 0 ? attrsUtils.mMaxHeaderScroll : 0;
            maxfooter = scrolltemp >= 0 ? attrsUtils.mMaxHeaderScroll : 0;
        } else {
            maxheader = attrsUtils.CANHEADER && scrolltemp <= 0 ? attrsUtils.mMaxHeaderScroll : 0;
            maxfooter = attrsUtils.CANFOOTR && scrolltemp >= 0 ? attrsUtils.mMaxFooterScroll : 0;
        }

        if (scrolls < -maxheader) {
            scrolls = -maxheader;
        }
        if (scrolls > maxfooter) {
            scrolls = maxfooter;
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (state.ordinal() < 2) {
            return;
        }

        boolean isvertical = attrsUtils.orentation == Orentation.VERTICAL;
        int dscroll = isvertical ? dyUnconsumed : dxUnconsumed;

        if ((dscroll < 0 && !canScroll(isvertical, -1)) || (dscroll > 0 && !canScroll(isvertical, 1))) {
            int tempscrolls = scrolls;
            scrolls += dscroll / attrsUtils.PULLRATE;
            checkBounds(tempscrolls);
            doScroll(isvertical);
            changeState(tempscrolls, dscroll);
            callbackScroll(state, scrolls);
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @Nullable int[] consumed) {
        if (state.ordinal() < 2) {
            return;
        }
        boolean isvertical = attrsUtils.orentation == Orentation.VERTICAL;
        int dscroll = isvertical ? dy - consumed[1] : dx - consumed[0];
        if ((dscroll > 0 && scrolls < 0) || (dscroll < 0 && scrolls > 0)) {
            int scrolltemp = scrolls;
            scrolls += dscroll / attrsUtils.PULLRATE;
            checkBounds(scrolltemp);
            if (isvertical) {
                consumed[1] = dscroll;
            } else {
                consumed[0] = dscroll;
            }
            doScroll(isvertical);
            changeState(scrolltemp, dscroll);
            callbackScroll(state, scrolls);
        }
    }

    private void doScroll(boolean isvertical) {
        if (isvertical) {
            MyScrollTo(0, scrolls);
        } else {
            MyScrollTo(scrolls, 0);
        }
    }

    private boolean canScroll(boolean isvertical, int direction) {
        if (isvertical) {
            return mScroll.canScrollVertically(direction);
        } else {
            return mScroll.canScrollHorizontally(direction);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        if (scrolls != 0) {
            ValueAnimator valueAnimator = getValueAnimator();
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
        }
        int ore = attrsUtils.orentation == Orentation.VERTICAL ? ViewCompat.SCROLL_AXIS_VERTICAL : ViewCompat.SCROLL_AXIS_HORIZONTAL;
        return (axes & ore) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
        helper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return helper.getNestedScrollAxes();
    }


    private Callback callback;

    public void setListener(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public abstract static class Callback {
        public abstract void call(State t);

        public void call(State t, int scroll) {
        }
    }

    /**
     * 初始化全局配置
     *
     * @param defaultBuilder
     */
    public static void init(DefaultBuilder defaultBuilder) {
        AttrsUtils.setBuilder(defaultBuilder);
    }

    /**
     * 解析xml属性
     */
    public static class AttrsUtils {
        /**
         * 头部最大滑动距离
         */
        private int mMaxHeaderScroll = -1;

        /**
         * 尾部最大刷新距离
         */
        private int mMaxFooterScroll = -1;

        /**
         * 头部刷新停留的位置
         */
        private int mHeaderRefreshPosition = -1;

        /**
         * 尾部刷新停留的位置
         */
        private int mFooterRefreshPosition = -1;

        /**
         * 快速滑动Overscroll的距离
         */
        private int mFlingmax;

        /**
         * 刷新完成停留的位置
         */
        private int mHeaderRefreshCompletePosition = -1;
        private int mFooterLoadingCompletePosition = -1;

        /**
         * 布局文件
         */
        private int HEADER_LAYOUTID, SCROLL_LAYOUT_ID, FOOTER_LAYOUTID;

        /**
         * 头部 尾部是否可滑
         */
        private Boolean CANHEADER = null, CANFOOTR = null, OVERSCROLL = null, OVERSCROLL_ELASTIC = null, EVALUATEABLE = null;

        /**
         * 滑动方向
         */
        private Orentation orentation = Orentation.VERTICAL;

        /**
         * 默认全局配置
         */
        private static DefaultBuilder builder = new DefaultBuilder();

        /**
         * 刷新完成延迟时间
         */
        private int delayCompleteTime = 300;

        /**
         * 拉伸张力
         */
        private float PULLRATE = -1;

        private static void setBuilder(DefaultBuilder builderx) {
            builder = builderx;
        }

        public void ParseAttrs(Context context, AttributeSet attr) {
            TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.RefreshLayout);
            if (HEADER_LAYOUTID == 0)
                HEADER_LAYOUTID = typedArray.getResourceId(R.styleable.RefreshLayout_headerID, builder.HEADER_LAYOUTID_DEFAULT);

            if (FOOTER_LAYOUTID == 0)
                FOOTER_LAYOUTID = typedArray.getResourceId(R.styleable.RefreshLayout_footerID, builder.FOOTER_LAYOUTID_DEFAULT);


            if (SCROLL_LAYOUT_ID == 0)
                SCROLL_LAYOUT_ID = typedArray.getResourceId(R.styleable.RefreshLayout_scrollID, builder.SCROLL_LAYOUT_ID_DEFAULT);

            if (CANHEADER == null)
                CANHEADER = typedArray.getBoolean(R.styleable.RefreshLayout_canHeader, builder.CANHEADER_DEFAULT);

            if (CANFOOTR == null)
                CANFOOTR = typedArray.getBoolean(R.styleable.RefreshLayout_canFooter, builder.CANFOOTR_DEFAULT);

            if (OVERSCROLL == null)
                OVERSCROLL = typedArray.getBoolean(R.styleable.RefreshLayout_overscroll, builder.OVERSCROLL_DEFAULT);
            if (OVERSCROLL_ELASTIC == null) {
                OVERSCROLL_ELASTIC = typedArray.getBoolean(R.styleable.RefreshLayout_elastic_overscroll, builder.OVERSCROLL_ELASTIC_DEFAULT);
                if (OVERSCROLL_ELASTIC) {
                    OVERSCROLL = true;
                }
            }

            if (EVALUATEABLE == null) {
                EVALUATEABLE = typedArray.getBoolean(R.styleable.RefreshLayout_evaluateable, false);
            }

            int orentation = typedArray.getInt(R.styleable.RefreshLayout_orentation, 1);
            if (orentation == 0) {
                this.orentation = Orentation.HORIZONTAL;
            }
            if (mMaxHeaderScroll == -1)
                mMaxHeaderScroll = (int) typedArray.getDimension(R.styleable.RefreshLayout_mMaxHeadertScroll, mMaxHeaderScroll);
            if (mMaxFooterScroll == -1)
                mMaxFooterScroll = (int) typedArray.getDimension(R.styleable.RefreshLayout_mMaxFooterScroll, mMaxFooterScroll);
            if (mHeaderRefreshPosition == -1)
                mHeaderRefreshPosition = (int) typedArray.getDimension(R.styleable.RefreshLayout_mHeaderRefreshPosition, mHeaderRefreshPosition);
            if (mFooterRefreshPosition == -1)
                mFooterRefreshPosition = (int) typedArray.getDimension(R.styleable.RefreshLayout_mFooterRefreshPosition, mFooterRefreshPosition);
            if (mHeaderRefreshCompletePosition == -1)
                mHeaderRefreshCompletePosition = (int) typedArray.getDimension(R.styleable.RefreshLayout_mHeaderRefreshCompletePosition, 0);
            if (mFooterLoadingCompletePosition == -1)
                mFooterLoadingCompletePosition = (int) typedArray.getDimension(R.styleable.RefreshLayout_mFooterLoadingCompletePosition, 0);
            if (mFlingmax == -1)
                mFlingmax = (int) typedArray.getDimension(R.styleable.RefreshLayout_mFlingmax, mFlingmax);
            if (delayCompleteTime == 800)
                delayCompleteTime = typedArray.getInt(R.styleable.RefreshLayout_delayCompleteTime, delayCompleteTime);

            if (PULLRATE == -1)
                PULLRATE = typedArray.getFloat(R.styleable.RefreshLayout_pullrate, builder.PULLRATE);

            String handler = typedArray.getString(R.styleable.RefreshLayout_stateHandler);

            if (!TextUtils.isEmpty(handler)) {
                try {
                    builder.defaultRefreshHandler = Class.forName(handler);
                } catch (Exception e) {
                    throw new UnsupportedOperationException(e.getCause());
                }
            }

            typedArray.recycle();
        }

        public int getmMaxHeaderScroll() {
            return mMaxHeaderScroll;
        }

        public void setmMaxHeaderScroll(int mMaxHeaderScroll) {
            this.mMaxHeaderScroll = mMaxHeaderScroll;
        }

        public int getmMaxFooterScroll() {
            return mMaxFooterScroll;
        }

        public void setmMaxFooterScroll(int mMaxFooterScroll) {
            this.mMaxFooterScroll = mMaxFooterScroll;
        }

        public int getmHeaderRefreshPosition() {
            return mHeaderRefreshPosition;
        }

        public void setmHeaderRefreshPosition(int mHeaderRefreshPosition) {
            this.mHeaderRefreshPosition = mHeaderRefreshPosition;
        }

        public int getmFooterRefreshPosition() {
            return mFooterRefreshPosition;
        }

        public void setmFooterRefreshPosition(int mFooterRefreshPosition) {
            this.mFooterRefreshPosition = mFooterRefreshPosition;
        }

        public int getmHeaderRefreshCompletePosition() {
            return mHeaderRefreshCompletePosition;
        }

        public void setmHeaderRefreshCompletePosition(int mHeaderRefreshCompletePosition) {
            this.mHeaderRefreshCompletePosition = mHeaderRefreshCompletePosition;
        }

        public int getmFooterLoadingCompletePosition() {
            return mFooterLoadingCompletePosition;
        }

        public void setmFooterLoadingCompletePosition(int mFooterLoadingCompletePosition) {
            this.mFooterLoadingCompletePosition = mFooterLoadingCompletePosition;
        }

        public int getHEADER_LAYOUTID() {
            return HEADER_LAYOUTID;
        }

        public void setHEADER_LAYOUTID(int HEADER_LAYOUTID) {
            this.HEADER_LAYOUTID = HEADER_LAYOUTID;
        }

        public int getSCROLL_LAYOUT_ID() {
            return SCROLL_LAYOUT_ID;
        }

        public void setSCROLL_LAYOUT_ID(int SCROLL_LAYOUT_ID) {
            this.SCROLL_LAYOUT_ID = SCROLL_LAYOUT_ID;
        }

        public int getFOOTER_LAYOUTID() {
            return FOOTER_LAYOUTID;
        }

        public void setFOOTER_LAYOUTID(int FOOTER_LAYOUTID) {
            this.FOOTER_LAYOUTID = FOOTER_LAYOUTID;
        }

        public boolean getCANHEADER() {
            return CANHEADER;
        }

        public void setCANHEADER(Boolean CANHEADER) {
            this.CANHEADER = CANHEADER;
        }

        public boolean getCANFOOTR() {
            return CANFOOTR;
        }

        public void setCANFOOTR(Boolean CANFOOTR) {
            this.CANFOOTR = CANFOOTR;
        }

        public boolean getOVERSCROLL() {
            return OVERSCROLL;
        }

        public void setOVERSCROLL(Boolean OVERSCROLL) {
            this.OVERSCROLL = OVERSCROLL;
        }

        public boolean getOVERSCROLL_ELASTIC() {
            return OVERSCROLL_ELASTIC;
        }

        public void setOVERSCROLL_ELASTIC(Boolean OVERSCROLL_ELASTIC) {
            this.OVERSCROLL_ELASTIC = OVERSCROLL_ELASTIC;
            if (OVERSCROLL_ELASTIC) {
                this.OVERSCROLL = true;
            }
        }

        public boolean getEVALUATEABLE() {
            return EVALUATEABLE;
        }

        public void setEVALUATEABLE(Boolean EVALUATEABLE) {
            this.EVALUATEABLE = EVALUATEABLE;
        }

        public Orentation getOrentation() {
            return orentation;
        }

        public void setOrentation(Orentation orentation) {
            this.orentation = orentation;
        }

        public int getDelayCompleteTime() {
            return delayCompleteTime;
        }

        public void setDelayCompleteTime(int delayCompleteTime) {
            this.delayCompleteTime = delayCompleteTime;
        }

        public float getPULLRATE() {
            return PULLRATE;
        }

        public void setPULLRATE(float PULLRATE) {
            this.PULLRATE = PULLRATE;
        }
    }

    /**
     * 保存全局默认配置
     */
    public static class DefaultBuilder {
        private int HEADER_LAYOUTID_DEFAULT, SCROLL_LAYOUT_ID_DEFAULT, FOOTER_LAYOUTID_DEFAULT;
        private float PULLRATE = 2.5f;
        private boolean CANHEADER_DEFAULT = true, CANFOOTR_DEFAULT = true, OVERSCROLL_DEFAULT = false, OVERSCROLL_ELASTIC_DEFAULT = false;
        private Class defaultRefreshHandler = RefreshHanderBase.class;

        public DefaultBuilder setBaseRefreshHandler(Class defaultRefreshWrap) {
            this.defaultRefreshHandler = defaultRefreshWrap;
            return this;
        }

        public DefaultBuilder setHeaderLayoutidDefault(int headerLayoutidDefault) {
            HEADER_LAYOUTID_DEFAULT = headerLayoutidDefault;
            return this;
        }

        public DefaultBuilder setScrollLayoutIdDefault(int scrollLayoutIdDefault) {
            SCROLL_LAYOUT_ID_DEFAULT = scrollLayoutIdDefault;
            return this;
        }

        public DefaultBuilder setFooterLayoutidDefault(int footerLayoutidDefault) {
            FOOTER_LAYOUTID_DEFAULT = footerLayoutidDefault;
            return this;
        }

        public DefaultBuilder setCanheaderDefault(boolean canheaderDefault) {
            CANHEADER_DEFAULT = canheaderDefault;
            return this;
        }

        public DefaultBuilder setCanfootrDefault(boolean canfootrDefault) {
            CANFOOTR_DEFAULT = canfootrDefault;
            return this;
        }

        public DefaultBuilder setOverscrollDefault(boolean overscrollDefault) {
            OVERSCROLL_DEFAULT = overscrollDefault;
            return this;
        }

        public DefaultBuilder setPullRate(int rate) {
            if (rate > 0) {
                PULLRATE = rate;
            }
            return this;
        }

    }


    private BaseRefreshHeaderAndFooterHandler baseRefreshHandler;

    public void setRefreshHandler(BaseRefreshHeaderAndFooterHandler baseRefreshHandler) {
        this.baseRefreshHandler = baseRefreshHandler;
        if (baseRefreshHandler.handleAdapter()) {
            attrsUtils.EVALUATEABLE = true;
            mHeader = null;
            mFooter = null;
        }
        if (!baseRefreshHandler.isinit) {
            baseRefreshHandler.initView(this);
            baseRefreshHandler.isinit = true;
        }
    }

    public static abstract class BaseRefreshHeaderAndFooterHandler<T> {
        protected T data;
        boolean isinit = false;

        public abstract void onPullHeader(View view, int scrolls);

        public abstract void onPullFooter(View view, int scrolls);

        public abstract void OnStateChange(State state);

        protected void initView(RefreshLayout layout) {

        }

        protected boolean handleAdapter() {
            return false;
        }

        protected void setData(Object data) {
            this.data = (T) data;
        }
    }


    public <T extends View> T getmHeader() {
        if (mHeader == null) {
            if (mScroll instanceof RecyclerView) {
                RecyclerView.Adapter adapter = ((RecyclerView) mScroll).getAdapter();
                if (adapter instanceof AdapterRefreshInterface) {
                    mHeader = ((AdapterRefreshInterface) adapter).getHeader();
                }
            }
        }
        return (T) mHeader;
    }

    public <T extends View> T getmFooter() {
        if (mFooter == null) {
            if (mScroll instanceof RecyclerView) {
                RecyclerView.Adapter adapter = ((RecyclerView) mScroll).getAdapter();
                if (adapter instanceof AdapterRefreshInterface) {
                    mFooter = ((AdapterRefreshInterface) adapter).getFooter();
                }
            }
        }
        return (T) mFooter;
    }

    public <T extends View> T getmScroll() {
        return (T) mScroll;
    }

    public <T extends View> T findInHeaderView(int id) {

        return (T) getmHeader().findViewById(id);
    }

    public <T extends View> T findInScrollView(int id) {

        return (T) getmScroll().findViewById(id);
    }

    public <T extends View> T findInFooterView(int id) {

        return (T) getmFooter().findViewById(id);
    }

    public <T extends BaseRefreshHeaderAndFooterHandler> T getBaseRefreshHandler() {
        return (T) baseRefreshHandler;
    }

    /**
     * 1+1/2+1/3+……+1/n=lnn+R
     * R为欧拉常数,约为0.5772.
     * 1 1/2 1/3 1/4 1/5
     *
     * @param current
     * @param base
     * @return
     */
    private int caculateZhangli(int current, int base) {
        float signum = Math.signum(current);
        int pullrate = Math.abs(current) / base;
        if (pullrate == 0) {
            return current;
        } else if (pullrate == 1)
            return (int) (signum * base + signum * (Math.abs(current) % base) / 3);
        else if (pullrate == 2) {
            return (int) (1.333333333f * signum * base + signum * (Math.abs(current) % base) / 4);
        } else if (pullrate == 3) {
            return (int) (1.583333333f * signum * base + signum * (Math.abs(current) % base) / 5);
        } else {
            return (int) (1.783333333f * signum * base + signum * (Math.abs(current) % base) / 6);
        }
    }

    public AttrsUtils getAttrsUtils() {
        return attrsUtils;
    }

    public void MyScrollTo(int x, int y) {
        int temp = attrsUtils.orentation == Orentation.VERTICAL ? y : x;
        if (attrsUtils.OVERSCROLL && attrsUtils.OVERSCROLL_ELASTIC) {
            temp = caculateZhangli(temp, attrsUtils.mMaxHeaderScroll / 3);
            if (temp <= 0) {
                mScroll.setPivotX(0);
                mScroll.setPivotY(0);
            } else {
                mScroll.setPivotX(attrsUtils.orentation == Orentation.VERTICAL ? 0 : mScroll.getMeasuredWidth());
                mScroll.setPivotY(attrsUtils.orentation == Orentation.VERTICAL ? mScroll.getMeasuredHeight() : 0);
            }
            mScroll.setScaleY(Math.min(1.4f, 1f + (float) Math.abs(temp) / attrsUtils.mMaxHeaderScroll / 3));
        } else {
            if (!attrsUtils.EVALUATEABLE) {
//                x = caculateZhangli(x, x <= 0 ? (mHeader == null ? attrsUtils.mMaxHeaderScroll / 3 : mHeader.getMeasuredWidth()) : (mFooter == null ? attrsUtils.mMaxFooterScroll / 3 : mFooter.getMeasuredWidth()));
//                y = caculateZhangli(y, y < 0 ? (mHeader == null ? attrsUtils.mMaxHeaderScroll / 3 : mHeader.getMeasuredHeight()) : (mFooter == null ? attrsUtils.mMaxFooterScroll / 3 : mFooter.getMeasuredHeight()));
                super.scrollTo(x, y);
            }
        }
    }
}
