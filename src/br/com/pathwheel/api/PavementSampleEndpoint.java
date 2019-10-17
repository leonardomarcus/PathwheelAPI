package br.com.pathwheel.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.pathwheel.dao.PavementSampleDAO;
import br.com.pathwheel.io.Logger;
import br.com.pathwheel.jdbc.JdbcDataAccessObjectListener;
import br.com.pathwheel.jdbc.JdbcPavementSampleDAO;
import br.com.pathwheel.request.RegisterSampleRequest;
import br.com.pathwheel.response.Response;

@Path("/v1/pavement-sample")
public class PavementSampleEndpoint implements JdbcDataAccessObjectListener {

	@POST
	@Path("/register")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	public Response insert(String jsonRequest) {
		Logger.info("==[/v1/pavement-sample/register]=");
		Response response = new Response();
		try {
			
			RegisterSampleRequest request = new ObjectMapper().readValue(jsonRequest.getBytes("UTF-8"), RegisterSampleRequest.class);
			Logger.info(request.toString());
			
			PavementSampleDAO dao = new JdbcPavementSampleDAO(this);
			dao.register(request.getSample(), request.getSmartDevice());
			
			response.setCode(200);
			response.setDescription("Success");
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setCode(500);
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
