
package com.todolists.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveTableHandler {
	/** */
	private static Logger LOGGER = LoggerFactory.getLogger(NewTodoHandler.class);

	@Execute
	public void execute(final EPartService partService) {
		final MPart activePart = partService.getActivePart();
		final String partName = activePart.getLabel();
		SaveTableHandler.LOGGER.debug("Saving the table [{}]", partName);
		/* Calls the save-method of the part */
		partService.savePart(activePart, true);
	}

	@CanExecute
	public final boolean canExecute(final EPartService partService) {
		final MPart activePart = partService.getActivePart();
		final String partName = activePart.getLabel();
		// SaveTableHandler.LOGGER.debug("Can execute save for the table [{}]",
		// partName);
		return activePart.isDirty();
	}
}