package sgen.android.photoput;

import sgen.DTO.UserDTO;
import sgen.common.PhotoEditor;
import sgen.sgen_pourney.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendListCell extends LinearLayout {
	//전달 받을 객체
	private Context mContext = null;
	private UserDTO mFriends;
	
	//view 변수들
	private ImageView friendProfile;
	private TextView friendName;

	//안쓸듯
	public FriendListCell(Context context, AttributeSet attrs, UserDTO friend) {
		super(context, attrs);
		mContext = context;
		mFriends = friend;
		initMarbleView(mContext, mFriends);
	}

	public FriendListCell(Context context, UserDTO friend) {
		super(context);
		mContext = context;
		mFriends = friend;
		initMarbleView(mContext, mFriends);
	}

	void initMarbleView(Context context, UserDTO friend) {
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(
				infService);
		View v = li.inflate(R.layout.friend_list_cell, this, false);
		addView(v);
		
		friendProfile = (ImageView)findViewById(R.id.friendprofileOnCell);
		friendName = (TextView)findViewById(R.id.friendname);
		
		friendName.setText(mFriends.getNickName());
		
		FriendProfileImageSetter friendProfileImageSetter = new FriendProfileImageSetter();
		friendProfileImageSetter.execute(mFriends, friendProfile);
	}

	public class FriendProfileImageSetter extends
			AsyncTask<Object, String, String> {
		private UserDTO foundFriend = new UserDTO();
		private ImageView targetImageView = null;
		
		private int photoAreaWidth;
		private int photoAreaHeight;
		
		private Bitmap profilePhoto;
		
		@Override
		protected String doInBackground(Object... params) {
			// parameter converting to original object
			foundFriend = (UserDTO) params[0];
			targetImageView = (ImageView) params[1];

			// image view setting
			profilePhoto = PhotoEditor.ImageurlToBitmapConverter(foundFriend
					.getProfileFilePath());
			if (profilePhoto != null) {
				// profile 사진 크기에 맞게 cover bitmap 설정
				BitmapDrawable bd = null;

				bd = (BitmapDrawable) getResources().getDrawable(
						R.drawable.i_profile_238x240_cover);

				Bitmap coverBitmap = bd.getBitmap();

				targetImageView.measure(MeasureSpec.UNSPECIFIED,
						MeasureSpec.UNSPECIFIED);
				photoAreaWidth = targetImageView.getMeasuredWidth();
				photoAreaHeight = targetImageView.getMeasuredHeight();
				Log.d("width height", photoAreaWidth + "    " + photoAreaHeight);
				PhotoEditor photoEdit = new PhotoEditor(profilePhoto,
						coverBitmap, photoAreaWidth, photoAreaHeight);
				profilePhoto = photoEdit.editPhotoAuto();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 친구 찾은 화면일 경우
			targetImageView.setImageBitmap(profilePhoto);
		}

	}

}
