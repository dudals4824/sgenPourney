package sgen.DTO;

public class TripDTO {
	private int tripId;
	private String triptitle;
	private String startdate;
	private String enddate;

	public TripDTO() {
		super();
	}

	public TripDTO(int tripId, String triptitle, String startdate, String enddate) {
		super();
		this.tripId = tripId;
		this.triptitle = triptitle;
		this.startdate = startdate;
		this.enddate = enddate;
	}

	public int getTripId() {
		return tripId;
	}
	
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public String getTriptitle() {
		return triptitle;
	}

	public void setTriptitle(String triptitle) {
		this.triptitle = triptitle;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	@Override
	public String toString() {
		return "TripDTO [tripId=" + tripId + ", triptitle=" + triptitle
				+ ", startdate=" + startdate + ", enddate=" + enddate + "]";
	}

}
