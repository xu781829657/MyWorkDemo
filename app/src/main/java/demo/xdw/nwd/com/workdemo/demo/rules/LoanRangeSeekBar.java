package demo.xdw.nwd.com.workdemo.demo.rules;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.DensityUtil;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * @author jayce
 * @date 2015/3/9
 */
public class LoanRangeSeekBar extends ViewGroup {
    private Drawable mThumbDrawable;
    private Drawable mThumbPlaceDrawable;

    private LoanThumbView mThumbLeft;   //左游标
    private LoanThumbView mThumbRight;  //右游标
    private int mProgressBarHeight;     //进度条的高度
    private int mThumbPlaceHeight;      //游标的高度

    private int mMaxValue = 130;   //分成100份，每一小格占2份

    private int mLeftValue;     //左游标  数值    (100分之多少)   例如：1就是 1/100
    private int mRightValue = mMaxValue;  //右游标  数值    (100分之多少)

    private int mLeftLimit;     //游标左边的限制坐标
    private int mRightLimit;        //游标右边的限制坐标
    private int proPaddingLeftAndRight;     //进度条左右的padding 等于游标图标宽度的一半
    private int mProBaseline;       //进度条top  坐标

    private static final int PART_ITEM = 5;//半小 占的分数
    private float mPartWidth;   //每一小份的宽度

    public static final int SHORTLINE_HEIGHT = 5; //短线的高度 （画刻度时会有长短线）
    public static final int LONGLINE_HEIGHT = 10; //长线的高度

    public static final int RULE_HEIGHT_DP = 20;  //尺子的高度  dp
    public static int RULE_HEIGHT_PX;

    private int degs[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};      //尺子上标记刻度值
    private String unitStr = "";     //尺子标记单位

    private OnRangeChangeListener mOnRangeChangeListener;       //当左右任意一个游标改变时，回调接口

    public interface OnRangeChangeListener {
        public void onRangeChange(int leftValue, int rightValue);
    }

    public LoanRangeSeekBar(Context context) {
        this(context, null);
    }

    public LoanRangeSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoanRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundDrawable(new BitmapDrawable());
        //换算px
        RULE_HEIGHT_PX = DensityUtil.dip2px(context, RULE_HEIGHT_DP);
        mProgressBarHeight = DensityUtil.dip2px(context, 4);

        mThumbDrawable = getResources().getDrawable(R.drawable.rod_handshank_butten);
        mThumbPlaceDrawable = getResources().getDrawable(R.drawable.rod_place_icon);

        mThumbPlaceHeight = mThumbPlaceDrawable.getIntrinsicHeight();
        mProBaseline = RULE_HEIGHT_PX + mThumbPlaceHeight;

        mThumbLeft = new LoanThumbView(getContext());
        mThumbLeft.setPadding(20,0,20,20);
        mThumbLeft.setTag("loan left");
        mThumbLeft.setRangeSeekBar(this);
        mThumbLeft.setImageDrawable(mThumbDrawable);
        mThumbRight = new LoanThumbView(getContext());
        mThumbRight.setPadding(20,0,20,20);

        mThumbRight.setTag("loan right");
        mThumbRight.setRangeSeekBar(this);
        mThumbRight.setImageDrawable(mThumbDrawable);

//        measureView(mThumbLeft);
//        measureView(mThumbRight);

        addView(mThumbLeft);
        addView(mThumbRight);
        mThumbLeft.setOnThumbListener(new LoanThumbView.OnThumbListener() {
            @Override
            public void onThumbChange(int i) {

                int progress = 100 * (mThumbLeft.getCenterX() - mLeftLimit - (int) (5 * mPartWidth)) / (mRightLimit - mLeftLimit - (int) (10 * mPartWidth));
                mLeftValue = progress;
                LogUtils.d(getClass(), "mLeftValue:" + mLeftValue);
                //当左侧滑条移动时，限制右侧滑条的左侧范围
                mThumbRight.setLimit(mThumbLeft.getCenterX(), mRightLimit - (int) (5 * mPartWidth));
                if (mOnRangeChangeListener != null) {
                    mOnRangeChangeListener.onRangeChange(mLeftValue, mRightValue);
                }

            }
        });
        mThumbRight.setOnThumbListener(new LoanThumbView.OnThumbListener() {
            @Override
            public void onThumbChange(int i) {

                int progress = 100 * (mThumbRight.getCenterX() - mLeftLimit - (int) (5 * mPartWidth)) / (mRightLimit - mLeftLimit - (int) (10 * mPartWidth));
                mRightValue = progress;
                LogUtils.d(getClass(), "mRightValue:" + mRightValue);
                //当右侧滑条移动时，限制左侧滑条的右侧范围
                int thumbLeftRightLimit = 0;
                if (mThumbRight.getCenterX() == mRightLimit - (int) (5 * mPartWidth)) {
                    thumbLeftRightLimit = mRightLimit - (int) (6 * mPartWidth);
                } else {
                    thumbLeftRightLimit = mThumbRight.getCenterX();
                }

                mThumbLeft.setLimit(mLeftLimit + (int) (5 * mPartWidth), thumbLeftRightLimit);
                if (mOnRangeChangeListener != null) {
                    mOnRangeChangeListener.onRangeChange(mLeftValue, mRightValue);
                }

            }
        });
    }

    public void setOnRangeChangeListener(OnRangeChangeListener mOnRangeChangeListener) {
        this.mOnRangeChangeListener = mOnRangeChangeListener;
    }

    /**
     * 画尺子
     *
     * @param canvas
     */
    protected void drawProgressBar(Canvas canvas) {
        //画背景
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.grey));
        Rect rect = new Rect(mLeftLimit, mProBaseline, mRightLimit, mProBaseline + mProgressBarHeight);
        canvas.drawRect(rect, paint);

        //画进度
        paint.setColor(getResources().getColor(R.color.blue));
        rect = new Rect(mThumbLeft.getCenterX(), mProBaseline, mThumbRight.getCenterX(), mProBaseline + mProgressBarHeight);
        //LogUtils.d(getClass(),"mThumbLeft.getY():"+mThumbLeft.getY()+",mThumbLeft.getHeight():"+mThumbLeft.getHeight());
        //rect = new Rect(mThumbLeft.getCenterX(), mProBaseline, mThumbRight.getCenterX(), mProBaseline+mProgressBarHeight+mThumbLeft.getHeight());
        canvas.drawRect(rect, paint);
    }

    /**
     * 画刻度尺
     *
     * @param canvas
     */
    protected void drawRule(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.grey));
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);

        //一次遍历两份,绘制的位置都是在奇数位置
        for (int i = 5; i <= mMaxValue; i += 2) {
            if (i < PART_ITEM || i > mMaxValue - PART_ITEM) {
                continue;
            }

            //每个刻度尺的起始x坐标
            float degX = mLeftLimit + i * mPartWidth;
            //每个刻度尺的到达x坐标
            int degY;


            if ((i - PART_ITEM) % (PART_ITEM * 2) == 0) {
                //当刻度在范围内改变字体颜色
                if (degX >= mThumbLeft.getCenterX() && degX <= mThumbRight.getCenterX()) {
                    paint.setColor(getResources().getColor(R.color.blue));
                } else {
                    paint.setColor(getResources().getColor(R.color.grey));
                }
                degY = mProBaseline - DensityUtil.dip2px(getContext(), LONGLINE_HEIGHT);
//                canvas.drawText(degs[(i - 5) / 10] + unitStr, degX, degY, paint);
                canvas.drawText((i - 5) * 3 / 10 + unitStr, degX, degY, paint);
            } else {
                degY = mProBaseline - DensityUtil.dip2px(getContext(), SHORTLINE_HEIGHT);
            }
            canvas.drawLine(degX, mProBaseline, degX, degY, paint);
        }
    }

    /**
     * 画 Thumb 位置的数值
     */
    protected void drawRodPlaceValue(Canvas canvas, LoanThumbView thumbView) {
        int centerX = thumbView.getCenterX();
        Paint paint = new Paint();
        BitmapDrawable bd = (BitmapDrawable) mThumbPlaceDrawable;
        canvas.drawBitmap(bd.getBitmap(), centerX - mThumbPlaceDrawable.getIntrinsicWidth() / 2, 0, paint);

        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30);
        canvas.drawText(geneareThumbValue(thumbView) + "", centerX, mThumbDrawable.getIntrinsicHeight() / 2, paint);
    }

    //onLayout调用后执行的函数
    private void onLayoutPrepared() {
        mThumbLeft.setCenterX(mLeftLimit);
        mThumbRight.setCenterX(mRightLimit);
    }

    private int geneareThumbValue(LoanThumbView view) {
        //todo 这里只是计算了100之多少的值，需要自行转换成刻度上的值
        int proValue = (int) (0.3 * (mMaxValue - 10) * (view.getCenterX() - mLeftLimit - (int) (5 * mPartWidth)) / (mRightLimit - mLeftLimit - (int) (10 * mPartWidth)));
        //int proValue = 100 * (view.getCenterX() - mLeftLimit) / (mRightLimit - mLeftLimit);
        return proValue;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);    //测量子控件
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        proPaddingLeftAndRight = mThumbLeft.getMeasuredWidth() / 2;
        mLeftLimit = proPaddingLeftAndRight;
        mRightLimit = mWidth - proPaddingLeftAndRight;

        //位置标记的高度+尺子的刻度高度+尺子的高度+游标的高度
//        setMeasuredDimension(mWidth, mThumbPlaceHeight + RULE_HEIGHT_PX + mProgressBarHeight + mThumbLeft.getMeasuredHeight()+60);
        setMeasuredDimension(mWidth, mThumbPlaceHeight + RULE_HEIGHT_PX + mProgressBarHeight + mThumbLeft.getMeasuredHeight());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawProgressBar(canvas);
        drawRule(canvas);

        if (mThumbLeft.isMoving()) {
            drawRodPlaceValue(canvas, mThumbLeft);
        } else if (mThumbRight.isMoving()) {
            drawRodPlaceValue(canvas, mThumbRight);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.d(getClass(), "onLayout left:" + l + ",top:" + t + ",right:" + r + ",b:" + b);
        int heightSum = 0;

        heightSum += mThumbPlaceHeight;

        heightSum += RULE_HEIGHT_PX;

        heightSum += mProgressBarHeight;

        mPartWidth = (mRightLimit - mLeftLimit) / (float) mMaxValue;   //计算一份所占的宽度  一定要用float

        mThumbLeft.setLimit(mLeftLimit + (int) (5 * mPartWidth), mRightLimit - (int) (6 * mPartWidth));    //设置可以移动的范围
        LogUtils.d(getClass(), "heightSum:" + heightSum + ",b:" + b + ",mThumbLeft.getMeasuredWidth():" + mThumbLeft.getMeasuredWidth() + ",mThumbLeft.getMeasuredHeight():" + mThumbLeft.getMeasuredHeight());

        mThumbLeft.layout(0, b - mThumbLeft.getMeasuredHeight(), mThumbLeft.getMeasuredWidth(), b -10);      //设置在父布局的位置

        mThumbRight.setLimit(mLeftLimit, mRightLimit);
        mThumbRight.layout(0, b - mThumbLeft.getMeasuredHeight(), mThumbLeft.getMeasuredWidth(), b -10);


        onLayoutPrepared();     //layout调用后调用的方法，比如设置thumb limit
    }


}
