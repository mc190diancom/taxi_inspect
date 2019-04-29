package com.miu360.taxi_check.model;

import java.util.List;

public class InspectTongJi {
    private int total;
    
    private List<rows> rows;
    
    public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<rows> getRows() {
		return rows;
	}

	public void setRows(List<rows> rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "InspectTongJi [total=" + total + ", rows=" + rows + "]";
	}


	public class rows{
    	private int STAT0;
    	
    	private int STAT2;
    	
    	private int STAT3;
    	
    	private int STAT4;
    	
    	private int STAT5;
    	
    	private String ZFRY1;

    	private String ZFRY2;
    	
    	private String ZFDWMC;
    	
    	private String SSCZ;
    	
    	private String SSZD;

		public int getSTAT0() {
			return STAT0;
		}

		public void setSTAT0(int sTAT0) {
			STAT0 = sTAT0;
		}

		public int getSTAT2() {
			return STAT2;
		}

		public void setSTAT2(int sTAT2) {
			STAT2 = sTAT2;
		}

		public int getSTAT3() {
			return STAT3;
		}

		public void setSTAT3(int sTAT3) {
			STAT3 = sTAT3;
		}

		public int getSTAT4() {
			return STAT4;
		}

		public void setSTAT4(int sTAT4) {
			STAT4 = sTAT4;
		}

		public int getSTAT5() {
			return STAT5;
		}

		public void setSTAT5(int sTAT5) {
			STAT5 = sTAT5;
		}

		public String getZFRY1() {
			return ZFRY1;
		}

		public void setZFRY1(String zFRY1) {
			ZFRY1 = zFRY1;
		}

		public String getZFRY2() {
			return ZFRY2;
		}

		public void setZFRY2(String zFRY2) {
			ZFRY2 = zFRY2;
		}

		public String getZFDWMC() {
			return ZFDWMC;
		}

		public void setZFDWMC(String zFDWMC) {
			ZFDWMC = zFDWMC;
		}

		public String getSSCZ() {
			return SSCZ;
		}

		public void setSSCZ(String sSCZ) {
			SSCZ = sSCZ;
		}

		public String getSSZD() {
			return SSZD;
		}

		public void setSSZD(String sSZD) {
			SSZD = sSZD;
		}

		@Override
		public String toString() {
			return "rows [STAT0=" + STAT0 + ", STAT2=" + STAT2 + ", STAT3="
					+ STAT3 + ", STAT4=" + STAT4 + ", STAT5=" + STAT5
					+ ", ZFRY1=" + ZFRY1 + ", ZFRY2=" + ZFRY2 + ", ZFDWMC="
					+ ZFDWMC + ", SSCZ=" + SSCZ + ", SSZD=" + SSZD + "]";
		}
    	
    	
    }
}
