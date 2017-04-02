package com.todolists.events;

/**
 * Events. See Vogella.
 *
 * @author Yasin Alakese
 *
 */
public interface ProjectEventConstants {
	// Vogella-Info : topic identifier for all todo topics
	String TOPIC_TODO = "TOPIC_TODOS";

	// Vogella-Info : this key can only be used for event registration, you
	// cannot send out generic events
	String TOPIC_PROJECT_ALLTOPICS = "TOPIC_TODOS/PROJECT/*";
	String TOPIC_PROJECT_NEW = "TOPIC_TODOS/PROJECT/NEW";
	String TOPIC_PROJECT_DELETED = "TOPIC_TODOS/PROJECT/DELETED";
	String TOPIC_PROJECT_OPENED = "TOPIC_TODOS/PROJECT/OPENED";
	String TOPIC_PROJECT_CLOSED = "TOPIC_TODOS/PROJECT/CLOSED";

	String TOPIC_TABLE_NEW = "TOPIC_TODOS/PROJECT/TABLE/NEW";
	String TOPIC_TABLE_OPEN = "TOPIC_TODOS/PROJECT/TABLE/OPEN";
	String TOPIC_TABLE_DELETED = "TOPIC_TODOS/PROJECT/TABLE/DELETED";

	String TOPIC_ALLTOPICS = "TOPIC_TODOS/*";

	String TOPIC_TODO_ALLTOPICS = "TOPIC_TODOS/TODO/*";
	String TOPIC_TODO_NEW = "TOPIC_TODOS/TODO/NEW";
	String TOPIC_TODO_SAVED = "TOPIC_TODOS/TODO/SAVED";
	String TOPIC_TODO_READ = "TOPIC_TODOS/TODO/READ";
}
