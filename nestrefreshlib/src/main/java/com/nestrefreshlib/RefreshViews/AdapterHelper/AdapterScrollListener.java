package com.nestrefreshlib.RefreshViews.AdapterHelper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.nestrefreshlib.RefreshViews.RefreshLayout;

/**
 * Created by ck on 2018/3/3.
 */

public class AdapterScrollListener extends RecyclerView.OnScrollListener {
    private RefreshLayout layout;
    private Callback callbackx;
    private AdapterScrollListener(){

    }
    public AdapterScrollListener(RefreshLayout layout){
        this.layout=layout;
    }
    public AdapterScrollListener(Callback callback){
        this.callbackx=callback;
    }
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState==RecyclerView.SCROLL_STATE_IDLE){
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int currentlast=-1;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            int itemCount = adapter.getItemCount();
            if(layoutManager instanceof StaggeredGridLayoutManager){
                int []current=new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(current);
                for (int i : current) {
                    if(i>currentlast){
                        currentlast=i;
                    }
                }
            }else{
                currentlast=((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }

            if(itemCount-1==currentlast){
                if(layout!=null) {
                    RefreshLayout.Callback callback = layout.getCallback();
                    if (callback != null) {
                        callback.call(RefreshLayout.State.LOADING);
                    }
                }
                if(callbackx!=null){
                    callbackx.call();
                }
            }

        }
    }
    public interface Callback{
        void call();
    }
}
