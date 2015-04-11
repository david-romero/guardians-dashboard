/**
 * PadreMadreOTutor.java
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 18:20
 */
package com.app.domain.model.types;

/**
 * imports
 */
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.util.Assert;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * Clase que representa al tutor legal del alumno en la educación del mismo
 */
public class PadreMadreOTutor extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6043543732172413695L;

	/**
	 * Constructor que inicializa las colecciones.
	 */
	public PadreMadreOTutor() {
		super();
		this.notificaciones = new ArrayList<Notificacion>();
		this.tutorandos = new ArrayList<Alumno>();
	}

	// Atributos

	// Relaciones

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (getNombre() != null ? "" + getNombre() + " " : "")
				+ (getApellidos() != null ? "" + getApellidos() : "")
				+ (getTelefono() != null ? ", Tlf: " + getTelefono() : "")
				+ (getEmail() != null ? ", Email: " + getEmail() : "");
	}

	/**
	 * Notificaciones enviadas y recibidas por el padre, madre o tutor
	 */
	private Collection<Notificacion> notificaciones;
	/**
	 * Tutorandos propios
	 */
	private Collection<Alumno> tutorandos;

	@NotNull
	@OneToMany(mappedBy = "padreMadreOTutor")
	/**
	 * @return the notificaciones
	 */
	public Collection<Notificacion> getNotificaciones() {
		return notificaciones;
	}

	/**
	 * @param notificaciones
	 *            the notificaciones to set
	 */
	public void setNotificaciones(Collection<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}

	/**
	 * Añade una notificación al padre, madre o tutor
	 * 
	 * @author David Romero Alcaide
	 * @param notificacion
	 */
	public void addNotificacion(Notificacion notificacion) {
		this.notificaciones.add(notificacion);
		notificacion.setPadreMadreOTutor(this);
	}

	/**
	 * Elimina una notificación
	 * 
	 * @author David Romero Alcaide
	 * @param notificacion
	 * @return
	 */
	public boolean removeNotificacion(Notificacion notificacion) {
		notificacion.setPadreMadreOTutor(null);
		return this.notificaciones.remove(notificacion);
	}

	@NotNull
	@ManyToMany(mappedBy = "padresMadresOTutores")
	/**
	 * @return the tutorandos
	 */
	public Collection<Alumno> getTutorandos() {
		return tutorandos;
	}

	/**
	 * @param tutorandos
	 *            the tutorandos to set
	 */
	public void setTutorandos(Collection<Alumno> tutorandos) {
		this.tutorandos = tutorandos;
	}

	/**
	 * agrega vinculación con un tutorando
	 * 
	 * @author David Romero Alcaide
	 * @param alumno
	 */
	public void addTutorando(Alumno alumno) {
		Assert.isTrue(!this.tutorandos.contains(alumno));
		this.tutorandos.add(alumno);
	}

	/**
	 * Elimina vinculación con un tutorando
	 * 
	 * @author David Romero Alcaide
	 * @param alumno
	 * @return
	 */
	public boolean removeTutorando(Alumno alumno) {
		Assert.isTrue(this.tutorandos.contains(alumno));
		alumno.removePadreMadreOTutor(this);
		return this.tutorandos.remove(alumno);
	}
}
