package code2040.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

public class Stage4 {
	private static final String URL = "http://challenge.code2040.org/api/time";
	private static final String URL_VALIDATE = "http://challenge.code2040.org/api/validatetime";
	
	private static JSONObject getDatestamp(String token) {
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			
			JSONObject send = new JSONObject();
			send.put("token", token);
			
			conn.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(send.toString());
			dos.flush();
			dos.close();
					
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject datestamp = new JSONObject(br.readLine());
			br.close();
						
			return (JSONObject) datestamp.get("result");
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String updateDatestamp(long interval, String datestamp) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			Calendar c = Calendar.getInstance();
			c.setTime(df.parse(datestamp));
			c.add(Calendar.SECOND, (int) interval);
			return df.format(c.getTime());
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void validateDatestamp(String token, String date) {
		try {
			URL url = new URL(URL_VALIDATE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			
			JSONObject validate = new JSONObject();
			validate.put("token", token);
			validate.put("datestamp", date);
			
			conn.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(validate.toString());
			dos.flush();
			dos.close();
					
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject result = new JSONObject(br.readLine());
			br.close();
			
			System.out.println(result.getString("result"));
			
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void main(String[] args) {
		String token = Registration.getToken();
		JSONObject datestamp = Stage4.getDatestamp(token);
		String date = Stage4.updateDatestamp(datestamp.getLong("interval"), datestamp.getString("datestamp"));
		Stage4.validateDatestamp(token, date);
	}
}
