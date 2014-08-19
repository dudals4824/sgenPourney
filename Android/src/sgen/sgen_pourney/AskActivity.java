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
		
	}

//	private void facebookSharing() {
//
//		List<String> permissions = new ArrayList<String>();
//		permissions.add("email");
//		permissions.add("public_profile");
//		// 포스팅위한 퍼미션
//		Session.openActiveSession(this, true, permissions,
//				new Session.StatusCallback() {
//
//					// callback when session changes state
//					@Override
//					public void call(Session session, SessionState state,
//							Exception exception) {
//						if (session.isOpened()) {/* make the API call */
//							new Request(session, "/{post-id}", null,
//									HttpMethod.GET, new Request.Callback() {
//										public void onCompleted(
//												Response response) {
//											/* handle the result */
//										}
//									}).executeAsync();
//						}
//					}
//				});
//
//		Session session = Session.getActiveSession();
//		List<String> permission = session.getPermissions();
//		if (!permission.contains("publish_actions")) {
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
//							/* handle the result */
//						}
//					}).executeAsync();
//		}
//		// TODO Auto-generated method stub
//
//	}
}
