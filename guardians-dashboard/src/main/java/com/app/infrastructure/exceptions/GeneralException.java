/**
* GeneralException.java
* appEducacional
* 30/07/2014 20:26:30
* Copyright David Romero Alcaide
* com.app.infrastructure.exceptions
*/
package com.app.infrastructure.exceptions;

/**
 * @author David Romero Alcaide
 *
 */
public class GeneralException extends Exception{

	/**
	 * Constructor
	 * @param e
	 */
	public GeneralException(Exception e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
