`


# NestPullView

基于之前分享的 《基于NestScroll机制实现下拉刷新 overScroll 等》
[《基于NestScroll机制实现下拉刷新 overScroll 等》](http://www.jianshu.com/p/4f6be42abad4)

进行头部抽离拆分封装，易于扩展性
继承RefreshWrapBase实现不同头尾
默认实现了一个DefaultRefreshWrap" --
[master (root-commit) 16e13c9] Initial commit 基于之前分享的 《基于NestScroll机制实现下拉刷新 overScroll 等》 进行头部抽离拆分封装，易于扩展性 继承RefreshWrapBase实现不同头尾 默认实现了一个DefaultRefreshWrap

```
public abstract class RefreshWrapBase {
    protected View viewLayout;

    private WrapInterface parent;

    protected int height;

    public RefreshWrapBase(WrapInterface parent){
        this.parent=parent;
        viewLayout = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent.getParentView(), false);
    }
    public abstract int getLayout();


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

    /**
     * 进行一些组件的初始化
     */
    public abstract void initViews();

    /**
     * 高度
     * @return
     */
    public abstract int getHeight();

    /**
     * 做一些销毁操作
     */
    public void OnDetachFromWindow(){
        viewLayout=null;
        parent=null;
        parent=null;
    }



    /**
     * 头跟布局
     * @param
     * @return
     */
    protected LinearLayout getHeaderWrapParent(){
        return parent.getHeaderLayout();
    }

    /**
     * 尾跟布局
     * @param
     * @return
     */
    protected LinearLayout getfooterWrapParent(){
        return parent.getFootLayout();
    }
}

```

```public class DefaultRefreshWrap extends RefreshWrapBase {
    private static String[] pulldown = {"下拉刷新", "释放刷新", "正在刷新", "刷新完成"};
    private static String[] pullup = {"上拉加载", "释放加载", "正在加载", "加载完成"};

    ImageView progress;
    TextView headTitle;


    private boolean header;
    private RotateAnimation animation;

    /**
     * 构造调用intViews初始化组件
     * @param parent
     * @param header
     */
    public DefaultRefreshWrap(WrapInterface parent, boolean header) {
        super(parent);
        this.header = header;
        initViews();
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
        LinearLayout wrapParent = header ? getHeaderWrapParent() : getfooterWrapParent();
        progress = (ImageView) viewLayout.findViewById(R.id.pull_to_refresh_image);
        headTitle = (TextView) viewLayout.findViewById(R.id.pull_to_refresh_text);
        String title = header ? pulldown[0] : pullup[0];
        if (title != headTitle.getText().toString())
            headTitle.setText(title);

        wrapParent.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight());
        params.gravity = Gravity.CENTER_VERTICAL;
        wrapParent.setOrientation(LinearLayout.HORIZONTAL);

        if (header)
            params.topMargin = -getHeight();

        wrapParent.setLayoutParams(params);
        wrapParent.addView(viewLayout);
    }

    @Override
    public int getHeight() {
        if (height == 0)
            height = dp2px(55);
        return height;
    }

    @Override
    public void OnDetachFromWindow() {
        progress = null;
        headTitle = null;
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
