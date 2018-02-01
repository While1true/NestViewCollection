package com.ck.hello.nestrefreshlib.View.State;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
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

    private int showstate=StateShowInterface.LOADING;

    private SparseArray<View> views = new SparseArray<>(4);

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
        views.put(StateShowInterface.CONTENT, contentView);
        addView(contentView);


        View loadingView = inflater.inflate(recorder.getLoadingres(), this, false);
        views.put(StateShowInterface.LOADING, loadingView);
        addView(loadingView);

        ViewStub emptyView = new ViewStub(getContext(), recorder.getEmptyres());
        views.put(StateShowInterface.EMPTY, emptyView);
        addView(emptyView);


        ViewStub errorView = new ViewStub(getContext(), recorder.getErrorres());
        addView(errorView);
        views.put(StateShowInterface.ERROR, errorView);
        showLoading();
        return this;
    }

    private interface StateShowInterface{
         int CONTENT=0;
         int LOADING=1;
         int EMPTY=2;
         int ERROR=3;
    }

    @Override
    public void showState(int showstate, Object o) {
        if(this.showstate!=showstate){
            stateHandler.switchState(showstate);
        }
        this.showstate=showstate;
        if (showstate==StateShowInterface.ERROR&&!errorInflated) {
            errorInflated = true;
            views.put(showstate, ((ViewStub) views.get(showstate)).inflate());
        }
        if (showstate==StateShowInterface.EMPTY&&!emptyInflated) {
            emptyInflated = true;
            views.put(showstate, ((ViewStub) views.get(showstate)).inflate());
        }
        for (int i = 0; i < views.size(); i++) {
            if(i!=showstate){
                views.get(i).setVisibility(GONE);
            }else{
                switch (showstate){
                    case StateShowInterface.CONTENT:
                        stateHandler.BindNomoreHolder(SimpleViewHolder.createViewHolder(views.get(showstate)),o);
                        break;
                    case StateShowInterface.LOADING:
                        stateHandler.BindLoadingHolder(SimpleViewHolder.createViewHolder(views.get(showstate)),o);
                        break;
                    case StateShowInterface.EMPTY:
                        stateHandler.BindEmptyHolder(SimpleViewHolder.createViewHolder(views.get(showstate)),o);
                        break;
                    case StateShowInterface.ERROR:
                        stateHandler.BindErrorHolder(SimpleViewHolder.createViewHolder(views.get(showstate)),o);
                        break;
                }
                views.get(i).setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void showEmpty() {
        showState(StateShowInterface.EMPTY,null);
    }

    @Override
    public void ShowError() {
        showState(StateShowInterface.ERROR,null);
    }

    @Deprecated
    @Override
    public void showItem() {

    }

    @Override
    public void showLoading() {
        showState(StateShowInterface.LOADING,null);
    }

    @Override
    public void showNomore() {
        showState(StateShowInterface.CONTENT,null);
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

