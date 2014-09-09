package com.xianzhi.tool.db;

public class PassengerHolder {
	private int id;
	private int dynamic_id;
	private int group_id;
	private String start_station;
	private String personal_code;
	private String arrive_station;
	private String tick_price;
	private String name;
	private String seat;
	
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDynamic_id() {
		return dynamic_id;
	}
	public void setDynamic_id(int dynamic_id) {
		this.dynamic_id = dynamic_id;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getStart_station() {
		return start_station;
	}
	public void setStart_station(String start_station) {
		this.start_station = start_station;
	}
	public String getPersonal_code() {
		return personal_code;
	}
	public void setPersonal_code(String personal_code) {
		this.personal_code = personal_code;
	}
	public String getArrive_station() {
		return arrive_station;
	}
	public void setArrive_station(String arrive_station) {
		this.arrive_station = arrive_station;
	}
	public String getTick_price() {
		return tick_price;
	}
	public void setTick_price(String tick_price) {
		this.tick_price = tick_price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PassengerHolder(int id, int dynamic_id, int group_id,
			String start_station, String personal_code, String arrive_station,
			String tick_price, String name,String seat) {
		super();
		this.id = id;
		this.dynamic_id = dynamic_id;
		this.group_id = group_id;
		this.start_station = start_station;
		this.personal_code = personal_code;
		this.arrive_station = arrive_station;
		this.tick_price = tick_price;
		this.name = name;
		this.seat = seat;
	}
	
}
