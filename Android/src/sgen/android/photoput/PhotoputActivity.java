package sgen.android.photoput;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sgen.DTO.TripDTO;
import sgen.DTO.UserDTO;
import sgen.android.multigallery.PhotoInfo;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.common.PhotoUploader;
import sgen.image.resizer.ImageResizer;
import sgen.image.resizer.ResizeMode;
import sgen.sgen_pourney.AskActivity;
import sgen.sgen_pourney.CoverActivity;
import sgen.sgen_pourney.LoginActivity;
import sgen.sgen_pourney.ProfileModi;
import sgen.sgen_pourney.R;
import sgen.sgen_pourney.SimpleSideDrawer;
import sgen.sgen_pourney.TravelInfoActivity;
import sgen.sgen_pourney.VideoMakingActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

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

	private TextView popupLocation, title, date;
	private ImageButton friendList, btnProfilePhoto, btnMakeVideo,
			btnTravelInfo;
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
	private GetFilename get;
	private ImageDownloader imagedown;
	private String[] imagepath;
	private int endNum = 0;
	private int pixNum = 0;
	private ImageView downloadedImg;
	private ProgressDialog simpleWaitDialog;
	private String SERVERURI = "http://54.178.166.213";
	private ArrayList<String> urllist = null;
	private String addUrl = null;
	private String upLoadServerUri = null;

	List<List<Bitmap>> listOfPhotoBitmapLists = new ArrayList<List<Bitmap>>();

	//
	private PhotoUploader photoUploader;
	private String intent_date;
	// 여기
	private UpdatePhotodate updatephotodate;

	private ArrayList<Integer> intent_dateList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		imgCheckBox = (CheckBox) findViewById(R.id.video_image_selection);

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
		btnMakeVideo = (ImageButton) findViewById(R.id.btnPhotoPlus);
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
		popupLocation.setText("왜 너만");// 디비에서 사람 수 불러와서 넣어주세요
		title.setText(trip.getTripTitle());
		date.setText(trip.getStartDateInDateFormat() + " ~ "
				+ trip.getEndDateInDateFormat());

		upLoadServerUri = "http://54.178.166.213/androidPixUploadToPhp.php";

		// friendlist 표시
		friendList.setOnClickListener(this);

		ProfileImageSetter profileImageSetter = new ProfileImageSetter();
		profileImageSetter.execute();

		if (trip.getPhotoCnt() > 0) {
			get = new GetFilename();
			get.execute(trip, intent_dateList);
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
		ArrayList<GregorianCalendar> gregorianArrayList = new ArrayList<GregorianCalendar>();
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

	/*
	 * public void setPhotoNum() {
	 * 
	 * for (int i = 0; i < listOfPhotoBitmapLists.size(); i++) { checkedNum=0;
	 * layoutAlbum.getContext().set(Integer.toString(checkedNum)); for (int k =
	 * 0; k < listOfPhotoBitmapLists.get(i).size(); k++) { if
	 * (imgCheckBox.isChecked() == true) { checkedNum++; } else checkedNum--;
	 * 
	 * photoNum.setText(Integer.toString(checkedNum));
	 * 
	 * } }
	 * 
	 * }
	 */

	public void onClick(View v) {
		if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else if (v.getId() == R.id.btnPhotoPlus) {
			Intent intent = new Intent(PhotoputActivity.this,
					VideoMakingActivity.class);
			startActivity(intent);
			finish();
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
				all_path.get(i).setFile(new File(all_path.get(i).getPath()));
				Bitmap bm = ImageResizer.resize(all_path.get(i).getFile(), 300,
						300, ResizeMode.FIT_TO_HEIGHT);
				dayalbumList.get(day).addLayoutGridalbum(
						new AlbumImgCell(PhotoputActivity.this, bm));
			}
			// 서버에 사진 업로드
			dialog = ProgressDialog.show(PhotoputActivity.this, "",
					"Uploading file...", true);
			// 한 날짜만 될 듯, 한번만 조회해서 18일것만 서버에서 조회하게 될 것 데이트 자체를 리스트로 받아서
			for (int i = 0; i < all_path.size(); i++) {
				Log.d("photoput", "upload(" + i + ")");
				// upload[i] = new ImageUploader();
				// upload[i].execute(all_path.get(i).getPath());
				photoUploader = new PhotoUploader(all_path.get(i).getPath(),
						user.getUserId(), trip.getTripId(), i_dayalbum);
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			btnProfilePhoto.setImageBitmap(userProfilePhoto);
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}// end of ProfileImageSetter

	/**
	 * 날짜별로 사진 받아오는거
	 */
	public class UpdatePhotodate extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub

			// 전달 받은 object tripDto로 캐스팅
			TripDTO td = new TripDTO();
			td = (TripDTO) params[0];

			InputStream is = null;
			StringBuilder sb = null;
			String filename = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", Integer
					.toString(td.getTripId())));
			nameValuePairs.add(new BasicNameValuePair("photo_date", Long
					.toString(td.getStartDate())));

			// 여행의 아이디가 들어와줘야한다. param[0] 세션에 저장되어 있는거 가져와서 넣어주면 됨
			// 저거 하나더 추가해서 trip_id photo_date라고 해서 string으로 변환해서 보내주면 됨
			// arg0[1] 이게 포토 데이트 여기서도 마찬가지로 보내주면 됨

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/updatePhotoDate.php");
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
				Log.d("getFile_logMsg", result); // result 가 null이지???

			} catch (Exception e) {
				Log.e("getFile_log_tag",
						"Error converting result " + e.toString());
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			String trip_id = "101";
			get = new GetFilename();
			get.execute(trip, intent_dateList);
			// get.execute(trip_id,photo_date)
			// 사진을 서버에 업로드시킨 다음에 업데이트 포토 데이트를 호출해서 사진 날짜를 디비에 업데이트

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}

	/**
	 * 사진 이름 리스트 받아오기
	 */
	public class GetFilename extends AsyncTask<Object, String, String> {
		TripDTO td = new TripDTO();
		ArrayList<Integer> dateList = new ArrayList<Integer>();

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
				urllist = new ArrayList<String>();
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);
					filename = json_data.getString("photo_filename");
					Log.d("filename", filename);
					// file이름 받아온 후,
					// 사진파일들이 저장되어있는 폴더 url에
					// 파일이름 string을 합쳐서 url list에 넣음
					urllist.add(addUrl = SERVERURI + filename);
				}
				// list에 다 넣은 후 post에서 다운로드 이미지 함수 호출
				Log.d("url List", urllist.toString());
				Log.d("date List", dateList.toString());

			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			// urllist, datelist
			// listOfPhotoURLLists;
			// url list에 있는 주소들의 날짜가 date리스트에 일치하는게 있으면.. 거기에 넣음
			Log.d("list size", "dateList : " + dateList.size() + "urllist : "
					+ urllist.size());
			for (int i = 0; i < dateList.size(); i++) {
				ArrayList<Bitmap> photoBitmapListInOneDay = new ArrayList<Bitmap>();
				for (int k = 0; k < urllist.size(); k++) {
					if (getDateFromImageUrl(urllist.get(k)) == dateList.get(i)) {
						photoBitmapListInOneDay.add(PhotoEditor
								.ImageurlToBitmapConverter(urllist.get(k)));
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
			// 이미지 여러개 다운받을 때 이미지 url들이 적힌 리스트를 파라미터로 전송
			// imagedown = new ImageDownloader();
			// imagedown.execute(urllist);
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
			// TODO Auto-generated method stub
			// dayalbumlist에 인덱스로 접근해서 addLayoutGridalbum으로 이미지를 한장씩 추가함
			// ArrayList<ArrayList<Bitmap>> imageList = new
			// ArrayList<ArrayList<Bitmap>>();
			for (int i = 0; i < listOfPhotoBitmapLists.size(); i++) {

				for (int k = 0; k < listOfPhotoBitmapLists.get(i).size(); k++) {
					dayalbumList.get(i).addLayoutGridalbum(
							new AlbumImgCell(PhotoputActivity.this,
									listOfPhotoBitmapLists.get(i).get(k)));

				}
			}

		}

	}// end of GetfileName

	public class ImageDownloader extends AsyncTask<String[], String, Bitmap> {

		@Override
		protected Bitmap doInBackground(String[]... param) {
			// TODO Auto-generated method stub
			Log.d("param length", Integer.toString(param[0].length));

			// param[0]에 url list들이 들어있으므로
			// 이미지 다운을 위해 downloadBitmap 함수 호출!
			return downloadBitmap(param[0]);
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
			simpleWaitDialog = ProgressDialog.show(PhotoputActivity.this,
					"Wait", "Downloading Image");

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			Log.i("Async-Example", "onPostExecute Called");
			simpleWaitDialog.dismiss();

			// asyncTask가 재호출이 되지 않기 때문에,
			// 서버에 저장되어있는 사진갯수보다,
			// 서버에서 안드로이드로 하나씩 보여주고 있는 사진 갯수가 적은 경우에만
			// ImageDownloader함수(현재 함수)를 또 호출해주기 위한 부분입니다!
			// 따라서 다 다운로드가 되면 더이상 imageDownloader가 호출되지 않음!
			if (endNum < pixNum) {
				// } else if (endNum < pixNum) {
				Log.d("urlist", urllist.toString());
				// imagedown = new ImageDownloader();
				// imagedown.execute(urllist);
			}

			// }

		}

		private Bitmap downloadBitmap(String[] url) {
			// initilize the default HTTP client object

			final DefaultHttpClient client = new DefaultHttpClient();

			// addview
			// forming a HttoGet request
			final HttpGet getRequest = new HttpGet(url[endNum++]);
			try {

				HttpResponse response = client.execute(getRequest);

				// check 200 OK for success
				final int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w("ImageDownloader", "Error " + statusCode
							+ " while retrieving bitmap from " + url);
					return null;

				}

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						// getting contents from the stream
						inputStream = entity.getContent();

						// decoding stream data back into image Bitmap that
						// android understands
						final Bitmap bitmap = BitmapFactory
								.decodeStream(inputStream);

						// addview가 activity에서 실행되어야 하는데,
						// asyncTask가 쓰레드 형식이라서
						// addview를 함수로 빼서
						// 쓰레드로 함수를 실행시키고
						// addview는 main class로 빼서 함수로 만들어버림.
						new Thread(new Runnable() {
							public void run() {
								runOnUiThread(new Runnable() {
									public void run() {
										dayalbumList
												.get(i_dayalbum)
												.addLayoutGridalbum(
														new AlbumImgCell(
																PhotoputActivity.this,
																bitmap));
										// addImageView(inHorizontalScrollView,
										// bitmap);
									}
								});
							}
						}).start();
						// return bitmap;
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				// You Could provide a more explicit error message for
				// IOException
				getRequest.abort();
				Log.d("ImageDownloader", "Something went wrong while"
						+ " retrieving bitmap from " + url + e.toString());
			}
			Log.d("endNum", Integer.toString(endNum));
			Log.d("pixNum", Integer.toString(pixNum));

			return null;
		}
	}

}
