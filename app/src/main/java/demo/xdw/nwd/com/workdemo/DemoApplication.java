package demo.xdw.nwd.com.workdemo;

import android.app.Application;

import com.android.base.frame.Base;

import demo.xdw.nwd.com.workdemo.demo.snapshot.ScreenShotListenManager;

/**
 * Created by xudengwang on 2016/10/11.
 */

public class DemoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Base.initialize(this);
        ScreenShotListenManager manager = ScreenShotListenManager.newInstance(getApplicationContext());
        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    public void onShot(String imagePath) {
                        Base.showToast("shot imagepath:"+imagePath);
                    }
                }
        );
        manager.startListen();
    }
}
