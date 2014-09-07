package com.cn.android.zhengxun.app.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * 屏幕信息管理
 * 
 * @author jerry
 */
public class ScreenManager
{
	private static final String TAG = "ScreenManager";
	private static ScreenManager instance;
	private static ScreenInfo mScreenInfo;
	private Context mContext;

	private ScreenManager(Context context)
	{
		mContext = context;
		mScreenInfo = new ScreenInfo();
		getScreenInfo();
	}

	private void getScreenInfo()
	{
		WindowManager windowManager = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		Display display = windowManager.getDefaultDisplay();
		display.getMetrics(displayMetrics);

		mScreenInfo.desity = displayMetrics.density;
		mScreenInfo.screenWidth = displayMetrics.widthPixels;
		mScreenInfo.screenHeight = displayMetrics.heightPixels;

		Log.i(TAG, "screen desity:" + mScreenInfo.desity + ";width:"
				+ mScreenInfo.screenWidth + ";height:"
				+ mScreenInfo.screenHeight);
	}

	public static void init(Context context)
	{
		if (instance == null)
		{
			instance = new ScreenManager(context);
		}
	}

	public static ScreenManager getInstance()
	{
		if (instance == null)
		{
			throw new RuntimeException("init method not invoked!");
		}
		return instance;
	}

	public float getScreenDensity()
	{
		if (mScreenInfo.desity == 0.0)
		{
			getScreenInfo();
		}
		return mScreenInfo.desity;
	}

	public int getScreenWidth()
	{
		if (mScreenInfo.screenWidth == 0)
		{
			getScreenInfo();
		}
		return mScreenInfo.screenWidth;
	}

	public int getScreenHeight()
	{
		if (mScreenInfo.screenHeight == 0)
		{
			getScreenInfo();
		}
		return mScreenInfo.screenHeight;
	}

	/**
	 * 屏幕信息
	 */
	class ScreenInfo
	{
		public int screenWidth; // 手机宽度
		public int screenHeight; // 手机高度
		public float desity; // 手机密度
	}
}
