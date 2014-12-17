package code2040.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Stage2 {
	private static final String URL = "http://challenge.code2040.org/api/haystack";
	private static final String URL_VALIDATE = "http://challenge.code2040.org/api/validateneedle";
	
	private static JSONObject getHaystack(String token) {
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
			
			JSONObject haystack = new JSONObject(br.readLine());
			br.close();
						
			return (JSONObject) haystack.get("result");
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static int findNeedle(String needle, JSONArray haystack) {
		for(int i=0; i<haystack.length(); i++) {
			if(needle.equals(haystack.getString(i))) {
				return i;
			}
		}
		return -1;
	}
	
	private static void validateNeedle(String token, int index) {
		try {
			URL url = new URL(URL_VALIDATE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			
			JSONObject validate = new JSONObject();
			validate.put("token", token);
			validate.put("needle", index);
			
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
		JSONObject haystack = Stage2.getHaystack(token);
		int index = Stage2.findNeedle(haystack.getString("needle"), (JSONArray) haystack.get("haystack"));
		Stage2.validateNeedle(token, index);
	}
}
