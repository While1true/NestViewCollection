package com.ck.hello.nestrefreshlib.View.Adpater.Base;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ck on 2017/9/10.
 */

public abstract class BaseAdapterRecord extends RecyclerView.Adapter {
    protected static Recorder globalrecorder;
    public static void init(Recorder globalrecordera) {
        globalrecorder = globalrecordera;
    }
}
