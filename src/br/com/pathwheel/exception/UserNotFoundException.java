package br.com.pathwheel.exception;

import br.com.pathwheel.response.Codes;

public class UserNotFoundException extends Exception {
	
	private static final long serialVersionUID = 8222901961285515624L;
	private final int code = Codes.NOT_FOUND;
	
	public UserNotFoundException() {
		super("User not found");
	}

	public int getCode() {
		return code;
	}

}
