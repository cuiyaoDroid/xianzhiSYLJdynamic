package com.xianzhisylj.dynamic;

import com.xianzhi.tool.db.NoteHelper;
import com.xianzhi.tool.db.NoteHolder;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class ImportWorkInfoActivity extends Activity{
	private TextView work_info_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importwork_info);
		initContentView();
	}
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
		work_info_txt=(TextView)findViewById(R.id.work_info_txt);
		work_info_txt.setText(Html.fromHtml(holder.getImport_info()));
	}
}
