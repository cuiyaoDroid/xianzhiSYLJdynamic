package com.xianzhi.tool.db;

import java.io.Serializable;
import java.util.Map;

public class MapHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9032951593683504134L;
	private Map<Integer,String> users;
	public Map<Integer, String> getUsers() {
		return users;
	}
	public void setUsers(Map<Integer, String> users) {
		this.users = users;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public MapHolder(Map<Integer, String> users) {
		super();
		this.users = users;
	}
	
}
