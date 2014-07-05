package sgen.sgen_pourney;

import android.os.Parcel;
import android.os.Parcelable;

public class Project_info implements Parcelable {

	private String project_name, place, date, explain;
	private int id, starttime, endtime;

	public Project_info(String project_name, String place, String date,
			String explain, int starttime, int endtime) {
		super();
		this.project_name = project_name;
		this.place = place;
		this.date = date;
		this.explain = explain;
		this.starttime = starttime;
		this.endtime = endtime;
	}

	public Project_info() {
		// TODO Auto-generated constructor stub
		this.project_name = null;
		this.place = null;
		this.date = null;
		this.explain = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public int getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (private int id,time; private String project_name, place, explain, date;
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(project_name);
		dest.writeString(place);
		dest.writeString(date);
		dest.writeString(explain);
		dest.writeInt(starttime);
		dest.writeInt(endtime);

	}

	public static final Parcelable.Creator<Project_info> CREATOR = new Creator<Project_info>() {

		@Override
		public Project_info[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Project_info createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			Project_info info = new Project_info(source.readString(),
					source.readString(), source.readString(),
					source.readString(), source.readInt(), source.readInt());
			return info;
		}
	};

}
