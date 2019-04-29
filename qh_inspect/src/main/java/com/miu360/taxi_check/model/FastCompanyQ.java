package com.miu360.taxi_check.model;

public class FastCompanyQ extends Page{
    private String name;
    private String xkzh;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getXkzh() {
		return xkzh;
	}
	public void setXkzh(String xkzh) {
		this.xkzh = xkzh;
	}
	@Override
	public String toString() {
		return "FastCompanyQ [name=" + name + ", xkzh=" + xkzh + "]";
	}
    
}
