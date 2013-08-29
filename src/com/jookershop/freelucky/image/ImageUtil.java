package com.jookershop.freelucky.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Environment;
import android.widget.ImageView;

public class ImageUtil {
	
	public  static Bitmap resizeBitmap(byte [] f, int imageSize){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

//	        FileInputStream fis = new FileInputStream(f);
	        ByteArrayInputStream fis = new ByteArrayInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();

	        int scale = 1;
	        if (o.outHeight > imageSize || o.outWidth > imageSize) {
	            scale = (int)Math.pow(2, (int) Math.round(Math.log(imageSize / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        fis = new ByteArrayInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        fis.close();
	    } catch (IOException e) {
	    }
	    return b;
	}

	public  static Bitmap resizeBitmap(byte [] f){
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        ByteArrayInputStream fis = new ByteArrayInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();

	        fis = new ByteArrayInputStream(f);
	        b = BitmapFactory.decodeStream(fis);
	        fis.close();
	    } catch (IOException e) {
	    }
	    return b;
	}	
	
	public static Bitmap getResizedBitmap(Bitmap bm, int size) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		int maxSize = Math.max(width, height);
		float scale = 1;
		if(maxSize > size) scale = ((float)size)/ maxSize;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}	
	
	public static Bitmap scropAndShadowProcess(Bitmap source, int newHeight, int newWidth) {
		int think = 30;
		
	    int sourceWidth = source.getWidth();
	    int sourceHeight = source.getHeight();

	    float xScale = (float) (newWidth - think) / sourceWidth;
	    float yScale = (float) (newHeight  - think)/ sourceHeight;
	    float scale = Math.max(xScale, yScale);

	    float scaledWidth = scale * sourceWidth;
	    float scaledHeight = scale * sourceHeight;

	    float left = (newWidth - scaledWidth) / 2;
	    float top = (newHeight - scaledHeight) / 2;

	    RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
	    
	    // Finally, we create a new bitmap of the specified size and draw our new,
	    // scaled bitmap onto it.
	    Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
	    Canvas canvas = new Canvas(dest);
	    
	    
	    int w = newWidth;
	    int h = newHeight;
//
	    int newW = (int)scaledWidth;//w - (think);
	    int newH = (int)scaledHeight;//h - (think);
	    
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    
	    // Right
	    Shader rshader = new LinearGradient(newW, 0, w, 0, Color.GRAY, Color.LTGRAY, Shader.TileMode.CLAMP);
	    paint.setShader(rshader);
	    canvas.drawRect(newW, think, w, newH, paint);

	    // Bottom
	    Shader bshader = new LinearGradient(0, newH, 0, h, Color.GRAY, Color.LTGRAY, Shader.TileMode.CLAMP);
	    paint.setShader(bshader);
	    canvas.drawRect(think, newH, newW  , h, paint);

	    //Corner
	    Shader cchader = new LinearGradient(0, newH, 0, h, Color.LTGRAY, Color.LTGRAY, Shader.TileMode.CLAMP);
	    paint.setShader(cchader);
	    canvas.drawRect(newW, newH, w  , h, paint);
	    
	    canvas.drawBitmap(source, null, targetRect, null);
	    return dest;
	}
	
	
	//圓角轉換函式，帶入Bitmap圖片及圓角數值則回傳圓角圖，回傳Bitmap再置入ImageView
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx)
	        {         
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), 
	                                            bitmap.getHeight(), 
	                                            Config.ARGB_8888);  
	        Canvas canvas = new Canvas(output);  
	        final int color = 0xffC9C9C9;  
	        final Paint paint = new Paint();  
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
	                                         bitmap.getHeight());  
	        final RectF rectF = new RectF(rect);  
	        paint.setAntiAlias(true);  
	        canvas.drawARGB(0, 0, 0, 0);  
	        paint.setColor(color);  
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint);  
	        return output;  
	        }  

	public static Bitmap getDropShadow3(Bitmap bitmap) {

	    if (bitmap==null) return null;
	    int think = 15;
	    int w = bitmap.getWidth();//110
	    int h = bitmap.getHeight();

	    int newW = w - (think);//100
	    int newH = h - (think);

	    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
	    Bitmap bmp = Bitmap.createBitmap(w, h, conf);
	    Bitmap sbmp = Bitmap.createScaledBitmap(bitmap, newW, newH, false);//100

	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    Canvas c = new Canvas(bmp);

	    // Right
	    Shader rshader = new LinearGradient(newW, 0, w, 0, Color.GRAY, Color.LTGRAY, Shader.TileMode.CLAMP);
	    paint.setShader(rshader);
	    c.drawRect(newW, think, w, newH, paint);

	    // Bottom
	    Shader bshader = new LinearGradient(0, newH, 0, h, Color.GRAY, Color.LTGRAY, Shader.TileMode.CLAMP);
	    paint.setShader(bshader);
	    c.drawRect(think, newH, newW  , h, paint);

	    //Corner
	    Shader cchader = new LinearGradient(0, newH, 0, h, Color.LTGRAY, Color.LTGRAY, Shader.TileMode.CLAMP);
	    paint.setShader(cchader);
	    c.drawRect(newW, newH, w  , h, paint);


	    c.drawBitmap(sbmp, 0, 0, null);

	    return bmp;
	}
	
	public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
	    int sourceWidth = source.getWidth();
	    int sourceHeight = source.getHeight();

	    // Compute the scaling factors to fit the new height and width, respectively.
	    // To cover the final image, the final scaling will be the bigger 
	    // of these two.
	    float xScale = (float) newWidth / sourceWidth;
	    float yScale = (float) newHeight / sourceHeight;
	    float scale = Math.max(xScale, yScale);

	    // Now get the size of the source bitmap when scaled
	    float scaledWidth = scale * sourceWidth;
	    float scaledHeight = scale * sourceHeight;

	    // Let's find out the upper left coordinates if the scaled bitmap
	    // should be centered in the new size give by the parameters
	    float left = (newWidth - scaledWidth) / 2;
	    float top = (newHeight - scaledHeight) / 2;

	    // The target rectangle for the new, scaled version of the source bitmap will now
	    // be
	    RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

	    // Finally, we create a new bitmap of the specified size and draw our new,
	    // scaled bitmap onto it.
	    Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
	    Canvas canvas = new Canvas(dest);
	    canvas.drawBitmap(source, null, targetRect, null);

	    return dest;
	}
	
}
