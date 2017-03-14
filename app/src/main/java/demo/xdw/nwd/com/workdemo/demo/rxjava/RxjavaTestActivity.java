package demo.xdw.nwd.com.workdemo.demo.rxjava;

import android.view.View;
import android.widget.Button;

import com.android.base.frame.activity.BaseActivity;

import butterknife.Bind;
import demo.xdw.nwd.com.workdemo.R;
import demo.xdw.nwd.com.workdemo.util.LogUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;


/**
 * Created by xudengwang on 2016/12/1.
 */

public class RxjavaTestActivity extends BaseActivity {
    @Bind(R.id.btn_test1)
    Button mBtnTest1;
    @Bind(R.id.btn_test2)
    Button mBtnTest2;
    @Bind(R.id.btn_test3)
    Button mBtnTest3;

    @Override
    protected void initData() {
        mBtnTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testOnSubscribe();
            }
        });
        mBtnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testOperatorMap();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rxjava_test;
    }

    private void testOperatorMap() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                LogUtils.i(getClass(), "Completed");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i(getClass(), "Error");
            }

            @Override
            public void onNext(String s) {
                LogUtils.i(getClass(), s);
            }
        };
        //使用Observable.create()创建被观察者
        Observable.just("hello,word").map(new Func1<String, String>() {
            @Override
            public String call(String o) {
                return o + "sss";
            }
        }).subscribe(observer);

    }

    private void testOnSubscribe() {
        //创建一个观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onCompleted() {
                LogUtils.i(getClass(), "Completed");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i(getClass(), "Error");
            }

            @Override
            public void onNext(String s) {
                LogUtils.i(getClass(), s);
            }
        };
        //使用Observable.create()创建被观察者
        Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Wrold");
                subscriber.onCompleted();
            }
        });
        observable1.map(new Func1<String, String>() {
            @Override
            public String call(String o) {
                return o + "sss";
            }
        });
        observable1.subscribe(observer);

    }


}
