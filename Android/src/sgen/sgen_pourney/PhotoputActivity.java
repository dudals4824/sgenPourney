package sgen.sgen_pourney;

import java.io.File;

import sgen.image.resizer.ImageResizer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class PhotoputActivity extends Activity implements OnClickListener {
	static final int SELECT_PICTURE = 1;
	LinearLayout layoutAlbum;
	private Uri currImageURI;
	private String imagePath;
	private SimpleSideDrawer mDrawer;
	private Button askBtn, logoutBtn, albumBtn;
	private String storagePath = Environment.DIRECTORY_DCIM + "/pic";
	private File imgFile;
	private File storageFile;
	private Bitmap mBitmap;
	private int tevelTerm;
	private Bitmap scaledBitmap;
	private GridLayout layoutGridPhotoAlbum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_photoput);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		mDrawer = new SimpleSideDrawer(this);
		mDrawer.setLeftBehindContentView(R.layout.left_behind_drawer);
		findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawer.toggleLeftDrawer();

			}
		});
		askBtn = (Button) findViewById(R.id.ask_text);
		askBtn.setOnClickListener(this);
		albumBtn = (Button) findViewById(R.id.last_album_text);
		albumBtn.setOnClickListener(this);
		logoutBtn = (Button) findViewById(R.id.log_out_text);
		logoutBtn.setOnClickListener(this);
		layoutAlbum = (LinearLayout) findViewById(R.id.layoutAlbum);

		layoutAlbum.addView(new DayAlbum(PhotoputActivity.this));
		layoutGridPhotoAlbum=(GridLayout)findViewById(R.id.layoutGridPhotoAlbum);
		tevelTerm = 3;
	}

	public void onClick(View v) {
		if (v.getId() == R.id.ask_text) {
			Intent intent = new Intent(this, AskActivity.class);
			startActivity(intent);
		}
		if (v.getId() == R.id.log_out_text) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		if (v.getId() == R.id.last_album_text) {
			Intent intent = new Intent(this, CoverActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d("onActivityResult", "onActivityResult");
		Log.d("onActivityResult", resultCode+"");
		if (resultCode  == RESULT_OK) {
			Log.d("onActivityResult", "requestCode==RESULT_OK");
			if (requestCode == SELECT_PICTURE) {
				currImageURI = data.getData();
				Log.d("KJK", "URI : " + currImageURI.toString());
				// �ㅼ젣 �덈�二쇱냼瑜�諛쏆븘��
				imagePath = getRealPathFromURI(data.getData());
				Log.d("KJK", "URI : " + currImageURI.toString());
				Log.d("KJK", "Real Path : " + imagePath);

				Log.d("path", imagePath);
				imgFile = new File(imagePath);
//				Log.d("path", storagePath);
				//이 부분이 저장될 파일 위치
				storageFile = new File("/storage/emulated/0/DCIM/Camera", "a.png");
				Log.d("path", storageFile.toString());
				scaledBitmap = ImageResizer.resize(imgFile, 300, 300);

				Log.d("scaledBitmap", scaledBitmap+"");
				ImageResizer.saveToFile(scaledBitmap, storageFile);
				// image path �살뼱�붿쑝硫�imgFile珥덇린��

				/* Log.d("resize","resizing!"); */

				// img file bitmap 蹂�꼍
				if (imgFile.exists()) {
					mBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					Log.d("mBitmap", imgFile.getAbsolutePath()+"");
					// getCroppedBitmap(mBitmap);
					layoutGridPhotoAlbum.addView(new AlbumImgCell(PhotoputActivity.this,mBitmap));
					Log.e("鍮꾪듃留�濡쒕뱶", "�깃났");
				}

				BitmapDrawable bd = (BitmapDrawable) this.getResources()
						.getDrawable(R.drawable.i_photo_gray_mask318x318);
				Bitmap coverBitmap = bd.getBitmap();

				// constructor
				// mBitmap��李띿� �ъ쭊 �ｊ린
				// cover��洹몃�濡�

				// photoEditor photoEdit = new photoEditor(mBitmap, coverBitmap,
				// photoAreaWidth, photoAreaHeight);
				// // resize
				// // crop roun
				// // overay cover
				//
				// // �닿굅�섎㈃ �대�吏��뗫맖
				// mBitmap = photoEdit.editPhotoAuto();
				// btn_picbtn.setImageBitmap(mBitmap);

			}
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
