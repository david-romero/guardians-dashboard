/**
* InstanciaCurso.java
* guardians-dashboard
* 16/4/2015 20:27:12
* Copyright David
* com.app.domain.model.types
*/
package com.app.domain.model.types;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
public class InstanciaCurso extends DomainEntity{

	private Curso curso;
	
	private CursoAcademico cursoAcademico;
	
	private Profesor profesor;
	
	/**
	 * alumnos adscritos en este curso
	 */
	private Collection<Alumno> alumnos;
	
	@Valid
	@NotNull
	@ManyToMany(mappedBy = "cursos")
	/**
	 * @return the alumnos
	 */
	public Collection<Alumno> getAlumnos() {
		return alumnos;
	}

	/**
	 * @param alumnos
	 *            the alumnos to set
	 */
	public void setAlumnos(Collection<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	
	@NotNull
	@Valid
	@ManyToOne()
	/**
	 * @return cursoAcademico
	 */
	public CursoAcademico getCursoAcademico() {
		return cursoAcademico;
	}

	/**
	 * @param cursoAcademico
	 *            the cursoAcademico to set Establecer el cursoAcademico
	 */
	public void setCursoAcademico(CursoAcademico cursoAcademico) {
		this.cursoAcademico = cursoAcademico;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return curso
	 */
	public Curso getCurso() {
		return curso;
	}

	/**
	 * @param curso the curso to set
	 * Establecer el curso
	 */
	public void setCurso(Curso curso) {
		this.curso = curso;
	}

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
	
	
	
	
	
}
