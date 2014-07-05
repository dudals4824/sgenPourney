package sgen.sgen_pourney;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Dayadapter extends BaseAdapter{
 String[] array={"SUN","MON","TUE","WED","THR","FRI","SAT"};
 Context context;
	public Dayadapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.length;
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
		TextView textview;
		if(convertView==null){
			textview=new TextView(context);
		}else{
			textview=(TextView)convertView;
		}
		textview.setTypeface(null, Typeface.BOLD);
		textview.setTextColor(Color.rgb(153, 153, 153));
		textview.setText(array[position]);
		textview.setGravity(Gravity.CENTER);
		textview.setPadding(5, 1, 5, 1);
		return textview;
	}

}
