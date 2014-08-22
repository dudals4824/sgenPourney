package sgen.sgen_pourney;

import sgen.DTO.UserDTO;
import sgen.common.PhotoEditor;
import sgen.session.UserSessionManager;
import sgen.sgen_pourney.CoverActivity.ProfileImageSetter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CoverSelection extends Activity implements OnCheckedChangeListener, OnClickListener{

	private RadioGroup coverRadioGroup;
	private ImageView imgviewCover;
	private SimpleSideDrawer mDrawer;
	private Button askBtn, logoutBtn, albumBtn, profileBtn;
	UserSessionManager session;
	private int photoAreaWidth;
	private int photoAreaHeight;
	private Bitmap userProfilePhoto = null;
	private ImageButton  btnProfilePhoto;
	private UserDTO user;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.album_cover_select);
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
		btnProfilePhoto=(ImageButton)findViewById(R.id.btnForProfilePhoto);
		imgviewCover=(ImageView)findViewById(R.id.imgviewCover);
		coverRadioGroup = (RadioGroup) findViewById(R.id.cover_select_radioGrp);
		coverRadioGroup.setOnCheckedChangeListener(this);
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
		int coverType=-1;
		switch (arg1) {
		case R.id.backcard1_radioBtn:
			coverType = 0;
			Log.d("backcard1", "1");
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

}
