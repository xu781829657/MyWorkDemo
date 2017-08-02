package demo.xdw.nwd.com.workdemo.demo.listview;

import com.android.base.frame.activity.BaseActivity;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.adapter.RvPullTestAdapter;

/**
 * Created by xudengwang on 2017/8/1.
 */

public class RvPullUpAc extends BaseActivity {
    @Bind(R.id.refresh)
    TwinklingRefreshLayout mRefreshLayout;
    @Bind(R.id.rv)
    RecyclerView mRv;
    private RvPullTestAdapter mAdapter;
    private List<String> mDatas = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.ac_rv_pull_up;
    }

    @Override
    protected void initData() {
        mContext = this;
        refreshData();
        refreshAdapter();
        setupRecyclerView();
    }

    private void refreshAdapter() {
        if (mAdapter == null) {
            mRv.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new RvPullTestAdapter(mContext, mDatas);
            mRv.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView() {

//        ProgressLayout headerView = new ProgressLayout(getContext());
//        BezierLayout headerView = new BezierLayout(this);
//        mRefreshLayout.setHeaderView(headerView);
        ProgressLayout headerView = new ProgressLayout(this);
        mRefreshLayout.setHeaderView(headerView);
        LoadingView loadingView = new LoadingView(this);
        mRefreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);
   //     mRefreshLayout.setPureScrollModeOn();
    //   mRefreshLayout.setEnableOverlayRefreshView(false);
//        mRefreshLayout.setAutoLoadMore(true);
//        mRefreshLayout.setOverScrollRefreshShow(false);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        refreshAdapter();
                        mRefreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                        refreshAdapter();
                        mRefreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });

    }

    private void refreshData() {
        mDatas.clear();
        for (int i = 0; i < 20; i++) {
            mDatas.add("item" + i);
        }
    }

    private void loadMore() {
        for (int i = mDatas.size(); i < mDatas.size() + 10; i++) {
            mDatas.add("item" + i);
        }
    }
}
