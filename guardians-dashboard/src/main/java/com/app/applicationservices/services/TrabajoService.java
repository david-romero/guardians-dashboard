/**
 * TrabajoService.java
 * appEducacional
 * 15/02/2014 17:19:43
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.Evaluacion;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.itemsevaluables.Trabajo;
import com.app.domain.repositories.TrabajoRepository;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 * 
 */
public class TrabajoService implements
		com.app.applicationservices.services.Service {

	/**
	 * Constructor
	 */
	public TrabajoService() {
		super();

	}

	@Autowired
	/**
	 * Repositorio para falta de asistencia para interactuar con la base de datos
	 */
	private TrabajoRepository trabajoRepositorio;

	// Servicios gestionados

	@Autowired
	/**
	 * Servicio de evaluacion gestionado por este servicio
	 */
	private EvaluacionService evaluacionService;

	// Métodos CRUD
	/**
	 * Crea una falta de asistencia
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Trabajo create() {
		Trabajo falta = new Trabajo();
		falta.setFecha(new Date(System.currentTimeMillis()));
		falta.setTitulo("Falta de Asistencia " + falta.getFecha());
		falta.setCalificacion(0.0);
		// Habria que llamar al servicio de evaluacion para que nos diera la
		// evaluacion
		// en la que estuviesemos
		Evaluacion ev = evaluacionService.findEvaluacionActual();
		falta.setEvaluacion(ev);
		return falta;
	}

	/**
	 * Obtener todas las faltas de asistencia
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<ItemEvaluable> findAll() {
		List<ItemEvaluable> trabajos = Lists.newArrayList();
		trabajos.addAll(trabajoRepositorio.findAll());
		return trabajos;
	}

	/**
	 * Encontrar una falta de asistencia por id
	 * 
	 * @author David Romero Alcaide
	 * @param profesorId
	 * @return
	 */
	public Trabajo findOne(int trabajoId) {
		Assert.isTrue(trabajoId > 0);
		Trabajo falta;
		falta = trabajoRepositorio.findOne(trabajoId);
		return falta;
	}

	/**
	 * Almacenar una falta de asistencia
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void save(Trabajo falta) {
		Assert.notNull(falta);
		Assert.notNull(falta.getAsignatura());
		Assert.notNull(falta.getAsignatura().getProfesor());
		Assert.isTrue(falta.getAsignatura().getProfesor()
				.isIdentidadConfirmada());
		trabajoRepositorio.save(falta);
	}

	/**
	 * Eliminar una falta de asistencia
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void delete(Trabajo falta) {
		Assert.notNull(falta);
		Assert.notNull(falta.getAsignatura());
		Assert.notNull(falta.getAsignatura().getProfesor());
		Assert.isTrue(falta.getAsignatura().getProfesor()
				.isIdentidadConfirmada());
		trabajoRepositorio.delete(falta);
	}

	// Otros métodos de negocio

	/**
	 * @author David Romero Alcaide
	 * @param clazz
	 */
	public void save(ItemEvaluable item) {
		Assert.isInstanceOf(Trabajo.class, item);
		Trabajo trabajo = (Trabajo) item;
		if (trabajo.getEvaluacion() == null) {
			Evaluacion ev = evaluacionService.findEvaluacionActual();
			item.setEvaluacion(ev);
		}
		save(trabajo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#findByDateAsignaturaAndTitulo
	 * (java.util.Date, com.app.domainLayer.domainModel.types.Asignatura,
	 * java.lang.String)
	 */
	
	public ItemEvaluable findByDateAsignaturaAndTitulo(Date date,
			Asignatura asign, String titulo, final Alumno a) {
		Collection<Trabajo> items = trabajoRepositorio
				.findByDateAsignaturaTitle(date, asign.getId(), titulo);
		Trabajo e = null;
		for (Trabajo trabajo : items) {
				if (trabajo.getAlumno().equals(a)) {
					e = trabajo;
				}
		}
		Assert.notNull(e);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#delete(com.app.domainLayer
	 * .domainModel.types.ItemEvaluable)
	 */
	
	public void delete(ItemEvaluable item) {
		Trabajo trab = (Trabajo) item;
		this.delete(trab);
	}

}
