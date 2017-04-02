
package com.todolists.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class AboutHandler {
	@Execute
	public void execute(final Shell shell) {
		MessageDialog.openInformation(shell, "About", "Manage a todolist v1.0.0.\n\nEmail : alakese@googlemail.com");
	}

}