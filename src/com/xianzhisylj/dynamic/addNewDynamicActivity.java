package com.xianzhisylj.dynamic;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xianzhi.tool.view.CustomTimeSeterHoldDialog;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhialarm.listener.TimeDialogListener;

public class addNewDynamicActivity extends Activity implements OnClickListener {
	private EditText code_edit;
	private EditText data_edit;
	private EditText team_edit;
	private EditText team_length_edit;
	private ImageButton right_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnew_dynamic);
		initContentView();
	}

	private void initContentView() {
		code_edit = (EditText) findViewById(R.id.code_edit);
		data_edit = (EditText) findViewById(R.id.data_edit);
		data_edit.setOnClickListener(this);
		team_edit = (EditText) findViewById(R.id.team_edit);
		team_length_edit = (EditText) findViewById(R.id.team_length_edit);
		right_btn = (ImageButton) findViewById(R.id.right_btn);
		right_btn.setOnClickListener(this);
	}

	private String curtime;

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.data_edit:
			CustomTimeSeterHoldDialog dialog = new CustomTimeSeterHoldDialog(
					addNewDynamicActivity.this, R.style.customDialog,
					R.layout.dialog_time_seter, System.currentTimeMillis());
			dialog.setTimeDialogListener(new TimeDialogListener() {
				@Override
				public void getTimeInMill(long time) {
					// TODO Auto-generated method stub
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					curtime=format.format(time);
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

	private void commit() {
		final String str_code=code_edit.getText().toString().trim();
		final String str_team=team_edit.getText().toString().trim();
		final String str_team_length=team_length_edit.getText().toString().trim();
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().addNewDynamicTrain(getApplicationContext()
						, str_code, curtime, str_team, str_team_length);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}
		};
		task.execute();
	}

}
