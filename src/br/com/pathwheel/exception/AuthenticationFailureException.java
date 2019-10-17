package br.com.pathwheel.exception;

import br.com.pathwheel.response.Codes;

public class AuthenticationFailureException extends Exception {
	
	private static final long serialVersionUID = 286067498797258567L;
	
	private final int code = Codes.UNAUTHORIZED;

	public AuthenticationFailureException() {
		super("Authentication failure");
	}

	public int getCode() {
		return code;
	}
		
}
