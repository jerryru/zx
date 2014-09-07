package com.cn.android.zhengxun.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.cn.android.zhengxun.app.model.UserModel;

public class UserSessionUtil {

	public static UserModel getUser(Context context){
		UserModel user=new UserModel();
		File file=new File(StringUtil.USER_TOKEN_PATH + File.separator
				+ context.getPackageName() + File.separator+StringUtil.USER_TMP_FILE);
		if (file.exists()) {
			List<String> result = new ArrayList<String>();
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String str = null;

				while ((str = br.readLine()) != null) {
					result.add(str);

					System.out.println(str);
				}

			} catch (FileNotFoundException e) {
				Log.e("excption","FileNotFoundException", e);
			} catch (IOException e) {
				Log.e("excption","IOException", e);
			}
			if(result.size()>1){
			user.setUserName(result.get(0));
			user.setSellerCode(result.get(1));
			}
			}
		return user;
	}
}
