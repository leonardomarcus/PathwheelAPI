package br.com.pathwheel.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.pathwheel.dao.SpotDAO;
import br.com.pathwheel.io.Logger;
import br.com.pathwheel.jdbc.JdbcDataAccessObjectListener;
import br.com.pathwheel.jdbc.JdbcSpotDAO;
import br.com.pathwheel.model.SpotReportType;
import br.com.pathwheel.request.RegisterSpotRequest;
import br.com.pathwheel.request.ReportSpotRequest;
import br.com.pathwheel.response.FetchSpotResponse;
import br.com.pathwheel.response.RegisterSpotResponse;
import br.com.pathwheel.response.Response;

@Path("/v1/spot")
public class SpotEndpoint implements JdbcDataAccessObjectListener {

	@POST
	@Path("/register")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	public RegisterSpotResponse register(String jsonRequest) {
		Logger.info("==[/v1/spot/register]=");
		RegisterSpotResponse response = new RegisterSpotResponse();
		try {
			RegisterSpotRequest request = new ObjectMapper().readValue(jsonRequest.getBytes("UTF-8"), RegisterSpotRequest.class);
			Logger.info(request.toString());
			
			SpotDAO dao = new JdbcSpotDAO(this);
			response.setSpot(dao.register(request.getSpot()));
			
			response.setCode(200);
			response.setDescription("Success");
								
		} catch (Exception e) {
			response.setCode(500);
			response.setDescription(e.getMessage());
		}
		Logger.info("<< "+response.toString());
		return response;
	}
	
	@GET
	@Path("/fetch/{id}")
	@Produces("application/json; charset=utf-8")
	public FetchSpotResponse fetch(@PathParam("id") Long id) {
		FetchSpotResponse response = new FetchSpotResponse();
		try {
			Logger.info("==[/v1/spot/fetch/"+id+"]=");
							
			SpotDAO dao = new JdbcSpotDAO(this);
			response.setSpot(dao.fetch(id));
			
			response.setCode(200);
			response.setDescription("Success");
								
		} catch (Exception e) {
			response.setCode(500);
			response.setDescription(e.getMessage());
		}
		Logger.info("<< "+response.toString());
		return response;
	}
	
	@POST
	@Path("/report")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	public Response report(String jsonRequest) {
		Logger.info("==[/v1/spot/report]=");
		Response response = new Response();
		try {
			ReportSpotRequest request = new ObjectMapper().readValue(jsonRequest.getBytes("UTF-8"), ReportSpotRequest.class);
			Logger.info(request.toString());
			
			SpotDAO dao = new JdbcSpotDAO(this);
			
			if(dao.hasReport(request.getSpotId(), request.getUserId(), request.getSpotReportTypeId())) {
				response.setCode(401);
				response.setDescription("User already reported");	
			}
			else {
				dao.report(request.getSpotId(), request.getUserId(), request.getSpotReportTypeId());
				if(request.getSpotReportTypeId() == SpotReportType.NOT_THERE && (
						dao.byUser(request.getSpotId(), request.getUserId()) ||
						 dao.countReports(request.getSpotId(), SpotReportType.NOT_THERE) > 2)) {
					dao.remove(request.getSpotId());
				}
				response.setCode(200);
				response.setDescription("Success");
			}
								
		} catch (Exception e) {
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
