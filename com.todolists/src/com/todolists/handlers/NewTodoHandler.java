
package com.todolists.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.events.ProjectEventConstants;
import com.todolists.model.ITodoServiceProvider;

public class NewTodoHandler {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(NewTodoHandler.class);
	/** */
	@Inject
	ITodoServiceProvider todoServiceProvider;

	@Execute
	public void execute(final EPartService partService, final IEventBroker broker) {
		final String partName = partService.getActivePart().getLabel();
		NewTodoHandler.LOGGER.debug("Active part is [{}]", partName);
		// // Get the service provider for this table
		// final ITodoService todoService =
		// this.todoServiceProvider.getTodoService(partName);
		// final Todo todo = todoService.createTodo("New", "<empty>", "<empty>",
		// "<empty>", new Date());
		// todoService.saveTodo(todo);

		broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_TODO_NEW);
	}
}