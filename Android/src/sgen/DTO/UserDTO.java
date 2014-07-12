package sgen.DTO;

public class UserDTO {
	private int userId;
	private String nickName;
	private String email;
	private String profileFilename;
	
	public UserDTO() {
		super();
	}

	public UserDTO(int userId, String nickName, String email,
			String profileFilename) {
		super();
		this.userId = userId;
		this.nickName = nickName;
		this.email = email;
		this.profileFilename = profileFilename;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileFilename() {
		return profileFilename;
	}

	public void setProfileFilename(String profileFilename) {
		this.profileFilename = profileFilename;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", nickName=" + nickName
				+ ", email=" + email + ", profileFilename=" + profileFilename
				+ "]";
	}
}
