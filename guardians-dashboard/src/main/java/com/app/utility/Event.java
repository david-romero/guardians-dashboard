/**
 * Event.java
 * appEducacional
 * 02/04/2014 13:25:00
 * Copyright David Romero Alcaide
 * com.app.utility
 */
package com.app.utility;

import java.util.Date;

/**
 * @author David Romero Alcaide Esta clase solo se utiliza para el caso de uso
 *         Ver Ficha para obtener el Calendario de Jquery
 */
public class Event {

	/**
	 * Constructor
	 */
	public Event() {
		super();

	}

	/**
	 * Constructor
	 * 
	 * @param title
	 * @param description
	 * @param date
	 * @param type
	 */
	public Event(String title, String description, Date date, String type) {
		super();
		this.title = title;
		this.description = description;
		this.date = date;
		this.type = type;
	}

	private String title;

	private String description;

	private Date date;

	private String type;

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set Establecer el title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set Establecer el description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set Establecer el date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set Establecer el type
	 */
	public void setType(String type) {
		this.type = type;
	}

}
