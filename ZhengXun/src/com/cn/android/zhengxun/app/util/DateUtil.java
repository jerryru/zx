package com.cn.android.zhengxun.app.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
 
	public static Date addDays(int days){
		Calendar c=Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}
	
	public static Date addDays(Date date,int days){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}
	public static String formatDate(Date date,String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		if(null!=date){
		return sdf.format(date);}else{
			return "";
		}
	}
	
	public static Date parser(String date,String pattern) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if(!"".equals(date)&&null!=date){
		return sdf.parse(date);}else{
			return null;
		}
	}
}
