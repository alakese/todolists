package com.todolists.model;

import java.util.List;

public interface ITodoServiceProvider {
	List<ITodoService> getTodoServices();

	void addTodoService(final ITodoService service);

	void deleteTodoService(final ITodoService service);

	ITodoService getTodoService(final String fileName);
}
