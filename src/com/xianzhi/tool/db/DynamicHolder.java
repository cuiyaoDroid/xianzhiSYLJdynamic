package com.xianzhi.tool.db;

import java.io.Serializable;

public class DynamicHolder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8371211362475915455L;
	private int id;
	private String train_number;
	private long start_time;
	private long arrive_time;
	private String start_position;
	private String arrive_position;
	private String watch_group;
	private String Interval_position;
	private String station;
	private int watch_group_id;
	public static final String daoda="到达";
	public static final String wandian="晚点";
	public static final String dafa="待发";
	public DynamicHolder(int id, String train_number, long start_time,
			long arrive_time, String start_position, String arrive_position,
			String watch_group, String interval_position, String station,int watch_group_id) {
		super();
		this.id = id;
		this.train_number = train_number;
		this.start_time = start_time;
		this.arrive_time = arrive_time;
		this.start_position = start_position;
		this.arrive_position = arrive_position;
		this.watch_group = watch_group;
		Interval_position = interval_position;
		this.station = station;
		this.watch_group_id=watch_group_id;
	}
	
	public int getWatch_group_id() {
		return watch_group_id;
	}

	public void setWatch_group_id(int watch_group_id) {
		this.watch_group_id = watch_group_id;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTrain_number() {
		return train_number;
	}
	public void setTrain_number(String train_number) {
		this.train_number = train_number;
	}
	public long getStart_time() {
		return start_time;
	}
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	public long getArrive_time() {
		return arrive_time;
	}
	public void setArrive_time(long arrive_time) {
		this.arrive_time = arrive_time;
	}
	public String getStart_position() {
		return start_position;
	}
	public void setStart_position(String start_position) {
		this.start_position = start_position;
	}
	public String getArrive_position() {
		return arrive_position;
	}
	public void setArrive_position(String arrive_position) {
		this.arrive_position = arrive_position;
	}
	public String getWatch_group() {
		return watch_group;
	}
	public void setWatch_group(String watch_group) {
		this.watch_group = watch_group;
	}
	public String getInterval_position() {
		return Interval_position;
	}
	public void setInterval_position(String interval_position) {
		Interval_position = interval_position;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
