package com.cn.android.zhengxun.app.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class LocalUtil {

	public static String getLoclPhonumber(Context context)
	{
	     TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	       
	       // String deviceid = tm.getDeviceId();
	        String tel = tm.getLine1Number();
	       // String imei = tm.getSimSerialNumber();
	        //String imsi = tm.getSubscriberId();
	        return tel;
	}
}
