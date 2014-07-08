package sgen.sgen_pourney;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;


public class CoverActivity extends Activity implements OnClickListener{

	private ImageButton btn_new_travel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_cover);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);		
		btn_new_travel=(ImageButton)findViewById(R.id.backcardNew);
		btn_new_travel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.backcardNew){
		Intent intent=new Intent(CoverActivity.this, TravelInfoActivity.class);
		startActivity(intent);// TODO Auto-generated method stub
		}
	}
}
