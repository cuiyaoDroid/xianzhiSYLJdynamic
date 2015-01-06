package com.xianzhi.tool.db;

public class ReviewHolder {
	private int id;
	private int train_id;
	private int user_id;
	private String user_name;
	private String user_phone;
	private String position_name;
	private String role_names;
	private String message;
	private int msg_status;
	private int msg_type;
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
	public String getPosition_name() {
		return position_name;
	}
	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}
	public String getRole_names() {
		return role_names;
	}
	public void setRole_names(String role_names) {
		this.role_names = role_names;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getMsg_status() {
		return msg_status;
	}
	public void setMsg_status(int msg_status) {
		this.msg_status = msg_status;
	}
	public int getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(int msg_type) {
		this.msg_type = msg_type;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public ReviewHolder(int id, int train_id, int user_id, String user_name,
			String user_phone, String position_name, String role_names,
			String message, int msg_status, int msg_type, long create_time) {
		super();
		this.id = id;
		this.train_id = train_id;
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_phone = user_phone;
		this.position_name = position_name;
		this.role_names = role_names;
		this.message = message;
		this.msg_status = msg_status;
		this.msg_type = msg_type;
		this.create_time = create_time;
	}

}
