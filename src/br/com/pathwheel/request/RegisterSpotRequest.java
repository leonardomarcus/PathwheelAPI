package br.com.pathwheel.request;

import br.com.pathwheel.model.Spot;

public class RegisterSpotRequest extends Request {
	private Spot spot;

	public Spot getSpot() {
		return spot;
	}

	public void setSpot(Spot spot) {
		this.spot = spot;
	}

	@Override
	public String toString() {
		return "RegisterSpotRequest []";
	}
	
}
