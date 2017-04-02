package com.todolists.parts;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

/**
 * @deprecated Using Todo instead
 *
 * @author user
 *
 */
@Deprecated
public class TableInfo {
	protected final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	private String state;
	private String description;
	private String action;
	private String result;
	private Date endDate;

	public TableInfo(final String state, final String description, final String action, final String result,
			final Date endDate) {
		this.state = state;
		this.description = description;
		this.action = action;
		this.result = result;
		this.endDate = endDate;
	}

	public void addPropertyChangeListener(final PropertyChangeListener l) {
		this.changeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(final PropertyChangeListener l) {
		this.changeSupport.removePropertyChangeListener(l);
	}

	/*
	 * Achtung : Methodenname soll dem Variablennamen gleich sein -case
	 * sensitive
	 */
	public String getState() {
		return this.state;
	}

	public void setState(final String state) {
		this.changeSupport.firePropertyChange("state", this.state, this.state = state);
	}

	/*
	 * Achtung : Methodenname soll dem Variablennamen gleich sein -case
	 * sensitive
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.changeSupport.firePropertyChange("description", this.description, this.description = description);
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(final String action) {
		this.changeSupport.firePropertyChange("action", this.action, this.action = action);
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(final String result) {
		this.changeSupport.firePropertyChange("result", this.result, this.result = result);
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(final Date endDate) {
		this.changeSupport.firePropertyChange("endDate", this.endDate, this.endDate = endDate);
	}

	@Override
	public String toString() {
		return "[State : " + this.state + ", Description : " + this.description + ", Action : " + this.action
				+ ", Result : " + this.result + ", Date : " + this.endDate.toString() + "]";
	}
}
