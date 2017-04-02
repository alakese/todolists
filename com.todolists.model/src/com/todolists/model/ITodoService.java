package com.todolists.model;

import java.util.Date;
import java.util.List;

import com.todolists.datamodel.Todo;

public interface ITodoService {
	/**
	 * Creates a new todo
	 *
	 * @param id
	 *            id
	 * @param state
	 *            State of the todo
	 * @param description
	 *            Description of the todo : decription of the problem
	 * @param action
	 *            Action of the todo : what did i do to solve this problem
	 * @param result
	 *            Result of the todo : what did happen
	 * @param endDate
	 *            Date to finish the todo
	 * @return Created todo
	 */
	Todo createTodo(final long id, final String state, final String description, final String action,
			final String result, final Date endDate);

	Todo createTodo(final String state, final String description, final String action, final String result,
			final Date endDate);

	Todo getTodo(long id);

	boolean saveTodo(Todo todo);

	boolean deleteTodo(long id);

	List<Todo> getTodos();

	void clearTodos();

	String getServiceName();
}
