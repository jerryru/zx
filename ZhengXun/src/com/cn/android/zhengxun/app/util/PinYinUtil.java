package com.cn.android.zhengxun.app.util;

import java.util.ArrayList;

import com.cn.android.zhengxun.app.util.HanziToPinyin.Token;

/**
 * 将汉字转成拼音
 * @author jru
 *
 */
public class PinYinUtil {
	public static String getPinYin(String input) {
		 ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
		
		         StringBuilder sb = new StringBuilder();
		 
		         if (tokens != null && tokens.size() > 0) {
		 
		             for (Token token : tokens) {
		 
		                 if (Token.PINYIN == token.type) {
		                	 
		                     sb.append(token.target);
		 
		                 } else {
		 
		                     sb.append(token.source);
		 
		                 }
		 
		             }		             
		 
		         }
		 
		         return sb.toString().toLowerCase();
		 
		     }

}
