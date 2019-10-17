package br.com.pathwheel.security;

public class ApiKey {
	public static final String pathwheelApiKey = "1:3iwJo0iWV!VcR";
	public static final String googleApiKey = "YOUR_API_KEY";
	
	public static boolean validateApiKey(String key) {
		return key == null || key.isEmpty() || !key.equals(pathwheelApiKey) ? false : true;
	}
}
