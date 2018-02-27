package com.ck.hello.nestpullview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.BaseHolder;
import com.nestrefreshlib.Adpater.Impliment.SAdapter;
import com.nestrefreshlib.RefreshViews.AdapterHelper.RefreshHeaderAndFooterAdapterWrap;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.RefreshViews.RefreshWrap.Base.RefreshInnerHandlerImpl;
import com.nestrefreshlib.RefreshViews.RefreshWrap.MyRefreshInnerHandler;
import com.nestrefreshlib.State.StateLayout;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private StateLayout stateLayout;
    private List<SAdapter.DifferCallback.differ> list;
    private SAdapter sAdapter;
    private RefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        stateLayout = findViewById(R.id.statelayout);

        initRefreshLayout();
    }

    public void v1(View v) {
        stateLayout.showLoading();
    }

    public void v2(View v) {
        stateLayout.ShowError();
    }

    public void v3(View v) {
        stateLayout.showEmpty();
    }

    public void v4(View v) {
        stateLayout.showItem();
    }

    public void v5(View v) {
        stateLayout.showNomore();
    }

    private void initRefreshLayout() {
        list = new ArrayList<>();
        addlist();

        layout = findViewById(R.id.refreshing);
        final RecyclerView recyclerView = layout.getmScroll();
        layout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.NotifyCompleteRefresh0();
                    }
                }, 1000);
            }

            @Override
            public void Loading() {
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.NotifyCompleteRefresh0();
                    }
                }, 1000);
            }
        });
        ArrayList<SAdapter.DifferCallback.differ> objects = new ArrayList<>(list);
        sAdapter = new SAdapter(objects)
                .addType(new BaseHolder<aa>(R.layout.footer) {

                    @Override
                    public void onViewBind(Holder holder, aa item, int position) {
                        holder.itemView.setBackgroundColor(0xffff0000);

                    }
                })
                .addType(new BaseHolder<bb>(R.layout.footer) {

                    @Override
                    public void onViewBind(Holder holder, bb item, int position) {
                        holder.itemView.setBackgroundColor(0xffffff00);

                    }

                })
                .addType(new BaseHolder<cc>(R.layout.footer) {

                    @Override
                    public void onViewBind(Holder holder, cc item, int position) {
                        holder.itemView.setBackgroundColor(0xffff00ff);
                    }

                    @Override
                    public boolean isfull() {
                        return true;
                    }
                })
                .addType(new BaseHolder<dd>(R.layout.footer) {

                    @Override
                    public void onViewBind(Holder holder, dd item, int position) {
                        holder.itemView.setBackgroundColor(0xff6f702f);
                    }
                })
                .addLifeOwener(this);
//        layout.setAdapter(sAdapter);
        layout.setInnerAdapter(new RefreshHeaderAndFooterAdapterWrap(sAdapter).attachView(layout), new MyRefreshInnerHandler(), new LinearLayoutManager(this));
    }

    private void addlist() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                list.add(new aa());
            } else if (i % 7 == 0) {
                list.add(new bb());
            } else if (i % 3 == 0) {
                list.add(new cc());
            } else {
                list.add(new dd());
            }
        }
    }

}
