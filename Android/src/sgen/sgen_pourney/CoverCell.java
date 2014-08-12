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
import sgen.application.PourneyApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoverCell extends LinearLayout implements View.OnClickListener {
	private TextView title, date, numberOfPeople, travelNumber;
	Context mContext = null;
	private TripDTO tripDTO = new TripDTO();

	public CoverCell(Context context, int attrs) {
		super(context);
		initMarbleView(context, attrs);
	}

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

		String fontpath = "fonts/WalbaumBook-BoldItalic.otf";
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), fontpath);
		title.setTypeface(tf);
		date.setTypeface(tf);
		numberOfPeople.setTypeface(tf);
		travelNumber.setTypeface(tf);

		// trip information setting asynctask
		GetTripInfo getTripInfo = new GetTripInfo();
		getTripInfo.execute(String.valueOf(attrs));
		
		title.setOnClickListener(this);
		date.setOnClickListener(this);
		numberOfPeople.setOnClickListener(this);
		travelNumber.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// trip 정보 setting
		PourneyApplication Application = (PourneyApplication) ((Activity) mContext)
				.getApplication();
		Application.setSelectedTrip(tripDTO);
		Log.d("CoverCell_LOG", "onclick"
				+ Application.getSelectedTrip().getTripTitle());
	}

	public class GetTripInfo extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			InputStream is = null;
			StringBuilder sb = null;
			String result = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("trip_id", params[0]));
			Log.d("trip_Id", "trip id : " + params[0]);
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
			// travel information setting
			Log.d("settext", tripDTO.toString());
			title.setText(tripDTO.getTripTitle());
			date.setText(tripDTO.getStartDate() + "~" + tripDTO.getEndDate());
			numberOfPeople.setText("With N people");
			travelNumber.setText(String.valueOf(tripDTO.getTripId()));

		}
	}

}
