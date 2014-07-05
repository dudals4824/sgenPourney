//package sgen.sgen_pourney;
//
//
//import android.support.v4.app.Fragment;
//import android.os.Bundle;
//import android.os.Build;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.*;
//import android.widget.EditText;
//
//
//public class MainActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        	super.onCreate(savedInstanceState);
//        	setContentView(R.layout.activity_main);
//        	
//        }
//  
//
//
//    public void login(View e){
//    	EditText editText=(EditText)findViewById(R.id.editText1);
//    	String emailAddress = editText.getText().toString();
//    	EditText editText1=(EditText)findViewById(R.id.editText2);
//    	String password = editText1.getText().toString();
//    	Intent intent= new Intent(MainActivity.this, Login.class);
//        intent.putExtra("emailAddress", emailAddress);
//        intent.putExtra("password", password);
//    	startActivity(intent);
//    }
//    
//    public void regis(View e){
//    	Intent intent= new Intent(MainActivity.this,regis.class);
//    	startActivity(intent);
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//    	getMenuInflater().inflate(R.menu.main,menu);
//    	return true;
//    }
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//}
