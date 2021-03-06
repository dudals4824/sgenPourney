package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

import sgen.DTO.UserDTO;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.session.UserSessionManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

//페북로긴에 필요함

public class LoginActivity extends Activity implements OnClickListener {

	private long m_startTime;
	private long m_endTime;
	private boolean m_isPressedBackButton;
	private ImageView logo;
	private ImageButton btnLogin;
	private ImageButton btnFacebook, btnJoin;
	private EditText editEmailaddress, editPassword;
	private boolean isLoginSuccessful;
	private UserDTO loggedInUser;

	Drawable sPhoto;
	private Bitmap mIcon;
	private String userId;
	private String email;
	private String nickName;
	private final String POURNEY_URL = "http://54.178.166.213";

	// User Session Manager Class
	UserSessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, SplashActivity.class));
		setContentView(R.layout.activity_login);
		init();

	}

	private void init() {
		Log.e("log_msg", "Initializing Layout...");

		logo = (ImageView) findViewById(R.id.imgLogoimage);
		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		btnFacebook = (ImageButton) findViewById(R.id.btnFacebook);
		btnJoin = (ImageButton) findViewById(R.id.btnJoin);
		editEmailaddress = (EditText) findViewById(R.id.editEmailaddress);
		editPassword = (EditText) findViewById(R.id.editPwd);
		btnLogin.setOnClickListener(this);
		btnFacebook.setOnClickListener(this);
		btnJoin.setOnClickListener(this);

		isLoginSuccessful = false;

		// UserDTO init
		loggedInUser = new UserDTO();

		// User Session Manager
		session = new UserSessionManager(getApplicationContext());

		// 자동 로그인
		if (session.isUserLoggedIn()) {
			Log.d("Login Activity", "auto login");
			LoginTask loginTask = new LoginTask();
			loginTask.execute(
					Integer.toString(session.getUserDetails().get("user_id")),
					"1");
		}
		Log.e("log_msg", "Initializing done...");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnLogin) {

			String emailAddress, password;
			emailAddress = editEmailaddress.getText().toString();
			password = editPassword.getText().toString();
			isLoginSuccessful = false;
			Log.e("log_msg", "Email : " + emailAddress + ", Password: "
					+ password + "/");

			if (emailAddress.trim().length() > 0
					&& password.trim().length() > 0) {

				LoginTask loginTask = new LoginTask();
				loginTask.execute(emailAddress.trim(), password.trim());
			} else {
				// user didn't entered username or password
				Toast.makeText(getApplicationContext(),
						"Please enter username and password", Toast.LENGTH_LONG)
						.show();
			}
		} // else if (v.getId() == R.id.editEmailaddress) {
			// editEmailaddress
			// .setBackgroundResource(R.drawable.i_emailaddress_put);
			// }
		else if (v.getId() == R.id.editPwd) {
			v.setOnTouchListener(new OnTouchListener() { // 버튼 터치시 이벤트
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) // 버튼을 누르고
																		// 있을 때
						editPassword
								.setBackgroundResource(R.drawable.i_password_put);
					if (event.getAction() == MotionEvent.ACTION_UP) { // 버튼에서 손을
																		// 떼었을 때
						editPassword
								.setBackgroundResource(R.drawable.i_password);
					}
					return false;
				}
			});
			// editPassword
			// .setBackgroundResource(R.drawable.i_password_put);
		} else if (v.getId() == R.id.btnFacebook) {
			facebookLogin();// 페북 로그인
		} else if (v.getId() == R.id.btnJoin) {
			System.out.println("Join");
			Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
			startActivity(intent);
		}
	}

	private void facebookLogin() { // facebook login 占쏙옙 id

		List<String> permissions = new ArrayList<String>();
		permissions.add("email");
		permissions.add("public_profile");
		Session.openActiveSession(this, true, permissions,
				new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						if (session.isOpened()) {
							// make request to the /me API
							Request.newMeRequest(session,
									new Request.GraphUserCallback() {

										// callback after Graph API response
										// with
										// user object
										@Override
										public void onCompleted(GraphUser user,
												Response response) {
											if (user != null) {
												nickName = user.getFirstName();
												// System.out.println(nickName);

												String id = user.getId();
												userId = id;
												// System.out.println(userId);
												email = user.getProperty(
														"email").toString();
												// System.out.println(email);
												new BackTask().execute(mIcon);

												// finish();
												// Intent intent = new
												// Intent(
												// LoginActivity.this,
												// CoverActivity.class);
												// startActivity(intent);
												// //액티비티넘기는거
											}
										}
									}).executeAsync();
						}
					}
				});

	}

	public class BackTask extends AsyncTask<Bitmap, String, String> {

		@Override
		protected String doInBackground(Bitmap... args) {
			mIcon = args[0];
			// ImageView icon_pic = (ImageView) findViewById(R.id.imgLogoimage);

			String img_value = "https://graph.facebook.com/" + userId
					+ "/picture?type=large";

			System.out.println(img_value);

			mIcon = PhotoEditor.ImageurlToBitmapConverter(img_value);
			// icon_pic.setImageBitmap(mIcon);

			sPhoto = new BitmapDrawable(getResources(), mIcon);// 페북 메일이랑 닉넴이랑
																// 프사
			System.out.println(email); // mIcon 이 bitmap사진임
			System.out.println(nickName);

			return null;
		}

		@Override
		protected void onPostExecute(String text) {
			// logo.setBackground(sPhoto);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	/*
	 * 김준기 login btn click시 호출되는 스레드 서버에 email, password 보내서 정보 요청함.
	 */
	public class LoginTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			Log.d("LoginTask", "email : " + params[0] + "    password : "
					+ params[1]);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email", params[0]));
			nameValuePairs.add(new BasicNameValuePair("password", params[1]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/login.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"utf-8"));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Log.e("log_tag", "error in http connection" + e.toString());
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");
				String line = "0";

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				is.close();
				result = sb.toString().trim();
				Log.e("result123", result);

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			try {

				if (result.equals("wrong password")) {
					// id인증 실패
					Log.e("log_msg", "wrong password");
				}
				// id 인증 성공
				else {
					Log.e("log_msg", "right password");
					// login 상태 성공으로 변경 activity에서 세션 저장용으로 사용.
					isLoginSuccessful = true;
					JSONArray jArray = new JSONArray(result);
					// id 값 없을 경우
					// wrong password 받으면서 json error난다.
					// 맞게하면 에러 안남.
					JSONObject json_data = null;
					json_data = jArray.getJSONObject(0);
					loggedInUser.setUserId(json_data.getInt("user_id"));
					loggedInUser.setNickName(json_data.getString("nick_name"));
					loggedInUser.setEmail(json_data.getString("email"));
					loggedInUser.setProfileFilePath(json_data
							.getString("profile_filename"));
					// 완성된 url 형태로 loggedin user에 저장.

					// 유저 정보 전역 객체에 추가
					loggedInUser.setProfileFilePath(POURNEY_URL
							+ loggedInUser.getProfileFilePath());
					PourneyApplication UserInfo = (PourneyApplication) getApplication();
					UserInfo.setLoggedInUser(loggedInUser);
					Log.e("user information", loggedInUser.toString());
				}
			} catch (JSONException e1) {
				Log.e("log_msg", e1.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// if(서버 로그인 성공이면)
			if (isLoginSuccessful) {
				Log.e("log_msg", "onPostExecute intent..");
				int TripId = 0;
				session.createUserLoginSession(loggedInUser.getUserId(), TripId);

				// Starting MainActivity
				Intent intent = new Intent(getApplicationContext(),
						CoverActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// Add new Flag to start new Activity
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
				Log.e("log_msg", "logged in..");
			} else {
				// username / password doesn't match&
				Toast.makeText(getApplicationContext(),
						"Username/Password is incorrect", Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

	public void onBackPressed() {
		m_endTime = System.currentTimeMillis();

		if (m_endTime - m_startTime > 2000)
			m_isPressedBackButton = false;

		if (m_isPressedBackButton == false) {
			m_isPressedBackButton = true;

			m_startTime = System.currentTimeMillis();

			Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
					.show();
		} else {
			finish();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	public static Bitmap ImageurlToBitmapConverter(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
