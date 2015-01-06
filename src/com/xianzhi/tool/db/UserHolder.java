package com.xianzhi.tool.db;

public class UserHolder {
	private int id;
	private String job_number;
	private String user_name;
	private String photo;
	private String phone;
	private int department_id;
	private String department_name;
	private int position_id;
	private String position_name;
	public UserHolder(int id, String job_number, String user_name,
			String photo, String phone, int department_id,
			String department_name, int position_id, String position_name) {
		super();
		this.id = id;
		this.job_number = job_number;
		this.user_name = user_name;
		this.photo = photo;
		this.phone = phone;
		this.department_id = department_id;
		this.department_name = department_name;
		this.position_id = position_id;
		this.position_name = position_name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJob_number() {
		return job_number;
	}
	public void setJob_number(String job_number) {
		this.job_number = job_number;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public int getPosition_id() {
		return position_id;
	}
	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}
	public String getPosition_name() {
		return position_name;
	}
	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}
	
}
