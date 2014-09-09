package com.xianzhi.tool.db;

public class ReviewHolder {
	private int id;
	private int train_id;
	private int user_id;
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
	public ReviewHolder(int id,int train_id, int user_id, String message, int status,
			int type, long create_time) {
		super();
		this.id=id;
		this.train_id = train_id;
		this.user_id = user_id;
		this.message = message;
		this.status = status;
		this.type = type;
		this.create_time = create_time;
	}
	

}
