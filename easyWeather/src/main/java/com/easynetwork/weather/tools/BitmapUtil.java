package com.easynetwork.weather.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;

/**
 * 位图工具
 * @author funben
 *
 */
public class BitmapUtil {

    /**
     * @Description 
     * @param bmp
     * @param File file
     * @return
     */
    public static boolean  saveBitmap2file(Bitmap bmp,File file){
    	String path = file.getAbsolutePath();
    	File fileDir = new File(path.substring(0, path.lastIndexOf("/")));
    	if (!fileDir.exists() || !fileDir.isDirectory()) {
    		fileDir.mkdirs();
        }
    	
    	if(file.exists()){
			file.delete();
		}
        CompressFormat format= Bitmap.CompressFormat.PNG;
        int quality = 100;
        OutputStream stream = null;
        boolean saveSuccess = false;
        try {
        	file.createNewFile();
            stream = new FileOutputStream(file);
            saveSuccess = bmp.compress(format, quality, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveSuccess;
    }
    
    /**
     * @Description 
     * @param bmp
     * @param filepath
     * @return
     */
    public static boolean  saveBitmap2file(Bitmap bmp,String filepath){
    	File file = new File(filepath);
    	return saveBitmap2file(bmp, file);
    }
    
    
    /**
     * @Description 获取图片的路径列表
     * @param filePath
     * @param uid
     * @return
     */
    public static List<String> getBmpFiles(String filePath) {
    	List<String> fileList = new ArrayList<String>();
		File fileDir = new File(filePath);
		if (fileDir.exists() && fileDir.isDirectory()) {// 路径存在并且是目录
			File[] files = fileDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getAbsolutePath().endsWith(".png")) {
    				fileList.add(files[i].getAbsolutePath());
    			}
			}
		}
		return fileList;
    }
   
    /**
     * @Description 使用Bitmap加Matrix来缩放
     * @param bitmap 
     * @param w 定义宽
     * @param h 定义高
     * @return
     */
   public static Bitmap resizeImage(Bitmap bitmap, int w, int h) 
   {  
       Bitmap BitmapOrg = bitmap;  
       int width = BitmapOrg.getWidth();  
       int height = BitmapOrg.getHeight();  
       int newWidth = w;  
       int newHeight = h;  

       float scaleWidth = ((float) newWidth) / width;  
       float scaleHeight = ((float) newHeight) / height;  

       Matrix matrix = new Matrix();  
       matrix.postScale(scaleWidth, scaleHeight);  
       // if you want to rotate the Bitmap   
       // matrix.postRotate(45);   
       Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,  height, matrix, true);  
       return resizedBitmap;  
   }
}
