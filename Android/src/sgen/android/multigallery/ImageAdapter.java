package sgen.android.multigallery;

import java.util.ArrayList;

import sgen.sgen_pourney.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//아직 미완성임
import android.widget.CheckBox;
import android.widget.ImageView;

//아직 미완성임
public class ImageAdapter extends BaseAdapter {
	private int count;
	private Context mContext;
	private int mCellLayout;
	private LayoutInflater mInflater;
	private boolean[] mThumbnailsselection;
	private String[] mArrPath;
	private ArrayList<ThumbImageInfo> mThumbImageInfoList;
	private Bitmap[] mThumbnails;

	public ImageAdapter(Context context, boolean[] thumbnailsselection,
			String[] arrPath, Bitmap[] thumbnails) {
		// TODO Auto-generated constructor stub
		super();
		mContext = context;
		mThumbnailsselection = thumbnailsselection;
		mArrPath = arrPath;
		mThumbnails = thumbnails;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.galleryitem, null);
			holder.imageview = (ImageView) convertView
					.findViewById(R.id.thumbImage);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.itemCheckBox);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.checkbox.setId(position);
		holder.imageview.setId(position);
		holder.checkbox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				CheckBox cb = (CheckBox) v;
				int id = cb.getId();
				if (mThumbnailsselection[id]) {
					cb.setChecked(false);
					mThumbnailsselection[id] = false;
				} else {
					cb.setChecked(true);
					mThumbnailsselection[id] = true;
				}
			}
		});
		holder.imageview.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + mArrPath[id]),
						"image/*");
				((Activity) mContext).startActivity(intent);
			}
		});
		holder.imageview.setImageBitmap(mThumbnails[position]);
		holder.checkbox.setChecked(mThumbnailsselection[position]);
		holder.id = position;
		return convertView;
	}
}

class ViewHolder {
	ImageView imageview;
	CheckBox checkbox;
	int id;
}
