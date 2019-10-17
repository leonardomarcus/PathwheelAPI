package br.com.pathwheel.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.pathwheel.dao.PavementSampleDAO;
import br.com.pathwheel.dao.SpotDAO;
import br.com.pathwheel.io.Logger;
import br.com.pathwheel.jdbc.JdbcDataAccessObjectListener;
import br.com.pathwheel.jdbc.JdbcPavementSampleDAO;
import br.com.pathwheel.jdbc.JdbcSpotDAO;
import br.com.pathwheel.mapping.Elevation;
import br.com.pathwheel.mapping.GeographicCoordinate;
import br.com.pathwheel.mapping.PavementSegment;
import br.com.pathwheel.mapping.Polyline;
import br.com.pathwheel.mapping.Route;
import br.com.pathwheel.model.PavementSample;
import br.com.pathwheel.model.Spot;
import br.com.pathwheel.request.RouteGoogleRequest;
import br.com.pathwheel.request.RouteMapRequest;
import br.com.pathwheel.response.OverviewResponse;
import br.com.pathwheel.response.RouteMapResponse;
import br.com.pathwheel.security.ApiKey;

@Path("/v1/mapping")
public class MappingEndpoint implements JdbcDataAccessObjectListener {
	
	@GET
	@Path("/overview")
	@Produces("application/json; charset=utf-8")
	public OverviewResponse overview(@QueryParam("northeast") String northeast, @QueryParam("southwest") String southwest, @QueryParam("travelModeId") Integer travelModeId) {
		Logger.info("==[/v1/mapping/overview]= northeast="+northeast+", southwest="+southwest+", travelModeId="+travelModeId);
		OverviewResponse response = new OverviewResponse();
		try {
			
			GeographicCoordinate geoNortheast = GeographicCoordinate.parse(northeast);
			GeographicCoordinate geoSothwest = GeographicCoordinate.parse(southwest);
			List<GeographicCoordinate> verticesPolygon = new ArrayList<GeographicCoordinate>();
			verticesPolygon.add(geoNortheast);
			verticesPolygon.add(new GeographicCoordinate(geoSothwest.getLatitude(),geoNortheast.getLongitude()));
			verticesPolygon.add(geoSothwest);
			verticesPolygon.add(new GeographicCoordinate(geoNortheast.getLatitude(),geoSothwest.getLongitude()));
			verticesPolygon.add(geoNortheast);
					
			PavementSampleDAO pavementSampleDAO = new JdbcPavementSampleDAO(this);
			response.setSamples(pavementSampleDAO.fetchByArea(verticesPolygon, travelModeId));					
			
			SpotDAO spotDao = new JdbcSpotDAO(this);
			response.setSpots(spotDao.fetchByArea(verticesPolygon, travelModeId));
			
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
	
	@POST
	@Path("/route/map")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	public RouteMapResponse routeMap(String jsonRequest) {
		Logger.info("==[/v1/route/map]=");
		RouteMapResponse response = new RouteMapResponse();
		try {
			
			RouteMapRequest request = new ObjectMapper().readValue(jsonRequest.getBytes("UTF-8"), RouteMapRequest.class);
			Logger.info(request.toString());
			
			List<GeographicCoordinate> coordinateSamples = new ArrayList<GeographicCoordinate>();
			List<PavementSegment> pavementSegments = new ArrayList<PavementSegment>();
			List<Spot> spots = new ArrayList<Spot>();
			double totalDistance = 0;
			double avgSpeed = 0;
			
			int countPavementSamples = 0;
			GeographicCoordinate previousCoordinate = null;
			for(GeographicCoordinate coordinate : request.getCoordinates()) {
				double distance = 0;
				double angle = 0;
				if(previousCoordinate != null) {
	                distance = GeographicCoordinate.getDistanceInMeters(previousCoordinate, coordinate);
	                angle = GeographicCoordinate.getAngle(previousCoordinate, coordinate);
	            }
				int sampleLength = 30;
	            if(distance > sampleLength) {
	                GeographicCoordinate referenceCoordinate = previousCoordinate;
	                
	                int fatias = (int)(distance/sampleLength);
	                
	                for(int i=0;i<fatias;i++) {
	                    referenceCoordinate = GeographicCoordinate.byReference(referenceCoordinate,sampleLength,angle);
	                    coordinateSamples.add(referenceCoordinate);
	                }	                
	            }
	            coordinateSamples.add(coordinate);
                totalDistance+=distance;
                previousCoordinate = coordinate;
	            
			}
			
			//getting elevations of coordinates
			List<Double> elevations = new ArrayList<Double>();
			try {
				elevations = Elevation.fromGoogleMapsApi(coordinateSamples, ApiKey.googleApiKey);
			} catch(Exception ex1) {
				Logger.error("google: "+ex1.getMessage());
				try {
					elevations = Elevation.fromOpenElevationApi(coordinateSamples);
				} catch(Exception ex2) {
					Logger.error("open-elevation: "+ex2.getMessage());	
				}
			} 
			
			previousCoordinate = null;
			Double previousElevation = null;
			PavementSampleDAO pavementSampleDao = new JdbcPavementSampleDAO(null);
			SpotDAO spotDao = new JdbcSpotDAO(null);
			
			for(GeographicCoordinate coordinate : coordinateSamples) {
				if(previousCoordinate == null) {
	                previousCoordinate = coordinate;
	                if(elevations.size() > 0) {
	                    previousElevation = elevations.remove(0);
	                }
	                continue;
	            }
				
				List<GeographicCoordinate> verticesPolygon = GeographicCoordinate.listVerticesNearPolygon(previousCoordinate, coordinate);
				List<PavementSample> pavementSamples = pavementSampleDao.fetchByArea(verticesPolygon, request.getTravelModeId());
				
				for(PavementSample pavementSample : pavementSamples) {
	                avgSpeed+=pavementSample.getSpeed();
	            }
	            countPavementSamples = countPavementSamples + pavementSamples.size();
	            
	            PavementSegment pavementSegment = new PavementSegment();
	            pavementSegment.setCoordinateInit(previousCoordinate);
	            pavementSegment.setCoordinateEnd(coordinate);
	            pavementSegment.setVerticalAcceleration(pavementSamples);
	            if(elevations.size() > 0) {
	                pavementSegment.setSlopePercentagem(previousElevation, elevations.get(0), GeographicCoordinate.getDistanceInMeters(previousCoordinate, coordinate));
	            }
	            pavementSegments.add(pavementSegment);
	            
	            spots.addAll(spotDao.fetchByArea(verticesPolygon, request.getTravelModeId()));
	            
	            if(elevations.size() > 0) {
	                previousElevation = elevations.remove(0);
	            }
	            previousCoordinate = coordinate;
			}
			
			if(countPavementSamples > 0) {
	            avgSpeed = avgSpeed/countPavementSamples;
	        }
	        
	        double estimatedTime = avgSpeed > 0 ? totalDistance/(avgSpeed/3.6) : 0;
			
			
	        response.setAvgSpeed(avgSpeed);
	        response.setCoordinateSamples(coordinateSamples);
	        response.setEstimatedTime(estimatedTime);
	        response.setPavementSegments(pavementSegments);
	        response.setSpots(spots);
	        response.setTotalDistance(totalDistance);
			
			
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
	
	@POST
	@Path("/route/google")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	public RouteMapResponse routeGoogle(String jsonRequest) {
		Logger.info("==[/v1/route/google]=");
		RouteMapResponse response = new RouteMapResponse();
		try {
			
			RouteGoogleRequest request = new ObjectMapper().readValue(jsonRequest.getBytes("UTF-8"), RouteGoogleRequest.class);
			
			Route route;
			try {
				route = Route.fromOpenSourceRouteMachine(
						request.getOrigin().getLatitude()+","+request.getOrigin().getLongitude(), 
						request.getDestination().getLatitude()+","+request.getDestination().getLongitude(), 
						Route.FOOT);
			} catch(Exception e1) {
				Logger.info("foot: "+e1.getMessage());
				try {
					route = Route.fromOpenSourceRouteMachine(
							request.getOrigin().getLatitude()+","+request.getOrigin().getLongitude(), 
							request.getDestination().getLatitude()+","+request.getDestination().getLongitude(), 
							Route.BIKE);
				} catch(Exception e2) {
					Logger.info("bike: "+e2.getMessage());
					try {
						route = Route.fromOpenSourceRouteMachine(
								request.getOrigin().getLatitude()+","+request.getOrigin().getLongitude(), 
								request.getDestination().getLatitude()+","+request.getDestination().getLongitude(), 
								Route.CAR);
					} catch(Exception e3) {
						Logger.info("car: "+e3.getMessage());
						route = Route.fromGoogleMapsApi(
								request.getOrigin().getLatitude()+","+request.getOrigin().getLongitude(), 
								request.getDestination().getLatitude()+","+request.getDestination().getLongitude(), 
								ApiKey.googleApiKey);
					}
				}
			}
			
			
			List<GeographicCoordinate> coordinates = Polyline.decode(route.getOverviewPolylinePoints());
			
			RouteMapRequest routeMapRequest = new RouteMapRequest();
			routeMapRequest.setCoordinates(coordinates);
			
			ObjectMapper mapper = new ObjectMapper();
			
			response = routeMap(mapper.writeValueAsString(routeMapRequest));
			
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
