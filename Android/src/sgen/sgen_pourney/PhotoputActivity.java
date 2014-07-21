package sgen.sgen_pourney;

import java.io.File;

import sgen.image.resizer.ImageResizer;
import sgen.image.resizer.ResizeMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

public class PhotoputActivity extends Activity {
	static final int SELECT_PICTURE = 1;
	LinearLayout layoutAlbum;
	private Uri currImageURI;
	private String imagePath;

	private String storagePath=Environment.DIRECTORY_DCIM+"/pic";
	private File imgFile;
	private File storageFile;
	private Bitmap mBitmap;
	private int tevelTerm;
	private Bitmap scaledBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_photoput);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);		
		layoutAlbum = (LinearLayout) findViewById(R.id.layoutAlbum);

		layoutAlbum.addView(new DayAlbum(PhotoputActivity.this));
		tevelTerm=3;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d("ddd", "ddd");
		switch (resultCode) {
		case SELECT_PICTURE:
			currImageURI = data.getData();
			Log.d("KJK", "URI : " + currImageURI.toString());
			// �ㅼ젣 �덈�二쇱냼瑜�諛쏆븘��
			imagePath = getRealPathFromURI(currImageURI);
			Log.d("KJK", "URI : " + currImageURI.toString());
			Log.d("KJK", "Real Path : " + imagePath);

			/*Log.d("path",imagePath);
			imgFile = new File(imagePath);
			Log.d("path",storagePath);
			storageFile=new File(storagePath);

			scaledBitmap = ImageResizer.resize(imgFile, 300, 300);
			
			ImageResizer.saveToFile(scaledBitmap, storageFile);*/
			
			// image path �살뼱�붿쑝硫�imgFile珥덇린��
	
/*			Log.d("resize","resizing!");*/

			// img file bitmap 蹂�꼍
			if (imgFile.exists()) {
				mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				// getCroppedBitmap(mBitmap);
				
				Log.e("鍮꾪듃留�濡쒕뱶", "�깃났");
			} 
			
			
			BitmapDrawable bd = (BitmapDrawable) this.getResources()
					.getDrawable(R.drawable.i_photo_gray_mask318x318);
			Bitmap coverBitmap = bd.getBitmap();

			// constructor
			// mBitmap��李띿� �ъ쭊 �ｊ린
			// cover��洹몃�濡�

//			photoEditor photoEdit = new photoEditor(mBitmap, coverBitmap,
//					photoAreaWidth, photoAreaHeight);
//			// resize
//			// crop roun
//			// overay cover
//
//			// �닿굅�섎㈃ �대�吏��뗫맖
//			mBitmap = photoEdit.editPhotoAuto();
//			btn_picbtn.setImageBitmap(mBitmap);
			break;

		default:
			break;
		}
	}

	private String getRealPathFromURI(Uri contentUri) {
		String path = null;
		String[] proj = { MediaStore.MediaColumns.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		if (cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			path = cursor.getString(column_index);
		}
		cursor.close();
		return path;
	}

}
