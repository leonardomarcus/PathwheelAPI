package br.com.pathwheel.request;

public class Request {
	protected String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Request [uuid=" + uuid + "]";
	}
	
}
