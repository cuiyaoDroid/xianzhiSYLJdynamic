package com.xianzhi.tool.db;

public class KeyworkAndRecordHolder {
	private int train_id;
	private String context;
	private int passenger_cnt;
	private int packet_cnt;
	private float passenger_rcpt;
	private float catering_rcpt;
	private int passenger_miss;
	private float receipts_miss;
	private int worker_miss;
	private String notes;
	
	public int getTrain_id() {
		return train_id;
	}

	public void setTrain_id(int train_id) {
		this.train_id = train_id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getPassenger_cnt() {
		return passenger_cnt;
	}

	public void setPassenger_cnt(int passenger_cnt) {
		this.passenger_cnt = passenger_cnt;
	}

	public int getPacket_cnt() {
		return packet_cnt;
	}

	public void setPacket_cnt(int packet_cnt) {
		this.packet_cnt = packet_cnt;
	}

	public float getPassenger_rcpt() {
		return passenger_rcpt;
	}

	public void setPassenger_rcpt(float passenger_rcpt) {
		this.passenger_rcpt = passenger_rcpt;
	}

	public float getCatering_rcpt() {
		return catering_rcpt;
	}

	public void setCatering_rcpt(float catering_rcpt) {
		this.catering_rcpt = catering_rcpt;
	}

	public int getPassenger_miss() {
		return passenger_miss;
	}

	public void setPassenger_miss(int passenger_miss) {
		this.passenger_miss = passenger_miss;
	}

	public float getReceipts_miss() {
		return receipts_miss;
	}

	public void setReceipts_miss(float receipts_miss) {
		this.receipts_miss = receipts_miss;
	}

	public int getWorker_miss() {
		return worker_miss;
	}

	public void setWorker_miss(int worker_miss) {
		this.worker_miss = worker_miss;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public KeyworkAndRecordHolder(int train_id, String context,
			int passenger_cnt, int packet_cnt, float passenger_rcpt,
			float catering_rcpt, int passenger_miss, float receipts_miss,
			int worker_miss,String notes) {
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
		this.notes=notes;
	}
	

}
