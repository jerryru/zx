package com.cn.android.zhengxun.app.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.UrlConfig;
import com.cn.android.zhengxun.app.data.SendInfoData;
import com.cn.android.zhengxun.app.model.SendInfoModel;
import com.cn.android.zhengxun.app.service.SendInfoService;
import com.cn.android.zhengxun.app.util.DateUtil;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;

public class SendInfoServiceImpl implements SendInfoService {

	private Context context;
	private SQLiteDatabase db;
	private Handler handler;

	public SendInfoServiceImpl(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public List<SendInfoData> getAll() {
		List<SendInfoData> results = new ArrayList<SendInfoData>();
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from send_info order by SendDate desc limit 30";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			SendInfoData result = new SendInfoData();
			result.setId(cursor.getString(cursor.getColumnIndex("SendID")));
			result.setTitle(cursor.getString(cursor.getColumnIndex("InfoTitle")));
			result.setContent(cursor.getString(cursor
					.getColumnIndex("InfoContent")));
			result.setCreateDate(new Date((cursor.getLong(cursor
					.getColumnIndex("SendDate")))));

			results.add(result);
		}
		closeDB(cursor);
		return results;
	}

	@Override
	public SendInfoData getDetail(String sendId) {
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from send_info where SendID=?";
		String[] array = { sendId };
		Cursor cursor = db.rawQuery(sql, array);
		SendInfoData result = null;
		while (cursor.moveToNext()) {
			result = new SendInfoData();
			result.setId(cursor.getString(cursor.getColumnIndex("SendID")));
			result.setTitle(cursor.getString(cursor.getColumnIndex("InfoTitle")));
			result.setContent(cursor.getString(cursor
					.getColumnIndex("InfoContent")));
			try {
				result.setCreateDate(DateFormat.getDateInstance().parse(
						cursor.getString(cursor.getColumnIndex("SendDate"))));
			} catch (ParseException e) {
				Log.e("exception", "ParseException", e);
			}

		}
		closeDB(cursor);
		return result;
	}

	private void closeDB(Cursor cursor) {
		cursor.close();
		db.close();
	}

	@Override
	public List<SendInfoData> getSynChonizedAll() {
		List<SendInfoData> results = null;
		HttpTransportSE ht = new HttpTransportSE(UrlConfig.ROOT_URL);

		// ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject request = new SoapObject(StringConstants.SERVICE_NS,
				StringConstants.GET_SEND_INFO);
		Date date = DateUtil.addDays(-10);

		request.addProperty("SendDate", DateUtil.formatDate(date, "yyyy-MM-dd"));
		envelope.bodyOut = request;
		envelope.dotNet = false;
		envelope.setOutputSoapObject(request);
		try {
			ht.call(StringConstants.GET_SEND_INFO_ACTION, envelope);

			System.out.println(envelope.bodyIn.toString());
			SoapObject result1 = (SoapObject) envelope.bodyIn;
			String result = result1.getProperty(0).toString();

			// 解析JSON
			results = parserJson(result);

		} catch (IOException e) {
			Log.e("exception", "IOException", e);
		} catch (XmlPullParserException e) {
			Log.e("exception", "XmlPullParserException", e);
		} catch (JSONException e) {
			// 解析json出错
			Log.e("exception", "JSONException", e);
		} catch (ParseException e) {
			// 转换日期出错
			Log.e("exception", "ParseException", e);
		}
		return results;
	}

	private List<SendInfoData> parserJson(String result) throws JSONException,
			ParseException {
		JSONArray sendInfoObjects = new JSONArray(result);
		List<SendInfoData> sendInfos = new ArrayList<SendInfoData>();
		for (int i = 0; i < sendInfoObjects.length(); i++) {
			JSONObject sendInfoObject = sendInfoObjects.getJSONObject(i);
			SendInfoData sendInfo = new SendInfoData();
			sendInfo.setId(sendInfoObject.getString(SendInfoModel.SEND_ID));
			sendInfo.setContent(sendInfoObject
					.getString(SendInfoModel.INFO_CONTENT));
			sendInfo.setCreateDate(DateUtil.parser(
					sendInfoObject.getString(SendInfoModel.SEND_DATE),
					"yyyy-MM-dd"));
			sendInfo.setInfo_type(sendInfoObject
					.getString(SendInfoModel.INFO_TYPE));
			sendInfo.setTitle(sendInfoObject
					.getString(SendInfoModel.INFO_TITLE));
			sendInfo.setIs_synchonized(1);
			sendInfo.setSender(sendInfoObject.getString(SendInfoModel.SENDER));
			sendInfos.add(sendInfo);
		}
		return sendInfos;
	}

	@Override
	public boolean insertToDB(List<SendInfoData> sendInfos) {
		db = MySQLiteOpenHelper.openDatabase(context);
		if(null!=sendInfos){
		for (SendInfoData sendInfo : sendInfos) {
			ContentValues values = new ContentValues();
			values.put(SendInfoModel.SEND_ID, sendInfo.getId());
			values.put(SendInfoModel.INFO_CONTENT, sendInfo.getContent());
			values.put(SendInfoModel.INFO_TITLE, sendInfo.getTitle());
			values.put(SendInfoModel.INFO_TYPE, sendInfo.getInfo_type());
			values.put(SendInfoModel.IS_SYN, 1);
			values.put(SendInfoModel.SEND_DATE,
					DateUtil.formatDate(sendInfo.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			values.put(SendInfoModel.SENDER, sendInfo.getSender());

//			db.insert(SendInfoModel.DEPLOYMENT, null, values);
			db.insertWithOnConflict(SendInfoModel.DEPLOYMENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		}
		db.close();}
		return true;
	}
	
	
	public String domain(String resource){
		 resource = resource.replace("\"","//");
		 resource = resource.replace("/r/n","//r//n");
		 resource = resource.replace("/t","//t");
		 resource = resource.replace("//","//");
		 resource = resource.replace("/b","//b");
	        return resource;
	}

}
