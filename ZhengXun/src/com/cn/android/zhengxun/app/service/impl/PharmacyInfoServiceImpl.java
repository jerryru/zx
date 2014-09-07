package com.cn.android.zhengxun.app.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.cn.android.zhengxun.app.model.PharmacyInfoModel;
import com.cn.android.zhengxun.app.service.PharmacyInfoService;
import com.cn.android.zhengxun.app.util.DateUtil;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;

public class PharmacyInfoServiceImpl implements PharmacyInfoService {

	private Context context;
	private Handler handler;
	private List<PharmacyInfoModel> pharmacyInfoModels;
	private SQLiteDatabase db;

	public PharmacyInfoServiceImpl(Context context) {
		this.context = context;
	}

	public PharmacyInfoServiceImpl(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public List<PharmacyInfoModel> getPharmacyInfoBySellerCode(String sellerCode) {
		pharmacyInfoModels = new ArrayList<PharmacyInfoModel>();
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Pharmacy_Info where SellerCode=?";
		String[] arrays = { sellerCode };
		Cursor cursor = db.rawQuery(sql, arrays);
		while (cursor.moveToNext()) {
			PharmacyInfoModel company = new PharmacyInfoModel();
			company.setBcompanyId(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANYID)));
			company.setBcompnayName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANY_NAME)));
			company.setBclass(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCLASS)));
			company.setCustomerLat(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LAT)));
			company.setCustomerLot(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LOT)));
			company.setBregaddress(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BREG_ADDRESS)));
			company.setBtype(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BTYPE)));
			company.setCityName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CITY_NAME)));
			company.setProvinceName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.PROVINCE_NAME)));
			company.setSellerCode(sellerCode);
			pharmacyInfoModels.add(company);
		}
		db.close();
		Message msg = handler.obtainMessage(HandlerCommand.GOT_PHARAMCY_LIST);
		handler.sendMessage(msg);
		return pharmacyInfoModels;
	}

	public List<PharmacyInfoModel> getPharmacyInfoModels() {
		return pharmacyInfoModels;
	}

	public void setPharmacyInfoModels(List<PharmacyInfoModel> pharmacyInfoModels) {
		this.pharmacyInfoModels = pharmacyInfoModels;
	}

	@Override
	public PharmacyInfoModel getPharmacyInfoById(String bcompanyId) {
		PharmacyInfoModel company = new PharmacyInfoModel();
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Pharmacy_Info where BcompanyID=?";
		String[] arrays = { bcompanyId };
		Cursor cursor = db.rawQuery(sql, arrays);
		while (cursor.moveToNext()) {
			company.setBcompanyId(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANYID)));
			company.setBcompnayName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANY_NAME)));
			company.setBclass(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCLASS)));
			company.setCustomerLat(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LAT)));
			company.setCustomerLot(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LOT)));
			company.setBregaddress(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BREG_ADDRESS)));
			company.setBtype(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BTYPE)));
			company.setCityName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CITY_NAME)));
			company.setProvinceName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.PROVINCE_NAME)));
			company.setSellerCode(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.SELLER_CODE)));
		}
		db.close();

		return company;
	}

	@Override
	public List<PharmacyInfoModel> getPharmacyInfoByName(String sellerCode,
			String search_area) {
		pharmacyInfoModels = new ArrayList<PharmacyInfoModel>();
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Pharmacy_Info where SellerCode=? and (BcompanyName_area like \"%"
				+ search_area
				+ "%\" or BcompanyName like \"%"
				+ search_area
				+ "%\")";
		String[] arrays = { sellerCode };
		Cursor cursor = db.rawQuery(sql, arrays);
		while (cursor.moveToNext()) {
			PharmacyInfoModel company = new PharmacyInfoModel();
			company.setBcompanyId(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANYID)));
			company.setBcompnayName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANY_NAME)));
			company.setBclass(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCLASS)));
			company.setCustomerLat(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LAT)));
			company.setCustomerLot(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LOT)));
			company.setBregaddress(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BREG_ADDRESS)));
			company.setBtype(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BTYPE)));
			company.setCityName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CITY_NAME)));
			company.setProvinceName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.PROVINCE_NAME)));
			company.setSellerCode(sellerCode);
			pharmacyInfoModels.add(company);
		}
		db.close();
		Message msg = handler.obtainMessage(HandlerCommand.GOT_PHARAMCY_LIST);
		handler.sendMessage(msg);
		return pharmacyInfoModels;
	}

	@Override
	public List<PharmacyInfoModel> getPharmacyInfoBySellerCodeForTour(
			String sellerCode) {
		pharmacyInfoModels = new ArrayList<PharmacyInfoModel>();
		db = MySQLiteOpenHelper.openDatabase(context);

		String sql = "select * from Pharmacy_Info where SellerCode=? and BcompanyID not in (select Bcompany_Id from Tour_Info where Tour_in_time>? and Tour_in_time<?)";
		String startDate = DateUtil.formatDate(
				Calendar.getInstance().getTime(), "yyyy-MM-dd");
		String endDate = DateUtil.formatDate(DateUtil.addDays(1), "yyyy-MM-dd");
		String[] arrays = { sellerCode, startDate, endDate };
		Cursor cursor = db.rawQuery(sql, arrays);
		while (cursor.moveToNext()) {
			PharmacyInfoModel company = new PharmacyInfoModel();
			company.setBcompanyId(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANYID)));
			company.setBcompnayName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANY_NAME)));
			company.setBclass(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCLASS)));
			company.setCustomerLat(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LAT)));
			company.setCustomerLot(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LOT)));
			company.setBregaddress(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BREG_ADDRESS)));
			company.setBtype(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BTYPE)));
			company.setCityName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CITY_NAME)));
			company.setProvinceName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.PROVINCE_NAME)));
			company.setSellerCode(sellerCode);
			pharmacyInfoModels.add(company);
		}
		db.close();
		Message msg = handler.obtainMessage(HandlerCommand.GOT_PHARAMCY_LIST);
		handler.sendMessage(msg);
		return pharmacyInfoModels;
	}

	@Override
	public List<PharmacyInfoModel> getPharmacyInfoByNameForTour(
			String sellerCode, String search_area) {
		pharmacyInfoModels = new ArrayList<PharmacyInfoModel>();
		db = MySQLiteOpenHelper.openDatabase(context);
		String sql = "select * from Pharmacy_Info where SellerCode=? and BcompanyID not in (select Bcompany_Id from Tour_Info where Tour_in_time>? and Tour_in_time<?) and (BcompanyName_area like \"%"
				+ search_area
				+ "%\" or BcompanyName like \"%"
				+ search_area
				+ "%\")";
		String startDate = DateUtil.formatDate(
				Calendar.getInstance().getTime(), "yyyy-MM-dd");
		String endDate = DateUtil.formatDate(DateUtil.addDays(1), "yyyy-MM-dd");
		String[] arrays = { sellerCode, startDate, endDate };
		Cursor cursor = db.rawQuery(sql, arrays);
		while (cursor.moveToNext()) {
			PharmacyInfoModel company = new PharmacyInfoModel();
			company.setBcompanyId(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANYID)));
			company.setBcompnayName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCOMPANY_NAME)));
			company.setBclass(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BCLASS)));
			company.setCustomerLat(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LAT)));
			company.setCustomerLot(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CUSTOMER_LOT)));
			company.setBregaddress(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BREG_ADDRESS)));
			company.setBtype(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.BTYPE)));
			company.setCityName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.CITY_NAME)));
			company.setProvinceName(cursor.getString(cursor
					.getColumnIndex(PharmacyInfoModel.PROVINCE_NAME)));
			company.setSellerCode(sellerCode);
			pharmacyInfoModels.add(company);
		}
		db.close();
		Message msg = handler.obtainMessage(HandlerCommand.GOT_PHARAMCY_LIST);
		handler.sendMessage(msg);
		return pharmacyInfoModels;
	}

}
