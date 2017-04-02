
package com.todolists.parts;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDateDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.DateCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.MultiLineTextCellEditor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsSortModel;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todolists.datamodel.Todo;
import com.todolists.db.DBActions;
import com.todolists.events.ProjectEventConstants;
import com.todolists.factory.TodoServiceFactory;
import com.todolists.model.ITodoService;
import com.todolists.model.ITodoServiceProvider;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;

public class TablesPart {
	/** Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(TablesPart.class);
	/** Todo-Service */
	private ITodoService todoService;
	/** Name of the file/table */
	private String tableName;

	@Inject
	ITodoServiceProvider todoServiceProvider;
	@Inject
	MDirtyable dirty;

	/* Column-descs */
	public static String COLUMN_ONE_LABEL = "ColumnOneLabel";
	public static String COLUMN_TWO_LABEL = "ColumnTwoLabel";
	public static String COLUMN_THREE_LABEL = "ColumnThreeLabel";
	public static String COLUMN_FOUR_LABEL = "ColumnFourLabel";
	public static String COLUMN_FIVE_LABEL = "ColumnFiveLabel";

	/* property names of the Person class */
	String[] propertyNames = { "state", "description", "action", "result", "endDate" };

	private transient final EventList<Todo> tableData = GlazedLists.eventList(new ArrayList<Todo>());
	private transient NatTable natTable;

	@PostConstruct
	public void createControls(final Composite parent,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) final IFile file, final EMenuService menuService,
			final MPart part) {
		/* Für sort-header */
		final ConfigRegistry configRegistry = new ConfigRegistry();

		/* mapping from property to label, needed for column header labels */
		final Map<String, String> propertyToLabelMap = new HashMap<String, String>();
		propertyToLabelMap.put("state", "State");
		propertyToLabelMap.put("description", "Description");
		propertyToLabelMap.put("action", "Action");
		propertyToLabelMap.put("result", "Result");
		propertyToLabelMap.put("endDate", "EndDate");

		this.tableName = file.getName();
		/* Get the infos from the service */
		this.todoService = this.todoServiceProvider.getTodoService(this.tableName);
		if (null == this.todoService) {
			/*
			 * If the service has not been created yet Create the service
			 * provider for this table
			 */
			this.todoService = TodoServiceFactory.getInstance(this.tableName);
			this.todoServiceProvider.addTodoService(this.todoService);
		}

		/* Add the infos into the table-list */
		for (final Todo todo : this.todoService.getTodos()) {
			this.tableData.add(todo);
		}

		/* Add the sort functionality */
		final SortedList<Todo> sortedList = new SortedList<>(this.tableData, null);

		/* Do nattable stuff */
		// final TableColumnAccessor accessor = new TableColumnAccessor();
		final IColumnPropertyAccessor<Todo> accessor = new TableColumnAccessor();
		final IDataProvider bodyDataProvider = new ListDataProvider<Todo>(sortedList, accessor);
		final DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		bodyDataLayer.setColumnPercentageSizing(true);

		final SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		final ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// build the column header layer stack
		final IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(this.propertyNames,
				propertyToLabelMap);
		final DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
		final ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

		final SortHeaderLayer<Todo> sortHeaderLayer = new SortHeaderLayer<Todo>(columnHeaderLayer,
				new GlazedListsSortModel<Todo>(sortedList, accessor, configRegistry, columnHeaderDataLayer));

		/* build the row header layer stack */
		final IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
		final DataLayer rowHeaderDataLayer = new DataLayer(rowHeaderDataProvider, 40, 20);
		final ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, viewportLayer, selectionLayer);

		/* build the corner layer stack */
		final ILayer cornerLayer = new CornerLayer(
				new DataLayer(new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider)),
				rowHeaderLayer, columnHeaderLayer);
		/* Put sortHeaderLayer hier as columnHeaderLayer */
		final GridLayer gridLayer = new GridLayer(viewportLayer, sortHeaderLayer, rowHeaderLayer, cornerLayer);

		/* add cols */
		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
		bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);
		columnLabelAccumulator.registerColumnOverrides(0, TablesPart.COLUMN_ONE_LABEL);
		columnLabelAccumulator.registerColumnOverrides(1, TablesPart.COLUMN_TWO_LABEL);
		columnLabelAccumulator.registerColumnOverrides(2, TablesPart.COLUMN_THREE_LABEL);
		columnLabelAccumulator.registerColumnOverrides(3, TablesPart.COLUMN_FOUR_LABEL);
		columnLabelAccumulator.registerColumnOverrides(4, TablesPart.COLUMN_FIVE_LABEL);

		this.natTable = new NatTable(parent, gridLayer, false);
		this.natTable.setConfigRegistry(configRegistry); /* for sort */
		this.natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		this.natTable.addConfiguration(new EditorConfiguration());
		this.natTable.addConfiguration(new SingleClickSortConfiguration());

		/* TODO add contextmenu */
		menuService.registerContextMenu(this.natTable, "com.todolists.popupmenu.newtodo");

		// get the menu registered by EMenuService
		final Menu e4Menu = this.natTable.getMenu();

		// remove the menu reference from NatTable instance
		this.natTable.setMenu(null);

		this.natTable.addConfiguration(new AbstractUiBindingConfiguration() {
			@Override
			public void configureUiBindings(final UiBindingRegistry uiBindingRegistry) {
				// add NatTable menu items and register the DisposeListener
				new PopupMenuBuilder(TablesPart.this.natTable, e4Menu).build();

				// uncomment this to automatically add an inspection command to
				// the view new PopupMenuBuilder(natTable,
				// e4Menu).withInspectLabelsMenuItem().build();

				// register the UI binding
				uiBindingRegistry.registerMouseDownBinding(
						new MouseEventMatcher(SWT.NONE, GridRegion.BODY, MouseEventMatcher.RIGHT_BUTTON),
						new PopupMenuAction(e4Menu));
			}
		});

		this.natTable.configure();
		GridDataFactory.fillDefaults().grab(true, true).applyTo(this.natTable);

		/* Set the state of the part to "opened" */
		final Map<String, String> persistedState = part.getPersistedState();
		persistedState.put("partstate", "opened");
	}

	/**
	 * Set the state of the part
	 *
	 * @param part
	 */
	@PersistState
	public void persistState(final MPart part) {
		/* Set the state of the part to "opened" */
		final Map<String, String> persistedState = part.getPersistedState();
		persistedState.put("partstate", "closed");
		/* Clear the nattable */
		this.tableData.clear();
	}

	/**
	 * Will be called, if a part saves a table
	 *
	 * @param partService
	 *            Part Service
	 * @throws CoreException
	 * @throws IOException
	 */
	@Persist
	public void save(final EPartService partService) throws CoreException, IOException {
		final String fileName = partService.getActivePart().getLabel();
		final ITodoService todoService = this.todoServiceProvider.getTodoService(fileName);

		TablesPart.LOGGER.debug("Save the part [{}]", fileName);
		for (final Todo todo : this.tableData) {
			TablesPart.LOGGER.debug("Saving [{}]", todo.toString());
			// Save in model
			todoService.saveTodo(todo);
		}

		/*
		 * Write to file. The description has the projectname. We saved it as we
		 * created the part.
		 */
		// this.writeToFile(this.todoService,
		// partService.getActivePart().getDescription(), fileName);
		this.saveDatabase(this.todoService, partService.getActivePart().getDescription(), fileName);
		// Saved
		TablesPart.this.dirty.setDirty(false);
	}

	/**
	 *
	 * @param todoService2
	 * @param description
	 * @param fileName
	 */
	private void saveDatabase(final ITodoService todoService, final String projectName, final String fileName) {
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getFile(fileName);
		final String absPath = file.getProject().getLocation().toString();
		final List<Todo> allTodosFromDB = DBActions.getAllTodosFromDB(absPath + File.separator + fileName);

		// TODO a better solution here
		for (final Todo todo : this.todoService.getTodos()) {
			boolean found = false;
			/* Check if this todo is in db */
			for (final Todo dbTodo : allTodosFromDB) {
				if (todo.getId() == dbTodo.getId()) {
					found = true;
				}
			}
			if (!found) {
				/* Not found, so insert it */
				DBActions.insertElement(absPath, fileName, todo);
			} else {
				/* Found, update if there are differences */
				DBActions.updateElement(absPath, fileName, todo);
			}
		}
	}

	// /**
	// * Writes the content of a todoService in to a tbl file
	// *
	// * @throws CoreException
	// * @throws IOException
	// *
	// * @param todoService
	// *
	// */
	// private void writeToFile(final ITodoService todoService, final String
	// projectName, final String fileName)
	// throws CoreException, IOException {
	// TablesPart.LOGGER.debug("[writeToFile] writeToFile() is empty");
	// // Clear the file content and write everything from table to file
	// final IFile file =
	// ResourcesPlugin.getWorkspace().getRoot().getProject(projectName).getFile(fileName);
	//
	// final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
	// Locale.GERMAN);
	// final StringBuilder content = new StringBuilder();
	// for (final Todo todo : todoService.getTodos()) {
	// final String endDateformat = formatter.format(todo.getEndDate());
	// content.append("[" + todo.getState() + "," + todo.getDescription() + ","
	// + todo.getAction() + ","
	// + todo.getResult() + "," + endDateformat + "]\n");
	// }
	// try (final InputStream inputSource = new
	// ByteArrayInputStream(content.toString().getBytes())) {
	// file.setContents(inputSource, IResource.FORCE, null);
	// }
	// }

	// TODO nattable and setfocus not good. opens always the first opened table
	// @Focus
	// public void setFocus() {
	// TablesPart.LOGGER.warn("setFocus() is empty");
	// // this.tableViewer.getControl().setFocus();
	// }

	@Inject
	@Optional
	private void subscribeTopicTodoAllTopics(final EPartService partService,
			@UIEventTopic(ProjectEventConstants.TOPIC_TODO) final String event) {
		TablesPart.LOGGER.warn("subscribeTopicTodoAllTopics() called for the [{}]", this.tableName);

		/*
		 * If this is the service of the active part, then continue, else return
		 */
		if (!this.tableName.equals(partService.getActivePart().getLabel())) {
			return;
		}
		/*
		 * Check, if the service has been created. If not, this means, the
		 * file/table was created in the past
		 */
		if (null == this.todoService) {
			/* If the service has not been created yet */
			// Create the service provider for this table
			this.todoService = TodoServiceFactory.getInstance(this.tableName);
		}

		if (event.equals(ProjectEventConstants.TOPIC_TODO_NEW)) {
			/* Add the new item */
			final Todo todo = this.todoService.createTodo("New", "<empty>", "<empty>", "<empty>", new Date());
			this.todoService.saveTodo(todo);
			this.tableData.add(todo);
			/* If a new todo inserted, then it is dirty */
			partService.getActivePart().setDirty(true);
			// TablesPart.this.dirty.setDirty(true);
			/* refresh the table */
			this.natTable.refresh();
		}
	}

	/**
	 * Class for nattable cells-definitions
	 *
	 * @author user
	 *
	 */
	class EditorConfiguration extends AbstractRegistryConfiguration {
		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.nebula.widgets.nattable.config.IConfiguration#
		 * configureRegistry(org.eclipse.nebula.widgets.nattable.config.
		 * IConfigRegistry)
		 */
		@Override
		public void configureRegistry(final IConfigRegistry configRegistry) {
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
					IEditableRule.ALWAYS_EDITABLE);

			this.registerColumnComboBoxStatus(configRegistry);
			this.registerMultilineEditor(configRegistry);
			this.registerColumnDateEditor(configRegistry);
		}

		/*
		 * Status
		 */
		private void registerColumnComboBoxStatus(final IConfigRegistry configRegistry) {
			/* register the combobox editor */
			final ComboBoxCellEditor comboBoxCellEditor = new ComboBoxCellEditor(
					Arrays.asList("New", "In Progress", "Finished"));
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboBoxCellEditor,
					DisplayMode.EDIT, TablesPart.COLUMN_ONE_LABEL);
		}

		/*
		 * Multiline - cols description, action and result
		 */
		private void registerMultilineEditor(final IConfigRegistry configRegistry) {
			/* configure the multi line cols */
			final Style cellStyle = new Style();
			cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.LEFT);
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new MultiLineTextCellEditor(true),
					DisplayMode.NORMAL, TablesPart.COLUMN_TWO_LABEL);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
					TablesPart.COLUMN_TWO_LABEL);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.EDIT,
					TablesPart.COLUMN_TWO_LABEL);

			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new MultiLineTextCellEditor(true),
					DisplayMode.NORMAL, TablesPart.COLUMN_THREE_LABEL);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
					TablesPart.COLUMN_THREE_LABEL);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.EDIT,
					TablesPart.COLUMN_THREE_LABEL);

			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new MultiLineTextCellEditor(true),
					DisplayMode.NORMAL, TablesPart.COLUMN_FOUR_LABEL);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL,
					TablesPart.COLUMN_FOUR_LABEL);
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.EDIT,
					TablesPart.COLUMN_FOUR_LABEL);
		}

		/*
		 * Date
		 */
		private void registerColumnDateEditor(final IConfigRegistry configRegistry) {
			configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, new DateCellEditor(),
					DisplayMode.EDIT, TablesPart.COLUMN_FIVE_LABEL);

			final DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
			final String pattern = ((SimpleDateFormat) formatter).toPattern();

			// using a DateCellEditor also needs a Date conversion to work
			// correctly
			configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER,
					new DefaultDateDisplayConverter(pattern), DisplayMode.NORMAL, TablesPart.COLUMN_FIVE_LABEL);
		}
	}

	/**
	 * Here we want to set the parts dirty to true, if a new value will be set
	 */
	class TableColumnAccessor implements IColumnPropertyAccessor<Todo> {
		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#getDataValue
		 * (java.lang.Object, int)
		 */
		@Override
		public Object getDataValue(final Todo rowObject, final int columnIndex) {
			switch (columnIndex) {
			case 0:
				return rowObject.getState();
			case 1:
				return rowObject.getDescription();
			case 2:
				return rowObject.getAction();
			case 3:
				return rowObject.getResult();
			case 4:
				return rowObject.getEndDate();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#setDataValue
		 * (java.lang.Object, int, java.lang.Object)
		 */
		@Override
		public void setDataValue(final Todo rowObject, final int columnIndex, final Object newValue) {
			// because of the registered conversion, the new value has to be an
			// Integer
			switch (columnIndex) {
			case 0:
				rowObject.setState((String) newValue);
				break;
			case 1:
				rowObject.setDescription((String) newValue);
				break;
			case 2:
				rowObject.setAction((String) newValue);
				break;
			case 3:
				rowObject.setResult((String) newValue);
				break;
			case 4:
				rowObject.setEndDate((Date) newValue);
				break;
			}
			// Set dirty
			TablesPart.this.dirty.setDirty(true);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.nebula.widgets.nattable.data.IColumnAccessor#
		 * getColumnCount ()
		 */
		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public String getColumnProperty(final int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "state";
			case 1:
				return "description";
			case 2:
				return "action";
			case 3:
				return "result";
			case 4:
				return "endDate";
			}
			return null;
		}

		@Override
		public int getColumnIndex(final String propertyName) {
			switch (propertyName) {
			case "state":
				return 0;
			case "description":
				return 1;
			case "action":
				return 2;
			case "result":
				return 3;
			case "endDate":
				return 4;
			}

			return -1;
		}
	}

	/**
	 * Add a new row
	 *
	 * @param data
	 *            Data
	 */
	public void addNewRow(final Todo todo) {
		this.tableData.add(todo);
		this.natTable.refresh();
		// New Item
		TablesPart.this.dirty.setDirty(false);
	}
}