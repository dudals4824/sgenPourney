package sgen.application;

import android.app.Application;
import sgen.DTO.UserDTO;
/** 
 * @author Junki
 * UserDTO를 전역으로 사용하기 위해 Application을 상속한 클래스.
 * 선언하고 전역 객체 처럼 사용하면 된다.
 */
public class PourneyApplication extends Application {

	private UserDTO loggedInUser;

	public UserDTO getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(UserDTO loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

}
