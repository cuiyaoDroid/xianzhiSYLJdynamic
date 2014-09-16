package com.xianzhisylj.dynamic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xianzhi.stool.T;
import com.xianzhi.tool.adapter.ReviewListAdapter;
import com.xianzhi.tool.db.ReviewHelper;
import com.xianzhi.tool.db.ReviewHolder;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class ReviewInfoActivity extends Activity{
	private ProgressBar progressBar;
	private ListView review_listView;
	private ArrayList<Map<String,Object>>listdata;
	private ReviewListAdapter adapter;
	private int train_id;
	private Button commit_btn;
	private EditText review_input_edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_info);
		train_id=getIntent().getIntExtra("id", -1);
		if(train_id==-1){
			return;
		}
		initContentView();
		getReviewInfo();
	}
	@SuppressLint("SimpleDateFormat")
	private void initContentView(){
		listdata=new ArrayList<Map<String,Object>>();
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		review_listView = (ListView)findViewById(R.id.review_listView);
		commit_btn = (Button)findViewById(R.id.commit_btn);
		review_input_edit = (EditText)findViewById(R.id.review_input_edit);
		commit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commit();
			}
		});
		boolean ismySelf=getIntent().getBooleanExtra("ismySelf",false);
		findViewById(R.id.review_layout).setVisibility(
				(!ismySelf)&&HttpJsonTool.reviewTrain==1?View.VISIBLE:View.GONE);
		
		adapter=new ReviewListAdapter(getApplicationContext(),listdata 
				, R.layout.cell_review_list, new String[]{}, new int[]{});
		review_listView.setAdapter(adapter);
		
		
//		approval_txt = (TextView)findViewById(R.id.approval_txt);
//		submit_txt = (TextView)findViewById(R.id.submit_txt);
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String str_approval = format.format(holder.getApproval_time())+"\u3000<font color=#006AE2>" 
//				+ holder.getApproval_name() + "</font>"+"\u3000已阅。";
//		approval_txt.setText(Html.fromHtml(str_approval));
//		String str_submit = format.format(holder.getSubmit_time())+"\u3000列车长<font color=#006AE2>" 
//				+ holder.getSubmit_name() + "</font>"+"\u3000提交本次乘务报告的最终版本。";
//		submit_txt.setText(Html.fromHtml(str_submit));
	}
	private void getReviewInfo(){
		progressBar.setVisibility(View.VISIBLE);
		review_listView.setVisibility(View.INVISIBLE);
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getReviewList(getApplicationContext(), train_id);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				review_listView.setVisibility(View.VISIBLE);
				if(result.startsWith(HttpJsonTool.ERROR403)){
					gotoLoginView();
					return;
				}else if(result.startsWith(HttpJsonTool.ERROR)){
					T.showLong(getApplicationContext(), result.replace(HttpJsonTool.ERROR, ""));
					return;
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					RefreshData();
					return;
				}
			}
		};
		task.execute();
	}
	private void commit() {
		
		final String message=review_input_edit.getText().toString();
		if(message.trim().length()==0){
			T.showLong(getApplicationContext(), "请输入审阅意见");
			return;
		}
		commit_btn.setEnabled(false);
		progressBar.setVisibility(View.VISIBLE);
		AsyncTask<Void, Void, String> task =new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				
				ReviewHolder holder=new ReviewHolder(-1, train_id, HttpJsonTool.userId
						, "", "", "", ""
						, message, 1, 2, -1);
				return HttpJsonTool.getInstance().saveReview(getApplicationContext(), holder);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				commit_btn.setEnabled(true);
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
					getReviewInfo();
					review_input_edit.setText("");
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
	@SuppressLint("SimpleDateFormat") 
	private void RefreshData(){
		listdata.clear();
		ReviewHelper helper=new ReviewHelper(getApplicationContext());
		ArrayList<ReviewHolder>holders=helper.selectData(train_id);
		helper.close();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(ReviewHolder holder:holders){
			Map<String,Object>data=new HashMap<String, Object>();
			data.put(ReviewHelper.CREATE_TIME, format.format(holder.getCreate_time()));
			data.put(ReviewHelper.ID, holder.getId());
			data.put(ReviewHelper.POSITION_NAME,holder.getPosition_name());
			data.put(ReviewHelper.USER_NAME, holder.getUser_name());
			data.put(ReviewHelper.MSG_TYPE, holder.getMsg_type());
			data.put(ReviewHelper.MESSAGE, holder.getMessage());
			listdata.add(data);
		}
		adapter.notifyDataSetChanged();
	}
}
