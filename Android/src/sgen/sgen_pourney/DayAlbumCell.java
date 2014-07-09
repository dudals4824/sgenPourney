package sgen.sgen_pourney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DayAlbumCell extends LinearLayout {
	TextView textDay,textPhotoNum ;
	ImageButton btnPhoto;
	Context mContext = null;
	static final int REQUEST_ALBUM = 1;
	static final int REQUEST_PICTURE = 2;


	public DayAlbumCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMarbleView(context);
		// TODO Auto-generated constructor stub
	}

	public DayAlbumCell(Context context) {
		super(context);
		initMarbleView(context);

		// TODO Auto-generated constructor stub
	}

	void initMarbleView(Context context) {

		mContext = context;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.dayalbum_cell, this, false);
		addView(v);
		textDay=(TextView)findViewById(R.id.textDay);
		textPhotoNum=(TextView)findViewById(R.id.textPhotoNum);
		btnPhoto=(ImageButton)findViewById(R.id.btnPhoto);
		
		btnPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				mContext.startActivity(
						Intent.createChooser(intent, "Select Picture"));
			
			}
		});
	}

}
