
package com.todolists.handlers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.events.ProjectEventConstants;
import com.todolists.model.ITodoService;
import com.todolists.model.ITodoServiceProvider;

public class DeleteTableHandler {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(DeleteTableHandler.class);
	/** */
	@Inject
	ITodoServiceProvider todoServiceProvider;

	/**
	 *
	 * @param partService
	 *            Partservice to edit the part
	 * @param file
	 *            The selected file / table
	 * @throws CoreException
	 *             - if the file can not be deleted
	 */
	@Execute
	public void execute(final EPartService partService, final IEventBroker broker,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IFile file) throws CoreException {
		final String tableName = file.getName();
		DeleteTableHandler.LOGGER.debug("Deleting the table [{}].", tableName);

		// check, if the editor is opened. If so close it.
		final List<MPart> parts = (List<MPart>) partService.getParts();
		for (final MPart mPart : parts) {
			final String foundLabel = mPart.getLabel();
			if ((foundLabel != null) && foundLabel.equals(tableName)) {
				DeleteTableHandler.LOGGER.debug("Editor is opened. Closing...");
				partService.hidePart(mPart);
			}
		}

		// delete the table
		DeleteTableHandler.LOGGER.debug("Deleting the service");
		final ITodoService todoService = this.todoServiceProvider.getTodoService(tableName);
		this.todoServiceProvider.deleteTodoService(todoService);

		// delete the file
		if (!file.exists()) {
			DeleteTableHandler.LOGGER.error("File does not exist. Something went wrong!");
			return;
		}

		DeleteTableHandler.LOGGER.debug("Deleting the DB [{}]", file.getName());
		file.delete(true, null);

		/* Send an event */
		DeleteTableHandler.LOGGER.debug("Sending deleted-event [{}]", file.getName());
		broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_TABLE_DELETED);

		DeleteTableHandler.LOGGER.debug("The table deleted successfully.", tableName);
	}

	/**
	 * Menuitem "Delete table" may only be activated, if a table is selected
	 *
	 * @param project
	 *            A selected project or null, if no project selected
	 * @return true, if can execute
	 */
	@CanExecute
	public final boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IFile file) {
		return (file != null) && file.isAccessible();
	}
}