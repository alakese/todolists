package com.todolists.handlers;

import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFileState;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
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

		final IFile file = new IFile() {

			@Override
			public boolean isConflicting(final ISchedulingRule rule) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean contains(final ISchedulingRule rule) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public <T> T getAdapter(final Class<T> adapter) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void touch(final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setTeamPrivateMember(final boolean isTeamPrivate) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setSessionProperty(final QualifiedName key, final Object value) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setResourceAttributes(final ResourceAttributes attributes) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setReadOnly(final boolean readOnly) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setPersistentProperty(final QualifiedName key, final String value) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public long setLocalTimeStamp(final long value) throws CoreException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setLocal(final boolean flag, final int depth, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setHidden(final boolean isHidden) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setDerived(final boolean isDerived, final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setDerived(final boolean isDerived) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void revertModificationStamp(final long value) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void refreshLocal(final int depth, final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void move(final IProjectDescription description, final boolean force, final boolean keepHistory,
					final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void move(final IProjectDescription description, final int updateFlags,
					final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void move(final IPath destination, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void move(final IPath destination, final boolean force, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isVirtual() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isTeamPrivateMember(final int options) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isTeamPrivateMember() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isSynchronized(final int depth) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isPhantom() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isLocal(final int depth) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isLinked(final int options) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isLinked() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isHidden(final int options) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isHidden() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isDerived(final int options) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isDerived() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isAccessible() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public IWorkspace getWorkspace() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getType() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getSessionProperty(final QualifiedName key) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<QualifiedName, Object> getSessionProperties() throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ResourceAttributes getResourceAttributes() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getRawLocationURI() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IPath getRawLocation() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IPath getProjectRelativePath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IProject getProject() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPersistentProperty(final QualifiedName key) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<QualifiedName, String> getPersistentProperties() throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IPathVariableManager getPathVariableManager() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IContainer getParent() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getModificationStamp() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public IMarker getMarker(final long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URI getLocationURI() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IPath getLocation() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getLocalTimeStamp() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String getFileExtension() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int findMaxProblemSeverity(final String type, final boolean includeSubtypes, final int depth)
					throws CoreException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public IMarker[] findMarkers(final String type, final boolean includeSubtypes, final int depth)
					throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IMarker findMarker(final long id) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean exists() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void deleteMarkers(final String type, final boolean includeSubtypes, final int depth)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void delete(final int updateFlags, final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void delete(final boolean force, final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public IResourceProxy createProxy() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IMarker createMarker(final String type) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void copy(final IProjectDescription description, final int updateFlags,
					final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void copy(final IProjectDescription description, final boolean force, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void copy(final IPath destination, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void copy(final IPath destination, final boolean force, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void clearHistory(final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void accept(final IResourceVisitor visitor, final int depth, final int memberFlags)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void accept(final IResourceVisitor visitor, final int depth, final boolean includePhantoms)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void accept(final IResourceProxyVisitor visitor, final int depth, final int memberFlags)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void accept(final IResourceProxyVisitor visitor, final int memberFlags) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void accept(final IResourceVisitor visitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContents(final IFileState source, final boolean force, final boolean keepHistory,
					final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContents(final InputStream source, final boolean force, final boolean keepHistory,
					final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContents(final IFileState source, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setContents(final InputStream source, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCharset(final String newCharset, final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCharset(final String newCharset) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void move(final IPath destination, final boolean force, final boolean keepHistory,
					final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isReadOnly() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String getName() {
				return "dummy.db";
			}

			@Override
			public IFileState[] getHistory(final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IPath getFullPath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getEncoding() throws CoreException {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public InputStream getContents(final boolean force) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getContents() throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IContentDescription getContentDescription() throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getCharsetFor(final Reader reader) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getCharset(final boolean checkImplicit) throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getCharset() throws CoreException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void delete(final boolean force, final boolean keepHistory, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void createLink(final URI location, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void createLink(final IPath localLocation, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void create(final InputStream source, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void create(final InputStream source, final boolean force, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void appendContents(final InputStream source, final boolean force, final boolean keepHistory,
					final IProgressMonitor monitor) throws CoreException {
				// TODO Auto-generated method stub

			}

			@Override
			public void appendContents(final InputStream source, final int updateFlags, final IProgressMonitor monitor)
					throws CoreException {
				// TODO Auto-generated method stub

			}
		};

		final IResource[] resources = new IResource[1];
		resources[0] = file;
		EasyMock.expect(projectMock.members()).andReturn(resources);

		final List<MPart> parts = new ArrayList<>();
		final MPart mPartMock = EasyMock.createMock(MPart.class);
		/* Lets make both names equal */
		EasyMock.expect(mPartMock.getLabel()).andReturn("dummy.db");
		final Map<String, String> mapMock = EasyMock.createMock(Map.class);
		EasyMock.expect(mPartMock.getPersistedState()).andReturn(mapMock);
		EasyMock.expect(mapMock.get("partstate")).andReturn("opened");
		parts.add(mPartMock);
		EasyMock.expect(ePartServiceMock.getParts()).andReturn(parts);

		EasyMock.expect(brokerMock.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_PROJECT_CLOSED))
				.andReturn(true);
		ePartServiceMock.hidePart(EasyMock.isA(MPart.class));
		EasyMock.expectLastCall().atLeastOnce();

		// TODO mock all
		EasyMock.replay(projectMock, brokerMock, ePartServiceMock, mPartMock, mapMock);

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
