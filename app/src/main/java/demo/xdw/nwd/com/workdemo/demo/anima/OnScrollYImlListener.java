package demo.xdw.nwd.com.workdemo.demo.anima;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * Created by xudengwang on 2017/4/6.
 */

public abstract class OnScrollYImlListener implements AbsListView.OnScrollListener {

    private Map<Integer, Integer> mItemHeights = new HashMap<Integer, Integer>();
    private ListView mListView;
    private int mLastFirstPostion;
    private boolean scrollFlag = false;// 标记是否滑动
    private int mLastFirstTop;
    private int touchSlop = 240;

    public OnScrollYImlListener(ListView listView) {
        this.mListView = listView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            scrollFlag = false;
            // 判断滚动到顶部
            if (mListView.getFirstVisiblePosition() == 0) {
                View firstChildView = mListView.getChildAt(0);
                if (firstChildView != null) {
                    if (Math.abs(mListView.getChildAt(0).getTop()) < 20) {
                        onScrollYPullDown();
                    }
                }

            }

        } else {
            scrollFlag = true;
        }

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!scrollFlag) {
            return;
        }
        int currentTop;
        LogUtils.d("firstVisibleItem" + firstVisibleItem);
        View firstChildView = mListView.getChildAt(0);
        if (firstChildView != null) {
            currentTop = mListView.getChildAt(0).getTop();
        } else {
            //ListView初始化的时候会回调onScroll方法，此时getChildAt(0)仍是为空的
            return;
        }
        int scrollY = getScrollY(currentTop, firstVisibleItem, firstChildView.getHeight());
        if (scrollY < touchSlop) {
            return;
        }
        if (firstVisibleItem != mLastFirstPostion) {
            //不是同一个位置
            if (firstVisibleItem > mLastFirstPostion) {
                LogUtils.d("上滑");
                onScrollYSlideUp();

            } else {
                LogUtils.d("1下拉");
                onScrollYPullDown();
            }
            mLastFirstTop = currentTop;
            mLastFirstPostion = firstVisibleItem;
        }
    }

    public int getScrollY(int currentTop, int firstVisibleItem, int childViewHeight) {
        int scrollY;

        scrollY = Math.abs(currentTop - mLastFirstTop) + Math.abs(firstVisibleItem - mLastFirstPostion) * childViewHeight;
        LogUtils.d("0000000000getScrollY scrollY:" + scrollY);
        return scrollY;
    }


    /**
     * 垂直方向滚动距离
     */
    protected abstract void onScrollYSlideUp();

    protected abstract void onScrollYPullDown();

}