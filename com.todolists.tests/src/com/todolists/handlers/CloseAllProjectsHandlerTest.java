package com.todolists.handlers;

import org.easymock.EasyMock;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.todolists.events.ProjectEventConstants;

/**
 * Tests CloseAllProjectsHandler
 *
 * @author Yasin Alakese
 * @datum 20.11.2016
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ResourcesPlugin.class })
@PowerMockIgnore({ "org.slf4j.*" })
public class CloseAllProjectsHandlerTest {
	/** Test object */
	private transient CloseAllProjectsHandler testlet;

	/**
	 * Set up before class
	 */
	@Before
	public void setUp() {
		this.testlet = new CloseAllProjectsHandler();
	}

	/**
	 * Clean up
	 */
	@After
	public void tearDown() {
		this.testlet = null;
	}

	/**
	 * @throws CoreException
	 */
	@Test
	public void testCloseAllProjectsHandlerExecute() throws CoreException {
		final IEventBroker brokerMock = EasyMock.createMock(IEventBroker.class);

		this.mockMethods(true);

		// final Map<String, String> map = new HashMap<String, String>();
		// map.put(ProjectEventConstants.TOPIC_PROJECT_ALLTOPICS,
		// ProjectEventConstants.TOPIC_PROJECT_CLOSED);
		// EasyMock.expect(brokerMock.post(ProjectEventConstants.TOPIC_PROJECT_CLOSED,
		// map)).andReturn(true);
		EasyMock.expect(brokerMock.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_CLOSED))
				.andReturn(true);

		EasyMock.replay(brokerMock);

		this.testlet.execute(brokerMock);
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseAllProjectsHandlerCanExecuteTrue() throws CoreException {
		this.mockMethods(true);
		Assert.assertTrue(this.testlet.canExecute());
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseAllProjectsHandlerCanExecuteFalse() throws CoreException {
		this.mockMethods(false);
		Assert.assertFalse(this.testlet.canExecute());
	}

	/**
	 * Mocks the Methoded
	 *
	 * @param isOpen
	 *            is project open?
	 * @throws CoreException
	 *             - if can not close the project
	 */
	private void mockMethods(final boolean isOpen) throws CoreException {
		PowerMock.mockStatic(ResourcesPlugin.class);

		final IWorkspace workspaceMock = EasyMock.createMock(IWorkspace.class);
		final IWorkspaceRoot workspaceRootMock = EasyMock.createMock(IWorkspaceRoot.class);
		final IProject projectMock = EasyMock.createMock(IProject.class);

		EasyMock.expect(ResourcesPlugin.getWorkspace()).andReturn(workspaceMock);
		EasyMock.expect(workspaceMock.getRoot()).andReturn(workspaceRootMock);

		final IProject[] projects = new IProject[1];
		projects[0] = projectMock;
		EasyMock.expect(workspaceRootMock.getProjects()).andReturn(projects);
		EasyMock.expect(projectMock.isOpen()).andReturn(isOpen);
		projectMock.close(null);
		EasyMock.expectLastCall();

		PowerMock.replay(ResourcesPlugin.class);
		EasyMock.replay(workspaceMock, workspaceRootMock, projectMock);
	}
}
