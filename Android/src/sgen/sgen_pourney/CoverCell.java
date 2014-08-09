package sgen.sgen_pourney;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoverCell extends LinearLayout {
	private TextView title,date,numberOfPeople,travelNumber;
	Context mContext = null;

	public CoverCell(Context context, int attrs) {
		super(context);
		initMarbleView(context,attrs);
	}

//	public CoverCell(Context context) {
//		super(context);
//		initMarbleView(context);
//	}
//
//	void initMarbleView(Context context) {
////		mContext = context;
////		String infService = Context.LAYOUT_INFLATER_SERVICE;
////		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
////				infService);
////		View v = li.inflate(R.layout.album_cover, this, false);
////		addView(v);
////		title = (TextView) findViewById(R.id.travelTitle);// 디비에서 해당 번째 앨범의
////		// 정보 불러와서 넣어주면
////		// 됩니다.
////		title.setText("집에가고싶다.");// 갯수만큼 돌리면서 변수 바꿔가면서 해야 할듯..
//	}
	
	void initMarbleView(Context context,int attrs) {
		System.out.println(attrs);
		mContext = context;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		String travelNum;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_cover, this, false);
		addView(v);
		title = (TextView) findViewById(R.id.travelTitle);
		date = (TextView) findViewById(R.id.dayBack);
		numberOfPeople = (TextView) findViewById(R.id.peopleBack);
		travelNumber = (TextView) findViewById(R.id.travelNumber);
		// 디비에서 해당 번째(attrs) 앨범의
		// 정보 불러와서 넣어주면
		// 됩니다.
		title.setText("집에가고싶다.");
		date.setText("2014.8.9~2014.8.11");
		numberOfPeople.setText("With N people");
		travelNum = Integer.toString(attrs);
		travelNumber.setText(travelNum);
		
	}
}
