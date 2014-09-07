package com.cn.android.zhengxun.app.util;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.cn.android.zhengxun.app.R;


public class AppHelper
{

	/**
	 * 弹出确认/取消对话框
	 * 
	 * @param context
	 */
	public static void showExitDialog(Context context, String message,
			OnClickListener clickListener)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.hint));
		builder.setMessage(message);
		builder.setPositiveButton(context.getString(R.string.ok), clickListener);
		builder.setNeutralButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		builder.show();
	}

	
	/**
	 * 密码加密
	 * 
	 * @param password
	 * @return
	 */
	public static String encrypt(String key, String password)
	{
		String result = null;
		try
		{
			byte[] enByte = DESUtils.encrypt(password, key.getBytes());
			result = Base64.encodeToString(enByte, Base64.DEFAULT);
		} catch (InvalidKeyException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
		} catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		} catch (IllegalBlockSizeException e)
		{
			e.printStackTrace();
		} catch (BadPaddingException e)
		{
			e.printStackTrace();
		}
		result = result.replaceAll("\n", "");
		return result;
	}

	/**
	 * 隐藏软件盘
	 * 
	 * @param context
	 */
	public static void hideSoftKeyboard(Activity context)
	{
		InputMethodManager inputMananger = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try
		{
			inputMananger.hideSoftInputFromWindow(context.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 格式化文件大小为MB
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSizeToMB(long length)
	{
		int sub_string = 0;
		sub_string = String.valueOf((float) length / 1048576).indexOf(".");
		String result = ((float) length / 1048576 + "000").substring(0,
				sub_string + 3);
		return result;
	}

	/**
	 * dip转px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 获取当天日期 格式:yyyy-MM-dd
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 获取当月第一天 格式:yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonth()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getCurrentDate(calendar.getTime());
	}

	/**
	 * ImageView填充正常图片
	 * 
	 * @param imageView
	 * @param imgMap
	 * @return
	 */
	public static void bindNormalImageView(ImageView imageView,
			Map<String, SoftReference<Bitmap>> imgMap)
	{
		for (String imgUrl : imgMap.keySet())
		{
			if (imgMap.get(imgUrl) != null)
			{
				Bitmap bitmap = imgMap.get(imgUrl).get();
				if (bitmap != null)
				{
					imageView.setImageBitmap(bitmap);
					bitmap = null;
				}
			}
			break;
		}
	}

	/**
	 * ImageView填充5度圆角图片
	 * 
	 * @param imageView
	 * @param imgMap
	 * @return
	 */
	public static boolean bindRoundCornerImageView(ImageView imageView,
			Map<String, SoftReference<Bitmap>> imgMap)
	{
		for (String imgUrl : imgMap.keySet())
		{
			if (imgMap.get(imgUrl) != null)
			{
				Bitmap bitmap = imgMap.get(imgUrl).get();
				if (bitmap != null)
				{
					imageView
							.setImageBitmap(AppHelper.toRoundCorner(bitmap, 5));
					return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * 绘制圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels)
	{

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static class FobbitMultClick
	{
		private static final int INTERVAL_DURATION = 500; // 间隔0.5秒
		private static long lastClickMillis = 0L; // 上次时间

		/**
		 * 防止0.5秒内的多次点击
		 * 
		 * @return
		 */
		public static boolean isMultClick()
		{
			boolean result = false;
			long currentClickMillis = System.currentTimeMillis();
			if (currentClickMillis - lastClickMillis < INTERVAL_DURATION)
			{
				result = true;
			}
			System.out.println("click interval time:"
					+ (currentClickMillis - lastClickMillis) + "ms");
			lastClickMillis = currentClickMillis;
			return result;
		}
	}

	/**
	 * 获取App版本号
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context)
	{
		String verName = "2.0.0";
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			verName = packInfo.versionName;

		} catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verName;
	}
	/**
	 * 判断是否联网
	 * 
	 * */
	public static boolean isNetworkConected(Context context) { 
		if (context != null) { 
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
		.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
		return mNetworkInfo.isAvailable(); 
		} 
		} 
		return false; 
		}
}
