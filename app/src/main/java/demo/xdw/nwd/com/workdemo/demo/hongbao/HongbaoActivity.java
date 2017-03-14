package demo.xdw.nwd.com.workdemo.demo.hongbao;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.base.frame.activity.BaseActivity;

import java.util.List;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;
import demo.xdw.nwd.com.workdemo.util.PreferencesUtils;

/**
 * Created by xudengwang on 2017/1/22.
 */

public class HongbaoActivity extends BaseActivity{
    @Bind(R.id.btn_open_rob)
    Button mBtnOpenRob;
    @Bind(R.id.btn_close_rob)
    Button mBtnCloseRob;

    @Bind(R.id.tv_log)
    TextView mTvlog;

    private static final String SERVICE_NAME = "demo.xdw.nwd.com.workdemo/.service.HongbaoService";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rob_hongbao;
    }

    @Override
    protected void initData() {
        mContext = this;
        mBtnOpenRob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEnable(SERVICE_NAME)) {
                    Intent settingintent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(settingintent);
                }

            }
        });

        mBtnCloseRob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEnable(SERVICE_NAME)) {
                    Intent settingintent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(settingintent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        if(isEnable(SERVICE_NAME)) {
            mBtnOpenRob.setText("已开启抢红包服务");
        } else {
            mBtnOpenRob.setText("开启抢红包服务");
        }

        mTvlog.setText(PreferencesUtils.getRobHongbaoRecords(mContext));
        super.onResume();
    }

    private boolean isEnable(String name) {
        AccessibilityManager am = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        List<AccessibilityServiceInfo> installedAccessibilityServiceList = am.getInstalledAccessibilityServiceList();
        for (AccessibilityServiceInfo info : serviceInfos) {
            LogUtils.d( "all -->" + info.getId());
            if (name.equals(info.getId())) {
                return true;
            }
        }
        return false;
    }

}
