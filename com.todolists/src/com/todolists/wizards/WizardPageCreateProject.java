package com.todolists.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wizard page which takes a projectname. If it exists, it shows an error text
 * on the page.
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class WizardPageCreateProject extends WizardPage {
	/** */
	private Text projectName;
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(WizardPageCreateProject.class);

	/** */
	public WizardPageCreateProject() {
		super("New project");
		this.setTitle("Create a new project");
		this.setDescription("Create a new project");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.
	 * widgets.Composite)
	 */
	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

		final Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Project name");

		this.projectName = new Text(container, SWT.BORDER);
		this.projectName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent event) {
				// Get the widget whose text was modified
				WizardPageCreateProject.this.projectName = (Text) event.widget;
				WizardPageCreateProject.LOGGER.debug("Projectname is {}",
						WizardPageCreateProject.this.projectName.getText());

				final WizardDialogCreateProject myDialog = (WizardDialogCreateProject) WizardPageCreateProject.this
						.getWizard();

				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				final IProject project = root.getProject(WizardPageCreateProject.this.projectName.getText());
				myDialog.setFinish(!project.exists());
				myDialog.getContainer().updateButtons();

				// show an error, if the project exists
				if (project.exists()) {
					WizardPageCreateProject.this.setErrorMessage("Project already exists");
				} else {
					WizardPageCreateProject.this.setErrorMessage(null);
				}
			}
		});
		this.projectName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.setControl(container);
		// setPageComplete(false);
	}

	/** */
	public String getText() {
		return this.projectName.getText();
	}
}
