package sgen.DTO;

public class TripDTO {
	private int tripId;
	private String tripTitle;
	private String startDate;
	private String endDate;

	public TripDTO() {
		super();
	}

	public TripDTO(int tripId, String tripTitle, String startDate,
			String endDate) {
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "TripDTO [tripId=" + tripId + ", tripTitle=" + tripTitle
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
