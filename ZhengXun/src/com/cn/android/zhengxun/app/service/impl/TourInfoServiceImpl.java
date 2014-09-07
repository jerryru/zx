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
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.activity.TourActivity;
import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.UrlConfig;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.model.TourModel;
import com.cn.android.zhengxun.app.service.TourInfoService;
import com.cn.android.zhengxun.app.util.DateUtil;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;
import com.cn.android.zhengxun.app.util.UserSessionUtil;

public class TourInfoServiceImpl implements TourInfoService {
	private Handler handler;
	private Context context;
	private SQLiteDatabase db;

	public TourInfoServiceImpl(Context context){
		this.context=context;
	}
	public TourInfoServiceImpl(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public boolean synChronizeWithServer(TourModel tourModel) {
		HttpTransportSE ht = new HttpTransportSE(UrlConfig.ROOT_URL);

		// ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject request = new SoapObject(StringConstants.SERVICE_NS,
				StringConstants.ADD_TOUR_INFO);
		
		request.addProperty(TourModel.Bcompany_Id, tourModel.getBcompanyId());
		request.addProperty(TourModel.Tour_in_time, DateUtil.formatDate(
				tourModel.getTourInTime(), "yyyy-MM-dd HH:mm:ss"));
		if(null!=tourModel.getDoor_Photo()){
		request.addProperty(TourModel.Door_Photo,
				Base64.encode(tourModel.getDoor_Photo()));}else{
					request.addProperty(TourModel.Door_Photo,Base64.encode(new byte[0]));
				}
		if(null!=tourModel.getDisplay_Photo()){
		request.addProperty(TourModel.Display_Photo,
				Base64.encode(tourModel.getDisplay_Photo()));
		}else{
			request.addProperty(TourModel.Display_Photo,
					Base64.encode(new byte[0]));
		}
		request.addProperty(TourModel.Tour_Remark, tourModel.getTour_Remark());
		request.addProperty(TourModel.Tour_in_lat, tourModel.getTour_in_lat());
		request.addProperty(TourModel.Tour_in_lot, tourModel.getTour_in_lot());
		request.addProperty(TourModel.Tour_in_Location,
				tourModel.getTour_in_Location());
		request.addProperty(TourModel.Tour_out_Time, DateUtil.formatDate(
				tourModel.getTour_out_Time(), "yyyy-MM-dd HH:mm:ss"));
		request.addProperty(TourModel.tour_out_lat, tourModel.getTourOutLat());	
		request.addProperty(TourModel.tour_out_lon, tourModel.getTourOutLon());
		request.addProperty(TourModel.Tour_out_Location,
				tourModel.getTour_out_Location());
		if(null!=tourModel.getTour_audio()){
		request.addProperty(TourModel.Tour_audio,
				Base64.encode(tourModel.getTour_audio()));}else{
					request.addProperty(TourModel.Tour_audio,
							Base64.encode(new byte[0]));	
				}
		request.addProperty("Abnormal_Out_Remark",
				tourModel.getAbnormal_Positioning_Remark());
		request.addProperty("Abnormal_In_Remark",
				tourModel.getAbnormal_Positioning_Remark());
		if(null!=tourModel.getAbnormal_Positioning_Photo()){
		request.addProperty(TourModel.Abnormal_Positioning_Photo,
				Base64.encode(tourModel.getAbnormal_Positioning_Photo()));
		}else{
			request.addProperty(TourModel.Abnormal_Positioning_Photo,
					Base64.encode(new byte[0]));
		}
		if(null!=tourModel.getAbnormal_Positioning_Audio()){
		request.addProperty(TourModel.Abnormal_Positioning_Audio,
				Base64.encode(tourModel.getAbnormal_Positioning_Audio()));}else{
					request.addProperty(TourModel.Abnormal_Positioning_Audio,
							Base64.encode(new byte[0]));
				}
		request.addProperty(TourModel.Sellercode, tourModel.getSellercode());
		
		// request.addProperty(TourModel.Abnormal_Positioning_Remark,
		// tourModel.getAbnormal_Positioning_Remark());
		request.addProperty(TourModel.Appraise_State,
				tourModel.getAppraiseState());
		request.addProperty(TourModel.Stock_State, tourModel.getStockState());		
		request.addProperty(TourModel.IsSynchronized, 1);
		
//		request.addProperty(TourModel.Tour_Id, tourModel.getTourId());
		
//		request.addProperty(TourModel.User_Id, tourModel.getUserId());
		
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		try {
			ht.call(StringConstants.ADD_TOUR_INFO_ATION, envelope);
			SoapObject result1 = (SoapObject) envelope.bodyIn;
			String result = result1.getProperty(0).toString().trim();
			if ("true".equals(result)) {
				return true;
			}

		} catch (IOException e) {
			Log.e("exception", "IOException", e);
		} catch (XmlPullParserException e) {
			Log.e("exception", "XmlPullParserException", e);
		}

		return false;
	}

	@Override
	public boolean updateInDB(TourModel model) {
		db = MySQLiteOpenHelper.openDatabase(context);
		ContentValues values = new ContentValues();
		String[] array={model.getTourId()};
		
		values.put(TourModel.Tour_in_Location, model.getTour_in_Location());
		values.put(TourModel.Tour_out_Location, model.getTour_out_Location());

		int len = db.update(TourModel.DEPLOYMENT,values," Tour_Id=?", array);
		if(len>0){
			return true;
		}else{
		return false;}
	}

	@Override
	public boolean insertDB(TourModel model) {
		model.setIsSynchronized(0);
		db = MySQLiteOpenHelper.openDatabase(context);
		ContentValues values = new ContentValues();
		values.put(TourModel.Abnormal_Positioning_Audio,
				model.getAbnormal_Positioning_Audio());
		values.put(TourModel.Abnormal_Positioning_Audio_File,
				model.getAbnormal_Positioning_Audio_File());
		values.put(TourModel.Abnormal_Positioning_Photo,
				model.getAbnormal_Positioning_Photo());

		values.put(TourModel.Abnormal_Positioning_Remark,
				model.getAbnormal_Positioning_Remark());
		values.put(TourModel.Appraise_State, model.getAppraiseState());
		values.put(TourModel.Bcompany_Id, model.getBcompanyId());
		values.put(TourModel.Display_Photo,
				model.getDisplay_Photo());
		values.put(TourModel.Door_Photo, model.getDoor_Photo());
		values.put(TourModel.IsSynchronized, model.getIsSynchronized());
		values.put(TourModel.Sellercode, model.getSellercode());
		values.put(TourModel.Stock_State, model.getStockState());
		values.put(TourModel.Tour_audio, model.getTour_audio());
		values.put(TourModel.Tour_audio_File, model.getTour_audio_file());
		values.put(TourModel.Tour_audio_length, model.getTour_audio_length());
		values.put(TourModel.Tour_Id, model.getTourId());
		values.put(TourModel.Tour_in_lat, model.getTour_in_lat());
		values.put(TourModel.Tour_in_Location, model.getTour_in_Location());
		values.put(TourModel.Tour_in_lot, model.getTour_in_lot());
		values.put(TourModel.Tour_in_time, DateUtil.formatDate(model.getTourInTime(),"yyyy-MM-dd HH:mm:ss"));
		values.put(TourModel.tour_out_lat, model.getTourOutLat());
		values.put(TourModel.Tour_out_Location, model.getTour_out_Location());
		values.put(TourModel.tour_out_lon, model.getTourOutLon());
		values.put(TourModel.Tour_out_Time, DateUtil.formatDate(model.getTour_out_Time(),"yyyy-MM-dd HH:mm:ss"));
		values.put(TourModel.Tour_Remark, model.getTour_Remark());
		values.put(TourModel.User_Id, model.getUserId());
		values.put(TourModel.IS_NORMAL, model.getIsNormal());
		long l = db.insert(TourModel.DEPLOYMENT, null, values);
		Log.i("Insert Result", l + "");
		db.close();
		if (l == -1l) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public List<TourModel> getTourInfos(Date start, Date end) {
		List<TourModel> results=new ArrayList<TourModel>();
		String startDate=DateUtil.formatDate(start, "yyyy-MM-dd");
		String endDate=DateUtil.formatDate(end, "yyyy-MM-dd");
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql="select * from Tour_Info where Tour_in_time>? and Tour_in_time<? and Sellercode=?";
		String sellerCode=UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array={startDate,endDate,sellerCode};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			TourModel tour=new TourModel();
			tour.setAbnormal_Positioning_Audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Audio)));
			tour.setAbnormal_Positioning_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Photo)));
			tour.setAbnormal_Positioning_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Remark)));
			tour.setAppraiseState(cursor.getInt(cursor.getColumnIndex(TourModel.Appraise_State)));
			tour.setBcompanyId(cursor.getString(cursor.getColumnIndex(TourModel.Bcompany_Id)));
			tour.setDisplay_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Display_Photo)));
			tour.setDoor_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Door_Photo)));
			tour.setIsSynchronized(cursor.getInt(cursor.getColumnIndex(TourModel.IsSynchronized)));
			tour.setSellercode(sellerCode);
			tour.setStockState(cursor.getInt(cursor.getColumnIndex(TourModel.Stock_State)));
			tour.setTour_audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Tour_audio)));
			tour.setTour_in_lat(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lat)));
			tour.setTour_in_Location(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_Location)));
			tour.setTour_in_lot(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lot)));
			tour.setTour_out_Location(cursor.getString(cursor.getColumnIndex(TourModel.Tour_out_Location)));
			try {
				tour.setTourInTime(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_time)), "yyyy-MM-dd HH:mm:ss"));
				tour.setTour_out_Time(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_out_Time)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("Exception", "ParseException", e);
			}
			tour.setTour_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Remark)));
			tour.setTourId(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Id)));
			tour.setTourOutLat(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lat)));
			tour.setTourOutLon(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lon)));
			tour.setUserId(cursor.getString(cursor.getColumnIndex(TourModel.User_Id)));
			tour.setIsNormal(cursor.getInt(cursor.getColumnIndex(TourModel.IS_NORMAL)));
			results.add(tour);
		}
		db.close();
		return results;
	}
	@Override
	public TourModel getTourById(String tourId) {
		TourModel tour=new TourModel();
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql="select * from Tour_Info where Tour_Id=?";
		
		String[] array={tourId};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			
			tour.setAbnormal_Positioning_Audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Audio)));
			tour.setAbnormal_Positioning_Audio_File(cursor.getString(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Audio_File)));
			tour.setAbnormal_Positioning_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Photo)));
			tour.setAbnormal_Positioning_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Remark)));
			tour.setAppraiseState(cursor.getInt(cursor.getColumnIndex(TourModel.Appraise_State)));
			tour.setBcompanyId(cursor.getString(cursor.getColumnIndex(TourModel.Bcompany_Id)));
			tour.setDisplay_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Display_Photo)));
			tour.setDoor_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Door_Photo)));
			tour.setIsSynchronized(cursor.getInt(cursor.getColumnIndex(TourModel.IsSynchronized)));
			tour.setSellercode(cursor.getString(cursor.getColumnIndex(TourModel.Sellercode)));
			tour.setStockState(cursor.getInt(cursor.getColumnIndex(TourModel.Stock_State)));
			tour.setTour_audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Tour_audio)));
			tour.setTour_audio_file(cursor.getString(cursor.getColumnIndex(TourModel.Tour_audio_File)));
			tour.setTour_audio_length(cursor.getInt(cursor.getColumnIndex(TourModel.Tour_audio_length)));
			tour.setTour_in_lat(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lat)));
			tour.setTour_in_Location(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_Location)));
			tour.setTour_in_lot(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lot)));
			tour.setTour_out_Location(cursor.getColumnName(cursor.getColumnIndex(TourModel.Tour_out_Location)));
			try {
				tour.setTourInTime(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_time)), "yyyy-MM-dd HH:mm:ss"));
				tour.setTour_out_Time(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_out_Time)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("Exception", "ParseException", e);
			}
			tour.setTour_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Remark)));
			tour.setTourId(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Id)));
			tour.setTourOutLat(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lat)));
			tour.setTourOutLon(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lon)));
			tour.setUserId(cursor.getString(cursor.getColumnIndex(TourModel.User_Id)));
			tour.setIsNormal(cursor.getInt(cursor.getColumnIndex(TourModel.IS_NORMAL)));
		}
		db.close();
		return tour;
	}
	@Override
	public List<TourModel> getAsynTourInfos() {
		List<TourModel> results=new ArrayList<TourModel>();		
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql="select * from Tour_Info where Sellercode=? and isSynchronized=0";
		String sellerCode=UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array={sellerCode};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			TourModel tour=new TourModel();
			tour.setAbnormal_Positioning_Audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Audio)));
			tour.setAbnormal_Positioning_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Photo)));
			tour.setAbnormal_Positioning_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Remark)));
			tour.setAppraiseState(cursor.getInt(cursor.getColumnIndex(TourModel.Appraise_State)));
			tour.setBcompanyId(cursor.getString(cursor.getColumnIndex(TourModel.Bcompany_Id)));
			tour.setDisplay_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Display_Photo)));
			tour.setDoor_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Door_Photo)));
			tour.setIsSynchronized(cursor.getInt(cursor.getColumnIndex(TourModel.IsSynchronized)));
			tour.setSellercode(sellerCode);
			tour.setStockState(cursor.getInt(cursor.getColumnIndex(TourModel.Stock_State)));
			tour.setTour_audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Tour_audio)));
			tour.setTour_in_lat(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lat)));
			tour.setTour_in_Location(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_Location)));
			tour.setTour_in_lot(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lot)));
			tour.setTour_out_Location(cursor.getString(cursor.getColumnIndex(TourModel.Tour_out_Location)));
			try {
				tour.setTourInTime(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_time)), "yyyy-MM-dd HH:mm:ss"));
				tour.setTour_out_Time(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_out_Time)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("Exception", "ParseException", e);
			}
			tour.setTour_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Remark)));
			tour.setTourId(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Id)));
			tour.setTourOutLat(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lat)));
			tour.setTourOutLon(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lon)));
			tour.setUserId(cursor.getString(cursor.getColumnIndex(TourModel.User_Id)));
			tour.setIsNormal(cursor.getInt(cursor.getColumnIndex(TourModel.IS_NORMAL)));
			results.add(tour);
		}
		db.close();
		return results;
	}
	@Override
	public boolean updateSynchronize(TourModel model) {
		db = MySQLiteOpenHelper.openDatabase(context);
		ContentValues values = new ContentValues();
		String[] array={model.getTourId()};
		values.put(TourModel.IsSynchronized,1);

		int len = db.update(TourModel.DEPLOYMENT,values," Tour_Id=?", array);
		if(len>0){
			return true;
		}else{
		return false;}
	}
	@Override
	public List<TourModel> getAsynLocalTourInfos() {
		List<TourModel> results=new ArrayList<TourModel>();		
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql="select * from Tour_Info where Sellercode=? and isSynchronized=0 and Tour_in_Location=''";
		String sellerCode=UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array={sellerCode};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			TourModel tour=new TourModel();
			tour.setAbnormal_Positioning_Audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Audio)));
			tour.setAbnormal_Positioning_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Photo)));
			tour.setAbnormal_Positioning_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Abnormal_Positioning_Remark)));
			tour.setAppraiseState(cursor.getInt(cursor.getColumnIndex(TourModel.Appraise_State)));
			tour.setBcompanyId(cursor.getString(cursor.getColumnIndex(TourModel.Bcompany_Id)));
			tour.setDisplay_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Display_Photo)));
			tour.setDoor_Photo(cursor.getBlob(cursor.getColumnIndex(TourModel.Door_Photo)));
			tour.setIsSynchronized(cursor.getInt(cursor.getColumnIndex(TourModel.IsSynchronized)));
			tour.setSellercode(sellerCode);
			tour.setStockState(cursor.getInt(cursor.getColumnIndex(TourModel.Stock_State)));
			tour.setTour_audio(cursor.getBlob(cursor.getColumnIndex(TourModel.Tour_audio)));
			tour.setTour_in_lat(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lat)));
			tour.setTour_in_Location(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_Location)));
			tour.setTour_in_lot(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_lot)));
			tour.setTour_out_Location(cursor.getString(cursor.getColumnIndex(TourModel.Tour_out_Location)));
			try {
				tour.setTourInTime(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_in_time)), "yyyy-MM-dd HH:mm:ss"));
				tour.setTour_out_Time(DateUtil.parser(cursor.getString(cursor.getColumnIndex(TourModel.Tour_out_Time)),"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("Exception", "ParseException", e);
			}
			tour.setTour_Remark(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Remark)));
			tour.setTourId(cursor.getString(cursor.getColumnIndex(TourModel.Tour_Id)));
			tour.setTourOutLat(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lat)));
			tour.setTourOutLon(cursor.getString(cursor.getColumnIndex(TourModel.tour_out_lon)));
			tour.setUserId(cursor.getString(cursor.getColumnIndex(TourModel.User_Id)));
			tour.setIsNormal(cursor.getInt(cursor.getColumnIndex(TourModel.IS_NORMAL)));
			results.add(tour);
		}
		db.close();
		return results;
	}

}
