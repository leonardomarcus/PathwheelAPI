package br.com.pathwheel.dao;

import br.com.pathwheel.exception.AuthenticationFailureException;
import br.com.pathwheel.exception.UserNotFoundException;
import br.com.pathwheel.model.User;

public interface UserDAO {
	User authenticate(String login, String secret) throws UserNotFoundException, AuthenticationFailureException;
}
