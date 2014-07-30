package sgen.sgen_pourney;

import sgen.sgen_pourney.R;
import sgen.sgen_pourney.R.id;
import sgen.sgen_pourney.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


public class DrawerActivity extends Activity implements OnClickListener{
    
    protected void onCreate (Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_behind_drawer);
        
        
    }
   
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.ask_text){
		Intent intent = new Intent(DrawerActivity.this,AskActivity.class);
		startActivity(intent);
		}
		if(v.getId()==R.id.last_album_text){
			Intent intent = new Intent(DrawerActivity.this,CoverActivity.class);
			startActivity(intent);
		}

		
	}
}
