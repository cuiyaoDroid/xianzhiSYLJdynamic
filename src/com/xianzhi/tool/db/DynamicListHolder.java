package com.xianzhi.tool.db;

public class DynamicListHolder {
	private int id;
	private String board_train_code;
	private long start_time;
	private String from_station_name;
	private String to_station_name;
	private String current_team;
	private int driving_status;
	private String user_ids;
	private String job_number;
	private String user_name;
	private String photo;
	private String phone;
	private int department_id;
	private String department_name;
	private int position_id;
	private String position_name;
	private String team_length;
	private int isFinal;
	private int isRead;
	private String date;
	public DynamicListHolder(int id, String board_train_code, long start_time,
			String from_station_name, String to_station_name,
			String current_team, int driving_status, String user_ids,
			String job_number, String user_name, String photo, String phone,
			int department_id, String department_name, int position_id,
			String position_name, String team_length,int isFinal,int isRead,String date) {
		super();
		this.id = id;
		this.board_train_code = board_train_code;
		this.start_time = start_time;
		this.from_station_name = from_station_name;
		this.to_station_name = to_station_name;
		this.current_team = current_team;
		this.driving_status = driving_status;
		this.user_ids = user_ids;
		this.job_number = job_number;
		this.user_name = user_name;
		this.photo = photo;
		this.phone = phone;
		this.department_id = department_id;
		this.department_name = department_name;
		this.position_id = position_id;
		this.position_name = position_name;
		this.team_length = team_length;
		this.isFinal=isFinal;
		this.isRead=isRead;
		this.date=date;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getIsFinal() {
		return isFinal;
	}

	public void setIsFinal(int isFinal) {
		this.isFinal = isFinal;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBoard_train_code() {
		return board_train_code;
	}

	public void setBoard_train_code(String board_train_code) {
		this.board_train_code = board_train_code;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public String getFrom_station_name() {
		return from_station_name;
	}

	public void setFrom_station_name(String from_station_name) {
		this.from_station_name = from_station_name;
	}

	public String getTo_station_name() {
		return to_station_name;
	}

	public void setTo_station_name(String to_station_name) {
		this.to_station_name = to_station_name;
	}

	public String getCurrent_team() {
		return current_team;
	}

	public void setCurrent_team(String current_team) {
		this.current_team = current_team;
	}

	public int getDriving_status() {
		return driving_status;
	}

	public void setDriving_status(int driving_status) {
		this.driving_status = driving_status;
	}

	public String getUser_ids() {
		return user_ids;
	}

	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
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

	public String getTeam_length() {
		return team_length;
	}

	public void setTeam_length(String team_length) {
		this.team_length = team_length;
	}
}
