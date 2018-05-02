## Refreshlayout Adapter Statelayout

## Refreshlayout
- 刷新库部分包含一下功能：
1.普通上拉刷新，下拉加载 
2.类似小米系统的橡皮泥模式
3.估值模式，直提供上下拉过程中的数值，可监听设置listener做你想要的事情
4.估值模式下的内刷新，与普通刷新相比，它是控制adapter最前面一个和最后面一个的高度来显示刷新头尾
![GIF.gif](http://upload-images.jianshu.io/upload_images/6456519-27f56d146baa0afb.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![GIF2.gif](http://upload-images.jianshu.io/upload_images/6456519-9e217be853b06569.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![GIF3.gif](http://upload-images.jianshu.io/upload_images/6456519-9c010d100480914e.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![GIF5.gif](https://github.com/While1true/NestViewCollection/blob/goldenVersion_Simple2/GIF.gif)

![GIF4.gif](https://upload-images.jianshu.io/upload_images/6456519-8166c67d8a762c32.gif?imageMogr2/auto-orient/strip)



```
 <coms.kxjsj.refreshlayout_master.RefreshLayout
        android:id="@+id/Refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:canFooter="true"
        app:footerID="@layout/footer_layout_horizontal"
        app:headerID="@layout/header_layout_horizontal"
        app:scrollID="@layout/xx" />
```

## 多类型Adapter使用 
> adpter包含以下主要功能：
1.多类型item的支持 通过addtype
2.不同模式下列宽的控制
3.只设置position 的，估值模式 
4.增量更新集成
5.复杂数据根据泛型自动匹配类型，也可重写istype自定义什么情况下是该类型
6.移除了状态布局的支持
![2017-09-10-15-46-16.png](https://github.com/While1true/NestPullView/blob/goldenVersion3/3YDT3SQ9EZKC48%25II_RVSG6.png)

> 默认根据itemholder的泛型确定相应position的类型
![2017-09-10-15-46-16.png](https://github.com/While1true/NestPullView/blob/goldenVersion3/0X%7EMX4_7I%7E%24RQ%7B%7DOM1YAJYQ.png)
## 状态布局的封装
>  原adapter的状态布局单独做成一个statlayout，已增加对非recyclerview的支持，用法与之前一样
![2017-09-10-15-46-16.png](https://github.com/While1true/NestPullView/blob/goldenVersion3/3.png)
## 更多功能
## itemholder 重写相应方法可达到gridlayoutManager staggled设置列宽
```
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







