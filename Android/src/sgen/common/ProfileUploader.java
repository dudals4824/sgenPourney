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

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class ProfileUploader {
	InputStream is;
	String stringResponse = null;
	private final String upLoadServerUri = "http://54.178.166.213/profileUpload.php";

	// 여기 고칠부분!!
	public String uploadFile(String sourceFileUri, String nickname, String email, String password) {
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
		try { // open a URL connection to the <span id="IL_AD7"
				// class="IL_AD">Servlet</span>
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
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", sourceFileUri);
			dos = new DataOutputStream(conn.getOutputStream());

			// Send parameter #1
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"nickname\""
					+ lineEnd + lineEnd);
			dos.writeBytes(nickname + lineEnd);

			// Send parameter #2
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"email\""
					+ lineEnd + lineEnd);
			dos.writeBytes(email + lineEnd);
			
			// send parameter #3
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"password\""
					+ lineEnd + lineEnd);
			dos.writeBytes(password + lineEnd);

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
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			String stringResponse = sb.toString();
			Log.d("response string:", stringResponse);

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
		return stringResponse;
	}

}