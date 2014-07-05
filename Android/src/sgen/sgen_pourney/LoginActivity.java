package sgen.sgen_pourney;

import sgen.sgen_pourney.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.model.*;


public class LoginActivity extends Activity implements OnClickListener{

	private ImageButton btnLogin;
	private ImageButton btnFacebook, btnJoin;
	private EditText editEmailaddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		btnLogin=(ImageButton)findViewById(R.id.btnLogin);
		btnFacebook=(ImageButton)findViewById(R.id.btnFacebook);
		btnJoin=(ImageButton)findViewById(R.id.btnJoin);
		editEmailaddress=(EditText)findViewById(R.id.editEmailaddress);
		btnLogin.setOnClickListener(this);
		btnFacebook.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btnLogin){
			System.out.println("login");
			Intent intent=new Intent(LoginActivity.this, TravelInfoActivity.class);
			startActivity(intent);
		}else if(v.getId()==R.id.editEmailaddress){
			editEmailaddress.setBackgroundResource(R.drawable.i_emailaddress_put);
		}else if(v.getId()==R.id.btnFacebook){ //facebook login 占쏙옙 id 占쏙옙占�
//			System.out.println("占싹뤄옙 占쏙옙占쏙옙 占승댐옙");
//		    Session.openActiveSession(this, true, new Session.StatusCallback() {
//				
//		        // callback when session changes state
//		        @Override
//		        public void call(Session session, SessionState state, Exception exception) {
//					System.out.println("占싹뤄옙 占쏙옙占쏙옙 占승댐옙");
//		        	if (session.isOpened()) {
//		  			System.out.println("占쏙옙占쏙옙 占쏙옙占싫댐옙占쏙옙");
//		            // make request to the /me API
//		            Request.newMeRequest(session, new Request.GraphUserCallback() {
//		    			
//		              // callback after Graph API response with user object
//		              @Override
//		              public void onCompleted(GraphUser user, Response response) {
//		      			System.out.println("占실몌옙 占쏙옙占싱듸옙 占쏙옙占쏙옙占쏙옙占�");
//		                if (user != null) {
//		                    TextView welcome = (TextView) findViewById(R.id.welcome);
//		                    welcome.setText(user.getName());
//		                }
//		              }
//		            }).executeAsync();
//		          }			System.out.println("占싹뤄옙 占쏙옙占쏙옙 占승댐옙22");
//		        }
//		      });
			Intent intent=new Intent(LoginActivity.this, CoverActivity.class);
			startActivity(intent);
		}
			else if(v.getId()==R.id.btnJoin){
			System.out.println("Join");
			Intent intent=new Intent(LoginActivity.this, JoinActivity.class);
			startActivity(intent);
		}
	}

	  @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }

}
