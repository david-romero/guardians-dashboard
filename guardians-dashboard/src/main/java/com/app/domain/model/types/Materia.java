/**
* AsignaturaImpartida.java
* guardians-dashboard
* 16/4/2015 18:43:14
* Copyright David
* com.app.domain.model.types
*/
package com.app.domain.model.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Assert;

import com.app.domain.model.DomainEntity;
import com.google.gwt.thirdparty.guava.common.collect.Maps;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 *
 */
public class Materia extends DomainEntity{
	
	public Materia(){
		this.itemsEvaluables = new ArrayList<ItemEvaluable>();
		this.criteriosDeEvaluacion = Maps.newHashMap();
	}

	private Asignatura asignatura;
	
	private InstanciaCurso curso;
	
	private CursoAcademico cursoAcademico;
	
	/**
	 * Colección de items evaluables pertenecientes a una asignatura
	 */
	private Collection<ItemEvaluable> itemsEvaluables;
	
	/**
	 * Mapa para almacenar los criterios de evaluación. La clave será un item
	 * evaluable El integer representa el tanto por ciento (0 - 100)
	 */
	private Map<Class<? extends ItemEvaluable>, Integer> criteriosDeEvaluacion;

	/**
	 * @return the criteriosDeEvaluacion Cuando se añada un criterio vigilar que
	 *         el entero oscile entre 0 y 100
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	public Map<Class<? extends ItemEvaluable>, Integer> getCriteriosDeEvaluacion() {
		return criteriosDeEvaluacion;
	}

	/**
	 * @param criteriosDeEvaluacion
	 *            the criteriosDeEvaluacion to set
	 */
	public void setCriteriosDeEvaluacion(
			Map<Class<? extends ItemEvaluable>, Integer> criteriosDeEvaluacion) {
		this.criteriosDeEvaluacion = criteriosDeEvaluacion;
	}
	
	/**
	 * Profesor que imparte la asignatura
	 */
	private Profesor profesor;
	
	/**
	 * Añade un criterio de evaluación a la asignatura
	 * 
	 * @author David Romero Alcaide
	 * @param item
	 * @param porcentaje
	 */
	public void addCriterioDeEvaluacion(Class<? extends ItemEvaluable> item, int porcentaje) {
		final int barrera1 = 101;
		final int barrera2 = 100;
		final int barrera3 = -1;
		Assert.assertTrue(porcentaje > barrera3 && porcentaje < barrera1);
		Assert.assertFalse(obtenerSumaPorcentajes() + porcentaje > barrera2);
		this.criteriosDeEvaluacion.put(item, porcentaje);
	}

	/**
	 * Elimina un criterio de evaluación
	 * 
	 * @author David Romero Alcaide
	 * @param item
	 */
	public void removeCriterioDeEvaluacion(Class<? extends ItemEvaluable>   item) {
		Assert.assertTrue(this.criteriosDeEvaluacion.containsKey(item));
		this.criteriosDeEvaluacion.remove(item);
	}

	/**
	 * Obtiene la suma de los porcentajes de los criterios de evaluación
	 */
	private int obtenerSumaPorcentajes() {
		int sumaPorcentajes = 0;
		for (Integer porcentajes : this.criteriosDeEvaluacion.values()) {
			sumaPorcentajes = sumaPorcentajes + porcentajes;
		}
		return sumaPorcentajes;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	/**
	 * @return the profesor
	 */
	public Profesor getProfesor() {
		return profesor;
	}

	/**
	 * @param profesor
	 *            the profesor to set
	 */
	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}
	
	/**
	 * @return the itemsEvaluables
	 */
	@NotNull
	@OneToMany(mappedBy = "materia")
	public Collection<ItemEvaluable> getItemsEvaluables() {
		return itemsEvaluables;
	}

	/**
	 * @param itemsEvaluables
	 *            the itemsEvaluables to set
	 */
	public void setItemsEvaluables(Collection<ItemEvaluable> itemsEvaluables) {
		this.itemsEvaluables = itemsEvaluables;
	}

	@Valid
	@NotNull
	@ManyToOne(optional=false)
	/**
	 * @return asignatura
	 */
	public Asignatura getAsignatura() {
		return asignatura;
	}

	/**
	 * @param asignatura the asignatura to set
	 * Establecer el asignatura
	 */
	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	@Valid
	@NotNull
	@ManyToOne(optional=false)
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
	@ManyToOne(optional=false)
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
