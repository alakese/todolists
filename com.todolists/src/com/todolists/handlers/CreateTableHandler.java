
package com.todolists.handlers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.db.DBActions;
import com.todolists.db.DBActionsException;
import com.todolists.events.ProjectEventConstants;
import com.todolists.factory.TodoServiceFactory;
import com.todolists.model.ITodoService;
import com.todolists.model.ITodoServiceProvider;
import com.todolists.wizards.WizardDialogCreateTable;

/**
 * Creates a table
 *
 *
 * @author Yasin Alakese
 * @date 15.11.2016
 */
public class CreateTableHandler {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(CreateTableHandler.class);

	/** */
	@Inject
	ITodoServiceProvider todoServiceProvider;

	/**
	 *
	 * @param project
	 *            Project, which will create the table
	 * @param broker
	 *            Sending an event to treeviewer
	 * @param parent
	 *            For WizardDialog
	 * @throws CoreException
	 *             if the file cannot be created or edited
	 * @throws IOException
	 *             if the inputstream cannot be closed
	 * @throws DBActionsException
	 *             if the database query fails
	 *
	 * @author Yasin Alakese
	 * @date 02.10.2016
	 */
	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IProject project,
			final IEventBroker broker, final Composite parent) throws CoreException, IOException, DBActionsException {
		CreateTableHandler.LOGGER.debug("Creating a table");

		// Get the projectname from page as reference, since only one string
		final StringBuilder sb = new StringBuilder(512);
		final WizardDialog wizardDialog = new WizardDialog(parent.getShell(), new WizardDialogCreateTable(sb, project));

		if (wizardDialog.open() == Window.OK) {
			final String tableName = sb.toString();
			// if (null == CreateTableHandler.createAFile(project.getName(),
			// tableName)) {
			// throw new IOException("Can not create file " + sb.toString());
			// }
			CreateTableHandler.createADatabase(project, tableName);
			CreateTableHandler.LOGGER.debug("Table created with the name [{}]", tableName);
			/*
			 * use refreshLocal when you do file I/O using your own natives, in
			 * order to see the file in tree
			 */
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			// Save the project for this file in a map and write the projectname
			// into the table

			// Create the service provider for this table
			final ITodoService todoService = TodoServiceFactory.getInstance(tableName);
			this.todoServiceProvider.addTodoService(todoService);

			// Send an event to treeviewer to update the list
			// final Map<String, String> map = new HashMap<String, String>();
			// map.put(ProjectEventConstants.TOPIC_TODO,
			// ProjectEventConstants.TOPIC_TABLE_NEW);
			// broker.post(ProjectEventConstants.TOPIC_TABLE_NEW, map);
			broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_TABLE_NEW);
		}
	}

	/**
	 *
	 * @param project
	 * @param fileName
	 * @return
	 * @throws DBActionsException
	 */
	private static void createADatabase(final IProject project, final String fileName) throws DBActionsException {
		final String absPath = project.getLocation().toString();
		DBActions.createDatabase(absPath, fileName);

	}

	/**
	 * Creates a new file, or edits if it exiss
	 *
	 * @param projectName
	 *            project name
	 * @param fileName
	 *            file name
	 * @return File
	 * @throws CoreException
	 *             if the file cannot be created or edited
	 * @throws IOException
	 *             if the inputstream cannot be closed
	 *
	 * @author Yasin Alakese
	 * @date 02.10.2016
	 */
	public static IFile createAFile(final String projectName, final String fileName) throws CoreException, IOException {
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getFile(fileName);
		// Create an empty file
		final byte[] bytes = new byte[0];
		try (final InputStream inputSource = new ByteArrayInputStream(bytes)) {
			if (!file.exists()) {
				file.create(inputSource, IResource.NONE, null);
			}
		}

		return file;
	}

	/**
	 * Menuitem "Create table" may only be activated, if a project is selected
	 *
	 * @param project
	 *            A selected project or null, if no project selected
	 * @return true, if can execute
	 */
	@CanExecute
	public final boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IProject project) {
		return project != null;
	}
}