
package com.todolists.handlers;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.events.ProjectEventConstants;

/**
 * Deleted the new project
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class DeleteProjectHandler {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(NewProjectHandler.class);

	/**
	 * Executes the delete handler.
	 *
	 * @param project
	 *            Selected project. Must be registered in TodoTreePart. See
	 *            ESelectionService service in TodoTreePart.
	 * @param broker
	 *            Used to fire an event to notify the treeviewer that a project
	 *            has been closed
	 * @throws CoreException
	 *             if project can not be deleted
	 */
	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IProject project,
			final IEventBroker broker) throws CoreException {
		DeleteProjectHandler.LOGGER.debug("Deleting project {}.", project.getName());
		if (!project.exists()) {
			DeleteProjectHandler.LOGGER.debug("Can not delete the project, since it does not exist!");
		}

		/* delete now */
		project.delete(true, new NullProgressMonitor());

		/*
		 * Refresh the toolbar after deleting a project. Otherwise the
		 * toolbar-item "delete" stays active
		 */
		broker.send(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);

		/* Send an event to treeviewer to update the list */
		broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_DELETED);

		DeleteProjectHandler.LOGGER.debug("Project deleted successfully.");
	}

	/**
	 * Menuitem "Delete project" may only be activated, if a project is selected
	 *
	 * @param project
	 *            A selected project or null, if no project selected
	 * @return true, if can execute
	 */
	@CanExecute
	public final boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IProject project) {
		/*
		 * checking project.exists is necessary, because after you delete the
		 * project, it stays as selected project in system. So, the menuitems
		 * are still active, and gives as error, if you click on them.
		 */
		return (project != null) && project.exists();
	}
}