package code2040.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Stage3 {
	private static final String URL = "http://challenge.code2040.org/api/prefix";
	private static final String URL_VALIDATE = "http://challenge.code2040.org/api/validateprefix";
	
	private static JSONObject getArray(String token) {
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
			
			JSONObject array = new JSONObject(br.readLine());
			br.close();
						
			return (JSONObject) array.get("result");
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static JSONArray findWords(String prefix, JSONArray array) {
		JSONArray newArray = new JSONArray();
		for(int i=0; i<array.length(); i++) {
			String str = array.getString(i);
			if(!str.startsWith(prefix)) {
				newArray.put(str);
			}
		}
		return newArray;
	}
	
	private static void validateWords(String token, JSONArray array) {
		try {
			URL url = new URL(URL_VALIDATE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			
			JSONObject validate = new JSONObject();
			validate.put("token", token);
			validate.put("array", array);
			
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
		JSONObject array = Stage3.getArray(token);
		JSONArray newArray = Stage3.findWords(array.getString("prefix"), (JSONArray) array.get("array"));
		Stage3.validateWords(token, newArray);
	}
}
