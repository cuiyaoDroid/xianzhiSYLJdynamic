package com.xianzhi.tool.db;

public class NoteHolder {
	private int id;
	private int dynamic_id;
	private String passenger_num;
	private String ticket_order;
	private String dining_order;
	private String passenger_dead_num;
	private String empoyee_dead_num;
	private String package_num;
	private String tick_lost;
	private String Notepad;
	private String import_info;
	private long submit_time;
	private String submit_name;
	private long approval_time;
	private String approval_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDynamic_id() {
		return dynamic_id;
	}
	public void setDynamic_id(int dynamic_id) {
		this.dynamic_id = dynamic_id;
	}
	public String getPassenger_num() {
		return passenger_num;
	}
	public void setPassenger_num(String passenger_num) {
		this.passenger_num = passenger_num;
	}
	public String getTicket_order() {
		return ticket_order;
	}
	public void setTicket_order(String ticket_order) {
		this.ticket_order = ticket_order;
	}
	public String getDining_order() {
		return dining_order;
	}
	public void setDining_order(String dining_order) {
		this.dining_order = dining_order;
	}
	public String getPassenger_dead_num() {
		return passenger_dead_num;
	}
	public void setPassenger_dead_num(String passenger_dead_num) {
		this.passenger_dead_num = passenger_dead_num;
	}
	public String getEmpoyee_dead_num() {
		return empoyee_dead_num;
	}
	public void setEmpoyee_dead_num(String empoyee_dead_num) {
		this.empoyee_dead_num = empoyee_dead_num;
	}
	public String getPackage_num() {
		return package_num;
	}
	public void setPackage_num(String package_num) {
		this.package_num = package_num;
	}
	public String getTick_lost() {
		return tick_lost;
	}
	public void setTick_lost(String tick_lost) {
		this.tick_lost = tick_lost;
	}
	public String getNotepad() {
		return Notepad;
	}
	public void setNotepad(String notepad) {
		Notepad = notepad;
	}
	public String getImport_info() {
		return import_info;
	}
	public void setImport_info(String import_info) {
		this.import_info = import_info;
	}
	public long getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(long submit_time) {
		this.submit_time = submit_time;
	}
	public String getSubmit_name() {
		return submit_name;
	}
	public void setSubmit_name(String submit_name) {
		this.submit_name = submit_name;
	}
	public long getApproval_time() {
		return approval_time;
	}
	public void setApproval_time(long approval_time) {
		this.approval_time = approval_time;
	}
	public String getApproval_name() {
		return approval_name;
	}
	public void setApproval_name(String approval_name) {
		this.approval_name = approval_name;
	}
	public NoteHolder(int id, int dynamic_id, String passenger_num,
			String ticket_order, String dining_order,
			String passenger_dead_num, String empoyee_dead_num,
			String package_num, String tick_lost, String notepad,
			String import_info, long submit_time, String submit_name,
			long approval_time, String approval_name) {
		super();
		this.id = id;
		this.dynamic_id = dynamic_id;
		this.passenger_num = passenger_num;
		this.ticket_order = ticket_order;
		this.dining_order = dining_order;
		this.passenger_dead_num = passenger_dead_num;
		this.empoyee_dead_num = empoyee_dead_num;
		this.package_num = package_num;
		this.tick_lost = tick_lost;
		Notepad = notepad;
		this.import_info = import_info;
		this.submit_time = submit_time;
		this.submit_name = submit_name;
		this.approval_time = approval_time;
		this.approval_name = approval_name;
	}
	
	
}
