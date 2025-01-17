/**
 * Asignatura.java
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 16:30
 */
package com.app.domain.model.types;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.app.domain.model.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David Romero Alcaide
 * Clase que representa una asignatura impartida por un profesor perteneciente
 * a un curso
 */
public class Asignatura extends DomainEntity {

	/**
	 * 
	 * Constructor para inicializar colecciones y mapas
	 */
	public Asignatura() {
		super();
		
	}
	
	/**
	 * Nombre o título de la asignatura
	 */
	private String nombre;




	/**
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set Establecer el nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Relaciones

	/**
	 * Curso al que pertenece la asignatura
	 */
	private Curso curso;



	/**
	 * @return the curso
	 */
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Curso getCurso() {
		return curso;
	}

	/**
	 * @param curso
	 *            the curso to set
	 */
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Asignatura other = (Asignatura) obj;
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		return true;
	}
}
