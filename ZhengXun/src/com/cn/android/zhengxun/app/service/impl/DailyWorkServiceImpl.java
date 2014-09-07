package com.cn.android.zhengxun.app.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.TypeIds;
import com.cn.android.zhengxun.app.constants.UrlConfig;
import com.cn.android.zhengxun.app.data.AttendenceData;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.model.AttendenceInfoModel;
import com.cn.android.zhengxun.app.service.DailyWorkService;
import com.cn.android.zhengxun.app.util.DateUtil;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;
import com.cn.android.zhengxun.app.util.UserSessionUtil;

public class DailyWorkServiceImpl implements DailyWorkService {

	private Context context;
	private SQLiteDatabase db;

	public DailyWorkServiceImpl(Context context) {
		this.context = context;
	}

	@Override
	public boolean sigin(AttendenceInfoModel data) {
		return submit(data);
	}

	private boolean submit(AttendenceInfoModel data) {
		db = MySQLiteOpenHelper.openDatabase(context);
		ContentValues cValues = new ContentValues();
		cValues.put(AttendenceInfoModel.ATTENDENCE_ID, data.getAttendceId());
		cValues.put(AttendenceInfoModel.ATTENDENCE_LAT, data.getLat());
		cValues.put(AttendenceInfoModel.ATTENDENCE_LOCATION,
				data.getAttendence_address());
		cValues.put(AttendenceInfoModel.ATTENDENCE_LOT, data.getLot());
		cValues.put(AttendenceInfoModel.ATTENDENCE_DATE, DateUtil.formatDate(data.getAttendenceTime(),"yyyy-MM-dd HH:mm:ss"));
		cValues.put(AttendenceInfoModel.ATTENDENCE_TYPE, data.getAttendenceType());
		cValues.put(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK,
				data.getAbnormal_Positioning_Remark());
		cValues.put(AttendenceInfoModel.BCOMPANY_ID, data.getBcompanyId());
		cValues.put(AttendenceInfoModel.SELLER_CODE, data.getSellerCode());
		cValues.put(AttendenceInfoModel.ATTENDENCE_TOKEN, data.getAttendanceToken());
		cValues.put(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO,data.getAbnormalPositioningAudio());
		cValues.put(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO,data.getAbnorma_PositioningPhoto());
		cValues.put(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO_FILE, data.getAbnormalPositioningAudioFile());
		long result = db.insert(AttendenceInfoModel.DEPLOYMENT, null, cValues);
		if (result == -1l) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean sigout(AttendenceInfoModel data) {
		return submit(data);
	}

	@Override
	public boolean addAttendenceToServer(AttendenceInfoModel data) {
		HttpTransportSE ht = new HttpTransportSE(UrlConfig.ROOT_URL);

		// ht.debug = true;			
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject request = new SoapObject(StringConstants.SERVICE_NS,
				StringConstants.ADD_ATTENDENCE_INFO);
		request.addProperty(AttendenceInfoModel.SELLER_CODE,
				data.getSellerCode());
		
		request.addProperty(AttendenceInfoModel.BCOMPANY_ID,
				data.getBcompanyId());
		request.addProperty(AttendenceInfoModel.ATTENDENCE_TYPE,
				data.getAttendenceType());
		request.addProperty(AttendenceInfoModel.ATTENDENCE_DATE, DateUtil
				.formatDate(data.getAttendenceTime(), "yyyy-MM-dd HH:mm:ss"));
		request.addProperty(AttendenceInfoModel.ATTENDENCE_LOT, data.getLot());
		request.addProperty(AttendenceInfoModel.ATTENDENCE_LAT, data.getLat());
		request.addProperty(AttendenceInfoModel.ATTENDENCE_LOCATION,
				data.getAttendence_address());
		request.addProperty(AttendenceInfoModel.IS_SYN,data.getIsSynchronized());
		request.addProperty(AttendenceInfoModel.ATTENDENCE_TOKEN,
				data.getAttendanceToken());
		
		
		if(null!=data.getAbnormalPositioningAudio()){
		request.addProperty(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO,
				Base64.encode(data.getAbnormalPositioningAudio()));}else{
					request.addProperty(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO,
							Base64.encode(new byte[0]));	
				}
		if(null!=data.getAbnorma_PositioningPhoto()){
		request.addProperty(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO,
				Base64.encode(data.getAbnorma_PositioningPhoto()));}else{
					request.addProperty(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO,
							Base64.encode(new byte[0]));
				}
		request.addProperty(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK,
				data.getAbnormal_Positioning_Remark());
//		request.addProperty(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO,
//				Base64.encode(new byte[0]));
//		request.addProperty(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO,
//				Base64.encode(new byte[0]));
		
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		

		try {
			ht.call(StringConstants.ADD_ATTENDENCE_INFO_ACTION, envelope);			
			SoapObject result1 = (SoapObject) envelope.bodyIn;
			String result = result1.getProperty(0).toString();
			if("true".equals(result)){
				return true;
			}
		} catch (IOException e) {
			Log.e("IOException", "exception", e);
		} catch (XmlPullParserException e) {
			Log.e("XmlPullParserException", "exception", e);
		}

		return false;
	}

	@Override
	public void update(AttendenceInfoModel dailyWorkData) {
		
		db = MySQLiteOpenHelper.openDatabase(context);		
		ContentValues cValues=new ContentValues();
		cValues.put(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK, dailyWorkData.getAbnormal_Positioning_Remark());
		cValues.put(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO,dailyWorkData.getAbnormalPositioningAudio());
		cValues.put(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO_FILE, dailyWorkData.getAbnormalPositioningAudioFile());
		cValues.put(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO,dailyWorkData.getAbnorma_PositioningPhoto());
		if(dailyWorkData.getAttendanceToken()==TypeIds.DAILY_WORK_SIGN_OUT){
			cValues.put(AttendenceInfoModel.ATTENDENCE_DATE,DateUtil.formatDate(dailyWorkData.getAttendenceTime(),"yyyy-MM-dd HH:mm:ss"));
		}
		Date date=dailyWorkData.getAttendenceTime();
		Date EndDate=DateUtil.addDays(date,1);	
		String[] array={DateUtil.formatDate(date, "yyyy-MM-dd"),DateUtil.formatDate(EndDate, "yyyy-MM-dd"),dailyWorkData.getSellerCode(),dailyWorkData.getAttendanceToken()+""};
		db.update(AttendenceInfoModel.DEPLOYMENT, cValues, " Attendance_datetime>? and Attendance_datetime<? and SellerCode=? and Attendance_Token=?", array);
		db.close();
	}
	
	@Override
	public boolean isExist(AttendenceInfoModel dailyWorkData){
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql="select count(*) from Attendance_Info where Attendance_datetime>? and Attendance_datetime<? and SellerCode=? and Attendance_Token=?";
		Date date=dailyWorkData.getAttendenceTime();
		Date EndDate=DateUtil.addDays(date,1);		
		String[] array={DateUtil.formatDate(date, "yyyy-MM-dd"),DateUtil.formatDate(EndDate, "yyyy-MM-dd"),dailyWorkData.getSellerCode(),dailyWorkData.getAttendanceToken()+""};
		Cursor cursor=db.rawQuery(sql, array);
		int count=0;
		while(cursor.moveToNext()){
			count=cursor.getInt(cursor.getColumnIndex("count(*)"));
		}
		db.close();
		if(count==0){
		return false;}
		else{
			return true;
		}
	}

	@Override
	public List<AttendenceInfoModel> getAttendenceInfo(Date start, Date end) {
		List<AttendenceInfoModel> results=new ArrayList<AttendenceInfoModel>();
		db = MySQLiteOpenHelper.openDatabase(context);
		String startDate=DateUtil.formatDate(start, "yyyy-MM-dd");
		String endDate=DateUtil.formatDate(end, "yyyy-MM-dd");
		String sql="select * from Attendance_Info where Attendance_datetime>? and Attendance_datetime<? and SellerCode=? order by Attendance_datetime desc";
		String sellerCode=UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array={startDate,endDate,sellerCode};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			AttendenceInfoModel model=new AttendenceInfoModel();
			model.setLot(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOT)));
			model.setLat(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LAT)));
			model.setAbnorma_PositioningPhoto(cursor.getBlob(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO)));
			model.setAbnormal_Positioning_Remark(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK)));
			model.setAbnormalPositioningAudio(cursor.getBlob(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO)));
			model.setAbnormalPositioningAudioFile(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO_FILE)));
			model.setAbnormalPositioningRemark(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK)));
			model.setAttendanceLoaction(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOCATION)));
			model.setAttendanceToken(cursor.getInt(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_TOKEN)));
			model.setAttendceId(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_ID)));
			model.setAttendence_address(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOCATION)));
			try {
				model.setAttendenceTime(DateUtil.parser(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_DATE)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.e("exception", "parser time :ParseException", e);
			}
			model.setAttendenceType(cursor.getInt(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_TYPE)));
			model.setBcompanyId(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.BCOMPANY_ID)));
			model.setIsSynchronized(cursor.getColumnIndex(AttendenceInfoModel.IS_SYN));
			results.add(model);
		}
		db.close();
		return results;
	}

	@Override
	public List<AttendenceInfoModel> getUnsynAttendenceInfos() {
		List<AttendenceInfoModel> results=new ArrayList<AttendenceInfoModel>();
		db = MySQLiteOpenHelper.openDatabase(context);
		
		String sql="select * from Attendance_Info where SellerCode=? and isSynchronized=0 order by Attendance_datetime desc";
		String sellerCode=UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array={sellerCode};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			AttendenceInfoModel model=new AttendenceInfoModel();
			model.setLot(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOT)));
			model.setLat(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LAT)));
			model.setAbnorma_PositioningPhoto(cursor.getBlob(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO)));
			model.setAbnormal_Positioning_Remark(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK)));
			model.setAbnormalPositioningAudio(cursor.getBlob(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO)));
			model.setAbnormalPositioningAudioFile(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO_FILE)));
			model.setAbnormalPositioningRemark(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK)));
			model.setAttendanceLoaction(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOCATION)));
			model.setAttendanceToken(cursor.getInt(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_TOKEN)));
			model.setAttendceId(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_ID)));
			model.setAttendence_address(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOCATION)));
			try {
				model.setAttendenceTime(DateUtil.parser(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_DATE)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.e("exception", "parser time :ParseException", e);
			}
			model.setSellerCode(sellerCode);
			model.setAttendenceType(cursor.getInt(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_TYPE)));
			model.setBcompanyId(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.BCOMPANY_ID)));
			model.setIsSynchronized(cursor.getColumnIndex(AttendenceInfoModel.IS_SYN));
			results.add(model);
		}
		db.close();
		return results;
	}

	@Override
	public void updateSynchronize(AttendenceInfoModel dailyWorkData ) {
		
		db = MySQLiteOpenHelper.openDatabase(context);		
		ContentValues cValues=new ContentValues();
		
		cValues.put(AttendenceInfoModel.IS_SYN,1);

		String[] array={dailyWorkData.getAttendceId()};
		db.update(AttendenceInfoModel.DEPLOYMENT, cValues, " Attendance_Id=?", array);
		db.close();
	}

	@Override
	public List<AttendenceInfoModel> getUnsynLocalAttendenceInfos() {
		List<AttendenceInfoModel> results=new ArrayList<AttendenceInfoModel>();
		db = MySQLiteOpenHelper.openDatabase(context);
		
		String sql="select * from Attendance_Info where SellerCode=? and isSynchronized=0 and Attendance_loaction='' order by Attendance_datetime desc";
		String sellerCode=UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array={sellerCode};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			AttendenceInfoModel model=new AttendenceInfoModel();
			model.setLot(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOT)));
			model.setLat(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LAT)));
			model.setAbnorma_PositioningPhoto(cursor.getBlob(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_PHOTO)));
			model.setAbnormal_Positioning_Remark(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK)));
			model.setAbnormalPositioningAudio(cursor.getBlob(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO)));
			model.setAbnormalPositioningAudioFile(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNORMAL_POSITONING_AUDIO_FILE)));
			model.setAbnormalPositioningRemark(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ABNOMAL_POSITIONING_REMARK)));
			model.setAttendanceLoaction(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOCATION)));
			model.setAttendanceToken(cursor.getInt(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_TOKEN)));
			model.setAttendceId(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_ID)));
			model.setAttendence_address(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_LOCATION)));
			try {
				model.setAttendenceTime(DateUtil.parser(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_DATE)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.e("exception", "parser time :ParseException", e);
			}
			model.setSellerCode(sellerCode);
			model.setAttendenceType(cursor.getInt(cursor.getColumnIndex(AttendenceInfoModel.ATTENDENCE_TYPE)));
			model.setBcompanyId(cursor.getString(cursor.getColumnIndex(AttendenceInfoModel.BCOMPANY_ID)));
			model.setIsSynchronized(cursor.getColumnIndex(AttendenceInfoModel.IS_SYN));
			results.add(model);
		}
		db.close();
		return results;
	}

	@Override
	public void updateLocal(AttendenceInfoModel dailyWorkData) {
		db = MySQLiteOpenHelper.openDatabase(context);		
		ContentValues cValues=new ContentValues();
		
		cValues.put(AttendenceInfoModel.ATTENDENCE_LOCATION, dailyWorkData.getAttendanceLoaction());
		
		String[] array={dailyWorkData.getAttendceId()};
		db.update(AttendenceInfoModel.DEPLOYMENT, cValues, " Attendance_Id=?", array);
		db.close();
		
	}

}
