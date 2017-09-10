package com.ck.hello.nestpullview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.View;

import com.ck.hello.nestrefreshlib.View.Adpater.StateListener;

/**
 * Created by ck on 2017/9/10.
 */

public class RKActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rk);
        findViewById(R.id.GridLayoutManager).setOnClickListener(this);
        findViewById(R.id.StaggledLayoutManager).setOnClickListener(this);
        findViewById(R.id.LinearLayoutManager).setOnClickListener(this);
        findViewById(R.id.SingleTypeAdapter).setOnClickListener(this);
        StateListener a= new StateListener(){
            @Override
            public void netError(Context context) {

            }
        };
        StateListener b=a;
        a=null;
        Log.i("Debug", "onCreate: "+(b==null));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.GridLayoutManager:
                startActivity(new Intent(this, GridLayoutManager.class));
                break;
            case R.id.StaggledLayoutManager:
                startActivity(new Intent(this,StaggedLayoutManager.class));
                break;
            case R.id.LinearLayoutManager:
                startActivity(new Intent(this,LinearLayoutManager.class));
                break;
            case R.id.SingleTypeAdapter:
                startActivity(new Intent(this,SingleLayoutManager.class));
                break;
        }

    }
}
