package robomap.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface ConnectionManager {
	
	public Connection getConnection();
	
	public void close(Connection connection);
	
	void close(PreparedStatement statement);

	void close(ResultSet result);
	
	public void close(Connection connection, PreparedStatement statement);
	
	public void close(Connection connection, PreparedStatement statement, ResultSet resultSet);	

}
