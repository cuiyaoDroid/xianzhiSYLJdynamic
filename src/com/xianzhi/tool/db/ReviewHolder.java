package com.xianzhi.tool.db;

public class ReviewHolder {
	private int id;
	private int train_id;
	private int user_id;
	private String user_name;
	private String user_phone;
	private String position;
	private String message;
	private int status;
	private int type;
	private long create_time;
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
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public ReviewHolder(int id, int train_id, int user_id, String user_name,
			String user_phone,String position, String message, int status, int type,
			long create_time) {
		super();
		this.id = id;
		this.train_id = train_id;
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_phone = user_phone;
		this.position =position;
		this.message = message;
		this.status = status;
		this.type = type;
		this.create_time = create_time;
	}
	

}
