package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
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
	EditText editEmail, editNickname, editPassword, editPasswordConfirm;
	ImageButton btnCheckDuplication, registBtn;

	Toast idDuplicationToast, emailDuplicationToast, duplicationCheckOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_join);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		// layout initializing
		initLayout();

		idDuplicationToast = Toast.makeText(this, "ID가 중복됩니다.",
				Toast.LENGTH_SHORT);
		emailDuplicationToast = Toast.makeText(this, "이메일이 중복됩니다.",
				Toast.LENGTH_SHORT);
		duplicationCheckOk = Toast.makeText(this, "중복없음ㅋ", Toast.LENGTH_SHORT);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public void initLayout() {
		// layout initializing -- kjk
		editEmail = (EditText) findViewById(R.id.editEmail);
		editNickname = (EditText) findViewById(R.id.editNickName);
		editPassword = (EditText) findViewById(R.id.editPassword);
		editPasswordConfirm = (EditText) findViewById(R.id.editPasswordConfirm);

		btnCheckDuplication = (ImageButton) findViewById(R.id.btnCheckDuplications);
		btnCheckDuplication.setOnClickListener(this);

		registBtn = (ImageButton) findViewById(R.id.btnJoin);
		registBtn.setOnClickListener(this);
	}

	// server task - regist
	public class RegistTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

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

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result != null) {
				Log.d("ASYNC", "result = " + result);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
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

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_join, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onClick(View arg0) {

		// join
		if (arg0.getId() == R.id.btnJoin) {

			String email = editEmail.getText().toString();
			String nickname = editNickname.getText().toString();
			String password = editPassword.getText().toString();
			String passwordConfirm = editPasswordConfirm.getText().toString();

			boolean isPasswordValid = false;

			isPasswordValid = PasswordValidityCheck(password);
			/*
			 * 1. 중복체크했는지 확인 2. password일치하는지 확인 3. id 중복인지 확인. 4.
			 */

			if (isDuplicationChecked && isPasswordValid && !isIdDuplicated
					&& !isEmailDuplicated && password.equals(passwordConfirm)) {
				// 5개 validation check 모두 했을시 회원가입 task 수행.
				registTask = new RegistTask();
				registTask.execute(email, nickname, password);
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

			/*
			 * if (!isDuplicationChecked) { Toast toast = Toast.makeText(this,
			 * "중복체크를 해주세요.", Toast.LENGTH_SHORT); toast.show(); } // password
			 * confirming and regist else if (password.equals(passwordConfirm)
			 * && !isIdDuplicated && !isEmailDuplicated) { // execute register
			 * task when password is same registTask = new RegistTask();
			 * registTask.execute(email, nickname, password); } else {
			 * 
			 * Log.e("register log", "password different"); Toast toast =
			 * Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
			 * toast.show(); }
			 */

		}
		// check duplication
		else if (arg0.getId() == R.id.btnCheckDuplications) {
			Dialog dialog=new Dialog(JoinActivity.this, R.style.CustomDialogTheme);
			dialog.setTitle("ddd");
			dialog.setCancelable(true);
			dialog.show();
			isDuplicationChecked = true;

			String email = editEmail.getText().toString();
			String nickname = editNickname.getText().toString();
			checkDuplication = new CheckDuplication();
			checkDuplication.execute(email, nickname);

		}
	}

	private boolean PasswordValidityCheck(String password) {
		if (password.length() < 8)
			return false;
		else
			return true;
	}
}
