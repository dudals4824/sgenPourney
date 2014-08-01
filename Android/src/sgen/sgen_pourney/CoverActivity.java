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
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class CoverActivity extends Activity implements OnClickListener {

	int numberOfCover = 3; // 디비에서 개인의 커버 갯수 받아와서 저장해주세요
	private TextView title, date, people;
	private TextView profileName;
	private GridLayout layout_cover;
	private ImageButton btn_new_travel;
	private SimpleSideDrawer mDrawer;
	private Button askBtn;
	private Button logoutBtn;
	private Button albumBtn;
	private Button profileBtn;
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

		// 여기부터 drawer
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		profileName = (TextView) findViewById(R.id.profileName);
		profileName.setText("공민아입니다?");// 여기 ""안에다가 사용자 이름 넣어주세요

		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});

		m_startTime = System.currentTimeMillis();
		layout_cover = (GridLayout) findViewById(R.id.layout_cover);
		for (int i = 0; i < numberOfCover; i++) {// 커버 갯수만큼 나타나게 해주는 거임
			layout_cover.addView(new CoverCell(this));
			title = (TextView) findViewById(R.id.travelTitle);// 디비에서 해당 번째 앨범의
																// 정보 불러와서 넣어주면
																// 됩니다.
			title.setText("집에가고싶다.");// 갯수만큼 돌리면서 변수 바꿔가면서 해야 할듯..
			// date = (TextView)findViewById(R.id.dayBack);
			// date.setText("");
			// people = (TextView)findViewById(R.id.peopleBack);
			// people.setText("");
		}
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
		profileBtn = (Button) findViewById(R.id.profile_modifying_text);
		profileBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnForProfilePhoto) {
			
		}
		else if (v.getId() == R.id.backcardNew) {
			Intent intent = new Intent(CoverActivity.this,
					TravelInfoActivity.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		else if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);
			startActivity(intent);
		}
		else if (v.getId() == R.id.profile_modifying_text) {
			Intent intent = new Intent(this, ProfileModi.class);
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

			Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
					.show();
		} else {
			finish();
			System.exit(0);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
