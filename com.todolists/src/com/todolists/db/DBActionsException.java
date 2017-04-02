package com.todolists.db;

/**
 *
 * @author user
 *
 */
public class DBActionsException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DBActionsException() {
	}

	public DBActionsException(final String arg0) {
		super(arg0);
	}

	public DBActionsException(final Throwable arg0) {
		super(arg0);
	}

	public DBActionsException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public DBActionsException(final String arg0, final Throwable arg1, final boolean arg2, final boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
