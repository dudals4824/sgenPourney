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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.View.OnClickListener;


public class CoverActivity extends Activity implements OnClickListener {

	private Button btn_temp;
	private ProfilePictureView profilePicture;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cover);
		
		btn_temp=(Button)findViewById(R.id.btn_temp);
		btn_temp.setOnClickListener(this);
	    Session.openActiveSession(this, true, new Session.StatusCallback() {//facebook login
			
	        // callback when session changes state
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
				System.out.println("세션확인");
	        	if (session.isOpened()) {
	  			System.out.println("열렸으면");
	            // make request to the /me API
	            Request.newMeRequest(session, new Request.GraphUserCallback() {
	    			
	              // callback after Graph API response with user object
	              @Override
	              public void onCompleted(GraphUser user, Response response) {
	      			System.out.println("되면");
	                if (user != null) {
	                	
	                	profilePicture = (ProfilePictureView) findViewById(R.id.profilePicture);
	                    profilePicture.setProfileId(user.getId());
	                }
	              }
	            }).executeAsync();
	          }
	        }
	      });		
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btn_temp){
			System.out.println("login");
			Intent intent=new Intent(CoverActivity.this, TravelInfoActivity.class);
			startActivity(intent);
		}else;
	}
}
