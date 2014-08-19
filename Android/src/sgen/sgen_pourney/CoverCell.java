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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sgen.DTO.TripDTO;
import sgen.sgen_pourney.CoverActivity;
import sgen.android.photoput.PhotoputActivity;
import sgen.application.PourneyApplication;

import sgen.common.ListViewDialog;
import sgen.common.PhotoEditor;
import sgen.common.ProfileUploader;
import sgen.common.ListViewDialog.ListViewDialogSelectListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CoverCell extends LinearLayout implements View.OnClickListener,
		View.OnLongClickListener {
	private TextView title, date, numberOfPeople, travelNumber;
	private ImageButton backcard, btnCoverPhoto;
	private Context mContext = null;
	private TripDTO tripDTO = new TripDTO();

	// photo type select dialog
	// private ListViewDialog mDialog;
	static String SAMPLEIMG = "profile.png";
	static final int REQUEST_ALBUM = 1;
	static final int REQUEST_PICTURE = 2;

	public static final int RESULT_OK = -1;

	private int photoAreaWidth;
	private int photoAreaHeight;
	// photo type select dialog
	private ListViewDialog mDialog;

	// for profile phpto
	private ProfileUploader pfUploader = null;
	private Uri currImageURI;
	private String imagePath = null;

	private File imgFile;
	private Bitmap mBitmap;

	private View v;

	// for profile phpto
	// private ProfileUploader pfUploader = null;
	// private Uri currImageURI;
	// private String imagePath = null;
	//
	// private File imgFile;
	// private Bitmap mBitmap;
	//
	// private int photoAreaWidth;
	// private int photoAreaHeight;

	public CoverCell(Context context, int attrs) {
		super(context);
		initMarbleView(context, attrs);
	}

	void initMarbleView(Context context, int attrs) {
		// System.out.println(attrs);
		mContext = context;
		// view layout 설정
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		v = li.inflate(R.layout.album_cover, this, false);
		addView(v);

		// view id 설정
		title = (TextView) findViewById(R.id.travelTitle);
		date = (TextView) findViewById(R.id.dayBack);
		numberOfPeople = (TextView) findViewById(R.id.peopleBack);
		travelNumber = (TextView) findViewById(R.id.travelNumber);
		backcard = (ImageButton) findViewById(R.id.backcard);

		btnCoverPhoto = (ImageButton) findViewById(R.id.cphoto);
		// 폰트 설정
		String fontpath = "fonts/WalbaumBook-BoldItalic.otf";
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontpath);
		title.setTypeface(tf);
		date.setTypeface(tf);
		numberOfPeople.setTypeface(tf);
		travelNumber.setTypeface(tf);

		// trip information setting asynctask
		GetTripInfo getTripInfo = new GetTripInfo();
		getTripInfo.execute(String.valueOf(attrs));

		// onclicklistenr 뷰 아무데나 눌러도 다 넘어가게 모든거에 onclicklistner다줌.....ㅠㅠ
		// title.setOnClickListener(this);
		// date.setOnClickListener(this);
		// numberOfPeople.setOnClickListener(this);
		// travelNumber.setOnClickListener(this);
		backcard.setOnClickListener(this);

		btnCoverPhoto.setOnClickListener(this);

		// title.setOnLongClickListener(this);
		// date.setOnLongClickListener(this);
		// numberOfPeople.setOnLongClickListener(this);
		// travelNumber.setOnLongClickListener(this);
		backcard.setOnLongClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// trip 정보 setting
		if (v.getId() == R.id.cphoto) {
			Log.e("onClick", "click buttons picture dialog.......");
			showListDialog();
		} else {
			PourneyApplication Application = (PourneyApplication) ((Activity) mContext)
					.getApplication();
			Application.setSelectedTrip(tripDTO);
			Log.d("CoverCell_LOG", "onclick"
					+ Application.getSelectedTrip().getTripTitle());

			Intent intent = new Intent(mContext, PhotoputActivity.class);
			mContext.startActivity(intent);
		}
	}

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
				tripDTO.setTripId(JsonObject.getInt("trip_id"));
				tripDTO.setTripTitle(JsonObject.getString("trip_name"));
				tripDTO.setStartDate(JsonObject.getLong("start_date"));
				tripDTO.setEndDate(JsonObject.getLong("end_date"));
				// tripDTO.setVideoDueDate(JsonObject.getLong("video_due_date"));
				tripDTO.setPhotoCnt(JsonObject.getInt("photo_count"));
				tripDTO.setPeopleCnt(JsonObject.getInt("people_count"));
				tripDTO.setVideoMade("1".equals(JsonObject
						.getString("is_video_made")));
			} catch (JSONException e1) {
				Log.e("log_msg", e1.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// travel information setting
			Log.d("settext", tripDTO.toString());
			title.setText(tripDTO.getTripTitle());
			date.setText(tripDTO.getStartDateInDateFormat() + " ~ "
					+ tripDTO.getEndDateInDateFormat());
			numberOfPeople
					.setText("With " + tripDTO.getPeopleCnt() + " people");
			travelNumber.setText(String.valueOf(tripDTO.getTripId()));
		}
	}

	/*
	 * Profile 사진 등록 클릭시 나타나는 dialog.
	 */
	void showListDialog() {
		/**
		 * 
		 * ListDialog를 보여주는 함수..
		 */
		// this.context = context;

		String[] item = mContext.getResources().getStringArray(
				R.array.cover_change_list_item);

		// array를 ArrayList로 변경을 하는 방법

		List<String> listItem = Arrays.asList(item);

		ArrayList<String> itemArrayList = new ArrayList<String>(listItem);

		mDialog = new ListViewDialog(mContext,
				getString(R.string.photo_change_title), itemArrayList);
		mDialog.onOnSetItemClickListener(new ListViewDialogSelectListener() {

			@Override
			public void onSetOnItemClickListener(int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					Log.v("dialog_msg", " 첫번째 인덱스가 선택되었습니다" + "여기에 맞는 작업을 해준다.");
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					((Activity) mContext).startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							REQUEST_ALBUM);
					// open gallery browser

					// CoverActivity covercells = new CoverActivity();
					// covercells.requestAlbum();
					// startActivityForResult(
					// Intent.createChooser(intent, "Select Picture"),
					// REQUEST_ALBUM);
				} else if (position == 1) {
					// 여기서 부터 카메라 사용
					Log.v("dialog_msg", " 두번째 인덱스가 선택되었습니다" + "여기에 맞는 작업을 해준다.");

					// CoverActivity covercells = new CoverActivity();
					// covercells.requestPicture();
					// startActivityForResult(intent, REQUEST_PICTURE);
				}
				mDialog.dismiss();
			}

		});
		mDialog.show();

	}

	public final String getString(int resId) {
		return getResources().getString(resId);
	}

	// @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_ALBUM) {
				Log.d("REQUEST_ALBUM", "REQUEST_ALBUM");
				// content:// URI of the image
				currImageURI = data.getData();
				// 실제 절대주소를 받아옴
				imagePath = getRealPathFromURI(currImageURI);
				Log.e("KJK", "URI : " + currImageURI.toString());
				Log.e("KJK", "Real Path : " + imagePath);

				// image path 얻어왔으면 imgFile초기화.
				imgFile = new File(imagePath);
				// img file bitmap 변경
				if (imgFile.exists()) {
					mBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					// getCroppedBitmap(mBitmap);
					Log.e("비트맵 로드", "성공");
				} else
					Log.e("비트맵 디코딩", "실패");

				backcard.setImageBitmap(mBitmap);
			} else if (requestCode == REQUEST_PICTURE) {
				Log.e("camera", "camera");
				mBitmap = loadPicture();
			}
			// mPictureBtn.setImageBitmap(overlayCover(getCroppedBitmap(resizeBitmapToProfileSize(mBitmap))));
			BitmapDrawable bd = (BitmapDrawable) this.getResources()
					.getDrawable(R.drawable.i_profilephoto_cover);
			Bitmap coverBitmap = bd.getBitmap();
			// constructor
			// mBitmap에 찍은 사진 넣기
			// cover은 그대로
			PhotoEditor photoEdit = new PhotoEditor(mBitmap, coverBitmap,
					photoAreaWidth, photoAreaHeight);
			// resize
			// crop roun
			// overay cover

			// 이거하면 이미지 셋됨
			// mBitmap = photoEdit.editPhotoAuto();
			// btnCoverPhoto.setImageBitmap(mBitmap);
		}
	}

	// load picture - helping method
	private Bitmap loadPicture() {
		Log.e("camera", "load");
		File file = new File(Environment.getExternalStorageDirectory(),
				SAMPLEIMG);
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 4;
		return rotate(BitmapFactory.decodeFile(file.getAbsolutePath(), option),
				90);
	}

	// helping method
	private Bitmap rotate(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees);
			try {
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				if (bitmap != converted) {
					bitmap = null;
					bitmap = converted;
					converted = null;
				}
			} catch (OutOfMemoryError ex) {
				Toast.makeText(mContext.getApplicationContext(), "메모리부족", 0)
						.show();
			}
		}
		return bitmap;
	}

	public void setImageBackCard(Bitmap bitmap) {
		backcard.setImageBitmap(bitmap);
	}

	// get real path - helping method
	private String getRealPathFromURI(Uri contentUri) {
		String path = null;
		String[] proj = { MediaStore.MediaColumns.DATA };
		Cursor cursor = mContext.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			path = cursor.getString(column_index);
		}
		cursor.close();
		return path;
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		showDeleteDialog();

		return false;
	}

	void showDeleteDialog() {
		/**
		 * 
		 * ListDialog를 보여주는 함수..
		 */
		// this.context = context;

		String item = mContext.getResources().getString(R.string.delete_text);
		// array를 ArrayList로 변경을 하는 방법

		List<String> listItem = Arrays.asList(item);

		ArrayList<String> itemArrayList = new ArrayList<String>(listItem);

		mDialog = new ListViewDialog(mContext, "해당 여행에서 나가시겠습니까?",
				itemArrayList);
		mDialog.onOnSetItemClickListener(new ListViewDialogSelectListener() {

			@Override
			public void onSetOnItemClickListener(int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					Log.v("dialog_msg", " 첫번째 인덱스가 선택되었습니다" + "여기에 맞는 작업을 해준다.");
					removeView(v);
				}
				mDialog.dismiss();
			}

		});
		mDialog.show();

	}

}
