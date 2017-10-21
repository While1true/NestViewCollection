package com.ck.hello.nestpullview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SingleLayoutManager extends AppCompatActivity implements View.OnClickListener {

    private SBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SingleLayoutManager");
        List<String> list = new ArrayList<>(56);
        for (int i = 0; i < 55; i++) {
            list.add(i + "");
        }
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        final SRecyclerView recyclerView = (SRecyclerView) findViewById(R.id.sre);
        adapter = new SBaseAdapter<String>(list,R.layout.test3) {
            @Override
            protected void onBindView(SimpleViewHolder holder, String item, int position) {
                holder.setText(R.id.tv, item + "--1");
                holder.setBackgroundColor(R.id.tv, 0xff226666);
            }
        }
                .setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        adapter.showState(SBaseAdapter.SHOW_LOADING, "ggg");
                        Toast.makeText(context, "ddd", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void showEmpty(Context context) {
                        super.showEmpty(context);
                        Toast.makeText(context, "ddad", Toast.LENGTH_LONG).show();
                    }
                });



        recyclerView.addDefaultHeaderFooter()
                .setRefreshMode(true, true, true, true)
                .setAdapter(new android.support.v7.widget.LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL, false), adapter)
                .setRefreshingListener(new SRecyclerView.OnRefreshListener() {
                    @Override
                    public void Refreshing() {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.showState(SBaseAdapter.SHOW_NOMORE, "ggg");
                                recyclerView.notifyRefreshComplete();
                            }
                        }, 1000);


                    }

                    @Override
                    public void PreLoading() {
                        super.PreLoading();
                        Toast.makeText(SingleLayoutManager.this, "滑倒底部，正在预加载", Toast.LENGTH_LONG).show();
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.notifyRefreshComplete();
                            }
                        }, 5000);
                    }

                    @Override
                    public void Loading() {
                        super.Loading();
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.notifyRefreshComplete();
                            }
                        }, 1000);
                    }
                }).setRefreshing();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                adapter.showLoading();
                break;
            case R.id.button1:
                adapter.showItem();
                break;
            case R.id.button2:
                adapter.showState(SBaseAdapter.SHOW_NOMORE, "没有更多了");
                break;
            case R.id.button3:
                adapter.ShowError();
                break;
            case R.id.button4:
                adapter.showEmpty();
                break;
        }
    }
}
