package demo.xdw.nwd.com.workdemo.demo.test;

import com.android.base.frame.activity.BaseActivity;

import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;

/**
 * Created by xudengwang on 2017/6/26.
 */

public class TestTextViewAc extends BaseActivity {
    @Bind(R.id.lin_root)
    LinearLayout mLinRoot;
    @Bind(R.id.btn_add)
    Button mBtnAdd;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_test_textview;
    }

    @Override
    protected void initData() {
        mContext = this;
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = new TextView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(15, 25, 15, 25);
                textView.setTextSize(20);
                textView.setText("动态添加");
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.grey));

                mLinRoot.addView(textView);
            }
        });

    }


}
