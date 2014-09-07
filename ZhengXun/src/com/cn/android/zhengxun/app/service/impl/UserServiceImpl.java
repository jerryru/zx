package com.cn.android.zhengxun.app.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cn.android.zhengxun.app.constants.StringConstants;
import com.cn.android.zhengxun.app.model.UserModel;
import com.cn.android.zhengxun.app.service.UserService;
import com.cn.android.zhengxun.app.util.MySQLiteOpenHelper;

public class UserServiceImpl implements UserService {
	
	private Context context;
	private SQLiteDatabase db;
	public UserServiceImpl(Context context){
		this.context=context;
	}
	@Override
	public int login(UserModel user) {
		if("".equals(user.getUserName())){
			return LoginResults.NULL;
		}
		db=MySQLiteOpenHelper.openDatabase(context);
		String sql="select * from User_Info where user_id=?";
		String[] array={user.getUserName()};
		Cursor cursor=db.rawQuery(sql, array);
		String psw="";
		if(cursor.getCount()==0)
		{
			closeDb(cursor);
			return LoginResults.NO_SUCH_USER;
		}
		while(cursor.moveToNext()){
			psw=cursor.getString(cursor.getColumnIndex(UserModel.USER_PSW));
			user.setSellerCode(cursor.getString(cursor.getColumnIndex(UserModel.SELLER_CODE)));
			break;
		}
		closeDb(cursor);
		if(user.getPassword().equals(psw)){
			return LoginResults.SUCESSFUL;
		}else{
			return LoginResults.WRONG_PSW;
		}
		
	}
	private void closeDb(Cursor cursor) {
		cursor.close();
		closeDb();
		
	}
	private void closeDb() {
		
		db.close();
	}
	@Override
	public boolean insertUser(UserModel user) {
		db=MySQLiteOpenHelper.openDatabase(context);
		ContentValues contentValues=new ContentValues();
		contentValues.put(UserModel.SELLER_CODE, user.getSellerCode());
		contentValues.put(UserModel.USER_NAME,user.getName());
		contentValues.put(UserModel.USER_ID,user.getUserName());
		contentValues.put(UserModel.USER_PSW,user.getPassword());
		contentValues.put(UserModel.AREA_NAME,user.getArea_name());
		contentValues.put(UserModel.CONTACT_PHONE,user.getT_phone());
		contentValues.put(UserModel.DEPT_NAME,user.getDept_name());
		contentValues.put(UserModel.IS_SYN,1);
		contentValues.put(UserModel.MANGER_ID,user.getManagerId());
		contentValues.put(UserModel.MANGER_NAME,user.getManager_name());
		contentValues.put(UserModel.MANGER_ROLE,user.getManager_role());
		contentValues.put(UserModel.ROLE,user.getRole());
		contentValues.put(UserModel.TOKEN,user.getUsetToken());
		long rowId=db.insert(StringConstants.USER_INFO, null, contentValues);
		closeDb();
		if(rowId==-1l){
		return false;
		}else{
			return true;
		}
	}

}
