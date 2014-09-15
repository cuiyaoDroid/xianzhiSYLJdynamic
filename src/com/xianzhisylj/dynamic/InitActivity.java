package com.xianzhisylj.dynamic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.xianzhi.service.dynamicService;
import com.xianzhi.tool.db.DBHelper;
import com.xianzhi.webtool.HttpsClient;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class InitActivity extends Activity {
	public static String SDCardRoot = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator;
	public static String RAIL = ".cc_rail";

	public static void putVersion(String s) {
		try {
			FileOutputStream outStream = new FileOutputStream(SDCardRoot + RAIL
					+ "/20.txt", false);
			OutputStreamWriter writer = new OutputStreamWriter(outStream,
					"gb2312");
			writer.write(s);
			writer.flush();
			writer.close();// ¼ÇµÃ¹Ø±Õ

			outStream.close();
		} catch (Exception e) {
			System.out.println("write to sdcard for error");
		}
	}
	private void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	private void upGradeDBifnessage(){
		DBHelper helper=new DBHelper(getApplicationContext());
		helper.close();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		createPath(SecurityCheckApp.appPath);
		upGradeDBifnessage();
//		Bundle bundle = getIntent().getExtras();
//		SecurityCheckApp.token = bundle.getString("token");
//		SecurityCheckApp.loginName = bundle.getString("loginName");
//		SecurityCheckApp.userId = bundle.getInt("userId");
//		putVersion(getString(R.string.version));
		HttpsClient.getInstance().init(getApplicationContext());
		startService(new Intent(InitActivity.this, dynamicService.class));
		Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
		startActivityForResult(intent, RESULT_OK);
		finish();
		
	}
}
