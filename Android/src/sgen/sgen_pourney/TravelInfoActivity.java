package sgen.sgen_pourney;

import java.util.GregorianCalendar;
import java.util.HashMap;

import sgen.session.UserSessionManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TravelInfoActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnFocusChangeListener {
	private ExpandableHeightGridView gridCalendar, gridDate;
	private TextView textTitle, textTitleHere, textCalendarHere,
			textPeopleHere, textInputInfo, textMonth;
	private ImageButton btnPrevMonth, btnNextMonth, btnPut;
	private EditText editTitle;
	private Dayinfo today;
	private int flagselectdate = 0;
	int startdate = 0;
	int enddate = 0;
	String[] DayArray;
	Dayadapter dayadapter;
	CalendarAdapter calendarAdapter;
	// 달력 이동을 위한 변수
	private int cnt = 0;
	private String strMonth[] = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.copyofactivity_travel_info);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		gridCalendar = (ExpandableHeightGridView) findViewById(R.id.gridCalendar);
		gridDate = (ExpandableHeightGridView) findViewById(R.id.gridDate);
		textTitle = (TextView) findViewById(R.id.textTitle);
		textTitleHere = (TextView) findViewById(R.id.textTitleHere);
		textCalendarHere = (TextView) findViewById(R.id.textCalendarHere);
		textPeopleHere = (TextView) findViewById(R.id.textPeopleHere);
		textInputInfo = (TextView) findViewById(R.id.textInputInfo);
		textMonth = (TextView) findViewById(R.id.textMonth);
		btnPrevMonth = (ImageButton) findViewById(R.id.btnPrevMonth);
		btnNextMonth = (ImageButton) findViewById(R.id.btnnextMonth);
		btnPut = (ImageButton) findViewById(R.id.btnPut);
		editTitle = (EditText) findViewById(R.id.editTitle);

		setFont();

		today = new Dayinfo();
		getCalendar(today);

		btnPrevMonth.setOnClickListener(this);
		btnNextMonth.setOnClickListener(this);
		btnPut.setOnClickListener(this);
		gridCalendar.setOnItemClickListener(this);
		editTitle.setOnFocusChangeListener(this);

		// session test code
		UserSessionManager session = new UserSessionManager(
				getApplicationContext());
		HashMap<String, String> map = new HashMap<String, String>();
		map = session.getUserDetails();
		String UserId = map.get("user_id");

		Toast.makeText(getApplicationContext(), "user id : " + UserId,
				Toast.LENGTH_LONG).show();
		// ////
	}

	private void setFont() {
		Typeface yoon320 = Typeface.createFromAsset(getAssets(), "yoon320.ttf");
		textTitleHere.setTypeface(yoon320);
		textCalendarHere.setTypeface(yoon320);
		textPeopleHere.setTypeface(yoon320);

		Typeface yoon330 = Typeface.createFromAsset(getAssets(), "yoon330.ttf");
		textInputInfo.setTypeface(yoon330);
	}

	public void getCalendar(Dayinfo today) {
		dayadapter = new Dayadapter(this);
		gridDate.setAdapter(dayadapter);
		setCalendar(today);
		calendarAdapter = new CalendarAdapter(TravelInfoActivity.this,
				R.layout.calendar_grid, DayArray, today, startdate, enddate);
		gridCalendar.setAdapter(calendarAdapter);
		gridCalendar.setExpanded(true);

	}

	// 날짜 계산
	public void setCalendar(Dayinfo today) {
		DayArray = new String[42];
		for (int i = 0; i < today.firstdayofthismonth - 1; i++) {
			DayArray[i] = "";
		}
		for (int i = today.firstdayofthismonth - 1, j = 1; j <= today.lastdayofthismonth; i++, j++) {
			DayArray[i] = Integer.toString(j);
		}
		textMonth.setText(strMonth[today.getMonth()]);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// editTitle.clearFocus();
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		findViewById(R.id.container).requestFocus();
		if (v.getId() == R.id.btnPrevMonth) {
			cnt--;
		} else if (v.getId() == R.id.btnnextMonth) {
			cnt++;
		} else if (v.getId() == R.id.btnPut) {
			Intent intent = new Intent(TravelInfoActivity.this,
					PhotoputActivity.class);
			startActivity(intent);
		}
		today = new Dayinfo(cnt);
		getCalendar(today);
		textMonth.setText(strMonth[today.getMonth()] + " " + today.getYear());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stube
		Log.d("id", id + "");
		flagselectdate++;
		int temp;
		if (flagselectdate == 1)
			startdate = (int) id;
		else if (flagselectdate == 2) {
			enddate = (int) id;
			flagselectdate = 0;
			// 선택된 날짜 중 큰 수를 startdate로
			if (startdate > enddate) {
				temp = startdate;
				startdate = enddate;
				enddate = temp;
			}
		}

		calendarAdapter = new CalendarAdapter(TravelInfoActivity.this,
				R.layout.calendar_grid, DayArray, today, startdate, enddate);
		gridCalendar.setAdapter(calendarAdapter);
		Log.d("flagselectdate", flagselectdate + "");
		Log.d("startdate", startdate + "");
		Log.d("enddate", enddate + "");

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.editTitle && hasFocus) {
			editTitle.setBackgroundResource(R.drawable.i_titleput_924x98);
		} else if (v.getId() == R.id.gridDate && hasFocus) {

		} else {
		}
		textTitle.setText(editTitle.getText());
	}

}
