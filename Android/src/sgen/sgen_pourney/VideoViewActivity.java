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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
	private ImageButton fbShareBtn, playBtn, pauseBtn, stretchBtn;
	private SimpleSideDrawer mDrawer;
	private TripDTO trip;
	private VideoView videoView;
	UserSessionManager session;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_video_complete);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		fbShareBtn = (ImageButton) findViewById(R.id.facebook_sharing_btn);
		askBtn = (Button) findViewById(R.id.ask_text);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);
		playBtn = (ImageButton) findViewById(R.id.playBtn);
		pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
		videoView=(VideoView)findViewById(R.id.VideoView);

		askBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		albumBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);
		pauseBtn.setOnClickListener(this);
		fbShareBtn.setOnClickListener(this);
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();
			}
		});

		

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
		videoView.setVideoURI(Uri.parse(videoUrl));
		videoView.pause();
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.playBtn)
					videoView.start();
				playBtn.setVisibility(View.GONE);

			}
		});

		
		if (videoView.isPlaying()) {
			System.out.println("재생중임!");
			videoView.setOnTouchListener(new OnTouchListener() {
				
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					if(v.getId()==R.id.VideoView)
					{
						System.out.println(event.getActionIndex());
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							pauseBtn.setVisibility(View.VISIBLE);
							break;

						default:
							break;
						}
					}
					return false;
				}
			});
		if(pauseBtn.getVisibility()==View.VISIBLE){
			try {
				wait(3000);
				pauseBtn.setVisibility(View.GONE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		}
			
			
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

		} else if (v.getId() == R.id.facebook_sharing_btn) {
			// 페북 공유
			facebookSharing();

		}

		else if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
			finish();
		}
		else if(v.getId()==R.id.pauseBtn){
	
					videoView.pause();
		
		}
	
	}

	private void facebookSharing() {
		System.out.println("좆같다");
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
										public void onCompleted(GraphUser user,
												Response response) {
											if (user != null) {
Bitmap mIcon = null;
												String nickName = user.getFirstName();
												System.out.println(nickName);
//											
//												String id = user.getId();
//												userId = id;
//												//System.out.println(userId);
//												email = user
//														.getProperty(
//																"email")
//														.toString();
//												//System.out.println(email);
												new BackTask().execute(mIcon);
												

												// finish();
												// Intent intent = new
												// Intent(
												// LoginActivity.this,
												// CoverActivity.class);
												// startActivity(intent);
												// //액티비티넘기는거
											}
										}
									}).executeAsync();
						}
					}
				});

		Session session = Session.getActiveSession();
		List<String> permission = session.getPermissions();
		if (!permission.contains("publish_actions")) {//퍼미션 추가
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
					this, Arrays.asList("publish_actions"))
					.setDefaultAudience(SessionDefaultAudience.EVERYONE);
			session.requestNewPublishPermissions(newPermissionsRequest);
			/* make the API call */
			Bundle params = new Bundle();
			params.putString("message", "Trip to paris" + "   "
					+ "2014.06.14 ~ 2014.06.16");// 여기에 여행 제목이랑 날짜 넣어주면 될듯
			params.putString("name", "Minha" + "'s Journey Movie");
			// minha's journey movie 처럼 개인 이름 넣으면 됨
			params.putString("link",
					"http://54.178.166.213/video/video_778/778.mp4");// 여기에 영상
																		// 주소 넣고
			params.putString("description", "..made by 'Pourney'");// 이건 우리 광고
			params.putString("icon",
					"http://54.178.166.213/video/video_763/i_logo.png");// 여기에
																		// icon
																		// 넣고
			/* make the API call */
			new Request(session, "/me/feed", params, HttpMethod.POST,
					new Request.Callback() {
						public void onCompleted(Response response) {
							System.out.println("TLqkf");
							/* handle the result */
						}
					}).executeAsync();
		}else if(permission.contains("publish_actions")){
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
			new Request(session, "/me/feed", params, HttpMethod.POST,
					new Request.Callback() {
						public void onCompleted(Response response) {
							System.out.println("TLqkf");
							/* handle the result */
						}
					}).executeAsync();
			
		}
		// TODO Auto-generated method stub

	}

	public class BackTask extends AsyncTask<Bitmap, String, String> {

		@Override
		protected String doInBackground(Bitmap... args) {

			return null;
		}

		@Override
		protected void onPostExecute(String text) {
			//logo.setBackground(sPhoto);

		}

	}
}
