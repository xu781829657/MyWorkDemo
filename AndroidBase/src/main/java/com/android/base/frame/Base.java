package com.android.base.frame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.android.base.util.ToastUtils;
import com.apkfuns.logutils.LogUtils;

/**
 * Base helps to get {@link Context}, {@link Resources}, {@link AssetManager}, {@link Configuration} and {@link DisplayMetrics} in any class.
 *
 * @author Leonardo Taehwan Kim
 */
public class Base {
    private static String PREF_NAME = "system.pref";
    private static Context context;

    public static void initialize(@NonNull Context context) {
        Base.context = context;
    }

    public static Context getContext() {
        synchronized (Base.class) {
            if (Base.context == null)
                throw new NullPointerException("Call Base.initialize(context) within your Application onCreate() method.");

            return Base.context.getApplicationContext();
        }
    }

    public static Resources getResources() {
        return Base.getContext().getResources();
    }

    public static Resources.Theme getTheme() {
        return Base.getContext().getTheme();
    }

    public static AssetManager getAssets() {
        return Base.getContext().getAssets();
    }

    public static Configuration getConfiguration() {
        return Base.getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return Base.getResources().getDisplayMetrics();
    }


    public static SharedPreferences getPreferences() {
        SharedPreferences pre = getContext().getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    public static int getWidth() {
        return getPreferences().getInt("screen_width", 720);
    }
    public static int getHeight() {
        return getPreferences().getInt("screen_height", 720);
    }

    public static float getDensity() {
        return getPreferences().getFloat("density", 2.0f);
    }

    public static void saveDisplaySize(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt("screen_width", displaymetrics.widthPixels);
        editor.putInt("screen_height", displaymetrics.heightPixels);
        editor.putFloat("density", displaymetrics.density);
        LogUtils.d("base", "widthPixels:"
                + displaymetrics.widthPixels + ",heightpixels:"
                + displaymetrics.heightPixels + ",density:" + displaymetrics.density);
        editor.commit();
    }

    public static String string(int id) {
        return getResources().getString(id);
    }

    public static String string(int id, Object... args) {
        return getResources().getString(id, args);
    }

    //toast提示
    public static void showToast(String string) {
        ToastUtils.getInstance().showToast(string);
    }

    public static void showToast(int resid) {
        ToastUtils.getInstance().showToast(string(resid));
    }
}
// TODO: Thread safety
// TODO: ripple, bitmap, time, contact list, picture list, video list, connectivity, wake lock, screen lock/off/on, get attributes, cookie, audio
// TODO: keystore
// TODO: http://jo.centis1504.net/?p=1189
// TODO: Test codes