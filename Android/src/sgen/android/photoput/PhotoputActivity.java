package sgen.android.photoput;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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


import sgen.DTO.TripDTO;

import sgen.DTO.UserDTO;
import sgen.android.multigallery.PhotoInfo;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.sgen_pourney.AskActivity;
import sgen.sgen_pourney.CoverActivity;
import sgen.sgen_pourney.LoginActivity;
import sgen.sgen_pourney.ProfileModi;
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
import android.widget.ImageView;
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


	private ImageButton btnForTest;

	private PopupWindow memoPopupWindow;
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
	private TripDTO trip;
	
	// 사진 가져오는
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;
	private GetFilename get;
	private ImageDownloader imagedown;
	private ImageUploader[] upload;
	private String[] imagepath;
	private int endNum = 0;
	private int pixNum = 0;
	private ImageView downloadedImg;
	private ProgressDialog simpleWaitDialog;
	private String downloadUrl = "http://54.178.166.213/test/";
	private String[] urllist = null;
	private String addUrl = null;
	private String upLoadServerUri = null;

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
		Log.d("PhotoputActivity_log", user.toString()+" , "+trip.toString());

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

		
		btnForTest = (ImageButton) findViewById(R.id.btnMakeVideo);
		btnForTest.setOnClickListener(this);
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
		title.setText(trip.getTripTitle());// 디비에서 여행 아이디에 맞는 제목 불러와서 넣어주세요
		date.setText(trip.getStartDate()+"~"+trip.getEndDate());// 디비에서 날짜 불러와서 넣어주세요

		//friendlist 표시
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
		else if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
		else if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
		else if (v.getId() == R.id.making_video) {
			Intent intent = new Intent(this, VideoMakingActivity.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
			finish();
		}
		else if (v.getId() == R.id.imgBack) {

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
		}else if (v.getId() == R.id.btnMakeVideo) {

			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View popupView = layoutInflater.inflate(R.layout.photo_memo, null);
			memoPopupWindow = new PopupWindow(popupView,
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
			memoPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			memoPopupWindow.setFocusable(true);
			memoPopupWindow.setOutsideTouchable(true);
			memoPopupWindow.setTouchInterceptor(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						memoPopupWindow.dismiss();
						return true;
					}
					return false;
				}
			});

			memoPopupWindow.showAtLocation(date, 0, 0, 218);

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
				dialog = ProgressDialog.show(PhotoputActivity.this, "",
						"Uploading file...", true);
				upload=new ImageUploader[all_path.size()];
				for (int i = 0; i < all_path.size(); i++) {
					upload[i] = new ImageUploader();
					upload[i].execute(all_path.get(i).getPath());
				}

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

	}//end of ProfileImageSetter
	
	public class ImageUploader extends AsyncTask<String, String, Integer>{


		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// 사진을 서버로 업로드 완료한 후에
			// 서버에 있는 사진을 다시 안드로이드에 뿌려주기위해
			// 방금 업로드한 사진파일이름 받아오는 부분
			// 같이 여행을 하는 사람들이 볼 수 있어야 하므로 trip_id를 파라미터로 전송
			// 아래 트립아이디는 임의로 해논것입니다!
			String trip_id = "101";
			get = new GetFilename();
			get.execute(trip_id);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		// pre-background-post
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			String sourceFileUri = arg0[0];
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;
			File sourceFile = new File(sourceFileUri);
			if (!sourceFile.isFile()) {
				Log.d("uploadFile", "Source File Does not exist");
				return null;
			}
			try { // open a URL connection to the <span id="IL_AD7"
					// class="IL_AD">Servlet</span>
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);
				conn = (HttpURLConnection) url.openConnection(); // Open a HTTP
				// connection to
				// the URL
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", sourceFileUri);
				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ sourceFileUri + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				bytesAvailable = fileInputStream.available(); // create a buffer
																// of
				// maximum size

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);
				if (serverResponseCode == 200) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(PhotoputActivity.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {
				dialog.dismiss();
				ex.printStackTrace();
				Log.d("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {
				dialog.dismiss();
				e.printStackTrace();
				Log.d("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();

			return serverResponseCode;

		}

	}//end of ImageUploader
	public class GetFilename extends AsyncTask<String, String, String>{

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// 이미지 여러개 다운받을 때 이미지 url들이 적힌 리스트를 파라미터로 전송
			imagedown = new ImageDownloader();
			imagedown.execute(urllist);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			InputStream is = null;
			StringBuilder sb = null;
			String filename = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", arg0[0]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/getPhotoFilename.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Log.d("log_tag", "error in http connection" + e.toString());
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
				Log.d("getFile_log_tag",
						"Error converting result " + e.toString());
			}

			// 이미지 url list 받아오는 부분
			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;

				pixNum = jArray.length();
				Log.d("pixNum", Integer.toString(pixNum));
				urllist = new String[pixNum];
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);
					filename = json_data.getString("photo_filename");
					Log.d("filename", filename);

					// file이름 받아온 후,
					// 사진파일들이 저장되어있는 폴더 url에
					// 파일이름 string을 합쳐서 url list에 넣음
					addUrl = filename;
					addUrl = downloadUrl + addUrl;
					urllist[i] = addUrl;

					Log.d("urlList" + i, urllist[i]);
					// list에 다 넣은 후 post에서 다운로드 이미지 함수 호출

				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			return null;
		}

	}//end of GetfileName
	public class ImageDownloader extends AsyncTask<String[], String, Bitmap>{

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}



		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// 이미지 여러개 다운받을 때 이미지 url들이 적힌 리스트를 파라미터로 전송
			imagedown = new ImageDownloader();
			imagedown.execute(urllist);
		
		}



		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			InputStream is = null;
			StringBuilder sb = null;
			String filename = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", arg0[0]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/getPhotoFilename.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Log.d("log_tag", "error in http connection" + e.toString());
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
				Log.d("getFile_log_tag",
						"Error converting result " + e.toString());
			}

			// 이미지 url list 받아오는 부분
			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;

				pixNum = jArray.length();
				Log.d("pixNum", Integer.toString(pixNum));
				urllist = new String[pixNum];
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);
					filename = json_data.getString("photo_filename");
					Log.d("filename", filename);

					// file이름 받아온 후,
					// 사진파일들이 저장되어있는 폴더 url에
					// 파일이름 string을 합쳐서 url list에 넣음
					addUrl = filename;
					addUrl = downloadUrl + addUrl;
					urllist[i] = addUrl;

					Log.d("urlList" + i, urllist[i]);
					// list에 다 넣은 후 post에서 다운로드 이미지 함수 호출

				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		@Override
		protected Bitmap doInBackground(String[]... params) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
