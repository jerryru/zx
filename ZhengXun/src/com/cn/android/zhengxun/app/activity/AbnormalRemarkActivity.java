package com.cn.android.zhengxun.app.activity;

import java.io.File;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.util.StringUtil;
import com.cn.android.zhengxun.app.widgets.SoundMeter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AbnormalRemarkActivity extends Activity {

	private Button btn_back, btn_submit,btn_camera,btn_audio;
	private EditText et_remark;
	private TextView title,tv_timer,tv_audio,tv_camera,tv_camera_del,tv_audio_del;
	private SoundMeter mSensor;
	private LinearLayout layout_audio,audio_too_short;
	private RelativeLayout rl_camera,rl_audio;
	private ImageView volume,img_close,sc_img1,img_camera;
	private String fileName;
	private Bitmap bitmap;
	private int flag=1;
	private int maxTime=30;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private OnTouchListener touchListener=new OnTouchListener(){


		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if (!Environment.getExternalStorageDirectory().exists()) {
				Toast.makeText(AbnormalRemarkActivity.this, "No SDCard", Toast.LENGTH_LONG).show();
				return false;
			}
			switch(v.getId()){
			case R.id.btn_abnormal_audio:
				if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
					if (!Environment.getExternalStorageDirectory().exists()) {
						Toast.makeText(AbnormalRemarkActivity.this, "No SDCard", Toast.LENGTH_LONG).show();
						return false;
					}
					fileName=StringUtil.getStringByDateTime()+".amr";
					maxTime=30;
					tv_timer.setText(maxTime+"");
					layout_audio.setVisibility(View.VISIBLE);
					audio_too_short.setVisibility(View.GONE);
					start(fileName);
					flag=2;
				}else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2){
					layout_audio.setVisibility(View.GONE);
					stop();	
					if(30-maxTime>0){
					rl_audio.setVisibility(View.VISIBLE);
					tv_audio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chatto_voice_playing, 0, 0, 0);
					tv_audio.setCompoundDrawablePadding(5);
					tv_audio.setText(30-maxTime+"");
					tv_audio.setGravity(Gravity.CENTER_VERTICAL);
					}else{
						audio_too_short.setVisibility(View.VISIBLE);
					}
					flag = 1;
				}
				
				break;
			
			}
			return false;
		}
		
	};
	protected AnimationDrawable anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.abnormal_layout);
		initViews();
		initListeners();
	}

	private void initListeners() {
		btn_back.setOnClickListener(listener);
		btn_camera.setOnClickListener(listener);
//		btn_audio.setOnClickListener(listener);
		btn_audio.setOnTouchListener(touchListener);
		img_close.setOnClickListener(listener);
		btn_submit.setOnClickListener(listener);
		tv_camera.setOnClickListener(listener);
		tv_audio.setOnClickListener(listener);
		
		tv_camera_del.setOnClickListener(listener);
		tv_audio_del.setOnClickListener(listener);
		
		img_camera.setOnClickListener(listener);
	}

	private void initViews() {
		btn_back = (Button) findViewById(R.id.back);
		btn_submit = (Button) findViewById(R.id.btn_right);
		title = (TextView) findViewById(R.id.txt_title);
		volume = (ImageView) this.findViewById(R.id.volume);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		layout_audio=(LinearLayout)findViewById(R.id.rc_popup);
		audio_too_short=(LinearLayout)findViewById(R.id.voice_rcd_hint_tooshort);
		et_remark=(EditText)findViewById(R.id.etxt_abnormal_remark);
		img_close=(ImageView)findViewById(R.id.img_close);
		mSensor=new SoundMeter(this);
		btn_submit.setText(R.string.submit);
		btn_submit.setVisibility(View.VISIBLE);
		title.setText(R.string.abnormal_postioning_record);
		btn_camera=(Button)findViewById(R.id.btn_abnormal_camera);
		btn_audio=(Button)findViewById(R.id.btn_abnormal_audio);
		tv_timer=(TextView)findViewById(R.id.tv_rc_timer);
		tv_audio=(TextView)findViewById(R.id.tv_abnormal_audio);
		tv_camera=(TextView)findViewById(R.id.tv_abnormal_camera);
		rl_camera=(RelativeLayout)findViewById(R.id.rl_abnormal_camera_del);
		rl_audio=(RelativeLayout)findViewById(R.id.rl_abnormal_audio_del);

		tv_camera_del=(TextView)findViewById(R.id.tv_abnormal_camera_del);
		tv_audio_del=(TextView)findViewById(R.id.tv_abnormal_audio_del);
		
		img_camera=(ImageView)findViewById(R.id.img_big_pic);
	}

	private OnClickListener listener = new OnClickListener() {

		

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				AbnormalRemarkActivity.this.finish();
				break;
			case R.id.btn_abnormal_camera:
				Intent intent = new Intent();
				intent.setClass(AbnormalRemarkActivity.this, PhotoActivity.class);
				intent.putExtra(StringConstants.TYPE, TypeIds.ABNORMAL_POSTIONING);
				AbnormalRemarkActivity.this.startActivityForResult(intent,
						TypeIds.ABNORMAL_POSTIONING);
				break;
			case R.id.btn_abnormal_audio:
				layout_audio.setVisibility(View.VISIBLE);
				Animation mLitteAnimation = AnimationUtils.loadAnimation(AbnormalRemarkActivity.this,
						R.anim.cancel_rc);
				Animation mBigAnimation = AnimationUtils.loadAnimation(AbnormalRemarkActivity.this,
						R.anim.cancel_rc2);
				sc_img1.startAnimation(mLitteAnimation);
				sc_img1.startAnimation(mBigAnimation);
				
				fileName=StringUtil.getStringByDateTime()+".amr";
				start(fileName);
				break;
			case R.id.img_close:
				stop();
				layout_audio.setVisibility(View.GONE);
				break;
			case R.id.btn_right:
				Intent intent_ab = new Intent(AbnormalRemarkActivity.this, MapActivity.class);
				intent_ab.putExtra(StringConstants.SUMMARY_AUDIO, fileName);
				intent_ab.putExtra(StringConstants.ABNORMAL_POSITIONING_PHOTO, bitmap);
				intent_ab.putExtra(StringConstants.ABNORMAL_POSITIONING_REMARK, et_remark.getText().toString());
				setResult(RESULT_OK, intent_ab);
				AbnormalRemarkActivity.this.finish();
				break;
			case R.id.tv_abnormal_camera:
				img_camera.setVisibility(View.VISIBLE);
				Drawable d=new BitmapDrawable(AbnormalRemarkActivity.this.getResources(),bitmap);
				img_camera.setBackgroundDrawable(d);
				break;
			case R.id.tv_abnormal_audio:
				playMusic(Environment
						.getExternalStorageDirectory()
						+ File.separator
						+ "Android"
						+ File.separator
						+ "data"
						+ File.separator
						+ AbnormalRemarkActivity.this.getPackageName()
						+ File.separator+fileName);
				tv_audio.setCompoundDrawablesWithIntrinsicBounds(R.anim.rc_playing, 0, 0, 0);
				anim=(AnimationDrawable)tv_audio.getCompoundDrawables()[0];
				anim.start();
				anim.setOneShot(false);
				
				break;
			case R.id.tv_abnormal_audio_del:
				File file=new File(Environment
						.getExternalStorageDirectory()
						+ File.separator
						+ "Android"
						+ File.separator
						+ "data"
						+ File.separator
						+ AbnormalRemarkActivity.this.getPackageName()
						+ File.separator+fileName);
				file.deleteOnExit();
				fileName="";
				rl_audio.setVisibility(View.GONE);
				break;
			case R.id.tv_abnormal_camera_del:
				bitmap=null;
				rl_camera.setVisibility(View.GONE);
				break;
			case R.id.img_big_pic:
				img_camera.setVisibility(View.GONE);
				break;
			}
		}

	};
	
	
	public static final int POLL_INTERVAL = 300;
	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	
	private Runnable mTimerTask = new Runnable() {

		@Override
		public void run() {
			maxTime--;
			tv_timer.setText(maxTime+"");
			if(maxTime==0)
			{
				stop();
				
			}else{
				handler.postDelayed(mTimerTask, 1000);
			}

		}

	};

	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			handler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};
	

	private void start(String name) {
		maxTime=30;
		mSensor.start(name);
		handler.postDelayed(mPollTask, POLL_INTERVAL);
		handler.postDelayed(mTimerTask, 1000);
	}

	private void stop() {		
		handler.removeCallbacks(mSleepTask);
		handler.removeCallbacks(mPollTask);
		handler.removeCallbacks(mTimerTask);
		mSensor.stop();		
		volume.setImageResource(R.drawable.amp1);
		
	}

	private void updateDisplay(double signalEMA) {
		
		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);
			
			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	private Handler handler=new Handler();

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			switch(requestCode){
			case TypeIds.ABNORMAL_POSTIONING:
				Bundle bundle = data.getExtras();
				bitmap = bundle
						.getParcelable(StringConstants.ABNORMAL_POSITIONING_PHOTO);
				if(null!=bitmap){
					rl_camera.setVisibility(View.VISIBLE);
					Drawable d=new BitmapDrawable(this.getResources(),bitmap);
					tv_camera.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
					tv_camera.setText("");
				}
				break;
			}
		}
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
				}
			});

		} catch (Exception e) {
			Log.e("exception", "Exception", e);
		}

	}

}
