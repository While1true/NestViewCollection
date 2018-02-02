package com.ck.hello.nestrefreshlib.View.State;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateEnum;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.BaseStateListener;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.ShowStateInterface;
import com.ck.hello.nestrefreshlib.View.Adpater.Interface.StateHandlerInterface;


/**
 * Created by ck on 2017/7/29.
 */

public class StateLayout extends FrameLayout implements ShowStateInterface {
    //全局id记录者
    protected static Recorder globalrecorder;
    private static Recorder recorder;
    //状态onindBView
    protected StateHandlerInterface stateHandler;
    boolean errorInflated = false, emptyInflated = false;

    private StateEnum showstate = StateEnum.SHOW_LOADING;

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

    public StateLayout setContent(int contentres) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View contentView = inflater.inflate(contentres, this, false);
        views.put(StateEnum.TYPE_ITEM, contentView);
        addView(contentView);


        View loadingView = inflater.inflate(recorder.getLoadingres(), this, false);
        views.put(StateEnum.SHOW_LOADING, loadingView);
        addView(loadingView);

        ViewStub emptyView = new ViewStub(getContext(), recorder.getEmptyres());
        views.put(StateEnum.SHOW_EMPTY, emptyView);
        addView(emptyView);


        ViewStub errorView = new ViewStub(getContext(), recorder.getErrorres());
        addView(errorView);
        views.put(StateEnum.SHOW_ERROR, errorView);
        showLoading();
        return this;
    }

    @Override
    public void showState(StateEnum showstate, Object o) {
        if (this.showstate != showstate) {
            stateHandler.switchState(showstate);
        }
        this.showstate = showstate;
        if (showstate == StateEnum.SHOW_ERROR && !errorInflated) {
            errorInflated = true;
            views.put(showstate, ((ViewStub) views.get(showstate.ordinal())).inflate());
        }
        if (showstate == StateEnum.SHOW_EMPTY && !emptyInflated) {
            emptyInflated = true;
            views.put(showstate, ((ViewStub) views.get(showstate.ordinal())).inflate());
        }
        for (StateEnum stateEnum : views.keySet()) {
            if (stateEnum == showstate) {
                views.get(stateEnum).setVisibility(GONE);
            } else {
                switch (showstate) {
                    case TYPE_ITEM:
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
        }
    }

    @Override
    public void showEmpty() {
        showState(StateEnum.SHOW_EMPTY, null);
    }

    @Override
    public void ShowError() {
        showState(StateEnum.SHOW_ERROR, null);
    }

    @Deprecated
    @Override
    public void showItem() {

    }

    @Override
    public void showLoading() {
        showState(StateEnum.SHOW_LOADING, null);
    }

    @Override
    public void showNomore() {
        showState(StateEnum.TYPE_ITEM, null);
    }

    public Button getButton(View view) {
        Log.i("TAG", "getButton: " + (view instanceof Button));
        if (view instanceof Button) {
            Log.i("TAG", "getButton:返回了button ");
            return (Button) view;
        } else if (view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                Button button = getButton(((ViewGroup) view).getChildAt(i));
                if (button != null)
                    return button;

            }
        }
        Log.i("TAG", "getButton:返回了null");
        return null;
    }
}

