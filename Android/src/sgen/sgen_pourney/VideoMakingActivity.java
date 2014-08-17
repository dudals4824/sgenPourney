package sgen.sgen_pourney;

import java.util.Calendar; //나중에 필요
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import sgen.android.photoput.PhotoputActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class VideoMakingActivity extends Activity implements OnClickListener {
	// final Calendar c = Calendar.getInstance();
	//
	// int Hour = c.get(Calendar.HOUR_OF_DAY); // HOUR는 12시간, HOUR_OF_DAY는 24시간
	// // 입니다.
	// int Minute = c.get(Calendar.MINUTE);
	// int Second = c.get(Calendar.SECOND); // 저장하기 위해서 시,분,초 값을 받아오는 부분
	// // 나중에는 과반수 이상이 만들기를 눌렀다는 조건문을 포함 시켜
	// int time = Hour*60*60+Minute*60+Second+24*60*60; // 줘야 함. 이건 디비에 저장하기
	private CountDownTimer countDownTimer;
	private TextView timer;
	
	private Calendar gregorian=new GregorianCalendar();

	private long currentTime=gregorian.getTimeInMillis();
	private final long videoDueTime=currentTime+10*1000;;
	private long startTime= videoDueTime-currentTime;// 24 * 60 * 60 * 1000; //24시간 밀리세컨
												// 단위임 비교한 값 여기에 넣으면 됨요
	private long temp=startTime;
	private final long interval = 100;
	private Button gogoVideo;
	private SimpleSideDrawer mDrawer;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_video);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		
		gogoVideo = (Button) findViewById(R.id.gogoVideo);
		gogoVideo.setOnClickListener(this);
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});
		
		timer = (TextView) this.findViewById(R.id.timer);
		countDownTimer = new MyCountDownTimer(temp, interval);

		timer.setText(timer.getText() + String.valueOf(startTime / 1000));
		countDownTimer.start();
		
	
	}

	public class MyCountDownTimer extends CountDownTimer {

		public MyCountDownTimer(long startTime, long interval) {

			super(startTime, interval);

		}

		@Override
		public void onFinish() {
			countDownTimer.cancel();
			timer.setText("Your Movie will be shown soon!");
			Intent intent = new Intent(VideoMakingActivity.this, VideoViewActivity.class);
			startActivity(intent);

		}

		@Override
		public void onTick(long millisUntilFinished) {

			timer.setText(String.valueOf(millisUntilFinished / 1000 / 60 / 60) // 시
					+ " : "
					+ String.valueOf((millisUntilFinished / 1000 / 60) % 60) // 분
					+ " : "
					+ String.valueOf((millisUntilFinished / 1000) % 60) // 초
					+ " : "
					+ String.valueOf((millisUntilFinished % 1000) / 100)); // 밀리초
			// timer.setText("" + millisUntilFinished / 1000);
			
		}
		
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.gogoVideo) {
			Intent intent = new Intent(this, VideoViewActivity.class);
			startActivity(intent);
		}
	
	}
	public void onBackPressed()
	{
		super.onBackPressed();
		countDownTimer.cancel();
		finish();
	}
}
