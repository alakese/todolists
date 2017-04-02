package com.todolists.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This is the class for treeviewer to show the IProject-stuff
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class ProjectsPartViewContentProvider implements ITreeContentProvider {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getElements(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		// return (File[]) inputElement;
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IProject) {
			IProject projects = (IProject) parentElement;
			try {
				// Alle Dateien inklusiv .project
				// return projects.members();
				// Die .project-Dateien rausfiltern
				IResource[] members = projects.members();
				List<IResource> filteredeMembers = new ArrayList<>();
				for (IResource iResource : members) {
					if (!iResource.getFileExtension().contains("project")) {
						filteredeMembers.add(iResource);
					}
				}
				return filteredeMembers.toArray();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// TODO : Folders
		// if (parentElement instanceof IFolder) {
		// IFolder ifolder = (IFolder) parentElement;
		// try {
		// return ifolder.members();
		// } catch (CoreException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.
	 * Object)
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof IProject) {
			IProject projects = (IProject) element;
			return projects.getParent();
		}
		// if (element instanceof IFolder) {
		// IFolder folder = (IFolder) element;
		// return folder.getParent();
		// }
		if (element instanceof IFile) {
			IFile file = (IFile) element;
			return file.getParent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IProject) {
			IProject projects = (IProject) element;
			try {
				return projects.members().length > 0;
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// if (element instanceof IFolder) {
		// IFolder folder = (IFolder) element;
		// try {
		// return folder.members().length > 0;
		// } catch (CoreException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return false;
	}
}
