package demo.xdw.nwd.com.workdemo.demo.rules;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * @author jayce
 * @date 2015/3/12
 */
public class LoanThumbView extends ImageView {

    private LoanRangeSeekBar rangeSeekBar;
    //标签
    private String TAG;

    private int mDownX = 0;
    //thumb宽度
    private int mWidth;

    private int mLeftLimit = 0;
    private int mRightLimit = Integer.MAX_VALUE;

    private Rect rect;

    private int mCenterX;   //游标的中心位置

    private boolean mIsMoving;      //游标是否正在移动

    private OnThumbListener listener;

    public LoanThumbView(Context context) {
        this(context, null);
    }

    public LoanThumbView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoanThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRangeSeekBar(LoanRangeSeekBar rangeSeekBar) {
        this.rangeSeekBar = rangeSeekBar;
    }

    public void setLimit(int mLeftLimit, int mRightLimit) {
        this.mLeftLimit = mLeftLimit;
        this.mRightLimit = mRightLimit;
    }

    public int getCenterX() {
        return mCenterX;
    }

    public void setTag(String tag){
        this.TAG = tag;
    }


    /**
     * 设置中心位置，不超过左右的limit，就刷新整个控件，并且回调onThumbChange()
     *
     * @param centerX
     */
    public void setCenterX(int centerX) {
        int left = centerX - mWidth / 2, right = centerX + mWidth / 2;
        if (centerX < mLeftLimit) {
            left = mLeftLimit - mWidth / 2;
            right = mLeftLimit + mWidth / 2;
        }

        if (centerX > mRightLimit) {
            left = mRightLimit - mWidth / 2;
            right = mRightLimit + mWidth / 2;
        }

        this.mCenterX = (left + right) / 2;

        if (left != rect.left || right != rect.right) {
            rect.union(left, rect.top, right, rect.bottom);
            layout(left, rect.top, right, rect.bottom);
            //invalidate(rect);
            rangeSeekBar.invalidate();

            if (listener != null) {
                listener.onThumbChange(100 * ((left + right) / 2 - mLeftLimit) / (mRightLimit - mLeftLimit));
            }
        }
    }

    public boolean isMoving() {
        return mIsMoving;
    }

    public void setOnThumbListener(OnThumbListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LogUtils.d(getClass(),"tag:"+TAG+",-----left:"+left+",top:"+top+",right:"+right+",bottom:"+bottom);
        rect = new Rect(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.d(getClass(),"tag:"+TAG);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mIsMoving = false;

                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getX();

                int left = rect.left + nowX - mDownX;
                int right = rect.right + nowX - mDownX;
                mIsMoving = true;
                setCenterX((left + right) / 2);
                break;
            case MotionEvent.ACTION_UP:
                mIsMoving = false;
                rangeSeekBar.invalidate();
                break;
        }
        return true;
    }

    public interface OnThumbListener {
        public void onThumbChange(int i);
    }
}
