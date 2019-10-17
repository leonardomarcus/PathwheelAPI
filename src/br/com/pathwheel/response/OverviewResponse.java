package br.com.pathwheel.response;

import java.util.List;

import br.com.pathwheel.model.PavementSample;
import br.com.pathwheel.model.Spot;

public class OverviewResponse extends Response {
	private List<PavementSample> samples;
	private List<Spot> spots;

	public List<PavementSample> getSamples() {
		return samples;
	}

	public void setSamples(List<PavementSample> samples) {
		this.samples = samples;
	}

	public List<Spot> getSpots() {
		return spots;
	}

	public void setSpots(List<Spot> spots) {
		this.spots = spots;
	}

	@Override
	public String toString() {
		return "OverviewResponse [samples=" + samples.size() + ", spots=" + spots.size() + "]";
	}
	
	
	
}
