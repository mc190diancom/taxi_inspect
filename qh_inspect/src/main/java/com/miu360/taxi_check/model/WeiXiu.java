package com.miu360.taxi_check.model;

import java.io.Serializable;

public class WeiXiu implements Serializable{
   private String NAME;
   
   private String LICENCE;
   
   private String ADDRESS;
   
   private String LEGAL;
   
   private String MANAGER;
   
   private String MANAGER_PHONE;
   
   private String EMPLOYED;
   
   private String EMPLOYED_MANAGE_QUALITY;
   
   private String LICENCE_BEGIN_TIME;
   
   private String LICENCE_END_TIME;

	@Override
    public String toString() {
		return "WeiXiu [NAME=" + NAME + ", LICENCE=" + LICENCE + ", ADDRESS="
				+ ADDRESS + ", LEGAL=" + LEGAL + ", MANAGER=" + MANAGER
				+ ", MANAGER_PHONE=" + MANAGER_PHONE + ", EMPLOYED=" + EMPLOYED
				+ ", EMPLOYED_MANAGE_QUALITY=" + EMPLOYED_MANAGE_QUALITY
				+ ", LICENCE_BEGIN_TIME=" + LICENCE_BEGIN_TIME
				+ ", LICENCE_END_TIME=" + LICENCE_END_TIME + "]";
     }

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
	
	public String getADDRESS() {
		return ADDRESS;
	}
	
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	
	public String getLEGAL() {
		return LEGAL;
	}
	
	public void setLEGAL(String lEGAL) {
		LEGAL = lEGAL;
	}
	
	public String getMANAGER() {
		return MANAGER;
	}
	
	public void setMANAGER(String mANAGER) {
		MANAGER = mANAGER;
	}
	
	public String getMANAGER_PHONE() {
		return MANAGER_PHONE;
	}
	
	public void setMANAGER_PHONE(String mANAGER_PHONE) {
		MANAGER_PHONE = mANAGER_PHONE;
	}
	
	public String getEMPLOYED() {
		return EMPLOYED;
	}
	
	public void setEMPLOYED(String eMPLOYED) {
		EMPLOYED = eMPLOYED;
	}
	
	public String getEMPLOYED_MANAGE_QUALITY() {
		return EMPLOYED_MANAGE_QUALITY;
	}
	
	public void setEMPLOYED_MANAGE_QUALITY(String eMPLOYED_MANAGE_QUALITY) {
		EMPLOYED_MANAGE_QUALITY = eMPLOYED_MANAGE_QUALITY;
	}
	
	public String getLICENCE_BEGIN_TIME() {
		return LICENCE_BEGIN_TIME;
	}
	
	public void setLICENCE_BEGIN_TIME(String lICENCE_BEGIN_TIME) {
		LICENCE_BEGIN_TIME = lICENCE_BEGIN_TIME;
	}
	
	public String getLICENCE_END_TIME() {
		return LICENCE_END_TIME;
	}
	
	public void setLICENCE_END_TIME(String lICENCE_END_TIME) {
		LICENCE_END_TIME = lICENCE_END_TIME;
	}
   
   
}
