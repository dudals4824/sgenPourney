package sgen.android.photoput;

import java.io.File;

import sgen.image.resizer.ImageResize;
import sgen.image.resizer.ImageResizer;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumImgCell extends RelativeLayout{

	private Context mContext = null;
	private Bitmap mBitmap=null;
	private ImageView imgPhoto=null;
	private File mImgFile=null;
	public AlbumImgCell(Context context, Bitmap bitmap, File imgFile) {
		super(context);
		// TODO Auto-generated constructor stub
		initMarbleView(context,bitmap,imgFile);
	}
	private void initMarbleView(Context context, Bitmap bitmap, File imgFile) {
		mContext = context;
		mBitmap=bitmap;
		mImgFile=imgFile;
		Log.d("AlbumImgCell", mBitmap.toString());
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_img_cell, this, false);
		addView(v);
		//scaledBitmap = ImageResizer.resize(imgFile, 300, 300);
		mBitmap=ImageResizer.resize(mImgFile, 300, 300);
		//이미지를 비트맵으로 받아와서 이미지뷰에 추가 리사이징 해야함
		imgPhoto=(ImageView)findViewById(R.id.imgPhoto);
		imgPhoto.setImageBitmap(mBitmap);
		
		
	}

}
