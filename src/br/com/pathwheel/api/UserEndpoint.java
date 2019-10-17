package br.com.pathwheel.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.spi.resource.Singleton;

import br.com.pathwheel.dao.UserDAO;
import br.com.pathwheel.exception.AuthenticationFailureException;
import br.com.pathwheel.exception.UserNotFoundException;
import br.com.pathwheel.io.Logger;
import br.com.pathwheel.jdbc.JdbcDataAccessObjectListener;
import br.com.pathwheel.jdbc.JdbcUserDAO;
import br.com.pathwheel.request.AuthenticateRequest;
import br.com.pathwheel.response.AuthenticateResponse;
import br.com.pathwheel.response.Codes;

@Path("/v1/user")
@Singleton
public class UserEndpoint implements JdbcDataAccessObjectListener {
	
	@POST
	@Path("/authenticate")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	public AuthenticateResponse authenticate(String jsonRequest) {
		Logger.info("==[/v1/user/authenticate]=");
		AuthenticateResponse response = new AuthenticateResponse();
		try {
			
			AuthenticateRequest request = new ObjectMapper().readValue(jsonRequest.getBytes("UTF-8"), AuthenticateRequest.class);
			Logger.info(request.toString());
			
			UserDAO dao = new JdbcUserDAO(this);
			response.setUser(dao.authenticate(request.getLogin(), request.getSecret()));
				
			response.setCode(200);
			response.setDescription("Success");
		} catch(UserNotFoundException e) {
			response.setCode(e.getCode());
			response.setDescription(e.getMessage());
		} catch(AuthenticationFailureException e) {
			response.setCode(e.getCode());
			response.setDescription(e.getMessage());		
		} catch(Exception e) {
			response.setCode(Codes.INTERNAL_SERVER_ERROR);
			response.setDescription(e.getMessage());
		}
		Logger.info("<< "+response.toString());
		return response;
	}
	

	@Override
	public void onErroJdbcDataAccessObject(Object sender, Exception e) {
		Logger.info(e.getMessage());
	}

	@Override
	public void onLogJdbcDataAccessObject(Object sender, String msg) {
		Logger.info(msg);
	}

}
