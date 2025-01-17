/**
* RelacionLaboral.java
* guardians-dashboard
* 17/4/2015 20:00:32
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
public class RelacionLaboral extends DomainEntity {
	
	private Profesor profesor;
	
	private Instituto instituto;
	
	private CursoAcademico cursoAcademico;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return profesor
	 */
	public Profesor getProfesor() {
		return profesor;
	}

	/**
	 * @param profesor the profesor to set
	 * Establecer el profesor
	 */
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

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
	 * @return cursoAcademico
	 */
	public CursoAcademico getCursoAcademico() {
		return cursoAcademico;
	}

	/**
	 * @param cursoAcademico the cursoAcademico to set
	 * Establecer el cursoAcademico
	 */
	public void setCursoAcademico(CursoAcademico cursoAcademico) {
		this.cursoAcademico = cursoAcademico;
	}
	
	

}
