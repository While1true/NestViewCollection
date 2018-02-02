package com.nestrefreshlib.Adpater.Base;

/**
 * Created by ck on 2017/10/21.
 */
public abstract class ItemHolder<T> {
    private int layout;
    /**
     * StaggeredLayoutManager重写此方法
     *
     * @return 是否占据一个
     */
    public boolean isfull() {
        return false;
    }

    /**
     * GrideLayoutManager重写此方法
     *
     * @param position
     * @return 占的个数
     */
    public int gridSpanSize(T item, int position) {
        return 1;
    }

    /**
     * onBind时调用
     *
     * @param holder
     * @param item
     * @param position
     */
    public abstract void onBind(Holder holder, T item, int position);

    /**
     * 是否是这种Type的view
     *
     * @param item
     * @param position
     * @return
     */
    public boolean istype(Object item, int position){
        return true;
    }

    public int getLayout() {
        return layout;
    }

    public ItemHolder<T> setLayout(int layout) {
        this.layout = layout;
        return this;
    }
}