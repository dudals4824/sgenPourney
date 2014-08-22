package sgen.sgen_pourney;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CoverSelection extends Activity implements OnCheckedChangeListener{

	private RadioGroup coverRadioGroup;
	private ImageView imgviewCover;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.album_cover_select);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		imgviewCover=(ImageView)findViewById(R.id.imgviewCover);
		coverRadioGroup = (RadioGroup) findViewById(R.id.cover_select_radioGrp);
		coverRadioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		Log.d("checked", "ddd");
		int coverType=-1;
		switch (arg1) {
		case R.id.backcard1_radioBtn:
			coverType = 0;
			Log.d("backcard1", "1");
			imgviewCover.setImageResource(R.drawable.i_backcard_1);
			break;
		case R.id.backcard2_radioBtn:
			coverType = 1;
			imgviewCover.setImageResource(R.drawable.i_backcard_2);
			break;
		case R.id.backcard3_radioBtn:
			coverType = 2;
			imgviewCover.setImageResource(R.drawable.i_backcard_3);
			break;
		case R.id.backcard4_radioBtn:
			coverType = 3;
			imgviewCover.setImageResource(R.drawable.i_backcard_4);
			break;
		default:
			coverType = -1;
			break;
		}
	}
}
