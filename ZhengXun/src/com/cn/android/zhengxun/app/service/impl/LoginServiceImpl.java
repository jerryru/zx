package com.cn.android.zhengxun.app.service.impl;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.constants.UrlConfig;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.LoginService;
import com.cn.android.zhengxun.app.util.HandlerCommand;

public class LoginServiceImpl implements LoginService {
	private Context context;
	private Handler handler;
	private UserModel returnUser=null;



	public LoginServiceImpl(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	public UserModel getReturnUser() {
		return returnUser;
	}

	public void setReturnUser(UserModel returnUser) {
		this.returnUser = returnUser;
	}
	@Override
	public UserModel login(UserModel user) {
		HttpTransportSE ht = new HttpTransportSE(UrlConfig.ROOT_URL);

		// ht.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject request = new SoapObject(StringConstants.SERVICE_NS,
				StringConstants.LOGIN);
		request.addProperty("user_id", user.getUserName());
		request.addProperty("password", user.getPassword());
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		try {
			ht.call(StringConstants.LOGIN_ACTION, envelope);

			SoapObject result1 = (SoapObject) envelope.bodyIn;
			String result = result1.getProperty(0).toString();
			UserModel userModel = parserJson(result.toString());
			this.setReturnUser(userModel);
			Message msg=handler.obtainMessage(HandlerCommand.LOGIN_SUCCESSFUL);
			handler.sendMessage(msg);
			return userModel;
		} catch (IOException e) {

			Log.e("IO exception", "exception", e);
			Message msg=handler.obtainMessage(HandlerCommand.LOGIN_IOEXCEPTION);
			handler.sendMessage(msg);
			
		} catch (XmlPullParserException e) {
			Log.e("XmlPullParserException", "exception", e);
			Message msg=handler.obtainMessage(HandlerCommand.LOGIN_XML_EXCEPTION);
			handler.sendMessage(msg);
			
		} catch (JSONException e) {
			Log.e("JSONException", "exception", e);
			Message msg=handler.obtainMessage(HandlerCommand.LOGIN_FAILDED);
			handler.sendMessage(msg);
			
		}
		return null;

	}

	private UserModel parserJson(String user_json) throws JSONException {
		UserModel user = new UserModel();
		JSONObject jsonObject = new JSONArray(user_json).getJSONObject(0);
		user.setUserName(jsonObject.getString(UserModel.USER_ID));
		user.setUsetToken(jsonObject.getString(UserModel.USER_PSW));
		user.setName(jsonObject.getString(UserModel.USER_NAME));
		user.setSellerCode(jsonObject.getString(UserModel.SELLER_CODE));
		user.setArea_name(jsonObject.getString(UserModel.AREA_NAME));
		user.setDept_name(jsonObject.getString(UserModel.DEPT_NAME));
		user.setManager_name(jsonObject.getString(UserModel.MANGER_NAME));
		user.setManager_role(jsonObject.getString(UserModel.MANGER_ROLE));
		user.setManagerId(jsonObject.getString(UserModel.MANGER_ID));
		user.setRole(jsonObject.getString(UserModel.ROLE));
		user.setT_phone(jsonObject.getString(UserModel.CONTACT_PHONE));

		return user;
	}

}
