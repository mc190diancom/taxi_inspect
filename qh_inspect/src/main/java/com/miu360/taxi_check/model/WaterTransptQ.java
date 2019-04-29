package com.miu360.taxi_check.model;

public class WaterTransptQ {
    private String hylb;
    
    private String compName;

	public String getHylb() {
		return hylb;
	}

	public void setHylb(String hylb) {
		this.hylb = hylb;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	@Override
	public String toString() {
		return "WaterTransptQ [hylb=" + hylb + ", compName=" + compName + "]";
	}
    
}
