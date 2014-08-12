package sgen.sgen_pourney;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sgen.DTO.TripDTO;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoverCell extends LinearLayout {
	private TextView title, date, numberOfPeople, travelNumber;
	Context mContext = null;
	private TripDTO tripDTO = new TripDTO();

	public CoverCell(Context context, int attrs) {
		super(context);
		initMarbleView(context, attrs);
	}

	// public CoverCell(Context context) {
	// super(context);
	// initMarbleView(context);
	// }
	//
	// void initMarbleView(Context context) {
	// // mContext = context;
	// // String infService = Context.LAYOUT_INFLATER_SERVICE;
	// // LayoutInflater li = (LayoutInflater) getContext().getSystemService(
	// // infService);
	// // View v = li.inflate(R.layout.album_cover, this, false);
	// // addView(v);
	// // title = (TextView) findViewById(R.id.travelTitle);// 디비에서 해당 번째 앨범의
	// // // 정보 불러와서 넣어주면
	// // // 됩니다.
	// // title.setText("집에가고싶다.");// 갯수만큼 돌리면서 변수 바꿔가면서 해야 할듯..
	// }

	void initMarbleView(Context context, int attrs) {
		// System.out.println(attrs);
		mContext = context;
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.album_cover, this, false);
		addView(v);
		title = (TextView) findViewById(R.id.travelTitle);
		date = (TextView) findViewById(R.id.dayBack);
		numberOfPeople = (TextView) findViewById(R.id.peopleBack);
		travelNumber = (TextView) findViewById(R.id.travelNumber);
		
		// trip information setting asynctask
		GetTripInfo getTripInfo = new GetTripInfo();
		getTripInfo.execute(String.valueOf(attrs));
	}

	public class GetTripInfo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", params[0]));
			Log.d("trip_Id", "trip id : "+ params[0]);
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://54.178.166.213/getCoverInfo.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "error in http connection" + e.toString());
			}
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");
				String line = "0";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString().trim();
				Log.e("log_tag", result);

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}
			try {
				JSONArray JsonArray = new JSONArray(result);
				JSONObject JsonObject = JsonArray.getJSONObject(0);
				tripDTO.setTripId(JsonObject.getInt("trip_id"));
				tripDTO.setTripTitle(JsonObject.getString("trip_name"));
				tripDTO.setStartDate(JsonObject.getInt("start_date"));
				tripDTO.setEndDate(JsonObject.getInt("end_date"));

			} catch (JSONException e1) {
				Log.e("log_msg", e1.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//travel information setting
			Log.d("settext", tripDTO.toString());
			title.setText(tripDTO.getTripTitle());
			date.setText(tripDTO.getStartDate()+"~"+tripDTO.getEndDate());
			numberOfPeople.setText("With N people");
			travelNumber.setText(String.valueOf(tripDTO.getTripId()));
		}
	}

}
