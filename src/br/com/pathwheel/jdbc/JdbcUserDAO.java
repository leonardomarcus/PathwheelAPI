package br.com.pathwheel.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.pathwheel.dao.UserDAO;
import br.com.pathwheel.exception.AuthenticationFailureException;
import br.com.pathwheel.exception.UserNotFoundException;
import br.com.pathwheel.model.User;

public class JdbcUserDAO extends JdbcDataAccessObject implements UserDAO {
	
	public JdbcUserDAO(JdbcDataAccessObjectListener listener) {
		super.listener = listener;
	}

	@Override
	public User authenticate(String login, String secret) throws UserNotFoundException, AuthenticationFailureException {
		Connection conn = null;
		try
		{
		    conn = PostgreSql.getConnection();
		    String sql = "select id, username from pathwheel.user where exclusion_date is null and (username = ? or email = ?)";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    int i = 1;
		    stmt.setObject(i++, login, java.sql.Types.VARCHAR);
		    stmt.setObject(i++, login, java.sql.Types.VARCHAR);		    
		    log(stmt.toString());
		    
		    ResultSet rs = stmt.executeQuery();
		    if (!rs.isBeforeFirst())    
			    throw new UserNotFoundException();
		    
		    sql = "select id, username, full_name, email, registration_date from pathwheel.user where exclusion_date is null and (username = ? or email = ?) and secret = ?";
		    stmt = conn.prepareStatement(sql);
		    i = 1;
		    stmt.setObject(i++, login, java.sql.Types.VARCHAR);
		    stmt.setObject(i++, login, java.sql.Types.VARCHAR);
		    stmt.setObject(i++, secret, java.sql.Types.VARCHAR);
		    log(stmt.toString());
		    rs = stmt.executeQuery();
		    
		    if (!rs.isBeforeFirst())    
			    throw new AuthenticationFailureException();
		    
		    User user = new User();
		    while (rs.next()) {
				user = User.parse(rs);
			}
		    
		    stmt.execute();
	        stmt.close();
	        return user;	        
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
		   closeConnection(conn);
		}
	}

}
