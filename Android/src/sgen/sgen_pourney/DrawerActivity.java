package sgen.sgen_pourney;

import sgen.sgen_pourney.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class DrawerActivity extends Activity implements OnClickListener {
	private ImageButton btnProfilePhoto;
	private Button btnAlbums, btnProfile, btnLogout, btnAsk;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.left_behind_drawer);

		// layout initializing
		btnProfilePhoto.findViewById(R.id.btnForProfilePhoto);
		btnAlbums.findViewById(R.id.last_album_text);
		btnProfile.findViewById(R.id.profile_modifying_text);
		btnLogout.findViewById(R.id.log_out_text);
		btnAsk.findViewById(R.id.ask_text);

		btnProfilePhoto.setOnClickListener(this);
		btnAlbums.setOnClickListener(this);
		btnProfile.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnAsk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.e("????", "what");
		switch (v.getId()) {
		case R.id.btnForProfilePhoto:
			Log.e("drawer", "profile button");
			break;
		case R.id.last_album_text:
			Log.e("drawer", "album button");
			Intent intent = new Intent(DrawerActivity.this, CoverActivity.class);
			startActivity(intent);
			break;
		case R.id.profile_modifying_text:
			Log.e("drawer", "profile modify button");
			break;
		case R.id.log_out_text:
			Log.e("drawer", "logout button");
			break;
		case R.id.ask_text:
			Log.e("drawer", "ask button");
			Intent intent2 = new Intent(DrawerActivity.this, AskActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
}
