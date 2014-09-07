package com.cn.android.zhengxun.app.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTool
{

	/**
	 * 读取InputStream的内容
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] read(InputStream is) throws Exception
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1)
		{
			os.write(buffer, 0, len);
		}
		is.close();
		return os.toByteArray();
	}

	public static void saveToSdCard(String filePath, InputStream is)
	{
		try
		{
			File file = new File(filePath);
			if (!file.getParentFile().exists())
			{
				file.getParentFile().mkdirs();
			}
			if (!file.exists())
			{
				file.createNewFile();
			}
			file.deleteOnExit();
			BufferedOutputStream os = null;
			os = new BufferedOutputStream(new FileOutputStream(file));
			os.write(read(is));
			os.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void saveToSdCard(String filePath, String content)
	{
		try
		{
			File file = new File(filePath);
			if (!file.getParentFile().exists())
			{
				file.getParentFile().mkdirs();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(content.getBytes());
			outStream.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
