package com.todolists.model.service;

import java.util.ArrayList;
import java.util.List;

import com.todolists.model.ITodoService;
import com.todolists.model.ITodoServiceProvider;

/**
 * Service provider for each tables
 *
 * @author Yasin Alakese
 * @date 22.11.2016
 */
public class MyTodoServiceProviderImpl implements ITodoServiceProvider {
	private final List<ITodoService> services = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 *
	 * @see com.todolists.model.ITodoServiceProvider#getTodoServices()
	 */
	@Override
	public List<ITodoService> getTodoServices() {
		return this.services;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.todolists.model.ITodoServiceProvider#addTodoService(com.todolists.
	 * model.ITodoService)
	 */
	@Override
	public void addTodoService(final ITodoService service) {
		if (!this.services.contains(service)) {
			this.services.add(service);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.todolists.model.ITodoServiceProvider#deleteTodoService(com.todolists.
	 * model.ITodoService)
	 */
	@Override
	public void deleteTodoService(final ITodoService service) {
		for (final ITodoService iTodoService : this.services) {
			if (((MyTodoServiceImpl) iTodoService).getServiceName()
					.equals(((MyTodoServiceImpl) service).getServiceName())) {
				this.services.remove(iTodoService);
				/*
				 * must finish here, cause we are iterating the same list, from
				 * which we deleted an item
				 */
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.todolists.model.ITodoServiceProvider#getTodoService(org.eclipse.core.
	 * resources.IFile)
	 */
	@Override
	public ITodoService getTodoService(final String fileName) {
		ITodoService todoService = null;
		for (final ITodoService iTodoService : this.services) {
			if (((MyTodoServiceImpl) iTodoService).getServiceName().equals(fileName)) {
				todoService = iTodoService;
			}
		}
		return todoService;
	}
}
