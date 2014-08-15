package sgen.android.photoput;

import sgen.android.multigallery.Action;
import sgen.android.multigallery.CustomGalleryActivity;
import sgen.sgen_pourney.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class AlbumImgBtnCell extends RelativeLayout {
	private Context mContext = null;
	private ImageButton btnPhotoAdd, btnMemoWrite;
	static final int SELECT_PICTURE = 1;
	private int mIntent_date;

	public AlbumImgBtnCell(Context context, int intent_date) {
		super(context);
		initMarbleView(context,intent_date);
		// TODO Auto-generated constructor stub
	}

	public AlbumImgBtnCell(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
	}

	void initMarbleView(Context context, int intent_date) {
		mContext = context;
		mIntent_date=intent_date;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_imgbtn_cell, this, false);
		addView(v);

		btnMemoWrite = (ImageButton) findViewById(R.id.btnMemoWrite);
		btnPhotoAdd = (ImageButton) findViewById(R.id.btnPhotoAdd);

		btnPhotoAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				i.putExtra("i_dayalbum",mIntent_date);
				((Activity) mContext).startActivityForResult(i, 200);

			}
		});
	}

}
