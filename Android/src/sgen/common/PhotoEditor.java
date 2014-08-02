package sgen.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class PhotoEditor {
	private Bitmap photoBitmap;
	private Bitmap coverBitmap;
	private Bitmap coveredPhoto;
	private int photoAreaWidth;
	private int photoAreaHeight;

	public PhotoEditor(Bitmap photoBitmap, Bitmap coverBitmap,
			int photoAreaWidth, int photoAreaHeight) {
		super();
		this.photoBitmap = photoBitmap;
		this.coverBitmap = coverBitmap;
		this.photoAreaWidth = photoAreaWidth;
		this.photoAreaHeight = photoAreaHeight;
	}

	public Bitmap getPhotoBitmap() {
		return photoBitmap;
	}

	public void setPhotoBitmap(Bitmap photoBitmap) {
		this.photoBitmap = photoBitmap;
	}

	public Bitmap getCoverBitmap() {
		return coverBitmap;
	}

	public void setCoverBitmap(Bitmap coverBitmap) {
		this.coverBitmap = coverBitmap;
	}

	public int getPhotoAreaWidth() {
		return photoAreaWidth;
	}

	public void setPhotoAreaWidth(int photoAreaWidth) {
		this.photoAreaWidth = photoAreaWidth;
	}

	public int getPhotoAreaHeight() {
		return photoAreaHeight;
	}

	public void setPhotoAreaHeight(int photoAreaHeight) {
		this.photoAreaHeight = photoAreaHeight;
	}

	public Bitmap editPhotoAuto() {
		resizeBitmapToProfileSize();
		getCroppedCircle();
		overlayCover();
		return coveredPhoto;
	}

	public void getCroppedCircle() {
		Bitmap output = Bitmap.createBitmap(photoAreaWidth, photoAreaHeight,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, photoAreaWidth, photoAreaHeight);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(photoAreaWidth / 2, photoAreaHeight / 2,
				photoAreaWidth / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(photoBitmap, rect, rect, paint);
		photoBitmap = output;
	}

	// bitmap을 profile width,height size에 맞게 resize
	public void resizeBitmapToProfileSize() {
		Bitmap resized;
		resized = Bitmap.createScaledBitmap(photoBitmap, photoAreaWidth,
				photoAreaHeight, true);
		photoBitmap = resized;
	}

	// cover 씌우는애
	public void overlayCover() {
		coveredPhoto = Bitmap.createBitmap(photoAreaWidth, photoAreaHeight,
				photoBitmap.getConfig());
		Canvas canvas = new Canvas(coveredPhoto);
		canvas.drawBitmap(photoBitmap, new Matrix(), null);
		canvas.drawBitmap(coverBitmap, new Matrix(), null);
	}

	public static Bitmap ImageurlToBitmapConverter(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
