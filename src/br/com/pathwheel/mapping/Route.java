package br.com.pathwheel.mapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import br.com.pathwheel.io.Logger;

public class Route {
	
	public static final int FOOT = 1;
	public static final int BIKE = 2;
	public static final int CAR = 3;
	
	private long distance;
	private long duration;
	private String overviewPolylinePoints;
	private String formattedDistance;
	private String formattedDuration;
	private String startAddress;
	private String endAddress;
	
	public long getDistance() {
		return distance;
	}
	public void setDistance(long distance) {
		this.distance = distance;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getOverviewPolylinePoints() {
		return overviewPolylinePoints;
	}
	public void setOverviewPolylinePoints(String overviewPolylinePoints) {
		this.overviewPolylinePoints = overviewPolylinePoints;
	}
	public String getFormattedDistance() {
		return formattedDistance;
	}
	public void setFormattedDistance(String formattedDistance) {
		this.formattedDistance = formattedDistance;
	}
	public String getFormattedDuration() {
		return formattedDuration;
	}
	public void setFormattedDuration(String formattedDuration) {
		this.formattedDuration = formattedDuration;
	}
	public String getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	public String getEndAddress() {
		return endAddress;
	}
	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	public static Route fromGoogleMapsApi(String origin, String destination, String key) throws Exception {
		URL url;

		//String strUrl = "http://maps.google.com/maps/api/directions/json?origin="+latOrigem+","+lngOrigem+"&destination="+latDestino+","+lngDestino+"&sensor=false";
		//strUrl = GoogleMapsLicense.getUrlWithDigitalSignature(strUrl, clientId, privateCryptoKey);
		
		String strUrl = "https://maps.googleapis.com/maps/api/directions/json?mode=walking&origin="+origin+"&destination="+destination+"&key="+key;
		
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
		
		JSONObject joStatus = new JSONObject(buffer); //(JSONObject) JSONValue.parse(buffer);
		String status = joStatus.get("status").toString().toUpperCase();
	    //log("status google: "+status);
	    
	    if(status.equals("OK")) {
	    	Route route = new Route();
			JSONObject jo = new JSONObject(buffer); //(JSONObject) JSONValue.parse(buffer);		    
		    JSONArray ja = (JSONArray) jo.get("routes");
		    JSONObject joo = (JSONObject) ja.get(0);
		    JSONArray jaa = (JSONArray)joo.get("legs");
		    JSONObject jooo = (JSONObject) jaa.get(0);
		    
		    JSONObject jaaa = (JSONObject)jooo.get("distance");
		    
		    route.setStartAddress(jooo.get("start_address").toString());
		    route.setEndAddress(jooo.get("end_address").toString());
		    
		    route.setDistance(Long.parseLong(jaaa.get("value").toString())); //distancia em metros
		    route.setFormattedDistance(jaaa.get("text").toString());
		    JSONObject jDuration = (JSONObject)jooo.get("duration");
		    route.setDuration(Long.parseLong(jDuration.get("value").toString())); //duração em segundos
		    route.setFormattedDuration(jDuration.get("text").toString());
		    
		    JSONObject jOverviewPolyline = (JSONObject) joo.get("overview_polyline");
		    route.setOverviewPolylinePoints(jOverviewPolyline.get("points").toString());
		    
		    return route;
	    }
	    else {
	    	throw new Exception("status: "+status);
	    }
	}
	
	public static Route fromOpenSourceRouteMachine(String origin, String destination, int profile) throws Exception {
		URL url;

		List<GeographicCoordinate> coordinates = new ArrayList<GeographicCoordinate>();
		coordinates.add(GeographicCoordinate.parse(origin));
		coordinates.add(GeographicCoordinate.parse(destination));
		
		String sProfile = "car";
		if(profile == FOOT)
			sProfile = "foot";
		else if(profile == BIKE)
			sProfile = "bike";
		else if(profile == CAR)
			sProfile = "car";
		
		//http://project-osrm.org/docs/v5.5.1/api/#general-options
		String strUrl = "http://router.project-osrm.org/route/v1/"+sProfile+"/polyline("+Polyline.encode(coordinates)+")?overview=full";
		
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
		
		JSONObject joStatus = new JSONObject(buffer); //(JSONObject) JSONValue.parse(buffer);
		String status = joStatus.get("code").toString().toUpperCase();
	    //log("status google: "+status);
	    
	    if(status.equals("OK")) {
	    	Route route = new Route();
			JSONObject jo = new JSONObject(buffer); //(JSONObject) JSONValue.parse(buffer);
			
			JSONArray routes = jo.getJSONArray("routes");
			for(int i=0; i<routes.length(); i++) {
				route.setOverviewPolylinePoints(routes.getJSONObject(i).getString("geometry"));
				route.setDistance(routes.getJSONObject(i).getLong("distance"));
				route.setFormattedDistance(String.valueOf(routes.getJSONObject(i).getLong("distance")/1000)+" km");
				
			}
			
			JSONArray waypoints = jo.getJSONArray("waypoints");
			
			route.setStartAddress(waypoints.getJSONObject(0).getString("name"));
			route.setEndAddress(waypoints.getJSONObject(waypoints.length()-1).getString("name"));
		    
		    return route;
	    }
	    else {
	    	throw new Exception("status: "+status);
	    }
	}
	@Override
	public String toString() {
		return "Route [distance=" + distance + ", duration=" + duration + ", overviewPolylinePoints="
				+ overviewPolylinePoints + ", formattedDistance=" + formattedDistance + ", formattedDuration="
				+ formattedDuration + ", startAddress=" + startAddress + ", endAddress=" + endAddress + "]";
	}	

		
}
