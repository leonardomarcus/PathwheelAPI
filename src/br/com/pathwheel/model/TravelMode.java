package br.com.pathwheel.model;

public class TravelMode {
	public static final int WALKING = 0;
	public static final int WHEELCHAIR = 1;
	public static final int CAR = 2;
	//public static final int BIKE = 3;
	
	private int id;
	private String description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "TravelMode [id=" + id + ", description=" + description + "]";
	}
	
	public static int detect(int travelModeReference, Double speed, Double distance, Integer steps) {
		
		double stepsPerMeter = (steps != null && steps > 0 && distance != null && distance > 0) ? (double)(steps/distance) : 0;
		System.out.println("steps per meters: "+stepsPerMeter);
		if(stepsPerMeter > 0.4d)
			return WALKING;
		if(speed < 15) {
			if(travelModeReference == WHEELCHAIR)
				return WHEELCHAIR;
			else
				return CAR;
		} else {
			return CAR;
		}
		//return travelModeReference;
	}
	
}
