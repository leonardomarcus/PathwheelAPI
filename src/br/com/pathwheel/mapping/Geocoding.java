package br.com.pathwheel.mapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import br.com.pathwheel.io.Logger;

public class Geocoding {
	
	//https://nominatim.org/release-docs/develop/api/Search/
	public static List<Address> fromNominatim(String query, String viewbox, int limit) throws Exception {
		List<Address> addrs = new ArrayList<Address>();
		
		URL url;

		//https://nominatim.openstreetmap.org/?format=json&addressdetails=1&q=jardim+dos+namorados&format=json&limit=5&viewbox=-38.45524,-12.99924,-38.44458,-13.00446
		String strUrl = "https://nominatim.openstreetmap.org/?format=json&addressdetails=1&q="+URLEncoder.encode(query, StandardCharsets.UTF_8.toString())+"&format=json&limit="+limit+"&viewbox="+URLEncoder.encode(viewbox, StandardCharsets.UTF_8.toString());
		
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
		//System.out.println(buffer.toString());
		
		JSONArray jsonArray = new JSONArray(buffer);
		for(int i=0;i<jsonArray.length();i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			Address addr = new Address();
			addr.getCoordinate().setLatitude(obj.getDouble("lat"));
			addr.getCoordinate().setLongitude(obj.getDouble("lon"));
			JSONObject jsonAddr = obj.getJSONObject("address");

			if(jsonAddr.has("park"))
				addr.setPlace(jsonAddr.getString("park"));
			if(jsonAddr.has("allotments"))
				addr.setPlace(jsonAddr.getString("allotments"));			
			if(jsonAddr.has("road"))
				addr.setPlace(jsonAddr.getString("road"));
			if(jsonAddr.has("library"))
				addr.setPlace(jsonAddr.getString("library"));
			if(jsonAddr.has("restaurant"))
				addr.setPlace(jsonAddr.getString("restaurant"));
			
			if(jsonAddr.has("address29"))
				addr.setPlace(addr.getPlace().isEmpty() ? jsonAddr.getString("address29") : jsonAddr.getString("address29")+", "+addr.getPlace());
			
			if(jsonAddr.has("city_district"))
				addr.setDistrict(jsonAddr.getString("city_district"));
			if(jsonAddr.has("suburb"))
				addr.setDistrict(jsonAddr.getString("suburb"));						
			
			if(jsonAddr.has("state"))
				addr.setState(jsonAddr.getString("state"));
			
			if(jsonAddr.has("city"))
				addr.setCity(jsonAddr.getString("city"));
			if(jsonAddr.has("town"))
				addr.setCity(jsonAddr.getString("town"));
			
			if(jsonAddr.has("country"))
				addr.setCountry(jsonAddr.getString("country"));
			
			if(jsonAddr.has("postcode"))
				addr.setPostcode(jsonAddr.getString("postcode"));
			
			addrs.add(addr);
		}
		

		return addrs;
	}
	
	public static void main(String[] args) throws Exception {
		List<Address> addrs = Geocoding.fromNominatim("jardim dos namorados", "-38.55524,-12.89924,-38.34458,-13.10446", 5);
		System.out.println("results: "+addrs.size());
		for(Address addr : addrs)
			System.out.println(addr);
	}

}
