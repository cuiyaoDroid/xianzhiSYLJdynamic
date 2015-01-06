package com.xianzhisylj.dynamiclcz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xianzhi.stool.PreferencesHelper;
import com.xianzhi.stool.T;
import com.xianzhi.tool.db.KeyworkRholder;
import com.xianzhi.tool.db.ReviewHolder;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;
import com.xianzhisylj.dynamiclcz.R;

public class commitActivity extends Activity implements OnClickListener {
	private ImageButton commit_btn;
	private int train_id;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);
		train_id=getIntent().getIntExtra("id", -1);
		if(train_id==-1){
			return;
		}
		pHelper = new PreferencesHelper(train_id, getApplicationContext());
		initContentView();
	}

	private void initContentView() {
		commit_btn=(ImageButton)findViewById(R.id.commit_btn);
		commit_btn.setOnClickListener(this);
		progressBar=(ProgressBar)findViewById(R.id.progressBar);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.commit_btn:
			commit();
			break;
		default:
			break;
		}
	}
	private PreferencesHelper pHelper;
	
	private void commit() {
		
		progressBar.setVisibility(View.VISIBLE);
		commit_btn.setVisibility(View.GONE);
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				KeyworkRholder Rholders=pHelper.getData();
//				if(!Rholders.isfull()){
//					return HttpJsonTool.ERROR+"请填写全部信息";
//				}
				String repone=HttpJsonTool.getInstance().saveKeyworkAndRecord(getApplicationContext(), Rholders);
				if(repone.startsWith(HttpJsonTool.ERROR)){
					return repone;
				}
				ReviewHolder holder=new ReviewHolder(-1, train_id, HttpJsonTool.userId
						, "", "", "", ""
						, "", 1, 3, -1);
				return HttpJsonTool.getInstance().saveReview(getApplicationContext(), holder);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				commit_btn.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				if(result==null){
					return;
				}
				if(result.startsWith(HttpJsonTool.ERROR403)){
					gotoLoginView();
					return;
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.show(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""), Toast.LENGTH_LONG);
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					T.show(getApplicationContext(), "提交成功", Toast.LENGTH_LONG);
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
