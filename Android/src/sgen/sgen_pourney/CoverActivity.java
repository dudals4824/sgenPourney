package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import sgen.DTO.UserDTO;
import sgen.android.photoput.PhotoputActivity;
import sgen.application.PourneyApplication;
import sgen.common.ListViewDialog;
import sgen.common.PhotoEditor;
import sgen.common.ProfileUploader;
import sgen.common.ListViewDialog.ListViewDialogSelectListener;
import sgen.session.UserSessionManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CoverActivity extends Activity implements OnClickListener {
	private int numberOfCover = 0; // 디비에서 개인의 커버 갯수 받아와서 저장해주세요
	private TextView profileName;
	private GridLayout layout_cover;
	private ImageButton btn_new_travel, btnProfilePhoto, albumCover;
	private SimpleSideDrawer mDrawer;
	private Button askBtn, logoutBtn, albumBtn, profileBtn;
	private long m_startTime;
	private long m_endTime;
	private boolean m_isPressedBackButton;
	private Bitmap userProfilePhoto = null;

	static final int REQUEST_ALBUM = 1;
	static final int REQUEST_PICTURE = 2;

	static String SAMPLEIMG = "profile.png";

	private Context context;

	private int photoAreaWidth;
	private int photoAreaHeight;

	UserSessionManager session;
	private UserDTO user;

	// trip id 저장용 array list
	private ArrayList<Integer> tripArray = new ArrayList<Integer>();

	// CoverCell marble=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_cover);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		// marble=(Cover_cell)findViewById(R.id.box1);

		// user 로그인 정보 setting
		PourneyApplication loggedInUser = (PourneyApplication) getApplication();
		user = new UserDTO();
		user = loggedInUser.getLoggedInUser();
		//Log.e("useruser", user.toString());

		// logout을 위한 session 정보 settting
		session = new UserSessionManager(getApplicationContext());

		// 커버 갯수 가져오기
		GetTripCount getTripCount = new GetTripCount();
		getTripCount.start();
		try {
			getTripCount.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 여기부터 drawer
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		profileName = (TextView) findViewById(R.id.profileName);
		profileName.setText(user.getNickName());// 여기 "" 안에다가 사용자 이름 넣어주세요 넣어주셔서
												// 감사합니다

		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();
			}
		});

		m_startTime = System.currentTimeMillis();

		Log.e("log_msg", "num of trip : " + numberOfCover);

		// cover 셋팅
		layout_cover = (GridLayout) findViewById(R.id.layout_cover);
		Log.e("cover activity", "trip array size : " + tripArray.size());
		if (tripArray.size() > 0) {
			for (int i = 0; i < numberOfCover; i++) {// 커버 갯수만큼 나타나게 해주는 거임
				layout_cover.addView(new CoverCell(this, tripArray.get(i)));
			}
			// albumCover = (ImageButton) findViewById(R.id.backcard);
			// albumCover.setOnClickListener(this);
		}
		// 맨뒤에 생길 추가용 cover
		layout_cover.addView(new CoverCellNew(this));

		// layout_cover_new = (GridLayout)findViewById(R.id.layout_cover_new);
		// layout_cover_new.addView(new Cover_cell_new(this));

		btnProfilePhoto = (ImageButton) findViewById(R.id.btnForProfilePhoto);
		btn_new_travel = (ImageButton) findViewById(R.id.backcardNew);
		askBtn = (Button) findViewById(R.id.ask_text);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);

		btnProfilePhoto.setOnClickListener(this);
		btn_new_travel.setOnClickListener(this);
		askBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);

		// 프로필이미지 셋팅
		ProfileImageSetter profileImageSetter = new ProfileImageSetter();
		profileImageSetter.execute();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnForProfilePhoto) {

		} else if (v.getId() == R.id.backcardNew) {
			Intent intent = new Intent(CoverActivity.this,
					TravelInfoActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.log_out_text) {
			Log.d("logout", "logout");
			session.logoutUser();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			// intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		} else if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
			finish();
		} else if (v.getId() == R.id.cphoto) {
			System.out.println("클릭됨요");
		} else if (v.getId() == R.id.backcard) {
			Intent intent = new Intent(CoverActivity.this,
					PhotoputActivity.class);
			startActivity(intent);
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

	public class ProfileImageSetter extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			userProfilePhoto = PhotoEditor.ImageurlToBitmapConverter(user
					.getProfileFilePath());
			if (userProfilePhoto != null) {
				BitmapDrawable bd = (BitmapDrawable) getResources()
						.getDrawable(R.drawable.i_profilephoto_cover);
				Bitmap coverBitmap = bd.getBitmap();
				photoAreaWidth = bd.getBitmap().getWidth();
				photoAreaHeight = bd.getBitmap().getHeight();
				PhotoEditor photoEdit = new PhotoEditor(userProfilePhoto,
						coverBitmap, photoAreaWidth, photoAreaHeight);
				userProfilePhoto = photoEdit.editPhotoAuto();
				btnProfilePhoto.setImageBitmap(userProfilePhoto);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

	public class GetTripCount extends Thread {
		@Override
		public void run() {
			super.run();

			Log.e("log_GetTripCount", "get trip count thread start");

			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", String
					.valueOf(user.getUserId())));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/getCoverCount.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "error in http connection" + e.toString());
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");
				String line = "0";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString().trim();
				Log.e("log_tag", result);

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			try {
				JSONObject JsonObject = new JSONObject(result);
				numberOfCover = JsonObject.getInt("tripCount");
				for (int i = 0; i < JsonObject.length() - 1; i++) {
					tripArray.add(JsonObject.getJSONObject(String.valueOf(i))
							.getInt("trip_id"));
				}
				Log.e("log_GetTripCount", "get trip count thread end");
			} catch (JSONException e1) {
				Log.e("log_msg", e1.toString());
			}
		}
	}

	// public void requestAlbum() {
	// // TODO Auto-generated method stub
	// Intent intent = new Intent();
	// intent.setType("image/*");
	// intent.setAction(Intent.ACTION_GET_CONTENT);
	// // startActivityForResult(
	// // Intent.createChooser(intent, "Select Picture"),
	// // REQUEST_ALBUM);
	// }
	//
	// public void requestPicture() {
	// // TODO Auto-generated method stub
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// File file = new File(Environment
	// .getExternalStorageDirectory(), SAMPLEIMG);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
	// startActivityForResult(intent, REQUEST_PICTURE);
	// }

}
