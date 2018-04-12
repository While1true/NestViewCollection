package com.nestrefreshlib.Adpater.Impliment;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.nestrefreshlib.Adpater.Base.Holder;
import com.nestrefreshlib.Adpater.Base.ItemHolder;
import com.nestrefreshlib.Adpater.Base.MyCallBack;

import java.lang.ref.WeakReference;


public abstract class BaseHolder<T> extends ItemHolder<T> {
    private MyCallBack<T> itemClickCallBack;
    private WeakReference<Context> context;

    public final MyCallBack getItemClickCallBack() {
        return itemClickCallBack;
    }

    public final void setItemClickCallBack(MyCallBack myCallback) {
        this.itemClickCallBack = myCallback;
    }

    public final void onBind(Holder p0, final T p1, int p2) {
        this.onViewBind(p0, p1, p2);
        if (itemClickCallBack != null) {
            p0.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickCallBack.call(p1);
                }
            });
        }

    }

    public final void setContext(Context context) {
        this.context = new WeakReference(context);
    }

    public final Context getContext() {
        return this.context != null ? context.get() : null;
    }

    public abstract void onViewBind(Holder holder, T item, int position);

    public BaseHolder() {
    }

    public BaseHolder(int layoutId) {
        this.setLayout(layoutId);
    }

    public BaseHolder(int layoutId, Context context) {
        this(layoutId);
        this.setContext(context);
    }

    @Override
    public boolean istype(Object item, int position) {
        if(item==null){
            return true;
        }
        String plix = getClass().getGenericSuperclass().toString().split("<")[1];
        String type=plix;
        if(plix.endsWith(">")) {
            type= plix.substring(0, plix.length() - 1);
        }
        return item.getClass().getName().equals(type) || Object.class.getName().equals(type);
    }
}
