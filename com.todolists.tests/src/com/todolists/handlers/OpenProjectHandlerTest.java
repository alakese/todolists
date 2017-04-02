package com.todolists.handlers;

import org.easymock.EasyMock;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
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
public class OpenProjectHandlerTest {
	/** Test object */
	private transient OpenProjectHandler testlet;

	/**
	 * Set up before class
	 */
	@Before
	public void setUp() {
		this.testlet = new OpenProjectHandler();
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
		final IProject projectMock = EasyMock.createMock(IProject.class);

		EasyMock.expect(projectMock.getName()).andReturn("Testproject").times(2);
		projectMock.open(EasyMock.isA(NullProgressMonitor.class));
		EasyMock.expectLastCall();
		EasyMock.expect(brokerMock.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_OPENED))
				.andReturn(true);

		EasyMock.replay(projectMock, brokerMock);

		this.testlet.execute(projectMock, brokerMock);
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseAllProjectsHandlerCanExecuteTrue() throws CoreException {
		final IProject projectMock = EasyMock.createMock(IProject.class);
		EasyMock.expect(projectMock.exists()).andReturn(true);
		EasyMock.expect(projectMock.isOpen()).andReturn(false);

		EasyMock.replay(projectMock);

		Assert.assertTrue(this.testlet.canExecute(projectMock));
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseAllProjectsHandlerCanExecuteFalse() throws CoreException {
		Assert.assertFalse(this.testlet.canExecute(null));
	}
}
