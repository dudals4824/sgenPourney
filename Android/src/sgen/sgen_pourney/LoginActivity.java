package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Session;

public class LoginActivity extends Activity implements OnClickListener {

	private ImageButton btnLogin;
	private ImageButton btnFacebook, btnJoin;
	private EditText editEmailaddress, editPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		btnFacebook = (ImageButton) findViewById(R.id.btnFacebook);
		btnJoin = (ImageButton) findViewById(R.id.btnJoin);
		editEmailaddress = (EditText) findViewById(R.id.editEmailaddress);
		editPassword = (EditText) findViewById(R.id.editPassword);
		btnLogin.setOnClickListener(this);
		btnFacebook.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnLogin) {
	
			String emailAddress, password;
			emailAddress = editEmailaddress.getText().toString();
			password = editPassword.getText().toString();
			
			LoginTask loginTask = new LoginTask();
			loginTask.execute(emailAddress, password);

			System.out.println("login");
			Intent intent = new Intent(LoginActivity.this,
					TravelInfoActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.editEmailaddress) {
			editEmailaddress
					.setBackgroundResource(R.drawable.i_emailaddress_put);
		} else if (v.getId() == R.id.btnFacebook) { // facebook login 占쏙옙 id
													// 占쏙옙占�
			// System.out.println("占싹뤄옙 占쏙옙占쏙옙 占승댐옙");
			// Session.openActiveSession(this, true, new
			// Session.StatusCallback() {
			//
			// // callback when session changes state
			// @Override
			// public void call(Session session, SessionState state, Exception
			// exception) {
			// System.out.println("占싹뤄옙 占쏙옙占쏙옙 占승댐옙");
			// if (session.isOpened()) {
			// System.out.println("占쏙옙占쏙옙 占쏙옙占싫댐옙占쏙옙");
			// // make request to the /me API
			// Request.newMeRequest(session, new Request.GraphUserCallback() {
			//
			// // callback after Graph API response with user object
			// @Override
			// public void onCompleted(GraphUser user, Response response) {
			// System.out.println("占실몌옙 占쏙옙占싱듸옙 占쏙옙占쏙옙占쏙옙占�");
			// if (user != null) {
			// TextView welcome = (TextView) findViewById(R.id.welcome);
			// welcome.setText(user.getName());
			// }
			// }
			// }).executeAsync();
			// } System.out.println("占싹뤄옙 占쏙옙占쏙옙 占승댐옙22");
			// }
			// });
			Intent intent = new Intent(LoginActivity.this, CoverActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.btnJoin) {
			System.out.println("Join");
			Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	/* 김준기
	 * login btn click시 호출되는 스레드
	 * 서버에 email, password 보내서 정보 요청함.
	 */
	class LoginTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("emailAddress", params[0]));
			nameValuePairs.add(new BasicNameValuePair("password", params[1]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/login.php");
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
				Log.e("result", result);

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			try {
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = null;

				json_data = jArray.getJSONObject(0);
				String nickname1 = json_data.getString("nick_name");
				Log.e("nickname", nickname1);

			} catch (JSONException e1) {
				Toast.makeText(getBaseContext(), "No", Toast.LENGTH_LONG)
						.show();
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
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

	}

}
