package com.miu360.taxi_check.model;

import java.io.Serializable;

import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.miu360.taxi_check.App;

@Table(name = "VehiclePositionS")
public class VehiclePositionModex implements Serializable {
	public static DaoConfig getDaoConfig() {
		DaoConfig config = new DaoConfig(App.self);
		config.setDbName("VehiclePositionS");
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
	@Column(column = "speed")
	private double speed;
	@Column(column = "head")
	private int head;
	@Column(column = "utc")
	private int utc;
	@Column(column = "strDate")
	private String strDate;
	@Column(column = "yujin_reason")
	private String yujin_reason;
	@Column(column = "alarmReason")
	private String alarmReason;

	public String getAlarmReason() {
		return alarmReason;
	}

	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getVname() {
		return this.vname;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLon() {
		return this.lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		return this.lat;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public int getHead() {
		return this.head;
	}

	public void setUtc(int utc) {
		this.utc = utc;
	}

	public int getUtc() {
		return this.utc;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getStrDate() {
		return this.strDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getYujin_reason() {
		return yujin_reason;
	}

	public void setYujin_reason(String yujin_reason) {
		this.yujin_reason = yujin_reason;
	}

}
