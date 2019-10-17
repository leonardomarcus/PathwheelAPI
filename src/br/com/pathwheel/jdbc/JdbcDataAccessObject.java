package br.com.pathwheel.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class JdbcDataAccessObject {
	
	protected JdbcDataAccessObjectListener listener;
	
	public JdbcDataAccessObject() { }
	
	public JdbcDataAccessObject(JdbcDataAccessObjectListener listener) {
		this.listener = listener;
	}
	
	public void setListener(JdbcDataAccessObjectListener listener) {
		this.listener = listener;
	}

	protected void log(String msg) {
		if(msg.startsWith("Pooled statement wrapping physical statement ")) {
			msg = msg.replaceFirst("Pooled statement wrapping physical statement ", "");
		}
		//System.out.println(msg);
		if(this.listener != null)
			this.listener.onLogJdbcDataAccessObject(this, msg);
	}
	
	protected void closeConnection(Connection conn) {
		if (conn != null)  {
	        try { conn.close(); } catch (SQLException e) {}
	    }
	}
}
