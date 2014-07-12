package sgen.sgen_pourney;

import java.io.File;

import sgen.image_resizing.Scaling;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

public class PhotoputActivity extends Activity {
	static final int SELECT_PICTURE = 1;
	LinearLayout layoutAlbum;
	private Uri currImageURI;
	private String imagePath;
	private File imgFile;
	private Bitmap mBitmap;
	private int tevelTerm;
	
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
		switch (resultCode) {
		case SELECT_PICTURE:
			currImageURI = data.getData();
			Log.d("KJK", "URI : " + currImageURI.toString());
			// 실제 절대주소를 받아옴
			imagePath = getRealPathFromURI(currImageURI);
			Log.d("KJK", "URI : " + currImageURI.toString());
			Log.d("KJK", "Real Path : " + imagePath);
			
			Scaling scaling=new Scaling();
			scaling.decodeFile(imagePath, 300, 300);
			// image path 얻어왔으면 imgFile초기화.
			imgFile = new File(imagePath);
			// img file bitmap 변경
			if (imgFile.exists()) {
				mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				// getCroppedBitmap(mBitmap);
				Log.e("비트맵 로드", "성공");
			} else
				Log.e("비트맵 디코딩", "실패");
			
			
			BitmapDrawable bd = (BitmapDrawable) this.getResources()
					.getDrawable(R.drawable.i_photo_gray_mask318x318);
			Bitmap coverBitmap = bd.getBitmap();

			// constructor
			// mBitmap에 찍은 사진 넣기
			// cover은 그대로

//			photoEditor photoEdit = new photoEditor(mBitmap, coverBitmap,
//					photoAreaWidth, photoAreaHeight);
//			// resize
//			// crop roun
//			// overay cover
//
//			// 이거하면 이미지 셋됨
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
