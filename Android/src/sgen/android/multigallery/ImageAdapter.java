package sgen.android.multigallery;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//아직 미완성임
public class ImageAdapter extends BaseAdapter{
	private Context mContext;
	private int mCellLayout;
	private LayoutInflater mLiInflater;
	private ArrayList<ThumbImageInfo> mThumbImageInfoList;
	
	public ImageAdapter(Context context, int cellLayout,
			ArrayList<ThumbImageInfo> thumbImageInfoList) {
		super();
		mContext = context;
		mCellLayout = cellLayout;
		mThumbImageInfoList = thumbImageInfoList;
		
		mLiInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ImageAdapter(Context context) {
		// TODO Auto-generated constructor stub
		super();
		mContext=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbImageInfoList.size();
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
		return null;
	}}
