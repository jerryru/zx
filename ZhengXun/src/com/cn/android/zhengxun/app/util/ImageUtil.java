package com.cn.android.zhengxun.app.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

public class ImageUtil {

	 //获取图片缩小的图片
    public static Bitmap scaleBitmap(String src,int max)
    {
        //获取图片的高和宽
        BitmapFactory.Options options = new BitmapFactory.Options();
        //这一个设置使 BitmapFactory.decodeFile获得的图片是空的,但是会将图片信息写到options中
        options.inJustDecodeBounds = true;         
        BitmapFactory.decodeFile(src, options); 
       // 计算比例 为了提高精度,本来是要640 这里缩为64
        max=max/10;
        int be = options.outWidth / max;
         if(be%10 !=0)
          be+=10;
         be=be/10;
         if (be <= 0)
          be = 1;
        options.inSampleSize = be;
        //设置可以获取数据
        options.inJustDecodeBounds = false;
        //获取图片
        return BitmapFactory.decodeFile(src, options);        
    }
    // 加水印 也可以加文字
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,
            String title) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight(); 
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了        
        Bitmap newb= Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src    
        Paint paint=new Paint();
        //加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            paint.setAlpha(50);
            cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印            
        }
        //加入文字
        if(title!=null)
        {
            String familyName ="宋体";
            Typeface font = Typeface.create(familyName,Typeface.BOLD);            
            TextPaint textPaint=new TextPaint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTypeface(font);
            textPaint.setTextSize(9);
            //这里是自动换行的
//            StaticLayout layout = new StaticLayout(title,textPaint,w,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
//            layout.draw(cv);
            //文字就加左上角算了
            cv.drawText(title,w-100,h-20,textPaint); 
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }
    
    public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
    
}
