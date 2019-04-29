package com.miu360.taxi_check.model;

public class FastCompany {
    private String NAME;
    private String LICENCE;
    private String LEGAL;
    private String ADDRESS;
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getLICENCE() {
		return LICENCE;
	}
	public void setLICENCE(String lICENCE) {
		LICENCE = lICENCE;
	}
	public String getLEGAL() {
		return LEGAL;
	}
	public void setLEGAL(String lEGAL) {
		LEGAL = lEGAL;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	@Override
	public String toString() {
		return "FastCompany [NAME=" + NAME + ", LICENCE=" + LICENCE
				+ ", LEGAL=" + LEGAL + ", ADDRESS=" + ADDRESS + "]";
	}
    
    
}
