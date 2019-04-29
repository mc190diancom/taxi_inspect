package com.miu360.taxi_check.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.util.Log;

/**
 * 閸ュ墽澧栫憗浣稿瀼閹垮秳缍旂猾锟�
 * @author mengbo
 *
 */
public class CutPhotos {
	int cavas_width = 1024;//閻㈣绔锋妯款吇婢堆冪毈   
    int cavas_height = 768;
      
    int cut_width;  
    int cut_height;  
      
    Matrix cut_matrix = new Matrix();  
      
    int startx = 0;//閸擃亜鍨忛惃鍕崳婵绮�  
    int starty = 0;  
  
    Canvas cutCanvas = null;  
    Bitmap cacheBitmap = null;  
    Bitmap resultBitmap = null;//鐟曚椒绻氱�妯兼畱閹稿洤鐣炬径褍鐨惃鍑歩tmap  
    
	public CutPhotos(Context context,int cut_width,int cut_height){
		cavas_width = cut_width;
		cavas_height = cut_height;
		cacheBitmap = Bitmap.createBitmap(cavas_width, cavas_height, Config.RGB_565);  
        cutCanvas = new Canvas();  
        cutCanvas.setBitmap(cacheBitmap);  
	}
	
	/**
	 * 娣囨繂鐡ㄩ崶鍓у
	 * @param file_name
	 */
	private String cutPhoto(String file_name,String foldPath){  
        cutCanvas.drawBitmap(resultBitmap, 0,0,null);  
        //String foldPath = "/mnt/sdcard/DCIM/Camera/";  
        File foldFile = new File(foldPath);  
        if(!foldFile.exists()){  
            foldFile.mkdirs();  
        }  
        String fileName = foldPath + file_name + ".jpg";  
        FileOutputStream out;  
        try {  
            File saveFile = new File(fileName);  
            if(saveFile.exists()){  
                saveFile.delete();  
            }  
            out = new FileOutputStream(new File(fileName));  
            cacheBitmap.compress(CompressFormat.JPEG, 100, out);//娣囨繂鐡ㄩ弬鍥︽   
            out.close();  
            return fileName;
        } catch (Exception e) {  
            e.printStackTrace();  
            return "";
        }  
          
    }  
      
      
    /**
     * 閼惧嘲褰囩憗浣稿瀼閸氬骸娴橀悧鍥熅瀵帮拷 
     * @param bitmap 閸樼喎娴橀悧锟�
     * @param file_name 娣囨繂鐡ㄩ崶鍓у閻ㄥ嫬鎮曠�锟�
     * @return String 鐟佷礁鍨忛崥搴ょ熅瀵帮拷
     */
    public String getCutPhotoPath(Bitmap bitmap,String file_name,String foldPath){  
//        if(bitmap.getWidth() > bitmap.getHeight()){//瑜版悩idth>height閻ㄥ嫭妞傞崐娆欑礉鐠佸墽鐤嗙紓鈺傛杹濮ｆ柧绶�  
            cut_width = bitmap.getWidth(); //閸樼喎娴樻妯侯啍
            cut_height = bitmap.getHeight();  
            cut_matrix.postScale(1,1);  //閸ュ墽澧栨稉宥嗗瘻濮ｆ柧绶ョ紓鈺傛杹
            startx = (bitmap.getWidth() - cavas_width)/2;//鐠佸墽鐤嗙挧宄邦瀶鐟佷礁鍨忛崸鎰垼
            starty = (bitmap.getHeight() - cavas_height)/2;  
            Log.e("", "cut_width="+cut_width+";cut_height="+cut_height+";cavas_width="+cavas_width+";cavas_height="+cavas_height);
            resultBitmap = Bitmap.createBitmap(bitmap,startx,starty,cavas_width,cavas_height,cut_matrix,true); 
//        }else if(bitmap.getWidth() < bitmap.getHeight()){//瑜版悩idth < height閻ㄥ嫭妞傞崐娆欑礉鐠佸墽鐤嗙紓鈺傛杹濮ｆ柧绶�  
//            cut_width = bitmap.getWidth();  
//            cut_height =  (bitmap.getWidth()*3)/4;  
//            float xb = ((float) cavas_width) / cut_width;  
//            float yb = ((float) cavas_height) / cut_height;  
//            cut_matrix.postScale(xb,yb);  
//            starty = (bitmap.getHeight() - cut_height)/2;  
//            resultBitmap = Bitmap.createBitmap(bitmap,0,starty,cut_width,cut_height,cut_matrix,true);  
//        }  
        return cutPhoto(file_name,foldPath);  
    }  
}
