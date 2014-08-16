package sgen.android.photoput;

import java.io.File;

import sgen.image.resizer.ImageResize;
import sgen.image.resizer.ImageResizer;
import sgen.image.resizer.ResizeMode;
import sgen.sgen_pourney.R;
import sgen.sgen_pourney.R.id;
import sgen.sgen_pourney.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumImgCell extends RelativeLayout implements
		View.OnClickListener {

	private Context mContext = null;
	private Bitmap mBitmap = null;
	private ImageView imgPhoto = null;
	private File mImgFile = null;
	private PopupWindow memoPopupWindow;
	private TextView date;
	public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";

	private CheckBox checkImage;
	public AlbumImgCell(Context context, Bitmap bitmap) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		mBitmap = bitmap;
		initMarbleView(context, bitmap);
	}

	private void initMarbleView(Context context, Bitmap bitmap) {

		date = (TextView) findViewById(R.id.textCalendar); // 여행 날짜

		// Log.d("AlbumImgCell", mBitmap.toString());

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_img_cell, this, false);
		addView(v);
		
		checkImage=(CheckBox) v.findViewById(R.id.checkImage);
		
		mBitmap=ImageResize.resize(mBitmap, 300, 300, ResizeMode.AUTOMATIC);
		// scaledBitmap = ImageResizer.resize(imgFile, 300, 300);
		mBitmap = ImageResize.resize(mBitmap, 300, 300, ResizeMode.AUTOMATIC);


		// mBitmap=ImageResizer.resize(mImgFile, 300, 300);
		// 이미지를 비트맵으로 받아와서 이미지뷰에 추가 리사이징 해야함
		imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
		imgPhoto.setImageBitmap(mBitmap);

		imgPhoto.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imgPhoto) {
			System.out.println("아오 짜증나");
		}
		// 메모 불러와야 될 건 photoput activity 에 memooutput 으로 검색 ㄱㄱ
	}

	// TODO Auto-generated method stub
	private Activity getBaseContext() {
		Context mBase = null;
		return (Activity) mBase;

	}

}
