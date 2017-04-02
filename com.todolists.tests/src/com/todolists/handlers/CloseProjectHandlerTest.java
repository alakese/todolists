package com.todolists.handlers;

import org.easymock.EasyMock;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class CloseProjectHandlerTest {
	/** Test object */
	private transient CloseProjectHandler testlet;

	/**
	 * Set up before class
	 */
	@Before
	public void setUp() {
		this.testlet = new CloseProjectHandler();
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
	public void testCloseProjectHandlerExecute() throws CoreException {
		final IProject projectMock = EasyMock.createMock(IProject.class);
		final IEventBroker brokerMock = EasyMock.createMock(IEventBroker.class);
		final EPartService ePartServiceMock = EasyMock.createMock(EPartService.class);

		EasyMock.expect(projectMock.getName()).andReturn("Testproject").times(2);
		projectMock.close(EasyMock.isA(NullProgressMonitor.class));
		EasyMock.expectLastCall();

		// final Map<String, String> map = new HashMap<String, String>();
		// map.put(ProjectEventConstants.TOPIC_PROJECT_ALLTOPICS,
		// ProjectEventConstants.TOPIC_PROJECT_CLOSED);
		// EasyMock.expect(brokerMock.post(ProjectEventConstants.TOPIC_PROJECT_CLOSED,
		// map)).andReturn(true);
		EasyMock.expect(brokerMock.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_CLOSED))
				.andReturn(true);
		ePartServiceMock.hidePart(EasyMock.isA(MPart.class));
		EasyMock.expectLastCall();

		// TODO mock all
		EasyMock.replay(projectMock, brokerMock, ePartServiceMock);

		this.testlet.execute(projectMock, ePartServiceMock, brokerMock);
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseProjectHandlerCanExecuteTrue() throws CoreException {
		final IProject projectMock = EasyMock.createMock(IProject.class);
		EasyMock.expect(projectMock.exists()).andReturn(true);
		EasyMock.expect(projectMock.isOpen()).andReturn(true);

		EasyMock.replay(projectMock);

		Assert.assertTrue(this.testlet.canExecute(projectMock));
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseProjectHandlerCanExecuteFalse() throws CoreException {
		Assert.assertFalse(this.testlet.canExecute(null));
	}
}
