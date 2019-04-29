package com.miu360.taxi_check.model;

public class DriverNameInfoQ {

    private String cyzgz;
	
    private String sfzh;
    
    private String name;
    
    private int startIndex;
    
    private int endIndex;

	public String getCyzgz() {
		return cyzgz;
	}

	public void setCyzgz(String cyzgz) {
		this.cyzgz = cyzgz;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	@Override
	public String toString() {
		return "DriverNameInfoQ [cyzgz=" + cyzgz + ", sfzh=" + sfzh + ", name="
				+ name + ", startIndex=" + startIndex + ", endIndex="
				+ endIndex + "]";
	}
    
    
}
