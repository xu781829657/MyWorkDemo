package demo.xdw.nwd.com.workdemo.util;

import android.util.Log;

/**
 * Created by xudengwang on 2016/6/1.
 */
public class LogUtils {

    public static boolean print = true;
    private static final String LOG_FILTER = "demo--";

    public static void d(String log) {
        if (print) {
            Log.d(LOG_FILTER, log);
        }
    }

    public static void d(Class<?> class_value, String log) {
        if (print) {
            String tag = class_value.getName();
            int index = tag.lastIndexOf('.');
            if (index != -1)
                tag = tag.substring(index + 1);
            Log.d(tag, LOG_FILTER + log);
        }
    }

    public static void i(Class<?> class_value, String log) {
        if (print) {
            String tag = class_value.getName();
            int index = tag.lastIndexOf('.');
            if (index != -1)
                tag = tag.substring(index + 1);
            Log.i(tag, LOG_FILTER + log);
        }
    }

    public static void e(Class<?> class_value, String log) {
        if (print) {
            String tag = class_value.getName();
            int index = tag.lastIndexOf('.');
            if (index != -1)
                tag = tag.substring(index + 1);
            Log.e(tag, LOG_FILTER + log);
        }
    }
}
