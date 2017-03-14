package demo.xdw.nwd.com.workdemo.demo.onlineservice;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.base.frame.Base;
import com.android.base.frame.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * Created by xudengwang on 2016/10/21.
 */

public class OnlineServiceEntryActivity extends BaseActivity {
    @Bind(R.id.iv_add)
    ImageView mIvAdd;

    private PopupWindow mPop;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_online_service_entry;
    }

    @Override
    protected void initData() {
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });

    }

    private void showPop() {
        if (mPop != null && mPop.isShowing()) {
            mPop.dismiss();
            return;
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindow_service_entry, null);
        LinearLayout serviceLin = (LinearLayout) view.findViewById(R.id.lin_online_service);
        LinearLayout moreLin = (LinearLayout) view.findViewById(R.id.lin_more);
        serviceLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Base.showToast("在线客服");
            }
        });
        moreLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Base.showToast("更多");
            }
        });
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPop != null && mPop.isShowing()) {
                    mPop.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPop = new PopupWindow(view, (int)(100* Base.getDensity()), WindowManager.LayoutParams.WRAP_CONTENT);
        LogUtils.d(getClass(),"mIvAdd.getMeasuredWidth():"+mIvAdd.getMeasuredWidth());
        mPop.showAsDropDown(mIvAdd, -((int)(100* Base.getDensity())-mIvAdd.getMeasuredWidth()), -20);

    }


}




