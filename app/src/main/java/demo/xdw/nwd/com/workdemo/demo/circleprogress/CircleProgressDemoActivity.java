package demo.xdw.nwd.com.workdemo.demo.circleprogress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import demo.xdw.nwd.com.workdemo.R;


public class CircleProgressDemoActivity extends Activity {
	private HalfRoundProgressBar mRoundProgressBar1;
	private RoundProgressBar mRoundProgressBar2 ,mRoundProgressBar3, mRoundProgressBar4, mRoundProgressBar5;
	private int progress = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cricle_progress);
		
		mRoundProgressBar1 = (HalfRoundProgressBar) findViewById(R.id.roundProgressBar1);
		mRoundProgressBar2 = (RoundProgressBar) findViewById(R.id.roundProgressBar2);
		mRoundProgressBar3 = (RoundProgressBar) findViewById(R.id.roundProgressBar3);
		mRoundProgressBar4 = (RoundProgressBar) findViewById(R.id.roundProgressBar4);
		mRoundProgressBar5 = (RoundProgressBar) findViewById(R.id.roundProgressBar5);
		
		((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progress = 50;
				System.out.println(progress);

				//mRoundProgressBar1.setProgress(progress);
				mRoundProgressBar2.setProgress(progress);
				mRoundProgressBar3.setProgress(progress);
				mRoundProgressBar4.setProgress(progress);
				mRoundProgressBar5.setProgress(progress);

//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						while(progress <= 100){
//							progress += 3;
//
//							System.out.println(progress);
//
//							mRoundProgressBar1.setProgress(progress);
//							mRoundProgressBar2.setProgress(progress);
//							mRoundProgressBar3.setProgress(progress);
//							mRoundProgressBar4.setProgress(progress);
//							mRoundProgressBar5.setProgress(progress);
//
//							try {
//								Thread.sleep(100);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//
//					}
//				}).start();
			}
		});
		
	}


}
