package com.miu30.common.data;

import android.content.Context;

import com.miu30.common.base.XPreference;
import com.miu30.common.config.Config;

public class FilePreference extends XPreference {
	private final static String SP_NAME = "template_file";

	private final static int DEFAULT_VERSION = 1;

	public FilePreference(Context mContext) {
		super(SP_NAME, mContext);
	}

	public int getLIVERECORD() {
		return getInt(Config.LIVERECORD, DEFAULT_VERSION);
	}

	public void setLIVERECORD(int version) {
		setInt(Config.LIVERECORD, version);
	}

	public int getTALKNOTICE() {
		return getInt(Config.TALKNOTICE, DEFAULT_VERSION);
	}

	public void setTALKNOTICE(int version) {
		setInt(Config.TALKNOTICE, version);
	}

	public int getFRISTREGISTER() {
		return getInt(Config.FRISTREGISTER, DEFAULT_VERSION);
	}

	public void setFRISTREGISTER(int version) {
		setInt(Config.FRISTREGISTER, version);
	}

	public int getCARDECIDE() {
		return getInt(Config.CARDECIDE, DEFAULT_VERSION);
	}

	public void setCARDECIDE(int version) {
		setInt(Config.CARDECIDE, version);
	}

	public int getLIVETRANSCRIPT() {
		return getInt(Config.LIVETRANSCRIPT, DEFAULT_VERSION);
	}

	public void setLIVETRANSCRIPT(int version) {
		setInt(Config.LIVETRANSCRIPT, version);
	}

	public int getADMINISTRATIVE() {
		return getInt(Config.ADMINISTRATIVE, DEFAULT_VERSION);
	}

	public void setADMINISTRATIVE(int version) {
		setInt(Config.ADMINISTRATIVE, version);
	}

	public int getCARFORM() {
		return getInt(Config.CARFORM, DEFAULT_VERSION);
	}

	public void setCARFORM(int version) {
		setInt(Config.CARFORM, version);
	}
}
