package com.miu360.taxi_check.model;

import java.util.List;

public class TaxiCompany {
    private int total;
    private List<Info> info;
    private List<Rows> rows;
    
  
    public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Info> getInfo() {
		return info;
	}

	public void setInfo(List<Info> info) {
		this.info = info;
	}

	public List<Rows> getRows() {
		return rows;
	}

	public void setRows(List<Rows> rows) {
		this.rows = rows;
	}

	public class Info {
	    private String ID;
	    private String NAME;
	    private String SIMPLE_NAME;
	    private String IS_LOCKED;
	    private String LICENCE;
	    private String ADDRESS;
	    private String FAX;
	    private String ZIP;
	    private String LEGAL;
	    private String LEGAL_IDCARD_TYPE;
	    private String LEGAL_IDCARD;
	    private String MANAGER;
	    private String MANAGER_PHONE;
	    private String MANAGER_IDCARD;
	    private String EMAIL;
	    private String ECONOMIC_TYPE;
	    private String BELONG_ORG;
	    private String JB_PERSON;
	    private String JB_PHONE;
	    private String JB_MOBILE;
	    private String HANDLE_WITHOUT_VALIDATE;
	    private String OFFICE_ADDR;
	    private String EDIT_TIME;
	    private String BUSINESS_LICENSE_NUMBER;
	    private String STATUS;
	    private String FIRST_TIME;
	    private String OLD_YHID;
	    private String START_DATE;
	    private String END_DATE;
	    private String REGISTERED_CAPITAL;
	    private String ORGANIZATION_CODE;
	    private String OFFICE_ORG;
	    private String CONTACT_NUMBER;
	    private String CONTACT;
	    private String PREPARE_DRIVER_NUM;
	    private String CAR_QUOTA;
	    private String CAR_NUMBER;
	    private String TAX_ID;
	    private String ACTUAL_PREPARE_RATE;
	    private String DRIVER_NUMBER;
	    private String PREPARE_RATE_QUOTA;
	    private String DOUBLE_RATE_QUOTA;
	    private String ACTUAL_DOUBLE_RATE;
	    private String EDIT_END;
	    private String GT_CZJCH_NAME;
	    private String YH_TYPE;
	    private String LH_DATE;
	    private String LH_REASON;
	    private String REMARK;
	    private String FWJDK_BG_REASON;
	    private String LHH;
	    private String EDITOR;
	    private String ECONOMIC_RANGE;
	    private String SERVICE_DATA_LIMIT;
	    private String BIZ_SERVICE_DATA_VALUE;
	    private String IS_BACKUPED;
	    private String CREATE_DATE;
	    private String UPDATE_DATE;
		public String getID() {
			return ID;
		}
		public void setID(String iD) {
			ID = iD;
		}
		public String getNAME() {
			return NAME;
		}
		public void setNAME(String nAME) {
			NAME = nAME;
		}
		public String getSIMPLE_NAME() {
			return SIMPLE_NAME;
		}
		public void setSIMPLE_NAME(String sIMPLE_NAME) {
			SIMPLE_NAME = sIMPLE_NAME;
		}
		public String getIS_LOCKED() {
			return IS_LOCKED;
		}
		public void setIS_LOCKED(String iS_LOCKED) {
			IS_LOCKED = iS_LOCKED;
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
		public String getFAX() {
			return FAX;
		}
		public void setFAX(String fAX) {
			FAX = fAX;
		}
		public String getZIP() {
			return ZIP;
		}
		public void setZIP(String zIP) {
			ZIP = zIP;
		}
		public String getLEGAL() {
			return LEGAL;
		}
		public void setLEGAL(String lEGAL) {
			LEGAL = lEGAL;
		}
		public String getLEGAL_IDCARD_TYPE() {
			return LEGAL_IDCARD_TYPE;
		}
		public void setLEGAL_IDCARD_TYPE(String lEGAL_IDCARD_TYPE) {
			LEGAL_IDCARD_TYPE = lEGAL_IDCARD_TYPE;
		}
		public String getLEGAL_IDCARD() {
			return LEGAL_IDCARD;
		}
		public void setLEGAL_IDCARD(String lEGAL_IDCARD) {
			LEGAL_IDCARD = lEGAL_IDCARD;
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
		public String getMANAGER_IDCARD() {
			return MANAGER_IDCARD;
		}
		public void setMANAGER_IDCARD(String mANAGER_IDCARD) {
			MANAGER_IDCARD = mANAGER_IDCARD;
		}
		public String getEMAIL() {
			return EMAIL;
		}
		public void setEMAIL(String eMAIL) {
			EMAIL = eMAIL;
		}
		public String getECONOMIC_TYPE() {
			return ECONOMIC_TYPE;
		}
		public void setECONOMIC_TYPE(String eCONOMIC_TYPE) {
			ECONOMIC_TYPE = eCONOMIC_TYPE;
		}
		public String getBELONG_ORG() {
			return BELONG_ORG;
		}
		public void setBELONG_ORG(String bELONG_ORG) {
			BELONG_ORG = bELONG_ORG;
		}
		public String getJB_PERSON() {
			return JB_PERSON;
		}
		public void setJB_PERSON(String jB_PERSON) {
			JB_PERSON = jB_PERSON;
		}
		public String getJB_PHONE() {
			return JB_PHONE;
		}
		public void setJB_PHONE(String jB_PHONE) {
			JB_PHONE = jB_PHONE;
		}
		public String getJB_MOBILE() {
			return JB_MOBILE;
		}
		public void setJB_MOBILE(String jB_MOBILE) {
			JB_MOBILE = jB_MOBILE;
		}
		public String getHANDLE_WITHOUT_VALIDATE() {
			return HANDLE_WITHOUT_VALIDATE;
		}
		public void setHANDLE_WITHOUT_VALIDATE(String hANDLE_WITHOUT_VALIDATE) {
			HANDLE_WITHOUT_VALIDATE = hANDLE_WITHOUT_VALIDATE;
		}
		public String getOFFICE_ADDR() {
			return OFFICE_ADDR;
		}
		public void setOFFICE_ADDR(String oFFICE_ADDR) {
			OFFICE_ADDR = oFFICE_ADDR;
		}
		public String getEDIT_TIME() {
			return EDIT_TIME;
		}
		public void setEDIT_TIME(String eDIT_TIME) {
			EDIT_TIME = eDIT_TIME;
		}
		public String getBUSINESS_LICENSE_NUMBER() {
			return BUSINESS_LICENSE_NUMBER;
		}
		public void setBUSINESS_LICENSE_NUMBER(String bUSINESS_LICENSE_NUMBER) {
			BUSINESS_LICENSE_NUMBER = bUSINESS_LICENSE_NUMBER;
		}
		public String getSTATUS() {
			return STATUS;
		}
		public void setSTATUS(String sTATUS) {
			STATUS = sTATUS;
		}
		public String getFIRST_TIME() {
			return FIRST_TIME;
		}
		public void setFIRST_TIME(String fIRST_TIME) {
			FIRST_TIME = fIRST_TIME;
		}
		public String getOLD_YHID() {
			return OLD_YHID;
		}
		public void setOLD_YHID(String oLD_YHID) {
			OLD_YHID = oLD_YHID;
		}
		public String getSTART_DATE() {
			return START_DATE;
		}
		public void setSTART_DATE(String sTART_DATE) {
			START_DATE = sTART_DATE;
		}
		public String getEND_DATE() {
			return END_DATE;
		}
		public void setEND_DATE(String eND_DATE) {
			END_DATE = eND_DATE;
		}
		public String getREGISTERED_CAPITAL() {
			return REGISTERED_CAPITAL;
		}
		public void setREGISTERED_CAPITAL(String rEGISTERED_CAPITAL) {
			REGISTERED_CAPITAL = rEGISTERED_CAPITAL;
		}
		public String getORGANIZATION_CODE() {
			return ORGANIZATION_CODE;
		}
		public void setORGANIZATION_CODE(String oRGANIZATION_CODE) {
			ORGANIZATION_CODE = oRGANIZATION_CODE;
		}
		public String getOFFICE_ORG() {
			return OFFICE_ORG;
		}
		public void setOFFICE_ORG(String oFFICE_ORG) {
			OFFICE_ORG = oFFICE_ORG;
		}
		public String getCONTACT_NUMBER() {
			return CONTACT_NUMBER;
		}
		public void setCONTACT_NUMBER(String cONTACT_NUMBER) {
			CONTACT_NUMBER = cONTACT_NUMBER;
		}
		public String getCONTACT() {
			return CONTACT;
		}
		public void setCONTACT(String cONTACT) {
			CONTACT = cONTACT;
		}
		public String getPREPARE_DRIVER_NUM() {
			return PREPARE_DRIVER_NUM;
		}
		public void setPREPARE_DRIVER_NUM(String pREPARE_DRIVER_NUM) {
			PREPARE_DRIVER_NUM = pREPARE_DRIVER_NUM;
		}
		public String getCAR_QUOTA() {
			return CAR_QUOTA;
		}
		public void setCAR_QUOTA(String cAR_QUOTA) {
			CAR_QUOTA = cAR_QUOTA;
		}
		public String getCAR_NUMBER() {
			return CAR_NUMBER;
		}
		public void setCAR_NUMBER(String cAR_NUMBER) {
			CAR_NUMBER = cAR_NUMBER;
		}
		public String getTAX_ID() {
			return TAX_ID;
		}
		public void setTAX_ID(String tAX_ID) {
			TAX_ID = tAX_ID;
		}
		public String getACTUAL_PREPARE_RATE() {
			return ACTUAL_PREPARE_RATE;
		}
		public void setACTUAL_PREPARE_RATE(String aCTUAL_PREPARE_RATE) {
			ACTUAL_PREPARE_RATE = aCTUAL_PREPARE_RATE;
		}
		public String getDRIVER_NUMBER() {
			return DRIVER_NUMBER;
		}
		public void setDRIVER_NUMBER(String dRIVER_NUMBER) {
			DRIVER_NUMBER = dRIVER_NUMBER;
		}
		public String getPREPARE_RATE_QUOTA() {
			return PREPARE_RATE_QUOTA;
		}
		public void setPREPARE_RATE_QUOTA(String pREPARE_RATE_QUOTA) {
			PREPARE_RATE_QUOTA = pREPARE_RATE_QUOTA;
		}
		public String getDOUBLE_RATE_QUOTA() {
			return DOUBLE_RATE_QUOTA;
		}
		public void setDOUBLE_RATE_QUOTA(String dOUBLE_RATE_QUOTA) {
			DOUBLE_RATE_QUOTA = dOUBLE_RATE_QUOTA;
		}
		public String getACTUAL_DOUBLE_RATE() {
			return ACTUAL_DOUBLE_RATE;
		}
		public void setACTUAL_DOUBLE_RATE(String aCTUAL_DOUBLE_RATE) {
			ACTUAL_DOUBLE_RATE = aCTUAL_DOUBLE_RATE;
		}
		public String getEDIT_END() {
			return EDIT_END;
		}
		public void setEDIT_END(String eDIT_END) {
			EDIT_END = eDIT_END;
		}
		public String getGT_CZJCH_NAME() {
			return GT_CZJCH_NAME;
		}
		public void setGT_CZJCH_NAME(String gT_CZJCH_NAME) {
			GT_CZJCH_NAME = gT_CZJCH_NAME;
		}
		public String getYH_TYPE() {
			return YH_TYPE;
		}
		public void setYH_TYPE(String yH_TYPE) {
			YH_TYPE = yH_TYPE;
		}
		public String getLH_DATE() {
			return LH_DATE;
		}
		public void setLH_DATE(String lH_DATE) {
			LH_DATE = lH_DATE;
		}
		public String getLH_REASON() {
			return LH_REASON;
		}
		public void setLH_REASON(String lH_REASON) {
			LH_REASON = lH_REASON;
		}
		public String getREMARK() {
			return REMARK;
		}
		public void setREMARK(String rEMARK) {
			REMARK = rEMARK;
		}
		public String getFWJDK_BG_REASON() {
			return FWJDK_BG_REASON;
		}
		public void setFWJDK_BG_REASON(String fWJDK_BG_REASON) {
			FWJDK_BG_REASON = fWJDK_BG_REASON;
		}
		public String getLHH() {
			return LHH;
		}
		public void setLHH(String lHH) {
			LHH = lHH;
		}
		public String getEDITOR() {
			return EDITOR;
		}
		public void setEDITOR(String eDITOR) {
			EDITOR = eDITOR;
		}
		public String getECONOMIC_RANGE() {
			return ECONOMIC_RANGE;
		}
		public void setECONOMIC_RANGE(String eCONOMIC_RANGE) {
			ECONOMIC_RANGE = eCONOMIC_RANGE;
		}
		public String getSERVICE_DATA_LIMIT() {
			return SERVICE_DATA_LIMIT;
		}
		public void setSERVICE_DATA_LIMIT(String sERVICE_DATA_LIMIT) {
			SERVICE_DATA_LIMIT = sERVICE_DATA_LIMIT;
		}
		public String getBIZ_SERVICE_DATA_VALUE() {
			return BIZ_SERVICE_DATA_VALUE;
		}
		public void setBIZ_SERVICE_DATA_VALUE(String bIZ_SERVICE_DATA_VALUE) {
			BIZ_SERVICE_DATA_VALUE = bIZ_SERVICE_DATA_VALUE;
		}
		public String getIS_BACKUPED() {
			return IS_BACKUPED;
		}
		public void setIS_BACKUPED(String iS_BACKUPED) {
			IS_BACKUPED = iS_BACKUPED;
		}
		public String getCREATE_DATE() {
			return CREATE_DATE;
		}
		public void setCREATE_DATE(String cREATE_DATE) {
			CREATE_DATE = cREATE_DATE;
		}
		public String getUPDATE_DATE() {
			return UPDATE_DATE;
		}
		public void setUPDATE_DATE(String uPDATE_DATE) {
			UPDATE_DATE = uPDATE_DATE;
		}
		

    }
    
    public class Rows{
    	private String JCBH;
	    private String SFZH;
	    private String JCR1;
	    private String JCR2;
	    private String AJZT;
	    private String LBMC;
	    private String LBMS;
	    private String JCSJ;
		public String getJCBH() {
			return JCBH;
		}
		public void setJCBH(String jCBH) {
			JCBH = jCBH;
		}
		public String getSFZH() {
			return SFZH;
		}
		public void setSFZH(String sFZH) {
			SFZH = sFZH;
		}
		public String getJCR1() {
			return JCR1;
		}
		public void setJCR1(String jCR1) {
			JCR1 = jCR1;
		}
		public String getJCR2() {
			return JCR2;
		}
		public void setJCR2(String jCR2) {
			JCR2 = jCR2;
		}
		public String getAJZT() {
			return AJZT;
		}
		public void setAJZT(String aJZT) {
			AJZT = aJZT;
		}
		public String getLBMC() {
			return LBMC;
		}
		public void setLBMC(String lBMC) {
			LBMC = lBMC;
		}
		public String getLBMS() {
			return LBMS;
		}
		public void setLBMS(String lBMS) {
			LBMS = lBMS;
		}
		public String getJCSJ() {
			return JCSJ;
		}
		public void setJCSJ(String jCSJ) {
			JCSJ = jCSJ;
		}
	    
    }
}
