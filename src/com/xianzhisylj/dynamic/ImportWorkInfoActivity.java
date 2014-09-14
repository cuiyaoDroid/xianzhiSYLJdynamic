package com.xianzhisylj.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.stool.T;
import com.xianzhi.tool.db.KeyworkAndRecordHelper;
import com.xianzhi.tool.db.KeyworkAndRecordHolder;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class ImportWorkInfoActivity extends Activity{
	private TextView work_info_txt;
	private ProgressBar progressBar;
	private int trainId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importwork_info);
		trainId=getIntent().getIntExtra("id", -1);
		if(trainId==-1){
			return;
		}
		initContentView();
		getImportInfo();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		RefreshGroupData();
	}
	private void getImportInfo(){
		progressBar.setVisibility(View.VISIBLE);
		work_info_txt.setVisibility(View.GONE);
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
				work_info_txt.setVisibility(View.VISIBLE);
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
			T.show(getApplicationContext(), "您的账号已在其他设备上登录，请重新登录", Toast.LENGTH_LONG);
			startActivity(intent);
		} catch (Exception e) {
		}
	}
	private void RefreshGroupData(){
		KeyworkAndRecordHelper helper=new KeyworkAndRecordHelper(getApplicationContext());
		KeyworkAndRecordHolder holder=helper.selectData_Id(trainId);
		helper.close();
		if(holder==null){
			return;
		}
		work_info_txt.setText(holder.getContext());
	}
	private void initContentView(){
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		work_info_txt=(TextView)findViewById(R.id.work_info_txt);
		work_info_txt.setText("暂无");
	}
}
