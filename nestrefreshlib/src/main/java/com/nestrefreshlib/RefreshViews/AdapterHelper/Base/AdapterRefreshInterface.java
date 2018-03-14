package com.nestrefreshlib.RefreshViews.AdapterHelper.Base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 不听话的好孩子 on 2018/3/14.
 */

public  interface AdapterRefreshInterface {
    View getHeader();

    View getFooter();

    RecyclerView.Adapter getWrapAdapter();
}