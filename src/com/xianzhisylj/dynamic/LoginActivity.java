package com.xianzhisylj.dynamic;

import com.xianzhi.stool.T;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhi.webtool.HttpStringMD5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends Activity{
	private CheckBox save_check;
	private ProgressDialog progreeDialog;
	private ImageButton login_btn;
	private EditText usr_edit;
	private EditText pass_edit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		save_check=(CheckBox)findViewById(R.id.save_check);
		login_btn=(ImageButton)findViewById(R.id.login_btn);
		usr_edit=(EditText)findViewById(R.id.usr_edit);
		pass_edit=(EditText)findViewById(R.id.pass_edit);
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		String loginName = userInfo.getString("loginName", "");
		String password = userInfo.getString("password", "");
		int savepassword = userInfo.getInt("savepassword", 0);
		usr_edit.setText(loginName);
		pass_edit.setText(password);
		save_check.setChecked(savepassword==1?true:false);
		initProgressDialog();
		login_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String loginName=usr_edit.getText().toString();
				String password=pass_edit.getText().toString();
				startLogin(loginName,password);
			}
		});
		
	}
	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("ÕýÔÚµÇÂ¼ÇëÉÔµÈ...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	private void startLogin(final String loginName,final String password){
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		userInfo.edit().putString("loginName", loginName).commit();
		if (save_check.isChecked()) {
			userInfo.edit().putString("password", password).commit();
			userInfo.edit().putInt("savepassword", 1).commit();
		} else {
			userInfo.edit().putString("password", "").commit();
			userInfo.edit().putInt("savepassword", 0).commit();
		}
		progreeDialog.show();
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				HttpJsonTool tool=HttpJsonTool.getInstance();
				return tool.login(loginName, HttpStringMD5.md5(password));
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if(result.startsWith(HttpJsonTool.ERROR)){
					T.showLong(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""));
					return;
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					Intent intent=new Intent(getApplicationContext(),DynamicMainActivity.class);
					startActivityForResult(intent, RESULT_OK);
					finish();
				}
			}
		};
		task.execute();
		
	}
}
