package sgen.android.photoput;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;

public abstract class BaseActivity extends Activity{
	protected ImageLoader imageLoader=ImageLoader.getInstance();

}
