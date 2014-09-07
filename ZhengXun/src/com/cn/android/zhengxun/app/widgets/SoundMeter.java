package com.cn.android.zhengxun.app.widgets;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public  class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;
	private Context context;
	public SoundMeter(Context context)
	{
		this.context=context;
	}

	public void start(String fileName) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(Environment.getExternalStorageDirectory()+File.separator+"Android"+File.separator+"data"+File.separator+context.getPackageName()+File.separator+fileName);
			try {
				mRecorder.prepare();
				mRecorder.start();
				
				mEMA = 0.0;
			} catch (IllegalStateException e) {
				Log.e("exception", "IllegalStateException", e);
			} catch (IOException e) {
				Log.e("exception", "IOException", e);
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
