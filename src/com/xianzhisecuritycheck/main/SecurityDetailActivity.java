package com.xianzhisecuritycheck.main;

import java.io.File;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.web.image.ImageDownloader;
import com.xianzhi.stool.DensityUtil;
import com.xianzhi.stool.FileNameTool;
import com.xianzhi.tool.db.PatrolHelper;
import com.xianzhi.tool.db.PatrolHolder;
import com.xianzhi.tool.view.ImageDialog;
import com.xianzhi.tool.view.ReadView;
import com.xianzhi.tool.view.VideoPlayerDialog;
import com.xianzhi.webtool.DownloadAsyncTask;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhi.webtool.downloadCallbackListener;
import com.xianzhisylj.dynamic.R;

public class SecurityDetailActivity extends Activity implements OnClickListener {
	private ImageButton goback_btn;
	private TextView time_txt;
	private TextView checker_txt;
	private ReadView content_txt;
	private TextView result_txt;
	// private TextView time2_txt;
	private LinearLayout Img_list;
	private LinearLayout page_layout;
	private RelativeLayout checker_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_detail_page);
		goback_btn = (ImageButton) findViewById(R.id.goback_btn);
		goback_btn.setOnClickListener(this);
		downloader = new ImageDownloader(getApplicationContext(), 1);
		downloader2 = new ImageDownloader(getApplicationContext(), 0);
		initContentView();
	}

	private PatrolHolder holder;

	@SuppressLint("SimpleDateFormat")
	private void initContentView() {
		time_txt = (TextView) findViewById(R.id.time_txt);
		// time2_txt = (TextView) findViewById(R.id.time2_txt);
		content_txt = (ReadView) findViewById(R.id.content_txt);
		checker_txt = (TextView) findViewById(R.id.checker_content);
		page_layout = (LinearLayout) findViewById(R.id.page_layout);
		checker_layout = (RelativeLayout) findViewById(R.id.checker_layout);
		// result_txt = (TextView) findViewById(R.id.result_txt);
		Img_list = (LinearLayout) findViewById(R.id.Img_list);
		int id = getIntent().getIntExtra("id", -1);
		if (id == -1) {
			return;
		}
		PatrolHelper helper = new PatrolHelper(getApplicationContext());
		holder = helper.selectData_Id(id);
		helper.close();
		if(holder==null){
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
		time_txt.setText(format2.format(holder.getCreatTime()));
		// time2_txt.setText(format2.format(holder.getCreatTime()));
		;
		
		String result_typeName=holder.getResult_typeId_name();
		if(result_typeName.equals("问题")){
			content_txt.setText(Html.fromHtml("<font color=#2E79C3>\u3000\u3000["
					+holder.getType_name()+"] </font>" + holder.getContent().replaceAll("\n", "<br />")+" <font color=#FF0000><small>"
							+holder.getResult_typeId_name()+"</small></font>"));
		}else if(result_typeName.equals("表扬")){
			content_txt.setText(Html.fromHtml("<font color=#2E79C3>\u3000\u3000["
					+holder.getType_name()+"] </font>" + holder.getContent().replaceAll("\n", "<br />")+" <font color=#FF6000><small>"
							+holder.getResult_typeId_name()+"</small></font>"));
		}else{
			content_txt.setText(Html.fromHtml("<font color=#2E79C3>\u3000\u3000["
					+holder.getType_name()+"] </font>" + holder.getContent().replaceAll("\n", "<br />")+" <font color=#248B00><small>"
							+holder.getResult_typeId_name()+"</small></font>"));
		}
		content_txt.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int height = content_txt.getTop() + content_txt.lineNum
						* content_txt.getLineHeight();
				if (content_txt.getText().toString().length() - 2 < holder
						.getContent().length()) {
					String nextpage = holder.getContent().substring(
							content_txt.getText().toString().length() - 2);
					checker_layout.setVisibility(View.GONE);
					addPageMiddle(nextpage);
				} else {
					if (DensityUtil.dip2px(getApplicationContext(), 600 - 400) > height) {
						String[] imgs = holder.getImg().split(";");
						Log.i("height",
								DensityUtil.dip2px(getApplicationContext(),
										600 - 400) + "  " + height);
						Img_list.removeAllViews();
						if (holder.getImg().length() > 0) {
							addImage(imgs, Img_list);
						}
						if (holder.getVideoData().length() > 0) {
							addVideo(holder.getVideoData(),Img_list);
						}
						if (DensityUtil.dip2px(getApplicationContext(),
								600 - 400 - 60) > height) {
							checker_layout.setVisibility(View.VISIBLE);
							String result_content = holder.getResult();
							addPageEnd(result_content, false);
						} else {
							checker_layout.setVisibility(View.GONE);
							String result_content = holder.getResult();
							addPageEnd(result_content, true);
						}
					}else{
						checker_layout.setVisibility(View.GONE);
						addPageMiddle("");
					}
					
				}
			}
		});
		String str_checker = "<font color=red>检查人：</font>"
				+ holder.getUserName() + "<br></br>"
				+ "<font color=red>责任人：</font>"
				+ holder.getResponsiblePersonName().trim() + "<br></br>"
				+ "<font color=red>整改期限：</font>"
				+ format.format(holder.getCompleteTime());
		checker_txt.setText(Html.fromHtml(str_checker));
		// result_txt.setText(holder.getResult().length() > 0 ? Html
		// .fromHtml(result_content) : "暂无");

		// if (holder.getVideoData().length() > 0) {
		// addVideo(holder.getVideoData());
		// }
	}

	private void addPageEnd(String content, boolean result) {
		View cell = getLayoutInflater().inflate(R.layout.cell_page_end, null);
		result_txt = (TextView) cell.findViewById(R.id.result_txt);
		RelativeLayout checker_layout = (RelativeLayout) cell
				.findViewById(R.id.checker_layout);
		checker_layout.setVisibility(result ? View.VISIBLE : View.GONE);
		if (result) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			TextView checkerTxt = (TextView) cell
					.findViewById(R.id.checker_content);
			String str_checker = "<font color=red>检查人：</font>"
					+ holder.getUserName() + "<br></br>"
					+ "<font color=red>责任人：</font>"
					+ holder.getResponsiblePersonName().trim() + "<br></br>"
					+ "<font color=red>整改期限：</font>"
					+ format.format(holder.getCompleteTime());
			checkerTxt.setText(Html.fromHtml(str_checker));
		}
		result_txt
				.setText(content.length() > 0 ? Html.fromHtml(content) : "暂无");
		page_layout.addView(cell);
	}

	private void addPageMiddle(final String content) {
		View cell = getLayoutInflater().inflate(R.layout.cell_page, null);
		final ReadView contentTVT = (ReadView) cell.findViewById(R.id.content);
		final LinearLayout cell_layout = (LinearLayout) cell
				.findViewById(R.id.cell_layout);
		final RelativeLayout checkerLayout = (RelativeLayout) cell
				.findViewById(R.id.checker_layout);
		final TextView checkerTxt = (TextView) cell
				.findViewById(R.id.checker_content);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str_checker = "<font color=red>检查人：</font>"
				+ holder.getUserName() + "<br></br>"
				+ "<font color=red>责任人：</font>"
				+ holder.getResponsiblePersonName().trim() + "<br></br>"
				+ "<font color=red>整改期限：</font>"
				+ format.format(holder.getCompleteTime());
		checkerTxt.setText(Html.fromHtml(str_checker));
		contentTVT.setText(content);
		contentTVT.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int height = contentTVT.getTop() + contentTVT.lineNum
						* contentTVT.getLineHeight();
				Log.i("first", height + " " + contentTVT.getLineCount() + " "
						+ contentTVT.getLineHeight());
				if (contentTVT.getText().toString().length() < content.length()) {
					String nextpage = content.substring(contentTVT.getText()
							.toString().length());
					addPageMiddle(nextpage);
				} else {

					if (DensityUtil.dip2px(getApplicationContext(), 600 - 400) > height) {
						String[] imgs = holder.getImg().split(";");
						Log.i("height",
								DensityUtil.dip2px(getApplicationContext(),
										600 - 400) + "  " + height);
						cell_layout.removeAllViews();
						if (holder.getImg().length() > 0) {
							addImage(imgs, cell_layout);
						}
						if (holder.getVideoData().length() > 0) {
							addVideo(holder.getVideoData(),cell_layout);
						}
						if (DensityUtil.dip2px(getApplicationContext(),
								600 - 400 - 60) > height) {
							checkerLayout.setVisibility(View.VISIBLE);
							String result_content = holder.getResult();
							addPageEnd(result_content, false);
						} else {
							checkerLayout.setVisibility(View.GONE);
							String result_content = holder.getResult();
							addPageEnd(result_content, true);
						}
					} else {
						addPageMiddle("");
					}

				}
			}
		});
		page_layout.addView(cell);
	}

	private ImageDownloader downloader;
	private ImageDownloader downloader2;
	String clickedPath = "";

	private void addImage(String[] imgs, LinearLayout v_list) {
		View cell = getLayoutInflater().inflate(R.layout.cell_image, null);
		final ImageView image = (ImageView) cell.findViewById(R.id.image);
		LinearLayout image_preview = (LinearLayout) cell
				.findViewById(R.id.image_preview);
		final ProgressBar progressBar=(ProgressBar)cell.findViewById(R.id.progressBar);
		
		int i = 0;
		for (final String imgpath : imgs) {
			if (i == 0) {
				downloader.download(HttpJsonTool.ServerUrl + imgpath, image,progressBar);
				clickedPath = HttpJsonTool.ServerUrl + imgpath;
			}
			View picView = getLayoutInflater().inflate(R.layout.cell_pre_pic,
					null);
			final ImageView picImg = (ImageView) picView
					.findViewById(R.id.pic_img);
			downloader2.download(HttpJsonTool.ServerUrl +"/s"+ imgpath, picImg);
			picView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					downloader
							.download(HttpJsonTool.ServerUrl + imgpath, image,progressBar);
					clickedPath = HttpJsonTool.ServerUrl + imgpath;
				}
			});
			image_preview.addView(picView);
			i++;
		}
		cell.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (clickedPath.length() == 0) {
					return;
				}
				ImageDialog imageDialog = new ImageDialog(
						SecurityDetailActivity.this, R.style.audioDialog,
						clickedPath, downloader);
				imageDialog.setCanceledOnTouchOutside(false);
				imageDialog.show();
			}
		});

		v_list.addView(cell);
	}

	private boolean downloading = false;

	private void downloadVideo(ProgressBar progressBar, final String videopath) {
		downloading = true;
		DownloadAsyncTask task = new DownloadAsyncTask(progressBar);
		progressBar.setVisibility(View.VISIBLE);
		Log.i("surfaceHolder", "downloading");
		task.setdownloadCallbackListener(new downloadCallbackListener() {
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				String Path = SecurityCheckApp.appPath
						+ FileNameTool.getExtensionName(videopath);
				if (new File(Path).exists()) {
					playVideo(Path);
				} else {
					Toast.makeText(getApplicationContext(), "下载文件出错，请检查网络",
							Toast.LENGTH_LONG).show();
				}
				downloading = false;
			}
		});
		task.execute(videopath);
	}

	private void addVideo(final String videopath,LinearLayout layout) {
		View cell = getLayoutInflater().inflate(R.layout.cell_video_line,
				null);
		final ProgressBar progressBar = (ProgressBar) cell
				.findViewById(R.id.progressBar);
		final TextView videoline = (TextView) cell
				.findViewById(R.id.video_text);
		videoline.setText(R.string.downloadvideo);
		videoline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String videoPath = SecurityCheckApp.appPath
						+ FileNameTool.getExtensionName(videopath);
				if (!new File(videoPath).exists()) {
					if (!downloading) {
						downloadVideo(progressBar, videopath);
					}
				} else {
					playVideo(videoPath);
				}
			}
		});
		layout.addView(cell);
	}

	VideoPlayerDialog videoDialog;

	public void playVideo(String videoPath) {
		videoDialog = new VideoPlayerDialog(SecurityDetailActivity.this,
				R.style.audioDialog, videoPath);
		videoDialog.setCanceledOnTouchOutside(false);
		videoDialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goback_btn:
			finish();
			break;
		default:
			break;
		}
	}

}
