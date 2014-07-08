package sgen.sgen_pourney;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoverCellNew extends LinearLayout {
	Context mContext = null;

	public CoverCellNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMarbleView(context);
	}

	public CoverCellNew(Context context) {
		super(context);
		initMarbleView(context);
	}

	void initMarbleView(Context context) {
		mContext = context;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_cover_new, this, false);
		addView(v);
	}
}
