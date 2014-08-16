package sgen.android.photoput;

import java.util.ArrayList;

import sgen.sgen_pourney.R;
import sgen.sgen_pourney.R.id;
import sgen.sgen_pourney.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
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
		mDate = date;
		mContext = context;

		initMarbleView(i);
		// TODO Auto-generated constructor stub
	}

	private void initMarbleView(int intent_date) {

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.dayalbum, this, false);
		addView(v);
		textDay = (TextView) findViewById(R.id.textDay);
		textPhotoNum = (TextView) findViewById(R.id.textPhotoNum);
		layoutGridPhotoAlbum = (GridLayout) findViewById(R.id.layoutGridPhotoAlbum);

		textDay.setText(mDate);
		
		layoutGridPhotoAlbum
				.addView(new AlbumImgBtnCell(mContext, intent_date));
	}

	public void addLayoutGridalbum(AlbumImgCell albumImgCell) {
		Log.d("Dayalbum", "addLayoutGridalbum called");
		layoutGridPhotoAlbum.addView(albumImgCell);
	}

	public ArrayList<String> getCheckedImageArray() {
		ArrayList<String> checkedList = new ArrayList<String>();

		for (int i = 1; i < layoutGridPhotoAlbum.getChildCount(); i++) {
				View v=(View)layoutGridPhotoAlbum.getChildAt(i);
				CheckBox checked;
				checked = (CheckBox) v.findViewById(R.id.checkImage);
				if (isCheckedImage(checked))
					checkedList.add(1 + "");
				else
					checkedList.add(0+"");
		}
		return checkedList;
	}

	public Boolean isCheckedImage(CheckBox checked) {
		if (checked.isChecked())
			return true;
		else
			return false;
	}
	
	public int getSizeOfDayAlbum(){
		return layoutGridPhotoAlbum.getChildCount()-1;
	}

}
