package demo.xdw.nwd.com.workdemo.demo.anima;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.base.frame.activity.BaseActivity;
import com.android.base.util.ScreenUtils;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * Created by xudengwang on 2017/3/27.
 */

public class AnimaAwingActivity extends BaseActivity {
    @Bind(R.id.btn_shrink)
    Button mBtnShink;
    @Bind(R.id.btn_expand)
    Button mBtnExpand;
    @Bind(R.id.iv3dview)
    Image3dView mIv3dView;
    @Bind(R.id.ll_source)
    LinearLayout mLlSource;

    @Bind(R.id.iv_source)
    ImageView mIvsource;
    /**
     * 右侧布局最多可以滑动到的左边缘。
     */

    /**
     * 右侧布局最多可以滑动到的右边缘。
     */
    private int topEdge = 0;


    /**
     * 右侧布局的参数，通过此参数来重新确定右侧布局的宽度。
     */
    //private ViewGroup.MarginLayoutParams sourceLayoutParams;

    /**
     * 3D视图的参数，通过此参数来重新确定3D视图的宽度。
     */
    private ViewGroup.LayoutParams mIv3dViewParams;

    /**
     * 左侧布局当前是显示还是隐藏。只有完全显示或隐藏时才会更改此值，滑动过程中此值无效。
     */
    private boolean isSourceLayoutVisible;

    private int height;


    @Override
    protected void initData() {
        mContext = this;
        //sourceLayoutParams = (ViewGroup.MarginLayoutParams)mLlSource.getLayoutParams();
        mIv3dViewParams = mIv3dView.getLayoutParams();
        //mLlSource.setLayoutParams(sourceLayoutParams);
        height = (int) (100 * ScreenUtils.getScreenDensity(mContext));
        // mIv3dViewParams = mIv3dView.getLayoutParams();
        //mIv3dViewParams.height = height;

        // 滑动的同时改变3D视图的大小
        //mIv3dView.setLayoutParams(mIv3dViewParams);
        // 保证在滑动过程中3D视图可见，左侧布局不可见
        // showImage3dView();
        ViewGroup.LayoutParams sourceParams = mLlSource.getLayoutParams();
        sourceParams.height = (int) (100 * ScreenUtils.getScreenDensity(mContext));
        sourceParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        mLlSource.setLayoutParams(sourceParams);
        mIv3dView.setSourceView(mLlSource);


        topEdge = 0;


        mBtnShink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateOnXCoordinateShink();
//                mIv3dView.clearSourceBitmap();
//                new ScrollTask().execute(-5);
            }
        });

        mBtnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateOnXCoordinateExpand();
//                mIv3dView.clearSourceBitmap();
//                new ScrollTask().execute(+2);
            }
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_anima_awing;
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int topMargin = height;
            LogUtils.d("doInBackground topMargin:" + topMargin + "speed:" + speed[0]);
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                topMargin = topMargin + speed[0];
                if (topMargin < 0) {
                    topMargin = 0;
                    break;
                }
                if (topMargin > height) {
                    topMargin = height;
                    break;
                }
                publishProgress(topMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠5毫秒，这样肉眼才能够看到滚动动画。
                sleep(10);
            }
            if (speed[0] > 0) {
                isSourceLayoutVisible = true;
            } else {
                isSourceLayoutVisible = false;
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            LogUtils.d("onProgressUpdate topMargin:" + topMargin[0]);
            mIv3dViewParams = mIv3dView.getLayoutParams();
            mIv3dViewParams.height = topMargin[0];
            mIv3dView.setLayoutParams(mIv3dViewParams);
            showImage3dView();
        }

        @Override
        protected void onPostExecute(Integer topMargin) {
//            Log.d("123", "onPostExecute rightMargin:" + rightMargin);
//            rightLayoutParams.rightMargin = rightMargin;
//            rightLayout.setLayoutParams(rightLayoutParams);
            mIv3dViewParams = mIv3dView.getLayoutParams();
            mIv3dViewParams.height = topMargin;
            mIv3dView.setLayoutParams(mIv3dViewParams);
            if (isSourceLayoutVisible) {
                // 保证在滑动结束后左侧布局可见，3D视图不可见。
                mIv3dView.setVisibility(View.INVISIBLE);
                mLlSource.setVisibility(View.VISIBLE);
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

    /**
     * 保证此时让左侧布局不可见，3D视图可见，从而让滑动过程中产生3D的效果。
     */
    private void showImage3dView() {
        if (mIv3dView.getVisibility() != View.VISIBLE) {
            mIv3dView.setVisibility(View.VISIBLE);
        }
        if (mLlSource.getVisibility() != View.INVISIBLE) {
            mLlSource.setVisibility(View.INVISIBLE);
        }
    }

    class PolyToPolyView extends View {

        private Bitmap mBitmap;
        private Matrix mMatrix;

        public PolyToPolyView(Context context) {
            super(context);
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.test);
            mMatrix = new Matrix();
            float[] src = {0, 0,//
                    mBitmap.getWidth(), 0,//
                    mBitmap.getWidth(), mBitmap.getHeight(),//
                    0, mBitmap.getHeight()};
            float[] dst = {0, 0,//
                    mBitmap.getWidth(), 0,//
                    mBitmap.getWidth() + 200, mBitmap.getHeight() - 200,//
                    0 + 200, mBitmap.getHeight() - 200};
            mMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }

    }


//
//    // 以X轴为轴心旋转
//    private void rotateOnXCoordinate() {
//        float centerX = mIvsource.getWidth() / 2.0f;
//        float centerY = 0f;
//        float centerZ = 0f;
//
//        Rotate3dAnimation rotate3dAnimationX = new Rotate3dAnimation(0, 180, centerX, centerY, centerZ, Rotate3dAnimation.ROTATE_X_AXIS, true);
//        rotate3dAnimationX.setDuration(1000);
//        mIvsource.startAnimation(rotate3dAnimationX);
//    }

    // 以X轴为轴心旋转
    private void rotateOnXCoordinateShink() {
        float centerX = mIvsource.getWidth() / 2.0f;
        float centerY = 0F;
        float depthZ = 0f;
        Rotate3dAnimation rotate3dAnimationX = new Rotate3dAnimation(0, -90, centerX, centerY, depthZ, Rotate3dAnimation.ROTATE_X_AXIS, true);
        rotate3dAnimationX.setDuration(1000);
        rotate3dAnimationX.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvsource.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rotate3dAnimationX.setFillAfter(true);
        mIvsource.startAnimation(rotate3dAnimationX);
    }

    // 以X轴为轴心旋转
    private void rotateOnXCoordinateExpand() {
        final float centerX = mIvsource.getWidth() / 2.0f;
        final float centerY = 0F;
        final float depthZ = 0f;
        Rotate3dAnimation rotate3dAnimationX = new Rotate3dAnimation(-90, 0, centerX, centerY, depthZ, Rotate3dAnimation.ROTATE_X_AXIS, true);
        rotate3dAnimationX.setDuration(1000);
        rotate3dAnimationX.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvsource.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mIvsource.startAnimation(rotate3dAnimationX);
    }


}
