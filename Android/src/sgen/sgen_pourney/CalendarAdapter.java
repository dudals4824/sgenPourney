package sgen.sgen_pourney;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.Context;
import android.graphics.Color;
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

	public CalendarAdapter(Context context, int grid, String[] dayArray,
			Dayinfo today) {
		// TODO Auto-generated constructor stub
		this.context = context;
		DayArray = dayArray;
		this.today = today;
		res_id = grid;
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("adapter", "캘린더어댑터");
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
		Log.d("adapter", "캘린더어댑터");
		this.startdate = startdate;
		this.enddate = enddate;
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
			// Log.d("getItemId", getItemId(position) + "");

			if (startdate <= getItemId(position)
					&& getItemId(position) <= enddate) {
				Log.d("getItemId", getItemId(position) + "");
				if (getItemId(position) == startdate
						|| getItemId(position) == enddate)
					convertView
							.setBackgroundResource(R.drawable.ic_numberput_84x84);
				convertView.setBackgroundColor(Color.rgb(120, 192, 242));

			}
		}
		
		textview.setGravity(Gravity.CENTER);
		convertView.setPadding(5, 5, 5, 5);
		return convertView;
	}

}
