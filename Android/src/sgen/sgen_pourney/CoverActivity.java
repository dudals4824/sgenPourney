package sgen.sgen_pourney;

import segn.Drawer.AskActivity;
import segn.Drawer.MainActivity;
import segn.Drawer.SimpleSideDrawer;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView; //�섎턿濡쒓릿���꾩슂��

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


public class CoverActivity extends Activity implements OnClickListener{
	
	private GridLayout layout_cover;
	private ImageButton btn_new_travel;
	private ImageButton btnMenu;
	private SimpleSideDrawer mDrawer;
	long m_startTime;       
	long m_endTime;
	boolean m_isPressedBackButton;
	//CoverCell marble=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_cover);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);		
		//marble=(Cover_cell)findViewById(R.id.box1);
	    mDrawer=new SimpleSideDrawer(this);
        mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
        findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();
			
			}
		});
        findViewById(R.id.ask_text).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CoverActivity.this, AskActivity.class);
				startActivity(intent);
			}
		});
		m_startTime=System.currentTimeMillis();
		layout_cover  = (GridLayout)findViewById(R.id.layout_cover);
		layout_cover.addView(new CoverCell(this));
		layout_cover.addView(new CoverCell(this));
		layout_cover.addView(new CoverCell(this));//�⑤쾾 媛�닔留뚰겮 �щЦ �뚮━硫����섏쨷��
		layout_cover.addView(new CoverCellNew(this));
//		layout_cover_new  = (GridLayout)findViewById(R.id.layout_cover_new);
//		layout_cover_new.addView(new Cover_cell_new(this));
//      嫄�洹몃━�붽굅��李멸퀬��mable 		
		
		btn_new_travel=(ImageButton)findViewById(R.id.backcardNew);
		btn_new_travel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.backcardNew){
		Intent intent=new Intent(CoverActivity.this, TravelInfoActivity.class);
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
	   
	        Toast.makeText(this, "'�ㅻ줈'踰꾪듉���쒕쾲 ���꾨Ⅴ�쒕㈃ 醫낅즺�⑸땲��", Toast.LENGTH_SHORT).show();
	    }
	    else {
	        finish();
	        System.exit(0);
	        android.os.Process.killProcess(android.os.Process.myPid());
	    }
	}
}
