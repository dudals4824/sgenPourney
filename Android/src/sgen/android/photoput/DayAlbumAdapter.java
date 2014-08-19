package sgen.android.photoput;

import sgen.sgen_pourney.R;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class DayAlbumAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.album_img_cell, null);
			holder=new ViewHolder();
			holder.imgQueue=(ImageView)convertView.findViewById(R.id.imgPhoto);
			convertView.setTag(holder);
			
			
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.imgQueue.setTag(position);
		
	//	imageLoader.displayImage("filr://"+Data.getContactLookupUri(resolver, dataUri)
			
		return convertView;
	}
	public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
	}

}
