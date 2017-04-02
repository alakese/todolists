package com.todolists.wizards;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * Opens a wizard dialog for table name. There is only one page for now.
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class WizardDialogCreateTable extends Wizard {
	private boolean finish = false;
	private WizardPageCreateTable page;
	private final StringBuilder projectName;
	private final IProject project;

	@Inject
	public WizardDialogCreateTable(final StringBuilder projectName, final IProject project) {
		this.setWindowTitle("New table");
		this.projectName = projectName;
		this.project = project;
	}

	public boolean isFinish() {
		return this.finish;
	}

	public void setFinish(final boolean finish) {
		this.finish = finish;
	}

	@Override
	public void addPages() {
		this.page = new WizardPageCreateTable(this.project);
		this.addPage(this.page);
	}

	@Override
	public boolean performFinish() {
		// File is a sqlite-file ".db"
		this.projectName.append(this.page.getText());
		this.projectName.append(".db");
		return true;
	}

	@Override
	public boolean canFinish() {
		return this.finish;
	}

	@Override
	public IWizardPage getNextPage(final IWizardPage page) {
		// Only one page
		return super.getNextPage(page);
	}
}
