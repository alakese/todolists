
package com.todolists.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
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

/**
 * Closes a project
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class CloseProjectHandler {
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
	 *             if project can not be closed
	 */
	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IProject project,
			final EPartService partService, final IEventBroker broker) throws CoreException {
		CloseProjectHandler.LOGGER.debug("Closing the project {}.", project.getName());

		/* Close this projects tables */
		for (final IResource iResource : project.members()) {
			if (iResource instanceof IFile) {
				final String tableName = ((IFile) iResource).getName();
				/* check, if the editor is already open? */
				final List<MPart> parts = (List<MPart>) partService.getParts();
				for (final MPart mPart : parts) {
					final String foundLabel = mPart.getLabel();
					if ((foundLabel != null) && foundLabel.equals(tableName)) {
						if ("opened".equals(mPart.getPersistedState().get("partstate"))) {
							/* yeah, it is opened */
							CloseProjectHandler.LOGGER.debug("Closing the table...");
							partService.hidePart(mPart);
						}
					}
				}
			}
		}

		// TODO progress monitor?
		project.close(new NullProgressMonitor());

		/* Send an event to treeviewer to update the list */
		// final Map<String, String> map = new HashMap<String, String>();
		// map.put(ProjectEventConstants.TOPIC_PROJECT_ALLTOPICS,
		// ProjectEventConstants.TOPIC_PROJECT_CLOSED);
		// broker.post(ProjectEventConstants.TOPIC_PROJECT_CLOSED, map);
		broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_CLOSED);

		CloseProjectHandler.LOGGER.debug("Project {} is closed.", project.getName());
	}

	/**
	 * Menuitem "Close project" may only be activated, if a project is selected
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
		 *
		 * TODO : Maybe try to remove the last selection?
		 */
		return (project != null) && project.exists() && project.isOpen();
	}
}