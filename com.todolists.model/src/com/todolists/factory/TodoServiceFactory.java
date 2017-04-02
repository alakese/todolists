package com.todolists.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.model.ITodoService;
import com.todolists.model.service.MyTodoServiceImpl;

/**
 * Creates a new service implementation for todo-objects
 *
 * @author Yasin Alakese
 * @datum 27.11.2016
 */
public class TodoServiceFactory {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(TodoServiceFactory.class);

	/**
	 * Creating a new instance of todo-service
	 *
	 * @param tableName
	 *            Name of the table will be used as the name of the service
	 * @return A new instance of todo-service
	 */
	public static ITodoService getInstance(final String tableName) {
		TodoServiceFactory.LOGGER.debug("Creating the service provider [{}].", tableName);
		final MyTodoServiceImpl service = new MyTodoServiceImpl(tableName);
		TodoServiceFactory.LOGGER.debug("Service created successfully.");
		return service;
	}
}
