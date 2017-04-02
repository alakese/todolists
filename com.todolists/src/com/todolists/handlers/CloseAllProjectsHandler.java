
package com.todolists.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.events.ProjectEventConstants;

/**
 * Closes all the projects
 *
 * @author Yasin Alakese
 * @datum 20.11.2016
 */
public class CloseAllProjectsHandler {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(CloseProjectHandler.class);

	/**
	 * Closes all the projects
	 *
	 * @param broker
	 *            Used to fire an event to notify the treeviewer that a project
	 *            has been closed
	 * @throws CoreException
	 *             if the projects can not be closed
	 */
	@Execute
	public void execute(final IEventBroker broker) throws CoreException {
		CloseAllProjectsHandler.LOGGER.debug("Closing all projects.");
		/* Get all projecst */
		final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (final IProject project : projects) {
			if (project.isOpen()) {
				project.close(null);
			}
		}
		/* Send an event to treeviewer to update the list */
		// final Map<String, String> map = new HashMap<String, String>();
		// map.put(ProjectEventConstants.TOPIC_PROJECT_ALLTOPICS,
		// ProjectEventConstants.TOPIC_PROJECT_CLOSED);
		// broker.post(ProjectEventConstants.TOPIC_PROJECT_CLOSED, map);
		broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_CLOSED);

		CloseAllProjectsHandler.LOGGER.debug("All projects closed successfully.");
	}

	/**
	 * Checks, if there is at least one project open.
	 *
	 * @return true, if there are projectes to close
	 */
	@CanExecute
	public final boolean canExecute() {
		/* Get all projects and check status */
		final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (final IProject project : projects) {
			/* If there is a opened project, then close all may be activated */
			if (project.isOpen()) {
				return true;
			}
		}

		return false;
	}
}