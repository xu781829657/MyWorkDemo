package demo.xdw.nwd.com.workdemo.demo.test;

import android.view.View;
import android.widget.Button;

import com.android.base.frame.Base;
import com.android.base.frame.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * Created by xudengwang on 2016/11/24.
 */

public class TestActivity extends BaseActivity{
    @Bind(R.id.btn_test_regular)
    Button mBtnTestRegular;

    private static final String REGULAR_NUM = "5.0年09个月28天";

    @Override
    protected void initData() {
        mBtnTestRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regularFilter();
                filter();
                matcher(REGULAR_NUM);
            }
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test;
}


    private void regularFilter() {
        Pattern p = Pattern.compile("\\d{1,}");//这个2是指连续数字的最少个数
        //String u = "abc435345defsfsaf564565fsabad5467755fewfadfgea";
        Matcher m = p.matcher(REGULAR_NUM);
        int i = 0;
        for(int j =0; j<m.groupCount();j++){
            LogUtils.d(getClass(),"m.groupCount()["+j+"]"+m.group(j));
        }
        while (m.find()) {

            System.out.println(m.group());
            i++;
        }
        Base.showToast(i+"");
        System.out.println(i);


    }

    private void filter(){
        String[] nums = REGULAR_NUM.split("\\D+");
        for (int i= 0; i<nums.length;i++){
            LogUtils.d(getClass(),"filter num["+i+"]"+nums[i]);
        }
        //LogUtils.d(getClass(),"nums.size");
    }

    private static Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");

    public  String[] matcher(String input) {
        Matcher matcher = pattern.matcher(input);
        List<String> list = new ArrayList();
        while (matcher.find()) {

            list.add(matcher.group());
            LogUtils.d(getClass(),"matcher.group():"+matcher.group());
        }
        return list.toArray(new String[0]);
    }

}
