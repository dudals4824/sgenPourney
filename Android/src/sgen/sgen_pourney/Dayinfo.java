package sgen.sgen_pourney;

import java.util.Calendar;

public class Dayinfo {
	int year, month, date, dayofmonth,day;
	int firstdayofthismonth, lastdayofthismonth;
	int[] DayArray;
	Calendar calendar;

	public Dayinfo() {
		super();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		day=calendar.get(Calendar.DAY_OF_MONTH);
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH);
		firstdayofthismonth = calendar.get(Calendar.DAY_OF_WEEK);
		lastdayofthismonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public Dayinfo(int i) {
		// TODO Auto-generated constructor stub
		this();
		calendar.add(Calendar.MONTH, i);
		day=calendar.get(Calendar.DAY_OF_MONTH);
		this.year = calendar.get(Calendar.YEAR);
		this.month = calendar.get(Calendar.MONTH);
		this.date = calendar.get(Calendar.DATE);
		firstdayofthismonth = calendar.get(Calendar.DAY_OF_WEEK);
		lastdayofthismonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	
}
