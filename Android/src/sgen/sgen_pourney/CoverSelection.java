package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.session.UserSessionManager;
import sgen.sgen_pourney.CoverActivity.ProfileImageSetter;
import android.app.Activity;

import android.content.Intent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.View;

import android.view.Window;

import android.widget.ImageButton;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CoverSelection extends Activity implements
		OnCheckedChangeListener, OnClickListener {

	private RadioGroup coverRadioGroup;
	private ImageView imgviewCover;

	private ImageButton btnSelectCover;
	private int coverType = -1;
	private int intent_cover;
	private int tripId;

	private SimpleSideDrawer mDrawer;
	private Button askBtn, logoutBtn, albumBtn, profileBtn;
	UserSessionManager session;
	private int photoAreaWidth;
	private int photoAreaHeight;
	private Bitmap userProfilePhoto = null;
	private ImageButton btnProfilePhoto;
	private UserDTO user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.album_cover_select);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		intent_cover = getIntent().getIntExtra("intent_cover", 300);
		tripId = getIntent().getIntExtra("tripId", 300);

		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});
		PourneyApplication Application = (PourneyApplication) getApplication();
		user = Application.getLoggedInUser();
		askBtn = (Button) findViewById(R.id.ask_text);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);
		btnProfilePhoto = (ImageButton) findViewById(R.id.btnForProfilePhoto);

		imgviewCover = (ImageView) findViewById(R.id.imgviewCover);
		btnSelectCover = (ImageButton) findViewById(R.id.btnSelectCover);
		coverRadioGroup = (RadioGroup) findViewById(R.id.cover_select_radioGrp);
		coverRadioGroup.setOnCheckedChangeListener(this);

		btnSelectCover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("커버 셀렉션 intent_cover", intent_cover + "");
				Intent intent = new Intent(CoverSelection.this,
						CoverActivity.class);
				intent.putExtra("coverType", coverType);
				intent.putExtra("intent_cover", intent_cover);
				Log.d("cover type", "                           "+coverType);
				
				ChangeCoverImage changeCoverImage = new ChangeCoverImage();
				changeCoverImage.execute(Integer.toString(tripId), Integer.toString(coverType));
				try {
					changeCoverImage.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				startActivity(intent);
				finish();
			}
		});

		session = new UserSessionManager(getApplicationContext());

		askBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);

		ProfileImageSetter profileImageSetter = new ProfileImageSetter();
		profileImageSetter.execute();

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		Log.d("checked", "ddd");

		switch (arg1) {
		case R.id.backcard1_radioBtn:
			coverType = 0;
			imgviewCover.setImageResource(R.drawable.i_backcard_1);
			break;
		case R.id.backcard2_radioBtn:
			coverType = 1;
			imgviewCover.setImageResource(R.drawable.i_backcard_2);
			break;
		case R.id.backcard3_radioBtn:
			coverType = 2;
			imgviewCover.setImageResource(R.drawable.i_backcard_3);
			break;
		case R.id.backcard4_radioBtn:
			coverType = 3;
			imgviewCover.setImageResource(R.drawable.i_backcard_4);
			break;
		default:
			coverType = -1;
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			session.logoutUser();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
			finish();
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			btnProfilePhoto.setImageBitmap(userProfilePhoto);
		}
	}

	public class ChangeCoverImage extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", params[0]));
			nameValuePairs.add(new BasicNameValuePair("cover_type", params[1]));
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/changeCoverImage.php");
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
				Log.e("log_tag", result);

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			try {
				JSONObject JsonObject = new JSONObject(result);
				result = JsonObject.getString("result");
			} catch (JSONException e1) {
				Log.e("log_msg", e1.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

}
