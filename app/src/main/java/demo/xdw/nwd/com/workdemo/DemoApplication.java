package demo.xdw.nwd.com.workdemo;

import android.app.Application;

import com.android.base.frame.Base;

/**
 * Created by xudengwang on 2016/10/11.
 */

public class DemoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Base.initialize(this);
    }
}
