package demo.xdw.nwd.com.workdemo.demo.anima;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.base.frame.activity.BaseActivity;
import com.android.base.util.ScreenUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.adapter.CommonListAdapter;
import demo.xdw.nwd.com.workdemo.adapter.TestListAdapter;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * Created by xudengwang on 2017/3/31.
 */

public class FadeAnimaActivity extends BaseActivity {

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
        mTvSource.setLayoutParams(mSourceLayoutParams);
        TestListAdapter adapter = new TestListAdapter(mContext, titles);
        mLvTest.setAdapter(adapter);
        mLvTest.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    scrollFlag = true;
                } else {
                    scrollFlag = false;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!scrollFlag) {
                    return;
                }
                int currentTop;
                LogUtils.d("firstVisibleItem" + firstVisibleItem);
                View firstChildView = absListView.getChildAt(0);
                if (firstChildView != null) {
                    currentTop = absListView.getChildAt(0).getTop();
                } else {
                    //ListView初始化的时候会回调onScroll方法，此时getChildAt(0)仍是为空的
                    return;
                }
                //判断上次可见的第一个位置和这次可见的第一个位置
                if (firstVisibleItem != mLastFirstPostion) {
                    //不是同一个位置
                    if (firstVisibleItem > mLastFirstPostion) {
                        LogUtils.d("1111111111111111上滑----------");
                        collapseRankLayout();

                    } else {
                        //TODO do up
                        LogUtils.d("1111111111111111下滑++++++++++++++++++++++++++");
                        expandRankLayout();
                    }
                    mLastFirstTop = currentTop;
                } else {
                    //是同一个位置
                    if (Math.abs(currentTop - mLastFirstTop) > touchSlop) {
                        //避免动作执行太频繁或误触，加入touchSlop判断，具体值可进行调整
                        if (currentTop > mLastFirstTop) {
                            LogUtils.d("1111111111111111下滑++++++++++++++++++++++++++");
                            expandRankLayout();
                        } else if (currentTop < mLastFirstTop) {
                            //TODO do down
                            LogUtils.d("1111111111111111上滑----------");
                            collapseRankLayout();
                        }
                        mLastFirstTop = currentTop;
                    }
                }
                mLastFirstPostion = firstVisibleItem;
            }
//
//            if (scrollFlag) {
//                    if (firstVisibleItem > lastVisibleItemPosition) {
//                        LogUtils.d("1111111111111111上滑----------");
//                        collapseRankLayout();
//                    }
//                    if (firstVisibleItem < lastVisibleItemPosition) {
//                        LogUtils.d("1111111111111111下滑++++++++++++++++++++++++++");
//                        expandRankLayout();
//                    }
//                    if (firstVisibleItem == lastVisibleItemPosition) {
//                        return;
//                    }
//                    lastVisibleItemPosition = firstVisibleItem;
//                }
//            }
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
        synchronized (mSourceLayoutParams) {
            if (!mIsAnima && mSourceLayoutParams.topMargin == 0) {
                ObjectAnimator translate = ObjectAnimator.ofFloat(mTvSource, "translationY", 0, -200);
                translate.setDuration(400);//设置动画时间
                translate.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        mSourceLayoutParams.topMargin = (int) value;
                        LogUtils.d("123 collapseRankLayout value:" + value);
                        mLvMarginLayoutParams.topMargin = 200 + (int) value;
                        mLvTest.setLayoutParams(mLvMarginLayoutParams);

                    }
                });
                translate.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mIsAnima = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LogUtils.d("123 collapseRankLayout onAnimation End:");
                        mIsAnima = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                translate.start();
            }

        }
    }

    //展开
    private void expandRankLayout() {
        synchronized (mSourceLayoutParams) {
            if (!mIsAnima && mSourceLayoutParams.topMargin == 200 * (-1)) {
                ObjectAnimator translate = ObjectAnimator.ofFloat(mTvSource, "translationY", -200, 0);
                translate.setDuration(400);//设置动画时间
                translate.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator animation) {
                        float value = (Float) animation.getAnimatedValue();
                        LogUtils.d("123 expandRankLayout value:" + value);
                        mSourceLayoutParams.topMargin = (int) value;
                        mLvMarginLayoutParams.topMargin = (int) value + 200;
                        mLvTest.setLayoutParams(mLvMarginLayoutParams);

                    }
                });
                translate.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mIsAnima = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LogUtils.d("123 expandRankLayout onAnimation End:");
                        mIsAnima = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                translate.start();

            }
        }

    }
}
