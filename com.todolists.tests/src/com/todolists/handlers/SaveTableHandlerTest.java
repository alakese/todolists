package com.todolists.handlers;

import org.easymock.EasyMock;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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

/**
 * Tests CloseAllProjectsHandler
 *
 * @author Yasin Alakese
 * @datum 20.11.2016
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ResourcesPlugin.class })
@PowerMockIgnore({ "org.slf4j.*" })
public class SaveTableHandlerTest {
	/** Test object */
	private transient SaveTableHandler testlet;

	/**
	 * Set up before class
	 */
	@Before
	public void setUp() {
		this.testlet = new SaveTableHandler();
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
	public void testSaveTableHandlerExecute() throws CoreException {
		final EPartService partService = EasyMock.createMock(EPartService.class);
		final MPart activePartMock = EasyMock.createMock(MPart.class);

		EasyMock.expect(partService.getActivePart()).andReturn(activePartMock);
		EasyMock.expect(activePartMock.getLabel()).andReturn("Part");
		EasyMock.expect(partService.savePart(activePartMock, true)).andReturn(true);

		EasyMock.replay(partService, activePartMock);

		this.testlet.execute(partService);
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseProjectHandlerCanExecuteTrue() throws CoreException {
		final EPartService partService = EasyMock.createMock(EPartService.class);
		final MPart activePartMock = EasyMock.createMock(MPart.class);

		EasyMock.expect(partService.getActivePart()).andReturn(activePartMock).times(2);
		EasyMock.expect(activePartMock.getLabel()).andReturn("Part");
		EasyMock.expect(activePartMock.isDirty()).andReturn(true);

		EasyMock.replay(partService, activePartMock);

		Assert.assertTrue(this.testlet.canExecute(partService));
	}

	/**
	 *
	 * @throws CoreException
	 */
	@Test
	public void testCloseProjectHandlerCanExecuteFalse() throws CoreException {
		final EPartService partService = EasyMock.createMock(EPartService.class);
		final MPart activePartMock = EasyMock.createMock(MPart.class);

		EasyMock.expect(partService.getActivePart()).andReturn(activePartMock).times(2);
		EasyMock.expect(activePartMock.getLabel()).andReturn("Part");
		EasyMock.expect(activePartMock.isDirty()).andReturn(false);

		EasyMock.replay(partService, activePartMock);

		Assert.assertFalse(this.testlet.canExecute(partService));
	}
}
