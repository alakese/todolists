package com.todolists.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.datamodel.Todo;

public class DBActions {

	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(DBActions.class);

	/** */
	private static final String SELECT_COMMAND = "SELECT * FROM TODOS;";

	/** */
	private static final String TABLE_COMMAND = "CREATE TABLE TODOS (ID INT PRIMARY KEY NOT NULL, "
			+ "STATE TEXT NOT NULL, DESCRIPTION TEXT NOT NULL, ACTION TEXT NOT NULL, RESULT TEXT NOT NULL, "
			+ "ENDDATE TEXT NOT NULL)";

	/**
	 *
	 * @param fileName
	 * @param projectName
	 * @throws DBActionsException
	 */
	public static void createDatabase(final String projectName, final String fileName) throws DBActionsException {
		Connection c = null;
		Statement stmt = null;
		final StringBuffer sb = new StringBuffer(255);
		sb.append("jdbc:sqlite:");
		sb.append(projectName);
		sb.append('/');
		sb.append(fileName);
		final String sqliteConnection = sb.toString();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqliteConnection);
			DBActions.LOGGER.debug("[createDatabase] Database opened successfully");
			stmt = c.createStatement();
			stmt.executeUpdate(DBActions.TABLE_COMMAND);
			stmt.close();
			c.close();
		} catch (final Exception e) {
			DBActions.LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
			throw new DBActionsException(e);
		}
		DBActions.LOGGER.debug("[createDatabase] Table created successfully");
	}

	/**
	 * @param fileName
	 * @param absPath
	 * @param todo
	 */
	public static void insertElement(final String absPath, final String fileName, final Todo todo) {
		Connection c = null;
		Statement stmt = null;
		final StringBuffer sb = new StringBuffer(255);
		sb.append("jdbc:sqlite:");
		sb.append(absPath);
		sb.append('/');
		sb.append(fileName);
		final String sqliteConnection = sb.toString();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqliteConnection);
			DBActions.LOGGER.debug("[insertElement] Database opened successfully");
			final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
			final String endDateformat = formatter.format(todo.getEndDate());
			stmt = c.createStatement();
			final String sql = "INSERT INTO \'TODOS\' (ID,STATE,DESCRIPTION,ACTION,RESULT,ENDDATE) VALUES ("
					+ todo.getId() + ", \"" + todo.getState() + "\", \"" + todo.getDescription() + "\", \""
					+ todo.getAction() + "\", \"" + todo.getResult() + "\", \"" + endDateformat + "\");";

			stmt.executeUpdate(sql);

			stmt.close();
			c.close();
		} catch (final Exception e) {
			DBActions.LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		DBActions.LOGGER.debug("[insertElement] Todo inserted successfully : " + todo.toString());
	}

	/**
	 *
	 * @param absPath
	 * @param fileName
	 * @param todo
	 */
	public static void updateElement(final String absPath, final String fileName, final Todo todo) {
		Connection c = null;
		Statement stmt = null;
		final StringBuffer sb = new StringBuffer(255);
		sb.append("jdbc:sqlite:");
		sb.append(absPath);
		sb.append('/');
		sb.append(fileName);
		final String sqliteConnection = sb.toString();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqliteConnection);
			DBActions.LOGGER.debug("[updateElement] Database opened successfully");
			final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
			final String endDateformat = formatter.format(todo.getEndDate());
			stmt = c.createStatement();
			final String sql = "UPDATE \'TODOS\' SET STATE=\"" + todo.getState() + "\",DESCRIPTION=\""
					+ todo.getDescription() + "\",ACTION=\"" + todo.getAction() + "\",RESULT=\"" + todo.getResult()
					+ "\",ENDDATE=\"" + endDateformat + "\" WHERE ID=" + todo.getId() + ";";
			stmt.executeUpdate(sql);

			stmt.close();
			c.close();
		} catch (final Exception e) {
			DBActions.LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		DBActions.LOGGER.debug("[updateElement] Todo updated successfully : " + todo.toString());
	}

	/**
	 *
	 * @param fullPathToFile
	 * @return
	 */
	public static List<Todo> getAllTodosFromDB(final String fullPathToFile) {
		final List<Todo> todos = new ArrayList<>();

		Connection c = null;
		Statement stmt = null;
		final StringBuffer sb = new StringBuffer(255);
		sb.append("jdbc:sqlite:");
		sb.append(fullPathToFile);
		final String sqliteConnection = sb.toString();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(sqliteConnection);
			DBActions.LOGGER.debug("[getAllTodosFromDB] Database opened successfully");
			stmt = c.createStatement();
			final ResultSet rs = stmt.executeQuery(DBActions.SELECT_COMMAND);
			while (rs.next()) {
				final int id = rs.getInt("id");
				final String state = rs.getString("state");
				final String desc = rs.getString("description");
				final String action = rs.getString("action");
				final String result = rs.getString("result");
				final String date = rs.getString("enddate");
				/* state, description, action and result, date */
				final DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
				final Date enddate = format.parse(date);
				final Todo todo = new Todo(id, state, desc, action, result, enddate);
				todos.add(todo);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (final Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return todos;
	}

}
