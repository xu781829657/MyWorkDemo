package demo.xdw.nwd.com.workdemo.demo.anima;


import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.base.frame.Base;
import com.android.base.frame.activity.BaseActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.adapter.AnimaListAdapter;
import demo.xdw.nwd.com.workdemo.adapter.MainRvAdapter;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

public class AnimaActivity extends BaseActivity {
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    private List<String> mDemos;
    private AnimaListAdapter mAdapter;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//    }
    @Override
    protected void initData() {
        mContext = this;
        Base.saveDisplaySize(this);
        String[] demo_string_array = getResources().getStringArray(R.array.anima_array);
        mDemos = Arrays.asList(demo_string_array);
        LogUtils.d(getClass(),"mdemos:"+mDemos.size());
        mAdapter = new AnimaListAdapter(mContext, mDemos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
       // recyclerView.addItemDecoration(new RecycleViewDivider(mContext,LinearLayoutManager.VERTICAL, 100, Color.BLACK));
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.GRAY)
                        .size(getResources().getDimensionPixelSize(R.dimen.divider))
                        .margin(getResources().getDimensionPixelSize(R.dimen.leftmargin),
                                getResources().getDimensionPixelSize(R.dimen.rightmargin))
                        .build());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }
}
