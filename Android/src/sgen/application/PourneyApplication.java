package sgen.application;

import android.app.Application;
import sgen.DTO.UserDTO;

public class PourneyApplication extends Application {

	private UserDTO loggedInUser;

	public UserDTO getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(UserDTO loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

}
