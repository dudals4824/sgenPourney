package sgen.sgen_pourney;

import android.widget.Button;

public class CalendarListInfo {
	
	String appointment;
	String participant;
	String place;
	int month,date;
	Button btn_createProject;
	public CalendarListInfo(String appointment, String participant, String place,int month,
			int date) {
		super();
		this.appointment = appointment;
		this.place=place;
		this.participant = participant;
		this.month = month;
		this.date = date;
	}
	public CalendarListInfo(Button btn_createProject){
		this.btn_createProject=btn_createProject;
	}
}
