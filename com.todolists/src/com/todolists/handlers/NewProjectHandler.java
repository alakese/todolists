
package com.todolists.handlers;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.events.ProjectEventConstants;
import com.todolists.wizards.WizardDialogCreateProject;

/**
 * Creates a new project
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class NewProjectHandler {
	/*
	 * INFO 1 : target und import package müssen für logging angepasst werden.
	 * Siehe http://www.vogella.com/tutorials/EclipseLogging/article.html#slf4j-
	 * logback-from-orbit
	 *
	 * INFO 2 : damit slf4j über product klappt, soll in feature project slf4j
	 * und alle drei logbacks hinzugefügt werdene
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(NewProjectHandler.class);

	/**
	 * Executes the new project handler.
	 *
	 * @param shell
	 *            Necessary for layout
	 * @param broker
	 *            Used to fire an event to notify the treeviewer that a project
	 *            has been created
	 * @throws CoreException
	 *             if project can not be created
	 */
	@Execute
	public void execute(final Shell shell, final IEventBroker broker) throws CoreException {
		NewProjectHandler.LOGGER.debug("Opening wizard for a new project");

		// Get the projectname from page as reference, since only one string
		final StringBuilder sb = new StringBuilder(512);
		final WizardDialog wizardDialog = new WizardDialog(shell, new WizardDialogCreateProject(sb));

		if (wizardDialog.open() == Window.OK) {
			final String projectName = sb.toString();
			NewProjectHandler.LOGGER.debug("Creating the project {}", projectName);
			NewProjectHandler.createEmptyProject(projectName);

			// Send an event to treeviewer to update the project list : with map
			// something wrong
			// final Map<String, String> map = new HashMap<String, String>();
			// map.put(ProjectEventConstants.TOPIC_PROJECT_ALLTOPICS,
			// ProjectEventConstants.TOPIC_PROJECT_NEW);
			// broker.post(ProjectEventConstants.TOPIC_PROJECT_NEW, map);
			broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_NEW);

			NewProjectHandler.LOGGER.debug("Project {} created successfully", projectName);
		}
	}

	/**
	 * Creates an empty prject. For empty project there is a nature defined in
	 * extension points*
	 *
	 * @param projectName
	 *            Name of the project
	 * @return Created project
	 * @throws CoreException
	 *             if project can not be created
	 */
	private static IProject createEmptyProject(final String projectName) throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = root.getProject(projectName);
		if (!project.exists()) {
			project.create(null);
		} else {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		}

		if (!project.isOpen()) {
			project.open(null);
		}

		// set the my nature
		final IProjectDescription description = project.getDescription();
		final String[] natures = description.getNatureIds();
		final String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		// not the complete id of nature in manifes.mf > extension points
		newNatures[natures.length] = "com.todolists.MyNature";
		description.setNatureIds(newNatures);
		project.setDescription(description, null);

		return project;
	}
}