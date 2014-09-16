package com.xianzhi.tool.db;

public class SeatInfoHolder {
	private int ticket_type;
	private String coach_no;
	private String sale_mode;
	private String to_tele_code;
	private String office_no;
	private String board_train_code;
	private String seat_type_code;
	private String seat_no;
	private String from_station_name;
	private String statistics_date;
	private String to_station_name;
	private String ticket_source;
	private String from_tele_code;
	private String train_date;
	private int window_no;
	private float ticket_price;
	private String ticket_no;
	private String inner_code;
	private String train_no;

	public SeatInfoHolder(int ticket_type, String coach_no,
			String sale_mode, String to_tele_code, String office_no,
			String board_train_code, String seat_type_code, String seat_no,
			String from_station_name, String statistics_date,
			String to_station_name, String ticket_source,
			String from_tele_code, String train_date, int window_no,
			float ticket_price, String ticket_no, String inner_code,
			String train_no) {
		super();
		this.ticket_type = ticket_type;
		this.coach_no = coach_no;
		this.sale_mode = sale_mode;
		this.to_tele_code = to_tele_code;
		this.office_no = office_no;
		this.board_train_code = board_train_code;
		this.seat_type_code = seat_type_code;
		this.seat_no = seat_no;
		this.from_station_name = from_station_name;
		this.statistics_date = statistics_date;
		this.to_station_name = to_station_name;
		this.ticket_source = ticket_source;
		this.from_tele_code = from_tele_code;
		this.train_date = train_date;
		this.window_no = window_no;
		this.ticket_price = ticket_price;
		this.ticket_no = ticket_no;
		this.inner_code = inner_code;
		this.train_no = train_no;
	}

	public int getTicket_type() {
		return ticket_type;
	}

	public void setTicket_type(int ticket_type) {
		this.ticket_type = ticket_type;
	}

	public String getCoach_no() {
		return coach_no;
	}

	public void setCoach_no(String coach_no) {
		this.coach_no = coach_no;
	}

	public String getSale_mode() {
		return sale_mode;
	}

	public void setSale_mode(String sale_mode) {
		this.sale_mode = sale_mode;
	}

	public String getTo_tele_code() {
		return to_tele_code;
	}

	public void setTo_tele_code(String to_tele_code) {
		this.to_tele_code = to_tele_code;
	}

	public String getOffice_no() {
		return office_no;
	}

	public void setOffice_no(String office_no) {
		this.office_no = office_no;
	}

	public String getBoard_train_code() {
		return board_train_code;
	}

	public void setBoard_train_code(String board_train_code) {
		this.board_train_code = board_train_code;
	}

	public String getSeat_type_code() {
		return seat_type_code;
	}

	public void setSeat_type_code(String seat_type_code) {
		this.seat_type_code = seat_type_code;
	}

	public String getSeat_no() {
		return seat_no;
	}

	public void setSeat_no(String seat_no) {
		this.seat_no = seat_no;
	}

	public String getFrom_station_name() {
		return from_station_name;
	}

	public void setFrom_station_name(String from_station_name) {
		this.from_station_name = from_station_name;
	}

	public String getStatistics_date() {
		return statistics_date;
	}

	public void setStatistics_date(String statistics_date) {
		this.statistics_date = statistics_date;
	}

	public String getTo_station_name() {
		return to_station_name;
	}

	public void setTo_station_name(String to_station_name) {
		this.to_station_name = to_station_name;
	}

	public String getTicket_source() {
		return ticket_source;
	}

	public void setTicket_source(String ticket_source) {
		this.ticket_source = ticket_source;
	}

	public String getFrom_tele_code() {
		return from_tele_code;
	}

	public void setFrom_tele_code(String from_tele_code) {
		this.from_tele_code = from_tele_code;
	}

	public String getTrain_date() {
		return train_date;
	}

	public void setTrain_date(String train_date) {
		this.train_date = train_date;
	}

	public int getWindow_no() {
		return window_no;
	}

	public void setWindow_no(int window_no) {
		this.window_no = window_no;
	}

	public float getTicket_price() {
		return ticket_price;
	}

	public void setTicket_price(float ticket_price) {
		this.ticket_price = ticket_price;
	}

	public String getTicket_no() {
		return ticket_no;
	}

	public void setTicket_no(String ticket_no) {
		this.ticket_no = ticket_no;
	}

	public String getInner_code() {
		return inner_code;
	}

	public void setInner_code(String inner_code) {
		this.inner_code = inner_code;
	}

	public String getTrain_no() {
		return train_no;
	}

	public void setTrain_no(String train_no) {
		this.train_no = train_no;
	}

	@Override
	public String toString() {
		return "车票类型：" + 1 + "\r\n编组："
				+ coach_no + "\r\n售票方式：" + sale_mode + "\r\n售票处号：" + office_no
				+ "\r\n车次：" + board_train_code
				+ "\r\n坐位类型：" + seat_type_code + "\r\n坐位号：" + seat_no
				+ "\r\n起始：" + from_station_name
				+ "\r\n到达："+ to_station_name + "\r\n统计时间：" + statistics_date + "\r\n发车时间："
				+ train_date + "\r\n窗口号：" + window_no + "\r\n车票价格："
				+ ticket_price + "\r\n票号：" + ticket_no + "\r\n全车辆号：" + train_no 
				+ "\r\n----------------------------------------";
	}

}
