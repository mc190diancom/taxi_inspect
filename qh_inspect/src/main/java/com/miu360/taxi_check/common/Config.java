package com.miu360.taxi_check.common;

import android.os.Environment;

import com.miu30.common.MiuBaseApp;
import com.miu360.inspect.BuildConfig;

public class Config {
	public final static String DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/jicha/";
	public final static String PATH = Environment.getExternalStorageDirectory().getPath() + "/qh_inspect/";
	public final static String FILE_NAME = "inspector.txt";
	public final static String FILE_NAME2 = "temp.txt";
	public final static String ID_FILE_NAME = "cardqr.txt";
	public final static String ID_FILE_NAME3 = "test.txt";

	// 正式
	//public static final String IP = "10.252.2.67";
	//public static final String SERVER_BASIC = "http://" + IP + ":9875/";
	// 专网测试
	//public static final String IP = "10.252.2.68";
	//public static final String SERVER_BASIC = "http://" + IP + ":9877/";

	// public static final String IP = "10.212.160.180";
	// public static final String SERVER_BASIC = "http://" + IP + ":9877/";
	// 外网测试
	// public static final String SERVER_BASIC =
	// "http://4404344c.nat123.net:9876/";

	// demo测试
	public static final String IP = BuildConfig.IP;
	public static final String SERVER_BASIC = "http://" + IP + BuildConfig.PORT;

	// public static final String IP = "123.57.236.212";
	// public static final String SERVER_BASIC = "http://" + IP+":8080/";
	// http://15c154k458.iask.in:14672
	public static int chk = 2;

	public static final String FLAG = "2";
	public static final String SERVER = SERVER_BASIC + "requestApi";
	public static final String SERVER_CHUZU = SERVER + "/chuzu";
	public static final String SERVER_LVYOU = SERVER + "/lvyou";
	public static final String SERVER_ZULIN = SERVER + "/zulin";
	public static final String SERVER_HUOYUN = SERVER + "/huoyun";
	public static final String SERVER_SHENGJI = SERVER + "/ShengJi";
	public static final String SERVER_XINXI = SERVER + "/xinxi";
	public static final String SERVER_FENBU = SERVER + "/person_fb";
	public static final String SERVER_ZFRY = SERVER + "/zfry";
	public static final String SERVER_OTHER = SERVER + "/other";
	public static final String SERVER_SPECIAL = SERVER + "/special";
	public static final String SERVER_ZFRY_PHOTO = SERVER_ZFRY + "/queryImage/";
	public static final String SERVER_ZFRY_SAVE = SERVER_ZFRY + "/saveImage/";
	public static final String SERVER_POSITION = SERVER + "/common";
	public static final String SERVER_ROATE = SERVER + "/permission";
	public static final String SERVER_SAVEINFO = SERVER + "/bs_Taxi";
	public static final String SERVER_TAXIINFO = SERVER + "/app_Taxi";
	public static final String SERVER_LINE = SERVER + "/Passengerline";
	public static final String SERVER_DAOLUINFO = SERVER + "/app_DaoLu";
	public static final String SERVER_LOADNOTECE_IMAGE = SERVER_SAVEINFO + "/queryImage/";
	public static final String SERVER_FZBSAVE = SERVER + "/special";
	public static final String SERVER_GROUP = SERVER + "/group";
	public static final String SERVER_POWER = SERVER + "/Powers";

	// 照片路径
	public static final String PHOTO_PATH = SERVER_BASIC + "downLaodImage/";

}
