package com.cn.android.zhengxun.app.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

public class StringUtil {
	
	public static final String USER_TMP_FILE = "user.tmp";
	public static String USER_TOKEN_PATH=Environment.getExternalStorageDirectory()+File.separator+"Android"+File.separator+"data";
	public static String USER_TOKEN_FILE="user.zx";
	public static String DB_FILE_NAME="zx_db.db";
	
	
	public static String getStringByDateTime(){
		
		Calendar c=Calendar.getInstance();
		Date time=c.getTime(); 
		DateFormat format=new SimpleDateFormat("yyMMDDHHmmssSSS");
		
		String date=format.format(time);
		
		System.out.println(date);
		return date;
	}
	
}
