package sgen.sgen_pourney;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoMakingActivity extends Activity {
	LinearLayout layoutAlbum;

//	final Calendar c = Calendar.getInstance();
//
//	int Hour = c.get(Calendar.HOUR_OF_DAY); // HOUR는 12시간, HOUR_OF_DAY는 24시간
//											// 입니다.
//	int Minute = c.get(Calendar.MINUTE);
//	int Second = c.get(Calendar.SECOND); // 저장하기 위해서 시,분,초 값을 받아오는 부분
//											// 나중에는 과반수 이상이 만들기를 눌렀다는 조건문을 포함 시켜
//	int time = Hour*60*60+Minute*60+Second+24*60*60; // 줘야 함. 이건 디비에 저장하기 
	private CountDownTimer countDownTimer;
	private TextView timer;
	private final long startTime = 24 * 60 * 60 * 1000; //24시간 밀리세컨 단위임 비교한 값 여기에 넣으면 됨요
	private final long interval = 100 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_video);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		
		timer = (TextView) this.findViewById(R.id.timer);
		countDownTimer = new MyCountDownTimer(startTime, interval);
		
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

		}

		@Override
		public void onTick(long millisUntilFinished) {

			timer.setText(String.valueOf(millisUntilFinished / 1000 / 60 /60 ) //시
					+" : "+String.valueOf((millisUntilFinished / 1000 / 60 )%60) //분
					+" : "+String.valueOf((millisUntilFinished / 1000)%60) //초
					+" : "+String.valueOf((millisUntilFinished % 1000)/100));  //밀리초
//			timer.setText("" + millisUntilFinished / 1000);

		}

	}

}
