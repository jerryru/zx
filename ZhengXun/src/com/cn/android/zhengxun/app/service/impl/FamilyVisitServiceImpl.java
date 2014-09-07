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

import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.UrlConfig;
import com.cn.android.zhengxun.app.data.UserData;
import com.cn.android.zhengxun.app.model.HomeVisitModel;
import com.cn.android.zhengxun.app.service.FamilyVisitService;
import com.cn.android.zhengxun.app.util.DateUtil;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;
import com.cn.android.zhengxun.app.util.UserSessionUtil;
import com.cn.android.zhengxun.app.widgets.IdGenerator;

public class FamilyVisitServiceImpl implements FamilyVisitService {

	private Context context;
	private Handler handler;
	private SQLiteDatabase db;

	public FamilyVisitServiceImpl(Context context) {
		this.context = context;
	}

	public FamilyVisitServiceImpl(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public boolean synchronziedWithServer(HomeVisitModel model) {
		HttpTransportSE ht = new HttpTransportSE(UrlConfig.ROOT_URL);

		// ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject request = new SoapObject(StringConstants.SERVICE_NS,
				StringConstants.ADD_FAMILY_VISIT);
		request.addProperty(HomeVisitModel.Bcompany_Id,model.getBcompany_Id());
		request.addProperty(HomeVisitModel.Clerk_Id,model.getClerk_Id());
		request.addProperty(HomeVisitModel.Door_Photo,Base64.encode(model.getDoor_Photo()));
		request.addProperty(HomeVisitModel.Gift_Photo,Base64.encode(model.getGift_Photo()));
		if(null!=model.getVisit_audio()){
		request.addProperty(HomeVisitModel.Visit_audio,Base64.encode(model.getVisit_audio()));}else{
			request.addProperty(HomeVisitModel.Visit_audio,Base64.encode(new byte[0]));
		}
		request.addProperty(HomeVisitModel.Visit_Remark,model.getVisit_Remark());
		request.addProperty(HomeVisitModel.Abnormal_in_Remark,model.getAbnormal_In_Remark());
		request.addProperty(HomeVisitModel.ABNORMAL_OUT_REMARK,model.getAbnormal_Out_Remark());
		if(null!=model.getAbnormal_Positioning_Photo()){
		request.addProperty(HomeVisitModel.Abnormal_Positioning_Photo,Base64.encode(model.getAbnormal_Positioning_Photo()));
		}else{
			request.addProperty(HomeVisitModel.Abnormal_Positioning_Photo,Base64.encode(new byte[0]));
		}
		if(null!=model.getAbnormal_Positioning_Audio()){
		request.addProperty(HomeVisitModel.Abnormal_Positioning_Audio,Base64.encode(model.getAbnormal_Positioning_Audio()));
		}else{
			request.addProperty(HomeVisitModel.Abnormal_Positioning_Audio,Base64.encode(new byte[0]));
		}
		request.addProperty(HomeVisitModel.Visit_out_Location,model.getVisit_out_Location());
		request.addProperty(HomeVisitModel.Visit_out_Lon,model.getVisit_out_Lon());
		request.addProperty(HomeVisitModel.Visit_out_Lat,model.getVisit_out_Lat());
		request.addProperty(HomeVisitModel.Visit_out_Time,DateUtil.formatDate(model.getVisit_out_Time(),"yyyy-MM-dd HH:mm:ss"));
		request.addProperty(HomeVisitModel.Visit_in_Location,model.getVisit_in_Location());
		request.addProperty(HomeVisitModel.Visit_in_Lon,model.getVisit_in_Lon());
		request.addProperty(HomeVisitModel.Visit_in_Lat,model.getVisit_in_Lat());
		request.addProperty(HomeVisitModel.Visit_in_Time,DateUtil.formatDate(model.getVisit_in_Time(),"yyyy-MM-dd HH:mm:ss"));
		request.addProperty(HomeVisitModel.IsSynchronized,1);
		request.addProperty(HomeVisitModel.SellerCode,model.getSellerCode());
		request.addProperty(HomeVisitModel.Appraise_State,model.getAppraise_State());
		
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		try {
			ht.call(StringConstants.ADD_FAMILY_VISIT_ACTION, envelope);
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
	public boolean updateInDB(HomeVisitModel model) {
		db = MySQLiteOpenHelper.openDatabase(context);
		ContentValues values = new ContentValues();		
		
		values.put(HomeVisitModel.Visit_in_Location, model.getVisit_in_Location());
		values.put(HomeVisitModel.Visit_out_Location, model.getVisit_out_Location());
		
		String[] array={model.getFamily_Visit_Id()};
		int l = db.update(HomeVisitModel.DEPLOYMENT,values," Family_Visit_Id=?",array);
		if(l>0){
			return true;
		}else{
		return false;
		}
	}

	@Override
	public boolean insertDB(HomeVisitModel model) {
		db = MySQLiteOpenHelper.openDatabase(context);
		ContentValues values = new ContentValues();
		values.put(HomeVisitModel.Abnormal_Positioning_Audio,
				model.getAbnormal_Positioning_Audio());
		values.put(HomeVisitModel.Abnormal_Positioning_Photo,
				model.getAbnormal_Positioning_Photo());
		values.put(HomeVisitModel.Abnormal_Positioning_Remark,
				model.getAbnormal_Positioning_Remark());
		values.put(HomeVisitModel.Appraise_State, model.getAppraise_State());
		values.put(HomeVisitModel.Bcompany_Id, model.getBcompany_Id());
		values.put(HomeVisitModel.Clerk_Id, model.getClerk_Id());
		values.put(HomeVisitModel.Door_Photo, model.getDoor_Photo());
		values.put(HomeVisitModel.Family_Visit_Id, IdGenerator.getId());
		values.put(HomeVisitModel.Gift_Photo, model.getGift_Photo());
		values.put(HomeVisitModel.IsSynchronized, model.getIsSynchronized());
		values.put(HomeVisitModel.SellerCode, model.getSellerCode());
		values.put(HomeVisitModel.User_Id, model.getUser_Id());
		values.put(HomeVisitModel.Visit_audio, model.getVisit_audio());
		values.put(HomeVisitModel.Visit_audio_File, model.getVisit_audio_File());
		values.put(HomeVisitModel.Visit_audio_length, model.getVisit_audio_length());
		values.put(HomeVisitModel.Abnormal_Positioning_Audio_File, model.getAbnormal_Positioning_Audio_File());
		values.put(HomeVisitModel.Visit_in_Lat, model.getVisit_in_Lat());
		values.put(HomeVisitModel.Visit_in_Location,
				model.getVisit_in_Location());
		values.put(HomeVisitModel.Visit_in_Lon, model.getVisit_in_Lon());
		values.put(HomeVisitModel.Visit_in_Time, DateUtil.formatDate(
				model.getVisit_in_Time(), "yyyy-MM-dd HH:mm:ss"));
		values.put(HomeVisitModel.Visit_out_Lat, model.getVisit_out_Lat());
		values.put(HomeVisitModel.Visit_out_Location,
				model.getVisit_out_Location());
		values.put(HomeVisitModel.Visit_out_Lon, model.getVisit_out_Lon());
		values.put(HomeVisitModel.Visit_out_Time, DateUtil.formatDate(
				model.getVisit_out_Time(), "yyyy-MM-dd HH:mm:ss"));
		values.put(HomeVisitModel.Visit_Remark, model.getVisit_Remark());
		long l = db.insert(HomeVisitModel.DEPLOYMENT, null, values);
		db.close();
		if (l == -1l) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<HomeVisitModel> getVisits(Date start, Date end) {
		List<HomeVisitModel> results = new ArrayList<HomeVisitModel>();
		String startDate = DateUtil.formatDate(start, "yyyy-MM-dd");
		String endDate = DateUtil.formatDate(end, "yyyy-MM-dd");
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Family_Visit_Info where Visit_in_Time>? and Visit_in_Time<? and SellerCode=? order by Visit_in_Time";
		String sellerCode = UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array = { startDate, endDate, sellerCode };
		Cursor cursor = db.rawQuery(sql, array);
		while (cursor.moveToNext()) {
			HomeVisitModel visit = new HomeVisitModel();
			visit.setAbnormal_Positioning_Audio(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Audio)));
			visit.setAbnormal_Positioning_Audio_File(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Audio_File)));
			visit.setAbnormal_Positioning_Photo(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Photo)));
			visit.setAbnormal_Positioning_Remark(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Remark)));
			visit.setBcompany_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Bcompany_Id)));
			visit.setClerk_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Clerk_Id)));
			visit.setFamily_Visit_Id(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Family_Visit_Id)));
			visit.setVisit_audio(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Visit_audio)));
			visit.setVisit_audio_File(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_audio_File)));
			visit.setSellerCode(sellerCode);
			visit.setVisit_in_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Location)));
			visit.setVisit_out_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Location)));
			visit.setVisit_in_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lat)));
			visit.setVisit_in_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lon)));
			visit.setVisit_out_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lat)));
			visit.setVisit_out_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lon)));
			try {
				visit.setVisit_in_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_in_Time)),
						"yyyy-MM-dd HH:mm:ss"));
				visit.setVisit_out_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_out_Time)),
						"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("exception", "parse time exception", e);
			}
			results.add(visit);
		}
		db.close();
		return results;
	}

	@Override
	public HomeVisitModel getVisitById(String visitId) {
		HomeVisitModel visit = new HomeVisitModel();
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Family_Visit_Info where Family_Visit_Id=?";
		String[] array = {visitId};
		Cursor cursor = db.rawQuery(sql, array);
		while (cursor.moveToNext()) {
			
			visit.setAbnormal_Positioning_Audio(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Audio)));
			visit.setAbnormal_Positioning_Photo(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Photo)));
			visit.setAbnormal_Positioning_Remark(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Remark)));
			visit.setBcompany_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Bcompany_Id)));
			visit.setClerk_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Clerk_Id)));
			visit.setFamily_Visit_Id(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Family_Visit_Id)));
			visit.setDoor_Photo(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Door_Photo)));
			visit.setGift_Photo(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Gift_Photo)));
			visit.setVisit_Remark(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_Remark)));
			visit.setVisit_audio(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Visit_audio)));
			visit.setVisit_audio_File(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_audio_File)));
			visit.setVisit_audio_length(cursor.getInt(cursor.getColumnIndex(HomeVisitModel.Visit_audio_length)));
			visit.setVisit_in_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Location)));
			visit.setVisit_out_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Location)));
			visit.setVisit_in_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lat)));
			visit.setVisit_in_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lon)));
			visit.setVisit_out_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lat)));
			visit.setVisit_out_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lon)));
			try {
				visit.setVisit_in_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_in_Time)),
						"yyyy-MM-dd HH:mm:ss"));
				visit.setVisit_out_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_out_Time)),
						"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("exception", "parse time exception", e);
			}
		}
		db.close();
		return visit;
	}

	@Override
	public List<HomeVisitModel> getUnsynVisits() {
		List<HomeVisitModel> results = new ArrayList<HomeVisitModel>();		
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Family_Visit_Info where SellerCode=? and isSynchronized=0 order by Visit_in_Time";
		String sellerCode = UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array = {sellerCode };
		Cursor cursor = db.rawQuery(sql, array);
		while (cursor.moveToNext()) {
			HomeVisitModel visit = new HomeVisitModel();
			visit.setAbnormal_Positioning_Audio(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Audio)));
			visit.setAbnormal_Positioning_Photo(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Photo)));
			visit.setAbnormal_Positioning_Remark(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Remark)));
			visit.setBcompany_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Bcompany_Id)));
			visit.setClerk_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Clerk_Id)));
			visit.setFamily_Visit_Id(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Family_Visit_Id)));
			visit.setDoor_Photo(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Door_Photo)));
			visit.setGift_Photo(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Gift_Photo)));
			visit.setVisit_Remark(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_Remark)));
			visit.setVisit_audio(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Visit_audio)));
			visit.setVisit_audio_File(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_audio_File)));
			visit.setSellerCode(sellerCode);
			visit.setVisit_in_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Location)));
			visit.setVisit_out_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Location)));
			visit.setVisit_in_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lat)));
			visit.setVisit_in_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lon)));
			visit.setVisit_out_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lat)));
			visit.setVisit_out_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lon)));
			try {
				visit.setVisit_in_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_in_Time)),
						"yyyy-MM-dd HH:mm:ss"));
				visit.setVisit_out_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_out_Time)),
						"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("exception", "parse time exception", e);
			}
			results.add(visit);
		}
		db.close();
		return results;
	}

	@Override
	public boolean updateSyn(HomeVisitModel model) {
		db = MySQLiteOpenHelper.openDatabase(context);
		ContentValues values = new ContentValues();
		
		values.put(HomeVisitModel.IsSynchronized,1);
	
		String[] array={model.getFamily_Visit_Id()};
		int l = db.update(HomeVisitModel.DEPLOYMENT,values," Family_Visit_Id=?",array);
		if(l>0){
			return true;
		}else{
		return false;
		}
	}

	@Override
	public List<HomeVisitModel> getUnsynLocalVisits() {
		List<HomeVisitModel> results = new ArrayList<HomeVisitModel>();		
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Family_Visit_Info where SellerCode=? and isSynchronized=0 and Visit_in_Location='' order by Visit_in_Time";
		String sellerCode = UserData.getInstance().getSellerCode();
		if (null == sellerCode||"".equals(sellerCode)){
			sellerCode=UserSessionUtil.getUser(context).getSellerCode();
		}
		String[] array = {sellerCode };
		Cursor cursor = db.rawQuery(sql, array);
		while (cursor.moveToNext()) {
			HomeVisitModel visit = new HomeVisitModel();
			visit.setAbnormal_Positioning_Audio(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Audio)));
			visit.setAbnormal_Positioning_Photo(cursor.getBlob(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Photo)));
			visit.setAbnormal_Positioning_Remark(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Abnormal_Positioning_Remark)));
			visit.setBcompany_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Bcompany_Id)));
			visit.setClerk_Id(cursor.getString(cursor
					.getColumnIndex(HomeVisitModel.Clerk_Id)));
			visit.setFamily_Visit_Id(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Family_Visit_Id)));
			visit.setDoor_Photo(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Door_Photo)));
			visit.setGift_Photo(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Gift_Photo)));
			visit.setVisit_Remark(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_Remark)));
			visit.setVisit_audio(cursor.getBlob(cursor.getColumnIndex(HomeVisitModel.Visit_audio)));
			visit.setVisit_audio_File(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_audio_File)));
			visit.setSellerCode(sellerCode);
			visit.setVisit_in_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Location)));
			visit.setVisit_out_Location(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Location)));
			visit.setVisit_in_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lat)));
			visit.setVisit_in_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_in_Lon)));
			visit.setVisit_out_Lat(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lat)));
			visit.setVisit_out_Lon(cursor.getString(cursor.getColumnIndex(HomeVisitModel.Visit_out_Lon)));
			try {
				visit.setVisit_in_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_in_Time)),
						"yyyy-MM-dd HH:mm:ss"));
				visit.setVisit_out_Time(DateUtil.parser(cursor.getString(cursor
						.getColumnIndex(HomeVisitModel.Visit_out_Time)),
						"yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Log.e("exception", "parse time exception", e);
			}
			results.add(visit);
		}
		db.close();
		return results;
	}

}
