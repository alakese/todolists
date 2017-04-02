package com.todolists.datamodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

/**
 * Ein Todo-Item
 *
 * @author user
 *
 */
public class Todo {
	public final long id;
	private String state = "";
	private String description = "";
	private String action = "";
	private String result = "";
	private Date endDate;

	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	public static final String FIELD_ID = "id";
	public static final String FIELD_STATE = "state";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_ACTION = "action";
	public static final String FIELD_RESULT = "result";
	public static final String FIELD_ENDDATE = "endDate";

	/**
	 * c'tor
	 *
	 * @param id
	 */
	public Todo(final long id) {
		this.id = id;
	}

	public Todo(final long id, final String state, final String description, final String action, final String result,
			final Date endDate) {
		this.id = id;
		this.state = state;
		this.description = description;
		this.action = action;
		this.result = result;
		this.endDate = endDate;
	}

	public String getState() {
		return this.state;
	}

	public void setState(final String state) {
		this.changes.firePropertyChange(Todo.FIELD_STATE, this.state, this.state = state);
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.changes.firePropertyChange(Todo.FIELD_DESCRIPTION, this.description, this.description = description);
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(final String action) {
		this.changes.firePropertyChange(Todo.FIELD_ACTION, this.action, this.action = action);
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(final String result) {
		this.changes.firePropertyChange(Todo.FIELD_RESULT, this.result, this.result = result);
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.changes.firePropertyChange(Todo.FIELD_ENDDATE, this.endDate, this.endDate = endDate);
	}

	public long getId() {
		return this.id;
	}

	public Todo copy() {
		return new Todo(this.id, this.state, this.description, this.action, this.result, this.endDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Todo other = (Todo) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Todo [id=" + this.id + ", state=" + this.state + ", description=" + this.description + ", action="
				+ this.action + ", result=" + this.result + ", date=" + this.endDate.toString() + "]";
	}

	public void addPropertyChangeListener(final PropertyChangeListener l) {
		this.changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(final PropertyChangeListener l) {
		this.changes.removePropertyChangeListener(l);
	}
}
