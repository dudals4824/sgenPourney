package sgen.android.multigallery;

import java.io.File;
import java.io.Serializable;

import android.graphics.Bitmap;

public class PhotoInfo implements Serializable{
	private String path;
	private int date=0;
	private File file;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
