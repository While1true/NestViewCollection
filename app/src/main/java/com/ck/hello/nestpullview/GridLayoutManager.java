package com.ck.hello.nestpullview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.BaseAdapterRecord;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GridLayoutManager extends AppCompatActivity implements View.OnClickListener {

    private BaseAdapterRecord adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("GridLayoutManager");
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
      adapter= new SBaseMutilAdapter<String,String>(list)
                .addType(R.layout.test1, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.tv, item + "类星2 ");
                        holder.setBackgroundColor(R.id.tv, 0xff228866);
                    }
                    @Override
                    public boolean istype(String item, int position) {
                        return position % 4 == 2;
                    }
                })
                .addType(R.layout.test1, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.tv, item + "类星0 ");
                        holder.setBackgroundColor(R.id.tv, 0xff666666);
                    }

                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return 2;
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return position % 4 == 0;
                    }
                }).addType(R.layout.test1, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.tv, item + "类星1 ");
                        holder.setBackgroundColor(R.id.tv, 0xff226666);
                    }
                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return 2;
                    }
                    @Override
                    public boolean istype(String item, int position) {
                        return position % 4 == 1;
                    }
                })

                .addType(R.layout.test3, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.tv, item + "类星3 ");
                        holder.itemView.setBackgroundColor(0xff662266);
                    }

                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return 5;
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return true;
                    }
                })
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
                .setAdapter(new android.support.v7.widget.GridLayoutManager(this, 5, android.support.v7.widget.GridLayoutManager.VERTICAL, false), adapter)
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
                        Toast.makeText(GridLayoutManager.this, "滑倒底部，正在预加载", Toast.LENGTH_LONG).show();
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
