package demo.xdw.nwd.com.workdemo.demo.pullnext;

import com.android.base.frame.activity.BaseActivity;
import com.mingle.pulltonextlayout.OnItemSelectListener;
import com.mingle.pulltonextlayout.PullToNextLayout;
import com.mingle.pulltonextlayout.adapter.PullToNextFragmentAdapter;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;

/**
 * Created by xudengwang on 2017/7/5.
 */

public class PullNextAc extends BaseActivity{
    @Bind(R.id.pullToNextLayout)
    PullToNextLayout pullToNextLayout;

    private ArrayList<Fragment> list;
    private boolean mAdd;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pull_next;
    }

    @Override
    protected void initData() {

        //pullToNextLayout= (PullToNextLayout) findViewById(R.id.pullToNextLayout);


        list=new ArrayList<>();

//        list.add( WebFragment.newInstant(0));
//
//        list.add( WebFragment.newInstant(2));


        //list.add( WebViewFragment.newInstant(0));
        list.add( WebFragment.newInstant(0));
        list.add( WebFragment.newInstant(1));
        list.add( WebFragment.newInstant(2));
        //list.add( WebViewFragment.newInstant(1));
        //list.add( WebViewFragment.newInstant(2));
        //list.add( WebViewFragment.newInstant(3));




       // list.add( WebViewFragment.newInstant(4));

        pullToNextLayout.setAdapter(new PullToNextFragmentAdapter(getSupportFragmentManager(), list));
        pullToNextLayout.setCurrentItem(1);

        pullToNextLayout.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void onSelectItem(int position, View view) {

            }
        });

    }
}
