/**
* Matricula.java
* guardians-dashboard
* 16/4/2015 18:39:54
* Copyright David
* com.app.domain.model.types
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
 * @author David
 *
 */
public class Matricula extends DomainEntity{

	private Instituto instituto;
	
	private InstanciaCurso curso;
	
	private Alumno alumno;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return instituto
	 */
	public Instituto getInstituto() {
		return instituto;
	}

	/**
	 * @param instituto the instituto to set
	 * Establecer el instituto
	 */
	public void setInstituto(Instituto instituto) {
		this.instituto = instituto;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return curso
	 */
	public InstanciaCurso getCurso() {
		return curso;
	}

	/**
	 * @param curso the curso to set
	 * Establecer el curso
	 */
	public void setCurso(InstanciaCurso curso) {
		this.curso = curso;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return alumno
	 */
	public Alumno getAlumno() {
		return alumno;
	}

	/**
	 * @param alumno the alumno to set
	 * Establecer el alumno
	 */
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	
	
	
}
