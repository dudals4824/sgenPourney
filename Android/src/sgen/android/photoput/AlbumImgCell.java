package sgen.android.photoput;

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
import sgen.common.ListViewDialog;
import sgen.common.ListViewDialog.ListViewDialogSelectListener;
import sgen.common.PhotoEditor;
import sgen.image.resizer.ImageResize;
import sgen.image.resizer.ResizeMode;
import sgen.sgen_pourney.R;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumImgCell extends RelativeLayout implements
		View.OnClickListener, View.OnLongClickListener {

	private Context mContext = null;
	private Bitmap mBitmap = null;
	private Bitmap mBitmapMemo = null;
	private Drawable sPhoto = null;
	private ImageView imgPhoto = null;
	private ImageButton regist, cancel;
	private File mImgFile = null;
	private PopupWindow memoPopupWindow;
	private TextView date;
	private PhotoDTO mPhoto;
	private int mUserId;
	// public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";
	private PhotoLike photoLike;
	private CheckAlreadyLiked checkLike;
	private CheckBox checkImage;
	private int likeFlag;
	private EditText memo;
	private String editedMemo;
	private ListViewDialog mDialog;
	private View v;
	private ImageButton btnMemo;
	
	public AlbumImgCell(Context context, Bitmap bitmap, PhotoDTO photo,
			int userId) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		mBitmap = bitmap;
		mPhoto = photo;
		mUserId = userId;
		initMarbleView(context, bitmap, photo, userId);
	}

	private void initMarbleView(Context context, Bitmap bitmap, PhotoDTO photo,
			int userId) {

		date = (TextView) findViewById(R.id.textCalendar); // 여행 날짜
		
		
		
		// Log.d("AlbumImgCell", mBitmap.toString());

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		v = li.inflate(R.layout.album_img_cell, this, false);
		addView(v);

		checkImage = (CheckBox) v.findViewById(R.id.checkImage);
		btnMemo=(ImageButton)v.findViewById(R.id.btnMemo);

		// mBitmap=ImageResizer.resize(mImgFile, 300, 300);
		// 이미지를 비트맵으로 받아와서 이미지뷰에 추가 리사이징 해야함
		imgPhoto = (ImageView) findViewById(R.id.imgPhoto);

		BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
				R.drawable.i_photo_gray_mask318x318);
		Bitmap coverBitmap = bd.getBitmap();
		PhotoEditor photoEdit = new PhotoEditor(bitmap, coverBitmap, coverBitmap.getWidth(), coverBitmap.getHeight());

		imgPhoto.setImageBitmap(photoEdit.editPhotoAutoRectangle());

		imgPhoto.setOnClickListener(this);
		imgPhoto.setOnLongClickListener(this);
		checkImage.setOnClickListener(this);
		btnMemo.setOnClickListener(this);

		checkLike = new CheckAlreadyLiked();
		checkLike.execute(mPhoto, mUserId, checkImage);
	}

	@Override
	public void onClick(View v) {
		 if(v.getId()==R.id.btnMemo){
			mBitmapMemo = ImageResize.resize(mBitmap, 900, 900,
					ResizeMode.AUTOMATIC);
			sPhoto = new BitmapDrawable(getResources(), mBitmapMemo);
			ImageView selectedPhoto;
			LayoutInflater layoutInflater = (LayoutInflater) ((ContextWrapper) mContext)
					.getBaseContext().getSystemService(
							mContext.LAYOUT_INFLATER_SERVICE);
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

			View contentView = memoPopupWindow.getContentView();

			memoPopupWindow.showAtLocation(imgPhoto, 0, 0, 218);
			selectedPhoto = (ImageView) contentView
					.findViewById(R.id.selectedphoto);
			selectedPhoto.setBackground(sPhoto);
			regist = (ImageButton) contentView.findViewById(R.id.regist);
			cancel = (ImageButton) contentView.findViewById(R.id.cancel);
			memo = (EditText) contentView.findViewById(R.id.memo);
			editedMemo = memo.getText().toString();

			regist.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("EditedMemo", editedMemo);
					System.out.println(editedMemo);
					// DB에 저장하는 부분
					memoPopupWindow.dismiss();
				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					memoPopupWindow.dismiss();

				}
			});

		}
		else if (v.getId() == R.id.checkImage) {
			photoLike = new PhotoLike();
			if (checkImage.isChecked()) {
				Log.d("checked", "checked : " + mPhoto.getPhotoId() + " "
						+ mPhoto.getPhoto_date());
				likeFlag = 1;
			} else {
				Log.d("unchecked", "unchecked : " + mPhoto.getPhotoId() + " "
						+ mPhoto.getPhoto_date());
				likeFlag = 0;
			}
			photoLike.execute(mPhoto, mUserId, likeFlag);
		}
		// 메모 불러와야 될 건 photoput activity 에 memooutput 으로 검색 ㄱㄱ
	}

	public class PhotoLike extends AsyncTask<Object, String, String> {
		private PhotoDTO photo = new PhotoDTO();
		private int userId, like;

		@Override
		protected String doInBackground(Object... params) {
			// convert object into photoDTO
			photo = (PhotoDTO) params[0];
			userId = (Integer) params[1];
			like = (Integer) params[2];

			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("photo_id", Integer
					.toString(photo.getPhotoId())));
			nameValuePairs.add(new BasicNameValuePair("trip_id", Integer
					.toString(photo.getTripId())));
			nameValuePairs.add(new BasicNameValuePair("user_id", Integer
					.toString(userId)));
			nameValuePairs.add(new BasicNameValuePair("like", Integer
					.toString(like)));
			Log.d("nameValuePairs", nameValuePairs.toString());

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/photoLike.php");
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
				Log.d("photoLike_logMsg", result); // result 가 null이지???

			} catch (Exception e) {
				Log.e("photoLike_logMsg",
						"Error converting result " + e.toString());
			}

			try {
				// JSONArray jArray = new JSONArray(result);
				// JSONObject json_data = null;
				// json_data = jArray.getJSONObject(0);

			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}// end of photoLike

	public class CheckAlreadyLiked extends AsyncTask<Object, String, String> {
		private PhotoDTO photo = new PhotoDTO();
		private int userId;
		private boolean isLiked;
		private CheckBox checkBox;

		@Override
		protected String doInBackground(Object... params) {
			// convert object into photoDTO
			photo = (PhotoDTO) params[0];
			userId = (Integer) params[1];
			checkBox = (CheckBox) params[2];
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("photo_id", Integer
					.toString(photo.getPhotoId())));
			nameValuePairs.add(new BasicNameValuePair("user_id", Integer
					.toString(userId)));

			Log.d("liked??", nameValuePairs.toString());

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/checkAlreadyLiked.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
				Log.d("photoLike_logMsg", result); // result 가 null이지???

			} catch (Exception e) {
				Log.e("photoLike_logMsg",
						"Error converting result " + e.toString());
			}

			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;
				json_data = jArray.getJSONObject(0);
				isLiked = (1 == json_data.getInt("isLiked"));

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (isLiked)
				checkBox.setChecked(true);
			else
				checkBox.setChecked(false);
		}

	}// end of photoLike

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		showDeleteDialog();
		return false;
	}

	private void showDeleteDialog() {
		/**
		 * 
		 * ListDialog를 보여주는 함수..
		 */
		// this.context = context;

		String item = "삭제";
		// array를 ArrayList로 변경을 하는 방법

		List<String> listItem = Arrays.asList(item);

		ArrayList<String> itemArrayList = new ArrayList<String>(listItem);

		mDialog = new ListViewDialog(mContext, "사진을 앨범에서 삭제하시겠습니까?",
				itemArrayList);
		mDialog.onOnSetItemClickListener(new ListViewDialogSelectListener() {
			// 여기서 inflate받은 뷰 v를 삭제함
			@Override
			public void onSetOnItemClickListener(int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					removeView(v);
				}
				mDialog.dismiss();
			}

		});
		mDialog.show();

	}

}
