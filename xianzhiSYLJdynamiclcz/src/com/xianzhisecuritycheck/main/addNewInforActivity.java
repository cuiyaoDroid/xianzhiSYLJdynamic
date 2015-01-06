package com.xianzhisecuritycheck.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.web.image.ImageFileCache;
import com.example.web.image.PictureUtil;
import com.xianzhi.tool.db.MapHolder;
import com.xianzhi.tool.db.PatrolHelper;
import com.xianzhi.tool.db.PatrolHolder;
import com.xianzhi.tool.view.CustomTimeSeterDialog;
import com.xianzhi.tool.view.SelectPicPopupWindow;
import com.xianzhi.webtool.HttpJsonTool;
import com.xianzhialarm.listener.TimeDialogListener;
import com.xianzhisylj.dynamiclcz.R;

public class addNewInforActivity extends Activity implements OnClickListener {
	private ImageButton gobackBtn;
	private ImageButton addNewBtn;
	private LinearLayout picLayout;
	private EditText contentEidt;
	private ImageButton newPersonBtn;
	private ImageButton newTimeBtn;
	private SelectPicPopupWindow popupWin = null;
	private LinearLayout userList;
	private TextView timeTxt;
	private ImageFileCache filecache;
	private ImageView videoImg;
	private ArrayList<Map<String, Object>> MediaData;

	private EditText stateEdit;
	private EditText typeEdit;
	private ImageButton stateBtn;
	private ImageButton typeBtn;

	public static void hideKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnew);
		MediaData = new ArrayList<Map<String, Object>>();
		gobackBtn = (ImageButton) findViewById(R.id.goback_btn);
		gobackBtn.setOnClickListener(this);
		addNewBtn = (ImageButton) findViewById(R.id.right_btn);
		addNewBtn.setOnClickListener(this);
		filecache = new ImageFileCache();
		typedata = new ArrayList<String>();
		result_typedata = new ArrayList<String>();
		initContentView();
		initProgressDialog();
	}

	private void initContentView() {
		picLayout = (LinearLayout) findViewById(R.id.pic_layout);
		addMediaContentView();
		contentEidt = (EditText) findViewById(R.id.content_edit);
		newPersonBtn = (ImageButton) findViewById(R.id.new_person_btn);
		newPersonBtn.setOnClickListener(this);
		newTimeBtn = (ImageButton) findViewById(R.id.new_time_btn);
		newTimeBtn.setOnClickListener(this);
		userList = (LinearLayout) findViewById(R.id.user_list);
		timeTxt = (TextView) findViewById(R.id.time_text);

		videoImg = (ImageView) findViewById(R.id.video_img);
		videoImg.setOnClickListener(this);
		stateEdit = (EditText) findViewById(R.id.state_edit);
		stateEdit.setOnClickListener(this);
		typeEdit = (EditText) findViewById(R.id.type_edit);
		typeEdit.setOnClickListener(this);
		stateBtn = (ImageButton) findViewById(R.id.state_btn);
		stateBtn.setOnClickListener(this);
		typeBtn = (ImageButton) findViewById(R.id.type_btn);
		typeBtn.setOnClickListener(this);
	}

	private Map<Integer, String> users = null;

	private void refreshUserView() {
		if (users == null) {
			return;
		}
		userList.removeAllViews();
		Iterator<Integer> userIterator = users.keySet().iterator();
		while (userIterator.hasNext()) {
			final int vid = userIterator.next();
			View view = getLayoutInflater().inflate(R.layout.cell_usertop_list,
					null);
			TextView txt = (TextView) view.findViewById(R.id.name_txt);
			final String[] user = users.get(vid).split(" ");
			txt.setText(user[0]);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					users.remove(vid);
					refreshUserView();
				}
			});
			userList.addView(view);
		}
	}

	/**
	 * 检查存储卡是否插入
	 * 
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/** 录制视频 **/
	public void startrecord() {
		Intent mIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		mIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
		startActivityForResult(mIntent, VIDEO_TAKE);
	}

	// 删除在/sdcard/dcim/Camera/默认生成的文件
	private Bitmap deleteDefaultFile(Uri uri) {
		String fileName = null;
		Bitmap bitmap = null;
		if (uri != null) {
			// content
			Log.d("Scheme", uri.getScheme().toString());
			if (uri.getScheme().toString().equals("content")) {
				Cursor cursor = this.getContentResolver().query(uri, null,
						null, null, null);
				if (cursor.moveToNext()) {
					int columnIndex = cursor
							.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
					fileName = cursor.getString(columnIndex);
					// 获取缩略图id
					int id = cursor.getInt(cursor
							.getColumnIndex(VideoColumns._ID));
					// 获取缩略图
					bitmap = Thumbnails.getThumbnail(getContentResolver(), id,
							Thumbnails.MICRO_KIND, null);

					Log.d("fileName", fileName);
				}
			}
		}
		// 删除文件
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
			Log.d("delete", "删除成功");
		}
		return bitmap;
	}

	private String cramepath = "";
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.photo_btn:
				Intent intent = new Intent();
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, IMAGEPATH_REQUEST);
				popupWin.dismiss();
				break;
			case R.id.camera_btn:
				Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (isHasSdcard()) {
					cramepath = System.currentTimeMillis() + ".jpg";
					camera.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(SecurityCheckApp.appPath
									+ cramepath)));
				} else {
					Toast.makeText(getApplicationContext(), "没有找到sd卡",
							Toast.LENGTH_LONG).show();
				}
				startActivityForResult(camera, CAMERA_TAKE);
				popupWin.dismiss();
				break;
			case R.id.video_btn:
				if (video_position != -1) {
					showReplaceDialog(REPLACE_DIALOG);
				} else {
					startrecord();
				}
				popupWin.dismiss();
			case R.id.cancel_btn:
				popupWin.dismiss();
			default:
				break;
			}

		}
	};
	final static int REPLACE_DIALOG = 0;

	public void showReplaceDialog(int dialogId) {
		Dialog dialog = null;
		switch (dialogId) {
		case REPLACE_DIALOG:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
					addNewInforActivity.this);
			alertBuilder
					.setTitle("")
					.setMessage("每次发布，只能有一段视频，确认替换？")
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									startrecord();
									dialog.dismiss();
								}
							});
			dialog = alertBuilder.create();
			break;

		}
		dialog.show();
	}

	public static final int IMAGEPATH_REQUEST = 1000;
	public static final int CAMERA_TAKE = 1001;
	public static final int VIDEO_TAKE = 1002;
	public static final int VIDEO_RECORDER = 1003;
	// private Map<Integer, String> imageuri;
	private String videoPath = "";
	private int video_position = -1;

	private void addMediaContentView() {
		if (MediaData.size() < 4
				&& (position >= MediaData.size() - 1 || position == -1)) {
			Map<String, Object> mData = new HashMap<String, Object>();
			mData.put("bitmap", null);
			mData.put("imgpath", "");
			mData.put("video", "");
			MediaData.add(mData);
		}
		refreshpicView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IMAGEPATH_REQUEST:
			if (resultCode == RESULT_OK && data != null) {
				try {
					Uri uri = data.getData();

					String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
					Cursor cursor = this.managedQuery(uri, imgs, null, null,
							null);
					int index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(index);
					File file = new File(path);
					Bitmap bitmap;
					if (file.length() > 1024 * 1024 * 2) {
						bitmap = PictureUtil.getimage(path, 480, 800,
								1024 * 1024 * 2);
					} else {
						bitmap = PictureUtil.getSmallBitmap(path);
					}
					Map<String, Object> mData = new HashMap<String, Object>();
					if (position != -1) {
						String savepath = SecurityCheckApp.appPath
								+ System.currentTimeMillis() + ".jpg";
						filecache.saveBitmap(bitmap, savepath, "");
						mData.put("bitmap", bitmap);
						mData.put("imgpath", savepath);
						mData.put("video", "");
						MediaData.remove(position);
						MediaData.add(position, mData);
						if (video_position == position) {
							video_position = -1;
						}
						addMediaContentView();
					}
				} catch (Exception e) {
					Log.e("Exception", e.getMessage(), e);
				}

			}
			break;
		case CAMERA_TAKE:
			Log.i("CAMERA_TAKE", "CAMERA_TAKE");
			if (resultCode == RESULT_OK) {
				try {
					if (position != -1) {
						Map<String, Object> mData = new HashMap<String, Object>();
						if (cramepath.length() > 0) {
							String savePath = SecurityCheckApp.appPath + "j_"
									+ cramepath;
							ImageFileCache.compressImage(
									SecurityCheckApp.appPath + cramepath,
									savePath);
							Bitmap bitmap = filecache.getImage(savePath, "");
							mData.put("bitmap", bitmap);
							mData.put("imgpath", savePath);
							mData.put("video", "");
							MediaData.remove(position);
							MediaData.add(position, mData);
							if (video_position == position) {
								video_position = -1;
							}
						}
						addMediaContentView();
					}
				} catch (Exception e) {
					Log.e("Exception", e.getMessage(), e);
				}
			}
			break;
		case AddnotifiUser:
			if (data != null) {
				MapHolder holder = (MapHolder) data
						.getSerializableExtra("users");
				Map<Integer, String> users = holder.getUsers();
				this.users = users;
				refreshUserView();
			}
			break;
		case VIDEO_TAKE:
			try {
				if (resultCode == RESULT_OK && data != null) {
					if (position != -1) {
						AssetFileDescriptor videoAsset = getContentResolver()
								.openAssetFileDescriptor(data.getData(), "r");
						FileInputStream fis = videoAsset.createInputStream();
						if (videoPath.length() > 0) {
							new File(videoPath).delete();
						}
						videoPath = SecurityCheckApp.appPath
								+ System.currentTimeMillis() + ".mp4";
						File tmpFile = new File(videoPath);
						FileOutputStream fos = new FileOutputStream(tmpFile);
						byte[] buf = new byte[1024];
						int len;
						while ((len = fis.read(buf)) > 0) {
							fos.write(buf, 0, len);
						}
						fis.close();
						fos.close();
						Bitmap bitmap = deleteDefaultFile(data.getData());
						Map<String, Object> mData = new HashMap<String, Object>();
						mData.put("bitmap", bitmap);
						mData.put("imgpath", "");
						mData.put("video", "video");
						MediaData.remove(position);
						MediaData.add(position, mData);
						if (video_position != -1 && video_position != position) {
							MediaData.remove(video_position);
						}
						addMediaContentView();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private ProgressDialog progressDialog;

	private void initProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("");
		progressDialog.setMessage("请稍等...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
	}

	private void sendPatrol(final PatrolHolder holder) {
		progressDialog.show();
		AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().sendPatrol(holder,
						progressDialog);
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog.dismiss();
				if (HttpJsonTool.state403) {
					Toast.makeText(getApplicationContext(), "账号在其他设备上登录，请重新登录",
							Toast.LENGTH_LONG).show();
					HttpJsonTool.state403 = false;
					gotoLoginView();
					return;
				}
				if (result != -1) {
					Toast.makeText(getApplicationContext(), "发布成功",
							Toast.LENGTH_LONG).show();
					holder.setId(result);
					PatrolHelper helper = new PatrolHelper(
							getApplicationContext());
					helper.insert(holder);
					helper.close();
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "发布失败，请检查网络",
							Toast.LENGTH_LONG).show();
				}
			}
		};
		task.execute();
	}

	private void gotoLoginView() {
		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			ComponentName componentName = new ComponentName(
					"com.xianzhi.rail.cc", "com.xianzhi.rail.cc.LogoActivity");
			intent.setComponent(componentName);
			startActivity(intent);
			overridePendingTransition(0, 0);
			finish();
		} catch (Exception e) {

		}
	}

	private int position = -1;

	private void refreshpicView() {
		picLayout.removeAllViews();
		for (int i = 0; i < MediaData.size(); i++) {
			final int key = i;
			View picView = getLayoutInflater().inflate(R.layout.cell_pic_view,
					null);
			final ImageView picImg = (ImageView) picView
					.findViewById(R.id.pic_img);
			Bitmap bitmap = (Bitmap) MediaData.get(i).get("bitmap");
			String video = (String) MediaData.get(i).get("video");
			if (video.equals("video")) {
				video_position = key;
			}
			if (bitmap != null) {
				picImg.setImageBitmap(bitmap);
			} else {
			}
			picView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideKeyboard(addNewInforActivity.this, contentEidt);
					if (popupWin == null) {
						popupWin = new SelectPicPopupWindow(
								getApplicationContext(), itemsOnClick);
					}
					popupWin.showAtLocation(addNewInforActivity.this
							.findViewById(R.id.addnew_layout), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
					position = key;
				}
			});
			picLayout.addView(picView);
		}

	}

	public static final int AddnotifiUser = 12324;
	private long completeTime = 0;

	private void commit() {
		if (contentEidt.getText().toString().length() == 0) {
			Toast.makeText(getApplicationContext(), "问题描述不能为空",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (completeTime == 0) {
			Toast.makeText(getApplicationContext(), "请设置整改时间",
					Toast.LENGTH_LONG).show();
			return;
		}
		Iterator<Integer> userIterator = users.keySet().iterator();
		String userDisplay = "";
		String toUserIds = "";
		while (userIterator.hasNext()) {
			int id = userIterator.next();
			final String[] user = users.get(id).split(" ");
			userDisplay += user[0] + ",";
			id -= 100;
			toUserIds += id + ",";
		}
		if (userDisplay.length() > 0) {
			userDisplay = userDisplay.substring(0, userDisplay.length() - 1);
		}
		if (toUserIds.length() > 0) {
			toUserIds = toUserIds.substring(0, toUserIds.length() - 1);
		}
		if (toUserIds.length() == 0) {
			Toast.makeText(getApplicationContext(), "请设置责任人", Toast.LENGTH_LONG)
					.show();
			return;
		}
		String img = "";
		for (Map<String, Object> mData : MediaData) {
			if (((String) mData.get("imgpath")).length() > 0) {
				img += ((String) mData.get("imgpath")) + ";";
			}
		}
		if (img.length() > 0) {
			img = img.substring(0, img.length() - 1);
		}
		if(type_id==-1){
			Toast.makeText(getApplicationContext(), "请选择发布类型", Toast.LENGTH_LONG).show();
			return;
		}
		if(result_type_id==-1){
			Toast.makeText(getApplicationContext(), "请选择问题类型", Toast.LENGTH_LONG).show();
			return;
		}
		String type_name=typeEdit.getText().toString();
		String result_type_name=stateEdit.getText().toString();
		PatrolHolder holder = new PatrolHolder(-1, contentEidt.getText()
				.toString(), img, videoPath, completeTime, toUserIds, 0,
				SecurityCheckApp.loginName, userDisplay, "",
				SecurityCheckApp.userId,type_id,type_name,result_type_id,result_type_name);
		sendPatrol(holder);
	}

	List<String> typedata;
	ArrayList<Map<String, Object>> mapdata;
	int type_id = -1;

	List<String> result_typedata;
	ArrayList<Map<String, Object>> result_mapdata;
	int result_type_id = -1;
	private PopupWindow popuplistwin;

	private void showTypePopupListWin(final EditText v) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwin_list, null);
		final ListView poplist = (ListView) view.findViewById(R.id.pop_list);
		final ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar);
		popuplistwin = new PopupWindow(view, v.getWidth(),
				LayoutParams.WRAP_CONTENT);
		if (typedata == null) {
			return;
		}
		if (typedata.size() > 0 && mapdata != null) {
			progressBar.setVisibility(View.GONE);
			poplist.setAdapter(new ArrayAdapter<String>(
					addNewInforActivity.this, R.layout.cell_popuplist_simple,
					typedata));
			poplist.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int location, long arg3) {
					// TODO Auto-generated method stub
					v.setText(typedata.get(location));
					type_id=(Integer)(mapdata.get(location).get("type_id"));
					popuplistwin.dismiss();
				}
			});
		} else {
			progressBar.setVisibility(View.VISIBLE);
			final AsyncTask<Void, Void, ArrayList<Map<String, Object>>> task = new AsyncTask<Void, Void, ArrayList<Map<String, Object>>>() {
				@Override
				protected ArrayList<Map<String, Object>> doInBackground(
						Void... params) {
					// TODO Auto-generated method stub
					return HttpJsonTool.getInstance().getAllType();
				}

				@Override
				protected void onPostExecute(
						ArrayList<Map<String, Object>> result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					progressBar.setVisibility(View.GONE);
					if (HttpJsonTool.state403) {
						Toast.makeText(getApplicationContext(),
								"账号在其他设备上登录，请重新登录", Toast.LENGTH_LONG).show();
						HttpJsonTool.state403 = false;
						gotoLoginView();
						return;
					}
					typedata.clear();
					for (Map<String, Object> data : result) {
						typedata.add((String) data.get("type_name"));
					}
					mapdata=result;
					poplist.setAdapter(new ArrayAdapter<String>(
							addNewInforActivity.this,
							R.layout.cell_popuplist_simple, typedata));
					poplist.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int location, long arg3) {
							// TODO Auto-generated method stub
							v.setText(typedata.get(location));
							type_id=(Integer)(mapdata.get(location).get("type_id"));
							popuplistwin.dismiss();
						}
					});
				}
			};
			task.execute();
			popuplistwin.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					task.cancel(true);
				}
			});
		}
		
		popuplistwin.update();
		popuplistwin.setFocusable(true);
		popuplistwin.setTouchable(true);
		popuplistwin.setOutsideTouchable(true);
		popuplistwin.setBackgroundDrawable(new BitmapDrawable());
		popuplistwin.showAsDropDown(v);
	}

	private void showResultTypePopupListWin(final EditText v) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popwin_list, null);
		final ListView poplist = (ListView) view.findViewById(R.id.pop_list);
		final ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressBar);
		popuplistwin = new PopupWindow(view, v.getWidth(),
				LayoutParams.WRAP_CONTENT);
		if (result_typedata == null) {
			return;
		}
		if (result_typedata.size() > 0 && result_mapdata != null) {
			progressBar.setVisibility(View.GONE);
			poplist.setAdapter(new ArrayAdapter<String>(
					addNewInforActivity.this, R.layout.cell_popuplist_simple,
					result_typedata));
			poplist.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int location, long arg3) {
					// TODO Auto-generated method stub
					v.setText(result_typedata.get(location));
					result_type_id=(Integer)(result_mapdata.get(location).get("result_type_id"));
					popuplistwin.dismiss();
				}
			});
		} else {
			progressBar.setVisibility(View.VISIBLE);
			final AsyncTask<Void, Void, ArrayList<Map<String, Object>>> task = new AsyncTask<Void, Void, ArrayList<Map<String, Object>>>() {
				@Override
				protected ArrayList<Map<String, Object>> doInBackground(
						Void... params) {
					// TODO Auto-generated method stub
					return HttpJsonTool.getInstance().getAllResultType();
				}

				@Override
				protected void onPostExecute(
						ArrayList<Map<String, Object>> result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					progressBar.setVisibility(View.GONE);
					if (HttpJsonTool.state403) {
						Toast.makeText(getApplicationContext(),
								"账号在其他设备上登录，请重新登录", Toast.LENGTH_LONG).show();
						HttpJsonTool.state403 = false;
						gotoLoginView();
						return;
					}
					result_typedata.clear();
					for (Map<String, Object> data : result) {
						result_typedata.add((String) data.get("result_type_name"));
					}
					result_mapdata=result;
					poplist.setAdapter(new ArrayAdapter<String>(
							addNewInforActivity.this,
							R.layout.cell_popuplist_simple, result_typedata));
					poplist.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int location, long arg3) {
							// TODO Auto-generated method stub
							v.setText(result_typedata.get(location));
							result_type_id=(Integer)(result_mapdata.get(location).get("result_type_id"));
							popuplistwin.dismiss();
						}
					});
				}
			};
			task.execute();
			popuplistwin.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					task.cancel(true);
				}
			});
		}
		
		popuplistwin.update();
		popuplistwin.setFocusable(true);
		popuplistwin.setTouchable(true);
		popuplistwin.setOutsideTouchable(true);
		popuplistwin.setBackgroundDrawable(new BitmapDrawable());
		popuplistwin.showAsDropDown(v);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goback_btn:
			finish();
			break;
		case R.id.right_btn:
			commit();
			break;
		case R.id.new_person_btn:
			Intent per_intent = new Intent(getApplicationContext(),
					AddnotifiUserListActivity.class);
			startActivityForResult(per_intent, AddnotifiUser);
			break;
		case R.id.new_time_btn:
			CustomTimeSeterDialog dialog = new CustomTimeSeterDialog(
					addNewInforActivity.this, R.style.customDialog,
					R.layout.dialog_time_seter, System.currentTimeMillis());
			dialog.setTimeDialogListener(new TimeDialogListener() {
				@Override
				public void getTimeInMill(long time) {
					// TODO Auto-generated method stub
					completeTime = time;
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					timeTxt.setText("限定" + format.format(time) + "前上报整改措施。");
				}
			});
			dialog.show();
			break;
		case R.id.video_img:
			// startrecord();
			break;
		case R.id.type_edit:
		case R.id.type_btn:
			showTypePopupListWin(typeEdit);
			break;
		case R.id.state_edit:
		case R.id.state_btn:
			showResultTypePopupListWin(stateEdit);
			break;
		default:
			break;
		}
	}
}
