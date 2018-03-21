package com.nestrefreshlib;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by S0005 on 2017/5/12.
 */

public class SLoading extends View {


    /**
     * 颜色变幻数组
     */
    private int[] color = {0xFFF4511E, 0xFFFDD835, 0xFF43A047,
            0xFF1E88E5, 0xFF8E24AA, 0xFF546E7A};

    /**
     * 圆间隔
     */
    private float gap = -1;

    /**
     * 圆半径
     */
    private float radius = -1;

    /**
     * 圆个数
     */
    private int num = 3;

    /**
     * 变方式
     * 0：单色 1：多色
     */
    int type = 0;

    private Paint paint;

    /**
     * 默认宽高
     */
    private int width = 60;
    private int height = 100;

    /**
     * 圆实体
     */
    List<Progress> list = new ArrayList<>();

    /**
     * 动画集
     */
    private AnimatorSet set;
    private List<Animator> animators;
    private int contentLength;

    public SLoading(Context context) {
        this(context, null);
    }

    public SLoading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainData(attrs);
        initialized();
    }

    private void obtainData(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SLoading);
        radius = a.getDimension(R.styleable.SLoading_sradius, -1);
        gap = a.getDimension(R.styleable.SLoading_sgap, -1);
        num = a.getInt(R.styleable.SLoading_snum, -1);
        type = a.getInt(R.styleable.SLoading_scolortype, 0);
        int resourceId = a.getResourceId(R.styleable.SLoading_scolorarray, 0);
        try {
            if (resourceId != 0)
                color = getResources().getIntArray(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.recycle();
    }

    private void initialized() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        contentLength = (int) (2 * num * radius + (num - 1) * gap);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = Math.min(contentLength, widthSize);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) Math.min(2 * radius, heightSize);
        }
        if (gap == -1)
            gap = height / 2;

        if (radius == -1)
            radius = height / 2;

        if (list.size() == 0) {
            for (int i = 0; i < num; i++) {
                list.add(new Progress(radius, type == 0 ? 0 : ((num - i) % num)));
            }

        }
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < num; i++) {
            paint.setAlpha((int) ((0.1 + 0.7 * list.get(i).getPercentage()) * 255));
            paint.setColor(color[list.get(i).getColorIndex() % color.length]);
            canvas.drawCircle(calculateCenterX(i), height / 2, list.get(i).getCurrent(), paint);
        }
    }

    private float calculateCenterX(int i) {
        return calculateStart() + i * 2 * radius + radius + i * gap;
    }


    private int calculateStart() {
        return width / 2 - contentLength / 2;
    }

    public SLoading setColor(int[] color) {
        this.color = color;
        return this;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SLoading startAnimator() {
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
                goAnimator();
            }
        });
        return this;
    }

    private ObjectAnimator getAnimator(final Progress progress, final int i) {
        ObjectAnimator animator = ObjectAnimator.ofObject(progress, "percentage", new FloatEvaluator(), 0.2, 1, 0.2);
        animator.setDuration(800);
        animator.setRepeatCount(-1);
        animator.setStartDelay((long) (i * 400 / num));
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatMode(ValueAnimator.REVERSE);
        if (i == 0) {
            ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    postInvalidate();
                }
            };
            animator.addUpdateListener(listener);
        }
        animator.addListener(new Anl(progress));

        return animator;
    }

    private void goAnimator() {
        if (set == null) {
            set = new AnimatorSet();
            animators = new ArrayList<>(num);
            for (int i = 0; i < num; i++) {
                ObjectAnimator animator = getAnimator(list.get(i), i);
                animators.add(animator);
            }
            set.playTogether(animators);
            set.start();
        } else {
            if (!set.isStarted()) {
                for (int i = 0; i < num; i++) {
                    list.get(i).setColorIndex(type == 0 ? 0 : (num - i % num));
                    list.get(i).setRadius(radius);
                    list.get(i).setPercentage(0);
                }
                set.start();
            }
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimator();
        if (animators != null)
            animators.clear();
        super.onDetachedFromWindow();
    }

    public void stopAnimator() {
        if (set != null) {
            set.cancel();
            for (Animator animator : set.getChildAnimations()) {
                animator.cancel();
            }
            set.getChildAnimations().clear();
            setVisibility(GONE);
        }
    }

    private static class Progress {
        //进度百分比
        float percentage = 0;
        //当前半径
        float current;
        //最大半径
        float radius;
        //颜色下标
        int colorIndex;

        public Progress(float radius, int colorIndex) {
            this.current = percentage * radius;
            this.radius = radius;
            this.colorIndex = colorIndex;
        }

        public void setColorIndex(int colorIndex) {
            this.colorIndex = colorIndex;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public int getColorIndex() {
            return colorIndex;
        }

        public float getCurrent() {
            return current;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
            this.current = percentage * radius;
        }
    }

    static class Anl implements Animator.AnimatorListener {
        Progress progress;

        public Anl(Progress progress) {
            this.progress = progress;
        }


        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {

        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            int colorIndex = progress.getColorIndex();
            progress.setColorIndex(++colorIndex);
        }
    }
}
