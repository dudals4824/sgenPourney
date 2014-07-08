package sgen.sgen_pourney;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Cover_cell extends LinearLayout {
	Context mContext = null;

	public Cover_cell(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMarbleView(context);
	}

	public Cover_cell(Context context) {
		super(context);
		initMarbleView(context);
	}

	void initMarbleView(Context context) {
		mContext = context;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_cover, this, false);
		addView(v);
	}
}
