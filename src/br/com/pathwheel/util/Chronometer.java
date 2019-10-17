package br.com.pathwheel.util;

public class Chronometer {
	private long millisIni;
	
	public void play() {
		this.millisIni = System.currentTimeMillis();			
	}

	public long stop() {
		return System.currentTimeMillis() - this.millisIni; 
	}
}
