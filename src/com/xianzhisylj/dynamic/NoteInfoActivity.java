package com.xianzhisylj.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.stool.T;
import com.xianzhi.tool.db.KeyworkAndRecordHelper;
import com.xianzhi.tool.db.KeyworkAndRecordHolder;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class NoteInfoActivity extends Activity {
	private TextView text_r1_t1;
	private TextView text_r1_t2;
	private TextView text_r1_t3;
	private TextView text_r2_t1;
	private TextView text_r2_t2;
	private TextView text_r2_t3;
	private TextView text_r3_t2;
	private TextView text_r5_t1;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_info);
		trainId=getIntent().getIntExtra("id", -1);
		if(trainId==-1){
			return;
		}
		initContentView();
		getImportInfo();
	}
	private void getImportInfo(){
		progressBar.setVisibility(View.VISIBLE);
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getKeyworkAndRecord(getApplicationContext(), trainId);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				if(result.startsWith(HttpJsonTool.ERROR403)){
					gotoLoginView();
					return;
				}else if(result.startsWith(HttpJsonTool.ERROR)){
//					T.showLong(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""));
					return;
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					RefreshGroupData();
					return;
				}
			}
		};
		task.execute();
	}
	private void gotoLoginView() {
		SecurityCheckApp.token = "";
		try {
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			T.show(getApplicationContext(), "�����˺����������豸�ϵ�¼�������µ�¼", Toast.LENGTH_LONG);
			startActivity(intent);
		} catch (Exception e) {
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		RefreshGroupData();
	}
	private void RefreshGroupData(){
		KeyworkAndRecordHelper helper=new KeyworkAndRecordHelper(getApplicationContext());
		KeyworkAndRecordHolder holder=helper.selectData_Id(trainId);
		helper.close();
		if(holder==null){
			return;
		}
		String str_passenger_num = "�����ÿ�����\u3000<font color=#006AE2>" + holder.getPassenger_cnt()
				+ "</font>";
		text_r1_t1.setText(Html.fromHtml(str_passenger_num));
		String str_ticket_order = "��Ʊ����\u3000<font color=#006AE2>" + holder.getPassenger_rcpt()
				+ "</font>";
		text_r1_t2.setText(Html.fromHtml(str_ticket_order));
		String str_passenger_dead_num = "�ÿ�����<font color=grey>(��/��)</font>\u3000<font color=#006AE2>" 
				+ holder.getPassenger_miss()+ "</font>";
		text_r1_t3.setText(Html.fromHtml(str_passenger_dead_num));
		
		String str_package_num = "�����а�����\u3000<font color=#006AE2>" + holder.getPacket_cnt()
				+ "</font>";
		text_r2_t1.setText(Html.fromHtml(str_package_num));
		String str_dining_order = "��Ӫ����\u3000<font color=#006AE2>" + holder.getCatering_rcpt()
				+ "</font>";
		text_r2_t2.setText(Html.fromHtml(str_dining_order));
		String str_empoyee_dead_num = "ְ������<font color=grey>(��/��)</font>\u3000<font color=#006AE2>" 
				+ holder.getWorker_miss()+ "</font>";
		text_r2_t3.setText(Html.fromHtml(str_empoyee_dead_num));
		
		text_r2_t2.setText(Html.fromHtml(str_dining_order));
		String str_tick_lost = "Ʊ�ʧ<font color=grey>(Ԫ/��)</font>\u3000<font color=#006AE2>" 
				+ holder.getReceipts_miss()+ "</font>";
		text_r3_t2.setText(Html.fromHtml(str_tick_lost));
		
		text_r5_t1.setText(Html.fromHtml(holder.getNotes()));
	}
	private int trainId;
	private void initContentView() {
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		text_r1_t1 = (TextView) findViewById(R.id.text_r1_t1);
		text_r1_t2 = (TextView) findViewById(R.id.text_r1_t2);
		text_r1_t3 = (TextView) findViewById(R.id.text_r1_t3);

		text_r2_t1 = (TextView) findViewById(R.id.text_r2_t1);
		text_r2_t2 = (TextView) findViewById(R.id.text_r2_t2);
		text_r2_t3 = (TextView) findViewById(R.id.text_r2_t3);

		text_r3_t2 = (TextView) findViewById(R.id.text_r3_t2);
		text_r5_t1 = (TextView) findViewById(R.id.text_r5_t1);
		
		String str_passenger_num = "�����ÿ�����\u3000<font color=#006AE2>" + "����"
				+ "</font>";
		text_r1_t1.setText(Html.fromHtml(str_passenger_num));
		String str_ticket_order = "��Ʊ����\u3000<font color=#006AE2>" + "����"
				+ "</font>";
		text_r1_t2.setText(Html.fromHtml(str_ticket_order));
		String str_passenger_dead_num = "�ÿ�����<font color=grey>(��/��)</font>\u3000<font color=#006AE2>" 
				+ "����"+ "</font>";
		text_r1_t3.setText(Html.fromHtml(str_passenger_dead_num));
		
		String str_package_num = "�����а�����\u3000<font color=#006AE2>" + "����"
				+ "</font>";
		text_r2_t1.setText(Html.fromHtml(str_package_num));
		String str_dining_order = "��Ӫ����\u3000<font color=#006AE2>" + "����"
				+ "</font>";
		text_r2_t2.setText(Html.fromHtml(str_dining_order));
		String str_empoyee_dead_num = "ְ������<font color=grey>(��/��)</font>\u3000<font color=#006AE2>" 
				+ "����"+ "</font>";
		text_r2_t3.setText(Html.fromHtml(str_empoyee_dead_num));
		
		text_r2_t2.setText(Html.fromHtml(str_dining_order));
		String str_tick_lost = "Ʊ�ʧ<font color=grey>(Ԫ/��)</font>\u3000<font color=#006AE2>" 
				+ "����"+ "</font>";
		text_r3_t2.setText(Html.fromHtml(str_tick_lost));
		
		text_r5_t1.setText(Html.fromHtml("����"));
	}
}
