package com.todolists.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * Opens a wizard dialog for projectname. There is only one page for now.
 *
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class WizardDialogCreateProject extends Wizard {
	private boolean finish = false;
	private WizardPageCreateProject page;
	private final StringBuilder projectName;

	@Inject
	public WizardDialogCreateProject(final StringBuilder projectName) {
		this.setWindowTitle("New project");
		this.projectName = projectName;
	}

	public boolean isFinish() {
		return this.finish;
	}

	public void setFinish(final boolean finish) {
		this.finish = finish;
	}

	@Override
	public void addPages() {
		this.page = new WizardPageCreateProject();
		this.addPage(this.page);
	}

	@Override
	public boolean performFinish() {
		this.projectName.append(this.page.getText());
		return true;
	}

	@Override
	public boolean canFinish() {
		return this.finish;
	}

	@Override
	public IWizardPage getNextPage(final IWizardPage page) {
		return super.getNextPage(page);
	}
}
