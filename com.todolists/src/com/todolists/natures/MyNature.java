package com.todolists.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * Nature to create an empty project
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class MyNature implements IProjectNature {
	// Hat das eine Wirkung?
	// public static final String NATURE_ID = "com.example.project.MyNature";
	private IProject project;

	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	@Override
	public void setProject(IProject value) {
		this.project = value;
	}

}
