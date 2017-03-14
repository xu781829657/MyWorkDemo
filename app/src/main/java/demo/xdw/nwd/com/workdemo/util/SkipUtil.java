package demo.xdw.nwd.com.workdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by xudengwang on 2016/10/11.
 */

public class SkipUtil {

    public static void gotoActivity(Context ctx,Class<? extends Activity> clazz, Bundle bundle, int flags) {
        Intent intent = new Intent(ctx, clazz);
        if (bundle != null) intent.putExtras(bundle);
        intent.addFlags(flags);
        ctx.startActivity(intent);
    }

    public static void gotoActivity(Context ctx,Class<? extends Activity> clazz) {
        Intent intent = new Intent(ctx, clazz);
        ctx.startActivity(intent);
    }
}

