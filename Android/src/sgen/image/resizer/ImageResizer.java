package sgen.image.resizer;

import java.io.File;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class ImageResizer {

	// Resize
	public static Bitmap resize(File original, int width, int height) {
		return ImageResize.resize(original, width, height, null);
	}
	
	public static Bitmap resize(File original, int width, int height, ResizeMode mode) {
		return ImageResize.resize(original, width, height, mode);
	}
	
	public static Bitmap resize(byte[] byteArray, int width, int height) {
		return ImageResize.resize(byteArray, width, height, null);
	}
	
	public static Bitmap resize(byte[] byteArray, int width, int height, ResizeMode mode) {
		return ImageResize.resize(byteArray, width, height, mode);
	}
	
	public static Bitmap resize(Resources resources, int resId, int width, int height) {
		return ImageResize.resize(resources, resId, width, height, null);
	}
	
	public static Bitmap resize(Resources resources, int resId, int width, int height, ResizeMode mode) {
		return ImageResize.resize(resources, resId, width, height, mode);
	}
	
	public static void saveToFile(Bitmap image, File file) {
		ImageWriter.writeToFile(image, file);
	}
	
}
