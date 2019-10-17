package br.com.pathwheel.mapping;

public class Address {
	private String place;
	private String district;
	private String state;
	private String city;	
	private String country;
	private String postcode;
	private GeographicCoordinate coordinate = new GeographicCoordinate();
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public GeographicCoordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(GeographicCoordinate coordinate) {
		this.coordinate = coordinate;
	}
	@Override
	public String toString() {
		return "Address [place=" + place + ", district=" + district + ", state=" + state + ", city=" + city
				+ ", country=" + country + ", postcode=" + postcode + ", coordinate=" + coordinate + "]";
	}
	
}
