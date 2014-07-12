package sgen.sgen_pourney;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

public class VideoMakingActivity extends Activity {
	LinearLayout layoutAlbum;
	private EditText timer;
	// private String time;
	final Calendar c = Calendar.getInstance();

	int Hour = c.get(Calendar.HOUR_OF_DAY); // HOUR는 12시간, HOUR_OF_DAY는 24시간
											// 입니다.
	int Minute = c.get(Calendar.MINUTE);
	int Second = c.get(Calendar.SECOND);
	int AmPm = c.get(Calendar.AM_PM);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_video);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		timer = (EditText) findViewById(R.id.timer);
		timer.setText(Hour + " : " + Minute + " : " + Second);
		
	}

}
