package sgen.android.photoput;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import sgen.DTO.UserDTO;
import sgen.android.multigallery.PhotoInfo;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.sgen_pourney.AskActivity;
import sgen.sgen_pourney.CoverActivity;
import sgen.sgen_pourney.LoginActivity;
import sgen.sgen_pourney.R;
import sgen.sgen_pourney.SimpleSideDrawer;
import sgen.sgen_pourney.VideoMakingActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoputActivity extends Activity implements OnClickListener {
	static final int SELECT_PICTURE = 1;
	private TextView profileName;
	LinearLayout layoutAlbum;
	private Uri currImageURI;
	private String imagePath;
	private SimpleSideDrawer mDrawer;

	private Button askBtn, logoutBtn, albumBtn, profileBtn, makingVideo;

	private TextView popupLocation, title, date;
	private ImageButton friendList, btnProfilePhoto;
	private String storagePath = Environment.DIRECTORY_DCIM + "/pic";
	private File imgFile;
	private File storageFile;
	private Bitmap mBitmap;
	private Bitmap scaledBitmap;
	private GridLayout layoutGridPhotoAlbum;
	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	// private ImageAdapter imageAdapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	// friend list var
	private PopupWindow friendListPopupWindow;
	private GridLayout layout_friend_cell;
	// 갤러리 사용을 위한 변수 선언
	private ProgressDialog mLoagindDialog;
	private GridView gridviewPhotoAlbum;

	private int travel = 3;
	private ArrayList<DayAlbum> dayalbumList;

	// private ImageAdapter mListAdapter;
	// private ArrayList<ThumbImageInfo> mThumbImageInfoList;

	// 선택된 사진들이 몇번째 데이앨범인지
	private int i_dayalbum;

	// 프로필사진 및 로그인 불러오는 변수
	private Bitmap userProfilePhoto = null;
	

	private int photoAreaWidth;
	private int photoAreaHeight;

	private UserDTO user;
	// 사진 가져오는
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_photoput);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// user 로그인 정보 setting
		PourneyApplication loggedInUser = (PourneyApplication) getApplication();
		user = new UserDTO();
		user = loggedInUser.getLoggedInUser();
		Log.e("useruser", user.toString());

		// 드로워임
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		profileName = (TextView) findViewById(R.id.profileName);
		profileName.setText(user.getNickName());// 여기 ""안에다가 사용자 이름 넣어주세요 넣어주셔서
												// 감사합니다
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});

		btnProfilePhoto = (ImageButton) findViewById(R.id.btnForProfilePhoto);
		btnProfilePhoto.setOnClickListener(this);
		askBtn = (Button) findViewById(R.id.ask_text);
		askBtn.setOnClickListener(this);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		albumBtn.setOnClickListener(this);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		logoutBtn.setOnClickListener(this);
		makingVideo = (Button) findViewById(R.id.making_video);
		makingVideo.setOnClickListener(this);
		layoutAlbum = (LinearLayout) findViewById(R.id.layoutAlbum);
		// for (int i = 0; i < travel; i++) {
		// layoutAlbum.addView(new DayAlbum(PhotoputActivity.this));
		// }
		init();
		gridviewPhotoAlbum = (GridView) findViewById(R.id.gridviewPhotoAlbum);
		layoutGridPhotoAlbum = (GridLayout) findViewById(R.id.layoutGridPhotoAlbum);

		friendList = (ImageButton) findViewById(R.id.imgBack);
		popupLocation = (TextView) findViewById(R.id.textPeople); // 여행 사람 수
		title = (TextView) findViewById(R.id.textTitle); // 여행 제목
		date = (TextView) findViewById(R.id.textCalendar); // 여행 날짜

		popupLocation.setText("왜 너만");// 디비에서 사람 수 불러와서 넣어주세요
		title.setText("지랄이니?");// 디비에서 여행 아이디에 맞는 제목 불러와서 넣어주세요
		date.setText("달력의 스펠링은 calendar");// 디비에서 날짜 불러와서 넣어주세요

		friendList.setOnClickListener(this);

		ProfileImageSetter profileImageSetter = new ProfileImageSetter();
		profileImageSetter.execute();
	}

	private void init() {
		// 여행일정만큼 어레이리스트 생성
		dayalbumList = new ArrayList<DayAlbum>();
		for (int i = 0; i < travel; i++) {
			dayalbumList.add(new DayAlbum(PhotoputActivity.this, i));
		}
		for (int i = 0; i < travel; i++) {
			layoutAlbum.addView(dayalbumList.get(i));
		}
	}

	public void onClick(View v) {
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
			finish();
		}
		if (v.getId() == R.id.making_video) {
			Intent intent = new Intent(this, VideoMakingActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.imgBack) {

			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View popupView = layoutInflater.inflate(R.layout.friend_list_popup,
					null);
			friendListPopupWindow = new PopupWindow(popupView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			friendListPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			friendListPopupWindow.setFocusable(true);
			friendListPopupWindow.setOutsideTouchable(true);
			friendListPopupWindow.setTouchInterceptor(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						friendListPopupWindow.dismiss();
						return true;
					}
					return false;
				}
			});
			for (int i = 0; i < 7; i++) {
				((GridLayout) friendListPopupWindow.getContentView()
						.findViewById(R.id.friendlistpopupback))
						.addView(new FriendListCell(this));
				friendListPopupWindow.showAsDropDown(popupLocation, -475, 27);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d("PhotoputActivity", "onActivityResult");
		Log.d("PhotoputActivity", resultCode + "");

		if (resultCode == RESULT_OK) {
			if (requestCode == 200) {
				// 선택된 데이앨범의 i값을 받아온다
				i_dayalbum = data.getIntExtra("i_dayalbum", 300);
				Log.d("Photoput : i_dayalbum ", i_dayalbum + "");
				// 사진 패스를 받아옴
				ArrayList<PhotoInfo> all_path = (ArrayList<PhotoInfo>) data
						.getExtras().getSerializable("list");

				for (int i = 0; i < all_path.size(); i++) {
					// 받아온 패스로 파일 만들어서 레이아웃 그리드 앨범에 추가한다.
					all_path.get(i)
							.setFile(new File(all_path.get(i).getPath()));
					dayalbumList.get(i_dayalbum).addLayoutGridalbum(
							new AlbumImgCell(PhotoputActivity.this, all_path
									.get(i).getFile()));
				}
				// 저 for문에서 이미지 뷰에 추가하고 파일 생성하니까 아마 이부분에서 서버 코드 추가하면 될꺼에요~

			}
		}
	}

	private String getRealPathFromURI(Uri selectedVideoUri,
			ContentResolver contentResolver)
			throws UnsupportedEncodingException {
		String filePath;
		String[] filePathColumn = { MediaStore.MediaColumns.DATA };

		Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn,
				null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		filePath = cursor.getString(columnIndex);
		cursor.close();
		String result = java.net.URLDecoder.decode(filePath, "UTF-8");
		return result;
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
