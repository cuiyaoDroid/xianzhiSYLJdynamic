package com.xianzhi.stool;

import com.xianzhisecuritycheck.main.SecurityMainActivity;
import com.xianzhisylj.dynamic.GroupInfoActivity;
import com.xianzhisylj.dynamic.ImportWorkEditActivity;
import com.xianzhisylj.dynamic.ImportWorkInfoActivity;
import com.xianzhisylj.dynamic.NoteEditActivity;
import com.xianzhisylj.dynamic.NoteInfoActivity;
import com.xianzhisylj.dynamic.ReviewInfoActivity;
import com.xianzhisylj.dynamic.TrainInfoActivity;

public class Constant {

	public static final class ConValue {

		public static String mTextviewArray[] = { "车次信息", "编组情况", "重点工作",
				"乘务记录", "安全检查", "审阅记录"};

		@SuppressWarnings("rawtypes")
		public static Class mTabClassArray[] = { TrainInfoActivity.class,
				GroupInfoActivity.class, ImportWorkEditActivity.class,//ImportWorkInfoActivity.class,
				NoteEditActivity.class, SecurityMainActivity.class,
				ReviewInfoActivity.class };
	}
}