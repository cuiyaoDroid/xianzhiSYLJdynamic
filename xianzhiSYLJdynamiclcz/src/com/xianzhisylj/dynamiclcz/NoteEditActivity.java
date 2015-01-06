package com.xianzhisylj.dynamiclcz;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xianzhi.stool.PreferencesHelper;
import com.xianzhi.tool.db.KeyworkAndRecordHelper;
import com.xianzhi.tool.db.KeyworkRholder;
import com.xianzhisylj.dynamiclcz.R;

public class NoteEditActivity extends Activity {
	private SharedPreferences userInfo;
	private EditText passage_edit;
	private EditText tick_edit;
	private EditText passager_die_edit;
	private EditText pakage_num_edit;
	private EditText mela_edit;
	private EditText employ_die_edit;
	private EditText tick_lose_edit;
	private EditText note_edit;
	private PreferencesHelper pHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_edit);
		int trainId = getIntent().getIntExtra("id", -1);
		if (trainId == -1) {
			return;
		}
		pHelper = new PreferencesHelper(trainId, getApplicationContext());
		userInfo = pHelper.getUserInfo();
		initContentView();

	}

	private void initContentView() {
		passage_edit = (EditText) findViewById(R.id.passage_edit);
		tick_edit = (EditText) findViewById(R.id.tick_edit);
		passager_die_edit = (EditText) findViewById(R.id.passager_die_edit);
		pakage_num_edit = (EditText) findViewById(R.id.pakage_num_edit);

		mela_edit = (EditText) findViewById(R.id.mela_edit);
		employ_die_edit = (EditText) findViewById(R.id.employ_die_edit);
		tick_lose_edit = (EditText) findViewById(R.id.tick_lose_edit);
		note_edit = (EditText) findViewById(R.id.note_edit);
		
		KeyworkRholder holder=pHelper.getData();
		passage_edit.setText(holder.getPassenger_cnt());
		tick_edit.setText(holder.getPassenger_rcpt());
		passager_die_edit.setText(holder.getPassenger_miss());
		pakage_num_edit.setText(holder.getPacket_cnt());
		
		mela_edit.setText(holder.getCatering_rcpt());
		employ_die_edit.setText(holder.getWorker_miss());
		tick_lose_edit.setText(holder.getReceipts_miss());
		note_edit.setText(holder.getNotes());

		passage_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.PASSENGER_CNT,
								passage_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		tick_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.PASSENGER_RCPT,
								tick_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		passager_die_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.PASSENGER_MISS,
								passager_die_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		pakage_num_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.PACKET_CNT,
								pakage_num_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		mela_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.CATERING_RCPT,
								mela_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		employ_die_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.WORKER_MISS,
								employ_die_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		tick_lose_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.RECEIPTS_MISS,
								tick_lose_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		note_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.NOTES,
								note_edit.getText().toString()).commit();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}
}
