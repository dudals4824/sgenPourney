package sgen.sgen_pourney;

import sgen.DTO.UserDTO;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

	int numberOfCover = 3; // 디비에서 개인의 커버 갯수 받아와서 저장해주세요
	private TextView title, date, people;
	private TextView profileName;
	private GridLayout layout_cover;
	private ImageButton btn_new_travel, btnProfilePhoto, albumCover;
	private SimpleSideDrawer mDrawer;
	private Button askBtn, logoutBtn, albumBtn, profileBtn;
	long m_startTime;
	long m_endTime;
	boolean m_isPressedBackButton;
	private Bitmap userProfilePhoto = null;

	private int photoAreaWidth;
	private int photoAreaHeight;

	private UserDTO user;

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
		Log.e("useruser", user.toString());

		// 여기부터 drawer
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		profileName = (TextView) findViewById(R.id.profileName);
		profileName.setText(user.getNickName());// 여기 ""안에다가 사용자 이름 넣어주세요 넣어주셔서 감사합니다

		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});

		m_startTime = System.currentTimeMillis();
		layout_cover = (GridLayout) findViewById(R.id.layout_cover);
		for (int i = 1; i < numberOfCover; i++) {// 커버 갯수만큼 나타나게 해주는 거임
			layout_cover.addView(new CoverCell(this,i));
			
			// date = (TextView)findViewById(R.id.dayBack);
			// date.setText("");
			// people = (TextView)findViewById(R.id.peopleBack);
			// people.setText("");
		}
		// 맨뒤에 생길거
		layout_cover.addView(new CoverCellNew(this));
		// layout_cover_new = (GridLayout)findViewById(R.id.layout_cover_new);
		// layout_cover_new.addView(new Cover_cell_new(this));
		// 椰꾬옙域밸챶�곻옙遺쎄탢占쏙옙筌〓㈇�э옙占퐉able

		btnProfilePhoto = (ImageButton) findViewById(R.id.btnForProfilePhoto);
		btn_new_travel = (ImageButton) findViewById(R.id.backcardNew);
		askBtn = (Button) findViewById(R.id.ask_text);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);
	//	albumCover = (ImageButton) findViewById(R.id.cphoto);
		
		btnProfilePhoto.setOnClickListener(this);
		btn_new_travel.setOnClickListener(this);
		askBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);
	//	albumCover.setOnClickListener(this);
		
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
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		} else if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
		} else if (v.getId() == R.id.cphoto) {
			System.out.println("클릭됨요");

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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}
}
