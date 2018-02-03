package com.ck.hello.nestpullview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;


import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Impliment.BaseHolder;
import com.nestrefreshlib.Adpater.Impliment.DefaultStateListener;
import com.nestrefreshlib.Adpater.Impliment.SAdapter;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            if(i%2==0){
                list.add(1.3f);
            }else if(i%7==0){
                list.add(new Rect());
            }else if(i%3==0){
                list.add(1);
            }else{
                list.add(1.5D);
            }
        }
       RecyclerView recyclerView=findViewById(R.id.recyxlerview);
        SAdapter sAdapter = new SAdapter(list)
                .addType(new BaseHolder<String>(R.layout.nomore) {

                    @Override
                    public void onViewBind(Holder holder, String item, int position) {
                        holder.itemView.setBackgroundColor(0xffff0000);
                        holder.setText(R.id.tv_nomore, position + "");

                    }
                })
                .addType(new BaseHolder<Rect>(R.layout.nomore) {

                    @Override
                    public void onViewBind(Holder holder, Rect item, int position) {
                        holder.itemView.setBackgroundColor(0xffffff00);
                        holder.setText(R.id.tv_nomore, position + "");

                    }

                })
                .addType(new BaseHolder<Integer>(R.layout.nomore) {

                    @Override
                    public void onViewBind(Holder holder, Integer item, int position) {
                        holder.itemView.setBackgroundColor(0xffff00ff);
                        holder.setText(R.id.tv_nomore, position + "");
                    }

                    @Override
                    public boolean isfull() {
                        return true;
                    }
                })
                .addType(new BaseHolder<Object>(R.layout.nomore) {

                    @Override
                    public void onViewBind(Holder holder, Object item, int position) {
                        holder.itemView.setBackgroundColor(0xff6f702f);
                        holder.setText(R.id.tv_nomore, position + "");
                    }
                })
                .setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {

                    }
                })
                .addLifeOwener(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL));
        recyclerView.setAdapter(sAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
