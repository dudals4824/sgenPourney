package sgen.android.photoput;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

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

import sgen.DTO.PhotoDTO;
import sgen.DTO.TripDTO;
import sgen.DTO.UserDTO;
import sgen.android.multigallery.PhotoInfo;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.common.PhotoUploader;
import sgen.image.resizer.ImageResizer;
import sgen.image.resizer.ResizeMode;
import sgen.session.UserSessionManager;
import sgen.sgen_pourney.AskActivity;
import sgen.sgen_pourney.CoverActivity;
import sgen.sgen_pourney.LoginActivity;
import sgen.sgen_pourney.ProfileModi;
import sgen.sgen_pourney.R;
import sgen.sgen_pourney.SimpleSideDrawer;
import sgen.sgen_pourney.VideoMakingActivity;
import sgen.sgen_pourney.VideoViewActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoputActivity extends Activity implements OnClickListener {
	static final int SELECT_PICTURE = 1;
	private TextView profileName;
	LinearLayout layoutAlbum;
	private Uri currImageURI;
	private String imagePath;
	private SimpleSideDrawer mDrawer;

	private int checkedNum = 0;
	private CheckBox imgCheckBox;
	private ImageButton btnForTest;

	private PopupWindow memoPopupWindow;
	private Button askBtn, logoutBtn, albumBtn, profileBtn;

	private TextView popupLocation, title, date, numberOfPeople;
	private ImageButton friendList, btnProfilePhoto, btnMakeVideo,
			btnTravelInfo, btnPhotoPlus;
	private String storagePath = Environment.DIRECTORY_DCIM + "/pic";
	private File imgFile;
	private File storageFile;
	private Bitmap mBitmap;
	private Bitmap scaledBitmap;
	private GridLayout layoutGridPhotoAlbum;
	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	private TextView photoNum;
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

	UserSessionManager session;
	// private ImageAdapter mListAdapter;
	// private ArrayList<ThumbImageInfo> mThumbImageInfoList;

	// 선택된 사진들이 몇번째 데이앨범인지
	private int i_dayalbum;

	// 프로필사진 및 로그인 불러오는 변수
	private Bitmap userProfilePhoto = null;

	private int photoAreaWidth;
	private int photoAreaHeight;

	private UserDTO user;
	// 트립아이디랑 스타트 데이트 등등
	private TripDTO trip;

	// 사진 가져오는
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;
	private GetAllPhotoByTripId getPhoto;
	private String[] imagepath;
	private int endNum = 0;
	private int pixNum = 0;
	private ImageView downloadedImg;
	private ProgressDialog simpleWaitDialog;
	private String SERVERURI = "http://54.178.166.213";
	private String addUrl = null;
	private String upLoadServerUri = null;

	List<List<Bitmap>> listOfPhotoBitmapLists = new ArrayList<List<Bitmap>>();

	//
	private PhotoUploader photoUploader;
	private String intent_date;

	private ArrayList<Integer> intent_dateList;
	
	@Override
	protected void onResume() {
		super.onResume();
		
		GetTripInfo getTrip = new GetTripInfo();
		getTrip.execute(Integer.toString(trip.getTripId()));
		
		CheckMakeVideo checkVideo = new CheckMakeVideo();
		checkVideo.execute(user, trip);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.enableDefaults();
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_photoput);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// user 로그인 정보 setting
		PourneyApplication Application = (PourneyApplication) getApplication();
		user = new UserDTO();
		trip = new TripDTO();
		user = Application.getLoggedInUser();
		trip = Application.getSelectedTrip();
		Log.d("PhotoputActivity_log", user.toString() + " , " + trip.toString());

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

		photoNum = (TextView) findViewById(R.id.textPhotoNum);

		btnProfilePhoto = (ImageButton) findViewById(R.id.btnForProfilePhoto);
		btnProfilePhoto.setOnClickListener(this);
		askBtn = (Button) findViewById(R.id.ask_text);
		askBtn.setOnClickListener(this);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		albumBtn.setOnClickListener(this);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		logoutBtn.setOnClickListener(this);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);
		profileBtn.setOnClickListener(this);

		layoutAlbum = (LinearLayout) findViewById(R.id.layoutAlbum);
		btnMakeVideo = (ImageButton) findViewById(R.id.btnMakeVideo);

		// 비디오 만들기 버튼 초기화
		btnPhotoPlus = (ImageButton) findViewById(R.id.btnPhotoPlus);
		// video 만들기 버튼 tripDTO확인해서 현재 영상을 만들어졌는지, 만들기를 눌렀는지, 안눌렀는지에 따라서 버튼 모양이
		// 바뀐다
		if (trip.isVideoMade()) {
			// 비디오가 만들어진 경우
			Log.d("비디오 만들어져있엉", "ㄱ만드는주으로 셋팅");
			btnPhotoPlus.setOnClickListener(this);
			Drawable res = getResources().getDrawable(
					R.drawable.i_video_making_go_582x100);
			btnPhotoPlus.setImageDrawable(res);
		} else {
			// 비디오 안만들어진경우
			// 지금 user가 비디오 만든앤지 체크.. 체크해서 onpost에서 버튼을 바꾼다.
			Log.d("비디오 안 만들어져있엉", "체크하자");
			CheckMakeVideo checkMakeVideo = new CheckMakeVideo();
			checkMakeVideo.execute(user, trip);
		}

		btnMakeVideo.setOnClickListener(this);
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
		
		// 여행 정보 setting
		popupLocation.setText(Integer.toString(trip.getPeopleCnt()));// 디비에서 사람
																		// 수
																		// 불러와서
																		// 넣어주세요
		title.setText(trip.getTripTitle());
		date.setText(trip.getStartDateInDateFormat() + " ~ "
				+ trip.getEndDateInDateFormat());

		upLoadServerUri = "http://54.178.166.213/androidPixUploadToPhp.php";

		// friendlist 표시
		friendList.setOnClickListener(this);

		ProfileImageSetter profileImageSetter = new ProfileImageSetter();
		profileImageSetter.execute();

		if (trip.getPhotoCnt() > 0) {
			Log.d("PhotoputActivity", "" + trip.getPhotoCnt());
			getPhoto = new GetAllPhotoByTripId();
			getPhoto.execute(trip, intent_dateList);
		}
	}

	private void init() {
		// 여행일정만큼 어레이리스트 생성
		dayalbumList = new ArrayList<DayAlbum>();
		intent_dateList = new ArrayList<Integer>();

		// 날짜계산
		GregorianCalendar gregorianStart = new GregorianCalendar();
		GregorianCalendar gregorianEnd = new GregorianCalendar();
		gregorianStart.setTimeInMillis(trip.getStartDate());
		gregorianEnd.setTimeInMillis(trip.getEndDate());
		if (gregorianStart.get(Calendar.MONTH) == gregorianEnd
				.get(Calendar.MONTH))
			travel = (gregorianEnd.get(Calendar.DATE) - gregorianStart
					.get(Calendar.DATE)) + 1;
		else {// 여행이 시작하는 날과 끝나는 날이 다른 경우
			travel = (gregorianStart.getMaximum(Calendar.DAY_OF_MONTH) - gregorianStart
					.get(Calendar.DATE)) + gregorianEnd.get(Calendar.DATE) + 1;
			Log.d("travel", travel + "");
		}
		for (int i = 0; i < travel; i++) {
			// 날짜 쪼개기
			String month = (gregorianStart.get(Calendar.MONTH) + 1) + "";
			String date = (gregorianStart.get(Calendar.DATE)) + "";
			String year = gregorianStart.get(Calendar.YEAR) + "";
			if (month.length() == 1)
				month = "0" + month;
			Log.d("month", month);
			if (date.length() == 1)
				date = "0" + date;
			intent_date = year + month + date;

			dayalbumList.add(new DayAlbum(PhotoputActivity.this, Integer
					.parseInt(intent_date),
					(gregorianStart.get(Calendar.MONTH) + 1) + "."
							+ gregorianStart.get(Calendar.DATE) + ""));
			layoutAlbum.addView(dayalbumList.get(i));
			intent_dateList.add(Integer.parseInt(intent_date));

			gregorianStart.add(Calendar.DATE, 1);
		}
		// setPhotoNum();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			session.logoutUser();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else if (v.getId() == R.id.btnPhotoPlus) {
			// 이미 영상 보러가기 그림 뜬 상태
			// 영상 페이지로 넘어가기
			Intent intent = new Intent(PhotoputActivity.this,
					VideoViewActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.last_album_text) {

			Intent intent = new Intent(this, CoverActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		} else if (v.getId() == R.id.profile_modifying_text) {

			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
			finish();
		} else if (v.getId() == R.id.imgBack) {

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
				friendListPopupWindow.showAtLocation(popupLocation, 0, 0, 218);

				// friendListPopupWindow.showAsDropDown(popupLocation, -475,
				// 27);
			}
		} else if (v.getId() == R.id.btnMakeVideo) {
			// 체크가 선택된 이미지들 가져오기
			// 체크된 애는 2갠데 view는 3개라서 3번 돌아서 죽음
			for (int i = 0; i < listOfPhotoBitmapLists.size(); i++) {
				Log.d("checked array", dayalbumList.get(i)
						.getCheckedImageArray().toString());
			}
		}

		// else if (v.getId() == R.id.btnMakeVideo) {
		//
		// LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
		// .getSystemService(LAYOUT_INFLATER_SERVICE);
		// View popupView = layoutInflater.inflate(R.layout.photo_memo, null);
		// memoPopupWindow = new PopupWindow(popupView,
		// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		// memoPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// memoPopupWindow.setFocusable(true);
		// memoPopupWindow.setOutsideTouchable(true);
		// memoPopupWindow.setTouchInterceptor(new OnTouchListener() {
		//
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
		// memoPopupWindow.dismiss();
		// return true;
		// }
		// return false;
		// }
		// });
		//
		// memoPopupWindow.showAtLocation(date, 0, 0, 218);
		//
		// }

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 200) {
				// 선택된 데이앨범의 i값을 받아온다
				i_dayalbum = data.getIntExtra("intent_date", 300);
				Log.d("Photoput : i_dayalbum ", i_dayalbum + "");
				// 사진 패스를 받아옴
				ArrayList<PhotoInfo> all_path = (ArrayList<PhotoInfo>) data
						.getExtras().getSerializable("list");
				// TODO Auto-generated method stub
				Log.d("all_path.length", all_path.size() + "");

				PhotoUploader photoUploader = null;

				int day = 0;
				for (int i = 0; i < intent_dateList.size(); i++) {
					if (intent_dateList.get(i) == i_dayalbum) {
						day = i;
					}
				}
				Log.d("day", day + "");
				for (int i = 0; i < all_path.size(); i++) {
					// 받아온 패스로 파일 만들어서 레이아웃 그리드 앨범에 추가한다.
					// 아직 서버 부분은 고려하지 않았기 때문에 선택된 사진의 수만큼만 반복되고,
					// 선택된 사진만 들어가는데 서버에서 사진 가져오는 부분에는 저 밑에
					// dayalbumList.get(i_dayalbum).addLayoutGridalbum(new
					// AlbumImgCell(PhotoputActivity.this,파일타입));
					// 넣으면 될 것 같아요.
					all_path.get(i)
							.setFile(new File(all_path.get(i).getPath()));
					Bitmap bm = ImageResizer.resize(all_path.get(i).getFile(),
							300, 300, ResizeMode.FIT_TO_HEIGHT);
					dayalbumList.get(day).addLayoutGridalbum(
							new AlbumImgCell(PhotoputActivity.this, bm,
									new PhotoDTO(), user.getUserId()));
				}
				// 서버에 사진 업로드
				dialog = ProgressDialog.show(PhotoputActivity.this, "",
						"Uploading file...", true);
				// 한 날짜만 될 듯, 한번만 조회해서 18일것만 서버에서 조회하게 될 것 데이트 자체를 리스트로 받아서
				for (int i = 0; i < all_path.size(); i++) {
					Log.d("photoput", "upload(" + i + ")");
					trip.setPhotoCnt(trip.getPhotoCnt() + 1);
					// upload[i] = new ImageUploader();
					// upload[i].execute(all_path.get(i).getPath());
					photoUploader = new PhotoUploader(
							all_path.get(i).getPath(), user.getUserId(),
							trip.getTripId(), i_dayalbum);
					photoUploader.start();
					try {
						photoUploader.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				dialog.dismiss();
				// updatephotodate = new UpdatePhotodate();
				// updatephotodate.execute(trip);
			}

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
			btnProfilePhoto.setImageBitmap(userProfilePhoto);
			super.onPostExecute(result);
		}
	}// end of ProfileImageSetter

	/**
	 * 해당 trip id에 해당하는 모든 사진을 받아와서 사진의 정보를 tripDTO에 저장한다. tripDTO들은 photoList에
	 * 저장된다.
	 */
	public class GetAllPhotoByTripId extends AsyncTask<Object, String, String> {
		TripDTO td = new TripDTO();
		ArrayList<Integer> dateList = new ArrayList<Integer>();
		ArrayList<PhotoDTO> photoList = new ArrayList<PhotoDTO>();

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			simpleWaitDialog = ProgressDialog.show(PhotoputActivity.this, "",
					"사진을 받아오는 중입니다.");
		}

		@Override
		protected String doInBackground(Object... params) {
			// convert object into tripDTO
			td = (TripDTO) params[0];
			dateList = (ArrayList<Integer>) params[1];
			InputStream is = null;
			StringBuilder sb = null;
			String filename = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", Integer
					.toString(td.getTripId())));
			// 여행의 아이디가 들어와줘야한다. param[0] 세션에 저장되어 있는거 가져와서 넣어주면 됨
			// 저거 하나더 추가해서 trip_id photo_date라고 해서 string으로 변환해서 보내주면 됨
			// arg0[1] 이게 포토 데이트

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/getPhotoFilename.php");
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
				result = sb.toString();
				Log.d("getFile_logMsg", result); // result 가 null이지???

			} catch (Exception e) {
				Log.e("getFile_log_tag",
						"Error converting result " + e.toString());
			}

			// 이미지 url list 받아오는 부분
			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;

				pixNum = jArray.length();
				Log.d("pixNum", Integer.toString(pixNum));
				photoList = new ArrayList<PhotoDTO>();
				for (int i = 0; i < jArray.length(); i++) {
					PhotoDTO photoDTO = new PhotoDTO();
					json_data = jArray.getJSONObject(i);
					photoDTO.setPhotoId(json_data.getInt("photo_id"));
					photoDTO.setUserId(json_data.getInt("user_id"));
					photoDTO.setTripId(json_data.getInt("trip_id"));
					photoDTO.setPhoto_date(json_data.getInt("photo_date"));
					photoDTO.setLikes(json_data.getInt("likes"));
					photoDTO.setPhotoFilename(SERVERURI
							+ json_data.getString("photo_filename"));
					// file이름 받아온 후,
					// 사진파일들이 저장되어있는 폴더 url에
					// 파일이름 string을 합쳐서 url list에 넣음
					photoList.add(photoDTO);
				}
				// list에 다 넣은 후 post에서 다운로드 이미지 함수 호출
				Log.d("photo information", photoList.toString());

			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			// listOfPhotoURLLists;
			// url list에 있는 주소들의 날짜가 date리스트에 일치하는게 있으면.. 거기에 넣음
			Log.d("list size", "dateList : " + dateList.size() + "photoList : "
					+ photoList.size());
			for (int i = 0; i < dateList.size(); i++) {
				ArrayList<Bitmap> photoBitmapListInOneDay = new ArrayList<Bitmap>();
				for (int k = 0; k < photoList.size(); k++) {
					if (getDateFromImageUrl(photoList.get(k).getPhotoFilename()) == dateList
							.get(i)) {
						photoBitmapListInOneDay.add(PhotoEditor
								.ImageurlToBitmapConverter(photoList.get(k)
										.getPhotoFilename()));
					}
				}
				listOfPhotoBitmapLists.add(photoBitmapListInOneDay);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 앨범에 listofPhotobitmaplist 보여주기
			getImages();
			simpleWaitDialog.dismiss();
		}

		// helper method of GetFilename
		private int getDateFromImageUrl(String url) {
			int i = 0;
			StringTokenizer stk = new StringTokenizer(url, "_");
			while (stk.hasMoreElements()) {
				stk.nextToken();
				if (i++ == 2)
					return Integer.parseInt(stk.nextToken());
			}
			return -1;
		}

		private void getImages() {
			// dayalbumlist에 인덱스로 접근해서 addLayoutGridalbum으로 이미지를 한장씩 추가함\
			// 사진을 추가할때 해당 view에 위치하는 photoDTO를 같이 전달한다.
			// view에서 이 photoDTO를 받아 좋아요를 처리한다. 좋아요 처리시 user아이디가 같이 필요하므로 같이 넘겨줌
			int y = 0;
			for (int i = 0; i < listOfPhotoBitmapLists.size(); i++) {

				for (int k = 0; k < listOfPhotoBitmapLists.get(i).size(); k++) {
					dayalbumList.get(i).addLayoutGridalbum(
							new AlbumImgCell(PhotoputActivity.this,
									listOfPhotoBitmapLists.get(i).get(k),
									photoList.get(y++), user.getUserId()));
				}
			}
		}
	}// end of GetfileName

	/**
	 * 
	 * @author Junki 비디오 만들기 버튼 누를시 호출. 유저 정보와 trip정보를 받아서 UserInTrips테이블에 등록한다.
	 */
	public class ConfirmMakeVideo extends AsyncTask<Object, String, String> {
		private UserDTO mUserDTO;
		private TripDTO mTripDTO;

		@Override
		protected String doInBackground(Object... params) {
			// convert object into photoDTO
			mUserDTO = (UserDTO) params[0];
			mTripDTO = (TripDTO) params[1];

			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", Integer
					.toString(mTripDTO.getTripId())));
			nameValuePairs.add(new BasicNameValuePair("user_id", Integer
					.toString(mUserDTO.getUserId())));
			nameValuePairs.add(new BasicNameValuePair("like", "1"));
			Log.d("nameValuePairs", nameValuePairs.toString());

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/confirmMakeVideo.php");
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
				result = sb.toString();
				Log.d("confirm_video_logMsg", result); // result 가 null이지???

			} catch (Exception e) {
				Log.e("confirm_video_logMsg",
						"Error converting result " + e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}// end of photoLike

	/**
	 * 
	 * @author Junki 비디오 만들기 버튼 눌렀을 때 호출
	 */
	public class CheckMakeVideo extends AsyncTask<Object, String, String> {
		private UserDTO mUserDTO;
		private TripDTO mTripDTO;
		private Boolean isMade;

		@Override
		protected String doInBackground(Object... params) {
			// convert object into DTO
			mUserDTO = (UserDTO) params[0];
			mTripDTO = (TripDTO) params[1];
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", Integer
					.toString(mTripDTO.getTripId())));
			nameValuePairs.add(new BasicNameValuePair("user_id", Integer
					.toString(mUserDTO.getUserId())));

			Log.d("Made??", nameValuePairs.toString());

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/checkMakeVideo.php");
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
				Log.d("photoLike_logMsg", result); // result 가 null이지???

			} catch (Exception e) {
				Log.e("photoLike_logMsg",
						"Error converting result " + e.toString());
			}

			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;
				json_data = jArray.getJSONObject(0);
				isMade = (1 == json_data.getInt("isMade"));

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (isMade) {
				// 이미 만들기 누른경우 만드는 중 표시
				Log.d("isMade?", "만들기 누른놈임");
				Drawable res = getResources().getDrawable(
						R.drawable.i_video_making_ing_582x100);
				btnPhotoPlus.setImageDrawable(res);
				btnPhotoPlus.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								VideoMakingActivity.class);
						startActivity(intent);
					}
				});
			} else {
				// 만들기 아직 안누른경우
				Log.d("isMade?", "만들기 안누른놈임");
				Drawable res = getResources().getDrawable(
						R.drawable.i_video_making_make_582x100);
				btnPhotoPlus.setImageDrawable(res);
				btnPhotoPlus.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d("PhotoputActivity btnPhotoPlus",
								"confirm making video");
						ConfirmMakeVideo makeVideo = new ConfirmMakeVideo();
						makeVideo.execute(user, trip);

						CheckMakeVideo checkMakeVideo = new CheckMakeVideo();
						checkMakeVideo.execute(user, trip);
						Drawable res = getResources().getDrawable(
								R.drawable.i_video_making_ing_582x100);
						btnPhotoPlus.setImageDrawable(res);
						isMade = true;
					}
				});
			}

		}

	}// end of photoLike
	
	public class GetTripInfo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", params[0]));
			Log.d("trip_Id", "trip id : " + params[0]);
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/getCoverInfo.php");
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
				JSONArray JsonArray = new JSONArray(result);
				JSONObject JsonObject = JsonArray.getJSONObject(0);
				trip.setTripId(JsonObject.getInt("trip_id"));
				trip.setTripTitle(JsonObject.getString("trip_name"));
				trip.setStartDate(JsonObject.getLong("start_date"));
				trip.setEndDate(JsonObject.getLong("end_date"));
				//tripDTO.setVideoDueDate(JsonObject.getLong("video_due_date"));
				trip.setPhotoCnt(JsonObject.getInt("photo_count"));
				trip.setPeopleCnt(JsonObject.getInt("people_count"));
				trip.setVideoMade("1".equals(JsonObject.getString("is_video_made")));
			} catch (JSONException e1) {
				Log.e("log_msg", e1.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// travel information setting
			Log.d("settext", trip.toString());
			title.setText(trip.getTripTitle());
			date.setText(trip.getStartDateInDateFormat() + " ~ "
					+ trip.getEndDateInDateFormat());
			popupLocation
					.setText("With " + trip.getPeopleCnt() + " people");
		}
	}



}
