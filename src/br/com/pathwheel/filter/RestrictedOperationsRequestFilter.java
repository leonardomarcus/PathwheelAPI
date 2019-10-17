package br.com.pathwheel.filter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import br.com.pathwheel.io.Logger;
import br.com.pathwheel.jdbc.JdbcDataAccessObjectListener;
import br.com.pathwheel.security.ApiKey;

@Provider
public class RestrictedOperationsRequestFilter implements ContainerRequestFilter, JdbcDataAccessObjectListener {

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		try {
					
			//Logger.info("key: "+request.getQueryParameters().getFirst("key"));
			
	        //checking the API key
			if(!ApiKey.validateApiKey(request.getQueryParameters().getFirst("key")))
				throw new Exception("API key is incorrect");
			
			return request;			
		} catch (Exception e) {
			Logger.info("<< filter exception: "+e.getMessage());
			ResponseBuilder builder = Response.status(Response.Status.UNAUTHORIZED).entity(new br.com.pathwheel.response.Response(404, e.getMessage())).type(MediaType.APPLICATION_JSON);
			throw new WebApplicationException(builder.build());	
		}		
	}

	@Override
	public void onErroJdbcDataAccessObject(Object sender, Exception e) {
		
	}

	@Override
	public void onLogJdbcDataAccessObject(Object sender, String msg) {
		Logger.info(msg);
	}
}
