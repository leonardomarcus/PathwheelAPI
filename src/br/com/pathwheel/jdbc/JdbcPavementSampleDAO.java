package br.com.pathwheel.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.pathwheel.dao.PavementSampleDAO;
import br.com.pathwheel.mapping.GeographicCoordinate;
import br.com.pathwheel.model.PavementSample;
import br.com.pathwheel.model.TravelMode;

public class JdbcPavementSampleDAO extends JdbcDataAccessObject implements PavementSampleDAO {
	
	public JdbcPavementSampleDAO(JdbcDataAccessObjectListener listener) {
		super.listener = listener;
	}

	public void register(PavementSample data, String smartDevice) {
		Connection conn = null;
		try	{
			
			conn = PostgreSql.getConnection();
		    
		    int smartDeviceId = 0;
		    
		    String sql = "SELECT id FROM pathwheel.smart_device where description = ?;"; 
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    int i = 1;
		    stmt.setObject(i++, smartDevice, java.sql.Types.VARCHAR);
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    while(rs.next())
		    	smartDeviceId = (rs.getInt("id"));
		    stmt.close();
		    
		    if(smartDeviceId == 0) {
		    	sql = "INSERT INTO pathwheel.smart_device (description) VALUES (?) RETURNING id;";
		    	stmt = conn.prepareStatement(sql);
		    	i = 1;
			    stmt.setObject(i++, smartDevice, java.sql.Types.VARCHAR);
			    log(stmt.toString());
		    	rs = stmt.executeQuery();

			    while(rs.next()) {
			    	smartDeviceId = rs.getInt("id");
			    }
			    stmt.close();
		    }
			
			
			sql = "INSERT INTO pathwheel.pavement_test_data(latitude_init, longitude_init, latitude_end, longitude_end, elapsed_time, vertical_acceleration, speed, distance, accuracy, pavement_test_id, user_id, smart_device_id, steps, travel_mode_id) "
		    		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, null, ?, ?, ?, ?) returning id;";
		    
			stmt = conn.prepareStatement(sql);			    
		    i= 1;
		    
		    stmt.setObject(i++, data.getLatitudeInit(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, data.getLongitudeInit(), java.sql.Types.DOUBLE);			    
		    stmt.setObject(i++, data.getLatitudeEnd(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, data.getLongitudeEnd(), java.sql.Types.DOUBLE);			    
		    stmt.setObject(i++, data.getElapsedTime(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, data.getVerticalAcceleration(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, data.getSpeed(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, data.getDistance(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, data.getAccuracy(), java.sql.Types.DOUBLE);
		    
		    stmt.setObject(i++, data.getUser() != null ? data.getUser().getId() : null, java.sql.Types.DOUBLE);
		    stmt.setObject(i++, smartDeviceId, java.sql.Types.INTEGER);
		    stmt.setObject(i++, data.getSteps(), java.sql.Types.INTEGER);
		    
		    int travelModeId = TravelMode.detect(data.getTravelModeId(), data.getSpeed(), data.getDistance(), data.getSteps());
		    stmt.setObject(i++, travelModeId, java.sql.Types.INTEGER);
		    
		    log(stmt.toString());
		    rs = stmt.executeQuery();
		    long pavementTestDataId = 0;
		    while(rs.next()) {
		    	pavementTestDataId = rs.getLong("id");
		    }
		    stmt.close();
		    
		    for(Double verticalAcceleration : data.getVerticalAccelerations()) {
			    sql = "INSERT INTO pathwheel.pavement_test_data_va(pavement_test_data_id, vertical_acceleration) VALUES (?, ?);";
			    stmt = conn.prepareStatement(sql);			    
			    i= 1;
			    stmt.setObject(i++, pavementTestDataId, java.sql.Types.BIGINT);
			    stmt.setObject(i++, verticalAcceleration, java.sql.Types.DOUBLE);
			    stmt.execute();
			    stmt.close();
		    }
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			if(this.listener != null)
				this.listener.onErroJdbcDataAccessObject(this, e);
			throw new RuntimeException(e.getMessage());
		}
		finally
		{
		   closeConnection(conn);
		}

	}

	@Override
	public List<PavementSample> fetchByArea(List<GeographicCoordinate> verticesPolygon, int travelModeId) {
		List<PavementSample> samples = new ArrayList<PavementSample>();
		String polygon = "";
        for(GeographicCoordinate vertex : verticesPolygon) {
            if(!polygon.equals("")) {
                polygon+=",";
            }
            polygon+=vertex.getLatitude()+" "+vertex.getLongitude();
        }
        polygon+= ","+verticesPolygon.get(0).getLatitude()+" "+verticesPolygon.get(0).getLongitude();
        
		Connection conn = null;
		try	{
			conn = PostgreSql.getConnection();
		    
			String sql = "SELECT ptd.id, ptd.latitude_init, ptd.longitude_init, ptd.latitude_end, ptd.longitude_end, "+ 
	                   "ptd.elapsed_time, ptd.vertical_acceleration, ptd.speed, ptd.distance, ptd.accuracy, "+ 
	                   "ptd.pavement_test_id "+
	                   "FROM pathwheel.pavement_test_data ptd "+
		            "LEFT join pathwheel.pavement_test pt ON pt.id = ptd.pavement_test_id "+ 
		            "WHERE ptd.registration_date BETWEEN localtimestamp - cast('1 year' as interval) AND localtimestamp "+
		            "AND ptd.travel_mode_id = ? "+
		            "AND public.ST_Intersects( "+
		            		"public.ST_GeographyFromText('LINESTRING('||ptd.latitude_init||' '||ptd.longitude_init||','||ptd.latitude_end||' '||ptd.longitude_end||')'), "+
		            		"public.ST_GeographyFromText('POLYGON(("+polygon+"))') "+
				      ") AND ptd.exclusion_date is null "+
		            "LIMIT 3000;";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    
		    int i = 1;
		    stmt.setObject(i++, travelModeId, java.sql.Types.INTEGER);
		    
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    while(rs.next()) {
		    	PavementSample pavementSample = new PavementSample();
		    	pavementSample.setId(rs.getLong("id"));
		    	pavementSample.setLatitudeInit(rs.getDouble("latitude_init"));
		    	pavementSample.setLongitudeInit(rs.getDouble("longitude_init"));
		    	pavementSample.setLatitudeEnd(rs.getDouble("latitude_end"));
		    	pavementSample.setLongitudeEnd(rs.getDouble("longitude_end"));
		    	pavementSample.setElapsedTime(rs.getDouble("elapsed_time"));
		    	pavementSample.setVerticalAcceleration(rs.getDouble("vertical_acceleration"));
		    	pavementSample.setSpeed(rs.getDouble("speed"));
		    	pavementSample.setDistance(rs.getDouble("distance"));
		    	pavementSample.setAccuracy(rs.getDouble("accuracy"));
		    	samples.add(pavementSample);
		    }
		    return samples;
		} 
		catch (SQLException e) {
			e.printStackTrace();
			if(this.listener != null)
				this.listener.onErroJdbcDataAccessObject(this, e);
			throw new RuntimeException(e.getMessage());
		}
		finally {
		   closeConnection(conn);
		}
	}

}
