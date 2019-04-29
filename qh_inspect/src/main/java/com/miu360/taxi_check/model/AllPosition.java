package com.miu360.taxi_check.model;

import java.io.Serializable;
import java.util.List;

public class AllPosition implements Serializable{
    private int total;
    private List<rows> rows;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		total = total;
	}
	public List<rows> getRows() {
		return rows;
	}
	public void setRows(List<rows> rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "AllPosition [total=" + total + ", rows=" + rows + "]";
	}
	
	public class rows implements Serializable{
		private String ACCOUNT;


		private double LON;


		private double LAT;


		private String PERSONNAME;


		private String SSDD;
		
		private String TIME;
		

		public String getTIME() {
			return TIME;
		}


		public void setTIME(String tIME) {
			TIME = tIME;
		}


		public String getACCOUNT() {
			return ACCOUNT;
		}


		public void setACCOUNT(String aCCOUNT) {
			ACCOUNT = aCCOUNT;
		}


		public double getLON() {
			return LON;
		}


		public void setLON(double lON) {
			LON = lON;
		}


		public double getLAT() {
			return LAT;
		}


		public void setLAT(double lAT) {
			LAT = lAT;
		}


		public String getPERSONNAME() {
			return PERSONNAME;
		}


		public void setPERSONNAME(String pERSONNAME) {
			PERSONNAME = pERSONNAME;
		}


		public String getSSDD() {
			return SSDD;
		}


		public void setSSDD(String sSDD) {
			SSDD = sSDD;
		}


		@Override
		public String toString() {
			return "rows [ACCOUNT=" + ACCOUNT + ", LON=" + LON + ", LAT=" + LAT + ", PERSONNAME=" + PERSONNAME
					+ ", SSDD=" + SSDD + "]";
		}

		
	}
    
}
