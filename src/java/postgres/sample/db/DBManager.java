package java.postgres.sample.db;

import java.postgres.sample.util.Util;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DBManager {

	private DriverManagerDataSource dataSource = null;

	public DBManager() {
		try {
			dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("org.postgresql.Driver");
			String dbUrl = Util.getDBIp();
			String username = Util.getDBUser();
			String password = Util.getDBPassword();
			String dbName = Util.getDBName();
			int port = Util.getDBPort();
			System.out.println("DBURL: " + dbUrl + " UserName: " + username
					+ " Password: " + password + " Port: " + port + " DBName: "
					+ dbName);
			dataSource.setUrl("jdbc:postgresql://" + dbUrl + ":" + port + "/"
					+ dbName + "?autoReconnect=true");
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			createTable("CREATE TABLE app42_user(name varchar(255), email varchar(255), description text)");
		} catch (Exception e) {
			// handle Exception
			System.out.println("Table Already Created");
		}
	}

	private static final DBManager dsManager = new DBManager();

	public static DBManager getInstance() {
		return dsManager;
	}

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Map<String, Object>> select(String sqlQuery)
			throws Exception {
		JdbcTemplate db = null;
		try {
			db = new JdbcTemplate(DBManager.getInstance().getDataSource());
		} catch (Exception e) {
			throw e;
		}

		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			List<Map<String, Object>> result = db.queryForList(sqlQuery);
			for (int i = 0; i < result.size(); i++) {
				Map<String, Object> rowMap = result.get(i);
				resultList.add(rowMap);
			}
		} catch (Exception e) {
			throw new Exception("Error executing query: " + sqlQuery);
		} finally {
			dataSource.getConnection().close();
		}
		return resultList;

	}

	/**
	 * Insert (This function inserts data into the table)
	 * 
	 * @param query
	 * @throws SQLException
	 */
	public void insert(final String query) throws Exception{
		JdbcTemplate db = new JdbcTemplate(DBManager.getInstance()
				.getDataSource());

		KeyHolder keyHolder = new GeneratedKeyHolder();
		db.update(new PreparedStatementCreator() {
			@Override
			public java.sql.PreparedStatement createPreparedStatement(
					java.sql.Connection connection) throws SQLException {
				PreparedStatement ps = (PreparedStatement) connection
						.prepareStatement(query);
				// ps.setString(1, name);
				return ps;
			}
		}, keyHolder);
		dataSource.getConnection().close();
		
	}

	/*
	 * Create table
	 */
	public static void createTable(String query) throws SQLException {
		JdbcTemplate db = new JdbcTemplate(DBManager.getInstance()
				.getDataSource());
		try {
			db.execute(query);
		} catch (Exception e) {
			System.out.println("---------------EXCEPTION------" + e);
			throw new SQLException("Error while executing query: ' " + query
					+ " '");
		}
	}

}
