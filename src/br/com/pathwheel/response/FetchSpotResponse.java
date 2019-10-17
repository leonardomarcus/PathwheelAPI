package br.com.pathwheel.response;

import br.com.pathwheel.model.Spot;

public class FetchSpotResponse extends Response {
	private Spot spot;

	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}	

}
