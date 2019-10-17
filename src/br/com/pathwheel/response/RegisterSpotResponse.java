package br.com.pathwheel.response;

import br.com.pathwheel.model.Spot;

public class RegisterSpotResponse extends Response {
	private Spot spot;

	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}

	@Override
	public String toString() {
		return "RegisterSpotResponse [spot=" + spot + "]";
	}

}
