package sgen.android.photoput;

import sgen.sgen_pourney.R;
import sgen.sgen_pourney.R.id;
import sgen.sgen_pourney.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DayAlbum extends LinearLayout {
	private TextView textDay, textPhotoNum;
	private ImageButton btnPhoto;
	private Context mContext = null;
	private GridLayout layoutGridPhotoAlbum;
	static final int SELECT_PICTURE = 1;
	static final int REQUEST_PICTURE = 2;
	private String mDate;

	public DayAlbum(Context context, int i, String date) {
		super(context);
		mDate=date;
		mContext = context;
		initMarbleView(i);
		// TODO Auto-generated constructor stub
	}

	private void initMarbleView(int i) {

		
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.dayalbum, this, false);
		addView(v);

		textDay = (TextView) findViewById(R.id.textDay);
		textPhotoNum = (TextView) findViewById(R.id.textPhotoNum);
		layoutGridPhotoAlbum = (GridLayout) findViewById(R.id.layoutGridPhotoAlbum);

		textDay.setText(mDate);
		
		layoutGridPhotoAlbum.addView(new AlbumImgBtnCell(mContext,i));
	}
	public void addLayoutGridalbum(AlbumImgCell albumImgCell){
		Log.d("Dayalbum", "addLayoutGridalbum called");
		layoutGridPhotoAlbum.addView(albumImgCell);
	}

}
