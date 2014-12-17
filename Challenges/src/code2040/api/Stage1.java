package code2040.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Stage1 {
	private static final String URL = "http://challenge.code2040.org/api/getstring";
	private static final String URL_VALIDATE = "http://challenge.code2040.org/api/validatestring";
	
	private static String getString(String token) {
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
			
			JSONObject string = new JSONObject(br.readLine());
			br.close();
			
			return string.getString("result");
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String reverseString(String str) {
		StringBuilder reverse = new StringBuilder();
		for(int i = str.length()-1; i >= 0; i--) {
			reverse.append(str.charAt(i));
		}
		return reverse.toString();
	}
	
	private static void validateString(String token, String reverse) {
		try {
			URL url = new URL(URL_VALIDATE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			
			JSONObject validate = new JSONObject();
			validate.put("token", token);
			validate.put("string", reverse);
			
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
		String str = Stage1.getString(token);
		String reverse = Stage1.reverseString(str);
		Stage1.validateString(token, reverse);
	}
}
