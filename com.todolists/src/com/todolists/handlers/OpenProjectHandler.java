
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.events.ProjectEventConstants;

/**
 * Opens the project
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class OpenProjectHandler {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(CloseProjectHandler.class);

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
	 *             if project can not be opened
	 */
	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IProject project,
			final IEventBroker broker) throws CoreException {
		OpenProjectHandler.LOGGER.debug("Opening the project {}.", project.getName());

		project.open(new NullProgressMonitor());

		/* Send an event to treeviewer to update the list */
		// final Map<String, String> map = new HashMap<String, String>();
		// map.put(ProjectEventConstants.TOPIC_PROJECT_ALLTOPICS,
		// ProjectEventConstants.TOPIC_PROJECT_OPENED);
		// broker.post(ProjectEventConstants.TOPIC_PROJECT_OPENED, map);
		broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_OPENED);

		OpenProjectHandler.LOGGER.debug("Project {} is opened.", project.getName());
	}

	/**
	 * Menuitem "Open project" may only be activated, if a project is selected
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
		return (project != null) && project.exists() && !project.isOpen();
	}
}