package com.miu360.taxi_check.model;

import java.io.Serializable;

import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.miu360.taxi_check.App;

@Table(name = "CameraDetailS")
public class CameraDetail implements Serializable {
	public static DaoConfig getDaoConfig() {
		DaoConfig config = new DaoConfig(App.self);
//		创建数据库库名
		config.setDbName("CameraDetailS");
//		创建数据库版本
		config.setDbVersion(1);
		return config;
	}

	/**
	 * 本地存储id
	 */
	@Id
	private long id;
	@Column(column = "vname")
	private String vname;
	@Column(column = "lon")
	private double lon;
	@Column(column = "lat")
	private double lat;
	@Column(column = "alarmReason")
	private String alarmReason;
	@Column(column = "SXT_ID")
	private String SXT_ID;
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getAlarmReason() {
		return alarmReason;
	}
	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}
	public String getSXT_ID() {
		return SXT_ID;
	}
	public void setSXT_ID(String sXT_ID) {
		SXT_ID = sXT_ID;
	}


}
