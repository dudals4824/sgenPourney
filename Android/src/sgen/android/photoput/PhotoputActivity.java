package sgen.android.photoput;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import sgen.android.multigallery.CustomGallery;
import sgen.android.multigallery.PhotoInfo;
import sgen.image.resizer.ImageResizer;
import sgen.sgen_pourney.AskActivity;
import sgen.sgen_pourney.CoverActivity;
import sgen.sgen_pourney.CoverCell;
import sgen.sgen_pourney.LoginActivity;
import sgen.sgen_pourney.R;
import sgen.sgen_pourney.SimpleSideDrawer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoputActivity extends Activity implements OnClickListener {
	static final int SELECT_PICTURE = 1;
	LinearLayout layoutAlbum;
	private Uri currImageURI;
	private String imagePath;
	private SimpleSideDrawer mDrawer;
	private Button askBtn, logoutBtn, albumBtn;
	private TextView popupLocation;
	private ImageButton friendList;
	private String storagePath = Environment.DIRECTORY_DCIM + "/pic";
	private File imgFile;
	private File storageFile;
	private Bitmap mBitmap;
	private Bitmap scaledBitmap;
	private GridLayout layoutGridPhotoAlbum;
	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	// private ImageAdapter imageAdapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	// friend list var
	private PopupWindow friendListPopupWindow;
	private GridLayout layout_friend_cell;
	// 갤러리 사용을 위한 변수 선언
	private ProgressDialog mLoagindDialog;
	private GridView gridviewPhotoAlbum;

	// private ImageAdapter mListAdapter;
	// private ArrayList<ThumbImageInfo> mThumbImageInfoList;

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
		gridviewPhotoAlbum = (GridView) findViewById(R.id.gridviewPhotoAlbum);
		layoutGridPhotoAlbum = (GridLayout) findViewById(R.id.layoutGridPhotoAlbum);
		friendList = (ImageButton) findViewById(R.id.imgBack);
		popupLocation = (TextView) findViewById(R.id.textPeople);

		friendList.setOnClickListener(this);

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
		if (v.getId() == R.id.imgBack) {

			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View popupView = layoutInflater.inflate(R.layout.friend_list_popup,
					null);
			friendListPopupWindow = new PopupWindow(popupView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			friendListPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			friendListPopupWindow.setFocusable(true);
			friendListPopupWindow.setOutsideTouchable(true);
			friendListPopupWindow.setTouchInterceptor(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						friendListPopupWindow.dismiss();
						return true;
					}
					return false;
				}
			});
for(int i=0;i<7;i++){
			((GridLayout) friendListPopupWindow.getContentView()
					.findViewById(R.id.friendlistpopupback))
					.addView(new FriendListCell(this));
			friendListPopupWindow.showAsDropDown(popupLocation, -475, 27);
		}}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d("PhotoputActivity", "onActivityResult");
		Log.d("PhotoputActivity", resultCode + "");
		if (resultCode == RESULT_OK) {
			// Log.d("PhotoputActivity", "requestCode==RESULT_OK");
			// if (requestCode == SELECT_PICTURE) {
			// currImageURI = data.getData();
			// Log.d("KJK", "URI : " + currImageURI.toString());
			// // �ㅼ젣 �덈�二쇱냼瑜�諛쏆븘��
			// imagePath = getRealPathFromURI(data.getData());
			// Log.d("KJK", "URI : " + currImageURI.toString());
			// Log.d("KJK", "Real Path : " + imagePath);
			//
			// // sample bitmaplist
			// ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
			// bitmapList.add(BitmapFactory.decodeResource(getResources(),
			// R.drawable.i_back));
			// bitmapList.add(BitmapFactory.decodeResource(getResources(),
			// R.drawable.i_caledar));
			// bitmapList.add(BitmapFactory.decodeResource(getResources(),
			// R.drawable.i_logo));
			//
			// // Log.d("path", imagePath);
			// imgFile = new File(imagePath);
			// // Log.d("path", storagePath);
			// // 이 부분이 저장될 파일 위치
			// storageFile = new File("/storage/emulated/0/DCIM/Camera",
			// "a.png");
			// Log.d("path", storageFile.toString());
			// scaledBitmap = ImageResizer.resize(imgFile, 300, 300);
			//
			// Log.d("scaledBitmap", scaledBitmap + "");
			// ImageResizer.saveToFile(scaledBitmap, storageFile);
			//
			// /* Log.d("resize","resizing!"); */
			//
			// // img file bitmap 蹂�꼍
			// if (imgFile.exists()) {
			// mBitmap = BitmapFactory.decodeFile(imgFile
			// .getAbsolutePath());
			// Log.d("mBitmap", imgFile.getAbsolutePath() + "");
			// // getCroppedBitmap(mBitmap);
			// for (int i = 0; i < bitmapList.size(); i++) {
			// // 리사이징 안할거니까 imgFile없애야함
			// layoutGridPhotoAlbum.addView(new AlbumImgCell(
			// PhotoputActivity.this, bitmapList.get(i),
			// imgFile));
			// }
			// Log.e("鍮꾪듃留�濡쒕뱶", "�깃났");
			// }
			//
			// BitmapDrawable bd = (BitmapDrawable) this.getResources()
			// .getDrawable(R.drawable.i_photo_gray_mask318x318);
			// Bitmap coverBitmap = bd.getBitmap();
			//
			// // constructor
			// // mBitmap��李띿� �ъ쭊 �ｊ린
			// // cover��洹몃�濡�
			//
			// // photoEditor photoEdit = new photoEditor(mBitmap, coverBitmap,
			// // photoAreaWidth, photoAreaHeight);
			// // // resize
			// // // crop roun
			// // // overay cover
			// //
			// // // �닿굅�섎㈃ �대�吏��뗫맖
			// // mBitmap = photoEdit.editPhotoAuto();
			// // btn_picbtn.setImageBitmap(mBitmap);

			// }
			if (requestCode == 200) {
				// 사진 패스를 받아옴
				ArrayList<PhotoInfo> all_path = (ArrayList<PhotoInfo>) data
						.getExtras().getSerializable("list");

				for (int i = 0; i < all_path.size(); i++) {
					// 받아온 패스로 파일 만든다
					all_path.get(i)
							.setFile(new File(all_path.get(i).getPath()));
					imgFile = new File(all_path.get(i).getPath());
					layoutGridPhotoAlbum.addView(new AlbumImgCell(
							PhotoputActivity.this, all_path.get(i).getFile()));
				}
				// 저 for문에서 이미지 뷰에 추가하고 파일 생성하니까 아마 이부분에서 서버 코드 추가하면 될꺼에요~

			}
		}
	}

	private String getRealPathFromURI(Uri selectedVideoUri,
			ContentResolver contentResolver)
			throws UnsupportedEncodingException {
		String filePath;
		String[] filePathColumn = { MediaStore.MediaColumns.DATA };

		Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn,
				null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		filePath = cursor.getString(columnIndex);
		cursor.close();
		String result = java.net.URLDecoder.decode(filePath, "UTF-8");
		return result;
	}
}
