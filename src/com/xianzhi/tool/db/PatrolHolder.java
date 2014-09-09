package com.xianzhi.tool.db;

import java.io.Serializable;

public class PatrolHolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 137755325896155008L;
	private int id;
	private String content;
	private String img;
	private String videoData;
	private long completeTime;
	private String toUserIds;
	private long creatTime;
	private String userName;
	private String responsiblePersonName;
	private String result;
	private int userId;
	private int typeId;
	private String type_name;
	private int result_typeId;
	private String result_typeId_name;	
	@Override
	public String toString() {
		return "PatrolHolder [id=" + id + ", content=" + content + ", img="
				+ img + ", videoData=" + videoData + ", completeTime="
				+ completeTime + ", toUserIds=" + toUserIds + ", creatTime="
				+ creatTime + ", userName=" + userName
				+ ", responsiblePersonName=" + responsiblePersonName
				+ ", result=" + result + "]";
	}
	
	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public int getResult_typeId() {
		return result_typeId;
	}

	public void setResult_typeId(int result_typeId) {
		this.result_typeId = result_typeId;
	}

	public String getResult_typeId_name() {
		return result_typeId_name;
	}

	public void setResult_typeId_name(String result_typeId_name) {
		this.result_typeId_name = result_typeId_name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getVideoData() {
		return videoData;
	}
	public void setVideoData(String videoData) {
		this.videoData = videoData;
	}
	public long getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(long completeTime) {
		this.completeTime = completeTime;
	}
	public String getToUserIds() {
		return toUserIds;
	}
	public void setToUserIds(String toUserIds) {
		this.toUserIds = toUserIds;
	}
	public long getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getResponsiblePersonName() {
		return responsiblePersonName;
	}
	public void setResponsiblePersonName(String responsiblePersonName) {
		this.responsiblePersonName = responsiblePersonName;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public PatrolHolder(int id, String content, String img, String videoData,
			long completeTime, String toUserIds, long creatTime,
			String userName, String responsiblePersonName, String result,
			int userId, int typeId, String type_name, int result_typeId,
			String result_typeId_name) {
		super();
		this.id = id;
		this.content = content;
		this.img = img;
		this.videoData = videoData;
		this.completeTime = completeTime;
		this.toUserIds = toUserIds;
		this.creatTime = creatTime;
		this.userName = userName;
		this.responsiblePersonName = responsiblePersonName;
		this.result = result;
		this.userId = userId;
		this.typeId = typeId;
		this.type_name = type_name;
		this.result_typeId = result_typeId;
		this.result_typeId_name = result_typeId_name;
	}
	
}
