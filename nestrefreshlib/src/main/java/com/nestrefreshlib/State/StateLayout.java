package com.nestrefreshlib.State;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;

import com.nestrefreshlib.State.Interface.Recorder;
import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.State.Interface.StateEnum;
import com.nestrefreshlib.State.Interface.BaseStateListener;
import com.nestrefreshlib.State.Interface.ShowStateInterface;
import com.nestrefreshlib.State.Interface.StateHandlerInterface;


/**
 * Created by ck on 2017/7/29.
 */

public class StateLayout extends FrameLayout implements ShowStateInterface {
    //全局id记录者
    protected static Recorder globalrecorder;
    private static Recorder recorder;
    //状态onindBView
    protected StateHandlerInterface stateHandler;
    boolean errorInflated = false, emptyInflated = false, nomoreInflated = false;

    private StateEnum showstate = StateEnum.TYPE_ITEM;

    private ArrayMap<StateEnum, View> views = new ArrayMap<>(4);

    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 0) {
            if (views.get(StateEnum.TYPE_ITEM) == null) {
                views.put(StateEnum.TYPE_ITEM, getChildAt(0));
                initState();
            }
        }
    }

    private void init() {
        recorder = globalrecorder;
        if (recorder == null)
            recorder = new Recorder.Builder().build();
        try {
            stateHandler = recorder.getClazz().newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public StateLayout setStateHandler(StateHandlerInterface stateHandler) {
        this.stateHandler = stateHandler;
        return this;
    }

    /**
     * 设置状态布局的监听
     *
     * @param listener
     * @return
     */
    public StateLayout setStateListener(BaseStateListener listener) {
        stateHandler.setStateClickListener(listener);
        return this;
    }

    /**
     * 全局初始化状态 layout id
     *
     * @param globalRecordera
     */
    public static void init(Recorder globalRecordera) {
        globalrecorder = globalRecordera;
    }

    public StateLayout setContent(View view) {
        views.put(StateEnum.TYPE_ITEM, view);
        addView(view);
        initState();
        return this;
    }

    public StateEnum getShowstate() {
        return showstate;
    }

    public void setShowstate(StateEnum showstate) {
        this.showstate = showstate;
    }

    private void initState() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View loadingView = inflater.inflate(recorder.getLoadingres(), this, false);
        views.put(StateEnum.SHOW_LOADING, loadingView);
        addView(loadingView);

        ViewStub emptyView = new ViewStub(getContext(), recorder.getEmptyres());
        views.put(StateEnum.SHOW_EMPTY, emptyView);
        addView(emptyView);


        ViewStub errorView = new ViewStub(getContext(), recorder.getErrorres());
        addView(errorView);
        views.put(StateEnum.SHOW_ERROR, errorView);

        ViewStub nomoreview = new ViewStub(getContext(), recorder.getNomore());
        addView(nomoreview);
        views.put(StateEnum.SHOW_NOMORE, nomoreview);
        showState(showstate, null);
    }

    public StateLayout setContent(int contentres) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View contentView = inflater.inflate(contentres, this, false);
        views.put(StateEnum.TYPE_ITEM, contentView);
        addView(contentView);

        initState();
        return this;
    }

    @Override
    public void showState(StateEnum showstate, Object o) {
        if (this.showstate != showstate) {
            stateHandler.switchState(showstate);
        }

        if (showstate == StateEnum.SHOW_ERROR && !errorInflated) {
            errorInflated = true;
            views.put(showstate, ((ViewStub) views.get(showstate)).inflate());
        }
        if (showstate == StateEnum.SHOW_EMPTY && !emptyInflated) {
            emptyInflated = true;
            views.put(showstate, ((ViewStub) views.get(showstate)).inflate());
        }
        if (showstate == StateEnum.SHOW_NOMORE && !nomoreInflated) {
            nomoreInflated = true;
            views.put(showstate, ((ViewStub) views.get(showstate)).inflate());
        }

        views.get(this.showstate).setVisibility(GONE);
        this.showstate = showstate;
        switch (showstate) {
            case SHOW_NOMORE:
                stateHandler.BindNomoreHolder(Holder.createViewHolder(views.get(showstate)), o);
                break;
            case SHOW_LOADING:
                stateHandler.BindLoadingHolder(Holder.createViewHolder(views.get(showstate)), o);
                break;
            case SHOW_EMPTY:
                stateHandler.BindEmptyHolder(Holder.createViewHolder(views.get(showstate)), o);
                break;
            case SHOW_ERROR:
                stateHandler.BindErrorHolder(Holder.createViewHolder(views.get(showstate)), o);
                break;
        }
        views.get(showstate).setVisibility(VISIBLE);
    }

    @Override
    public void showEmpty() {
        showState(StateEnum.SHOW_EMPTY, null);
    }

    @Override
    public void ShowError() {
        showState(StateEnum.SHOW_ERROR, null);
    }

    @Override
    public void showItem() {
        showState(StateEnum.TYPE_ITEM, null);
    }

    @Override
    public void showLoading() {
        showState(StateEnum.SHOW_LOADING, null);
    }


    @Override
    public void showNomore() {
        showState(StateEnum.SHOW_NOMORE, null);
    }

    public Button getButton(View view) {
        if (view instanceof Button) {
            return (Button) view;
        } else if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                Button button = getButton(((ViewGroup) view).getChildAt(i));
                if (button != null)
                    return button;

            }
        }
        return null;
    }
}

