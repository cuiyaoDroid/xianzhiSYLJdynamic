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
import java.util.List;
import java.util.Map;

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
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xianzhi.stool.L;
import com.xianzhi.tool.db.ContactListHelper;
import com.xianzhi.tool.db.ContactListHolder;
import com.xianzhi.tool.db.DynamicListHelper;
import com.xianzhi.tool.db.DynamicListHolder;
import com.xianzhi.tool.db.PatrolHelper;
import com.xianzhi.tool.db.PatrolHolder;
import com.xianzhi.tool.db.lock;
import com.xianzhi.webtool.MyMultipartEntity.ProgressListener;
import com.xianzhisecuritycheck.main.SecurityCheckApp;
import com.xianzhisylj.dynamic.LoginActivity;

public class HttpJsonTool {

	public enum LoginInfo {
		correct, webfaild, wronginput, correctnoname
	};

	// public static final String ServerUrl =
	// "http://v.cc-railway.xzh-soft.com:8083";
	public static final String ServerUrl = "http://192.168.1.107:8080";
	// public static final String ServerUrl =
	// "https://eaccess.syrailway.cn:8443/mapping/sjznbg";
	private static HttpJsonTool httpjsontool = null;
	public static String username = "";
	public static int userId = -1;
	public static boolean state403 = false;
	public static final String ERROR = "<error>";
	public static final String SUCCESS = "<success>";

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
			JSONObject jsonObject = new JSONObject(builder.toString());
			String error = jsonObject.optString("error");
			if (error.length() > 0) {
				return ERROR + error;
			}
			JSONObject json = jsonObject.getJSONObject("user");
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
			if(minId!=-1){
				params.add(new BasicNameValuePair("minId", String.valueOf(minId)));
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
				gotoLoginView(context);
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
	public synchronized String addNewDynamicTrain(Context context,String trainCode,String startTime
			,String currentTeam,String teamLength) {
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
				gotoLoginView(context);
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
			//JSONObject object = jsonObject.getJSONObject("train");
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
				DynamicListHolder holder = new DynamicListHolder(id,
						board_train_code, start_time, from_station_name,
						to_station_name, current_team, driving_status, user_id,
						job_number, user_name, photo, phone, department_id,
						department_name, position_id, position_name,
						team_length);
				helper.insert(holder, db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
		helper.close();
	}

	private void gotoLoginView(Context context) {
		state403 = false;
		httpjsontool = null;
		SecurityCheckApp.token = "";
		try {
			Intent intent = new Intent(context, LoginActivity.class);
			context.startActivity(intent);
		} catch (Exception e) {

		}
	}

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

	public static final int PAGE_SIZE = 20;

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
