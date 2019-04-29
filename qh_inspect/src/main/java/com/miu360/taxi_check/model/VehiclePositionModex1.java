package com.miu360.taxi_check.model;

import java.io.Serializable;

import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.miu360.taxi_check.App;
	
	
@Table(name = "VehiclePositionS")
public class VehiclePositionModex1 implements Serializable {
	public static DaoConfig getDaoConfig() {
		DaoConfig config = new DaoConfig(App.self);
		config.setDbName("VehiclePositionS");
		config.setDbVersion(1);
		return config;
	}
	private int ID;
	@Column(column = "CPH")
	private String CPH;
	@Column(column = "RY_MC")
	private String RY_MC;
	@Column(column = "RY_SFZH")
	private String RY_SFZH;
	@Column(column = "RY_ZJZH")
	private String RY_ZJZH;
	@Column(column = "YH_MC")
	private String YH_MC;
	@Column(column = "YXBZ")
	private String YXBZ;
	@Column(column = "RKSJ")
	private long RKSJ;
	@Column(column = "RKSM")
	private String RKSM;
	@Column(column = "CKSJ")
	private String CKSJ;
	@Column(column = "CKSM")
	private String CKSM;
	@Column(column = "KYK_LX")
	private String KYK_LX;
	@Column(column = "KYX_BMS")
	private String KYX_BMS;
	@Column(column = "KYX_MCS")
	private String KYX_MCS;
	@Column(column = "ADK_WFXW")
	private String ADK_WFXW;
	@Column(column = "CL_ID")
	private String CL_ID;
	@Column(column = "YH_ID")
	private String YH_ID;
	@Column(column = "RY_ID")
	private String RY_ID;
	@Column(column = "CREATE_DATE")
	private long CREATE_DATE;
	@Column(column = "UPDATE_DATE")
	private long UPDATE_DATE;
	@Column(column = "GSPDATA")
	private GSPDATA GSPDATA;
	
	public void setID(int ID){
	this.ID = ID;
	}
	public int getID(){
	return this.ID;
	}
	public void setCPH(String CPH){
	this.CPH = CPH;
	}
	public String getCPH(){
	return this.CPH;
	}
	public void setRY_MC(String RY_MC){
	this.RY_MC = RY_MC;
	}
	public String getRY_MC(){
	return this.RY_MC;
	}
	public void setRY_SFZH(String RY_SFZH){
	this.RY_SFZH = RY_SFZH;
	}
	public String getRY_SFZH(){
	return this.RY_SFZH;
	}
	public void setRY_ZJZH(String RY_ZJZH){
	this.RY_ZJZH = RY_ZJZH;
	}
	public String getRY_ZJZH(){
	return this.RY_ZJZH;
	}
	public void setYH_MC(String YH_MC){
	this.YH_MC = YH_MC;
	}
	public String getYH_MC(){
	return this.YH_MC;
	}
	public void setYXBZ(String YXBZ){
	this.YXBZ = YXBZ;
	}
	public String getYXBZ(){
	return this.YXBZ;
	}
	public void setRKSJ(int RKSJ){
	this.RKSJ = RKSJ;
	}
	public long getRKSJ(){
	return this.RKSJ;
	}
	public void setRKSM(String RKSM){
	this.RKSM = RKSM;
	}
	public String getRKSM(){
	return this.RKSM;
	}
	public void setCKSJ(String CKSJ){
	this.CKSJ = CKSJ;
	}
	public String getCKSJ(){
	return this.CKSJ;
	}
	public void setCKSM(String CKSM){
	this.CKSM = CKSM;
	}
	public String getCKSM(){
	return this.CKSM;
	}
	public void setKYK_LX(String KYK_LX){
	this.KYK_LX = KYK_LX;
	}
	public String getKYK_LX(){
	return this.KYK_LX;
	}
	public void setKYX_BMS(String KYX_BMS){
	this.KYX_BMS = KYX_BMS;
	}
	public String getKYX_BMS(){
	return this.KYX_BMS;
	}
	public void setKYX_MCS(String KYX_MCS){
	this.KYX_MCS = KYX_MCS;
	}
	public String getKYX_MCS(){
	return this.KYX_MCS;
	}
	public void setADK_WFXW(String ADK_WFXW){
	this.ADK_WFXW = ADK_WFXW;
	}
	public String getADK_WFXW(){
	return this.ADK_WFXW;
	}
	public void setCL_ID(String CL_ID){
	this.CL_ID = CL_ID;
	}
	public String getCL_ID(){
	return this.CL_ID;
	}
	public void setYH_ID(String YH_ID){
	this.YH_ID = YH_ID;
	}
	public String getYH_ID(){
	return this.YH_ID;
	}
	public void setRY_ID(String RY_ID){
	this.RY_ID = RY_ID;
	}
	public String getRY_ID(){
	return this.RY_ID;
	}
	public void setCREATE_DATE(int CREATE_DATE){
	this.CREATE_DATE = CREATE_DATE;
	}
	public long getCREATE_DATE(){
	return this.CREATE_DATE;
	}
	public void setUPDATE_DATE(int UPDATE_DATE){
	this.UPDATE_DATE = UPDATE_DATE;
	}
	public long getUPDATE_DATE(){
	return this.UPDATE_DATE;
	}
	public void setGSPDATA(GSPDATA GSPDATA){
	this.GSPDATA = GSPDATA;
	}
	public GSPDATA getGSPDATA(){
	return this.GSPDATA;
	}

	@Override
	public String toString() {
		return "VehiclePositionModex1 [ID=" + ID + ", CPH=" + CPH + ", RY_MC="
				+ RY_MC + ", RY_SFZH=" + RY_SFZH + ", RY_ZJZH=" + RY_ZJZH
				+ ", YH_MC=" + YH_MC + ", YXBZ=" + YXBZ + ", RKSJ=" + RKSJ
				+ ", RKSM=" + RKSM + ", CKSJ=" + CKSJ + ", CKSM=" + CKSM
				+ ", KYK_LX=" + KYK_LX + ", KYX_BMS=" + KYX_BMS + ", KYX_MCS="
				+ KYX_MCS + ", ADK_WFXW=" + ADK_WFXW + ", CL_ID=" + CL_ID
				+ ", YH_ID=" + YH_ID + ", RY_ID=" + RY_ID + ", CREATE_DATE="
				+ CREATE_DATE + ", UPDATE_DATE=" + UPDATE_DATE + ", GSPDATA="
				+ GSPDATA + "]";
	}

	public 	class GSPDATA implements Serializable{
		@Column(column = "vname")
		private String vname;
		@Column(column = "lon")
		private double lon;
		@Column(column = "lat")
		private double lat;
		@Column(column = "speed")
		private double speed;
		@Column(column = "head")
		private int head;
		@Column(column = "utc")
		private long utc;
		@Column(column = "strDate")
		private String strDate;
		@Column(column = "alarmReason")
		private String alarmReason;
		
		@Override
		public String toString() {
			return "GSPDATA [vname=" + vname + ", lon=" + lon + ", lat=" + lat
					+ ", speed=" + speed + ", head=" + head + ", utc=" + utc
					+ ", strDate=" + strDate + ", alarmReason=" + alarmReason
					+ "]";
		}
		public void setVname(String vname){
		this.vname = vname;
		}
		public String getVname(){
		return this.vname;
		}
		public void setLon(double lon){
		this.lon = lon;
		}
		public double getLon(){
		return this.lon;
		}
		public void setLat(double lat){
		this.lat = lat;
		}
		public double getLat(){
		return this.lat;
		}
		public void setSpeed(double speed){
		this.speed = speed;
		}
		public double getSpeed(){
		return this.speed;
		}
		public void setHead(int head){
		this.head = head;
		}
		public int getHead(){
		return this.head;
		}
		public void setUtc(long utc){
		this.utc = utc;
		}
		public long getUtc(){
		return this.utc;
		}
		public void setStrDate(String strDate){
		this.strDate = strDate;
		}
		public String getStrDate(){
		return this.strDate;
		}
		public void setAlarmReason(String alarmReason){
		this.alarmReason = alarmReason;
		}
		public String getAlarmReason(){
		return this.alarmReason;
		}

		
	}
}

