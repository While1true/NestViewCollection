package com.ck.hello.nestpullview;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;


import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.BaseHolder;
import com.nestrefreshlib.Adpater.Impliment.SAdapter;
import com.nestrefreshlib.RefreshViews.RefreshWrap.MyRefreshInnerWrap;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.State.StateLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private StateLayout stateLayout;
    private List<SAdapter.DifferCallback.differ> list;
    private SAdapter sAdapter;

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
        addlist();
        ArrayList<SAdapter.DifferCallback.differ> objects = new ArrayList<>(list);
        sAdapter.differUpdate(objects);

    }
    private void initRefreshLayout() {
        list = new ArrayList<>();
        addlist();

        final RefreshLayout layout = findViewById(R.id.refreshing);
        final RecyclerView recyclerView = layout.getmScroll();
        layout.setListener(new RefreshLayout.Callback() {
            @Override
            public void call(RefreshLayout.State t) {

                if (t == RefreshLayout.State.REFRESHING) {
                    getWindow().getDecorView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.NotifyCompleteRefresh0();
                        }
                    }, 3000);

                } else if (t == RefreshLayout.State.LOADING) {
                    getWindow().getDecorView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layout.NotifyCompleteRefresh0();
                        }
                    }, 3000);

                }
            }
        });
        ArrayList<SAdapter.DifferCallback.differ> objects = new ArrayList<>(list);
        sAdapter = new SAdapter(objects)
                .addType(new BaseHolder<aa>(R.layout.state_nomore) {

                    @Override
                    public void onViewBind(Holder holder, aa item, int position) {
                        holder.itemView.setBackgroundColor(0xffff0000);
                        holder.setText(R.id.tv_nomore, position + "");

                    }
                })
                .addType(new BaseHolder<bb>(R.layout.state_nomore) {

                    @Override
                    public void onViewBind(Holder holder, bb item, int position) {
                        holder.itemView.setBackgroundColor(0xffffff00);
                        holder.setText(R.id.tv_nomore, position + "");

                    }

                })
                .addType(new BaseHolder<cc>(R.layout.state_nomore) {

                    @Override
                    public void onViewBind(Holder holder, cc item, int position) {
                        holder.itemView.setBackgroundColor(0xffff00ff);
                        holder.setText(R.id.tv_nomore, position + "");
                    }

                    @Override
                    public boolean isfull() {
                        return true;
                    }
                })
                .addType(new BaseHolder<dd>(R.layout.state_nomore) {

                    @Override
                    public void onViewBind(Holder holder, dd item, int position) {
                        holder.itemView.setBackgroundColor(0xff6f702f);
                        holder.setText(R.id.tv_nomore, position + "");
                    }
                })
                .addLifeOwener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        MyRefreshInnerWrap.setInnerRecyclerviewAdapter(layout, sAdapter);
    }

    private void addlist() {
        for (int i = 0; i <20; i++) {
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
