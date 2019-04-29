package com.miu360.taxi_check.model;

public class ZhiFaJianChaModel {
	// "STARTDATE":"2017-04-07 11:23:20",打开新增表的时间
	// "ENDDATE":"2017-04-07 12:23:20",提交表的时间
	// "PERSON_ID":"",执法人员信息表ID（不填）
	// "ORGANIZATION_ID":"",必填 机构ID
	// "PERSON_CODE":"0000000",必填 执法证号，填 登录账号
	// "IS_DELETE":"",删除标志
	// "PERSON_NAME":"",必填 执法人员姓名
	// "MODULE_NAME":"",必填 模块名称
	// "CHECK_ITEM_NAME":"",必填 事项名称
	// "IS_STANDARD":0,必填 是否合格
	// "MODULE_ID":"",模块ID
	// "CHECK_ITEM_ID":"",事项ID
	// "IS_ENABLE":1,必填 是否启用
	// "CHECK_LIST_NAME":"",必填 检查单名称
	// "CREATE_USER_CODE":"",
	// "CREATE_TIME":"",必填 创建时间
	// "CREATE_USER_ID":"",必填 创建人ID
	// "CREATE_USER_NAME":"",必填 创建人姓名
	// "PUNISHMENT_TYPE":"",行政处罚类型
	// "IS_SUBMIT":"",是否提交
	// "REVIEW_RESULT_EXPLAIN":"",复查结果说明
	// "REVIEW_RESULT":"",复查结果说明
	// "REVIEW_DATE":"",复核日期
	// "DEAL_STATE":"",必填 状态办理
	// "CHECK_PLACE":"",必填 检查地点
	// "PEOPLE_TYPE":"",必填 当事人类型
	// "CITIZEN_NAME":"",公民姓名
	// "CITIZEN_PHONE":"",公民联系方式
	// "UNIT_NAME":"",单位名称
	// "LEGAL_PERSON":"",法定代表人
	// "LEGAL_PERSON_PHONE":"", 法人联系方式
	// "ORGANIZATION_NAME":"",组织机构名称
	// "ORGANIZATION_PERSON":"",组织机构责任人
	// "ORGANIZATION_PHONE":"",组织机构联系方式
	// "CHECK_RESULT":"",检查结果
	// "CHECK_EXPLAIN":"",检查说明
	// "IS_CHARGE_REFORM":"",必填 是否责令整改
	// "CHARGE_REFORM":"",责令整改
	// "REFORM_RESULT":"",整改结果
	// "REFORM_START_DATE":"",整改起始日期
	// "REFORM_END_DATE":"",整改截止日期
	// "IS_PUNISHMENT":""否行政处罚
	// VNAME VARCHAR2(32 BYTE) Yes 36 车牌号
	// SFZH VARCHAR2(32 BYTE) Yes 37 身份证号
	//
	// 奇华 李文豪 2017/4/18 星期二 9:11:35
	// ADDRESS VARCHAR2(155 BYTE) No 1 地址

	private String VNAME;
	private String SFZH;
	private String ADDRESS;
	private String HYLB;

	private String STARTDATE;

	private String ENDDATE;

	private String PERSON_ID;

	private String ORGANIZATION_ID;

	private String PERSON_CODE;

	private String IS_DELETE;

	private String PERSON_NAME;

	private String MODULE_NAME;

	private String CHECK_ITEM_NAME;

	private int IS_STANDARD;

	private String MODULE_ID;

	private String CHECK_ITEM_ID;

	private int IS_ENABLE;

	private String CHECK_LIST_NAME;

	private String CREATE_USER_CODE;

	private String CREATE_TIME;

	private String CREATE_USER_ID;

	private String CREATE_USER_NAME;

	private String PUNISHMENT_TYPE;

	private String IS_SUBMIT;

	private String REVIEW_RESULT_EXPLAIN;

	private String REVIEW_RESULT;

	private String REVIEW_DATE;

	private String DEAL_STATE;

	private String CHECK_PLACE;

	private String PEOPLE_TYPE;

	private String CITIZEN_NAME;

	private String CITIZEN_PHONE;

	private String UNIT_NAME;

	private String LEGAL_PERSON;

	private String LEGAL_PERSON_PHONE;

	private String ORGANIZATION_NAME;

	private String ORGANIZATION_PERSON;

	private String ORGANIZATION_PHONE;

	private String CHECK_RESULT;

	private String CHECK_EXPLAIN;

	private String IS_CHARGE_REFORM;

	private String CHARGE_REFORM;

	private String REFORM_RESULT;

	private String REFORM_START_DATE;

	private String REFORM_END_DATE;

	private String IS_PUNISHMENT;

	private String ZFZH2;

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public void setSFZH(String sFZH) {
		SFZH = sFZH;
	}

	public void setVNAME(String vNAME) {
		VNAME = vNAME;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public String getSFZH() {
		return SFZH;
	}

	public String getVNAME() {
		return VNAME;
	}

	public void setHYLB(String HYLB) {
		this.HYLB = HYLB;
	}

	public String getHYLB() {
		return this.HYLB;
	}

	public void setSTARTDATE(String STARTDATE) {
		this.STARTDATE = STARTDATE;
	}

	public String getSTARTDATE() {
		return this.STARTDATE;
	}

	public void setENDDATE(String ENDDATE) {
		this.ENDDATE = ENDDATE;
	}

	public String getENDDATE() {
		return this.ENDDATE;
	}

	public void setPERSON_ID(String PERSON_ID) {
		this.PERSON_ID = PERSON_ID;
	}

	public String getPERSON_ID() {
		return this.PERSON_ID;
	}

	public void setORGANIZATION_ID(String ORGANIZATION_ID) {
		this.ORGANIZATION_ID = ORGANIZATION_ID;
	}

	public String getORGANIZATION_ID() {
		return this.ORGANIZATION_ID;
	}

	public void setPERSON_CODE(String PERSON_CODE) {
		this.PERSON_CODE = PERSON_CODE;
	}

	public String getPERSON_CODE() {
		return this.PERSON_CODE;
	}

	public void setIS_DELETE(String IS_DELETE) {
		this.IS_DELETE = IS_DELETE;
	}

	public String getIS_DELETE() {
		return this.IS_DELETE;
	}

	public void setPERSON_NAME(String PERSON_NAME) {
		this.PERSON_NAME = PERSON_NAME;
	}

	public String getPERSON_NAME() {
		return this.PERSON_NAME;
	}

	public void setMODULE_NAME(String MODULE_NAME) {
		this.MODULE_NAME = MODULE_NAME;
	}

	public String getMODULE_NAME() {
		return this.MODULE_NAME;
	}

	public void setCHECK_ITEM_NAME(String CHECK_ITEM_NAME) {
		this.CHECK_ITEM_NAME = CHECK_ITEM_NAME;
	}

	public String getCHECK_ITEM_NAME() {
		return this.CHECK_ITEM_NAME;
	}

	public void setIS_STANDARD(int IS_STANDARD) {
		this.IS_STANDARD = IS_STANDARD;
	}

	public int getIS_STANDARD() {
		return this.IS_STANDARD;
	}

	public void setMODULE_ID(String MODULE_ID) {
		this.MODULE_ID = MODULE_ID;
	}

	public String getMODULE_ID() {
		return this.MODULE_ID;
	}

	public void setCHECK_ITEM_ID(String CHECK_ITEM_ID) {
		this.CHECK_ITEM_ID = CHECK_ITEM_ID;
	}

	public String getCHECK_ITEM_ID() {
		return this.CHECK_ITEM_ID;
	}

	public void setIS_ENABLE(int IS_ENABLE) {
		this.IS_ENABLE = IS_ENABLE;
	}

	public int getIS_ENABLE() {
		return this.IS_ENABLE;
	}

	public void setCHECK_LIST_NAME(String CHECK_LIST_NAME) {
		this.CHECK_LIST_NAME = CHECK_LIST_NAME;
	}

	public String getCHECK_LIST_NAME() {
		return this.CHECK_LIST_NAME;
	}

	public void setCREATE_USER_CODE(String CREATE_USER_CODE) {
		this.CREATE_USER_CODE = CREATE_USER_CODE;
	}

	public String getCREATE_USER_CODE() {
		return this.CREATE_USER_CODE;
	}

	public void setCREATE_TIME(String CREATE_TIME) {
		this.CREATE_TIME = CREATE_TIME;
	}

	public String getCREATE_TIME() {
		return this.CREATE_TIME;
	}

	public void setCREATE_USER_ID(String CREATE_USER_ID) {
		this.CREATE_USER_ID = CREATE_USER_ID;
	}

	public String getCREATE_USER_ID() {
		return this.CREATE_USER_ID;
	}

	public void setCREATE_USER_NAME(String CREATE_USER_NAME) {
		this.CREATE_USER_NAME = CREATE_USER_NAME;
	}

	public String getCREATE_USER_NAME() {
		return this.CREATE_USER_NAME;
	}

	public void setPUNISHMENT_TYPE(String PUNISHMENT_TYPE) {
		this.PUNISHMENT_TYPE = PUNISHMENT_TYPE;
	}

	public String getPUNISHMENT_TYPE() {
		return this.PUNISHMENT_TYPE;
	}

	public void setIS_SUBMIT(String IS_SUBMIT) {
		this.IS_SUBMIT = IS_SUBMIT;
	}

	public String getIS_SUBMIT() {
		return this.IS_SUBMIT;
	}

	public void setREVIEW_RESULT_EXPLAIN(String REVIEW_RESULT_EXPLAIN) {
		this.REVIEW_RESULT_EXPLAIN = REVIEW_RESULT_EXPLAIN;
	}

	public String getREVIEW_RESULT_EXPLAIN() {
		return this.REVIEW_RESULT_EXPLAIN;
	}

	public void setREVIEW_RESULT(String REVIEW_RESULT) {
		this.REVIEW_RESULT = REVIEW_RESULT;
	}

	public String getREVIEW_RESULT() {
		return this.REVIEW_RESULT;
	}

	public void setREVIEW_DATE(String REVIEW_DATE) {
		this.REVIEW_DATE = REVIEW_DATE;
	}

	public String getREVIEW_DATE() {
		return this.REVIEW_DATE;
	}

	public void setDEAL_STATE(String DEAL_STATE) {
		this.DEAL_STATE = DEAL_STATE;
	}

	public String getDEAL_STATE() {
		return this.DEAL_STATE;
	}

	public void setCHECK_PLACE(String CHECK_PLACE) {
		this.CHECK_PLACE = CHECK_PLACE;
	}

	public String getCHECK_PLACE() {
		return this.CHECK_PLACE;
	}

	public void setPEOPLE_TYPE(String PEOPLE_TYPE) {
		this.PEOPLE_TYPE = PEOPLE_TYPE;
	}

	public String getPEOPLE_TYPE() {
		return this.PEOPLE_TYPE;
	}

	public void setCITIZEN_NAME(String CITIZEN_NAME) {
		this.CITIZEN_NAME = CITIZEN_NAME;
	}

	public String getCITIZEN_NAME() {
		return this.CITIZEN_NAME;
	}

	public void setCITIZEN_PHONE(String CITIZEN_PHONE) {
		this.CITIZEN_PHONE = CITIZEN_PHONE;
	}

	public String getCITIZEN_PHONE() {
		return this.CITIZEN_PHONE;
	}

	public void setUNIT_NAME(String UNIT_NAME) {
		this.UNIT_NAME = UNIT_NAME;
	}

	public String getUNIT_NAME() {
		return this.UNIT_NAME;
	}

	public void setLEGAL_PERSON(String LEGAL_PERSON) {
		this.LEGAL_PERSON = LEGAL_PERSON;
	}

	public String getLEGAL_PERSON() {
		return this.LEGAL_PERSON;
	}

	public void setLEGAL_PERSON_PHONE(String LEGAL_PERSON_PHONE) {
		this.LEGAL_PERSON_PHONE = LEGAL_PERSON_PHONE;
	}

	public String getLEGAL_PERSON_PHONE() {
		return this.LEGAL_PERSON_PHONE;
	}

	public void setORGANIZATION_NAME(String ORGANIZATION_NAME) {
		this.ORGANIZATION_NAME = ORGANIZATION_NAME;
	}

	public String getORGANIZATION_NAME() {
		return this.ORGANIZATION_NAME;
	}

	public void setORGANIZATION_PERSON(String ORGANIZATION_PERSON) {
		this.ORGANIZATION_PERSON = ORGANIZATION_PERSON;
	}

	public String getORGANIZATION_PERSON() {
		return this.ORGANIZATION_PERSON;
	}

	public void setORGANIZATION_PHONE(String ORGANIZATION_PHONE) {
		this.ORGANIZATION_PHONE = ORGANIZATION_PHONE;
	}

	public String getORGANIZATION_PHONE() {
		return this.ORGANIZATION_PHONE;
	}

	public void setCHECK_RESULT(String CHECK_RESULT) {
		this.CHECK_RESULT = CHECK_RESULT;
	}

	public String getCHECK_RESULT() {
		return this.CHECK_RESULT;
	}

	public void setCHECK_EXPLAIN(String CHECK_EXPLAIN) {
		this.CHECK_EXPLAIN = CHECK_EXPLAIN;
	}

	public String getCHECK_EXPLAIN() {
		return this.CHECK_EXPLAIN;
	}

	public void setIS_CHARGE_REFORM(String IS_CHARGE_REFORM) {
		this.IS_CHARGE_REFORM = IS_CHARGE_REFORM;
	}

	public String getIS_CHARGE_REFORM() {
		return this.IS_CHARGE_REFORM;
	}

	public void setCHARGE_REFORM(String CHARGE_REFORM) {
		this.CHARGE_REFORM = CHARGE_REFORM;
	}

	public String getCHARGE_REFORM() {
		return this.CHARGE_REFORM;
	}

	public void setREFORM_RESULT(String REFORM_RESULT) {
		this.REFORM_RESULT = REFORM_RESULT;
	}

	public String getREFORM_RESULT() {
		return this.REFORM_RESULT;
	}

	public void setREFORM_START_DATE(String REFORM_START_DATE) {
		this.REFORM_START_DATE = REFORM_START_DATE;
	}

	public String getREFORM_START_DATE() {
		return this.REFORM_START_DATE;
	}

	public void setREFORM_END_DATE(String REFORM_END_DATE) {
		this.REFORM_END_DATE = REFORM_END_DATE;
	}

	public String getREFORM_END_DATE() {
		return this.REFORM_END_DATE;
	}

	public void setIS_PUNISHMENT(String IS_PUNISHMENT) {
		this.IS_PUNISHMENT = IS_PUNISHMENT;
	}

	public String getIS_PUNISHMENT() {
		return this.IS_PUNISHMENT;
	}

	public String getZFZH2() {
		return ZFZH2;
	}

	public void setZFZH2(String ZFZH2) {
		this.ZFZH2 = ZFZH2;
	}
}
