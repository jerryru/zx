package com.cn.android.zhengxun.app.activity;

import java.io.File;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.model.HomeVisitModel;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.util.StringUtil;
import com.cn.android.zhengxun.app.widgets.SoundMeter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

public class SummaryActivity extends Activity {
	private EditText et_summary;
	private ImageView  volume, img_close, sc_img1;
	private Button btn_back, btn_submit,btn_audio;
	private LinearLayout layout_audio,audio_too_short;
	private RelativeLayout rl_audio;
	private int typeId;
	private SoundMeter mSensor;
	private String fileName;
	private TextView txt_title,tv_timer,tv_audio_del,tv_audio;
	private int maxTime;
	private int audio_length;
	private int flag=1;
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	protected AnimationDrawable anim;
	private OnTouchListener onTouchListener=new OnTouchListener(){

		

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (!Environment.getExternalStorageDirectory().exists()) {
				Toast.makeText(SummaryActivity.this, "No SDCard", Toast.LENGTH_LONG).show();
				return false;
			}
			switch(v.getId()){
			case R.id.btn_audio_summary:
				if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
					if (!Environment.getExternalStorageDirectory().exists()) {
						Toast.makeText(SummaryActivity.this, "No SDCard", Toast.LENGTH_LONG).show();
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
					audio_length=30-maxTime;
					tv_audio.setGravity(Gravity.CENTER_VERTICAL);
					flag = 1;}else{						
						audio_too_short.setVisibility(View.VISIBLE);
					}
				}
				
				break;
			
			}
			return false;
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_layout);
		intitViews();
		initListeners();
		initData();
	}

	private void initData() {
		Intent intent_r = this.getIntent();
		typeId = intent_r.getIntExtra(StringConstants.TYPE, 0);
		switch (typeId) {
		case TypeIds.TOUR_SUMMARY:
			txt_title.setText(getString(R.string.tour_summary));
			break;
		case TypeIds.VISIT_SUMMARY:
			txt_title.setText(getString(R.string.visit_summary));
			break;
		}
	}

	private void initListeners() {
		btn_back.setOnClickListener(listener);
//		img_audio.setOnClickListener(listener);
		btn_audio.setOnTouchListener(onTouchListener);
		btn_submit.setOnClickListener(listener);
		img_close.setOnClickListener(listener);
		tv_audio.setOnClickListener(listener);
		tv_audio_del.setOnClickListener(listener);
	}

	private void intitViews() {
		et_summary = (EditText) findViewById(R.id.et_text_summary);
		txt_title = (TextView) findViewById(R.id.txt_title);
		btn_audio = (Button) findViewById(R.id.btn_audio_summary);
		img_close = (ImageView) findViewById(R.id.img_close);
		btn_back = (Button) findViewById(R.id.back);
		btn_submit = (Button) findViewById(R.id.btn_right);
		volume = (ImageView) this.findViewById(R.id.volume);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		layout_audio = (LinearLayout) findViewById(R.id.rc_popup);
		audio_too_short=(LinearLayout)findViewById(R.id.voice_rcd_hint_tooshort);
		mSensor = new SoundMeter(this);
		btn_submit.setVisibility(View.VISIBLE);
		btn_submit.setText(R.string.save);
		tv_timer=(TextView)findViewById(R.id.tv_rc_timer);
		
		rl_audio=(RelativeLayout)findViewById(R.id.rl_audio_del);
		tv_audio_del=(TextView)findViewById(R.id.tv_summary_audio_del);
		tv_audio=(TextView)findViewById(R.id.tv_summary_audio);
	}

	private OnClickListener listener = new OnClickListener() {

		

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				SummaryActivity.this.finish();
				break;
//			case R.id.btn_audio_summary:
//				layout_audio.setVisibility(View.VISIBLE);
//				Animation mLitteAnimation = AnimationUtils.loadAnimation(
//						SummaryActivity.this, R.anim.cancel_rc);
//				Animation mBigAnimation = AnimationUtils.loadAnimation(
//						SummaryActivity.this, R.anim.cancel_rc2);
//				sc_img1.startAnimation(mLitteAnimation);
//				sc_img1.startAnimation(mBigAnimation);
//
//				fileName = StringUtil.getStringByDateTime() + ".amr";
//				start(fileName);
//				break;
			case R.id.btn_right:
				switch (typeId) {
				case TypeIds.TOUR_SUMMARY:
					Intent intent = new Intent(SummaryActivity.this,
							TourActivity.class);
					intent.putExtra(StringConstants.SUMMARY_AUDIO, fileName);
					intent.putExtra(StringConstants.SUMMARY_TEXT, et_summary
							.getText().toString());
					intent.putExtra(TourModel.Tour_audio_length, audio_length);
					setResult(RESULT_OK, intent);
					System.out.println(typeId + "got values");
					break;
				case TypeIds.VISIT_SUMMARY:
					Intent intent_v = new Intent(SummaryActivity.this,
							HomeVisitActivity.class);
					intent_v.putExtra(StringConstants.SUMMARY_AUDIO, fileName);
					intent_v.putExtra(StringConstants.SUMMARY_TEXT, et_summary
							.getText().toString());
					intent_v.putExtra(HomeVisitModel.Visit_audio_length, audio_length);
					setResult(RESULT_OK, intent_v);
					break;
				}
				SummaryActivity.this.finish();
				break;
			case R.id.img_close:
				stop();
				layout_audio.setVisibility(View.GONE);
				break;
			case R.id.tv_summary_audio:
				playMusic(Environment
						.getExternalStorageDirectory()
						+ File.separator
						+ "Android"
						+ File.separator
						+ "data"
						+ File.separator
						+ SummaryActivity.this.getPackageName()
						+ File.separator+fileName);
				tv_audio.setCompoundDrawablesWithIntrinsicBounds(R.anim.rc_playing, 0, 0, 0);				
				anim=(AnimationDrawable)tv_audio.getCompoundDrawables()[0];
				anim.start();
				anim.setOneShot(false);
				break;
			case R.id.tv_summary_audio_del:
				File file=new File(Environment
						.getExternalStorageDirectory()
						+ File.separator
						+ "Android"
						+ File.separator
						+ "data"
						+ File.separator
						+ SummaryActivity.this.getPackageName()
						+ File.separator+fileName);
				if(mMediaPlayer.isPlaying()){
					mMediaPlayer.stop();
				}
				file.deleteOnExit();				
				fileName="";
				rl_audio.setVisibility(View.GONE);
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
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			handler.postDelayed(mPollTask, POLL_INTERVAL);

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

	private void start(String name) {
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

	private Handler handler = new Handler();
}
