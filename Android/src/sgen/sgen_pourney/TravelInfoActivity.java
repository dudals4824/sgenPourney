package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sgen.DTO.TripDTO;
import sgen.DTO.UserDTO;
import sgen.session.UserSessionManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TravelInfoActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnFocusChangeListener {
	private ExpandableHeightGridView gridCalendar, gridDate;
	private TextView textTitle, textTitleHere, textCalendarHere,
			textPeopleHere, textInputInfo, textMonth;
	private ImageButton btnPrevMonth, btnNextMonth, btnPut;
	private ImageButton btnPeople1, btnPeople2, btnPeople3;
	private EditText editTitle, peopleName;
	private Dayinfo today;
	private int flagselectdate = 0;
	UserSessionManager session; // 민아
	private UserDTO loggedInUser;
	private TripDTO insertInTrip; // 민아
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

	private TravleInfoPhp travelInfoPhp;// 민아
	private UserInTrips userInTrips;// 민아

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		insertInTrip = new TripDTO();
		loggedInUser = new UserDTO();

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
		btnPeople1 = (ImageButton) findViewById(R.id.btnPeople1);
		btnPeople2 = (ImageButton) findViewById(R.id.btnPeople2);
		btnPeople3 = (ImageButton) findViewById(R.id.btnPeople3);
		editTitle = (EditText) findViewById(R.id.editTitle);
		peopleName = (EditText) findViewById(R.id.peopleName);

		setFont();

		today = new Dayinfo();
		getCalendar(today);

		btnPrevMonth.setOnClickListener(this);
		btnNextMonth.setOnClickListener(this);
		btnPeople1.setOnClickListener(this);
		btnPeople2.setOnClickListener(this);
		btnPeople3.setOnClickListener(this);
		btnPut.setOnClickListener(this);
		gridCalendar.setOnItemClickListener(this);
		editTitle.setOnFocusChangeListener(this);

		// session test code
		UserSessionManager session = new UserSessionManager(
				getApplicationContext());
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map = session.getUserDetails();
		int userId = map.get("user_id");
		
		loggedInUser.setUserId(userId);

		Toast.makeText(getApplicationContext(), "user id : " + userId,
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
		} else if (v.getId() == R.id.btnPeople1) {// 친구찾기 검색창 부분

			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View popupView = layoutInflater.inflate(R.layout.find_friend_popup,
					null);

			final PopupWindow popupWindow = new PopupWindow(popupView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setTouchInterceptor(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

						popupWindow.dismiss();

						return true;

					}

					return false;

				}
			});
			ImageButton btnDismiss = (ImageButton) popupView
					.findViewById(R.id.cancel);
			ImageButton findfriend = (ImageButton) popupView
					.findViewById(R.id.findfriend);
			btnDismiss.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (v.getId() == R.id.cancel) {
						popupWindow.dismiss();
					} else if (v.getId() == R.id.findfriend) {
						// 친구찾은 목록 보여주믄 됨
					}
				}

			});

			popupWindow.showAsDropDown(textCalendarHere, -150, 50);

		} else if (v.getId() == R.id.btnPeople2) {

		} else if (v.getId() == R.id.btnPeople3) {

		}

		else if (v.getId() == R.id.btnPut) {
			Intent intent = new Intent(TravelInfoActivity.this,
					PhotoputActivity.class);
			startActivity(intent);

			String trip_name = editTitle.getText().toString();
			String start_date = Integer.toString(startdate);
			String end_date = Integer.toString(enddate);
			UserSessionManager session = new UserSessionManager(
					getApplicationContext());
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map = session.getUserDetails();
			int UserId = map.get("user_id");
			String User_Id = Integer.toString(UserId);

			travelInfoPhp = new TravleInfoPhp();
			travelInfoPhp.execute(trip_name, start_date, end_date, User_Id);

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

	public class TravleInfoPhp extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			String trip_id = null;
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_name", arg0[0]));
			nameValuePairs.add(new BasicNameValuePair("start_date", arg0[1]));
			nameValuePairs.add(new BasicNameValuePair("end_date", arg0[2]));
			nameValuePairs.add(new BasicNameValuePair("user_id", arg0[3]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/travelInformation.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "error in http connection" + e.toString());
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");
				String line = "0";

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				is.close();
				result = sb.toString().trim();
				Log.e("trips123", result);

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;

				json_data = jArray.getJSONObject(0);
				insertInTrip.setTripId(json_data.getInt("trip_id"));
				insertInTrip.setTriptitle(json_data.getString("trip_name"));
				insertInTrip.setStartdate(Integer.toString(json_data
						.getInt("start_date")));
				insertInTrip.setEnddate(Integer.toString(json_data
						.getInt("end_date")));
				Log.e("Trip information", insertInTrip.toString());
			} catch (JSONException e1) {
				Log.e("log_msg", e1.toString());
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Log.e("log_msg", "onPostExecute intent.. hererere");
			// session.createUserLoginSession(loggedInUser.getUserId(),
			// insertInTrip.getTripId());
			Log.e("user info",
					loggedInUser.getUserId() + " " + insertInTrip.getTripId());

			Log.e("log_msg", "INSERT SUCCESS..");

			String friend1 = "test";
			String friend2 = "mnsjdj";

			userInTrips = new UserInTrips();
			userInTrips.execute(friend1, friend2);

			// Starting MainActivity
			Intent intent = new Intent(getApplicationContext(),
					CoverActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Add new Flag to start new Activity
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	public class UserInTrips extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			UserSessionManager session = new UserSessionManager(
					getApplicationContext());
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map = session.getUserDetails();
			int TripId = map.get("trip_id");
			String trip_id = Integer.toString(TripId);

			for (int k = 0; k < arg0.length; k++) {
				String user_id = null;
				InputStream is = null;
				StringBuilder sb = null;
				String result = null;

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("nick_name", arg0[k]));
				nameValuePairs.add(new BasicNameValuePair("trip_id", trip_id));
				Log.e("trip_id", trip_id);
				Log.e("nick_name", arg0[k]);

				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(
							"http://54.178.166.213/userInTrips.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
				} catch (Exception e) {
					Log.e("log_tag", "error in http connection" + e.toString());
				}
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "iso-8859-1"), 8);
					sb = new StringBuilder();
					sb.append(reader.readLine() + "\n");
					String line = "0";

					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}

					is.close();
					result = sb.toString();
					Log.e("result_user_id", result);

				} catch (Exception e) {
					Log.e("log_tag", "Error converting result " + e.toString());
				}

				try {
					JSONArray jArray = new JSONArray(result);
					JSONObject json_data = null;

					json_data = jArray.getJSONObject(0);
					user_id = json_data.getString("user_id");
					Log.e("user_id", user_id);

				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				Log.d("ASYNC", "result = " + result);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}

}
