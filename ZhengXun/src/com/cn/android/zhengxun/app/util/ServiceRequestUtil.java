package com.cn.android.zhengxun.app.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class ServiceRequestUtil
{

	private static Map<String, String> parmas = new HashMap<String, String>();
	private static HttpResponse response;

	/**
	 * 以表单的形式并采用post方式将数据提交给给服务器
	 * 
	 * @param url
	 *            action
	 * @param parmas
	 *            表单中的参数
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse postRequest(String url,
			Map<String, String> parmas) throws URISyntaxException,
			ClientProtocolException, IOException
	{

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000); // socket连接超时
		HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000); // socket读取超时

		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost request = new HttpPost();
		request.setHeader("Content-type", "application/x-www-form-urlencoded");
		request.setURI(new URI(url));

		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		System.out.println(url);
		if (parmas != null)
		{
			Set<String> keys = parmas.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();)
			{
				String key = (String) i.next();
				pairs.add(new BasicNameValuePair(key, parmas.get(key)));
				System.out.println(key + "=" + parmas.get(key) + "");
			}
		}
		request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8")); // 设置参数的编码
		response = client.execute(request);
		return response;

	}

	public static HttpResponse deleteFavorites(String url,
			Map<String, String[]> parmas) throws URISyntaxException,
			ClientProtocolException, IOException
	{

		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost();
		request.setHeader("Content-type", "application/x-www-form-urlencoded");
		request.setURI(new URI(url));

		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		if (parmas != null)
		{
			Set<String> keys = parmas.keySet();
			for (Iterator<String> i = keys.iterator(); i.hasNext();)
			{
				String key = (String) i.next();
				for (int j = 0; j < parmas.get(key).length; j++)
				{
					pairs.add(new BasicNameValuePair(key, parmas.get(key)[j]));
				}
			}
		}

		request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8")); // 设置参数的编码
		response = client.execute(request);
		return response;

	}

	public static Map<String, String> getParmas()
	{
		return parmas;
	}

}
