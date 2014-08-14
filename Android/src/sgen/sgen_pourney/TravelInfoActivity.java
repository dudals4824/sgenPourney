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
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.session.UserSessionManager;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TravelInfoActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnFocusChangeListener, OnDismissListener {
	private ExpandableHeightGridView gridCalendar, gridDate;
	private TextView textTitle, textCalendar, textTitleHere, textCalendarHere,
			textPeopleHere, textInputInfo, textMonth, name;
	private Button askBtn, logoutBtn, albumBtn, profileBtn;
	private ImageButton btnPrevMonth, btnNextMonth, btnPut, btnMakeVideo, btnInputPhoto;
	private ArrayList<ImageButton> btnFriend = new ArrayList<ImageButton>();
	private EditText editTitle, peopleName;
	private Dayinfo today;
	private int flagselectdate = 0;
	private SimpleSideDrawer mDrawer;
	private UserDTO loggedInUser;
	private TripDTO selectedTrip; // 민아
	private String frinedNameToFind;

	
	// 사진 관련 변수
	private int photoAreaWidth;
	private int photoAreaHeight;

	// 친구찾기에서 사용할 popup관련 변수들
	private PopupWindow findFriendPopupWindow;
	private PopupWindow FriendListPopupWindow;
	private ImageView friendProfileOnPopupWindow;

	// array list for add friend junki
	private ArrayList<String> friendList = new ArrayList<String>();

	private int startdate = 0;
	private int enddate = 0;
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

	// 친구 목록용 변수
	private Bitmap friendProfilePhoto = null;
	int[] ids = { R.id.btnFriend1, R.id.btnFriend2, R.id.btnFriend3,
			R.id.btnFriend4, R.id.btnFriend5, R.id.btnFriend6, R.id.btnFriend7 };
	private int lastFriendButtonIndex = 0;

	// 상수
	private final String POURNEY_URL = "http://54.178.166.213";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		selectedTrip = new TripDTO();
		loggedInUser = new UserDTO();

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_travel_info);
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
		askBtn = (Button) findViewById(R.id.ask_text);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		albumBtn = (Button) findViewById(R.id.last_album_text);
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
		btnInputPhoto=(ImageButton)findViewById(R.id.btnInputPhoto);
		btnMakeVideo=(ImageButton)findViewById(R.id.btnMakeVideo);
		

		for (int i = 0; i < 7; i++) {
			Log.e("numbertest", "" + i);
			btnFriend.add((ImageButton) findViewById(ids[i]));
		}
		for (int i = 0; i < 7; i++) {
			Log.e("numbertest", "" + i);
			btnFriend.get(i).setOnClickListener(this);
		}
		// last friend button에 0번 버튼 지정
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
		btnInputPhoto.setOnClickListener(this);
		btnMakeVideo.setOnClickListener(this);
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
		Typeface yoon320 = Typeface.createFromAsset(getAssets(), "fonts/yoon320.ttf");
		textTitleHere.setTypeface(yoon320);
		textCalendarHere.setTypeface(yoon320);
		textPeopleHere.setTypeface(yoon320);
		Typeface yoon330 = Typeface.createFromAsset(getAssets(), "fonts/yoon330.ttf");
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
		if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		if(v.getId()==R.id.btnInputPhoto){
			Intent intent = new Intent(TravelInfoActivity.this, PhotoputActivity.class);
			startActivity(intent);
			finish();
		}
		
		if(v.getId()==R.id.btnMakeVideo){
			Intent intent = new Intent(TravelInfoActivity.this, VideoMakingActivity.class);
			startActivity(intent);
			finish();
		}
		if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
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
		} else if ((v.getId() == R.id.btnFriend1)
				|| (v.getId() == R.id.btnFriend2)
				|| (v.getId() == R.id.btnFriend3)
				|| (v.getId() == R.id.btnFriend4)
				|| (v.getId() == R.id.btnFriend5)
				|| (v.getId() == R.id.btnFriend6)
				|| (v.getId() == R.id.btnFriend7)) {// 친구찾기 검색창 부분
			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View findFriendPopupView = layoutInflater.inflate(
					R.layout.find_friend_popup, null);
			findFriendPopupWindow = new PopupWindow(findFriendPopupView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			findFriendPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			findFriendPopupWindow.setFocusable(true);
			findFriendPopupWindow.setOutsideTouchable(true);
			findFriendPopupWindow.setTouchInterceptor(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						findFriendPopupWindow.dismiss();
						return true;
					}
					return false;
				}
			});
			findFriendPopupWindow.setOnDismissListener(this);
			ImageButton btnDismiss = (ImageButton) findFriendPopupView
					.findViewById(R.id.cancel);
			ImageButton findfriend = (ImageButton) findFriendPopupView
					.findViewById(R.id.findfriend);
			btnDismiss.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (v.getId() == R.id.cancel) {
						findFriendPopupWindow.dismiss();
					} else
						;
				}

			});

			// 친구 찾기 버튼
			findfriend.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v.getId() == R.id.findfriend) {
						View contentView = findFriendPopupWindow
								.getContentView();
						peopleName = (EditText) contentView
								.findViewById(R.id.peopleName);
						frinedNameToFind = peopleName.getText().toString();
						// 친구찾기 task 수행.
						GetFriendList getFriendList = new GetFriendList();
						getFriendList.execute(frinedNameToFind);
					}
				}
			});

			findFriendPopupWindow.showAsDropDown(textCalendarHere, -150, 50);

		} else if (v.getId() == R.id.btnFriend2) {

		} else if (v.getId() == R.id.btnFriend3) {

		}

		else if (v.getId() == R.id.btnPut) {
			String trip_name = editTitle.getText().toString();
			String start_date = Integer.toString(startdate);
			String end_date = Integer.toString(enddate);
			
			String str_startdate = Integer.toString(startdate);
			int year;
			int month;
			int day;
			Log.d("GregorianCalendar", Integer.toString(startdate)+"");
			year = Integer.parseInt(str_startdate.substring(0, 4));
			month = Integer.parseInt(str_startdate.substring(4, 6));
			day = Integer.parseInt(str_startdate.substring(6));

			GregorianCalendar gregorianCalendar = new GregorianCalendar(year,
					month, day);
			Log.d("GregorianCalendar", gregorianCalendar+"");

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
				selectedTrip.setTripTitle(json_data.getString("trip_name"));
				selectedTrip.setStartDate(json_data.getInt("start_date"));
				selectedTrip.setEndDate(json_data.getInt("end_date"));
				
				PourneyApplication Application = (PourneyApplication)getApplication();
				Application.setSelectedTrip(selectedTrip);
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

	/**
	 * @author Junki 친구 목록 불러오는 asynctask input : nickname , output : friend
	 *         user 객체
	 */
	public class GetFriendList extends AsyncTask<String, String, String> {
		private UserDTO friend;
		private boolean isFoundFriend = false;

		@Override
		protected String doInBackground(String... params) {
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;
			friend = new UserDTO();

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("nick_name", params[0]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/findFriends.php");
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
				Log.e("log_tag", "get friend list result : " + result);

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;
				json_data = jArray.getJSONObject(0);
				isFoundFriend = "1".equals(json_data.getString("isSuccess"));
				if (isFoundFriend) {
					friend.setUserId(json_data.getInt("user_id"));
					friend.setNickName(json_data.getString("nick_name"));
					friend.setEmail(json_data.getString("email"));
					friend.setProfileFilePath(POURNEY_URL
							+ json_data.getString("profile_filename"));
					Log.e("log_msg",
							"friend information = " + friend.toString());
				} else {
					Log.e("log_msg", "no such friend");
				}
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
			// 친구 찾으면 팝업 띄움
			if (isFoundFriend) {
				LayoutInflater layoutInflater1 = (LayoutInflater) getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View foundFriendPopupView = layoutInflater1.inflate(
						R.layout.find_friend_success, null);

				friendProfileOnPopupWindow = (ImageView) foundFriendPopupView
						.findViewById(R.id.friendProfile);

				FriendListPopupWindow = new PopupWindow(foundFriendPopupView,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						true);
				FriendListPopupWindow
						.setBackgroundDrawable(new BitmapDrawable());
				FriendListPopupWindow.setFocusable(true);
				FriendListPopupWindow.setOutsideTouchable(true);
				FriendListPopupWindow
						.setTouchInterceptor(new OnTouchListener() {

							public boolean onTouch(View v, MotionEvent event) {
								if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
									FriendListPopupWindow.dismiss();
									return true;
								}
								return false;
							}
						});

				// 취소 버튼
				ImageButton btnDismiss = (ImageButton) foundFriendPopupView
						.findViewById(R.id.cancel);
				btnDismiss
						.setOnClickListener(new ImageButton.OnClickListener() {
							@Override
							public void onClick(View v) {
								FriendListPopupWindow.dismiss();
							}
						});

				// 확인버튼
				ImageButton confirm = (ImageButton) foundFriendPopupView
						.findViewById(R.id.confirm);
				confirm.setOnClickListener(new ImageButton.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 친구 중복 추가 방지를 위한 friend list 검사
						boolean isFriendDuplicated = false;
						for (int i = 0; i < friendList.size(); i++)
							if (isFriendDuplicated = friend.getNickName()
									.equals(friendList.get(i)))
								break;

						// 중복 추가 검사
						if (isFriendDuplicated) {
							Toast.makeText(getApplicationContext(),
									"이미 추가된 친구입니다.", Toast.LENGTH_SHORT).show();
						}
						// 7명 초과 검사 및 친구 추가. friend list에 추가 후 비어있는 friend
						// profile을 사진으로 바꿔준다
						else if (lastFriendButtonIndex < 7) {
							friendList.add(friend.getNickName());
							FriendListPopupWindow.dismiss();
							Toast.makeText(getApplicationContext(),
									friend.getNickName() + "가 친구목록에 추가되었습니다.",
									Toast.LENGTH_SHORT).show();

							FriendProfileImageSetter imageSetter = new FriendProfileImageSetter();
							// 친구 추가할때마다 button indicator 한개씩 증가시켜줌. 7번까지만
							Log.e("log_msg", "button number : "
									+ lastFriendButtonIndex);
							imageSetter.execute(friend,
									btnFriend.get(lastFriendButtonIndex++));
						} else {
							Toast.makeText(getApplicationContext(),
									"친구 목록이 이미 꽉 찼습니다.", Toast.LENGTH_SHORT)
									.show();
						}

					}
				});

				((TextView) FriendListPopupWindow.getContentView()
						.findViewById(R.id.foundFriend))
						.setText(frinedNameToFind);
				FriendListPopupWindow
						.showAsDropDown(textCalendarHere, -150, 50);
				isFoundFriend = false;

				// 친구 찾은 화면
				FriendProfileImageSetter imageSetter = new FriendProfileImageSetter();
				imageSetter.execute(friend, friendProfileOnPopupWindow);
			}// if
			else {
				// 친구 못찾은 toast 띄우기
				Toast.makeText(getApplicationContext(), "검색된 친구가 없어요!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 친구찾기 Popup dismiss 됐을 시 호출.
	 */
	@Override
	public void onDismiss() {
		Log.e("msg_log", "popup friend windows dismissed");
	}

	/**
	 * @author Junki param[0] : 찾은 친구의 userDTO, param[1] : image를 setting할
	 *         imageView 객체 두개를 각각 전달받아 친구의 프로필 사진을 param[1]의 imageView에
	 *         setBitmap한다.
	 */
	public class FriendProfileImageSetter extends
			AsyncTask<Object, String, String> {

		UserDTO foundFriend = new UserDTO();
		ImageView targetImageView = null;

		@Override
		protected String doInBackground(Object... params) {
			// parameter converting to original object
			foundFriend = (UserDTO) params[0];
			targetImageView = (ImageView) params[1];

			// image view setting
			Log.e("log_msg", "friend profile ->>>" + foundFriend.toString());
			friendProfilePhoto = PhotoEditor
					.ImageurlToBitmapConverter(foundFriend.getProfileFilePath());
			if (friendProfilePhoto != null) {
				// profile 사진 크기에 맞게 cover bitmap 설정
				BitmapDrawable bd = null;
				if (targetImageView.equals(friendProfileOnPopupWindow)) {
					bd = (BitmapDrawable) getResources().getDrawable(
							R.drawable.i_findfriend_profile_cover);
				} else {
					bd = (BitmapDrawable) getResources().getDrawable(
							R.drawable.i_profile_200x200_cover);
				}
				Bitmap coverBitmap = bd.getBitmap();

				photoAreaWidth = targetImageView.getWidth();
				photoAreaHeight = targetImageView.getHeight();
				PhotoEditor photoEdit = new PhotoEditor(friendProfilePhoto,
						coverBitmap, photoAreaWidth, photoAreaHeight);
				friendProfilePhoto = photoEdit.editPhotoAuto();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 친구 찾은 화면일 경우
			targetImageView.setImageBitmap(friendProfilePhoto);
		}

	}
}
