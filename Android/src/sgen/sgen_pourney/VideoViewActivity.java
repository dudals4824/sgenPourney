package sgen.sgen_pourney;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.VideoView;

public class VideoViewActivity extends Activity implements MediaPlayerControl{

	final String videoUrl="http://54.178.166.213/final.mp4";
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_video_complete);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		
		VideoView videoView = (VideoView) findViewById(R.id.VideoView);
		//Use a media controller so that you can scroll the video contents
		//and also to pause, start the video.
		MediaController mediaController = new MediaController(this); 
		mediaController.setMediaPlayer(this);
		mediaController.setAnchorView((MediaController)findViewById(R.id.controller));
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
}
