package demo.xdw.nwd.com.workdemo.demo.rules;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import com.android.base.frame.Base;
import com.android.base.frame.activity.BaseActivity;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;

/**
 * Created by xudengwang on 2016/10/11.
 */

public class RulesDemoActivity extends BaseActivity {
    Toolbar mToolbar;
    @Bind(R.id.rsb_rate)
    RangeSeekBar mRsbRate;
    @Bind(R.id.rsb_loan)
    RangeSeekBar mRsbLoan;

    @Bind(R.id.tiEt_rate_min)
    TextInputEditText mEtRateMin;
    @Bind(R.id.tiEt_rate_max)
    TextInputEditText mEtRateMax;
    @Bind(R.id.tiEt_loan_min)
    TextInputEditText mEtLoanMin;
    @Bind(R.id.tiEt_loan_max)
    TextInputEditText mEtLoanMax;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_rules_demo;
    }

    @Override
    protected void initData() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("智能抢标");

        mRsbRate.setOnRangeChangeListener(new RangeSeekBar.OnRangeChangeListener() {
            @Override
            public void onRangeChange(int leftValue, int rightValue) {
                LogUtils.d(getClass(), "onRangeChange:" + leftValue + ",rightValue" + rightValue);
                mEtRateMin.setText(String.valueOf(leftValue));
                mEtRateMax.setText(String.valueOf(rightValue));
            }
        });
        mRsbLoan.setOnRangeChangeListener(new RangeSeekBar.OnRangeChangeListener() {
            @Override
            public void onRangeChange(int leftValue, int rightValue) {
                LogUtils.d(getClass(), "onRangeChange:" + leftValue + ",rightValue" + rightValue);
                mEtLoanMin.setText(String.valueOf(leftValue));
                mEtLoanMax.setText(String.valueOf(rightValue));
            }
        });

        mEtRateMin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtils.d(getClass(), hasFocus + "");
                if (hasFocus) {
                    mEtRateMin.addTextChangedListener(new EtWatcher(mEtRateMin, FLAG_RATE_MIN));
                } else {
                    mEtRateMin.addTextChangedListener(null);
                }
            }
        });
        mEtRateMax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtRateMax.addTextChangedListener(new EtWatcher(mEtRateMax, FLAG_RATE_MAX));
                } else {
                    mEtRateMax.addTextChangedListener(null);
                }
            }
        });

        mEtLoanMin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtLoanMin.addTextChangedListener(new EtWatcher(mEtLoanMin, FLAG_LOAN_MIN));
                } else {
                    mEtLoanMin.addTextChangedListener(null);
                }
            }
        });

        mEtLoanMax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtLoanMax.addTextChangedListener(new EtWatcher(mEtLoanMax, FLAG_LOAN_MAX));
                } else {
                    mEtLoanMax.addTextChangedListener(null);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int FLAG_RATE_MIN = 0;
    private static final int FLAG_RATE_MAX = 1;
    private static final int FLAG_LOAN_MIN = 2;
    private static final int FLAG_LOAN_MAX = 3;

    class EtWatcher implements TextWatcher {
        private TextInputEditText editText;
        private int flag;

        public EtWatcher(TextInputEditText editText, int flag) {
            this.editText = editText;
            this.flag = flag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LogUtils.d(getClass(), "onTextChanged s.tostring:" + s.toString());
            editText.setSelection(s.length());
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString())) {
                return;
            }
            switch (this.flag) {
                case FLAG_RATE_MIN:
                    mRsbRate.setMinValue(Integer.valueOf(s.toString()));
                    break;
                case FLAG_RATE_MAX:
                    mRsbRate.setmMaxValue(Integer.valueOf(s.toString()));
                    break;
                case FLAG_LOAN_MIN:
                    mRsbLoan.setMinValue(Integer.valueOf(s.toString()));
                    break;
                case FLAG_LOAN_MAX:
                    mRsbLoan.setmMaxValue(Integer.valueOf(s.toString()));
                    break;
            }

        }
    }
}
