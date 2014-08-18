package sgen.android.photoput;

import sgen.android.multigallery.GalleryAdapter.ViewHolder;
import sgen.sgen_pourney.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DayAlbumAdapter extends BaseAdapter {
	private LayoutInflater inflater;

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
			
		}
		return convertView;
	}

}
