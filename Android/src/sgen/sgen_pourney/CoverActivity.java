package sgen.sgen_pourney;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView; //페북로긴에 필요함

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
import android.view.View.OnClickListener;


public class CoverActivity extends Activity implements OnClickListener{
	
	private GridLayout layout_cover;
	private ImageButton btn_new_travel;
	//CoverCell marble=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_cover);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);		
		//marble=(Cover_cell)findViewById(R.id.box1);

		layout_cover  = (GridLayout)findViewById(R.id.layout_cover);
		layout_cover.addView(new CoverCell(this));
		layout_cover.addView(new CoverCell(this));
		layout_cover.addView(new CoverCell(this));//앨범 갯수만큼 포문 돌리면 됨 나중에
		layout_cover.addView(new CoverCellNew(this));
//		layout_cover_new  = (GridLayout)findViewById(R.id.layout_cover_new);
//		layout_cover_new.addView(new Cover_cell_new(this));
//      걍 그리는거임 참고용 mable 		
		
		btn_new_travel=(ImageButton)findViewById(R.id.backcardNew);
		btn_new_travel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.backcardNew){
		Intent intent=new Intent(CoverActivity.this, VideoMakingActivity.class);
		startActivity(intent);
		}
	}
}
