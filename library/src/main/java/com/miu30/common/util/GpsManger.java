package com.miu30.common.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

public class GpsManger {
	/** 
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 
     * @param context 
     * @return true 表示开启 
     */  
	  public static final boolean isOPen(final Context context) {  
	        LocationManager locationManager   
	                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
	        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
	        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
	        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
	        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
	        if (gps || network) {  
	            return true;  
	        }  
	  
	        return false;  
	    } 
	  
	  
	  public static final void openGps(Activity context){
		  // 转到手机设置界面，用户设置GPS  
          Intent intent = new Intent(  
                  Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
          context.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面 
		  
	  }
	  /** 
	     * 强制帮用户打开GPS 
	     * @param context 
	     */  
	    public static final void openGPS(Context context) {  
	        Intent GPSIntent = new Intent();  
	        GPSIntent.setClassName("com.android.settings",  
	                "com.android.settings.widget.SettingsAppWidgetProvider");  
	        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");  
	        GPSIntent.setData(Uri.parse("custom:3"));  
	        try {  
	            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();  
	        } catch (CanceledException e) {  
	            e.printStackTrace();  
	        } 
	        
	        
//	        Intent intent = new Intent("com.android.settings.location.MODE_CHANGING");
//	        int currentMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
//	                                       Settings.Secure.LOCATION_MODE_OFF);
//	        intent.putExtra("CURRENT_MODE", currentMode);
//	        intent.putExtra("NEW_MODE", mode);
//
//	        Log.e("jerry", "currentMode="+currentMode + " newmode="+mode);
//	        context.sendBroadcast(intent, android.Manifest.permission.WRITE_SECURE_SETTINGS);
//	        Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, mode);
	    } 
	
}
