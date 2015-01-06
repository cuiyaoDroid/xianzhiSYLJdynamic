package com.xianzhi.tool.db;

public class KeyworkRholder {
	private String train_id;
	private String context;
	private String passenger_cnt;
	private String packet_cnt;
	private String passenger_rcpt;
	private String catering_rcpt;
	private String passenger_miss;
	private String receipts_miss;
	private String worker_miss;
	private String notes;
	public KeyworkRholder(String train_id, String context,
			String passenger_cnt, String packet_cnt, String passenger_rcpt,
			String catering_rcpt, String passenger_miss, String receipts_miss,
			String worker_miss, String notes) {
		super();
		this.train_id = train_id;
		this.context = context;
		this.passenger_cnt = passenger_cnt;
		this.packet_cnt = packet_cnt;
		this.passenger_rcpt = passenger_rcpt;
		this.catering_rcpt = catering_rcpt;
		this.passenger_miss = passenger_miss;
		this.receipts_miss = receipts_miss;
		this.worker_miss = worker_miss;
		this.notes = notes;
	}
	
	@Override
	public String toString() {
		return "KeyworkRholder [train_id=" + train_id + ", context=" + context
				+ ", passenger_cnt=" + passenger_cnt + ", packet_cnt="
				+ packet_cnt + ", passenger_rcpt=" + passenger_rcpt
				+ ", catering_rcpt=" + catering_rcpt + ", passenger_miss="
				+ passenger_miss + ", receipts_miss=" + receipts_miss
				+ ", worker_miss=" + worker_miss + ", notes=" + notes + "]";
	}

	public boolean isfull(){
		return context.trim().length()>0
				&&passenger_cnt.trim().length()>0
				&&packet_cnt.trim().length()>0
				&&passenger_rcpt.trim().length()>0
				&&catering_rcpt.trim().length()>0
				&&passenger_miss.trim().length()>0
				&&receipts_miss.trim().length()>0
				&&worker_miss.trim().length()>0
				&&notes.trim().length()>0;
	}
	public String getTrain_id() {
		return train_id;
	}
	public void setTrain_id(String train_id) {
		this.train_id = train_id;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getPassenger_cnt() {
		return passenger_cnt;
	}
	public void setPassenger_cnt(String passenger_cnt) {
		this.passenger_cnt = passenger_cnt;
	}
	public String getPacket_cnt() {
		return packet_cnt;
	}
	public void setPacket_cnt(String packet_cnt) {
		this.packet_cnt = packet_cnt;
	}
	public String getPassenger_rcpt() {
		return passenger_rcpt;
	}
	public void setPassenger_rcpt(String passenger_rcpt) {
		this.passenger_rcpt = passenger_rcpt;
	}
	public String getCatering_rcpt() {
		return catering_rcpt;
	}
	public void setCatering_rcpt(String catering_rcpt) {
		this.catering_rcpt = catering_rcpt;
	}
	public String getPassenger_miss() {
		return passenger_miss;
	}
	public void setPassenger_miss(String passenger_miss) {
		this.passenger_miss = passenger_miss;
	}
	public String getReceipts_miss() {
		return receipts_miss;
	}
	public void setReceipts_miss(String receipts_miss) {
		this.receipts_miss = receipts_miss;
	}
	public String getWorker_miss() {
		return worker_miss;
	}
	public void setWorker_miss(String worker_miss) {
		this.worker_miss = worker_miss;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	

}
