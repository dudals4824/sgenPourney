package sgen.DTO;

public class UserDTO {
	private int userId;
	private String nickName;
	private String email;
	private String profileFilePath;
	
	public UserDTO() {
		super();
	}

	public UserDTO(int userId, String nickName, String email,
			String profileFilePath) {
		super();
		this.userId = userId;
		this.nickName = nickName;
		this.email = email;
		this.profileFilePath = profileFilePath;
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

	public String getProfileFilePath() {
		return profileFilePath;
	}

	public void setProfileFilePath(String profileFilePath) {
		this.profileFilePath = profileFilePath;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", nickName=" + nickName
				+ ", email=" + email + ", profileFilePath=" + profileFilePath
				+ "]";
	}
}
