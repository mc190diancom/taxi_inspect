package com.miu360.taxi_check.common;

public final class MsgConfig {

	// 这部分参数是APP运行时候根据实际情况动态设置
	public static long DEVICE_ID = 0L; // 终端唯一标识，APP根据实际情况设置，为0表示未设置终端
	public static double lng = 0; // 实时经度
	public static double lat = 0; // 实时纬度
	public static double select_lng = 0; // 用户自选经度
	public static double select_lat = 0; // 用户自选纬度
	public static double common_lng = 0; // 执法常用经度
	public static double common_lat = 0; // 执法常用纬度

	public static boolean isUpdate = false;
	// 这部分参数用于配置连接信息
	public final static String MSG_SERVER_IP = "http://4404344c.nat123.net:9876/";//
	// 正式消息服务器IP
	// public final static String MSG_SERVER_IP = "http://10.252.2.67:9876/";// 测试消息服务器IP
//	public final static short MSG_SERVER_PORT = 8081;// 消息服务器端口
//	public final static short MSG_DEVICE_TYPE = 2; // 司机端用1，用车端用2
}
