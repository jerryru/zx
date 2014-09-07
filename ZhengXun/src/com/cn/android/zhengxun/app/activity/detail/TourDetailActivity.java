package com.cn.android.zhengxun.app.activity.detail;

import java.io.File;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.service.TourInfoService;
import com.cn.android.zhengxun.app.service.impl.TourInfoServiceImpl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TourDetailActivity extends Activity {

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.stop();
			}
			TourDetailActivity.this.finish();
	}

	private ImageView img_door, img_display,img_big_pic;
	private TextView tv_audio, tv_summary, tv_title;
	private Button back,btn_select_door,btn_select_display;
	private String tourId;
	private TourInfoService tourService;
	private TourModel model;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	protected AnimationDrawable anim;
	private OnClickListener listener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.back:
				if(mMediaPlayer.isPlaying()){
				mMediaPlayer.stop();
				}
				TourDetailActivity.this.finish();
				break;
			case R.id.tv_summary_audio:
				playMusic(Environment
						.getExternalStorageDirectory()
						+ File.separator
						+ "Android"
						+ File.separator
						+ "data"
						+ File.separator
						+ TourDetailActivity.this.getPackageName()
						+ File.separator+model.getTour_audio_file());
				tv_audio.setCompoundDrawablesWithIntrinsicBounds(R.anim.rc_playing, 0, 0, 0);				
				anim=(AnimationDrawable)tv_audio.getCompoundDrawables()[0];
				anim.start();
				anim.setOneShot(false);
				break;
			case R.id.img_big_pic:
				img_big_pic.setVisibility(View.GONE);
				break;
			case R.id.btn_look_gate_photo:
				Drawable drawable=new BitmapDrawable(TourDetailActivity.this.getResources(),BitmapFactory.decodeByteArray(
						model.getDoor_Photo(), 0, model.getDoor_Photo().length));
				img_big_pic.setBackgroundDrawable(drawable);
				img_big_pic.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_look_display_photo:
				Drawable bitmapDraw=new BitmapDrawable(TourDetailActivity.this.getResources(),BitmapFactory.decodeByteArray(
						model.getDisplay_Photo(), 0, model.getDisplay_Photo().length));
				img_big_pic.setBackgroundDrawable(bitmapDraw);
				img_big_pic.setVisibility(View.VISIBLE);
				break;
			}
			
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tour_detail_layout);
		Intent intent = this.getIntent();
		tourId = intent.getStringExtra(TourModel.Tour_Id);
		initViews();
		initListeners();
		initData();
	}

	private void initData() {
		tourService = new TourInfoServiceImpl(this);
		model = tourService.getTourById(tourId);		
		img_door.setImageBitmap(BitmapFactory.decodeByteArray(
				model.getDoor_Photo(), 0, model.getDoor_Photo().length));
		img_display.setImageBitmap(BitmapFactory.decodeByteArray(
				model.getDisplay_Photo(), 0, model.getDisplay_Photo().length));
		if (null != model.getTour_Remark()
				&& !"".equals(model.getTour_Remark())) {
			tv_summary.setText(model.getTour_Remark());
		} else {
			tv_summary.setText(R.string.none);
		}
		tv_title.setText(R.string.tour_detail);
		if(null!=model.getTour_audio_file()&&!"".equals(model.getTour_audio_file())){
			
			tv_audio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chatto_voice_playing, 0, 0, 0);
			tv_audio.setCompoundDrawablePadding(5);
//			try {
//				mMediaPlayer.setDataSource(Environment
//						.getExternalStorageDirectory()
//						+ File.separator
//						+ "Android"
//						+ File.separator
//						+ "data"
//						+ File.separator
//						+ TourDetailActivity.this.getPackageName()
//						+ File.separator+model.getTour_audio_file());
////				tv_audio.setText(mMediaPlayer.getDuration()/1000);
//			} catch (IllegalArgumentException e) {
//				Log.e("exception", "Exception", e);
//			} catch (SecurityException e) {
//				Log.e("exception", "Exception", e);
//			} catch (IllegalStateException e) {
//				Log.e("exception", "Exception", e);
//			} catch (IOException e) {
//				Log.e("exception", "Exception", e);
//			}
			tv_audio.setGravity(Gravity.CENTER_VERTICAL);
			tv_audio.setText(model.getTour_audio_length()+"");
		}
	}

	private void initListeners() {
		back.setOnClickListener(listener);
		tv_audio.setOnClickListener(listener);
		img_big_pic.setOnClickListener(listener);
		btn_select_door.setOnClickListener(listener);
		btn_select_display.setOnClickListener(listener);
	}

	private void initViews() {
		img_door = (ImageView) findViewById(R.id.img_gate_photo);
		img_display = (ImageView) findViewById(R.id.img_display_photo);
		tv_audio = (TextView) findViewById(R.id.tv_summary_audio);
		tv_summary = (TextView) findViewById(R.id.tv_summary_remark);
		tv_title = (TextView) findViewById(R.id.txt_title);
		back = (Button) findViewById(R.id.back);
		img_big_pic=(ImageView)findViewById(R.id.img_big_pic);
		btn_select_door=(Button)findViewById(R.id.btn_look_gate_photo);
		btn_select_display=(Button)findViewById(R.id.btn_look_display_photo);
	}

	/**
	 * @Description
	 * @param name The file name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					anim.stop();
					tv_audio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chatto_voice_playing, 0, 0, 0);
					if(null!=model){
					tv_audio.setText(model.getTour_audio_length()+"");
					}
				}
			});

		} catch (Exception e) {
			Log.e("exception", "Exception", e);
		}

	}

}
