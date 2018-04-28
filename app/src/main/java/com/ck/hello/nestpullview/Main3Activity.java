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
import com.nestrefreshlib.Adpater.Impliment.SAdapter;
import com.nestrefreshlib.RecyclerviewFloatHelper.*;
import com.nestrefreshlib.RecyclerviewFloatHelper.RecyclerviewFloatHelper;
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
        sAdapter = new SAdapter(objects)
                .addType(new BaseHolder<xx>(R.layout.xxx) {
                    @Override
                    public void onViewBind(Holder holder, xx item, int position) {
                        holder.setText(R.id.tv, "xxx" + position);
                        holder.itemView.setBackgroundColor(0xffff4070);
                    }
                })
                .addType(new BaseHolder<aa>(R.layout.footer) {

                    @Override
                    public void onViewBind(Holder holder, aa item, int position) {
                        if (position == 2) {
                            holder.itemView.setBackgroundColor(0xff666666);
                        } else {
                            holder.itemView.setBackgroundColor(0xffffffff);
                        }


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
                    public boolean istype(Object item, int position) {
                        return super.istype(item, position);
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
        recyclerView1 = layout.getmScroll();
//        recyclerView1.setAdapter(sAdapter);
//        recyclerView.addOnScrollListener(new AdapterScrollListener(layout));
//        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        new RefreshAdapterHandler().attachRefreshLayout(layout, sAdapter, new LinearLayoutManager(this));
        v1(null);
    }

    private void addlist() {
        for (int i = 0; i < 100000; i++) {
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
        floatInterface = new PositionFloatView(fm, 11, 21, 36,38);
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
}
