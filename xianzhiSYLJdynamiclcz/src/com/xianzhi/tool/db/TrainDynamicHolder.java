package com.xianzhi.tool.db;

public class TrainDynamicHolder {
	private int id;
	private int train_id;
	private String STATION_NAME;
	private String STATION_NO;
	private String TIME_ARRIVAL;
	private String TIME_ARRIVAL_TD;
	private String TIME_DEPART;
	private String TIME_DEPART_TD;
	private int CUR;
	private int ON_TIME;
	private int PASSED;
	private int PRE_PASSED;
	private int NEXT_PASSED;
	public TrainDynamicHolder(int id,int train_id, String sTATION_NAME,
			String sTATION_NO, String tIME_ARRIVAL, String tIME_ARRIVAL_TD,
			String tIME_DEPART, String tIME_DEPART_TD, int cUR, int oN_TIME,
			int pASSED, int pRE_PASSED, int nEXT_PASSED) {
		super();
		this.id=id;
		this.train_id = train_id;
		STATION_NAME = sTATION_NAME;
		STATION_NO = sTATION_NO;
		TIME_ARRIVAL = tIME_ARRIVAL;
		TIME_ARRIVAL_TD = tIME_ARRIVAL_TD;
		TIME_DEPART = tIME_DEPART;
		TIME_DEPART_TD = tIME_DEPART_TD;
		CUR = cUR;
		ON_TIME = oN_TIME;
		PASSED = pASSED;
		PRE_PASSED = pRE_PASSED;
		NEXT_PASSED = nEXT_PASSED;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrain_id() {
		return train_id;
	}
	public void setTrain_id(int train_id) {
		this.train_id = train_id;
	}
	public String getSTATION_NAME() {
		return STATION_NAME;
	}
	public void setSTATION_NAME(String sTATION_NAME) {
		STATION_NAME = sTATION_NAME;
	}
	public String getSTATION_NO() {
		return STATION_NO;
	}
	public void setSTATION_NO(String sTATION_NO) {
		STATION_NO = sTATION_NO;
	}
	public String getTIME_ARRIVAL() {
		return TIME_ARRIVAL;
	}
	public void setTIME_ARRIVAL(String tIME_ARRIVAL) {
		TIME_ARRIVAL = tIME_ARRIVAL;
	}
	public String getTIME_ARRIVAL_TD() {
		return TIME_ARRIVAL_TD;
	}
	public void setTIME_ARRIVAL_TD(String tIME_ARRIVAL_TD) {
		TIME_ARRIVAL_TD = tIME_ARRIVAL_TD;
	}
	public String getTIME_DEPART() {
		return TIME_DEPART;
	}
	public void setTIME_DEPART(String tIME_DEPART) {
		TIME_DEPART = tIME_DEPART;
	}
	public String getTIME_DEPART_TD() {
		return TIME_DEPART_TD;
	}
	public void setTIME_DEPART_TD(String tIME_DEPART_TD) {
		TIME_DEPART_TD = tIME_DEPART_TD;
	}
	public int getCUR() {
		return CUR;
	}
	public void setCUR(int cUR) {
		CUR = cUR;
	}
	public int getON_TIME() {
		return ON_TIME;
	}
	public void setON_TIME(int oN_TIME) {
		ON_TIME = oN_TIME;
	}
	public int getPASSED() {
		return PASSED;
	}
	public void setPASSED(int pASSED) {
		PASSED = pASSED;
	}
	public int getPRE_PASSED() {
		return PRE_PASSED;
	}
	public void setPRE_PASSED(int pRE_PASSED) {
		PRE_PASSED = pRE_PASSED;
	}
	public int getNEXT_PASSED() {
		return NEXT_PASSED;
	}
	public void setNEXT_PASSED(int nEXT_PASSED) {
		NEXT_PASSED = nEXT_PASSED;
	}
	
}
