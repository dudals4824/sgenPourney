package sgen.sgen_pourney;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import sgen.common.PhotoEditor;
import sgen.sgen_pourney.R;
import sgen.sgen_pourney.LoginActivity.BackTask;
import sgen.sgen_pourney.R.layout;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AskActivity extends Activity {

	private TextView e_mail;
	private TextView e_mail_address;
	private TextView call;
	private TextView call_info;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ask_layout);

		e_mail = (TextView) findViewById(R.id.e_mail);
		e_mail_address = (TextView) findViewById(R.id.e_mail_address);
		call = (TextView) findViewById(R.id.call);
		call_info = (TextView) findViewById(R.id.call_info);
		String fontpath = "fonts/WalbaumBook-BoldItalic.otf";

		Typeface tf = Typeface.createFromAsset(getAssets(), fontpath);
		e_mail.setTypeface(tf);
		e_mail_address.setTypeface(tf);
		call.setTypeface(tf);
		call_info.setTypeface(tf);
		
//		facebookSharing();
	}

//	private void facebookSharing() {
//		System.out.println("좆같다");
//		List<String> permissions = new ArrayList<String>();
//		permissions.add("email");
//		permissions.add("public_profile");
//	
//		Session.openActiveSession(this, true, permissions,
//				new Session.StatusCallback() {
//
//					// callback when session changes state
//					@Override
//					public void call(Session session, SessionState state,
//							Exception exception) {
//						if (session.isOpened()) {
//							// make request to the /me API
//							Request.newMeRequest(session,
//									new Request.GraphUserCallback() {
//
//										// callback after Graph API response
//										// with
//										// user object
//										@Override
//										public void onCompleted(
//												GraphUser user,
//												Response response) {
//											if (user != null) {
//Bitmap mIcon = null;
//												String nickName = user.getFirstName();
//												System.out.println(nickName);
////											
////												String id = user.getId();
////												userId = id;
////												//System.out.println(userId);
////												email = user
////														.getProperty(
////																"email")
////														.toString();
////												//System.out.println(email);
//												new BackTask().execute(mIcon);
//												
//
//												// finish();
//												// Intent intent = new
//												// Intent(
//												// LoginActivity.this,
//												// CoverActivity.class);
//												// startActivity(intent); //액티비티넘기는거
//											}
//										}
//									}).executeAsync();
//						}
//					}
//				});
//
//		Session session = Session.getActiveSession();
//		List<String> permission = session.getPermissions();
//		if (!permission.contains("publish_actions")) {//퍼미션 추가
//			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
//					this, Arrays.asList("publish_actions"))
//					.setDefaultAudience(SessionDefaultAudience.EVERYONE);
//			session.requestNewPublishPermissions(newPermissionsRequest);
//			/* make the API call */
//			Bundle params = new Bundle();
//			params.putString("message", "Trip to paris" + "   "
//					+ "2014.06.14 ~ 2014.06.16");// 여기에 여행 제목이랑 날짜 넣어주면 될듯
//			params.putString("name", "Minha" + "'s Journey Movie");
//			// minha's journey movie 처럼 개인 이름 넣으면 됨 
//			params.putString("link", "http://54.178.166.213/video/video_778/778.mp4");// 여기에 영상 주소 넣고
//			params.putString("description", "..made by 'Pourney'");// 이건 우리 광고
//			params.putString("icon",
//					"http://54.178.166.213/video/video_763/i_logo.png");// 여기에
//																		// icon
//																		// 넣고
//			/* make the API call */
//			new Request(session, "/me/feed", params, HttpMethod.POST,
//					new Request.Callback() {
//						public void onCompleted(Response response) {
//							System.out.println("TLqkf");
//							/* handle the result */
//						}
//					}).executeAsync();
//		}else if(permission.contains("publish_actions")){
//			/* make the API call */
//			Bundle params = new Bundle();
//			params.putString("message", "Trip to paris" + "   "
//					+ "2014.06.14 ~ 2014.06.16");// 여기에 여행 제목이랑 날짜 넣어주면 될듯
//			params.putString("name", "Minha" + "'s Journey Movie");
//			// minha's journey movie 처럼 개인 이름 넣으면 됨 
//			params.putString("link", "http://54.178.166.213/video/video_778/778.mp4");// 여기에 영상 주소 넣고
//			params.putString("description", "..made by 'Pourney'");// 이건 우리 광고
//			params.putString("icon",
//					"http://54.178.166.213/video/video_763/i_logo.png");// 여기에
//																		// icon
//																		// 넣고
//			/* make the API call */
//			new Request(session, "/me/feed", params, HttpMethod.POST,
//					new Request.Callback() {
//						public void onCompleted(Response response) {
//							System.out.println("TLqkf");
//							/* handle the result */
//						}
//					}).executeAsync();
//			
//		}
//		// TODO Auto-generated method stub
//
//	}
//	public class BackTask extends AsyncTask<Bitmap, String, String> {
//
//		@Override
//		protected String doInBackground(Bitmap... args) {
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String text) {
//			//logo.setBackground(sPhoto);
//
//		}
//
//	}
}
