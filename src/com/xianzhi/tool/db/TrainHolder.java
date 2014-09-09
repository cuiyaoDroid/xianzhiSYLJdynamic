package com.xianzhi.tool.db;

public class TrainHolder {
	private String place_group;
	private String time_group;
	private String name;
	private String hole_time;
	private String type;
	public TrainHolder(String place_group, String time_group,
			String name, String hole_time, String type) {
		super();
		this.place_group = place_group;
		this.time_group = time_group;
		this.name = name;
		this.hole_time = hole_time;
		this.type = type;
	}
	public String getPlace_group() {
		return place_group;
	}
	public void setPlace_group(String place_group) {
		this.place_group = place_group;
	}
	public String getTime_group() {
		return time_group;
	}
	public void setTime_group(String time_group) {
		this.time_group = time_group;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHole_time() {
		return hole_time;
	}
	public void setHole_time(String hole_time) {
		this.hole_time = hole_time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
