package sgen.sgen_pourney;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CoverSelection extends Activity implements OnCheckedChangeListener{

	private RadioGroup coverRadioGroup;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.album_cover_select);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		
		coverRadioGroup = (RadioGroup) findViewById(R.id.cover_select_radioGrp);
		coverRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		int coverType=-1;
		switch (arg1) {
		case R.id.backcard1_radioBtn:
			coverType = 0;
			break;
		case R.id.backcard2_radioBtn:
			coverType = 1;
			break;
		case R.id.backcard3_radioBtn:
			coverType = 2;
			break;
		case R.id.backcard4_radioBtn:
			coverType = 3;
			break;
		default:
			coverType = -1;
			break;
		}
	}
}
