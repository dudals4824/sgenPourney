package sgen.android.photoput;

import java.io.File;
import java.util.ArrayList;

import sgen.image.resizer.ImageResizer;
import sgen.sgen_pourney.AskActivity;
import sgen.sgen_pourney.CoverActivity;
import sgen.sgen_pourney.LoginActivity;
import sgen.sgen_pourney.R;
import sgen.sgen_pourney.SimpleSideDrawer;
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
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

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
	private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
//   private ImageAdapter imageAdapter;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

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
		layoutGridPhotoAlbum = (GridLayout) findViewById(R.id.layoutGridPhotoAlbum);
		tevelTerm = 3;
		
//		//여기서부터 갤러리 이미지 다중 선택을 위해서
//		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
//        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
//        Cursor imagecursor = managedQuery(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
//                null, orderBy + " DESC");
// 
//        this.imageUrls = new ArrayList<String>();
// 
//        for (int i = 0; i < imagecursor.getCount(); i++) {
//            imagecursor.moveToPosition(i);
//            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
//            imageUrls.add(imagecursor.getString(dataColumnIndex));
// 
//            System.out.println("=====> Array path => "+imageUrls.get(i));
//        }
// 
//        options = new DisplayImageOptions.Builder()
//            .showStubImage(R.drawable.i_memo_back)
//            .showImageForEmptyUri(R.drawable.i_memo)
//            .cacheInMemory()
//            .cacheOnDisc()
//            .build();
// 
//        imageAdapter = new ImageAdapter(this, imageUrls);
//		//여기까지
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
		Log.d("onActivityResult", resultCode + "");
		if (resultCode == RESULT_OK) {
			Log.d("onActivityResult", "requestCode==RESULT_OK");
			if (requestCode == SELECT_PICTURE) {
				currImageURI = data.getData();
				Log.d("KJK", "URI : " + currImageURI.toString());
				// �ㅼ젣 �덈�二쇱냼瑜�諛쏆븘��
				imagePath = getRealPathFromURI(data.getData());
				Log.d("KJK", "URI : " + currImageURI.toString());
				Log.d("KJK", "Real Path : " + imagePath);

				// sample bitmaplist
				ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
				bitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.i_back));
				bitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.i_caledar));
				bitmapList.add(BitmapFactory.decodeResource(getResources(), R.drawable.i_logo));

				// Log.d("path", imagePath);
				imgFile = new File(imagePath);
				// Log.d("path", storagePath);
				// 이 부분이 저장될 파일 위치
				storageFile = new File("/storage/emulated/0/DCIM/Camera",
						"a.png");
				Log.d("path", storageFile.toString());
				scaledBitmap = ImageResizer.resize(imgFile, 300, 300);

				Log.d("scaledBitmap", scaledBitmap + "");
				ImageResizer.saveToFile(scaledBitmap, storageFile);

				/* Log.d("resize","resizing!"); */

				// img file bitmap 蹂�꼍
				if (imgFile.exists()) {
					mBitmap = BitmapFactory.decodeFile(imgFile
							.getAbsolutePath());
					Log.d("mBitmap", imgFile.getAbsolutePath() + "");
					// getCroppedBitmap(mBitmap);
					for (int i = 0; i < bitmapList.size(); i++) {
						//리사이징 안할거니까 imgFile없애야함
						layoutGridPhotoAlbum.addView(new AlbumImgCell(PhotoputActivity.this, bitmapList.get(i), imgFile));
					}
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
//	@Override
//    protected void onStop() {
//        imageLoader.stop();
//        super.onStop();
//    }
 
//    public void btnChoosePhotosClick(View v){
// 
//        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();
//
//    }
// 
//    /*private void startImageGalleryActivity(int position) {
//        Intent intent = new Intent(this, ImagePagerActivity.class);
//        intent.putExtra(Extra.IMAGES, imageUrls);
//        intent.putExtra(Extra.IMAGE_POSITION, position);
//        startActivity(intent);
//    }*/
// 
//    public class ImageAdapter extends BaseAdapter {
// 
//        ArrayList<String> mList;
//        LayoutInflater mInflater;
//        Context mContext;
//        SparseBooleanArray mSparseBooleanArray;
// 
//        public ImageAdapter(Context context, ArrayList<String> imageList) {
//            // TODO Auto-generated constructor stub
//            mContext = context;
//            mInflater = LayoutInflater.from(mContext);
//            mSparseBooleanArray = new SparseBooleanArray();
//            mList = new ArrayList<String>();
//            this.mList = imageList;
// 
//        }
// 
//        public ArrayList<String> getCheckedItems() {
//            ArrayList<String> mTempArry = new ArrayList<String>();
// 
//            for(int i=0;i<mList.size();i++) {
//                if(mSparseBooleanArray.get(i)) {
//                    mTempArry.add(mList.get(i));
//                }
//            }
// 
//            return mTempArry;
//        }
// 
//        @Override
//        public int getCount() {
//            return imageUrls.size();
//        }
// 
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
// 
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
// 
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
// 
//            if(convertView == null) {
//                convertView = mInflater.inflate(R.layout.row_multiphoto_item, null);
//            }
// 
//            CheckBox mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
//            final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
// 
// 
// 
//            mCheckBox.setTag(position);
//            mCheckBox.setChecked(mSparseBooleanArray.get(position));
//            mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
// 
//            return convertView;
//        }
// 
//        OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
// 
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // TODO Auto-generated method stub
//                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
//            }
//        };
//    }

}
