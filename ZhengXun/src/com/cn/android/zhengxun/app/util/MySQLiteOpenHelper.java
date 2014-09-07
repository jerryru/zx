package com.cn.android.zhengxun.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class MySQLiteOpenHelper{


	
	public static SQLiteDatabase openDatabase(Context context,String path,String fileName)
	{
		 // 获得文件的绝对路径 
        String databaseFilename = path + File.separator+context.getPackageName()+File.separator + fileName;
        File file = new File(databaseFilename);
        SQLiteDatabase database = null;
        try {   		
        if(!file.getParentFile().exists())
        {
        	file.getParentFile().mkdirs();
        }
        if (!file.exists()) {  
            InputStream is;
            file.createNewFile();
				is = context.getAssets().open(fileName);
				
            FileOutputStream fos = new FileOutputStream(databaseFilename);  
            byte[] buffer = new byte[8192];  
            int count = 0;  
            // 开始复制文件  
            while ((count = is.read(buffer)) > 0) {  
                fos.write(buffer, 0, count);  
            }  
            fos.close();  
            is.close();   
            
        }
        database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);  
    	} catch (IOException e) {
			Log.e("exception", "IOException", e);
			return null;
		}
		
		return database;  
		
		
	}
	
	public static SQLiteDatabase openLocalDatabase(Context context,String localpath,String targetFileName,String dbFileName)
	{
		// 获得文件的绝对路径 
		String databaseFilename = localpath + File.separator + targetFileName;
		File dir = new File(localpath);
		SQLiteDatabase database = null;
		try {
			
			
			if(!dir.exists())
			{
				dir.mkdir();
			}
			//如果不存在数据库文件，则从程序中复制一份模板数据库
			if (!(new File(databaseFilename)).exists()) {  
				InputStream is;
				
				is = context.getAssets().open(dbFileName);
				
				FileOutputStream fos = new FileOutputStream(databaseFilename);  
				byte[] buffer = new byte[8192];  
				int count = 0;  
				// 开始复制文件  
				while ((count = is.read(buffer)) > 0) {  
					fos.write(buffer, 0, count);  
				}  
				fos.close();  
				is.close();   
				
			}
			database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return database;  
		
		
	}
	
	public static SQLiteDatabase openDatabase(Context context){
		
		return MySQLiteOpenHelper.openDatabase(context, StringUtil.USER_TOKEN_PATH, StringUtil.DB_FILE_NAME);
		
	}
	
	public static SQLiteDatabase openSDcardDatabase(Context context){
		return MySQLiteOpenHelper.openLocalDatabase(context, StringUtil.USER_TOKEN_PATH+File.separator+context.getPackageName(), StringUtil.DB_FILE_NAME, StringUtil.DB_FILE_NAME);
	}
}
