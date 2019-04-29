package com.miu360.taxi_check.model;

public class RegisterInfo {
	private String id;

	private String hylb;

	private String jdkh;

	private String driverName;

	private String llh;

	private String zfry1;

	private String zfry2;

	private String address;

	private String zfsj;

	private String vname;

	private String sfzh;

	private String corpname;

	private String yyzh;

	private String status;

	private String vehicle_check_items;

	private String person_check_items;

	private String yehu_check_items;

	private String zfzh;

	private String zfdwmc;

	private String startTime;

	private String endTime;

	private int normal;

	private int illegal;

	private int total;

	private int dataSource;

	private double lon;

	private double lat;

	private String jyxkz;

	private String zwstatus;

	private String jcbh;
	
	private String jclb;
	

	public String getJclb() {
		return jclb;
	}

	public void setJclb(String jclb) {
		this.jclb = jclb;
	}

	public String getZwstatus() {
		return zwstatus;
	}

	public void setZwstatus(String zwstatus) {
		this.zwstatus = zwstatus;
	}

	public String getJcbh() {
		return jcbh;
	}

	public void setJcbh(String jcbh) {
		this.jcbh = jcbh;
	}

	public String getJyxkz() {
		return jyxkz;
	}

	public void setJyxkz(String jyxkz) {
		this.jyxkz = jyxkz;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHylb() {
		return hylb;
	}

	public void setHylb(String hylb) {
		this.hylb = hylb;
	}

	public String getJdkh() {
		return jdkh;
	}

	public void setJdkh(String jdkh) {
		this.jdkh = jdkh;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getLlh() {
		return llh;
	}

	public void setLlh(String llh) {
		this.llh = llh;
	}

	public String getZfry1() {
		return zfry1;
	}

	public void setZfry1(String zfry1) {
		this.zfry1 = zfry1;
	}

	public String getZfry2() {
		return zfry2;
	}

	public void setZfry2(String zfry2) {
		this.zfry2 = zfry2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZfsj() {
		return zfsj;
	}

	public void setZfsj(String zfsj) {
		this.zfsj = zfsj;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getYyzh() {
		return yyzh;
	}

	public void setYyzh(String yyzh) {
		this.yyzh = yyzh;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicle_check_items() {
		return vehicle_check_items;
	}

	public void setVehicle_check_items(String vehicle_check_items) {
		this.vehicle_check_items = vehicle_check_items;
	}

	public String getPerson_check_items() {
		return person_check_items;
	}

	public void setPerson_check_items(String person_check_items) {
		this.person_check_items = person_check_items;
	}

	public String getYehu_check_items() {
		return yehu_check_items;
	}

	public void setYehu_check_items(String yehu_check_items) {
		this.yehu_check_items = yehu_check_items;
	}

	public String getZfzh() {
		return zfzh;
	}

	public void setZfzh(String zfzh) {
		this.zfzh = zfzh;
	}

	public String getZfdwmc() {
		return zfdwmc;
	}

	public void setZfdwmc(String zfdwmc) {
		this.zfdwmc = zfdwmc;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getNormal() {
		return normal;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public int getIllegal() {
		return illegal;
	}

	public void setIllegal(int illegal) {
		this.illegal = illegal;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getDataSource() {
		return dataSource;
	}

	public void setDataSource(int dataSource) {
		this.dataSource = dataSource;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((corpname == null) ? 0 : corpname.hashCode());
		result = prime * result + dataSource;
		result = prime * result
				+ ((driverName == null) ? 0 : driverName.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((hylb == null) ? 0 : hylb.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + illegal;
		result = prime * result + ((jcbh == null) ? 0 : jcbh.hashCode());
		result = prime * result + ((jclb == null) ? 0 : jclb.hashCode());
		result = prime * result + ((jdkh == null) ? 0 : jdkh.hashCode());
		result = prime * result + ((jyxkz == null) ? 0 : jyxkz.hashCode());
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((llh == null) ? 0 : llh.hashCode());
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + normal;
		result = prime
				* result
				+ ((person_check_items == null) ? 0 : person_check_items
						.hashCode());
		result = prime * result + ((sfzh == null) ? 0 : sfzh.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + total;
		result = prime
				* result
				+ ((vehicle_check_items == null) ? 0 : vehicle_check_items
						.hashCode());
		result = prime * result + ((vname == null) ? 0 : vname.hashCode());
		result = prime
				* result
				+ ((yehu_check_items == null) ? 0 : yehu_check_items.hashCode());
		result = prime * result + ((yyzh == null) ? 0 : yyzh.hashCode());
		result = prime * result + ((zfdwmc == null) ? 0 : zfdwmc.hashCode());
		result = prime * result + ((zfry1 == null) ? 0 : zfry1.hashCode());
		result = prime * result + ((zfry2 == null) ? 0 : zfry2.hashCode());
		result = prime * result + ((zfsj == null) ? 0 : zfsj.hashCode());
		result = prime * result + ((zfzh == null) ? 0 : zfzh.hashCode());
		result = prime * result
				+ ((zwstatus == null) ? 0 : zwstatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisterInfo other = (RegisterInfo) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (corpname == null) {
			if (other.corpname != null)
				return false;
		} else if (!corpname.equals(other.corpname))
			return false;
		if (dataSource != other.dataSource)
			return false;
		if (driverName == null) {
			if (other.driverName != null)
				return false;
		} else if (!driverName.equals(other.driverName))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (hylb == null) {
			if (other.hylb != null)
				return false;
		} else if (!hylb.equals(other.hylb))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (illegal != other.illegal)
			return false;
		if (jcbh == null) {
			if (other.jcbh != null)
				return false;
		} else if (!jcbh.equals(other.jcbh))
			return false;
		if (jclb == null) {
			if (other.jclb != null)
				return false;
		} else if (!jclb.equals(other.jclb))
			return false;
		if (jdkh == null) {
			if (other.jdkh != null)
				return false;
		} else if (!jdkh.equals(other.jdkh))
			return false;
		if (jyxkz == null) {
			if (other.jyxkz != null)
				return false;
		} else if (!jyxkz.equals(other.jyxkz))
			return false;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (llh == null) {
			if (other.llh != null)
				return false;
		} else if (!llh.equals(other.llh))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		if (normal != other.normal)
			return false;
		if (person_check_items == null) {
			if (other.person_check_items != null)
				return false;
		} else if (!person_check_items.equals(other.person_check_items))
			return false;
		if (sfzh == null) {
			if (other.sfzh != null)
				return false;
		} else if (!sfzh.equals(other.sfzh))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (total != other.total)
			return false;
		if (vehicle_check_items == null) {
			if (other.vehicle_check_items != null)
				return false;
		} else if (!vehicle_check_items.equals(other.vehicle_check_items))
			return false;
		if (vname == null) {
			if (other.vname != null)
				return false;
		} else if (!vname.equals(other.vname))
			return false;
		if (yehu_check_items == null) {
			if (other.yehu_check_items != null)
				return false;
		} else if (!yehu_check_items.equals(other.yehu_check_items))
			return false;
		if (yyzh == null) {
			if (other.yyzh != null)
				return false;
		} else if (!yyzh.equals(other.yyzh))
			return false;
		if (zfdwmc == null) {
			if (other.zfdwmc != null)
				return false;
		} else if (!zfdwmc.equals(other.zfdwmc))
			return false;
		if (zfry1 == null) {
			if (other.zfry1 != null)
				return false;
		} else if (!zfry1.equals(other.zfry1))
			return false;
		if (zfry2 == null) {
			if (other.zfry2 != null)
				return false;
		} else if (!zfry2.equals(other.zfry2))
			return false;
		if (zfsj == null) {
			if (other.zfsj != null)
				return false;
		} else if (!zfsj.equals(other.zfsj))
			return false;
		if (zfzh == null) {
			if (other.zfzh != null)
				return false;
		} else if (!zfzh.equals(other.zfzh))
			return false;
		if (zwstatus == null) {
			if (other.zwstatus != null)
				return false;
		} else if (!zwstatus.equals(other.zwstatus))
			return false;
		return true;
	}

}
