package com.xianzhisylj.dynamic;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.xianzhi.tool.db.NoteHelper;
import com.xianzhi.tool.db.NoteHolder;

public class NoteInfoActivity extends Activity {
	private TextView text_r1_t1;
	private TextView text_r1_t2;
	private TextView text_r1_t3;
	private TextView text_r2_t1;
	private TextView text_r2_t2;
	private TextView text_r2_t3;
	private TextView text_r3_t2;
	private TextView text_r5_t1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_info);
		initContentView();
	}

	private void initContentView() {
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
		text_r1_t1 = (TextView) findViewById(R.id.text_r1_t1);
		text_r1_t2 = (TextView) findViewById(R.id.text_r1_t2);
		text_r1_t3 = (TextView) findViewById(R.id.text_r1_t3);

		text_r2_t1 = (TextView) findViewById(R.id.text_r2_t1);
		text_r2_t2 = (TextView) findViewById(R.id.text_r2_t2);
		text_r2_t3 = (TextView) findViewById(R.id.text_r2_t3);

		text_r3_t2 = (TextView) findViewById(R.id.text_r3_t2);
		text_r5_t1 = (TextView) findViewById(R.id.text_r5_t1);
		
		String str_passenger_num = "�����ÿ�����\u3000<font color=#006AE2>" + holder.getPassenger_num()
				+ "</font>";
		text_r1_t1.setText(Html.fromHtml(str_passenger_num));
		String str_ticket_order = "��Ʊ����\u3000<font color=#006AE2>" + holder.getTicket_order()
				+ "</font>";
		text_r1_t2.setText(Html.fromHtml(str_ticket_order));
		String str_passenger_dead_num = "�ÿ�����<font color=grey>(��/��)</font>\u3000<font color=#006AE2>" 
				+ holder.getPassenger_dead_num()+ "</font>";
		text_r1_t3.setText(Html.fromHtml(str_passenger_dead_num));
		
		String str_package_num = "�����а�����\u3000<font color=#006AE2>" + holder.getPackage_num()
				+ "</font>";
		text_r2_t1.setText(Html.fromHtml(str_package_num));
		String str_dining_order = "��Ӫ����\u3000<font color=#006AE2>" + holder.getDining_order()
				+ "</font>";
		text_r2_t2.setText(Html.fromHtml(str_dining_order));
		String str_empoyee_dead_num = "ְ������<font color=grey>(��/��)</font>\u3000<font color=#006AE2>" 
				+ holder.getEmpoyee_dead_num()+ "</font>";
		text_r2_t3.setText(Html.fromHtml(str_empoyee_dead_num));
		
		text_r2_t2.setText(Html.fromHtml(str_dining_order));
		String str_tick_lost = "Ʊ�ʧ<font color=grey>(Ԫ/��)</font>\u3000<font color=#006AE2>" 
				+ holder.getTick_lost()+ "</font>";
		text_r3_t2.setText(Html.fromHtml(str_tick_lost));
		
		text_r5_t1.setText(Html.fromHtml(holder.getNotepad()));
	}
}
