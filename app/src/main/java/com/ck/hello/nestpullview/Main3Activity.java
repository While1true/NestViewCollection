package com.ck.hello.nestpullview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.BaseHolder;
import com.nestrefreshlib.Adpater.Impliment.PositionHolder;
import com.nestrefreshlib.Adpater.Impliment.SAdapter;
import com.nestrefreshlib.RecyclerviewFloatHelper.*;
import com.nestrefreshlib.RecyclerviewFloatHelper.RecyclerviewFloatHelper;
import com.nestrefreshlib.RefreshViews.AdapterHelper.AdapterScrollListener;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.nestrefreshlib.RefreshViews.RefreshWrap.RefreshAdapterHandler;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private List<SAdapter.DifferCallback.differ> list;
    private SAdapter sAdapter;
    private RefreshLayout layout;
    private FrameLayout fm;
    private RecyclerView recyclerView1;
    private RecyclerviewFloatHelper.FloatInterface floatInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        fm = findViewById(R.id.fm);

        initRefreshLayout();
    }

    private void initRefreshLayout() {
        list = new ArrayList<>();
        addlist();

        layout = findViewById(R.id.refreshing);
        final RecyclerView recyclerView = layout.getmScroll();
        layout.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.NotifyCompleteRefresh0();
                    }
                }, 3000);
            }

            @Override
            public void Loading() {
                TextView tv = layout.findInFooterView(R.id.textView);
                tv.setText("加载完成");
                Toast.makeText(Main3Activity.this, "加载了", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.NotifyCompleteRefresh0();
                    }
                }, 1000);
            }

            @Override
            public void call(RefreshLayout.State t, int scroll) {
                super.call(t, scroll);
            }
        });
        ArrayList<SAdapter.DifferCallback.differ> objects = new ArrayList<>(list);
        sAdapter = new SAdapter(Integer.MAX_VALUE-5)
                .addType(R.layout.xxx, new PositionHolder() {
                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.setText(R.id.tv, "xxx" + position);
                        holder.itemView.setBackgroundColor(0xffff4070);
                    }

                    @Override
                    public boolean istype(int position) {
                        return position % 3 == 0;
                    }
                })
                .addType(R.layout.footer, new PositionHolder() {

                    @Override
                    public void onBind(Holder holder, int position) {
                        if (position == 2) {
                            holder.itemView.setBackgroundColor(0xff666666);
                        } else {
                            holder.itemView.setBackgroundColor(0xffffffff);
                        }

                    }

                    @Override
                    public boolean istype(int position) {
                        return position % 7 == 0;
                    }
                })
                .addType(R.layout.footer, new PositionHolder() {

                    @Override
                    public void onBind(Holder holder, int position) {
                        holder.itemView.setBackgroundColor(0xff88320);
                    }

                    @Override
                    public boolean istype(int position) {
                        return position % 11 == 0;
                    }

                }).addType(R.layout.footer, new PositionHolder() {

            @Override
            public void onBind(Holder holder, int position) {
                holder.itemView.setBackgroundColor(0xffff00ff);
            }

            @Override
            public boolean istype(int position) {
                return true;
            }

        })
                .addLifeOwener(this);
        recyclerView1 = layout.getmScroll();
        recyclerView1.setAdapter(sAdapter);
        recyclerView.addOnScrollListener(new AdapterScrollListener(layout));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
//        new RefreshAdapterHandler().attachRefreshLayout(layout, sAdapter, new LinearLayoutManager(this));
        v2(null);
    }

    private void addlist() {
        for (int i = 0; i < 100; i++) {
            if (i == 10 || i == 20 || i == 35) {
                list.add(new xx());
            } else if (i % 2 == 0) {
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

    public void v1(View view) {
        if (floatInterface != null) {
            floatInterface.detachRecyclerview();
        }
        floatInterface = new PositionFloatView(fm, 0,2,7,8,12,17);
        floatInterface.attachRecyclerview(recyclerView1);
        floatInterface.setOnFloatClickListener(new RecyclerviewFloatHelper.OnFloatClickListener() {
            @Override
            public void onClick(View v, int position) {
                recyclerView1.smoothScrollToPosition(position);
                Toast.makeText(v.getContext(), "xxxx: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void v2(View view) {
        if (floatInterface != null) {
            floatInterface.detachRecyclerview();
        }
        floatInterface = new ViewTypeFloatView(fm, 0);
        floatInterface.attachRecyclerview(recyclerView1);
        floatInterface.setOnFloatClickListener(new RecyclerviewFloatHelper.OnFloatClickListener() {
            @Override
            public void onClick(View v, int position) {
                recyclerView1.smoothScrollToPosition(position);
                Toast.makeText(v.getContext(), "xxxx: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
int xx=100000;
    public void v3(View view) {
        recyclerView1.scrollToPosition(xx+=200000);
    }
}
