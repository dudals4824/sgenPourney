package sgen.android.photoput;

import sgen.android.multigallery.CustomGalleryActivity;
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
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumImgBtnCell extends RelativeLayout {
	private Context mContext = null;
	private ImageButton btnPhotoAdd, btnMemoWrite;
	static final int SELECT_PICTURE = 1;

	public AlbumImgBtnCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMarbleView(context);
		// TODO Auto-generated constructor stub
	}

	public AlbumImgBtnCell(Context context) {
		super(context);
		initMarbleView(context);

		// TODO Auto-generated constructor stub
	}

	void initMarbleView(Context context) {
		mContext = context;
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
				Intent intent = new Intent(mContext,
						CustomGalleryActivity.class);
				// intent.setType("image/*");
				// // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// ((Activity)
				// mContext).startActivityForResult(Intent.createChooser(intent,"Select Picture"),
				// SELECT_PICTURE);
				// //여기서 갤러리 액티비티로 넘어가게
				((Activity) mContext).startActivity(intent);

			}
		});
	}

}
