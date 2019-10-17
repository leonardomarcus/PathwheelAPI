package br.com.pathwheel.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.pathwheel.dao.SpotDAO;
import br.com.pathwheel.mapping.GeographicCoordinate;
import br.com.pathwheel.model.Spot;
import br.com.pathwheel.model.SpotReportType;
import br.com.pathwheel.model.SpotType;
import br.com.pathwheel.model.User;

public class JdbcSpotDAO extends JdbcDataAccessObject implements SpotDAO {
	
	public JdbcSpotDAO(JdbcDataAccessObjectListener listener) {
		super.listener = listener;
	}

	@Override
	public Spot register(Spot spot) {
		Connection conn = null;
		try
		{
		    conn = PostgreSql.getConnection();
		    String sql = "INSERT INTO pathwheel.spot(spot_type_id, latitude, longitude, comment, user_id, picture, travel_mode_id) VALUES (?, ?, ?, ?, ?, decode(?,'base64')::bytea, ?) returning id, TO_CHAR(registration_date,'dd/mm/yyyy hh24:mi:ss') as registration_date;";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    int i = 1;
		    stmt.setObject(i++, spot.getSpotType().getId(), java.sql.Types.INTEGER);
		    stmt.setObject(i++, spot.getLatitude(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, spot.getLongitude(), java.sql.Types.DOUBLE);
		    stmt.setObject(i++, spot.getComment(), java.sql.Types.VARCHAR);
		    stmt.setObject(i++, spot.getUser().getId(), java.sql.Types.BIGINT);
		    stmt.setObject(i++, spot.getPicture(), java.sql.Types.VARCHAR);
		    stmt.setObject(i++, spot.getTravelModeId(), java.sql.Types.INTEGER);
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    while(rs.next()) {
		    	spot.setId(rs.getLong("id"));
		    	spot.setRegistrationDate(rs.getString("registration_date"));
		    }
		    
	        stmt.close();
	        return spot;
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
	public List<Spot> fetchByArea(List<GeographicCoordinate> verticesPolygon, int travelModeId) {
		List<Spot> spots = new ArrayList<Spot>();
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
		    
		    String sql = "SELECT "+
		            "s.id, s.spot_type_id, TO_CHAR(s.registration_date,'dd/mm/yyyy hh24:mi:ss') as registration_date, s.latitude, s.longitude, s.comment, "+ 
                    "s.user_id, u.username, "+
		            "(CASE WHEN s.picture IS NOT NULL THEN true ELSE false END) as has_picture, "+
					"(SELECT count(*) from pathwheel.spot_report WHERE spot_id = s.id AND spot_report_type_id = 1) as count_still_there, "+
					"(SELECT count(*) from pathwheel.spot_report WHERE spot_id = s.id AND spot_report_type_id = 2) as count_not_there "+
         "FROM pathwheel.spot s "+ 
         "LEFT JOIN pathwheel.user u ON u.id = s.user_id "+
         "WHERE "+
         "public.ST_Intersects( "+
         		"public.ST_GeographyFromText('POINT('||s.latitude||' '||s.longitude||')'), "+
         		"public.ST_GeographyFromText('POLYGON(("+polygon+"))') "+
		  ") "+
         "AND s.exclusion_date is null "+
         "AND s.travel_mode_id = ? "+
         "AND s.update_date BETWEEN localtimestamp - cast('1 month' as interval) AND localtimestamp "+
         "LIMIT 500;";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    int i = 1;
		    stmt.setObject(i++, travelModeId, java.sql.Types.INTEGER);
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    while(rs.next()) {
		    	Spot spot = new Spot();
		    	spot.setId(rs.getLong("id"));
		    	SpotType spotType = new SpotType();
		    	spotType.setId(rs.getInt("spot_type_id"));
		    	spot.setSpotType(spotType);
		    	spot.setRegistrationDate(rs.getString("registration_date"));
		    	spot.setLatitude(rs.getDouble("latitude"));
		    	spot.setLongitude(rs.getDouble("longitude"));
		    	spot.setComment(rs.getString("comment"));
		    	spot.setHasPicture(rs.getBoolean("has_picture"));
		    	spot.setCountStillThere(rs.getInt("count_still_there"));
		    	spot.setCountNotThere(rs.getInt("count_not_there"));
		    	//spot.setPicture(rs.getString("picture"));
		    	
		    	User user = new User();
		    	user.setId(rs.getLong("user_id"));
		    	user.setUsername(rs.getString("username"));
		    	spot.setUser(user);
		    	
		    	spots.add(spot);
		    }
		    return spots;
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

	@Override
	public Spot fetch(long id) {

		Connection conn = null;
		try	{
			conn = PostgreSql.getConnection();
		    
		    String sql = "SELECT "+
		            "s.id, s.spot_type_id, TO_CHAR(s.registration_date,'dd/mm/yyyy hh24:mi:ss') as registration_date, s.latitude, s.longitude, s.comment, "+ 
                    "s.user_id, encode(s.picture, 'base64') as picture, u.username, "+
		            "(CASE WHEN s.picture IS NOT NULL THEN true ELSE false END) as has_picture, "+
					"(SELECT count(*) from pathwheel.spot_report WHERE spot_id = s.id AND spot_report_type_id = 1) as count_still_there, "+
					"(SELECT count(*) from pathwheel.spot_report WHERE spot_id = s.id AND spot_report_type_id = 2) as count_not_there "+
			         "FROM pathwheel.spot s "+ 
			         "LEFT JOIN pathwheel.user u ON u.id = s.user_id "+
			         "WHERE s.id = ? AND s.exclusion_date IS NULL";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    
		    int i = 1;
		    stmt.setObject(i++, id, java.sql.Types.BIGINT);
		    
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    
		    Spot spot = new Spot();
		    while(rs.next()) {	    	
		    	spot.setId(rs.getLong("id"));
		    	SpotType spotType = new SpotType();
		    	spotType.setId(rs.getInt("spot_type_id"));
		    	spot.setSpotType(spotType);
		    	spot.setRegistrationDate(rs.getString("registration_date"));
		    	spot.setLatitude(rs.getDouble("latitude"));
		    	spot.setLongitude(rs.getDouble("longitude"));
		    	spot.setComment(rs.getString("comment"));
		    	spot.setPicture(rs.getString("picture"));
		    	spot.setHasPicture(rs.getBoolean("has_picture"));
		    	spot.setCountStillThere(rs.getInt("count_still_there"));
		    	spot.setCountNotThere(rs.getInt("count_not_there"));
		    	
		    	User user = new User();
		    	user.setId(rs.getLong("user_id"));
		    	user.setUsername(rs.getString("username"));
		    	spot.setUser(user);
		    }
		    return spot;
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
	
	@Override
	public void remove(long spotId) {
		Connection conn = null;
		try	{
			conn = PostgreSql.getConnection();
			
			String sql = "UPDATE pathwheel.spot SET exclusion_date = localtimestamp WHERE id = ?;";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);		    
		    
		    int i = 1;
		    stmt.setObject(i++, spotId, java.sql.Types.DOUBLE);		    
		    
		    log(stmt.toString());
		    stmt.execute();
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

	@Override
	public void report(long spotId, long spotUser, int spotReportTypeId) {
		Connection conn = null;
		try	{
			conn = PostgreSql.getConnection();
			
			String sql = "INSERT INTO pathwheel.spot_report (spot_id, user_id, spot_report_type_id, report_date) VALUES (?, ?, ?, localtimestamp);";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);		    
		    
		    int i = 1;
		    stmt.setObject(i++, spotId, java.sql.Types.BIGINT);
		    stmt.setObject(i++, spotUser, java.sql.Types.BIGINT);
		    stmt.setObject(i++, spotReportTypeId, java.sql.Types.INTEGER);
		    
		    log(stmt.toString());
		    stmt.execute();
		    
		    if(spotReportTypeId == SpotReportType.STILL_THERE) {
		    	sql = "UPDATE pathwheel.spot SET update_date = LOCALTIMESTAMP WHERE id = ?;";
		    	stmt = conn.prepareStatement(sql);
		    	i = 1;
			    stmt.setObject(i++, spotId, java.sql.Types.DOUBLE);
			    log(stmt.toString());
			    stmt.execute();
		    }
		    
		    stmt.close();
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
	
	@Override
	public boolean hasReport(long spotId, long spotUser, int sporReportTypeId) {
		Connection conn = null;
		try	{
			conn = PostgreSql.getConnection();
			
			String sql = "SELECT count(*) AS qtd FROM pathwheel.spot_report WHERE spot_id = ? AND user_id = ? AND spot_report_type_id = ?;";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);		    
		    
		    int i = 1;
		    stmt.setObject(i++, spotId, java.sql.Types.BIGINT);
		    stmt.setObject(i++, spotUser, java.sql.Types.BIGINT);
		    stmt.setObject(i++, sporReportTypeId, java.sql.Types.INTEGER);
		    
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    boolean exists = false;
		    while(rs.next()) {
		    	log("qtd: "+rs.getInt("qtd"));
		    	if(rs.getInt("qtd") > 0)
		    		exists = true;
		    }
		    return exists;
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
	
	@Override
	public int countReports(long spotId, int sporReportTypeId) {
		Connection conn = null;
		try	{
			conn = PostgreSql.getConnection();
			
			String sql = "select count(*) as qtd from pathwheel.spot_report where spot_id = ? and spot_report_type_id = ?;";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);		    
		    
		    int i = 1;
		    stmt.setObject(i++, spotId, java.sql.Types.BIGINT);
		    stmt.setObject(i++, sporReportTypeId, java.sql.Types.INTEGER);
		    
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    int count = 0;
		    while(rs.next()) {
		    	count = rs.getInt("qtd");
		    }
		    return count;
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
	
	@Override
	public boolean byUser(long spotId, long spotUser) {
		Connection conn = null;
		try	{
			conn = PostgreSql.getConnection();
			
			String sql = "select count(*) as qtd from pathwheel.spot where id = ? and user_id = ?;";
		    
		    PreparedStatement stmt = conn.prepareStatement(sql);		    
		    
		    int i = 1;
		    stmt.setObject(i++, spotId, java.sql.Types.BIGINT);
		    stmt.setObject(i++, spotUser, java.sql.Types.BIGINT);
		    
		    log(stmt.toString());
		    ResultSet rs = stmt.executeQuery();
		    boolean exists = false;
		    while(rs.next()) {
		    	if(rs.getInt("qtd") > 0)
		    		exists = true;
		    }
		    return exists;
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
