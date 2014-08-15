package sgen.sgen_pourney;

import sgen.sgen_pourney.R;
import sgen.sgen_pourney.R.layout;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class AskActivity extends Activity {
    
    private TextView e_mail;
    private TextView e_mail_address;
    private TextView call;
    private TextView call_info;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_layout);
        
        e_mail=(TextView)findViewById(R.id.e_mail);
        e_mail_address=(TextView)findViewById(R.id.e_mail_address);
        call=(TextView)findViewById(R.id.call);
        call_info=(TextView)findViewById(R.id.call_info);
        String fontpath="fonts/WalbaumBook-BoldItalic.otf";
        
        Typeface tf = Typeface.createFromAsset(getAssets(),
				fontpath);
		e_mail.setTypeface(tf);
		e_mail_address.setTypeface(tf);
		call.setTypeface(tf);
		call_info.setTypeface(tf);
		
    }

}
