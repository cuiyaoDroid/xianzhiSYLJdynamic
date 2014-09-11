package com.xianzhi.stool;

import android.content.Context;
import android.content.SharedPreferences;

import com.xianzhi.tool.db.KeyworkAndRecordHelper;
import com.xianzhi.tool.db.KeyworkRholder;
import com.xianzhi.webtool.HttpJsonTool;

public class PreferencesHelper {
	private SharedPreferences userInfo;
	private int trainId;
	public PreferencesHelper(int trainId,Context context){
		this.trainId=trainId;
		userInfo = context.getSharedPreferences(HttpJsonTool.userId + "__" + trainId, 0);
	}
	
	public SharedPreferences getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(SharedPreferences userInfo) {
		this.userInfo = userInfo;
	}

	public KeyworkRholder getData(){
		
		String context = userInfo.getString(KeyworkAndRecordHelper.CONTEXT, "");
		String passenger_cnt = userInfo.getString(KeyworkAndRecordHelper.PASSENGER_CNT, "");
		String packet_cnt = userInfo.getString(KeyworkAndRecordHelper.PACKET_CNT, "");
		String passenger_rcpt = userInfo.getString(KeyworkAndRecordHelper.PASSENGER_RCPT, "");
		String catering_rcpt = userInfo.getString(KeyworkAndRecordHelper.CATERING_RCPT, "");
		String passenger_miss = userInfo.getString(KeyworkAndRecordHelper.PASSENGER_MISS, "");
		String receipts_miss = userInfo.getString(KeyworkAndRecordHelper.RECEIPTS_MISS, "");
		String worker_miss = userInfo.getString(KeyworkAndRecordHelper.WORKER_MISS, "");
		String notes = userInfo.getString(KeyworkAndRecordHelper.NOTES, "");
		
		KeyworkRholder holder = new KeyworkRholder(String.valueOf(trainId), context, passenger_cnt
				, packet_cnt
				, passenger_rcpt, catering_rcpt, passenger_miss, receipts_miss, worker_miss,notes);
		return holder;
		
	}
}
