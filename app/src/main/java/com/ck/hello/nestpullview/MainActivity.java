package com.ck.hello.nestpullview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ck.hello.nestrefreshlib.View.RefreshViews.SRecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SRecyclerView recyclerView = (SRecyclerView) findViewById(R.id.sre);
        recyclerView.addDefaultHeaderFooter()
                .setRefreshMode(true, true, true, true)
                .setAdapter(new LinearLayoutManager(this), new RecyclerView.Adapter() {
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        TextView textView = new TextView(parent.getContext());
                        textView.setText("dsfdsds");
                        return new RecyclerView.ViewHolder(textView) {
                            @Override
                            public String toString() {
                                return super.toString();
                            }
                        };
                    }

                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                    }

                    @Override
                    public int getItemCount() {
                        return 20;
                    }
                }).setRefreshingListener(new SRecyclerView.OnRefreshListener() {
            @Override
            public void Refreshing() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                        recyclerView.notifyRefreshComplete();
                    }
                }, 1000);
            }
        });
    }
}
