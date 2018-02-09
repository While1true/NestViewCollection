## Refreshlayout Adapter Statelayout

## Refreshlayout
> 刷新库部分包含一下功能：
1.普通上拉刷新，下拉加载 
2.类似小米系统的橡皮泥模式
3.估值模式，直提供上下拉过程中的数值，可监听设置listener做你想要的事情
4.估值模式下的内刷新，与普通刷新相比，它是控制adapter最前面一个和最后面一个的高度来显示刷新头尾
![GIF.gif](http://upload-images.jianshu.io/upload_images/6456519-27f56d146baa0afb.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![GIF2.gif](http://upload-images.jianshu.io/upload_images/6456519-9e217be853b06569.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![GIF3.gif](http://upload-images.jianshu.io/upload_images/6456519-9c010d100480914e.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
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



---
## [github 地址](https://github.com/While1true/SuperAdapter)
## [简书 地址](http://www.jianshu.com/p/3e413feabdcd)
## [示例apk 地址](https://github.com/While1true/SuperAdapter/blob/master/app-debug.apk)

### 做了些什么？
### Recyclerview
1. 封装了下拉刷新，便于扩展
2. 支持预加载一件设置
3. 多种刷新下拉模式支持
---
 srecyclerview
                .addDefaultHeaderFooter()
                .setRefreshMode(false,false,false,false)
                .setAdapter(manager, adapter);

  更多详细看之前文档
---


### Apdapter部分
1. 封装了错误布局，空布局，底部更多布局
2. 支持多类型布局 
3. 支持GridLayoutManage 设置单个spancount
4. 支持StaggledLayoutManager 设置单个类型item、全屏
5. 支付扩展状态布局类型，调用等
6. 支持list模式的多类型布局，和非list类型的多类型布局

---
list模式
  adapter = new SAdapter(List<String>)
                .addType(R.layout.label_layout, new StringHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                    }

                    @Override
                    public boolean istype(String item, int position) {
                        return true;
                    }
                })
                .addType(){...}
                .setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        loadData();
                    }
                });

非list模式
    adapter=new SAdapter(100)
                .addType(R.layout.conversation, new PositionHolder() {
                    @Override
                    public void onBind(SimpleViewHolder holder, int position) {

                    }

                    @Override
                    public boolean istype(int position) {
                        return false;
                    }
                })
                .addType()

切换状态
    void showState(int showstate, E e);

    //不需要传递改变就调用如下
    void showEmpty();

    void ShowError();

    void showItem();

    void showLoading();

---



### StateLayout
1. 封装了错误布局，空布局，底部更多布局

---
切换状态
    void showState(int showstate, E e);

    //不需要传递改变就调用如下
    void showEmpty();

    void ShowError();

    void showItem();

    void showLoading();

    void showNomore();
---

---
### 1.效果

![2017-09-10-15-46-16.gif](http://upload-images.jianshu.io/upload_images/6456519-6ca0f77cb7dceb4d.gif?imageMogr2/auto-orient/strip)
### 2.使用
### -  简单使用

切换状态
```

    /*params showstate  SHOW_EMPTY、SHOW_LOADING 、SHOW_ERROR、SHOW_NOMORE、TYPE_ITEM
    *@ params E 传递给相应布局数据
    */
    void showState(int showstate, E e);

    //不需要传递改变就调用如下
    void showEmpty();

    void ShowError();

    void showItem();

    void showLoading();

    void showNomore();
```

  
```
单item类型
 final SRecyclerView recyclerView = (SRecyclerView) findViewById(R.id.sre);
        adapter = new SBaseAdapter<String>(list,R.layout.test3) {
            @Override
            protected void onBindView(SimpleViewHolder holder, String item, int position) {
                holder.setText(R.id.tv, item + "--1");
                holder.setBackgroundColor(R.id.tv, 0xff226666);
            }
        }
            

```

```
多item类型
new SBaseMutilAdapter(list)
               
                .addType(R.layout.test1, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, int position) {
                        holder.setText(R.id.tv, item + "类0 ");
                        holder.setBackgroundColor(R.id.tv, 0xff666666);
                    }
                     @Override
                    public boolean istype(String item, int position) {
                        return item.type==1;
                    }
                })
                 .addType(R.layout.test1, new SBaseMutilAdapter.ITEMHOLDER<String>() {
                    @Override
                    public void onBind(SimpleViewHolder holder, String item, 
                     .....
                 })

```


- 设置状态布局相应按钮的监听
```
    .setStateListener(new DefaultStateListener() {
                    @Override
                    public void netError(Context context) {
                        adapter.showState(SBaseAdapter.SHOW_LOADING, "ggg");
                        Toast.makeText(context, "ddd", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void showEmpty(Context context) {
                        super.showEmpty(context);
                        Toast.makeText(context, "ddad", Toast.LENGTH_LONG).show();
                    }
                });
```
- 设置GridLayoutManager staggedlayoutManager item占的空间

```
                 //如果是GridelayoutManager 重写ITEMHOLDER此方法返回item占据大小
                    @Override
                    protected int gridSpanSize(String item, int position) {
                        return 2;
                    }
                    //如果是staggedlayoutManager 重写ITEMHOLDER此方法返回
                   //是该类型布局就return true
                    @Override
                    protected boolean isfull() {
                        return super.isfull();
                    }
```
### - 自定义使用
-    在application中配置全局的资源id

```
 BaseAdapterRecord.init(new Recorder.Builder()
                .setEmptyRes()
                .setNomoreRes()
                .setLoadingRes()
                .setErrorRes()
                .setStateHandlerClazz(DefaultStateHandler.class)
                .build());
```
- 自定义状态布局处理handler
```
实现StateHandlerInterface接口
可以仿照默认的DefaultStateHandler来编写

    /**
     * 用于传递相应的监听 在Adapter设置监听
     * @param listener 继承BaseStateListener 定义自己想要的监听方法
     * @return
     */
    StateHandlerInterface setStateClickListener(BaseStateListener listener);
    BaseStateListener getStateClickListener();


    /**
     * BindView时调用
     * @param holder
     * @param t 由Adapter showState(int showstate, E e);传递过来的数据
     *          默认定义为String，如果你有不同需求，可改为任意类型
     */
    void BindEmptyHolder(SimpleViewHolder holder,T t);
    void BindErrorHolder(SimpleViewHolder holder,T t);
    void BindLoadingHolder(SimpleViewHolder holder,T t);
    void BindNomoreHolder(SimpleViewHolder holder,T t);

    /**
     * detachFromWindow时调用 销毁一些持有的引用
     */
    void destory();

    /**
     * 切换状态时调用，可以用来关闭一些之前状态的动画
     */
    void switchState();
```
- 代码中也可局部改变单一的状态布局id 和相应handler

### 实现方式
- 拦截Adapter的getItemCount() getItemViewType()来实现状态布局
- 多类型布局是托管给静态内部类ITEMHOLDER，通过添加不同ITEMHOLDER实例达到不同item类型


### 总结
 具体实现请下载源码查看，参考。ViewHolder封装类是参考鸿洋大神的，但进行了一定改版。

















-------------------------------------SrecyclerView----------------------------------------------------------
### 让自己的库易于扩展
> 浅谈本人的封装抽离思路
---
> [本文简书地址](http://www.jianshu.com/p/88795dff987c)

> 基于之前分享的 [《基于NestScroll机制实现下拉刷新 overScroll 等》](http://www.jianshu.com/p/4f6be42abad4) 

---
> 将之前库进行，头尾布局拆分、抽离易于扩展。

---
![image](http://upload-images.jianshu.io/upload_images/6456519-612198e8d0577168.gif?imageMogr2/auto-orient/strip)
#### 1.相比之前做了些什么？
- 新的头尾布局，继承RefreshWrapBase 根据自身需求，实现下拉，上拉过程中动画，文字等相关变化
- 默认实现了一个DefaultRefreshWrap，同时包含头尾布局


#### 2.思路
- 无论是上拉刷新，下拉加载，要完成完整过程动画需要知道以下几点
  1. 滑动的过程值（用于过程动画及文字变化）
  2. 正在刷新状态 （正在刷新动画）
  3. 完成状态     （完成状态下，让动画结束或文字改变）
  
定义一个BaseClass将需要的方法申明
```
    /**
     * 拉动过程
     * @param pull
     */
    public abstract void onPull(int pull);

    /**
     * 处于刷新状态下调用
     */
    public abstract void onRefresh();

    /**
     * 刷新完整调用
     */
    public abstract void onComplete();
```

   
- 父布局在滑动，或刷新完成过程中，只需要将值和状态传给header和footer。 header和footer 自己实现不同布局，不同组件的动画
- 我打算header或footer用同一个BaseClass.不同头布局 或者尾部局只要实现baseClass就行
- 由于要把头尾布局添加到父ViewGroup的头部或尾部，BaseClass需要知道以下几点
  1. 用户实现的是头布局还是尾部局（头布局就添加到头布局，尾布局。。。）
  2. 获取Context以及父布局的头尾容器（Context用于LayoutInflater，父布局头尾容器用于将头尾布局添加到父ViewGroup)
  
 获取父布局的相关属性，定义如下接口，让父View实现

```
public interface WrapInterface {
    LinearLayout getHeaderLayout();
    LinearLayout getFootLayout();
    Context getContext();
}
```
将wrapInterface 和是否是头尾布局通过构造函数传入

```
public RefreshWrapBase(WrapInterface parent,boolean header){
        this.header=header;
        this.parent=parent;
        //加载自定义布局
        viewLayout =LayoutInflater.from(parent.getContext()).inflate(getLayout(),parent.getHeaderLayout(),false);
        //抽象方法，让子类实现，获取组件做相应动画
        initViews();
        //将自定义布局添加到父头、尾布局
        addRefreshtoParent();
    }
    
    
    
     /**
     * 将布局添加到父ViewGroup
     * @param
     */
    protected void addRefreshtoParent(){
        LinearLayout wrapParent = header ? getHeaderWrapParent() : getfooterWrapParent();
        wrapParent.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight());
        params.gravity = Gravity.CENTER_VERTICAL;
        wrapParent.setOrientation(LinearLayout.HORIZONTAL);

        if (header)
            params.topMargin = -getHeight();

        wrapParent.setLayoutParams(params);
        wrapParent.addView(viewLayout);
    }


    /**
     * 获取父View头容器
     * @param
     * @return
     */
    protected LinearLayout getHeaderWrapParent(){
        return parent.getHeaderLayout();
    }

    /**
     * 获取父View尾容器
     * @param
     * @return
     */
    protected LinearLayout getfooterWrapParent(){
        return parent.getFootLayout();
    }
```
### 3.默认实现 继承Base

1. getLayout 中返回当前自定义布局id，getHeight（）设置布局高度
2. 在initView中获取参与滑动的组件，设置初始值
3. onPull（）方法中根据值做执行过程动画
4. onRefresh（）正在刷新时的方法
5. onComplete（）刷新完成的方法
6. OnDetachFromWindow（）进行销毁操作

```
public class DefaultRefreshWrap extends RefreshWrapBase {
    private static String[] pulldown = {"下拉刷新", "释放刷新", "正在刷新", "刷新完成"};
    private static String[] pullup = {"上拉加载", "释放加载", "正在加载", "加载完成"};

    ImageView progress;
    TextView headTitle;

    private RotateAnimation animation;

    /**
     * 构造调用intViews初始化组件
     *
     * @param parent
     * @param header
     */
    public DefaultRefreshWrap(WrapInterface parent, boolean header) {
        super(parent, header);
        initViews();
    }
    /**
     * 高度
     *
     * @return
     */
    @Override
    public int getHeight() {
        return dp2px(45);
    }

    @Override
    public int getLayout() {
        return R.layout.pull_to_refreshlayout;
    }

    @Override
    public void onPull(int pull) {
        if (Math.abs(pull) >= getHeight()) {
            String headerstr = this.header ? pulldown[1] : pullup[1];
            if (headerstr != headTitle.getText().toString())
                headTitle.setText(headerstr);
        } else {
            String footstr = header ? pulldown[0] : pullup[0];
            if (footstr != headTitle.getText().toString())
                headTitle.setText(footstr);
        }
        progress.setRotation(pull);
    }

    @Override
    public void onRefresh() {
        startRotate();
        headTitle.setText(header ? pulldown[2] : pullup[2]);

    }

    @Override
    public void onComplete() {
        if (animation != null)
            animation.cancel();
        headTitle.setText(header ? pulldown[3] : pullup[3]);
    }

    @Override
    public void initViews() {
        progress = (ImageView) viewLayout.findViewById(R.id.pull_to_refresh_image);
        headTitle = (TextView) viewLayout.findViewById(R.id.pull_to_refresh_text);
        String title = header ? pulldown[0] : pullup[0];
        headTitle.setText(title);
    }

    @Override
    public void OnDetachFromWindow() {
        progress = null;
        headTitle = null;
        if(animation!=null)
        animation.cancel();
        animation = null;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, viewLayout.getResources().getDisplayMetrics());
    }

    /**
     * 下拉progress的动画
     */
    private void startRotate() {
        if (animation == null) {
            animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(430);
            animation.setRepeatCount(-1);
        } else {
            animation.cancel();
        }

        progress.startAnimation(animation);
    }
}
```
### 4.header或footer的传入
1. 通过addHeader（）addFooter（）传入头尾布局
2. 或者addDefaultHeaderFooter（）来设置初始
   
```
   public SRecyclerView addHeader(RefreshWrapBase wrapBase) {
        this.headerRefreshWrap = wrapBase;
        return this;
    }

    public SRecyclerView addFooter(RefreshWrapBase wrapBase) {
        this.footerRefreshWrap = wrapBase;
        return this;
    }
    
    
   public SRecyclerView addDefaultHeaderFooter() {
       headerRefreshWrap = new DefaultRefreshWrap(this, true);
       footerRefreshWrap = new DefaultRefreshWrap(this, false);
       return this;
    }
    
```
### 5.使用（可参考上篇文章）
> lib同样实现了RecyclerView和ScrollView
[《基于NestScroll机制实现下拉刷新 overScroll 等》](http://www.jianshu.com/p/4f6be42abad4) 
```
 recyclerView.addDefaultHeaderFooter()
             .addHeader(new DefaultRefreshWrap(this,true)
             .addfooter(new DefaultRefreshWrap(this,false)
```

### 6.总结：
同之前一样，本文也只是提供一些拆分，扩展思路。
毕竟水平有限，一个全面的封装要考虑太多。

---
总之适合自己的才是最好的。

[Github地址](https://github.com/While1true/NestPullView)






