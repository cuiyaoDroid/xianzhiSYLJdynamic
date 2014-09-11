package com.xianzhisylj.dynamic;

import com.xianzhi.tool.db.KeyworkAndRecordHelper;
import com.xianzhi.webtool.HttpJsonTool;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class ImportWorkEditActivity extends Activity {
	private EditText import_input_edit;
	private TextView num_txt;
	private SharedPreferences userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importwork_edit);
		int trainId = getIntent().getIntExtra("id", -1);
		if (trainId == -1) {
			return;
		}
		userInfo = getSharedPreferences(HttpJsonTool.userId + "__" + trainId, 0);

		import_input_edit = (EditText) findViewById(R.id.import_input_edit);
		String context = userInfo.getString(KeyworkAndRecordHelper.CONTEXT, "");
		import_input_edit.setText(context);
		
		num_txt = (TextView) findViewById(R.id.num_txt);
		import_input_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				num_txt.setText("还可以输入"
						+ (240 - import_input_edit.getText().toString()
								.length()) + "个字");

				userInfo.edit()
						.putString(KeyworkAndRecordHelper.CONTEXT,
								import_input_edit.getText().toString())
						.commit();
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
