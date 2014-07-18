package sgen.session;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import sgen.sgen_pourney.*;


/*
 * class : UserSessionManager.
 * class for saving user account information.
 * using android sharedpreference.
 */

public class UserSessionManager {

	// Shared Preferences reference
	SharedPreferences pref;

	// Editor reference for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREFER_NAME = "PourneyPref";

	// All Shared Preferences Keys
	private static final String IS_USER_LOGIN = "IsUserLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_ID = "user_id";

	// Trip id (make variable public to access from outside)
	public static final String KEY_TRIP_ID = "trip_id";

	// Constructor
	public UserSessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	// Create login session
	public void createUserLoginSession(int user_id, int trip_id) {
		// Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);
		// Storing userid in pref
		editor.putInt(KEY_ID, user_id);
		// Storing tripid in pref
		editor.putInt(KEY_TRIP_ID, trip_id);
		// commit changes
		editor.commit();
	}
	
	//Change login session
	public void changeUserSession(int user_id, int trip_id){
		if(user_id>0)
			editor.putInt(KEY_ID, user_id);
		if(trip_id>0)
			editor.putInt(KEY_TRIP_ID, trip_id);
		editor.commit();
	}
	
	/**
	 * Check login method will check user login status If false it will redirect
	 * user to login page Else do anything
	 * */
	public boolean checkLogin() {
		// Check login status
		if (!this.isUserLoggedIn()) {

			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, TravelInfoActivity.class);

			// Closing all the Activities from stack
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);

			return true;
		}
		return false;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, Integer> getUserDetails() {

		// Use hashmap to store user credentials
		HashMap<String, Integer> user = new HashMap<String, Integer>();

		// user name
		user.put(KEY_ID, pref.getInt(KEY_ID, 0));
		
		user.put(KEY_TRIP_ID, pref.getInt(KEY_TRIP_ID, 0));

		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {

		// Clearing all user data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Login Activity
		Intent i = new Intent(_context, TravelInfoActivity.class);

		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	// Check for login
	public boolean isUserLoggedIn() {
		return pref.getBoolean(IS_USER_LOGIN, false);
	}
}