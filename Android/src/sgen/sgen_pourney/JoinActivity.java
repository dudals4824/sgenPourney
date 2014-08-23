package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

import sgen.DTO.UserDTO;
import sgen.application.PourneyApplication;
import sgen.common.ListViewDialog;
import sgen.common.ListViewDialog.ListViewDialogSelectListener;
import sgen.common.PhotoEditor;
import sgen.common.ProfileUploader;
import sgen.common.ProfileUploaderByUserId;
import sgen.session.UserSessionManager;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class JoinActivity extends Activity implements OnClickListener {

	private boolean isIdDuplicated = false, isEmailDuplicated = false;
	private RegistTask registTask;
	private CheckDuplication checkDuplication;
	private boolean isDuplicationChecked = false;
	private EditText editEmail, editNickname, editPassword,
			editPasswordConfirm;
	private ImageButton btnCheckDuplication, btnRegist, btnProfilePhoto;
	private Toast idDuplicationToast, emailDuplicationToast,
			duplicationCheckOk;

	// photo type select dialog
	private ListViewDialog mDialog;
	static String SAMPLEIMG = "profile.png";
	static final int REQUEST_ALBUM = 1;
	static final int REQUEST_PICTURE = 2;

	// for profile phpto
	private Uri currImageURI;
	private String imagePath = null;

	private File imgFile;
	private Bitmap mBitmap;

	private int photoAreaWidth;
	private int photoAreaHeight;

	// User Session Manager Class
	UserSessionManager session;

	//
	private String email;
	private String nickname;
	private String password;
	private String passwordConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_join);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// User Session Manager
		session = new UserSessionManager(getApplicationContext());

		// layout initializing
		initLayout();

		idDuplicationToast = Toast.makeText(this, "닉네임이 중복됩니다.",
				Toast.LENGTH_SHORT);
		emailDuplicationToast = Toast.makeText(this, "이메일이 중복됩니다.",
				Toast.LENGTH_SHORT);
		duplicationCheckOk = Toast.makeText(this, "사용가능한 닉네임, 이메일 입니다.",
				Toast.LENGTH_SHORT);

		BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
				R.drawable.i_profilephoto);

		// photoAreaWidth = mPictureBtn.getWidth();
		// photoAreaHeight = mPictureBtn.getHeight();
		photoAreaWidth = bd.getBitmap().getWidth();
		photoAreaHeight = bd.getBitmap().getHeight();
	}

	public void initLayout() {
		// layout initializing -- kjk
		editEmail = (EditText) findViewById(R.id.editEmail);
		editNickname = (EditText) findViewById(R.id.editNickName);
		editPassword = (EditText) findViewById(R.id.editPassword);
		editPasswordConfirm = (EditText) findViewById(R.id.editPasswordConfirm);

		btnCheckDuplication = (ImageButton) findViewById(R.id.btnCheckDuplications);
		btnCheckDuplication.setOnClickListener(this);

		btnRegist = (ImageButton) findViewById(R.id.btnJoin);
		btnRegist.setOnClickListener(this);

		btnProfilePhoto = (ImageButton) findViewById(R.id.btnForProfilePhoto);
		btnProfilePhoto.setOnClickListener(this);
	}

	// server task - regist
	public class RegistTask extends AsyncTask<String, String, String> {
		private static final String SERVER_URI = "http://54.178.166.213";
		private boolean isSuccess;
		private UserDTO newUser = new UserDTO();

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			InputStream is = null;
			StringBuilder sb = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email", params[0]));
			nameValuePairs.add(new BasicNameValuePair("nick_name", params[1]));
			nameValuePairs.add(new BasicNameValuePair("password", params[2]));

			Log.e("arrayList -> ", "email:" + params[0] + " nickname:"
					+ params[1] + " password:" + params[2]);
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/regis.php");
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
				Log.e("log_tag", "result = " + result);
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			// paring data
			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = jArray.getJSONObject(0);
				newUser.setUserId(json_data.getInt("user_id"));
				newUser.setNickName(json_data.getString("nick_name"));
				newUser.setEmail(json_data.getString("email"));
				newUser.setProfileFilePath(SERVER_URI
						+ json_data.getString("profile_filename"));

				Log.e("user information", newUser.toString());
			} catch (JSONException e1) {
				Log.e("log_tag", "Error converting result " + e1.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		public UserDTO getRegisteredUser() {
			return newUser;
		}
	}

	public class CheckDuplication extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String result = null;
			InputStream is = null;
			StringBuilder sb = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email", params[0]));
			nameValuePairs.add(new BasicNameValuePair("nick_name", params[1]));

			Log.e("log_tag", "email: " + params[0] + "nick_name: " + params[1]);

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/checkDuplication.php");
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
				Log.e("log_tag", "result = " + result);
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			// paring data
			try {
				JSONArray ja = new JSONArray(result);
				JSONObject jsonObject = ja.getJSONObject(0);

				// convert int to boolean
				isIdDuplicated = (0 != jsonObject.getInt("isIdDuplicated"));
				isEmailDuplicated = (0 != jsonObject
						.getInt("isEmailDuplicated"));
				Log.e("log_tag", "isIdDuplicated = " + isIdDuplicated
						+ "isEmailDuplicated = " + isEmailDuplicated);
			} catch (JSONException e1) {
				Log.e("log_tag", "Error converting result " + e1.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (isIdDuplicated)
				idDuplicationToast.show();
			else if (isEmailDuplicated)
				emailDuplicationToast.show();
			else
				duplicationCheckOk.show();
			if (result != null) {
				Log.d("ASYNC", "result = " + result);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.btnForProfilePhoto) {
			Log.e("onClick", "click button picture dialog.......");
			showListDialog();
		}
		// join
		else if (arg0.getId() == R.id.btnJoin) {
			email = editEmail.getText().toString();
			nickname = editNickname.getText().toString();
			password = editPassword.getText().toString();
			passwordConfirm = editPasswordConfirm.getText().toString();

			boolean isPasswordValid = false;
			isPasswordValid = PasswordValidityCheck(password);
			/*
			 * 1. 중복체크했는지 확인 2. password일치하는지 확인 3. id 중복인지 확인. 4.
			 */

			if (isDuplicationChecked && isPasswordValid && !isIdDuplicated
					&& !isEmailDuplicated && password.equals(passwordConfirm)) {
				// 5개 validation check 모두 했을시 회원가입 task 수행.
				UserDTO newUser = new UserDTO();

				// registTask로 가입하고 가입해서 받은 newUser의 userId로 image 올림
				registTask = new RegistTask();
				registTask.execute(email, nickname, password);
				try {
					registTask.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				newUser = registTask.getRegisteredUser();

				if (imagePath == null) {
					Log.d("Join Activity.btnJoin", "join without profile image");
				} else {
					/*
					 * ProfileUploader profileUploader = new ProfileUploader(
					 * imagePath, nickname, email, password);
					 * profileUploader.start(); try { profileUploader.join(); }
					 * catch (InterruptedException e) { e.printStackTrace(); }
					 */
					ProfileUploaderByUserId profileUploader = new ProfileUploaderByUserId(imagePath,
							newUser);
					profileUploader.start();
					try {
						profileUploader.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					newUser = profileUploader.getRegisteredUser(); // 사진 포함정보로
																	// 업뎃
				}
				
				Log.d("new user 정보!!!!!!", newUser.toString());

				if (newUser != null) {
					// 유저 정보 전역 객체에 추가
					session.createUserLoginSession(newUser.getUserId(), 0);
					PourneyApplication UserInfo = (PourneyApplication) getApplication();
					UserInfo.setLoggedInUser(newUser);
					Intent intent = new Intent(JoinActivity.this,
							CoverActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(JoinActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				}

			}
			// 예외처리
			else if (!isDuplicationChecked) {
				Toast toast = Toast.makeText(this, "ID 중복체크를 하지 않았습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (!isPasswordValid) {
				Toast toast = Toast.makeText(this, "Password가 너무 짧습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (isIdDuplicated) {
				Toast toast = Toast.makeText(this, "ID가 중복됩니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (isEmailDuplicated) {
				Toast toast = Toast.makeText(this, "메일주소가 중복됩니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (!password.equals(passwordConfirm)) {
				Toast toast = Toast.makeText(this, "비밀번호가 일치하지 않습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			} else {
				Toast toast = Toast.makeText(this, "다시 시도해 주십시오.",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		// check duplication
		else if (arg0.getId() == R.id.btnCheckDuplications) {
			isDuplicationChecked = true;

			String email = editEmail.getText().toString();
			String nickname = editNickname.getText().toString();
			checkDuplication = new CheckDuplication();
			checkDuplication.execute(email, nickname);
		}
	}

	/*
	 * Profile 사진 등록 클릭시 나타나는 dialog.
	 */
	private void showListDialog() {
		/**
		 * 
		 * ListDialog를 보여주는 함수..
		 */
		String[] item = getResources().getStringArray(
				R.array.photo_change_list_item);

		// array를 ArrayList로 변경을 하는 방법

		List<String> listItem = Arrays.asList(item);

		ArrayList<String> itemArrayList = new ArrayList<String>(listItem);

		mDialog = new ListViewDialog(this,
				getString(R.string.photo_change_title), itemArrayList);
		mDialog.onOnSetItemClickListener(new ListViewDialogSelectListener() {

			@Override
			public void onSetOnItemClickListener(int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					Log.v("dialog_msg", " 첫번째 인덱스가 선택되었습니다" + "여기에 맞는 작업을 해준다.");
					// open gallery browser
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							REQUEST_ALBUM);
				} else if (position == 1) {
					// 여기서 부터 카메라 사용
					Log.v("dialog_msg", " 두번째 인덱스가 선택되었습니다" + "여기에 맞는 작업을 해준다.");
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Environment
							.getExternalStorageDirectory(), SAMPLEIMG);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					startActivityForResult(intent, REQUEST_PICTURE);
				}
				mDialog.dismiss();
			}
		});
		mDialog.show();

	}

	private boolean PasswordValidityCheck(String password) {
		if (password.length() < 4)
			return false;
		else
			return true;
	}

	// To handle when an image is selected from the browser, add the following
	// to your Activity
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_ALBUM) {
				// currImageURI is the global variable I'm using to hold the
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
			mBitmap = photoEdit.editPhotoAuto();
			btnProfilePhoto.setImageBitmap(mBitmap);
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
				Toast.makeText(getApplicationContext(), "메모리부족", 0).show();
			}
		}
		return bitmap;
	}

	// get real path - helping method
	private String getRealPathFromURI(Uri contentUri) {
		String path = null;
		String[] proj = { MediaStore.MediaColumns.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			path = cursor.getString(column_index);
		}
		cursor.close();
		return path;
	}

}
