/**
 * Cita.java
 */
package com.app.domain.model.types;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 16:45
 */
public class Cita extends Notificacion {

	/**
	 * Empty constructor
	 */
	public Cita() {
		super();
	}

	/**
	 * confirmado profesor
	 */
	private boolean confirmadoProfesor;

	/**
	 * confirmado tutor
	 */
	private boolean confirmadoTutor;

	/**
	 * @return confirmadoProfesor
	 */
	public boolean isConfirmadoProfesor() {
		return confirmadoProfesor;
	}

	/**
	 * @param confirmadoProfesor
	 *            the confirmadoProfesor to set Establecer el confirmadoProfesor
	 */
	public void setConfirmadoProfesor(boolean confirmadoProfesor) {
		this.confirmadoProfesor = confirmadoProfesor;
	}

	/**
	 * @return confirmadoTutor
	 */
	public boolean isConfirmadoTutor() {
		return confirmadoTutor;
	}

	/**
	 * @param confirmadoTutor
	 *            the confirmadoTutor to set Establecer el confirmadoTutor
	 */
	public void setConfirmadoTutor(boolean confirmadoTutor) {
		this.confirmadoTutor = confirmadoTutor;
	}

}
