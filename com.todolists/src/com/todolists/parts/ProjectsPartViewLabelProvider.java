package com.todolists.parts;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * Standard class for treeviewer to show IProject
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class ProjectsPartViewLabelProvider implements ILabelProvider {
	private static final int IMG_PROJECT_OPENED = 0;
	private static final int IMG_PROJECT_CLOSED = 1;
	private static final int IMG_FILE = 2;
	private final ImageDescriptor projectOpenImage;
	private final ImageDescriptor projectCloseImage;
	private final ImageDescriptor fileImage;
	private ResourceManager resourceManager;

	/**
	 *
	 * @param imageDescriptors
	 *            Images for icons
	 */
	public ProjectsPartViewLabelProvider(final List<ImageDescriptor> imageDescriptors) {
		this.projectOpenImage = imageDescriptors.get(ProjectsPartViewLabelProvider.IMG_PROJECT_OPENED);
		this.projectCloseImage = imageDescriptors.get(ProjectsPartViewLabelProvider.IMG_PROJECT_CLOSED);
		this.fileImage = imageDescriptors.get(ProjectsPartViewLabelProvider.IMG_FILE);
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof IProject) {
			final String text = ((IProject) element).getName();
			return text;
		}
		// if (element instanceof IFolder) {
		// String text = ((IFolder) element).getName();
		// return text;
		// }
		if (element instanceof IFile) {
			final String text = ((IFile) element).getName();
			return text;
		}
		return null;
	}

	@Override
	public Image getImage(final Object element) {
		if (element instanceof IProject) {
			final boolean isPrjOpen = ((IProject) element).isOpen();
			if (isPrjOpen) {
				return this.getResourceManager().createImage(this.projectOpenImage);
			} else {
				return this.getResourceManager().createImage(this.projectCloseImage);
			}
		}
		// if (element instanceof IFolder) {
		// return getResourceManager().createImage(folderImage);
		// }
		return this.getResourceManager().createImage(this.fileImage);
	}

	@Override
	public void dispose() {
		// garbage collect system resources
		if (this.resourceManager != null) {
			this.resourceManager.dispose();
			this.resourceManager = null;
		}
	}

	protected ResourceManager getResourceManager() {
		if (this.resourceManager == null) {
			this.resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return this.resourceManager;
	}

	@Override
	public void addListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
