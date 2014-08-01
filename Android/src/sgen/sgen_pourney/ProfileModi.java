package sgen.sgen_pourney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ProfileModi extends Activity implements OnClickListener{
	private SimpleSideDrawer mDrawer;
	private TextView profileName;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_profile);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		profileName = (TextView)findViewById(R.id.profileName);
        profileName.setText("공민아입니다?");//여기 ""안에다가 사용자 이름 넣어주세요 
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backcardNew) {
			Intent intent = new Intent(ProfileModi.this,
					TravelInfoActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
			startActivity(intent);
		}
	}

}
