package com.xianzhisylj.dynamic;

import java.text.SimpleDateFormat;

import com.xianzhi.tool.db.NoteHelper;
import com.xianzhi.tool.db.NoteHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class ReviewInfoActivity extends Activity{
	private TextView approval_txt;
	private TextView submit_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_info);
		initContentView();
	}
	@SuppressLint("SimpleDateFormat")
	private void initContentView(){
		int id=getIntent().getIntExtra("id", -1);
		if(id==-1){
			return;
		}
		NoteHelper helper=new NoteHelper(getApplicationContext());
		NoteHolder holder=helper.selectData_dynamicId(id);
		helper.close();
		if(holder==null){
			return;
		}
		approval_txt = (TextView)findViewById(R.id.approval_txt);
		submit_txt = (TextView)findViewById(R.id.submit_txt);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str_approval = format.format(holder.getApproval_time())+"\u3000<font color=#006AE2>" 
				+ holder.getApproval_name() + "</font>"+"\u3000已阅。";
		approval_txt.setText(Html.fromHtml(str_approval));
		String str_submit = format.format(holder.getSubmit_time())+"\u3000列车长<font color=#006AE2>" 
				+ holder.getSubmit_name() + "</font>"+"\u3000提交本次乘务报告的最终版本。";
		submit_txt.setText(Html.fromHtml(str_submit));
	}
}
