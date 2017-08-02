package demo.xdw.nwd.com.workdemo.demo.cityaddress;

import com.android.base.frame.activity.BaseActivity;
import com.lljjcoder.citypickerview.widget.CityPicker;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;

/**
 * Created by xudengwang on 2017/7/21.
 * 注意你的思想，因为它将变成言辞；
 * 注意你的言辞，因为它将变成行动；
 * 注意你的行动，因为它将变成习惯；
 * 注意你的习惯，因为它将变成性格；
 * 注意你的性格，因为它将决定你的命运.
 */

public class CityAddressAc extends BaseActivity {
    @Bind(R.id.btn_city_address)
    Button mBtnCityAddress;
    @Bind(R.id.tv_result)
    TextView mTvResult;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_city_address;
    }

    @Override
    protected void initData() {
        mContext = this;
        mBtnCityAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityPicker cityPicker = new CityPicker.Builder(mContext).textSize(20)
                        .province("上海市")
                        .city("上海市")
                        .district("黄浦区")
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(true)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .build();

                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        mTvResult.setText("选择结果：\n省：" + citySelected[0] + "\n市：" + citySelected[1] + "\n区："
                                + citySelected[2] + "\n邮编：" + citySelected[3]);
                    }
                });
            }
        });
    }
}
