package br.com.pathwheel.request;

import br.com.pathwheel.model.PavementSample;

public class RegisterSampleRequest extends Request {
	private PavementSample sample;
	private String smartDevice;

	public PavementSample getSample() {
		return sample;
	}

	public void setSample(PavementSample sample) {
		this.sample = sample;
	}	

	public String getSmartDevice() {
		return smartDevice;
	}

	public void setSmartDevice(String smartDevice) {
		this.smartDevice = smartDevice;
	}

	@Override
	public String toString() {
		return "RegisterPavementSampleRequest [sample=" + sample + ", smartDevice=" + smartDevice + "]";
	}

	
	
}
