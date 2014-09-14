package com.xianzhi.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import com.xianzhi.stool.L;
import com.xianzhi.stool.PreferencesHelper;
import com.xianzhi.stool.T;
import com.xianzhi.tool.db.KeyworkRholder;
import com.xianzhi.tool.db.ReviewHolder;
import com.xianzhi.webtool.HttpJsonTool;

public class dynamicService extends Service {
	private static final String RECONNECT_ALARM = "com.tingshuo.service.RECONNECT_ALARM";
	private BroadcastReceiver mAlarmReceiver = new ReconnectAlarmReceiver();
	private Intent mAlarmIntent = new Intent(RECONNECT_ALARM);
	private PendingIntent mPAlarmIntent;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		L.i("dynamicService onCreate");
		mPAlarmIntent = PendingIntent.getBroadcast(this, 0, mAlarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		registerReceiver(mAlarmReceiver, new IntentFilter(RECONNECT_ALARM));
		((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setRepeating(
				AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				30 * 60 * 1000, mPAlarmIntent);
	}
	private PreferencesHelper pHelper;
	private void commit() {
		if(HttpJsonTool.train_id==-1){
			return;
		}
		pHelper = new PreferencesHelper(HttpJsonTool.train_id, getApplicationContext());
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				KeyworkRholder Rholders = pHelper.getData();
				if (!Rholders.isfull()) {
					return HttpJsonTool.ERROR + "请填写全部信息";
				}
				return HttpJsonTool.getInstance().saveKeyworkAndRecord(
						getApplicationContext(), Rholders);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result == null) {
					return;
				}
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					return;
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
				}
			}
		};
		task.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	// 自动重连广播
	private class ReconnectAlarmReceiver extends BroadcastReceiver {
		public void onReceive(Context ctx, Intent i) {
			L.i("onReceive");
			commit();
		}
	}

}
