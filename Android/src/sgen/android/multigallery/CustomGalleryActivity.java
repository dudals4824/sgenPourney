package sgen.android.multigallery;

import sgen.sgen_pourney.R;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class CustomGalleryActivity extends Activity implements OnClickListener {
	private int count;
	private Bitmap[] thumbnails;
	private boolean[] thumbnailsselection;
	private String[] arrPath;
	private ImageAdapter imageAdapter;
	private Button btnSelectPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customgallery);

		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media._ID;
		Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy);
		int image_column_index = imagecursor
				.getColumnIndex(MediaStore.Images.Media._ID);
		this.count = imagecursor.getCount();
		this.thumbnails = new Bitmap[this.count];
		this.arrPath = new String[this.count];
		this.thumbnailsselection = new boolean[this.count];
		for (int i = 0; i < this.count; i++) {
			imagecursor.moveToPosition(i);
			int id = imagecursor.getInt(image_column_index);
			int dataColumnIndex = imagecursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
					getApplicationContext().getContentResolver(), id,
					MediaStore.Images.Thumbnails.MICRO_KIND, null);
			arrPath[i] = imagecursor.getString(dataColumnIndex);
		}
		GridView gridviewGallery = (GridView) findViewById(R.id.gridviewGallery);
		imageAdapter = new ImageAdapter(CustomGalleryActivity.this);
		gridviewGallery.setAdapter(imageAdapter);
		imagecursor.close();
		btnSelectPhotos = (Button) findViewById(R.id.btnSelectPhotos);
		btnSelectPhotos.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSelectPhotos) {

			// TODO Auto-generated method stub
			final int len = thumbnailsselection.length;
			int cnt = 0;
			String selectImages = "";
			for (int i = 0; i < len; i++) {
				if (thumbnailsselection[i]) {
					cnt++;
					selectImages = selectImages + arrPath[i] + "|";
				}
			}
			if (cnt == 0) {
				Toast.makeText(getApplicationContext(),
						"Please select at least one image", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"You've selected Total " + cnt + " image(s).",
						Toast.LENGTH_LONG).show();
				Log.d("SelectedImages", selectImages);
			}

		}
	}

}
