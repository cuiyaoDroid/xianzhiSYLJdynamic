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

		public static String mTextviewArray[] = { "������Ϣ", "�������", "�ص㹤��",
				"�����¼", "��ȫ���", "���ļ�¼"};

		@SuppressWarnings("rawtypes")
		public static Class mTabClassArray[] = { TrainInfoActivity.class,
				GroupInfoActivity.class, ImportWorkEditActivity.class,//ImportWorkInfoActivity.class,
				NoteEditActivity.class, SecurityMainActivity.class,
				ReviewInfoActivity.class };
	}
}