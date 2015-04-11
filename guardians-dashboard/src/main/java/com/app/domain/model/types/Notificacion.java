/**
 * Notificacion.java
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 18:10
 */
package com.app.domain.model.types;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.app.domain.model.DomainEntity;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * Clase que representa a las notificaciones enviadas o recibidas
 */
public class Notificacion extends DomainEntity {

	/**
	 * Constructor vacio
	 */
	public Notificacion() {
		super();
	}

	/**
	 * Contenido de la notificación
	 */
	private String contenido;
	
	/**
	 * Titulo de la notificación
	 */
	private String titulo;

	/**
	 * Quien envia la notificacion: Profesor o Tutor
	 */
	private String emisor;
	
	private boolean leida;

	@NotBlank
	/**
	 * @return the contenido
	 */
	public String getContenido() {
		return contenido;
	}

	/**
	 * @param contenido
	 *            the contenido to set
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@NotBlank
	/**
	 * @return the contenido
	 */
	public String getEmisor() {
		return emisor;
	}

	/**
	 * @param contenido
	 *            the contenido to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	/**
	 * Fecha de la notificación
	 */
	private Date fecha;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		if (fecha == null) {
			return new Date();
		}
		return (Date) fecha.clone();
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(Date fecha) {
		if (fecha != null) {
			this.fecha = new Date(fecha.getTime());
		} else {
			this.fecha = new Date();
		}
	}

	// Relaciones
	/**
	 * Padre, Madre o Tutor receptor o emisor
	 */
	private PadreMadreOTutor padreMadreOTutor;
	/**
	 * Profesor receptor o emisor
	 */
	private Profesor profesor;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	/**
	 * @return the padreMadreOTutor
	 */
	public PadreMadreOTutor getPadreMadreOTutor() {
		return padreMadreOTutor;
	}

	/**
	 * @param padreMadreOTutor
	 *            the padreMadreOTutor to set
	 */
	public void setPadreMadreOTutor(PadreMadreOTutor padreMadreOTutor) {
		this.padreMadreOTutor = padreMadreOTutor;
	}

	@NotNull
	@Valid
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Notificacion clone() throws CloneNotSupportedException {
		Notificacion noti = (Notificacion) super.clone();
		noti.setContenido(contenido);
		noti.setFecha(fecha);
		noti.setPadreMadreOTutor(padreMadreOTutor);
		noti.setProfesor(profesor);
		return noti;
	}

	@NotNull
	/**
	 * @return leida
	 */
	public boolean isLeida() {
		return leida;
	}

	/**
	 * @param leida the leida to set
	 * Establecer el leida
	 */
	public void setLeida(boolean leida) {
		this.leida = leida;
	}

	@NotEmpty
	/**
	 * @return titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 * Establecer el titulo
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
