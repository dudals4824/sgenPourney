package sgen.DTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class TripDTO {
	private int tripId;
	private String tripTitle;
	private long startDate;
	private long endDate;
	private int photoCnt;

	public TripDTO() {
		super();
	}

	public TripDTO(int tripId, String tripTitle, int startDate, int endDate) {
		super();
		this.tripId = tripId;
		this.tripTitle = tripTitle;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getTripId() {
		return tripId;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public String getTripTitle() {
		return tripTitle;
	}

	public void setTripTitle(String tripTitle) {
		this.tripTitle = tripTitle;
	}

	public long getStartDate() {
		return startDate;
		// 밀리세컨드로 가져옴 그레고리안에 넣어서 사용하면됨
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public String getStartDateInDateFormat() {
		// GregorianCalendar date = new GregorianCalendar(Locale.KOREA);
		// date.setTimeInMillis(startDate);
		// date.add(Calendar.MONTH, -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.KOREA);
		return dateFormat.format(startDate);
	}

	public String getEndDateInDateFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.KOREA);
		return dateFormat.format(endDate);
	}

	public int getPhotoCnt() {
		return photoCnt;
	}

	public void setPhotoCnt(int photoCnt) {
		this.photoCnt = photoCnt;
	}

	@Override
	public String toString() {
		return "TripDTO [tripId=" + tripId + ", tripTitle=" + tripTitle
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", photoCnt=" + photoCnt + "]";
	}

}
