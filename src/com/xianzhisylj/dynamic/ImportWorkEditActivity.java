package com.xianzhisylj.dynamic;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class ImportWorkEditActivity extends Activity{
	private EditText import_input_edit;
	private TextView num_txt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importwork_edit);
		import_input_edit=(EditText)findViewById(R.id.import_input_edit);
		num_txt=(TextView)findViewById(R.id.num_txt);
		import_input_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				num_txt.setText("还可以输入"+(240-import_input_edit.getText().toString().length())+"个字");
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
