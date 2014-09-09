package com.xianzhi.webtool;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;


public class MyMultipartEntity extends MultipartEntity {

	private ProgressListener listener;

	public MyMultipartEntity() {
		super();

	}

	//��Ӽ��ӽӿ�
	
	public void setPropressListener(final ProgressListener listener) {
		this.listener = listener;

	}

	public MyMultipartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public MyMultipartEntity(HttpMultipartMode mode, final String boundary,
			final Charset charset, final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	//�ϴ�ʱ����ӹ�������ʵ�ֽ�������
	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener {
		void transferred(long num);
	}

	//�ڲ���Ĺ�����ʵ�������ϴ�����
	public static class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;//��������
		private long transferred; //����

		public CountingOutputStream(final OutputStream out,
				final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		//����
		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}
		//����
		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}
}
