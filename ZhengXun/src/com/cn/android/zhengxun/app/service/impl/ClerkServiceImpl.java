package com.cn.android.zhengxun.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import org.kobjects.base64.Base64;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cn.android.zhengxun.app.model.ClerkModel;
import com.cn.android.zhengxun.app.service.ClerkInfoService;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;

public class ClerkServiceImpl implements ClerkInfoService {

	private SQLiteDatabase db;
	private Context context;
	public ClerkServiceImpl(Context context){
		this.context=context;
	}
	
	@Override
	public List<ClerkModel> getClerks(String bCompanyId) {
		List<ClerkModel> clerks=new ArrayList<ClerkModel>();
		db=MySQLiteOpenHelper.openDatabase(context);
		String sql="select * from Clerk_Info where BcompanyID=?";
		String[] array={bCompanyId};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			ClerkModel clerk=new ClerkModel();
			clerk.setAddress(cursor.getString(cursor.getColumnIndex(ClerkModel.ADDRESS)));
			clerk.setBcompanyID(bCompanyId);
			clerk.setBirthday(new Date(cursor.getLong(cursor.getColumnIndex(ClerkModel.BIRTHDAY))));
//			clerk.setBustPhoto(Base64.decode(cursor.getString(cursor.getColumnIndex(ClerkModel.BUST_PHOTO))));
			clerk.setCardId(cursor.getString(cursor.getColumnIndex(ClerkModel.CARD_ID)));
			clerk.setClerkType(cursor.getString(cursor.getColumnIndex(ClerkModel.CLERK_TYPE)));
			clerk.setEmail(cursor.getString(cursor.getColumnIndex(ClerkModel.EMAIL)));
//			clerk.setFullPhoto(Base64.decode(cursor.getString(cursor.getColumnIndex(ClerkModel.FULL_PHOTO))));
			clerk.setId(cursor.getString(cursor.getColumnIndex(ClerkModel.ID)));
			clerk.setName(cursor.getString(cursor.getColumnIndex(ClerkModel.NAME)));
			clerk.setQq(cursor.getString(cursor.getColumnIndex(ClerkModel.QQ)));
			clerk.setSellerCode(cursor.getString(cursor.getColumnIndex(ClerkModel.SELLER_CODE)));
			clerk.setSex(cursor.getString(cursor.getColumnIndex(ClerkModel.SEX)));
			clerk.setTelphoneNum(cursor.getString(cursor.getColumnIndex(ClerkModel.TEL)));
			clerk.setWeiXin(cursor.getString(cursor.getColumnIndex(ClerkModel.WEIXIN)));
			clerks.add(clerk);
		}
		return clerks;
	}

	@Override
	public ClerkModel getClerk(String clerk_Id) {
		ClerkModel clerk=new ClerkModel();
		db=MySQLiteOpenHelper.openDatabase(context);
		String sql="select * from Clerk_Info where Id=?";
		String[] array={clerk_Id};
		Cursor cursor=db.rawQuery(sql, array);
		while(cursor.moveToNext()){
			
			clerk.setAddress(cursor.getString(cursor.getColumnIndex(ClerkModel.ADDRESS)));
			
			clerk.setBirthday(new Date(cursor.getLong(cursor.getColumnIndex(ClerkModel.BIRTHDAY))));
//			clerk.setBustPhoto(Base64.decode(cursor.getString(cursor.getColumnIndex(ClerkModel.BUST_PHOTO))));
			clerk.setCardId(cursor.getString(cursor.getColumnIndex(ClerkModel.CARD_ID)));
			clerk.setClerkType(cursor.getString(cursor.getColumnIndex(ClerkModel.CLERK_TYPE)));
			clerk.setEmail(cursor.getString(cursor.getColumnIndex(ClerkModel.EMAIL)));
//			clerk.setFullPhoto(Base64.decode(cursor.getString(cursor.getColumnIndex(ClerkModel.FULL_PHOTO))));
			clerk.setId(cursor.getString(cursor.getColumnIndex(ClerkModel.ID)));
			clerk.setName(cursor.getString(cursor.getColumnIndex(ClerkModel.NAME)));
			clerk.setQq(cursor.getString(cursor.getColumnIndex(ClerkModel.QQ)));
			clerk.setSellerCode(cursor.getString(cursor.getColumnIndex(ClerkModel.SELLER_CODE)));
			clerk.setSex(cursor.getString(cursor.getColumnIndex(ClerkModel.SEX)));
			clerk.setTelphoneNum(cursor.getString(cursor.getColumnIndex(ClerkModel.TEL)));
			clerk.setWeiXin(cursor.getString(cursor.getColumnIndex(ClerkModel.WEIXIN)));
			
		}
		return clerk;
	}

	
}
