package com.todolists.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
 * Wizard page which takes a table name. If it exists, it shows an error text on
 * the page.
 *
 * @author Yasin Alakese
 * @date 02.10.2016
 */
public class WizardPageCreateTable extends WizardPage {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(WizardPageCreateTable.class);
	/** */
	private Text tableName;
	/** */
	private final IProject project;

	/**
	 * @param project
	 */
	public WizardPageCreateTable(final IProject project) {
		super("New table");
		this.setTitle("Create a new table");
		this.setDescription("Create a new table");
		this.project = project;
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
		lblNewLabel.setText("Table name");

		this.tableName = new Text(container, SWT.BORDER);
		this.tableName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent event) {
				// Get the widget whose text was modified
				WizardPageCreateTable.this.tableName = (Text) event.widget;
				WizardPageCreateTable.LOGGER.debug("Entered-Tablename is {}",
						WizardPageCreateTable.this.tableName.getText());

				final WizardDialogCreateTable myDialog = (WizardDialogCreateTable) WizardPageCreateTable.this
						.getWizard();
				/* Get the project */
				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				final IProject project = root.getProject(WizardPageCreateTable.this.project.getName());
				/* Look, if file/table exists */
				boolean fileExists = false;
				WizardPageCreateTable.LOGGER.debug("Checking if the table already exists.");
				try {
					for (final IResource resource : project.members()) {
						if (resource instanceof IFile) {
							if (resource.getName().equals(".project")) {
								continue;
							}
							final String fileNameWithoutExt = resource.getName().substring(0,
									resource.getName().indexOf("."));
							WizardPageCreateTable.LOGGER.debug("Found file {} vs. entered {}", fileNameWithoutExt,
									WizardPageCreateTable.this.tableName.getText());
							if (fileNameWithoutExt.equals(WizardPageCreateTable.this.tableName.getText())) {
								fileExists = true;
							}
						}
					}
				} catch (final CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				myDialog.setFinish(!fileExists);
				myDialog.getContainer().updateButtons();

				if (fileExists) {
					WizardPageCreateTable.this.setErrorMessage("Table already exists");
				} else {
					WizardPageCreateTable.this.setErrorMessage(null);
				}
			}
		});
		this.tableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		this.setControl(container);
		// setPageComplete(false);
	}

	/** */
	public String getText() {
		return this.tableName.getText();
	}

}
