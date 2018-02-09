package com.ck.hello.nestpullview;

import android.app.Application;

import com.nestrefreshlib.RefreshViews.RefreshLayout;

/**
 * Created by 不听话的好孩子 on 2018/2/6.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RefreshLayout.init(new RefreshLayout.DefaultBuilder()
                .setHeaderLayoutidDefault(R.layout.header_layout)
                .setFooterLayoutidDefault(R.layout.footer_layout)
                .setScrollLayoutIdDefault(R.layout.recyclerview)
        );
        System.out.println("xzzzzzzzzzzzzzzzzz"+R.layout.recyclerview);
    }
}
