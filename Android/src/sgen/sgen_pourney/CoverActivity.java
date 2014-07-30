package sgen.sgen_pourney;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView; //占쎌꼶�욘에�볥┸占쏙옙占쎄쑴�귨옙占�
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class CoverActivity extends Activity implements OnClickListener {

	private GridLayout layout_cover;
	private ImageButton btn_new_travel;
	private SimpleSideDrawer mDrawer;
	private Button askBtn;
	private Button logoutBtn;
	private Button albumBtn;
	long m_startTime;
	long m_endTime;
	boolean m_isPressedBackButton;

	// CoverCell marble=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_cover);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		// marble=(Cover_cell)findViewById(R.id.box1);
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});
		
		m_startTime = System.currentTimeMillis();
		layout_cover = (GridLayout) findViewById(R.id.layout_cover);
		layout_cover.addView(new CoverCell(this));
		layout_cover.addView(new CoverCell(this));
		layout_cover.addView(new CoverCell(this));// 커버 수만큼 출력
													// 맨뒤에 생길거
		layout_cover.addView(new CoverCellNew(this));
		// layout_cover_new = (GridLayout)findViewById(R.id.layout_cover_new);
		// layout_cover_new.addView(new Cover_cell_new(this));
		// 椰꾬옙域밸챶�곻옙遺쎄탢占쏙옙筌〓㈇�э옙占퐉able

		btn_new_travel = (ImageButton) findViewById(R.id.backcardNew);
		btn_new_travel.setOnClickListener(this);
		askBtn = (Button) findViewById(R.id.ask_text);
		askBtn.setOnClickListener(this);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		logoutBtn.setOnClickListener(this);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		albumBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.backcardNew) {
			Intent intent = new Intent(CoverActivity.this,
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
	}

	public void onBackPressed() {
		m_endTime = System.currentTimeMillis();

		if (m_endTime - m_startTime > 2000)
			m_isPressedBackButton = false;

		if (m_isPressedBackButton == false) {	
			m_isPressedBackButton = true;

			m_startTime = System.currentTimeMillis();

			Toast.makeText(this,
					"'뒤로'버튼을 한번 더 누르시면 종료됩니다.",
					Toast.LENGTH_SHORT).show();
		} else {
			finish();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
