package br.com.pathwheel.request;

import br.com.pathwheel.mapping.GeographicCoordinate;

public class RouteGoogleRequest extends Request {
	private GeographicCoordinate origin;
	private GeographicCoordinate destination;
	public GeographicCoordinate getOrigin() {
		return origin;
	}
	public void setOrigin(GeographicCoordinate origin) {
		this.origin = origin;
	}
	public GeographicCoordinate getDestination() {
		return destination;
	}
	public void setDestination(GeographicCoordinate destination) {
		this.destination = destination;
	}
	@Override
	public String toString() {
		return "RouteGoogleRequest [origin=" + origin + ", destination=" + destination + "]";
	}
	
}
