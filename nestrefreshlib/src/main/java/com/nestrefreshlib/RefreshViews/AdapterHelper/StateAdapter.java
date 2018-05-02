package com.nestrefreshlib.RefreshViews.AdapterHelper;

import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.ViewGroup;

import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.SAdapter;
import com.nestrefreshlib.State.Interface.BaseStateListener;
import com.nestrefreshlib.State.Interface.Recorder;
import com.nestrefreshlib.State.Interface.ShowStateInterface;
import com.nestrefreshlib.State.Interface.StateEnum;
import com.nestrefreshlib.State.Interface.StateHandlerInterface;

import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/3/14.
 */

public class StateAdapter extends SAdapter implements ShowStateInterface {
    private Object object;


    public StateAdapter(List<?> list) {
        super(list);
        init();
    }

    public StateAdapter(int count) {
        super(count);
        init();
    }

    public StateAdapter() {
        super();
        init();
    }

    //全局id记录者
    protected static Recorder globalrecorder;
    //状态onindBView
    protected StateHandlerInterface stateHandler;

    private StateEnum showstate = StateEnum.TYPE_ITEM;
    private static final int BASE_INDEX = 99999;

    private ArrayMap<StateEnum, Integer> viewids = new ArrayMap<>(4);

    private void init() {
        Recorder recorder = globalrecorder;
        if (globalrecorder == null)
            recorder = new Recorder.Builder().build();
        viewids.put(StateEnum.SHOW_EMPTY, recorder.getEmptyres());
        viewids.put(StateEnum.SHOW_NOMORE, recorder.getNomore());
        viewids.put(StateEnum.SHOW_LOADING, recorder.getLoadingres());
        viewids.put(StateEnum.SHOW_ERROR, recorder.getErrorres());

        try {
            stateHandler = recorder.getClazz().newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public StateAdapter setStateHandler(StateHandlerInterface stateHandler) {
        this.stateHandler = stateHandler;
        return this;
    }

    @Override
    public int setIfGridLayoutManagerSpan(int itemViewType, int position, int spanCount) {
        if (itemViewType >= BASE_INDEX && itemViewType <= BASE_INDEX + 3) {
            return spanCount;
        }
        return super.setIfGridLayoutManagerSpan(itemViewType, position, spanCount);
    }

    @Override
    public boolean setIfStaggedLayoutManagerFullspan(int itemViewType) {
        if (itemViewType >= BASE_INDEX && itemViewType <= BASE_INDEX + 3) {
            return true;
        }
        return super.setIfStaggedLayoutManagerFullspan(itemViewType);
    }

    /**
     * 设置状态布局的监听
     *
     * @param listener
     * @return
     */
    public StateAdapter setStateListener(BaseStateListener listener) {
        stateHandler.setStateClickListener(listener);
        return this;
    }

    public void setLayoutId(StateEnum showstate, int layoutid) {
        viewids.put(showstate, layoutid);
    }

    /**
     * 全局初始化状态 layout id
     *
     * @param globalRecordera
     */
    public static void init(Recorder globalRecordera) {
        globalrecorder = globalRecordera;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType>=BASE_INDEX) {
            return Holder.createViewHolder(InflateView(viewids.get(showstate), parent));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected int getType(int position) {
        switch (showstate) {
            case TYPE_ITEM:
                return super.getType(position);
            case SHOW_NOMORE:
                if (position == getItemCount() - 1)
                    return BASE_INDEX + showstate.ordinal();
                else
                    return super.getType(position);
            default:
                return BASE_INDEX + showstate.ordinal();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (showstate.ordinal() < 4) {
            switch (showstate) {
                case SHOW_EMPTY:
                    stateHandler.BindEmptyHolder((Holder) holder, object);
                    break;
                case SHOW_ERROR:
                    stateHandler.BindErrorHolder((Holder) holder, object);
                    break;
                case SHOW_LOADING:
                    stateHandler.BindLoadingHolder((Holder) holder, object);
                    break;
                case SHOW_NOMORE:
                    if (position == getItemCount() - 1)
                        stateHandler.BindNomoreHolder((Holder) holder, object);
                    else super.onBindViewHolder(holder, position);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }

    }

    @Override
    public int getItemCount() {
        if (showstate.ordinal() < 3) {
            return 1;
        } else if (showstate.ordinal() == 3) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public void showState(StateEnum showstate, Object o) {
        object = o;
        this.showstate = showstate;
        notifyDataSetChanged();
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
}
