package com.xianzhisylj.dynamiclcz;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.xianzhi.stool.L;
import com.xianzhi.stool.T;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.webtool.DownloadAsyncTask;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhi.webtool.HttpStringMD5;
import com.xianzhi.webtool.downloadCallbackListener;
import com.xianzhisylj.dynamiclcz.R;

public class LoginActivity extends Activity {
	private CheckBox save_check;
	private ProgressDialog progreeDialog;
	private ImageButton login_btn;
	private EditText usr_edit;
	private EditText pass_edit;
	private String preloginName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		save_check = (CheckBox) findViewById(R.id.save_check);
		login_btn = (ImageButton) findViewById(R.id.login_btn);
		usr_edit = (EditText) findViewById(R.id.usr_edit);
		pass_edit = (EditText) findViewById(R.id.pass_edit);
		SharedPreferences userInfo = getSharedPreferences("user_info", 0);
		preloginName = userInfo.getString("loginName", "");
		String password = userInfo.getString("password", "");
		int savepassword = userInfo.getInt("savepassword", 0);
		usr_edit.setText(preloginName);
		pass_edit.setText(password);
		save_check.setChecked(savepassword == 1 ? true : false);
		initProgressDialog();
		initdowloadprogressDialog();
		login_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String loginName = usr_edit.getText().toString();
				if (!preloginName.equals(loginName)) {
					DynamicListHelper helper = new DynamicListHelper(
							getApplicationContext());
					helper.clear();
					helper.close();
				}
				String password = pass_edit.getText().toString();
				startLogin(loginName, password);
			}
		});
		//checkUpdate();
	}

	private void initProgressDialog() {
		progreeDialog = new ProgressDialog(this);
		progreeDialog.setTitle("");
		progreeDialog.setMessage("正在登录请稍等...");
		progreeDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	private void startLogin(final String loginName, final String password) {
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
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				HttpJsonTool tool = HttpJsonTool.getInstance();
				return tool.login(loginName, HttpStringMD5.md5(password));
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if (result.startsWith(HttpJsonTool.ERROR)) {
					T.showLong(getApplicationContext(),
							result.replace(HttpJsonTool.ERROR, ""));
					return;
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					Intent intent = new Intent(getApplicationContext(),
							DynamicMainActivity.class);
					startActivityForResult(intent, RESULT_OK);
					finish();
				}
			}
		};
		task.execute();

	}

	AsyncTask<Void, Void, String> updatetask;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (updatetask != null) {
			updatetask.cancel(true);
		}
	}

	private void checkUpdate() {
		updatetask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				HttpJsonTool tool = HttpJsonTool.getInstance();
				return tool.checkupdata(21);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progreeDialog.dismiss();
				if (result.startsWith(HttpJsonTool.ERROR)) {
					return;
				} else {
					String args[] = result.split(";");
					if (args.length > 1) {
						float version = Float.parseFloat(getResources()
								.getString(R.string.version));
						float new_version = Float.parseFloat(args[0]);
						L.i("new_version > version"+new_version+"    "+version);
						if (new_version > version) {
							showUpdateDialog(args[1]);
						}
					}
				}
			}
		};
		updatetask.execute();
	}

	private void showUpdateDialog(final String url) {
		AlertDialog.Builder builder = new Builder(LoginActivity.this);
		builder.setMessage("发现系统有更新版本，是否更新？");
		builder.setTitle("提示");
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				downloadAPK(url);
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	ProgressDialog dowloadprogressDialog;
	private void initdowloadprogressDialog(){
		dowloadprogressDialog = new ProgressDialog(this);
		dowloadprogressDialog.setTitle("");
		dowloadprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dowloadprogressDialog.setMessage("正在下载请稍等...");
		dowloadprogressDialog.setCancelable(false);
	}
	private void downloadAPK(String url){
		dowloadprogressDialog.show();
		DownloadAsyncTask task=new DownloadAsyncTask(dowloadprogressDialog);
		task.setdownloadCallbackListener(new downloadCallbackListener() {
			@Override
			public void onComplete(File downloadFile) {
				// TODO Auto-generated method stub
				installApk(downloadFile);
			}
		});
		task.execute(url);
	}
	private void installApk(File downloadFile) {
		if(downloadFile==null){
			T.show(getApplicationContext(), "下载的文件有损坏，请重试",Toast.LENGTH_LONG);
			return;
		}
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(downloadFile), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
