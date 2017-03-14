package demo.xdw.nwd.com.workdemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


@SuppressWarnings("unused")
public class PreferencesUtils {

    public static final String PREF_HONGBAO = "pref_user_info";

    public static final String KEY_HONGBAO_RECORDS = "key_hongbao_records";

    public static void clearUserData(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_HONGBAO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    public static void saveRobHongbaoRecords(Context ctx, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        SharedPreferences sp = ctx.getSharedPreferences(PREF_HONGBAO,
                Context.MODE_PRIVATE);
        String records = sp.getString(KEY_HONGBAO_RECORDS, "");
        StringBuffer buffer = new StringBuffer(records);
        LogUtils.d("buffer:" + buffer.toString());
        if (TextUtils.isEmpty(buffer.toString())) {
            buffer.append("抢红包记录：\n");
        } else {
            buffer.append("\n");
        }
        buffer.append(value);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_HONGBAO_RECORDS, buffer.toString());
        editor.commit();
    }

    public static String getRobHongbaoRecords(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF_HONGBAO,
                Context.MODE_PRIVATE);
        String records = sp.getString(KEY_HONGBAO_RECORDS, "");
        return records;
    }

}
