package com.xianzhi.tool.db;

public class GroupHolder {
	private int id;
	private int crew_group_id;
	private String train_code;
	private String passenger_num;
	private String name;
	private String train_type;
	private String crew_em_ids;
	public GroupHolder(int id, int crew_group_id, String train_code,
			String passenger_num, String name, String train_type,String crew_em_ids) {
		super();
		this.id = id;
		this.crew_group_id = crew_group_id;
		this.train_code = train_code;
		this.passenger_num = passenger_num;
		this.name = name;
		this.train_type = train_type;
		this.crew_em_ids = crew_em_ids;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCrew_em_ids() {
		return crew_em_ids;
	}
	public void setCrew_em_ids(String crew_em_ids) {
		this.crew_em_ids = crew_em_ids;
	}
	public int getCrew_group_id() {
		return crew_group_id;
	}
	public void setCrew_group_id(int crew_group_id) {
		this.crew_group_id = crew_group_id;
	}
	public String getTrain_code() {
		return train_code;
	}
	public void setTrain_code(String train_code) {
		this.train_code = train_code;
	}
	public String getPassenger_num() {
		return passenger_num;
	}
	public void setPassenger_num(String passenger_num) {
		this.passenger_num = passenger_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTrain_type() {
		return train_type;
	}
	public void setTrain_type(String train_type) {
		this.train_type = train_type;
	}
	
}
