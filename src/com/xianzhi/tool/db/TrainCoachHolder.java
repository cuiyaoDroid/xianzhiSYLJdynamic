package com.xianzhi.tool.db;

public class TrainCoachHolder {
	private String coach_no;
	private String train_no;
	private String coach_type;
	private int limit1;
	private String trainDate;
	private String trainCode;
	private int train_id;

	public TrainCoachHolder(String coach_no, String train_no,
			String coach_type, int limit1, String trainDate, String trainCode,
			int train_id) {
		super();
		this.coach_no = coach_no;
		this.train_no = train_no;
		this.coach_type = coach_type;
		this.limit1 = limit1;
		this.trainDate = trainDate;
		this.trainCode = trainCode;
		this.train_id = train_id;
	}

	public int getTrain_id() {
		return train_id;
	}

	public void setTrain_id(int train_id) {
		this.train_id = train_id;
	}

	public String getCoach_no() {
		return coach_no;
	}

	public void setCoach_no(String coach_no) {
		this.coach_no = coach_no;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}

	public String getCoach_type() {
		return coach_type;
	}

	public void setCoach_type(String coach_type) {
		this.coach_type = coach_type;
	}

	public int getLimit1() {
		return limit1;
	}

	public void setLimit1(int limit1) {
		this.limit1 = limit1;
	}

	public String getTrainDate() {
		return trainDate;
	}

	public void setTrainDate(String trainDate) {
		this.trainDate = trainDate;
	}

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

}
