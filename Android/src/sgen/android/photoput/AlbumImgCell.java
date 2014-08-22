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
import org.apache.http.impl.conn.Wire;
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
import android.annotation.SuppressLint;
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
import android.widget.Toast;

public class AlbumImgCell extends RelativeLayout implements
		View.OnClickListener, View.OnLongClickListener {

	private Context mContext = null;
	private Bitmap mBitmap = null;
	private Bitmap mBitmapMemo = null;
	private Drawable sPhoto = null;
	private ImageView imgPhoto = null;
	private ImageButton regist, cancel;
	private ImageView selectedPhoto;
	private File mImgFile = null;
	private PopupWindow photoPopupWindow;
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

		btnMemo = (ImageButton) v.findViewById(R.id.btnMemo);

		// mBitmap=ImageResizer.resize(mImgFile, 300, 300);
		// 이미지를 비트맵으로 받아와서 이미지뷰에 추가 리사이징 해야함
		imgPhoto = (ImageView) findViewById(R.id.imgPhoto);

		BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
				R.drawable.i_photo_gray_mask318x318);
		Bitmap coverBitmap = bd.getBitmap();
		PhotoEditor photoEdit = new PhotoEditor(bitmap, coverBitmap,
				coverBitmap.getWidth(), coverBitmap.getHeight());

		imgPhoto.setImageBitmap(photoEdit.editPhotoAutoRectangle());

		imgPhoto.setOnClickListener(this);
		imgPhoto.setOnLongClickListener(this);
		checkImage.setOnClickListener(this);
		btnMemo.setOnClickListener(this);

		checkLike = new CheckAlreadyLiked();
		checkLike.execute(mPhoto, mUserId, checkImage);
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnMemo) {
			mBitmapMemo = ImageResize.resize(mBitmap, 900, 900,
					ResizeMode.AUTOMATIC);
			sPhoto = new BitmapDrawable(getResources(),mBitmap);
			ImageView selectedPhoto;
			LayoutInflater layoutInflater = (LayoutInflater) ((ContextWrapper) mContext)
					.getBaseContext().getSystemService(
							mContext.LAYOUT_INFLATER_SERVICE);
			View popupView = layoutInflater.inflate(R.layout.photo_memo, null);
			photoPopupWindow = new PopupWindow(popupView,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
			photoPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			photoPopupWindow.setFocusable(true);
			photoPopupWindow.setOutsideTouchable(true);
			photoPopupWindow.setTouchInterceptor(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						photoPopupWindow.dismiss();
						return true;
					}
					return false;
				}
			});

			View contentView = photoPopupWindow.getContentView();

			photoPopupWindow.showAtLocation(imgPhoto, 0, 0, 218);
			selectedPhoto = (ImageView) contentView
					.findViewById(R.id.selectedphoto);
			selectedPhoto.setBackground(sPhoto);
			regist = (ImageButton) contentView.findViewById(R.id.regist);
			cancel = (ImageButton) contentView.findViewById(R.id.cancel);
			memo = (EditText) contentView.findViewById(R.id.memo);

			regist.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					editedMemo = memo.getText().toString();
					Log.d("EditedMemo", editedMemo);
					// DB에 저장하는 부분
					WriteComment writeComment = new WriteComment();
					writeComment.execute(mPhoto, editedMemo);
				}
			});
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					photoPopupWindow.dismiss();
				}
			});

		} else if (v.getId() == R.id.checkImage) {
			photoLike = new PhotoLike();
			if (checkImage.isChecked()) {
				Log.d("checked", "checked : " + mPhoto.getPhotoId() + " "
						+ mPhoto.getPhoto_date());
				likeFlag = 1;
				Toast.makeText(mContext.getApplicationContext(),
						"사진이 선택되었습니다.", Toast.LENGTH_SHORT).show();
			} else {
				Log.d("unchecked", "unchecked : " + mPhoto.getPhotoId() + " "
						+ mPhoto.getPhoto_date());
				likeFlag = 0;
				Toast.makeText(mContext.getApplicationContext(),
						"사진 선택을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
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

	public class WriteComment extends AsyncTask<Object, String, String> {
		private PhotoDTO photo = new PhotoDTO();
		private int photoId, tripId, userId;
		private String comment;

		@Override
		protected String doInBackground(Object... params) {
			// convert object into photoDTO
			photo = (PhotoDTO) params[0];
			photoId = photo.getPhotoId();
			tripId = photo.getTripId();
			userId = photo.getUserId();
			comment = (String) params[1];

			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			// parameter setting
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("photo_id", Integer
					.toString(photoId)));
			nameValuePairs.add(new BasicNameValuePair("trip_id", Integer
					.toString(tripId)));
			nameValuePairs.add(new BasicNameValuePair("user_id", Integer
					.toString(userId)));
			nameValuePairs.add(new BasicNameValuePair("comment", comment));
			Log.d(getClass().getName(),
					"comment info : " + nameValuePairs.toString());

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/writeComment.php");
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
			Toast.makeText(mContext.getApplicationContext(),
					"댓글을 등록했습니다.", Toast.LENGTH_SHORT).show();
			photoPopupWindow.dismiss();
		}
	}//write comment

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

	public class DeletePhoto extends AsyncTask<Object, String, String> {
		PhotoDTO photo = new PhotoDTO();
		
		@Override
		protected String doInBackground(Object... params) {
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;
			String photoId = Integer.toString(((PhotoDTO)params[0]).getPhotoId());

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("photo_id",photoId));
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/deletePhoto.php");
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
			if ("1".equals(result)) {
				Toast.makeText(mContext.getApplicationContext(),
						"사진을 삭제했습니다.", Toast.LENGTH_SHORT).show();
				removeView(v);
			}else
			{
				Toast.makeText(mContext.getApplicationContext(),
						"사진을 삭제하지 못했습니다..", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
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
					DeletePhoto deletePhoto = new DeletePhoto();
					deletePhoto.execute(mPhoto , v);
				}
				mDialog.dismiss();
			}

		});
		mDialog.show();

	}

}
