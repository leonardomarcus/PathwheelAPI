package br.com.pathwheel.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Spot {
	private Long id;
	private SpotType spotType = new SpotType();
	private String registrationDate;
	private Double latitude;
	private Double longitude;
	private String comment;
	private String picture;
	private User user = new User();
	private boolean hasPicture;
	private int countStillThere;
	private int countNotThere;
	private Integer travelModeId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SpotType getSpotType() {
		return spotType;
	}
	public void setSpotType(SpotType spotType) {
		this.spotType = spotType;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public boolean isHasPicture() {
		return hasPicture;
	}
	public void setHasPicture(boolean hasPicture) {
		this.hasPicture = hasPicture;
	}
	public int getCountStillThere() {
		return countStillThere;
	}
	public void setCountStillThere(int countStillThere) {
		this.countStillThere = countStillThere;
	}
	public int getCountNotThere() {
		return countNotThere;
	}
	public void setCountNotThere(int countNotThere) {
		this.countNotThere = countNotThere;
	}
	
	public Integer getTravelModeId() {
		return travelModeId;
	}
	public void setTravelModeId(Integer travelModeId) {
		this.travelModeId = travelModeId;
	}
	@Override
	public String toString() {
		return "Spot [id=" + id + ", spotType=" + spotType + ", registrationDate=" + registrationDate + ", latitude="
				+ latitude + ", longitude=" + longitude + ", comment=" + comment + ", user=" + user + ", hasPicture="
				+ hasPicture + ", countStillThere=" + countStillThere + ", countNotThere=" + countNotThere + ", travelModeId="+travelModeId+"]";
	}
	
}
