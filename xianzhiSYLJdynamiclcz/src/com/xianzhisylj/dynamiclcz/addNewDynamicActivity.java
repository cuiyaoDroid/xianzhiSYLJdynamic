package com.xianzhisylj.dynamiclcz;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xianzhi.stool.T;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.view.CustomTimeSeterHoldDialog;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhialarm.listener.TimeDialogListener;
import com.xianzhisecuritycheck.main.SecurityCheckApp;
import com.xianzhisylj.dynamiclcz.R;

public class addNewDynamicActivity extends Activity implements OnClickListener {
	private EditText code_edit;
	private EditText data_edit;
	private EditText team_edit;
	private EditText team_length_edit;
	private ImageButton right_btn;
	private TextView title_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnew_dynamic);
		initProgressDialog();
		initContentView();
	}
	private int id=-1;
	private boolean isfirst=true;
	@SuppressLint("SimpleDateFormat") 
	private void initContentView() {
		code_edit = (EditText) findViewById(R.id.code_edit);
		data_edit = (EditText) findViewById(R.id.data_edit);
		data_edit.setOnClickListener(this);
		team_edit = (EditText) findViewById(R.id.team_edit);
		team_length_edit = (EditText) findViewById(R.id.team_length_edit);
	
		right_btn = (ImageButton) findViewById(R.id.right_btn);
		right_btn.setOnClickListener(this);
		title_txt = (TextView) findViewById(R.id.title_txt);
		title_txt.setText("新建");
		isfirst =getIntent().getBooleanExtra("isfirst", true);
		team_edit.setVisibility(isfirst?View.VISIBLE:View.GONE);
		team_length_edit.setVisibility(isfirst?View.VISIBLE:View.GONE);
		id = getIntent().getIntExtra("id",-1);
		if(id!=-1){
			long start_time=getIntent().getLongExtra(DynamicListHelper.start_time,0);
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format2 = new SimpleDateFormat(
					"yyyyMMdd");
			curtime=format.format(start_time);
			date=format2.format(start_time);
			data_edit.setText(curtime);
			data_edit.setClickable(false);
			String train_code = getIntent().getStringExtra(DynamicListHelper.board_train_code);
			code_edit.setText(train_code);
			code_edit.setEnabled(false);
			title_txt.setText("认领");
		}
	}

	private String curtime="";
	private String date="";
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.data_edit:
			if(!isfirst){
				return;
			}
			CustomTimeSeterHoldDialog dialog = new CustomTimeSeterHoldDialog(
					addNewDynamicActivity.this, R.style.customDialog,
					R.layout.dialog_time_seter, System.currentTimeMillis());
			dialog.setTimeDialogListener(new TimeDialogListener() {
				@Override
				public void getTimeInMill(long time) {
					// TODO Auto-generated method stub
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat format2 = new SimpleDateFormat(
							"yyyyMMdd");
					curtime=format.format(time);
					date=format2.format(time);
					data_edit.setText(curtime);
				}
			});
			dialog.show();
			break;
		case R.id.right_btn:
			commit();
			break;
		default:
			break;
		}
	}
	private ProgressDialog progreeDialog;
	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("正在初始化信息请稍等...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	private void commit() {
		
		final String str_code=code_edit.getText().toString().trim();
		final String str_team=team_edit.getText().toString().trim();
		final String str_team_length=team_length_edit.getText().toString().trim();
		if((str_code.length()==0||str_team.length()==0||str_team_length.length()==0
				||curtime.length()==0)&&isfirst){
			T.show(getApplicationContext(), "请填写全部信息", Toast.LENGTH_LONG);
			return;
		}
		progreeDialog.show();
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().addNewDynamicTrain(getApplicationContext()
						,id, str_code, curtime, str_team, str_team_length,date,isfirst);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if(result.startsWith(HttpJsonTool.ERROR403)){
					gotoLoginView();
					return;
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.show(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""), Toast.LENGTH_LONG);
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					T.show(getApplicationContext(), "创建成功", Toast.LENGTH_LONG);
					finish();
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
}
