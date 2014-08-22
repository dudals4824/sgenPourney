package sgen.sgen_pourney;

import smu.tp.calendar.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Handler hd = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				finish();
			}
			
		};
		hd.sendEmptyMessageDelayed(0, 3000);
	}
}
