package br.com.pathwheel.request;

import java.util.ArrayList;
import java.util.List;

import br.com.pathwheel.mapping.GeographicCoordinate;

public class RouteMapRequest extends Request {
	List<GeographicCoordinate> coordinates = new ArrayList<GeographicCoordinate>();
	private Integer travelModeId = new Integer(1);

	public List<GeographicCoordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<GeographicCoordinate> coordinates) {
		this.coordinates = coordinates;
	}
	

	public Integer getTravelModeId() {
		return travelModeId;
	}

	public void setTravelModeId(Integer travelModeId) {
		this.travelModeId = travelModeId;
	}

	@Override
	public String toString() {
		return "RouteMapRequest [coordinates=" + coordinates + ", travelModeId=" + travelModeId + "]";
	}
	
	
}
