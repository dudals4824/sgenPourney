package sgen.DTO;

public class PhotoDTO {
	private int photoId;
	private int tripId;
	private int userId;
	private String photoFilename;
	private int likes;
	private long photo_date;

	public PhotoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhotoDTO(int photoId, int tripId, int userId, String photoFilename,
			int likes, int photo_date) {
		super();
		this.photoId = photoId;
		this.tripId = tripId;
		this.userId = userId;
		this.photoFilename = photoFilename;
		this.likes = likes;
		this.photo_date = photo_date;
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public int getTripId() {
		return tripId;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPhotoFilename() {
		return photoFilename;
	}

	public void setPhotoFilename(String photoFilename) {
		this.photoFilename = photoFilename;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public long getPhoto_date() {
		return photo_date;
	}

	public void setPhoto_date(long photo_date) {
		this.photo_date = photo_date;
	}

	@Override
	public String toString() {
		return "PhotoDTO [photoId=" + photoId + ", tripId=" + tripId
				+ ", userId=" + userId + ", photoFilename=" + photoFilename
				+ ", likes=" + likes + ", photo_date=" + photo_date + "]";
	}

}
