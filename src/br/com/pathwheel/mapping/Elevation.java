package br.com.pathwheel.mapping;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import br.com.pathwheel.io.Logger;

public class Elevation {
	
	public static List<Double> fromGoogleMapsApi(List<GeographicCoordinate> coordinates, String key) throws Exception {
			
		List<Double> elevations = new ArrayList<Double>();
		URL url;
		
		String strUrl = "https://maps.googleapis.com/maps/api/elevation/json?locations=enc:"+Polyline.encode(coordinates)+"&key="+key;
		
		url = new URL(strUrl);
		Logger.info(strUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
		connection.setDoInput(true);
		connection.setDoOutput(false);
		connection.connect(); 
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String buffer = "";
		String s = "";
		while (null != ((s = br.readLine()))) {
			 buffer = buffer + s;			
		}
		br.close();
		
		//System.out.println(buffer);
		
		JSONObject json = new JSONObject(buffer); //(JSONObject) JSONValue.parse(buffer);
		String status = json.get("status").toString().toUpperCase();
	    //log("status google: "+status);
	    
	    if(status.equals("OK")) {
	    	JSONArray results = json.getJSONArray("results");
	    	for(int i=0;i<results.length();i++) {
	    		elevations.add( results.getJSONObject(i).getDouble("elevation"));
	    	}
		    
		    return elevations;
	    }
	    else {
	    	throw new Exception("status: "+status);
	    }
	}
	
	public static List<Double> fromOpenElevationApi(List<GeographicCoordinate> coordinates) throws Exception {
		
		List<Double> elevations = new ArrayList<Double>();
		URL url;
		
		JSONObject request = new JSONObject();
		JSONArray locations = new JSONArray();
		
		for(GeographicCoordinate coordinate : coordinates) {
			JSONObject location = new JSONObject();
			location.put("latitude", coordinate.getLatitude());
			location.put("longitude", coordinate.getLongitude());
			locations.put(location);
		}
		request.put("locations", locations);
		
		System.out.println(request.toString());
		//System.exit(0);
		//locations += locations.isEmpty() ? coordinate.getLatitude()+","+coordinate.getLongitude() : "|"+coordinate.getLatitude()+","+coordinate.getLongitude();
		
		String strUrl = "https://api.open-elevation.com/api/v1/lookup";
		
		url = new URL(strUrl);
		Logger.info(strUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.8,en-US;q=0.6,en;q=0.4");
		//connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod( "POST" );
		connection.setRequestProperty( "Content-Type", "application/json"); 
		connection.setRequestProperty( "Accept", "application/json");
		connection.setRequestProperty( "charset", "utf-8");
		connection.setRequestProperty( "Content-Length", Integer.toString(  request.toString().getBytes(StandardCharsets.UTF_8).length ));
		connection.setUseCaches( false );
		
		//connection.connect();
		try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
			   wr.write( request.toString().getBytes(StandardCharsets.UTF_8) );
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String buffer = "";
		String s = "";
		while (null != ((s = br.readLine()))) {
			 buffer = buffer + s;			
		}
		br.close();
		
		//System.out.println(buffer);
		
		JSONObject json = new JSONObject(buffer); //(JSONObject) JSONValue.parse(buffer);
	    
    	JSONArray results = json.getJSONArray("results");
    	for(int i=0;i<results.length();i++) {
    		elevations.add( results.getJSONObject(i).getDouble("elevation"));
    	}
	    
	    return elevations;
	    
	}
	
	public static void main(String[] args) throws Exception {
		List<GeographicCoordinate> coordinates = Polyline.decode("pfknApxviF}@wAGQu@sF]wCi@kEs@sFw@cG]_DMe@S[wBmBgDoCAGSSaCiBi@m@}A{B{CqDkCmDe@w@wB}DeAiBk@eAe@mAg@aB_@qAYw@}@aC}@qCm@sA[a@WWSOe@HmBr@mBl@kA]");
		System.out.println("coordinates: "+coordinates.size());
		List<Double> elevations;

		
		elevations = fromOpenElevationApi(coordinates);
		System.out.println("elevations: "+elevations.size());
		System.out.println(elevations);
	}
}
