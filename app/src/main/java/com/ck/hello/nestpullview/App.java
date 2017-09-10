package com.ck.hello.nestpullview;

import android.app.Application;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder;
import com.ck.hello.nestrefreshlib.View.Adpater.DefaultStateHandler;
import com.ck.hello.nestrefreshlib.View.Adpater.SBaseMutilAdapter;

/**
 * Created by ck on 2017/9/10.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SBaseMutilAdapter.init(new Recorder.Builder()
        .setStateHandlerClazz(DefaultStateHandler.class)
        .build());
    }
}
