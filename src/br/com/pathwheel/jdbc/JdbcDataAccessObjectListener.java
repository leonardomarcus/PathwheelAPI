package br.com.pathwheel.jdbc;

public interface JdbcDataAccessObjectListener {
	void onErroJdbcDataAccessObject(Object sender, Exception e);
	void onLogJdbcDataAccessObject(Object sender, String msg);
}
