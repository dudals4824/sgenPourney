package sgen.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sgen.image.resizer.ImageResize;
import sgen.image.resizer.ImageResizer;
import sgen.image.resizer.ResizeMode;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class BitmapPhotoUploader extends Thread {
	private final String upLoadServerUri = "http://54.178.166.213/photoUpload.php";
	private Bitmap bm;
	private String user_id;
	private String trip_id;
	long photo_date;

	public BitmapPhotoUploader(Bitmap bm, int user_id, int trip_id,
			long photo_date) {
		super();
		this.bm = bm;
		this.user_id = Integer.toString(user_id);
		this.trip_id = Integer.toString(trip_id);
		this.photo_date = photo_date;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		InputStream is;
		String stringResponse = null;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		//File sourceFile = new File(sourceFileUri);
		try { // open a URL connection to the <span id="IL_AD7"
				// class="IL_AD">Servlet</span>
			//FileInputStream fileInputStream = new FileInputStream(sourceFile);
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
			conn.setRequestProperty("uploaded_file", "photo");
			dos = new DataOutputStream(conn.getOutputStream());

			// Send parameter #1
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"user_id\""
					+ lineEnd + lineEnd);
			dos.writeBytes(user_id + lineEnd);

			// Send parameter #2
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"trip_id\""
					+ lineEnd + lineEnd);
			dos.writeBytes(trip_id + lineEnd);

			// Send parameter #3
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"photo_date\""
					+ lineEnd + lineEnd);
			dos.writeBytes(photo_date + lineEnd);

			// Send a binary file
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
					+ "photo" + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size
			bytesAvailable = bm.getByteCount();
			bufferSize = Math.max(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = bytesAvailable;

			Log.e("log_msg", "bytes read = " + bytesRead);
			Log.e("bitmap validation",	bm.getWidth() + " " + bm.getHeight());
			
			//bitmap to stream
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 70, byteOutputStream);
			byte[] bitmapdata = byteOutputStream.toByteArray();
			ByteArrayInputStream byteInputsStream = new ByteArrayInputStream(bitmapdata);
			
			buffer = byteOutputStream.toByteArray();
			bufferSize = byteOutputStream.toByteArray().length;
			// bitmap to byte array

			// write file..
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = byteInputsStream.available();
				//bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = byteInputsStream.read(buffer, 0, bytesAvailable);
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
			stringResponse = sb.toString();
			Log.d("response string:", stringResponse);

			// close the streams //
			//fileInputStream.close();
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
	}
}
