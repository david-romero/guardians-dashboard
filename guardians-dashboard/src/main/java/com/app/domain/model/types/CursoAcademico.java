/**
 * CursoAcademico.java
 * @author David
 * @copyright David Romero Alcaide
 * 22/07/2014 17:00
 */
package com.app.domain.model.types;

/**
 * imports
 */
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.app.domain.model.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * Clase que representa a un curso en un año academico
 * Ej: 3 E.S.O. B
 */
public class CursoAcademico extends DomainEntity {

	/**
	 * Constructor para inicializar coleciones
	 */
	public CursoAcademico() {
		super();
	}

	/**
	 * inicio del curso academico casi siempre septiembre
	 */
	private Date inicio;
	/**
	 * 13/14, 15/16 etc...
	 */
	private String denominacion;

	/**
	 * fin del curso academico
	 */
	private Date fin;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	/**
	 * @return inicio
	 */
	public Date getInicio() {
		return inicio;
	}

	/**
	 * @param inicio
	 *            the inicio to set Establecer el inicio
	 */
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	@NotBlank
	/**
	 * @return denominacion
	 */
	public String getDenominacion() {
		return denominacion;
	}

	/**
	 * @param denominacion
	 *            the denominacion to set Establecer el denominacion
	 */
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	/**
	 * @return fin
	 */
	public Date getFin() {
		return fin;
	}

	/**
	 * @param fin
	 *            the fin to set Establecer el fin
	 */
	public void setFin(Date fin) {
		this.fin = fin;
	}

}
