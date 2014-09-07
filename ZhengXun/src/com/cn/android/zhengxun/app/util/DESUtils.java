package com.cn.android.zhengxun.app.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <pre>
 *         Achievo, Inc.
 *         Copyright (C): 2006
 *        
 *         Description:
 *         对密码进行DES加密
 *          
 *         Revision History:
 *         2006-8-28 x_zhoubin		initial version.
 * 
 * </pre>
 */
public class DESUtils
{

	public static byte[] generateKey() throws NoSuchAlgorithmException
	{
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		// 为我们选择的DES算法生成一个KeyGenerator对象
		KeyGenerator kg = KeyGenerator.getInstance("DES");
		kg.init(sr);

		// 生成密匙
		SecretKey key = kg.generateKey();

		// 获取密匙数据，该密匙应被传给解密方
		byte rawKeyData[] = key.getEncoded();
		return rawKeyData;
	}

	/**
	 * 对密码进行DES加密处理
	 * 
	 * @param password
	 *            密码
	 * @param rawKeyData
	 *            密匙
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws IllegalStateException
	 */
	public static byte[] encrypt(String password, byte[] rawKeyData)
			throws NoSuchAlgorithmException, InvalidKeyException,
			InvalidKeySpecException, NoSuchPaddingException,
			UnsupportedEncodingException, IllegalStateException,
			IllegalBlockSizeException, BadPaddingException
	{
		DESKeySpec dks = new DESKeySpec(rawKeyData);
		SecureRandom sr = new SecureRandom();

		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);

		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");

		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);

		// 现在，获取数据并加密
		byte data[] = password.getBytes("UTF-8");

		// 正式执行加密操作
		byte encryptedData[] = cipher.doFinal(data);

		return encryptedData;
	}

	/**
	 * 
	 * 解密
	 * 
	 * @param src
	 *            数据源
	 * 
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * 
	 * @return 返回解密后的原始数据
	 * 
	 * @throws Exception
	 */

	public static byte[] decrypt(byte[] src, byte[] key) throws Exception
	{

		// DES算法要求有一个可信任的随机数源

		SecureRandom sr = new SecureRandom();

		// 从原始密匙数据创建一个DESKeySpec对象

		DESKeySpec dks = new DESKeySpec(key);

		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成

		// 一个SecretKey对象

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

		SecretKey securekey = keyFactory.generateSecret(dks);

		// Cipher对象实际完成解密操作

		Cipher cipher = Cipher.getInstance("DES");

		// 用密匙初始化Cipher对象

		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		// 现在，获取数据并解密

		// 正式执行解密操作

		return cipher.doFinal(src);

	}

}
