package com.cn.android.zhengxun.app.widgets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 用于生成id 
 * @author dluo
 *
 */
public class IdGenerator {


	private static Date date = new Date();
	private static StringBuilder buf = new StringBuilder();
	private static  Random random = new Random();

	public static synchronized String getId(){
		if(null!=buf)
			buf.delete(0, buf.length());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//时间格式
		Date nowDate = new Date();//得到当前时间
		String st= sdf.format(nowDate);
		st=st.substring(2, st.length());
		for(int i=0;i<=st.length()-2;i=i+2)
		{
			String st1String=st.substring(i, i+2);
			char x=turnformate(st1String);
			if(x!='!')
			buf.append(x);
			
		}

		int	seq=Math.abs(random.nextInt()%61);
		int	seq1=Math.abs(random.nextInt()%61);
		buf.append(turnformate(String.valueOf(seq)));
		buf.append(turnformate(String.valueOf(seq1)));	
		return buf.toString();
	}

	public static char turnformate(String st)
	{
		int k=Integer.parseInt(st);
		char x='!';
		if(k<=9)
		{
			x=st.charAt(st.length()-1);
			return x;
		}
		else if(k>9&&k<=35)
		{
			int i=k-9;
			i=i+96;
			return (char) i ;
		}
		else if(k>=36&&k<=61)
		{
			int i=k-36;
			i=i+65;
			return (char) i;
		}
		else
			return x;


	}

}
