package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import sgen.android.photoput.PhotoputActivity;
import sgen.session.UserSessionManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TravelInfoActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnFocusChangeListener {
	private ExpandableHeightGridView gridCalendar, gridDate;
	private TextView textTitle, textCalendar, textTitleHere, textCalendarHere,
			textPeopleHere, textInputInfo, textMonth;
	private Button askBtn,logoutBtn,albumBtn,profileBtn;
	private ImageButton btnPrevMonth, btnNextMonth, btnPut;
	private ImageButton btnPeople1, btnPeople2, btnPeople3;
	private EditText editTitle, peopleName;
	private Dayinfo today;
	private int flagselectdate = 0;
	private SimpleSideDrawer mDrawer;
	private UserDTO loggedInUser;
	private TripDTO selectedTrip; // 민아

	// array list for add friend junki
	private ArrayList<String> friendList = new ArrayList<String>();

	int startdate = 0;
	int enddate = 0;
	String[] DayArray;
	Dayadapter dayadapter;
	CalendarAdapter calendarAdapter;

	/**
	 * @author Junki user id, trip id의 세션 유지를 위한 세션 매니저. userId는 로그인에서 얻어온
	 *         userId, tripId는 커버에서 선택한 Id를 유지한다. 새로 trip 생성시에는 tripId 초기 값으로 0을
	 *         가진다
	 */
	UserSessionManager session;

	// 달력 이동을 위한 변수
	private int cnt = 0;
	private String strMonth[] = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	private MakeTravelTask makeTravelTask;// 민아
	private InsertUsersInTrip insertUserInTrips;// 민아

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		selectedTrip = new TripDTO();
		loggedInUser = new UserDTO();

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.copyofactivity_travel_info);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});
		askBtn=(Button)findViewById(R.id.ask_text);
		logoutBtn=(Button)findViewById(R.id.log_out_text);
		albumBtn=(Button)findViewById(R.id.last_album_text);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);
		gridCalendar = (ExpandableHeightGridView) findViewById(R.id.gridCalendar);
		gridDate = (ExpandableHeightGridView) findViewById(R.id.gridDate);
		textTitle = (TextView) findViewById(R.id.textTitle);
		textCalendar = (TextView) findViewById(R.id.textCalendar);
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

		askBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);
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

		// session test code, 화면에 토스트로 현재 세선 정보를 보여준다.
		session = new UserSessionManager(getApplicationContext());
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map = session.getUserDetails();
		int userId = map.get("user_id");
		int tripId = map.get("trip_id");
		loggedInUser.setUserId(userId);
		selectedTrip.setTripId(tripId);
		Toast.makeText(getApplicationContext(),
				"user id : " + userId + "  trip id : " + tripId,
				Toast.LENGTH_LONG).show();
	}// TravelActivity onCreate();

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
		if(v.getId()==R.id.ask_text)
		{
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);
			startActivity(intent);
			finish();
		}
		if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
			finish();
		}
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
					} else;
				}

			});
			
			findfriend.setOnClickListener(new ImageButton.OnClickListener(){
				
				@Override
				public void onClick(View v) {
					if(v.getId() == R.id.findfriend){
						//친구 찾기!!!
						if(1==1){//친구 찾은 경우
							
							LayoutInflater layoutInflater1 = (LayoutInflater) getBaseContext()
									.getSystemService(LAYOUT_INFLATER_SERVICE);
							View popupView1 = layoutInflater1.inflate(R.layout.find_friend_success,
									null);

							final PopupWindow popupWindow1 = new PopupWindow(popupView1,
									LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
							popupWindow1.setBackgroundDrawable(new BitmapDrawable());
							popupWindow1.setFocusable(true);
							popupWindow1.setOutsideTouchable(true);
							
							popupWindow1.setTouchInterceptor(new OnTouchListener() {

								public boolean onTouch(View v, MotionEvent event) {
									if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

										popupWindow1.dismiss();

										return true;

									}

									return false;

								}
							});
							ImageButton btnDismiss = (ImageButton) popupView1
									.findViewById(R.id.cancel);
							ImageButton confirm = (ImageButton) popupView1
									.findViewById(R.id.confirm);
							btnDismiss.setOnClickListener(new ImageButton.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (v.getId() == R.id.cancel) {
										popupWindow1.dismiss();
									} else if (v.getId() == R.id.confirm ) {
										//친구 찾은거 이름 넘갸주면 됨
									}
								}

							});
							//name = (TextView)findViewById(R.id.foundFriend);
							//name.setText(text); //text에 사람 이름 넣으면 될겁니다 아마요..
							
							popupWindow1.showAsDropDown(textCalendarHere, -150, 50);
								
							
							
						}else{//못찾은 경우
							//없는 아이디라고 토스트 부르면 좋을듯
						}
					
					}
				}
			});
			

			popupWindow.showAsDropDown(textCalendarHere, -150, 50);

		} else if (v.getId() == R.id.btnPeople2) {

		} else if (v.getId() == R.id.btnPeople3) {

		}

		else if (v.getId() == R.id.btnPut) {
			String trip_name = editTitle.getText().toString();
			String start_date = Integer.toString(startdate);
			String end_date = Integer.toString(enddate);

			makeTravelTask = new MakeTravelTask();
			makeTravelTask.execute(trip_name, start_date, end_date,
					Integer.toString(loggedInUser.getUserId()));
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
		String start_date1 = " ";
		String end_date1 = " ";
		
		textTitle.setText(editTitle.getText());

		start_date1 = Integer.toString(startdate / 10000) + " "
				+ Integer.toString((startdate % 10000) / 100) + "."
				+ Integer.toString(startdate % 100);
		end_date1 = Integer.toString(enddate / 10000) + " "
				+ Integer.toString((enddate % 10000) / 100) + "."
				+ Integer.toString(enddate % 100);

		textCalendar.setText(start_date1 + "~" + end_date1);
	}

	/**
	 * @author Junki MakeTravel 여행정보 추가 AsyncTask 여행의 기본 정보들(trip_name,
	 *         start_date, end_date) DB에 등록하고 travelinfoactivity에서 추가된 친구목록을
	 *         userInTrips 테이블에 추가한다.
	 */
	public class MakeTravelTask extends AsyncTask<String, String, String> {

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

			Log.e("MakeTravelTask_nameValuePairs", nameValuePairs.toString());

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/makeTravel.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("MakeTravel_logMsg",
						"error in http connection" + e.toString());
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
				Log.e("MakeTravel_logMsg", result);
			} catch (Exception e) {
				Log.e("MakeTravel_logMsg",
						"Error converting result " + e.toString());
			}

			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;

				// get json object
				json_data = jArray.getJSONObject(0);

				// set selectedtrip information
				selectedTrip.setTripId(json_data.getInt("trip_id"));
				selectedTrip.setTriptitle(json_data.getString("trip_name"));
				selectedTrip.setStartdate(Integer.toString(json_data
						.getInt("start_date")));
				selectedTrip.setEnddate(Integer.toString(json_data
						.getInt("end_date")));
				Log.e("MakeTravel_logMsg", selectedTrip.toString());

				// change saved session
				// -1 means do not change user_id.
				session.changeUserSession(-1, selectedTrip.getTripId());
			} catch (JSONException e1) {
				Log.e("MakeTravel_logMsg", e1.toString());
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
			Log.e("MakeTravel_onPostExcute", "onPostExecute intent.. hererere");
			Log.e("MakeTravel_onPostExcute", loggedInUser.getUserId() + " "
					+ selectedTrip.getTripId());

			Log.e("MakeTravel_onPostExcute", "INSERT SUCCESS..");

			friendList.add("test");
			friendList.add("xxx");

			insertUserInTrips = new InsertUsersInTrip();
			insertUserInTrips.execute(friendList);

			// makeTravel이 종료되면 PhotoputActivity를 실행시킨다.
			Log.e("btnPut", "make Travel is finished, go to photoputactivity");
			Intent intent = new Intent(getApplicationContext(),
					PhotoputActivity.class);
			startActivity(intent);
			finish();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	/**
	 * 
	 * @author Junki InsertUsersInTrip 여행에 추가할 친구들이 포함되어있는 ArrayList를 입력으로 받아
	 *         userInTrips.php에 인자로 전달한다. userInTrips.php는 전달받은 친구 목록을
	 *         userInTrips에 등록한다.
	 * 
	 */
	public class InsertUsersInTrip extends
			AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... arg0) {
			Log.e("UserInTrips", "insert users into userInTrips");

			// get trip id from session
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map = session.getUserDetails();
			int TripId = map.get("trip_id");
			String trip_id = Integer.toString(TripId);

			// get friend nickname list
			ArrayList<String> passedFriendsList = arg0[0];

			for (int k = 0; k < passedFriendsList.size(); k++) {
				InputStream is = null;
				StringBuilder sb = null;
				String result = null;

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("nick_name",
						passedFriendsList.get(k)));
				nameValuePairs.add(new BasicNameValuePair("trip_id", trip_id));
				Log.e("trip_id", trip_id);
				Log.e("nick_name", passedFriendsList.get(k));

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
			}

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}

}
