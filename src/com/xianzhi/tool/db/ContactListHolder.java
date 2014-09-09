package com.xianzhi.tool.db;

public class ContactListHolder {
	private int id;
	private String name;
	private String position;
	private int deptId;
	private int sort;
	
	public ContactListHolder(int id, String name, String position, int deptId,int sort) {
		super();
		this.id = id;
		this.name = name;
		this.position = position;
		this.deptId = deptId;
		this.sort=sort;
	}
	
	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
