package com.cn.android.zhengxun.app.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.UrlConfig;
import com.cn.android.zhengxun.app.model.ClerkModel;
import com.cn.android.zhengxun.app.model.PharmacyInfoModel;
import com.cn.android.zhengxun.app.model.PrincipleModel;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.PrincipleService;
import com.cn.android.zhengxun.app.util.DateUtil;
import com.cn.android.zhengxun.app.util.HandlerCommand;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;
import com.cn.android.zhengxun.app.util.PinYinUtil;

public class PrincipleServiceImpl implements PrincipleService {

	private Context context;
	private Handler handler;
	private SQLiteDatabase db;
	private PrincipleModel principle;

	public PrincipleServiceImpl(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public PrincipleModel getPrincipleByUser(UserModel user) {
		HttpTransportSE ht = new HttpTransportSE(UrlConfig.ROOT_URL);

		// ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject request = new SoapObject(StringConstants.SERVICE_NS,
				StringConstants.GET_USER_INFO);
		request.addProperty(UserModel.USER_ID, user.getUserName());
		request.addProperty("password", user.getPassword());
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		try {
			ht.call(StringConstants.GET_USER_INFO_ACTION, envelope);
			System.out.println(envelope.bodyIn.toString());
			SoapObject result1 = (SoapObject) envelope.bodyIn;
			String result = result1.getProperty(0).toString();
			principle = parserJSON(result);
			Message msg = handler.obtainMessage(HandlerCommand.GOT_USER_INFO);
			handler.sendMessage(msg);
		} catch (IOException e) {
			Log.e("exception", "IOException", e);
		} catch (XmlPullParserException e) {
			Log.e("exception", "XmlPullParserException", e);
		} catch (JSONException e) {
			Log.e("JSONException", "exception", e);
		} catch (ParseException e) {
			Log.e("ParseException","exceprion", e);
		}

		return principle;
	}

	private PrincipleModel parserJSON(String result) throws JSONException,
			ParseException {
		JSONArray arrays = new JSONArray(result);
		PrincipleModel pricipleModel = new PrincipleModel();
		UserModel user = new UserModel();
		List<ClerkModel> clerks = new ArrayList<ClerkModel>();
		List<PharmacyInfoModel> pharmacys = new ArrayList<PharmacyInfoModel>();
		if (arrays.length() > 0) {
			JSONObject principle = (JSONObject) arrays.get(0);
			user.setUserName(principle.getString(UserModel.USER_ID));
			// user.setUsetToken(principle.getString(UserModel.USER_PSW));
			user.setName(principle.getString(UserModel.USER_NAME));
			user.setSellerCode(principle.getString(UserModel.SELLER_CODE));
			user.setArea_name(principle.getString(UserModel.AREA_NAME));
			user.setDept_name(principle.getString(UserModel.DEPT_NAME));
			user.setManager_name(principle.getString(UserModel.MANGER_NAME));
			user.setManager_role(principle.getString(UserModel.MANGER_ROLE));
			user.setManagerId(principle.getString(UserModel.MANGER_ID));
			user.setRole(principle.getString(UserModel.ROLE));
			user.setT_phone(principle.getString(UserModel.CONTACT_PHONE));
			pricipleModel.setUser(user);

		}
		for (int i = 0; i < arrays.length(); i++) {
			JSONObject principleObject = (JSONObject) arrays.get(i);
			PharmacyInfoModel company = new PharmacyInfoModel();
			company.setBclass(principleObject
					.getString(PharmacyInfoModel.BCLASS));
			company.setBcompanyId(principleObject
					.getString(PharmacyInfoModel.BCOMPANYID));
			company.setBcompnayName(principleObject
					.getString(PharmacyInfoModel.BCOMPANY_NAME));
			company.setBregaddress(principleObject
					.getString(PharmacyInfoModel.BREG_ADDRESS));
			company.setBtype(principleObject.getString(PharmacyInfoModel.BTYPE));
			company.setCityName(principleObject
					.getString(PharmacyInfoModel.CITY_NAME));
			company.setCustomerLat(principleObject
					.getString(PharmacyInfoModel.CUSTOMER_LAT));
			company.setCustomerLot(principleObject
					.getString(PharmacyInfoModel.CUSTOMER_LOT));
			company.setIsSynchronized(1);
			company.setProvinceName(principleObject
					.getString(PharmacyInfoModel.PROVINCE_NAME));
			company.setSellerCode(principleObject
					.getString(PharmacyInfoModel.SELLER_CODE));
			pharmacys.add(company);
			ClerkModel clerk = new ClerkModel();
			clerk.setAddress(principleObject.getString(ClerkModel.ADDRESS));
			clerk.setBcompanyID(principleObject
					.getString(ClerkModel.BCOMPANYID));
			clerk.setBirthday(DateUtil.parser(
					principleObject.getString(ClerkModel.BIRTHDAY),
					"yyyy-MM-dd"));
			// clerk.setBustPhoto(Base64.decode(principleObject
			// .getString(ClerkModel.BUST_PHOTO)));
			// clerk.setFullPhoto(Base64.decode(principleObject
			// .getString(ClerkModel.FULL_PHOTO)));
			clerk.setCardId(principleObject.getString(ClerkModel.CARD_ID));
			clerk.setClerkType(principleObject.getString(ClerkModel.CLERK_TYPE));
			clerk.setId(principleObject.getString(ClerkModel.ID));
			clerk.setName(principleObject.getString(ClerkModel.NAME));
			clerk.setQq(principleObject.getString(ClerkModel.QQ));
			clerk.setSex(principleObject.getString(ClerkModel.SEX));
			clerk.setTelphoneNum(principleObject.getString(ClerkModel.TEL));
			clerk.setEmail(principleObject.getString(ClerkModel.EMAIL));
			clerk.setUserId(principleObject.getString(UserModel.USER_ID));
			clerk.setWeiXin(principleObject.getString(ClerkModel.WEIXIN));
			clerk.setSellerCode(principleObject
					.getString(ClerkModel.SELLER_CODE));
			clerks.add(clerk);
		}
		pricipleModel.setBcompany(pharmacys);
		pricipleModel.setClerk(clerks);
		return pricipleModel;
	}

	@Override
	public boolean insertToDB(PrincipleModel model) {
		db = MySQLiteOpenHelper.openDatabase(context);
		// db.beginTransaction();
		for (ClerkModel clerk : model.getClerk()) {
			ContentValues values = new ContentValues();
			values.put(ClerkModel.ADDRESS, clerk.getAddress());
			values.put(ClerkModel.BCOMPANYID, clerk.getBcompanyID());
			values.put(ClerkModel.BIRTHDAY,
					DateUtil.formatDate(clerk.getBirthday(), "yyyy-MM-dd"));
			values.put(ClerkModel.CARD_ID, clerk.getCardId());
			values.put(ClerkModel.CLERK_TYPE, clerk.getClerkType());
			values.put(ClerkModel.EMAIL, clerk.getEmail());
			values.put(ClerkModel.ID, clerk.getId());
			values.put(ClerkModel.IS_SYNCHONIZED, 1);
			values.put(ClerkModel.NAME, clerk.getName());
			values.put(ClerkModel.QQ, clerk.getQq());
			values.put(ClerkModel.SEX, clerk.getSex());
			values.put(ClerkModel.TEL, clerk.getTelphoneNum());
			values.put(ClerkModel.WEIXIN, clerk.getWeiXin());
			values.put(ClerkModel.SELLER_CODE, clerk.getSellerCode());
			db.insertWithOnConflict(ClerkModel.DEPLOYMENT, null, values,
					SQLiteDatabase.CONFLICT_IGNORE);
		}
		for (PharmacyInfoModel company : model.getBcompany()) {
			ContentValues values = new ContentValues();
			values.put(PharmacyInfoModel.BCLASS, company.getBclass());
			values.put(PharmacyInfoModel.BCOMPANY_NAME,
					company.getBcompnayName());
			values.put(PharmacyInfoModel.BCOMPANYID, company.getBcompanyId());
			values.put(PharmacyInfoModel.BREG_ADDRESS, company.getBregaddress());
			values.put(PharmacyInfoModel.BTYPE, company.getBtype());
			values.put(PharmacyInfoModel.CITY_NAME, company.getCityName());
			values.put(PharmacyInfoModel.CUSTOMER_LAT, company.getCustomerLat());
			values.put(PharmacyInfoModel.IS_SYN, 1);
			values.put(PharmacyInfoModel.CUSTOMER_LOT, company.getCustomerLot());
			values.put(PharmacyInfoModel.PROVINCE_NAME,
					company.getProvinceName());
			values.put(PharmacyInfoModel.SELLER_CODE, company.getSellerCode());
			values.put(PharmacyInfoModel.BCOMPANY_NAME_AREA,PinYinUtil.getPinYin(company.getBcompnayName())+company.getBcompnayName());
			db.insertWithOnConflict(PharmacyInfoModel.DEPLOYMENT, null, values,
					SQLiteDatabase.CONFLICT_IGNORE);
		}

		// db.endTransaction();

		db.close();
		Message msg = handler.obtainMessage(HandlerCommand.INITAILIZED);
		handler.sendMessage(msg);
		Log.i("insert principle", "insert principle successfully.");
		return true;
	}

	@Override
	public PrincipleModel getPrinciple() {
		return principle;
	}

	public void setPriciple(PrincipleModel principle) {
		this.principle = principle;
	}

}
