package sgen.sgen_pourney;

import sgen.DTO.UserDTO;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.sgen_pourney.CoverActivity.ProfileImageSetter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileModi extends Activity implements OnClickListener {
	private SimpleSideDrawer mDrawer;
	private TextView profileName;

	private UserDTO user;
	private Bitmap userProfilePhoto = null;

	private ImageButton btnProfilePhoto;
	private Button askBtn, logoutBtn, albumBtn, profileBtn;
	private int photoAreaWidth;
	private int photoAreaHeight;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_profile);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// user 로그인 정보 setting
		PourneyApplication loggedInUser = (PourneyApplication) getApplication();
		user = new UserDTO();
		user = loggedInUser.getLoggedInUser();
		Log.e("useruser", user.toString());
		// drawer
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		profileName = (TextView) findViewById(R.id.profileName);
		profileName.setText(user.getNickName());// 여기 ""안에다가 사용자 이름 넣어주세요
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});

		btnProfilePhoto = (ImageButton) findViewById(R.id.btnForProfilePhoto);

		askBtn = (Button) findViewById(R.id.ask_text);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);

		btnProfilePhoto.setOnClickListener(this);

		askBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);

		ProfileImageSetter profileImageSetter = new ProfileImageSetter();
		profileImageSetter.execute();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backcardNew) {
			Intent intent = new Intent(ProfileModi.this,
					TravelInfoActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.ask_text) {
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
		}
		if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

}
