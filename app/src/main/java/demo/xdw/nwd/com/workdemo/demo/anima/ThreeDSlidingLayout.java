package demo.xdw.nwd.com.workdemo.demo.anima;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * 三维滑动菜单框架。
 * <p>
 * 访问 http://blog.csdn.net/guolin_blog/article/details/10471245 阅读详细讲解
 *
 * @author guolin
 */
public class ThreeDSlidingLayout extends RelativeLayout implements OnTouchListener {

    /**
     * 滚动显示和隐藏左侧布局时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;

    /**
     * 滑动状态的一种，表示未进行任何滑动。
     */
    public static final int DO_NOTHING = 0;

    /**
     * 滑动状态的一种，表示正在滑出左侧菜单。
     */
    public static final int SHOW_MENU = 1;

    /**
     * 滑动状态的一种，表示正在隐藏左侧菜单。
     */
    public static final int HIDE_MENU = 2;

    /**
     * 记录当前的滑动状态
     */
    private int slideState;

    /**
     * 屏幕宽度值。
     */
    private int screenWidth;
    /**
     * 屏幕高度值。
     */
    private int screenHeight;


    /**
     * 右侧布局最多可以滑动到的左边缘。
     */
    private int topEdge = 0;

    /**
     * 右侧布局最多可以滑动到的右边缘。
     */
    private int bottomEdge = 0;

    /**
     * 在被判定为滚动之前用户手指可以移动的最大值。
     */
    private int touchSlop;

    /**
     * 记录手指按下时的横坐标。
     */
    private float xDown;

    /**
     * 记录手指按下时的纵坐标。
     */
    private float yDown;

    /**
     * 记录手指移动时的横坐标。
     */
    private float xMove;

    /**
     * 记录手指移动时的纵坐标。
     */
    private float yMove;

    /**
     * 记录手机抬起时的横坐标。
     */
    private float xUp;

    /**
     * 记录手机抬起时的横坐标。
     */
    private float yUp;

    /**
     * 左侧布局当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
     */
    private boolean isTopLayoutVisible;

    /**
     * 是否正在滑动。
     */
    private boolean isSliding;

    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;

    /**
     * 左侧布局对象。
     */
    private View topLayout;

    /**
     * 右侧布局对象。
     */
    private View bottomLayout;

    /**
     * 在滑动过程中展示的3D视图
     */
    private Image3dView image3dView;

    /**
     * 用于监听侧滑事件的View。
     */
    private View mBindView;

    /**
     * 左侧布局的参数，通过此参数来重新确定左侧布局的宽度，以及更改topMargin的值。
     */
    private MarginLayoutParams topLayoutParams;

    /**
     * 右侧布局的参数，通过此参数来重新确定右侧布局的宽度。
     */
    private MarginLayoutParams bottomLayoutParams;

    /**
     * 3D视图的参数，通过此参数来重新确定3D视图的宽度。
     */
    private ViewGroup.LayoutParams image3dViewParams;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 重写SlidingLayout的构造函数，其中获取了屏幕的宽度。
     *
     * @param context
     * @param attrs
     */
    public ThreeDSlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 绑定监听侧滑事件的View，即在绑定的View进行滑动才可以显示和隐藏左侧布局。
     *
     * @param bindView 需要绑定的View对象。
     */
    public void setScrollEvent(View bindView) {
        mBindView = bindView;
        mBindView.setOnTouchListener(this);
    }

    /**
     * 将屏幕滚动到左侧布局界面，滚动速度设定为10.
     */
    public void scrollToTopLayout() {
        image3dView.clearSourceBitmap();
        new ScrollTask().execute(-10);
    }

    /**
     * 将屏幕滚动到右侧布局界面，滚动速度设定为-10.
     */
    public void scrollToBottomLayout() {
        image3dView.clearSourceBitmap();
        new ScrollTask().execute(10);
    }

    /**
     * 左侧布局是否完全显示出来，或完全隐藏，滑动过程中此值无效。
     *
     * @return 左侧布局完全显示返回true，完全隐藏返回false。
     */
    public boolean isTopLayoutVisible() {
        return isTopLayoutVisible;
    }

    /**
     * 在onLayout中重新设定左侧布局和右侧布局的参数。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            // 获取左侧布局对象
            topLayout = findViewById(R.id.menu);
            topLayoutParams = (MarginLayoutParams) topLayout.getLayoutParams();
            LogUtils.d(getClass(),"topLayoutParams.height:"+topLayoutParams.height);
            bottomEdge = -topLayoutParams.height;
            // 获取右侧布局对象
            bottomLayout = findViewById(R.id.content);
            bottomLayoutParams = (MarginLayoutParams) bottomLayout.getLayoutParams();
            bottomLayoutParams.height = screenHeight;
            Log.d("123", "bottomLayoutParams.bottommargin:" + bottomLayoutParams.bottomMargin);
            Log.d("123", "bottomLayoutParams.bottom:" + bottomLayoutParams.bottomMargin);
            bottomLayout.setLayoutParams(bottomLayoutParams);
            // 获取3D视图对象
            image3dView = (Image3dView) findViewById(R.id.image_3d_view);
            // 将左侧布局传入3D视图中作为生成源
            image3dView.setSourceView(topLayout);
            loadOnce = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，记录按下时的横坐标
                xDown = event.getRawX();
                yDown = event.getRawY();
                slideState = DO_NOTHING;
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整右侧布局的topMargin值，从而显示和隐藏左侧布局
                xMove = event.getRawX();
                yMove = event.getRawY();
                int moveDistanceX = (int) (xMove - xDown);
                int moveDistanceY = (int) (yMove - yDown);
                checkSlideState(moveDistanceX, moveDistanceY);
                switch (slideState) {
                    case SHOW_MENU:
                        bottomLayoutParams.bottomMargin = -moveDistanceY;
                        onSlide();
                        break;
                    case HIDE_MENU:
                        bottomLayoutParams.bottomMargin = bottomEdge - moveDistanceY;
                        onSlide();
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                yUp = event.getRawY();
                int upDistanceX = (int) (yUp - yDown);
                if (isSliding) {
                    // 手指抬起时，进行判断当前手势的意图
                    switch (slideState) {
                        case SHOW_MENU:
                            if (shouldScrollTotopLayout()) {
                                scrollToTopLayout();
                            } else {
                                scrollToBottomLayout();
                            }
                            break;
                        case HIDE_MENU:
                            if (shouldScrollTobottomLayout()) {
                                scrollToBottomLayout();
                            } else {
                                scrollToTopLayout();
                            }
                            break;
                        default:
                            break;
                    }
                } else if (upDistanceX < touchSlop && isTopLayoutVisible) {
                    scrollToBottomLayout();
                }
                recycleVelocityTracker();
                break;
        }
        if (v.isEnabled()) {
            if (isSliding) {
                unFocusBindView();
                return true;
            }
            if (isTopLayoutVisible) {
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * 执行滑动过程中的逻辑操作，如边界检查，改变偏移值，可见性检查等。
     */
    private void onSlide() {
        checkSlideBorder();
        bottomLayoutParams.topMargin = -bottomLayoutParams.bottomMargin;
        bottomLayout.setLayoutParams(bottomLayoutParams);
        image3dView.clearSourceBitmap();
        image3dViewParams = image3dView.getLayoutParams();
        image3dViewParams.height = -bottomLayoutParams.bottomMargin;
        // 滑动的同时改变3D视图的大小
        image3dView.setLayoutParams(image3dViewParams);
        // 保证在滑动过程中3D视图可见，左侧布局不可见
        showImage3dView();
    }

    /**
     * 根据手指移动的距离，判断当前用户的滑动意图，然后给slideState赋值成相应的滑动状态值。
     *
     * @param moveDistanceX 横向移动的距离
     * @param moveDistanceY 纵向移动的距离
     */
    private void checkSlideState(int moveDistanceX, int moveDistanceY) {
        if (isTopLayoutVisible) {
            if (!isSliding && Math.abs(moveDistanceY) >= touchSlop && moveDistanceY < 0) {
                isSliding = true;
                slideState = HIDE_MENU;
            }
        } else if (!isSliding && Math.abs(moveDistanceY) >= touchSlop && moveDistanceY > 0
                && Math.abs(moveDistanceX) < touchSlop) {
            isSliding = true;
            slideState = SHOW_MENU;
        }
    }

    /**
     * 在滑动过程中检查左侧菜单的边界值，防止绑定布局滑出屏幕。
     */
    private void checkSlideBorder() {
        if (bottomLayoutParams.bottomMargin > topEdge) {
            bottomLayoutParams.bottomMargin = topEdge;
        } else if (bottomLayoutParams.bottomMargin < bottomEdge) {
            bottomLayoutParams.bottomMargin = bottomEdge;
        }
    }

    /**
     * 判断是否应该滚动将左侧布局展示出来。如果手指移动距离大于屏幕的1/2，或者手指移动速度大于SNAP_VELOCITY，
     * 就认为应该滚动将左侧布局展示出来。
     *
     * @return 如果应该滚动将左侧布局展示出来返回true，否则返回false。
     */
    private boolean shouldScrollTotopLayout() {
        return yUp - yDown > topLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 判断是否应该滚动将右侧布局展示出来。如果手指移动距离加上topLayoutPadding大于屏幕的1/2，
     * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将右侧布局展示出来。
     *
     * @return 如果应该滚动将右侧布局展示出来返回true，否则返回false。
     */
    private boolean shouldScrollTobottomLayout() {
        return yDown - yUp > topLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * 创建VelocityTracker对象，并将触摸事件加入到VelocityTracker当中。
     *
     * @param event 右侧布局监听控件的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在右侧布局的监听View上的滑动速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 使用可以获得焦点的控件在滑动的时候失去焦点。
     */
    private void unFocusBindView() {
        if (mBindView != null) {
            mBindView.setPressed(false);
            mBindView.setFocusable(false);
            mBindView.setFocusableInTouchMode(false);
        }
    }

    /**
     * 保证此时让左侧布局不可见，3D视图可见，从而让滑动过程中产生3D的效果。
     */
    private void showImage3dView() {
        if (image3dView.getVisibility() != View.VISIBLE) {
            image3dView.setVisibility(View.VISIBLE);
        }
        if (topLayout.getVisibility() != View.INVISIBLE) {
            topLayout.setVisibility(View.INVISIBLE);
        }
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int bottomMargin = bottomLayoutParams.bottomMargin;
            Log.d("123", "bottomEdge:" + bottomEdge + ",topEdge:" + topEdge);
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                bottomMargin = bottomMargin + speed[0];
                if (bottomMargin < bottomEdge) {
                    bottomMargin = bottomEdge;
                    break;
                }
                if (bottomMargin > topEdge) {
                    bottomMargin = topEdge;
                    break;
                }
                publishProgress(bottomMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠5毫秒，这样肉眼才能够看到滚动动画。
                sleep(5);
            }
            if (speed[0] > 0) {
                isTopLayoutVisible = false;
            } else {
                isTopLayoutVisible = true;
            }
            isSliding = false;
            return bottomMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... bottomMargin) {
            Log.d("123", "onProgressUpdate bottomMargin:" + bottomMargin[0]);
            bottomLayoutParams.bottomMargin = bottomMargin[0];
            bottomLayoutParams.topMargin = -bottomMargin[0];
            bottomLayout.setLayoutParams(bottomLayoutParams);
            image3dViewParams = image3dView.getLayoutParams();
            image3dViewParams.height = -bottomLayoutParams.bottomMargin;
            image3dView.setLayoutParams(image3dViewParams);
            showImage3dView();
            unFocusBindView();
        }

        @Override
        protected void onPostExecute(Integer bottomMargin) {
            Log.d("123", "onPostExecute bottomMargin:" + bottomMargin);
            bottomLayoutParams.bottomMargin = bottomMargin;
            bottomLayoutParams.topMargin = -bottomMargin;
            bottomLayout.setLayoutParams(bottomLayoutParams);
            image3dViewParams = image3dView.getLayoutParams();
            image3dViewParams.height = -bottomLayoutParams.bottomMargin;
            image3dView.setLayoutParams(image3dViewParams);
            if (isTopLayoutVisible) {
                // 保证在滑动结束后左侧布局可见，3D视图不可见。
                image3dView.setVisibility(View.INVISIBLE);
                topLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param millis 指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
