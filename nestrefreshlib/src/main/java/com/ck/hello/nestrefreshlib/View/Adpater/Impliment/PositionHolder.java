package com.ck.hello.nestrefreshlib.View.Adpater.Impliment;

import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder;
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder;

/**
 * Created by ck on 2017/10/21.
 */

public abstract class PositionHolder extends ItemHolder {
    @Override
    public void onBind(Holder holder, Object item, int position) {
        onBind(holder,position);
    }

    @Override
    public boolean istype(Object item, int position) {
        return istype(position);
    }

    public abstract void onBind(Holder holder, int position);

    public abstract boolean istype(int position);
}
