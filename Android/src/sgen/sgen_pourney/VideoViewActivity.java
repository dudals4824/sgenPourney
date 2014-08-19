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

import sgen.DTO.TripDTO;
import sgen.DTO.UserDTO;
import sgen.application.PourneyApplication;
import sgen.common.PhotoEditor;
import sgen.session.UserSessionManager;
import sgen.sgen_pourney.LoginActivity.BackTask;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoViewActivity extends Activity implements MediaPlayerControl,
		OnClickListener {
	private String videoUrl = "http://54.178.166.213/video/video_";
	private Button askBtn, logoutBtn, albumBtn, profileBtn;
	private ImageButton fbShareBtn;
	private SimpleSideDrawer mDrawer;
	private TripDTO trip;
	UserSessionManager session;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_video_complete);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		fbShareBtn=(ImageButton)findViewById(R.id.facebook_sharing_btn);
		askBtn = (Button) findViewById(R.id.ask_text);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);
		askBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);
		fbShareBtn.setOnClickListener(this);
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();
			}
		});

		VideoView videoView = (VideoView) findViewById(R.id.VideoView);

		// Use a media controller so that you can scroll the video contents
		// and also to pause, start the video.

		PourneyApplication Application = (PourneyApplication) getApplication();
		trip = new TripDTO();
		trip = Application.getSelectedTrip();

		String trip_id = Integer.toString(trip.getTripId());
		videoUrl = videoUrl + trip_id + "/" + trip_id + ".mp4";
		Log.d("video url", videoUrl);

		MediaController mediaController = new MediaController(this);
		mediaController.setMediaPlayer(this);
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);
		videoView.setVideoURI(Uri.parse(videoUrl));
		videoView.start();

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void seekTo(int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.log_out_text) {
			Log.d("logout", "logout");
			session.logoutUser();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			// intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		} else if(v.getId() == R.id.facebook_sharing_btn){
			// 페북 공유
			facebookSharing();
			
		}
		
		else if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
			finish();
		}
	}
	private void facebookSharing() { // facebook login 占쏙옙 id
		
		List<String> permissions = new ArrayList<String>();
		permissions.add("email");
		permissions.add("public_profile");
		Session.openActiveSession(this, true, permissions,
				new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						if (session.isOpened()) {
							// make request to the /me API
							Request.newMeRequest(session,
									new Request.GraphUserCallback() {

										// callback after Graph API response
										// with
										// user object
										@Override
										public void onCompleted(
												GraphUser user,
												Response response) {
											if (user != null) {
												String nickName = user
														.getFirstName();
												System.out.println(nickName);
											
												String id = user.getId();
												String userId = id;
												System.out.println(userId);
												String email = user
														.getProperty(
																"email")
														.toString();
												Bitmap mIcon = null;
												//System.out.println(email);
												new BackTask().execute(mIcon);
												

												// finish();
												// Intent intent = new
												// Intent(
												// LoginActivity.this,
												// CoverActivity.class);
												// startActivity(intent); //액티비티넘기는거
											}
										}
									}).executeAsync();
						}
					}
				});

	
		
	
		Session session1 = Session.getActiveSession();
		List<String> permission1 = session1.getPermissions();
		if (!permission1.contains("publish_actions")) {
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
					this, Arrays.asList("publish_actions"))
					.setDefaultAudience(SessionDefaultAudience.EVERYONE);
			session1.requestNewPublishPermissions(newPermissionsRequest);
			/* make the API call */
			Bundle params = new Bundle();
			params.putString("message", "Trip to paris" + "   "
					+ "2014.06.14 ~ 2014.06.16");// 여기에 여행 제목이랑 날짜 넣어주면 될듯
			params.putString("name", "Minha" + "'s Journey Movie");
			// minha's journey movie 처럼 개인 이름 넣으면 됨 
			params.putString("link", "http://54.178.166.213/video/video_778/778.mp4");// 여기에 영상 주소 넣고
			params.putString("description", "..made by 'Pourney'");// 이건 우리 광고
			params.putString("icon",
					"http://54.178.166.213/video/video_763/i_logo.png");// 여기에
																		// icon
																		// 넣고
			/* make the API call */
			new Request(session1, "/me/feed", params, HttpMethod.POST,
					new Request.Callback() {
						public void onCompleted(Response response) {
							/* handle the result */
						}
					}).executeAsync();
		}
		// TODO Auto-generated method stub

	}
	public class BackTask extends AsyncTask<Bitmap, String, String> {

		@Override
		protected String doInBackground(Bitmap... args) {
			Bitmap mIcon = args[0];
			//ImageView icon_pic = (ImageView) findViewById(R.id.imgLogoimage);

			return null;
		}

		@Override
		protected void onPostExecute(String text) {
			//logo.setBackground(sPhoto);

		}

	}
}
