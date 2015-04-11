/**
 * NotaPorEvaluacion.java
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 18:00
 */
package com.app.domain.model.types;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.app.domain.model.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "alumno_id",
		"asignatura_id", "evaluacion_id" }))
/**
 * @author David
 * Clase que representa a la nota obtenida por una alumno para una asignatura
 * en una evaluación.
 */
public class NotaPorEvaluacion extends DomainEntity {

	/**
	 * Constructor vacío
	 */
	public NotaPorEvaluacion() {
		super();
	}

	/**
	 * Calculada a partir de sus items evaluables y los criterios de evaluación
	 * de la asignatura
	 */
	private double notaFinal;

	@Range(min = 0, max = 10)
	/**
	 * @return the notaFinal
	 */
	public double getNotaFinal() {
		return notaFinal;
	}

	/**
	 * @param notaFinal
	 *            the notaFinal to set
	 */
	public void setNotaFinal(double notaFinal) {
		this.notaFinal = notaFinal;
	}

	// Relaciones

	/**
	 * Alumno que ha obtenido la calificación
	 */
	private Alumno alumno;
	/**
	 * Evaluación a la que pertenece la nota
	 */
	private Evaluacion evaluacion;
	/**
	 * Asignatura a la que pertenece la calificación.
	 */
	private Asignatura asignatura;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return the asignatura
	 */
	public Asignatura getAsignatura() {
		return asignatura;
	}

	/**
	 * @param asignatura
	 *            the asignatura to set
	 */
	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	@Valid
	@NotNull
	@ManyToOne
	/**
	 * @return the alumno
	 */
	public Alumno getAlumno() {
		return alumno;
	}

	/**
	 * @param alumno
	 *            the alumno to set
	 */
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return the evaluacion
	 */
	public Evaluacion getEvaluacion() {
		return evaluacion;
	}

	/**
	 * @param evaluacion
	 *            the evaluacion to set
	 */
	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}

}
