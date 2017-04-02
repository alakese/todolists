
package com.todolists.parts;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.events.ProjectEventConstants;

public class ProjectsPart {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(ProjectsPart.class);
	/** */
	private TreeViewer viewer;
	/**
	 * To register the selection, so that the parts/handlers which needs the
	 * active selected project can call it
	 */
	@Inject
	ESelectionService service;

	@Inject
	private ECommandService commandService;

	@Inject
	private EHandlerService handlerService;

	/**
	 *
	 * @param parent
	 *            For layout
	 * @param menuService
	 *            Contextmenus can be registered through menuService
	 *
	 * @author Yasin Alakese
	 * @date 02.10.2016
	 */
	@PostConstruct
	public void createControls(final Composite parent, final EMenuService menuService, final EPartService partService,
			final IEventBroker broker) {
		ProjectsPart.LOGGER.debug("Creating controls in ProjectsPart {}",
				Platform.getConfigurationLocation().getURL().getPath());

		// create the icons which will be used in treeviewer
		final List<ImageDescriptor> images = new ArrayList<>();
		images.add(this.createImage("icons/openedprj.gif"));
		images.add(this.createImage("icons/closedprj.gif"));
		images.add(this.createImage("icons/table.gif"));

		parent.setLayout(new GridLayout(1, false));

		this.viewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		final Tree tree = this.viewer.getTree();
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				/* At double-click, open the project, if closed */
				final IStructuredSelection selection = (IStructuredSelection) ProjectsPart.this.viewer.getSelection();
				final Object firstElement = selection.getFirstElement();
				/*
				 * If the user double-clicks on an empty area, then do nothing
				 */
				if (null == firstElement) {
					return;
				}
				if (firstElement instanceof IProject) {
					final IProject project = (IProject) firstElement;
					/* if the selected project is closed, then open it */
					if (!project.isOpen()) {
						try {
							project.open(null);
							ProjectsPart.this.viewer.refresh();
						} catch (final CoreException e1) {
							ProjectsPart.LOGGER.debug(e1.getMessage(), e);
							return;
						}
					}
				} else if (firstElement instanceof IFile) {
					/* Double-click on a file -> open the table */
					final IFile file = (IFile) firstElement;
					final String tableName = file.getName();
					ProjectsPart.LOGGER.debug("Opening the table [{}]", tableName);
					final Command cmd = ProjectsPart.this.commandService.getCommand("com.todolists.command.opentable");
					final ParameterizedCommand pCmd = new ParameterizedCommand(cmd, null);
					if (ProjectsPart.this.handlerService.canExecute(pCmd)) {
						ProjectsPart.this.handlerService.executeHandler(pCmd);
					}
				}
			}
		});
		/* If the mouse will be clicked, then open a popup menu */
		tree.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				/*
				 * Decide, which contextmenu will be shown e.g. if Project is
				 * clicked, then show for example "Close project" else "Open
				 * project"
				 */
				final Point point = new Point(event.x, event.y);
				final TreeItem item = tree.getItem(point);
				if (item != null) {
					final Object data = item.getData();
					if (data instanceof IProject) {
						if (((IProject) data).isOpen()) {
							ProjectsPart.LOGGER.debug("Popup : close project");
							menuService.registerContextMenu(ProjectsPart.this.viewer.getControl(),
									"com.todolists.popupmenu.projectpopup.closeproject");
						} else {
							ProjectsPart.LOGGER.debug("Popup : open project");
							menuService.registerContextMenu(ProjectsPart.this.viewer.getControl(),
									"com.todolists.popupmenu.projectpopup.openproject");
						}
						/*
						 * Register the selection, so that the part which needs
						 * this, can call it via
						 *
						 * @Optional @Named(IServiceConstants. ACTIVE_SELECTION)
						 * IProject project. See handlers.
						 */
						final IStructuredSelection selection = (IStructuredSelection) ProjectsPart.this.viewer
								.getSelection();
						ProjectsPart.this.service.setSelection(selection.getFirstElement());
					}
					if (data instanceof IFile) {
						ProjectsPart.LOGGER.debug("Popup : file project");
						menuService.registerContextMenu(ProjectsPart.this.viewer.getControl(),
								"com.todolists.popupmenu.filecontextmenu");
						/* Register the selection */
						final IStructuredSelection selection = (IStructuredSelection) ProjectsPart.this.viewer
								.getSelection();
						final IFile firstElement = (IFile) selection.getFirstElement();
						ProjectsPart.this.service.setSelection(firstElement);
						ProjectsPart.LOGGER.debug("Set selection file [{}]", firstElement);
					}
				} else {
					// If the free area has been selected
					ProjectsPart.LOGGER.debug("Popup : new project");
					menuService.registerContextMenu(ProjectsPart.this.viewer.getControl(),
							"com.todolists.popupmenu.projectpopup.newproject");
				}
			}
		});
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Add treeviewer stuff
		this.viewer.setContentProvider(new ProjectsPartViewContentProvider());
		this.viewer.setLabelProvider(new ProjectsPartViewLabelProvider(images));
		// Set the projects from the workspace
		this.viewer.setInput(ResourcesPlugin.getWorkspace());

		ProjectsPart.LOGGER.debug("Controls created successfully");
	}

	/**
	 * Receive the event : events will be sent from "New project",
	 * "Delete project", "Close project" and "Open project" This is the auto
	 * communication between the treeviewer and the handlers
	 */
	@Inject
	@Optional
	private void subscribeTopicTodoAllTopics(@UIEventTopic(ProjectEventConstants.TOPIC_TODO) final String event) {
		if (this.viewer != null) {
			// TODO setInput necessary?
			// this.viewer.setInput(ResourcesPlugin.getWorkspace());
			this.viewer.refresh();
		}
	}
	// @Inject
	// @Optional
	// private void subscribeTopicTodoAllTopics(
	// @UIEventTopic(ProjectEventConstants.TOPIC_PROJECT_ALLTOPICS) final
	// Map<String, String> event) {
	// if (this.viewer != null) {
	// // TODO setInput necessary?
	// // viewer.setInput(ResourcesPlugin.getWorkspace());
	// this.viewer.refresh();
	// }
	// }

	/**
	 * Create the images for icons here
	 *
	 * @param path
	 *            Pfad zu den Icons. Default it is under /icons
	 * @return An ImageDescriptor
	 */
	private ImageDescriptor createImage(final String path) {
		final Bundle bundle = FrameworkUtil.getBundle(ProjectsPartViewLabelProvider.class);
		final URL url = FileLocator.find(bundle, new Path(path), null);
		final ImageDescriptor folderImage = ImageDescriptor.createFromURL(url);
		return folderImage;
	}

	/*
	 * This is necessary
	 */
	@Focus
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}
}