package com.xianzhi.webtool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xianzhi.stool.L;
import com.xianzhi.tool.db.ContactListHelper;
import com.xianzhi.tool.db.ContactListHolder;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.DynamicListHolder;
import com.xianzhi.tool.db.KeyworkAndRecordHelper;
import com.xianzhi.tool.db.KeyworkAndRecordHolder;
import com.xianzhi.tool.db.KeyworkRholder;
import com.xianzhi.tool.db.PatrolHelper;
import com.xianzhi.tool.db.PatrolHolder;
import com.xianzhi.tool.db.ReviewHelper;
import com.xianzhi.tool.db.ReviewHolder;
import com.xianzhi.tool.db.SeatInfoHelper;
import com.xianzhi.tool.db.SeatInfoHolder;
import com.xianzhi.tool.db.TrainCoachHelper;
import com.xianzhi.tool.db.TrainCoachHolder;
import com.xianzhi.tool.db.TrainDynamicHelper;
import com.xianzhi.tool.db.TrainDynamicHolder;
import com.xianzhi.tool.db.lock;
import com.xianzhi.webtool.MyMultipartEntity.ProgressListener;
import com.xianzhisecuritycheck.main.SecurityCheckApp;

public class HttpJsonTool {

	public enum LoginInfo {
		correct, webfaild, wronginput, correctnoname
	};

//	public static final String ServerUrl = "http://v.cc-railway.xzh-soft.com:8082/dynamic";
	// public static final String ServerUrl = "http://192.168.1.107:8080";
	public static final String ServerUrl ="https://eaccess.syrailway.cn:8443/mapping/sjznbg/dynamic/";
	// "https://eaccess.syrailway.cn:8443/mapping/sjznbg";
	private static HttpJsonTool httpjsontool = null;
	public static String username = "";
	public static int manageTrain = 0;
	public static int reviewTrain = 0;
	public static int userId = -1;
	public static boolean state403 = false;
	public static final String ERROR = "<error>";
	public static final String SUCCESS = "<success>";
	public static final String ERROR403 = "<error403>";
	public static int train_id=-1;
	public static synchronized HttpJsonTool getInstance() {
		if (httpjsontool == null) {
			httpjsontool = new HttpJsonTool();
		}
		return httpjsontool;
	}

	private static CookieStore cookieInfo = null;

	public void getCookieInfo() {
		Thread th = new Thread() {
			public void run() {
				HttpClient httpClient = HttpsClient.getInstance()
						.getHttpsClient();
				HttpGet httpRequest = new HttpGet(ServerUrl + "/1.jsp");
				try {
					httpClient.execute(httpRequest);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cookieInfo = ((AbstractHttpClient) httpClient).getCookieStore();
			}
		};
		th.start();
	}

	public synchronized String login(String loginName, String password) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl + "/userLogin.json");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("loginName", loginName));
			params.add(new BasicNameValuePair("password", password));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
				SecurityCheckApp.token = "";
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONObject json = jsonObject.getJSONObject("user");
			JSONObject userRight = json.getJSONObject("userRight");
			manageTrain = userRight.optInt("manageTrain");
			reviewTrain = userRight.optInt("reviewTrain");
			userId = json.getInt("id");
			SecurityCheckApp.token = json.getString("token");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	public synchronized String getTrainList(int minId, String tranCode,
			Context context) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/train/list.json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (minId != -1) {
				params.add(new BasicNameValuePair("minId", String
						.valueOf(minId)));
			}
			params.add(new BasicNameValuePair("pageSize", String
					.valueOf(PAGE_SIZE)));
			if (tranCode != null) {
				if (tranCode.length() > 0) {
					params.add(new BasicNameValuePair("trainCode", tranCode));
				}
			}
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONArray list = jsonObject.getJSONArray("trainList");
			insertTrainList(context, list);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	public synchronized String getTrainDynamic(Context context, int train_id) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/train/getTrainDynamic.json?token="
					+ SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", String.valueOf(train_id)));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONArray list = jsonObject.getJSONArray("stationList");
			insertTrainDynamic(context, list, train_id);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	private void insertTrainDynamic(Context context, JSONArray list,
			int train_id) throws JSONException {
		TrainDynamicHelper helper = new TrainDynamicHelper(context);
		helper.delete_train_id(train_id);
		synchronized (lock.Lock) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.beginTransaction();
			for (int i = 0; i < list.length(); i++) {
				JSONObject json = (JSONObject) list.get(i);
				String STATION_NAME = json.optString("STATION_NAME");
				String STATION_NO = json.optString("STATION_NO");
				String TIME_ARRIVAL = json.optString("TIME_ARRIVAL");
				String TIME_ARRIVAL_TD = json.optString("TIME_ARRIVAL_TD");
				String TIME_DEPART = json.optString("TIME_DEPART");
				String TIME_DEPART_TD = json.optString("TIME_DEPART_TD");
				int CUR = json.optInt("CUR");
				int ON_TIME = json.optInt("ON_TIME");
				int PASSED = json.optInt("PASSED");
				int PRE_PASSED = json.optInt("PRE_PASSED");
				int NEXT_PASSED = json.optInt("NEXT_PASSED");

				TrainDynamicHolder holder = new TrainDynamicHolder(-1,train_id,
						STATION_NAME, STATION_NO, TIME_ARRIVAL,
						TIME_ARRIVAL_TD, TIME_DEPART, TIME_DEPART_TD, CUR,
						ON_TIME, PASSED, PRE_PASSED, NEXT_PASSED);
				helper.insert(holder, db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}

	public synchronized String addNewDynamicTrain(Context context,
			String trainCode, String startTime, String currentTeam,
			String teamLength) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/train/save.json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("trainCode", trainCode));
			params.add(new BasicNameValuePair("startTime", startTime));
			params.add(new BasicNameValuePair("currentTeam", currentTeam));
			params.add(new BasicNameValuePair("teamLength", teamLength));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONObject object = jsonObject.getJSONObject("train");
			insertTrainInfo(context, object);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	private void insertTrainList(Context context, JSONArray list)
			throws JSONException {
		DynamicListHelper helper = new DynamicListHelper(context);
		synchronized (lock.Lock) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.beginTransaction();
			for (int i = 0; i < list.length(); i++) {
				JSONObject json = (JSONObject) list.get(i);
				int id = json.optInt("id");
				String board_train_code = json.optString("board_train_code");
				long start_time = json.optLong("start_time");
				String from_station_name = json.optString("from_station_name");
				String to_station_name = json.optString("to_station_name");
				String current_team = json.optString("current_team");
				int driving_status = json.optInt("driving_status");
				int user_id = json.optInt("user_id");
				String job_number = json.optString("job_number");
				String user_name = json.optString("user_name");
				String photo = json.optString("photo");
				String phone = json.optString("phone");
				int department_id = json.optInt("department_id");
				String department_name = json.optString("department_name");
				int position_id = json.optInt("position_id");
				String position_name = json.optString("position_name");
				String team_length = json.optString("team_length");
				int isFinal = json.optInt("isFinal");
				DynamicListHolder holder = new DynamicListHolder(id,
						board_train_code, start_time, from_station_name,
						to_station_name, current_team, driving_status, user_id,
						job_number, user_name, photo, phone, department_id,
						department_name, position_id, position_name,
						team_length, isFinal, 0);
				helper.insert(holder, db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}

	private void insertTrainInfo(Context context, JSONObject json)
			throws JSONException {
		DynamicListHelper helper = new DynamicListHelper(context);
		int id = json.optInt("id");
		String board_train_code = json.optString("board_train_code");
		long start_time = json.optLong("start_time");
		String from_station_name = json.optString("from_station_name");
		String to_station_name = json.optString("to_station_name");
		String current_team = json.optString("current_team");
		int driving_status = json.optInt("driving_status");
		int user_id = json.optInt("user_id");
		String job_number = json.optString("job_number");
		String user_name = json.optString("user_name");
		String photo = json.optString("photo");
		String phone = json.optString("phone");
		int department_id = json.optInt("department_id");
		String department_name = json.optString("department_name");
		int position_id = json.optInt("position_id");
		String position_name = json.optString("position_name");
		String team_length = json.optString("team_length");
		int isFinal = json.optInt("isFinal");
		DynamicListHolder holder = new DynamicListHolder(id, board_train_code,
				start_time, from_station_name, to_station_name, current_team,
				driving_status, user_id, job_number, user_name, photo, phone,
				department_id, department_name, position_id, position_name,
				team_length, isFinal, 0);
		synchronized (lock.Lock) {
			helper.insert(holder, helper.getWritableDatabase());
		}
		helper.close();
	}

	public synchronized String getKeyworkAndRecord(Context context, int trainId) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/keyworkAndRecord/getByTrainId/" + trainId
					+ ".json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("train_id", String
			// .valueOf(trainId)));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONObject crewRecord = jsonObject.getJSONObject("crewRecord");
			JSONObject keywork = jsonObject.getJSONObject("keywork");
			insertKeyworkAndRecord(context, crewRecord, keywork);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	public synchronized String saveKeyworkAndRecord(Context context,
			KeyworkRholder holder) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/keyworkAndRecord/save.json?token="
					+ SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("trainId", holder.getTrain_id()));
			params.add(new BasicNameValuePair("context", holder.getContext()));
			params.add(new BasicNameValuePair("passengerCount", holder
					.getPassenger_cnt()));
			params.add(new BasicNameValuePair("packetCount", holder
					.getPacket_cnt()));
			params.add(new BasicNameValuePair("passengerReceipts", holder
					.getPassenger_rcpt()));
			params.add(new BasicNameValuePair("cateringReceipts", holder
					.getCatering_rcpt()));
			params.add(new BasicNameValuePair("passengerMiss", holder
					.getPassenger_miss()));
			params.add(new BasicNameValuePair("receiptsMiss", holder
					.getReceipts_miss()));
			params.add(new BasicNameValuePair("workerMiss", holder
					.getWorker_miss()));
			params.add(new BasicNameValuePair("notes", holder.getNotes()));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONObject crewRecord = jsonObject.getJSONObject("crewRecord");
			JSONObject keywork = jsonObject.getJSONObject("keywork");
			insertKeyworkAndRecord(context, crewRecord, keywork);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	private void insertKeyworkAndRecord(Context context, JSONObject crewRecord,
			JSONObject keywork) {
		KeyworkAndRecordHelper helper = new KeyworkAndRecordHelper(context);
		int train_id = crewRecord.optInt("train_id");
		String strcontext = keywork.optString("context");
		float passenger_cnt = crewRecord.optInt("passenger_cnt");
		float packet_cnt = crewRecord.optInt("packet_cnt");
		float passenger_rcpt = crewRecord.optInt("passenger_rcpt");
		float catering_rcpt = crewRecord.optInt("catering_rcpt");
		float passenger_miss = crewRecord.optInt("passenger_miss");
		float receipts_miss = crewRecord.optInt("receipts_miss");
		float worker_miss = crewRecord.optInt("worker_miss");
		String notes = crewRecord.optString("notes");

		KeyworkAndRecordHolder holder = new KeyworkAndRecordHolder(train_id,
				strcontext, passenger_cnt, packet_cnt, passenger_rcpt,
				catering_rcpt, passenger_miss, receipts_miss, worker_miss,
				notes);
		helper.insert(holder);
		helper.close();
	}

	public synchronized String getReviewList(Context context, int trainId) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/review/getByTrainId/" + trainId + ".json?token="
					+ SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("trainId", String
			// .valueOf(trainId)));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONArray reviewArray = jsonObject.getJSONArray("review");
			insertReview(context, reviewArray, trainId);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	private void insertReview(Context context, JSONArray list, int trainId)
			throws JSONException {
		ReviewHelper helper = new ReviewHelper(context);
		helper.delete_Trainid(trainId);
		synchronized (lock.Lock) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.beginTransaction();
			for (int i = 0; i < list.length(); i++) {
				JSONObject json = (JSONObject) list.get(i);
				int id = json.optInt("id");
				int train_id = json.optInt("train_id");
				int user_id = json.optInt("user_id");
				String user_name = json.optString("user_name");
				String user_phone = json.optString("user_phone");
				String position_name = json.optString("position_name");
				String role_names = json.optString("role_names");
				String message = json.optString("message");
				int msg_status = json.optInt("msg_status");
				int msg_type = json.optInt("msg_type");
				long create_time = json.optLong("create_time");
				ReviewHolder holder = new ReviewHolder(id, train_id, user_id,
						user_name, user_phone, position_name, role_names,
						message, msg_status, msg_type, create_time);
				helper.insert(holder, db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}

	public synchronized String saveReview(Context context, ReviewHolder holder) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/review/save.json?token=" + SecurityCheckApp.token);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("trainId", String.valueOf(holder
					.getTrain_id())));
			params.add(new BasicNameValuePair("userId", String.valueOf(holder
					.getUser_id())));
			params.add(new BasicNameValuePair("message", String.valueOf(holder
					.getMessage())));
			params.add(new BasicNameValuePair("status", String.valueOf(holder
					.getMsg_status())));
			params.add(new BasicNameValuePair("type", String.valueOf(holder
					.getMsg_type())));
			if (holder.getCreate_time() != -1) {
				params.add(new BasicNameValuePair("createTime", String
						.valueOf(holder.getCreate_time())));
			}

			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			// JSONObject model = jsonObject.getJSONObject("model");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	public synchronized String getTrainCoachList(Context context, int trainId) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/train/getTrainCoachList.json?token="
					+ SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", String.valueOf(trainId)));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONArray coachList = jsonObject.getJSONArray("coachList");
			String trainCode = jsonObject.optString("trainCode");
			String trainDate = jsonObject.optString("trainDate");
			insertTrainCoach(context, trainCode, trainDate, trainId, coachList);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	private void insertTrainCoach(Context context, String trainCode,
			String trainDate, int trainId, JSONArray list) throws JSONException {
		TrainCoachHelper helper = new TrainCoachHelper(context);
		helper.delete_train_id(trainId);
		synchronized (lock.Lock) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.beginTransaction();
			for (int i = 0; i < list.length(); i++) {
				JSONObject json = (JSONObject) list.get(i);
				String coach_no = json.optString("COACH_NO");
				String train_no = json.optString("TRAIN_NO");
				String coach_type = json.optString("COACH_TYPE");
				int limit1 = json.optInt("LIMIT1");
				TrainCoachHolder holder = new TrainCoachHolder(coach_no,
						train_no, coach_type, limit1, trainDate, trainCode,
						trainId);
				helper.insert(holder, db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}

	public synchronized String getSeatInfo(Context context, String trainCode,
			String trainDate, String coachNo, Set<String> hashSet) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/train/getSeatInfo.json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("trainCode", trainCode));
			params.add(new BasicNameValuePair("trainDate", trainDate));
			params.add(new BasicNameValuePair("coachNo", coachNo));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				return ERROR403;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			L.i(builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONObject seat = jsonObject.getJSONObject("seat");
			insertSeatInfo(seat, context, trainCode, trainDate, coachNo,
					hashSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ERROR + "ÍøÂç´íÎó";
		}
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	private void insertSeatInfo(JSONObject seat, Context context,
			String trainCode, String trainDate, String coachNo,
			Set<String> hashSet) throws JSONException {
		SeatInfoHelper helper = new SeatInfoHelper(context);
		helper.delete_trainCode(trainCode, trainDate, coachNo);
		// L.i("delete :"+delete);
		Iterator<String> iterator = seat.keys();
		synchronized (lock.Lock) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.beginTransaction();
			while (iterator.hasNext()) {
				String key = iterator.next();
				hashSet.add(key);
				JSONArray array = seat.getJSONArray(key);
				for (int i = 0; i < array.length(); i++) {
					JSONObject json = (JSONObject) array.get(i);
					int ticket_type = json.optInt("TICKET_TYPE");
					String coach_no = json.optString("COACH_NO");
					String sale_mode = json.optString("SALE_MODE");
					String to_tele_code = json.optString("TO_TELE_CODE");
					String office_no = json.optString("OFFICE_NO");
					String board_train_code = json
							.optString("BOARD_TRAIN_CODE");
					String seat_type_code = json.optString("SEAT_TYPE_CODE");
					String seat_no = json.optString("SEAT_NO");
					String from_station_name = json
							.optString("FROM_STATION_NAME");
					String statistics_date = json.optString("STATISTICS_DATE");
					String to_station_name = json.optString("TO_STATION_NAME");
					String ticket_source = json.optString("TICKET_SOURCE");
					String from_tele_code = json.optString("FROM_TELE_CODE");
					String train_date = json.optString("TRAIN_DATE");
					int window_no = json.optInt("WINDOW_NO");
					float ticket_price = json.optInt("TICKET_PRICE");
					String ticket_no = json.optString("TICKET_NO");
					String inner_code = json.optString("INNER_CODE");
					String train_no = json.optString("TRAIN_NO");
					// L.i(seat_no+"  "+window_no);
					SeatInfoHolder holder = new SeatInfoHolder(ticket_type,
							coach_no, sale_mode, to_tele_code, office_no,
							board_train_code, seat_type_code, seat_no,
							from_station_name, statistics_date,
							to_station_name, ticket_source, from_tele_code,
							train_date, window_no, ticket_price, ticket_no,
							inner_code, train_no);
					helper.insert(holder, db);
				}

			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}

	/**
	 * °²È«¹ÜÀí
	 * 
	 * @param context
	 */

	public synchronized void checkOutDepartmentContact(Context context) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/department/list.json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
				SecurityCheckApp.token = "";
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			// Log.i("checkOutContact", builder.toString());

			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONArray jsonlist = jsonObject.getJSONArray("departmentList");
			autoinsertContact(jsonlist, context);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void checkOutUserContext(Context context) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/department/getAllUser.json?token="
					+ SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
				SecurityCheckApp.token = "";
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			// Log.i("checkOutUserContext", builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONArray jsonlist = jsonObject.getJSONArray("users");
			autoinsertContact(jsonlist, context);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void autoinsertContact(JSONArray jsonlist, Context context)
			throws JSONException {
		ContactListHelper helper = new ContactListHelper(context);
		// Log.i("jsonlist.length()", "" + jsonlist.length());
		for (int i = 0; i < jsonlist.length(); i++) {
			JSONObject json = (JSONObject) jsonlist.get(i);
			int id = json.optInt("id");
			String name = json.optString("name");
			String position = json.optString("position");
			int deptId = json.optInt("departmentId");
			int sort = json.optInt("sort");
			if (deptId != 0) {
				id += 100;
			}
			ContactListHolder holder = new ContactListHolder(id, name,
					position, deptId, sort);
			helper.insert(holder);
		}
		helper.close();
	}

	public static final int PAGE_SIZE = 15;

	public synchronized void checkOutPatrolList(Context context) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/patrol/list.json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("minId", ""+min));
			params.add(new BasicNameValuePair("pageSize", "" + PAGE_SIZE));
			params.add(new BasicNameValuePair("type", "1"));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			Log.i("builder", builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONArray json = jsonObject.getJSONArray("patrolList");
			inputListCach(json, context);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized ArrayList<Map<String, Object>> getAllType() {
		ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/patrol/getAllType.json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("statusCode", statusCode + "");
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			Log.i("builder", builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONArray jsons = jsonObject.getJSONArray("patrolTypes");
			for (int i = 0; i < jsons.length(); i++) {
				JSONObject json = (JSONObject) jsons.get(i);
				Map<String, Object> type_data = new HashMap<String, Object>();
				int id = json.optInt("id");
				String name = json.optString("name");
				type_data.put("type_name", name);
				type_data.put("type_id", id);
				data.add(type_data);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public synchronized ArrayList<Map<String, Object>> getAllResultType() {
		ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/patrol/getAllResultType.json?token="
					+ SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			Log.i("builder", builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONArray jsons = jsonObject.getJSONArray("patrolResultTypes");
			for (int i = 0; i < jsons.length(); i++) {
				JSONObject json = (JSONObject) jsons.get(i);
				Map<String, Object> type_data = new HashMap<String, Object>();
				int id = json.optInt("id");
				String name = json.optString("name");
				type_data.put("result_type_name", name);
				type_data.put("result_type_id", id);
				data.add(type_data);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public synchronized String getTaskStats(String interfaceTime) {
		String result = "";
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/patrol/getTaskStats.json?token="
					+ SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("month", interfaceTime));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			Log.i("builder", builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONObject jsons = jsonObject.getJSONObject("taskStats");
			int totalNumber = jsons.optInt("totalNumber");
			int finishedNumber = jsons.optInt("finishedNumber");
			if (totalNumber != 0 && finishedNumber != 0) {
				result += totalNumber + "," + finishedNumber;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public synchronized void checkOutPatrolMoreList(int minId, Context context) {
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			if (cookieInfo != null) {
				((AbstractHttpClient) client).setCookieStore(cookieInfo);
			}
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/patrol/list.json?token=" + SecurityCheckApp.token);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("minId", "" + minId));
			params.add(new BasicNameValuePair("pageSize", "" + PAGE_SIZE));
			params.add(new BasicNameValuePair("type", "1"));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			Log.i("builder", builder.toString());
			JSONObject jsonObject = new JSONObject(builder.toString());
			JSONArray json = jsonObject.getJSONArray("patrolList");
			inputListCach(json, context);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void inputListCach(JSONArray jsons, Context context)
			throws JSONException {
		PatrolHelper helper = new PatrolHelper(context);
		for (int i = 0; i < jsons.length(); i++) {
			JSONObject patrol = jsons.getJSONObject(i);
			int id = patrol.optInt("id");
			String content = patrol.optString("content");
			String video = patrol.optString("video");
			String img = patrol.optString("img");
			String responsiblePersonIds = patrol
					.optString("responsiblePersonIds");
			String responsiblePersonName = patrol
					.optString("responsiblePersonName");
			String userName = patrol.optString("userName");
			int userId = patrol.optInt("userId");
			String result = patrol.optString("result");
			long completeTime = patrol.optLong("completeTime");
			long creatTime = patrol.optLong("createTime");

			int typeId = patrol.optInt("typeId");
			String typeName = patrol.optString("typeName");
			int resultTypeId = patrol.optInt("resultTypeId");
			String resultTypeName = patrol.optString("resultTypeName");
			PatrolHolder holder = new PatrolHolder(id, content, img, video,
					completeTime, "," + responsiblePersonIds + ",", creatTime,
					userName, responsiblePersonName, result, userId, typeId,
					typeName, resultTypeId, resultTypeName);
			helper.insert(holder);
		}
		helper.close();
	}

	long filesize = 0;

	@SuppressLint("SimpleDateFormat")
	public synchronized int sendPatrol(PatrolHolder holder,
			final ProgressDialog progressDialog) {
		int id = -1;
		JSONObject sendMailJson = null;
		try {
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			client.getParams().setParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET,
					Charset.forName("UTF-8"));
			StringBuilder builder = new StringBuilder();
			HttpPost httpRequest = new HttpPost(ServerUrl
					+ "/patrol/add.json?token=" + SecurityCheckApp.token);
			boolean ifimg = holder.getImg().length() > 0;
			filesize = 0;
			ProgressListener listener = new ProgressListener() {

				@Override
				public void transferred(long num) {
					// TODO Auto-generated method stub
					progressDialog.setProgress((int) num);
				}
			};
			MyMultipartEntity mpEntity = new MyMultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE, null,
					Charset.forName("UTF-8"), listener);
			if (ifimg) {
				String[] imgs = holder.getImg().split(";");
				for (int i = 0; i < imgs.length; i++) {
					String img = imgs[i];
					File f_file = new File(img);
					if (f_file.exists()) {
						filesize += f_file.length();
						ContentBody cbFile = new FileBody(f_file, "", "UTF-8");
						String imgnum = "img" + (i + 1);
						// Log.i("img:"+imgnum, "img:"+img);
						mpEntity.addPart(imgnum, cbFile);
					}
				}
			}

			String videoPath = holder.getVideoData();

			if (videoPath.length() > 0) {
				File f_file = new File(videoPath);
				if (f_file.exists()) {
					filesize += f_file.length();
					ContentBody cbFile = new FileBody(f_file, "", "UTF-8");
					mpEntity.addPart("videoData", cbFile);
				}
			}
			filesize += 1024;
			progressDialog.setMax((int) filesize);
			mpEntity.addPart("content", new StringBody(holder.getContent(),
					Charset.forName("UTF-8")));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time = format.format(holder.getCompleteTime());
			mpEntity.addPart("completeTime",
					new StringBody(time, Charset.forName("UTF-8")));
			mpEntity.addPart("toUserIds", new StringBody(holder.getToUserIds(),
					Charset.forName("UTF-8")));

			mpEntity.addPart(
					"typeId",
					new StringBody(String.valueOf(holder.getTypeId()), Charset
							.forName("UTF-8")));
			mpEntity.addPart("resultTypeId",
					new StringBody(String.valueOf(holder.getResult_typeId()),
							Charset.forName("UTF-8")));
			httpRequest.setEntity(mpEntity);
			HttpResponse response = client.execute(httpRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 403) {
				state403 = true;
				httpjsontool = null;
				SecurityCheckApp.token = "";
			}
			// Log.i("sendPatrol", statusCode + "");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			Log.i("error", builder.toString());
			sendMailJson = new JSONObject(builder.toString());
			JSONObject json = sendMailJson.getJSONObject("patrol");
			id = json.getInt("id");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
}
