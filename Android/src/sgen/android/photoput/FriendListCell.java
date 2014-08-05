package sgen.android.photoput;

import sgen.sgen_pourney.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class FriendListCell extends LinearLayout {
	Context mContext = null;

	public FriendListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMarbleView(context);
	}

	public FriendListCell(Context context) {
		super(context);
		initMarbleView(context);
	}

	void initMarbleView(Context context) {
		mContext = context;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.friend_list_cell, this, false);
		addView(v);
	}
}
