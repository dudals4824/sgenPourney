package sgen.sgen_pourney;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	Context context;
	String[] DayArray;
	ArrayList<CalendarListInfo> listitem;
	int firstdayofthismonth;
	Dayinfo today;
	String id;
	NumberFormat numberFormat = NumberFormat.getIntegerInstance();
	Project_info[] info;
	String month, year, date;
	int res_id;
	LayoutInflater inflator;
	TextView textview;
	ImageView gridflag;
	int startdate, enddate;
	private int selectedId;

	public CalendarAdapter(Context context, int grid, String[] dayArray,
			Dayinfo today) {
		// TODO Auto-generated constructor stub
		this.context = context;
		DayArray = dayArray;
		this.today = today;
		res_id = grid;
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public CalendarAdapter(Context context, int grid, String[] dayArray,
			Dayinfo today, int startdate, int enddate) {
		// TODO Auto-generated constructor stub
		this.context = context;
		DayArray = dayArray;
		this.today = today;
		res_id = grid;
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.startdate = startdate;
		this.enddate = enddate;
	}
	public CalendarAdapter(Context context, int grid, String[] dayArray,
			Dayinfo today, int startdate, int enddate, int position) {
		// TODO Auto-generated constructor stub
		this.context = context;
		DayArray = dayArray;
		this.today = today;
		res_id = grid;
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.startdate = startdate;
		this.enddate = enddate;
		selectedId= Integer.parseInt(Long.toString(getItemId(position)));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return DayArray.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		numberFormat.setMinimumIntegerDigits(2);
		month = numberFormat.format(today.month + 1);
		year = today.year + "";
		if ((position - today.firstdayofthismonth + 2) < 0)
			date = "00";
		else {
			date = numberFormat
					.format(position - today.firstdayofthismonth + 2);
		}
		id = year + month + date;
		return Integer.parseInt(id);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// // TODO Auto-generated method stub

		if (convertView == null) {
			convertView = inflator.inflate(res_id, parent, false);
		}
		//
		
		textview = (TextView) convertView.findViewById(R.id.textview_grid);
		textview.setText(DayArray[position]);
		if (startdate > 0) {
			if (startdate <= getItemId(position) && getItemId(position) <= enddate) {
				convertView.setBackgroundColor(Color.rgb(120, 192, 242));
				textview.setTextColor(Color.WHITE);
			}
		}
		if(getItemId(position)==selectedId&&selectedId!=0){
			Log.d("selected", selectedId+"");
			Log.d("getItemId(position)", getItemId(position)+"");
			convertView.setBackgroundColor(Color.rgb(120, 192, 242));
			textview.setTextColor(Color.WHITE);
		}
		textview.setGravity(Gravity.CENTER);
		convertView.setPadding(5, 5, 5, 5);
		if (today.lastdayofthismonth >= getItemId(position)) {
			Log.d("getItemId(position) : ", getItemId(position) + "");
			convertView.setClickable(false);
		}
		return convertView;
	}
}
