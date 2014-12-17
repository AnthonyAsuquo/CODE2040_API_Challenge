package code2040.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Registration {
	private static final String URL = "http://challenge.code2040.org/api/register";
	private static final String URL_GRADES = "http://challenge.code2040.org/api/status";
	private static final String EMAIL = "asuquo@usc.edu";
	private static final String GITHUB = "https://github.com/AnthonyAsuquo/CODE2040_API_Challenge.git";
	
	public static String getToken() {
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			
			JSONObject register = new JSONObject();
			register.put("email", EMAIL);
			register.put("github", GITHUB);
			
			conn.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(register.toString());
			dos.flush();
			dos.close();
					
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JSONObject token = new JSONObject(br.readLine());
			br.close();
			
			return token.getString("result");
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void getGrades(String token) {
		try {
			URL url = new URL(URL_GRADES);
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
			
			JSONObject jo = new JSONObject(br.readLine());
			br.close();
			
			JSONObject grades = jo.getJSONObject("result");		
			System.out.println(grades.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

