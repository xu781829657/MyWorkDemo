package demo.xdw.nwd.com.workdemo.demo.anima;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.base.frame.activity.BaseActivity;
import com.android.base.util.ScreenUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.adapter.TestListAdapter;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * Created by xudengwang on 2017/3/31.
 */

public class FadeAnimaFixHeightActivity extends BaseActivity {

    @Bind(R.id.lv_test)
    ListView mLvTest;
    @Bind(R.id.btn_fade_in)
    Button mBtnFadeIn;
    @Bind(R.id.btn_fade_out)
    Button mBtnFadeOut;

    @Bind(R.id.tv_anima_source)
    TextView mTvSource;

    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition;// 标记上次滑动位置

    private boolean mIsAnima;

    private int mLastFirstPostion;
    private int mLastFirstTop;
    private int touchSlop = 1;
    ViewGroup.MarginLayoutParams mLvMarginLayoutParams;
    private volatile ViewGroup.MarginLayoutParams mSourceLayoutParams;
    private int SOURCE_HEIGHT;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fade_anima;
    }

    @Override
    protected void initData() {
        mContext = this;
        mLvMarginLayoutParams = (ViewGroup.MarginLayoutParams) mLvTest.getLayoutParams();
        mSourceLayoutParams = (ViewGroup.MarginLayoutParams) mTvSource.getLayoutParams();

        List<String> titles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String title = "标题" + i;
            titles.add(title);
        }
        SOURCE_HEIGHT = (int) ScreenUtils.getScreenDensity(mContext) * 100;

        mLvTest.setLayoutParams(mLvMarginLayoutParams);
        mLvMarginLayoutParams.topMargin = SOURCE_HEIGHT;
        mLvMarginLayoutParams.bottomMargin = -SOURCE_HEIGHT;
        mTvSource.setLayoutParams(mSourceLayoutParams);
        TestListAdapter adapter = new TestListAdapter(mContext, titles);
        mLvTest.setAdapter(adapter);
        mLvTest.setOnScrollListener(new OnScrollYImlListener(mLvTest) {
            @Override
            protected void onScrollYSlideUp() {
                collapseRankLayout();
                //               LogUtils.d("11111111111onScrollYSlideUp " + scrolledY);
//                if (scrolledY > 120 * ScreenUtils.getScreenDensity(Base.getContext())) {
//
//                }
            }

            @Override
            protected void onScrollYPullDown() {
                expandRankLayout();
//                LogUtils.d("22222222222222onScrollYPullDown " + scrolledY);
//                if (scrolledY > 120 * ScreenUtils.getScreenDensity(Base.getContext())) {
//
//                }
            }
        });

        mBtnFadeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (mTvSource.getVisibility() != View.VISIBLE) {
//                    TranslateAnimation showAnimation = new TranslateAnimation(
//                            Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, -1.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f);
//                    showAnimation.setDuration(400);
//                    mTvSource.setAnimation(showAnimation);
//                    mTvSource.setVisibility(View.VISIBLE);

                ObjectAnimator translate = ObjectAnimator.ofFloat(mTvSource, "translationY", -200, 0);
                translate.setDuration(300);//设置动画时间
                translate.start();
                //mTvSource.setVisibility(View.VISIBLE);
                // }

//                ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
//                anim.setDuration(300);
//                anim.start();
            }
        });

        mBtnFadeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (mTvSource.getVisibility() == View.VISIBLE) {
//                ObjectAnimator animator = ObjectAnimator.ofFloat(textview, "translationX", curTranslationX, -500f, curTranslationX);
//                animator.setDuration(5000);
//                animator.start();
                ObjectAnimator translate = ObjectAnimator.ofFloat(mTvSource, "translationY", 0, -200);
                translate.setDuration(300);//设置动画时间
                translate.start();
                //mTvSource.setVisibility(View.GONE);
//                    TranslateAnimation hiddenAnimation = new TranslateAnimation(
//                            Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, -1.0f);
//                    hiddenAnimation.setDuration(200);
//                    mTvSource.setAnimation(hiddenAnimation);

                //  }

            }
        });

    }

    private void collapseRankLayout() {

            if (!mIsAnima && mSourceLayoutParams.topMargin == 0) {
                ObjectAnimator sourceTranslate = ObjectAnimator.ofFloat(mTvSource, "Y", 0, -200);
                sourceTranslate.addListener(animationListener);
                sourceTranslate.addUpdateListener(updateListener);
                ObjectAnimator lvTranslate = ObjectAnimator.ofFloat(mLvTest, "translationY", 0, -200);
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(sourceTranslate, lvTranslate);
                animatorSet2.setDuration(400);
                animatorSet2.start();
            }

    }

    //展开
    private void expandRankLayout() {

            if (!mIsAnima && mSourceLayoutParams.topMargin == 200 * (-1)) {
                ObjectAnimator sourceTranslate = ObjectAnimator.ofFloat(mTvSource, "Y", -200, 0);
                sourceTranslate.addListener(animationListener);
                sourceTranslate.addUpdateListener(updateListener);
                ObjectAnimator lvTranslate = ObjectAnimator.ofFloat(mLvTest, "translationY", -200, 0);
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playTogether(sourceTranslate, lvTranslate);
                animatorSet2.setDuration(400);
                animatorSet2.start();

            }


    }

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener(){
        @Override
        public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator animation) {
            float value = (Float) animation.getAnimatedValue();
            LogUtils.d("123 expandRankLayout value:" + value);
            mSourceLayoutParams.topMargin = (int) value;
        }
    };
    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mIsAnima = true;

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mIsAnima = false;

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
}
