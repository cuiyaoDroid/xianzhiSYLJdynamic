package com.xianzhi.tool.db;

public class EmployeesHolder {
	private int id;
	private int crew_group_id;
	private int group_id;
	private String head_img;
	private String personal_code;
	private String phone_num;
	private String position;
	private String name;
	private int type;
	public static final int TYPE_1=1;
	public static final int TYPE_2=2;
	public static final int TYPE_3=3;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCrew_group_id() {
		return crew_group_id;
	}
	public void setCrew_group_id(int crew_group_id) {
		this.crew_group_id = crew_group_id;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public String getPersonal_code() {
		return personal_code;
	}
	public void setPersonal_code(String personal_code) {
		this.personal_code = personal_code;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public EmployeesHolder(int id, int crew_group_id, int group_id,
			String head_img, String personal_code, String phone_num,
			String position, String name, int type) {
		super();
		this.id = id;
		this.crew_group_id = crew_group_id;
		this.group_id = group_id;
		this.head_img = head_img;
		this.personal_code = personal_code;
		this.phone_num = phone_num;
		this.position = position;
		this.name = name;
		this.type = type;
	}
	
}
