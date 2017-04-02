
package com.todolists.handlers;

import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.datamodel.Todo;
import com.todolists.db.DBActions;
import com.todolists.events.ProjectEventConstants;
import com.todolists.factory.TodoServiceFactory;
import com.todolists.model.ITodoService;
import com.todolists.model.ITodoServiceProvider;

/**
 * Opens a created table
 *
 * @author Yasin Alakese
 * @since 06.12.2016
 */
public class OpenTableHandler {
	/** Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(OpenTableHandler.class);

	/** */
	@Inject
	ITodoServiceProvider todoServiceProvider;

	/**
	 *
	 * @param application
	 * @param partService
	 * @param modelService
	 * @param shell
	 * @param file
	 * @throws CoreException
	 *             - if the file cant be read
	 * @throws ParseException
	 *             - if the date-info -which will be read from the file- can not
	 *             be parsed into a date-object
	 */
	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IFile file,
			final MApplication application, final EPartService partService, final EModelService modelService,
			final Shell shell, final IEventBroker broker) throws CoreException, ParseException {
		/* Tablename = filename */
		final String tableName = file.getName();
		OpenTableHandler.LOGGER.debug("Opening table : [{}]", tableName);

		/* check, if the editor is already open? */
		final List<MPart> parts = (List<MPart>) partService.getParts();
		for (final MPart mPart : parts) {
			final String foundLabel = mPart.getLabel();
			if ((foundLabel != null) && foundLabel.equals(tableName)) {
				if ("opened".equals(mPart.getPersistedState().get("partstate"))) {
					/* yeah, it is opened */
					// part exists, just show it
					partService.showPart(mPart, EPartService.PartState.ACTIVATE);
					OpenTableHandler.LOGGER.debug("Table is opened already.");
					return;
				}
			}
		}

		/*
		 * get the infos from the db
		 */
		final String absPath = file.getLocation().toString();
		final List<Todo> allTodosFromDB = DBActions.getAllTodosFromDB(absPath);

		/*
		 * Read the file first, because as soon as we call
		 * partService.showPart(part, PartState.ACTIVATE) we need the info to
		 * fill the nattable
		 */
		// final List<String> todosFromFile = new ArrayList<>();
		// try (final Scanner scanner = new Scanner(file.getContents())) {
		// final StringBuffer buffer = new StringBuffer(512);
		// while (scanner.hasNextLine()) {
		// // todosFromFile.add(scanner.nextLine());
		// /*
		// * in multilines it is possible that a line starts with [ but
		// * not ends with ] or starts and ends with both or neither
		// */
		// final String nextLine = scanner.nextLine();
		// /* save it to buffer */
		// buffer.append(nextLine);
		// /* ] means it has ended */
		// if (nextLine.endsWith("]")) {
		// todosFromFile.add(buffer.toString());
		// /* clear the buffer */
		// buffer.setLength(0);
		// } else {
		// /* then this is multiline text */
		// buffer.append(System.lineSeparator());
		// }
		// }
		// }

		/* Put the info into todoservice */
		ITodoService todoService = this.todoServiceProvider.getTodoService(tableName);
		if (null == todoService) {
			/* If the service has not been created yet */
			// Create the service provider for this table
			todoService = TodoServiceFactory.getInstance(tableName);
			this.todoServiceProvider.addTodoService(todoService);
		}

		/* Each time the table opens, must clear the list */
		todoService.clearTodos();

		/* Now load the todos again */
		for (final Todo todo : allTodosFromDB) {
			todoService.saveTodo(todo);
		}
		// for (final String todoString : todosFromFile) {
		// final String line = todoString.replaceAll("\\[",
		// "").replaceAll("\\]", "");
		// final String[] lineSplit = line.split(",");
		// /* state, description, action and result, date */
		// final DateFormat format = new SimpleDateFormat("dd/MM/yyyy",
		// Locale.GERMAN);
		// final Date enddate = format.parse(lineSplit[4]);
		// final Todo createdTodo = todoService.createTodo(lineSplit[0],
		// lineSplit[1], lineSplit[2], lineSplit[3],
		// enddate);
		// todoService.saveTodo(createdTodo);
		// }

		/*
		 * this editor is not opened, so create it -table will be created
		 * dynamically-
		 */
		final MPart part = partService.createPart("com.todolists.partdescriptor.todotable");
		part.setLabel(tableName);
		part.setCloseable(true);
		/*
		 * We need to get the projectname from the mpart. Trick : write the
		 * projectname as description and read it
		 */
		part.setDescription(file.getParent().getName());

		final List<MPartStack> stacks = modelService.findElements(application, "com.todolists.partstack.tables",
				MPartStack.class, null);
		if (stacks.size() < 1) {
			MessageDialog.openError(shell, "Error ",
					"part stack not found. ID correct? " + "com.todolists.partstack.tables");
			return;
		}
		stacks.get(0).getChildren().add(part);
		partService.showPart(part, PartState.ACTIVATE);

		OpenTableHandler.LOGGER.debug("Table {} opened. Reading the contents from the table", tableName);

		/* Send an event */
		broker.post(ProjectEventConstants.TOPIC_TODO, ProjectEventConstants.TOPIC_TODO_READ);
		OpenTableHandler.LOGGER.debug("The table read from file [{}] successfully.", file.getFullPath());
	}
}
