package com.ck.hello.nestpullview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener;
import com.ck.hello.nestrefreshlib.View.State.StateLayout;

public class Main2Activity extends AppCompatActivity {

    private StateLayout stateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateLayout = new StateLayout(this)
                .setContent(R.layout.activity_main2)
                .setStateListener(new DefaultStateListener() {
                    @Override
                    public void showEmpty(Context context) {
                        super.showEmpty(context);
                        stateLayout.ShowError();
                        Toast.makeText(Main2Activity.this,"fftf",0).show();
                    }

                    @Override
                    public void netError(Context context) {
                        stateLayout.showEmpty();
                        Toast.makeText(Main2Activity.this,"fftf",0).show();
                    }
                });
        setContentView(stateLayout);
        stateLayout.showLoading();
        stateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                stateLayout.ShowError();
            }
        },2000);
    }
}
