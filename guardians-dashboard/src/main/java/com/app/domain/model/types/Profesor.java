/**
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 18:45
 */
package com.app.domain.model.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Assert;

import com.google.gwt.thirdparty.guava.common.collect.Sets;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * Esta clase es la encargada de simular el trabajo diario de un profesor.
 */
public class Profesor extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3057914910504296347L;

	/**
	 * 
	 */
	public Profesor() {
		super();
		this.eventos = new ArrayList<Evento>();
		this.materias = Sets.newHashSet();
		this.notificaciones = new ArrayList<Notificacion>();
		this.relacionesLaborales = Sets.newHashSet();
		this.cursos = Sets.newHashSet();
	}

	// Atributos

	/**
	 * Este campo es utilizado para almacenar las preferencias horarias de un
	 * profesor en torno a concertar una cita
	 */
	private String preferenciasCita;

	/**
	 * @return the preferenciasCita
	 */
	@NotBlank
	public String getPreferenciasCita() {
		return preferenciasCita;
	}

	/**
	 * @param preferenciasCita
	 *            the preferenciasCita to set
	 */
	public void setPreferenciasCita(String preferenciasCita) {
		this.preferenciasCita = preferenciasCita;
	}

	// Relaciones

	/**
	 * Eventos creados por el profesor
	 */
	private Collection<Evento> eventos;
	/**
	 * Asignaturas en las que imparte docencias el profesor
	 */
	private Collection<Materia> materias;
	/**
	 * Notificaciones enviadas y recibidas por el profesor
	 */
	private Collection<Notificacion> notificaciones;
	
	private Collection<RelacionLaboral> relacionesLaborales;
	
	private Collection<InstanciaCurso> cursos;
	
	
	@NotNull
	@OneToMany(mappedBy = "profesor")
	/**
	 * @return relacionesLaborales
	 */
	public Collection<RelacionLaboral> getRelacionesLaborales() {
		return relacionesLaborales;
	}

	/**
	 * @param relacionesLaborales the relacionesLaborales to set
	 * Establecer el relacionesLaborales
	 */
	public void setRelacionesLaborales(
			Collection<RelacionLaboral> relacionesLaborales) {
		this.relacionesLaborales = relacionesLaborales;
	}
	
	
	@NotNull
	@OneToMany(mappedBy = "profesor")
	/**
	 * @return cursos
	 */
	public Collection<InstanciaCurso> getCursos() {
		return cursos;
	}

	/**
	 * @param cursos the cursos to set
	 * Establecer el cursos
	 */
	public void setCursos(Collection<InstanciaCurso> cursos) {
		this.cursos = cursos;
	}

	@NotNull
	@OneToMany(mappedBy = "profesor")
	@LazyCollection(LazyCollectionOption.FALSE)
	/**
	 * @return the eventos
	 */
	public Collection<Evento> getEventos() {
		return eventos;
	}

	/**
	 * @param eventos
	 *            the eventos to set
	 */
	public void setEventos(Collection<Evento> eventos) {
		this.eventos = eventos;
	}

	public void addEvento(Evento evento) {
		Assert.isTrue(this.materias.contains(evento.getMateria()));
		this.eventos.add(evento);
		evento.setProfesor(this);
	}

	@NotNull
	@OneToMany(mappedBy = "profesor")
	@LazyCollection(LazyCollectionOption.FALSE)
	/**
	 * @return the asignaturas
	 */
	public Collection<Materia> getMaterias() {
		return materias;
	}

	/**
	 * @param asignaturas
	 *            the asignaturas to set
	 */
	public void setMaterias(Collection<Materia> materias) {
		this.materias = materias;
	}

	@NotNull
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "profesor")
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
	 * 
	 * @author David Romero Alcaide
	 * @param notificacion
	 */
	public void addNotificacion(Notificacion notificacion) {
		Assert.notNull(notificacion);
		this.notificaciones.add(notificacion);
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param notificacion
	 */
	public boolean removeNotificacion(Notificacion notificacion) {
		Assert.notNull(notificacion);
		return this.notificaciones.remove(notificacion);
	}

	@Transient
	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	private Collection<String> getNombresAsignaturas() {
		return this.materias.stream().map(materia->{
			return materia.getAsignatura().getNombre();
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""
				+ (getNombre() != null ? "" + getNombre() + " " : "")
				+ (getApellidos() != null ? "" + getApellidos() + ", " : "")
				+ (getTelefono() != null ? " Tlf: " + getTelefono() + ", " : "")
				+ (getEmail() != null ? " email: " + getEmail() : "") + "";
	}

}
