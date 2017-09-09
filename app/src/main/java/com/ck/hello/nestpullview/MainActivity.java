package com.ck.hello.nestpullview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateClickListener;
import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> list = new ArrayList<>(50);
        for (int i = 0; i < 54; i++) {
            list.add(i + "");
        }
        final SRecyclerView recyclerView = (SRecyclerView) findViewById(R.id.sre);
        final SBaseMutilAdapter<String,String> adapter = new SBaseMutilAdapter<>(list)
               .addType(R.layout.nomore, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.tv_nomore, item);
                    }

                    @Override
                    public boolean istype(int position) {
                        return position % 3 == 0;
                    }
                }).addType(R.layout.network_error, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setButtonText(R.id.reload, item + "button");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position % 3 == 1;
                    }
                }).addType(R.layout.empty_textview, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.tv, item + "tv");
                    }

                    @Override
                    public boolean istype(int position) {
                        return position % 3 == 2;
                    }})
                .setStateListener(new StateClickListener() {
                    @Override
                    public void netError(Context context) {
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
                .setAdapter(new LinearLayoutManager(this), adapter).setRefreshingListener(new SRecyclerView.OnRefreshListener() {
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
            public void Loading() {
                super.Loading();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.showState(SBaseAdapter.SHOW_ERROR, "ggg");
                        recyclerView.notifyRefreshComplete();
                    }
                }, 1000);
            }
        }).setRefreshing();
    }
}
