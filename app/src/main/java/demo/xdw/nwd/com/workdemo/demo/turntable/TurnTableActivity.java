package demo.xdw.nwd.com.workdemo.demo.turntable;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.base.frame.activity.BaseActivity;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;

/**
 * Created by xudengwang on 2017/2/22.
 */

public class TurnTableActivity extends BaseActivity implements RotatePan.AnimationEndListener {
    @Bind(R.id.rotatePan)
    RotatePan rotatePan;
    @Bind(R.id.luckpan_layout)
    LuckPanLayout luckPanLayout;
    @Bind(R.id.iv_go)
    ImageView goBtn;

    private String[] strs = {"华为手机", "谢谢惠顾", "iPhone 6s", "mac book", "魅族手机", "小米手机"};

    @Override
    protected void initData() {
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.startLuckLight();
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        rotatePan.setAnimationEndListener(this);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotation();
            }
        });

        luckPanLayout.post(new Runnable() {
            @Override
            public void run() {
                int height = getWindow().getDecorView().getHeight();
                int width = getWindow().getDecorView().getWidth();

                int backHeight = 0;
                //取当前屏幕高宽最小值
                int minValue = Math.min(width, height);
                //减去2边10dp
                minValue -= Util.dip2px(TurnTableActivity.this, 10) * 2;

                //用于gobtn的坐标调整
                backHeight = minValue / 2;

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) luckPanLayout.getLayoutParams();
                lp.width = minValue;
                lp.height = minValue;
                luckPanLayout.setLayoutParams(lp);

                //距轮盘背景的距离
                minValue -= Util.dip2px(TurnTableActivity.this, 20) * 2;
                lp = (RelativeLayout.LayoutParams) rotatePan.getLayoutParams();
                lp.height = minValue;
                lp.width = minValue;
                rotatePan.setLayoutParams(lp);


                lp = (RelativeLayout.LayoutParams) goBtn.getLayoutParams();
                lp.topMargin += backHeight;
                lp.topMargin -= (goBtn.getHeight() / 2);
                goBtn.setLayoutParams(lp);

                getWindow().getDecorView().requestLayout();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_turn_table;
    }

    private void rotation() {
        rotatePan.startRotate(-1);
        luckPanLayout.setDelayTime(100);
        goBtn.setEnabled(false);
    }

    @Override
    public void endAnimation(int position) {
        goBtn.setEnabled(true);
        luckPanLayout.setDelayTime(500);
        Toast.makeText(this, "Position = " + position + "," + strs[position], Toast.LENGTH_SHORT).show();
    }
}
