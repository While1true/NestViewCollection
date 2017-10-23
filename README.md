


### 新版
> 数据结构是list 使用和之前一样 不是list 设置position 只对position预估
--- 
名称更改
new SAdapter.addType(R.layout.layout,new PositionHandler
{})

--- 

> Stateyout采用和adapter一样的状态布局，适用于activity
--- 
setcontentView(new StateLayout(this).setcontent(R.Layout.layout))
--- 








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






