package sgen.sgen_pourney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumImgCell extends RelativeLayout{
	private Context mContext = null;
	private ImageButton btnPhotoAdd, btnMemoWrite;
	static final int SELECT_PICTURE = 1;

	public AlbumImgCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMarbleView(context);
		// TODO Auto-generated constructor stub
	}

	public AlbumImgCell(Context context) {
		super(context);
		initMarbleView(context);

		// TODO Auto-generated constructor stub
	}
 
	void initMarbleView(Context context) {
		mContext = context;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_img_cell, this, false);
		addView(v);
		
		btnMemoWrite=(ImageButton)findViewById(R.id.btnMemoWrite);
		btnPhotoAdd=(ImageButton)findViewById(R.id.btnPhotoAdd);
		
		btnPhotoAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				((Activity) mContext).startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
			
			}
		});
	}


}
