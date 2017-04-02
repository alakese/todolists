package com.todolists.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.datamodel.Todo;
import com.todolists.model.ITodoService;

/**
 *
 *
 * @author Yasin Alakese
 * @date 22.11.2016
 */
public class MyTodoServiceImpl implements ITodoService {
	private final String serviceName;
	// /** Current added todo id */
	// private static int current = -1;
	/** List of todos */
	private final List<Todo> todos;
	/** Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(MyTodoServiceImpl.class);

	/**
	 *
	 * @param serviceName
	 *            The name of the table will be used as the name of the service
	 */
	public MyTodoServiceImpl(final String serviceName) {
		this.todos = new ArrayList<>();
		// this.todos = this.createInitialModel();
		this.serviceName = serviceName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.todolists.model.ITodoService#getTodo(long)
	 */
	@Override
	public Todo getTodo(final long id) {
		MyTodoServiceImpl.LOGGER.debug("[getTodo()] retrieving the todo {}", id);
		final Todo todo = this.findById(id);
		if (todo != null) {
			MyTodoServiceImpl.LOGGER.debug("[getTodo()] todo {} found", id);
			return todo.copy();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.todolists.model.ITodoService#saveTodo(com.todolists.datamodel.Todo)
	 */
	@Override
	public boolean saveTodo(final Todo newTodo) {
		MyTodoServiceImpl.LOGGER.debug("[saveTodo()] saving the todo [{}]", newTodo.getId());
		final Todo updateTodo = this.findById(newTodo.getId());
		if (updateTodo == null) {
			this.todos.add(newTodo);
			MyTodoServiceImpl.LOGGER.debug("[saveTodo()] the todo [{}] does not exist. Will be added to the list.",
					newTodo.getId());
			return false;
		}
		updateTodo.setState(newTodo.getState());
		updateTodo.setDescription(newTodo.getDescription());
		updateTodo.setAction(newTodo.getAction());
		updateTodo.setResult(newTodo.getResult());
		updateTodo.setEndDate(newTodo.getEndDate());
		MyTodoServiceImpl.LOGGER.debug("[saveTodo()] the todo [{}] saved successfully", newTodo.getId());
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.todolists.model.ITodoService#deleteTodo(long)
	 */
	@Override
	public boolean deleteTodo(final long id) {
		MyTodoServiceImpl.LOGGER.debug("[deleteTodo()] deleting the todo [{}]", id);
		for (final Todo todo : this.todos) {
			if (todo.getId() == id) {
				this.todos.remove(todo);
				MyTodoServiceImpl.LOGGER.debug("[deleteTodo()] the todo [{}] deleted.", id);
				return true;
			}
		}
		MyTodoServiceImpl.LOGGER.debug("[deleteTodo()] the todo [{}] does not exist.", id);
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.todolists.model.ITodoService#getTodos()
	 */
	@Override
	public List<Todo> getTodos() {
		final List<Todo> list = new ArrayList<>();
		for (final Todo todo : this.todos) {
			list.add(todo.copy());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.todolists.model.ITodoService#clearTodos()
	 */
	@Override
	public void clearTodos() {
		final List<Todo> copyTodos = new ArrayList<>();
		/* Copy the items */
		for (final Todo todo : this.todos) {
			copyTodos.add(todo.copy());
		}
		/* Clear all */
		for (final Todo todo : copyTodos) {
			this.deleteTodo(todo.getId());
		}
	}

	/**
	 * Searches ein Todo with the id
	 *
	 * @param id
	 *            Id of the todo
	 * @return Todo-Objekt, if found
	 */
	private Todo findById(final long id) {
		MyTodoServiceImpl.LOGGER.debug("[findById()] looking for the todo [{}]", id);
		for (final Todo todo : this.todos) {
			if (id == todo.getId()) {
				MyTodoServiceImpl.LOGGER.debug("[findById()] the todo [{}] found.", id);
				return todo;
			}
		}
		MyTodoServiceImpl.LOGGER.debug("[findById()] the todo [{}] does not exist.", id);
		return null;
	}

	/**
	 *
	 * @return Name of the service
	 */
	public String getServiceName() {
		return this.serviceName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.todolists.model.ITodoService#createTodo(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Todo createTodo(final long id, final String state, final String description, final String action,
			final String result, final Date endDate) {
		return new Todo(id, state, description, action, result, endDate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.todolists.model.ITodoService#createTodo(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Todo createTodo(final String state, final String description, final String action, final String result,
			final Date endDate) {
		return new Todo(this.todos.size() + 1, state, description, action, result, endDate);
	}

	// private List<Todo> createInitialModel() {
	// final List<Todo> list = new ArrayList<Todo>();
	// list.add(this.createTodo("Application model 1", "Flexible and extensible
	// 1"));
	// list.add(this.createTodo("Application model 2", "Flexible and extensible
	// 2"));
	// list.add(this.createTodo("Application model 3", "Flexible and extensible
	// 3"));
	// list.add(this.createTodo("Application model 4", "Flexible and extensible
	// 4"));
	// return list;
	// }
}