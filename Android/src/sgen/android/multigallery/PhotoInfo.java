package sgen.android.multigallery;

import java.io.File;
import java.io.Serializable;

import android.graphics.Bitmap;

public class PhotoInfo implements Serializable{
	private String path;
	private long date;
	//file지우기
	private File file;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
