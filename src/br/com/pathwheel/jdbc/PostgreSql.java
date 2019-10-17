package br.com.pathwheel.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGPoolingDataSource;

public abstract class PostgreSql {
	
	private static PGPoolingDataSource source;
	
	public static synchronized void init(String dataSourceName, String serverName, String databaseName, String user, String password, int initialConns, int maxConns) {
		if(source == null) {
			source = new PGPoolingDataSource();
			source.setDataSourceName(dataSourceName);
			source.setServerName(serverName);
			source.setDatabaseName(databaseName);
			source.setUser(user);
			source.setPassword(password);
			source.setInitialConnections(initialConns);
			source.setMaxConnections(maxConns);
		}
	}

	public static synchronized PGPoolingDataSource getSource() {
		return source;
	}
	
	public static synchronized Connection getConnection() throws SQLException {
		return source.getConnection();
	}
}
