package sgen.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import sgen.DTO.UserDTO;
import sgen.application.PourneyApplication;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class ProfileUploaderByUserId extends Thread {
	private static final String SERVER_URI = "http://54.178.166.213";
	private UserDTO newUser = new UserDTO();
	private InputStream is;
	String stringResponse = null;
	private final String upLoadServerUri = "http://54.178.166.213/profileUploadById.php";

	private String sourceFileUri;
	private String userId;
	private String nickname;
	private String email;
	private String password;

	public ProfileUploaderByUserId(String sourceFileUri, UserDTO newUser) {
		super();
		this.sourceFileUri = sourceFileUri;
		this.newUser = newUser;
		this.userId = Integer.toString(newUser.getUserId());
	}

	public void run() {
		super.run();
		uploadFile(sourceFileUri, newUser);
	}

	// 여기 고칠부분!!
	public UserDTO uploadFile(String sourceFileUri, UserDTO newUser) {
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			return null;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
			conn = (HttpURLConnection) url.openConnection(); // Open a HTTP
			// connection to
			// the URL
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			//conn.setRequestProperty("Content-Type","multipart/form-data;charset=utf-8;text/plain;boundary=" + boundary);
			conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", sourceFileUri);
			dos = new DataOutputStream(conn.getOutputStream());

			// Send parameter #1
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"user_id\""
					+ lineEnd + lineEnd);
			dos.writeBytes(userId + lineEnd);

			// Send a binary file
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
					+ sourceFileUri + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.max(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			Log.e("log_msg", "bytes read = " + bytesRead);

			// byte array to bitmap..
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = 8;
			Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0,
					buffer.length, option);
			Log.e("bitmap validation",
					bitmap.getWidth() + " " + bitmap.getHeight());
			// resize bitmap
			// bitmap = getResizedBitmap(bitmap, 200, 200);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			buffer = stream.toByteArray();
			bufferSize = stream.toByteArray().length;
			// bitmap to byte array

			// write file..
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			String stringResponse = sb.toString();
			Log.d("response string:", stringResponse);

			JSONArray jArray = new JSONArray(stringResponse);
			JSONObject json_data = jArray.getJSONObject(0);
			newUser.setUserId(json_data.getInt("user_id"));
			newUser.setNickName(json_data.getString("nick_name"));
			newUser.setEmail(json_data.getString("email"));
			newUser.setProfileFilePath(SERVER_URI
					+ json_data.getString("profile_filename"));
			// close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Upload file to server Exception",
					"Exception : " + e.getMessage(), e);
		}
		return newUser;
	}

	public UserDTO getRegisteredUser() {
		return newUser;
	}

}
