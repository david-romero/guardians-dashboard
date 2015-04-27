/**
 * FaltaDeAsistenciaService.java
 * appEducacional
 * 27/01/2014 09:15:00
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

/**
 * imports
 */
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
import com.app.domain.model.types.itemsevaluables.FaltaDeAsistencia;
import com.app.domain.repositories.FaltaDeAsistenciaRepository;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class FaltaDeAsistenciaService implements
		com.app.applicationservices.services.Service {

	/**
	 * Constructor
	 */
	public FaltaDeAsistenciaService() {
		super();
	}

	@Autowired
	/**
	 * Repositorio para falta de asistencia para interactuar con la base de datos
	 */
	private FaltaDeAsistenciaRepository faltaDeAsistenciaRepositorio;

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
	public FaltaDeAsistencia create() {
		FaltaDeAsistencia falta = new FaltaDeAsistencia();
		falta.setFecha(new Date(System.currentTimeMillis()));
		falta.setTitulo("Falta de Asistencia " + falta.getFecha());
		falta.setCalificacion(0.0);
		falta.setJustificada(false);
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
		List<FaltaDeAsistencia> ite;
		ite = faltaDeAsistenciaRepositorio.findAll();
		List<ItemEvaluable> list = Lists.newArrayList();
		list.addAll(ite);
		return list;
	}

	/**
	 * Encontrar una falta de asistencia por id
	 * 
	 * @author David Romero Alcaide
	 * @param profesorId
	 * @return
	 */
	public FaltaDeAsistencia findOne(int faltaDeAsistenciaId) {
		Assert.isTrue(faltaDeAsistenciaId > 0);
		FaltaDeAsistencia falta;
		falta = faltaDeAsistenciaRepositorio.findOne(faltaDeAsistenciaId);
		return falta;
	}

	/**
	 * Almacenar una falta de asistencia
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void save(FaltaDeAsistencia falta) {
		Assert.notNull(falta.getMateria(), "pasarLista.asignatura");

		Assert.isTrue(falta.getMateria().getProfesor()
				.isIdentidadConfirmada());

		Assert.notNull(falta, "pasarLista.error");
		Assert.isTrue(
				falta.getFecha().after(
						new Date(System.currentTimeMillis() - 18000000)),
				"pasarLista.error");
		Assert.isTrue(falta.getMateria().getProfesor()
				.isIdentidadConfirmada(), "pasarLista.error");

		faltaDeAsistenciaRepositorio.save(falta);
	}

	/**
	 * Eliminar una falta de asistencia
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void delete(FaltaDeAsistencia falta) {
		Assert.notNull(falta);
		Assert.notNull(falta.getMateria());
		Assert.notNull(falta.getMateria().getProfesor());
		Assert.isTrue(falta.getMateria().getProfesor()
				.isIdentidadConfirmada());
		Assert.notNull(falta);
		faltaDeAsistenciaRepositorio.delete(falta);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#save(com.app.domainLayer
	 * .domainModel.types.ItemEvaluable)
	 */
	public void save(ItemEvaluable item) {
		FaltaDeAsistencia falta = (FaltaDeAsistencia) item;
		this.save(falta);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#delete(com.app.domainLayer
	 * .domainModel.types.ItemEvaluable)
	 */
	public void delete(ItemEvaluable item) {
		Assert.notNull(item);
		Assert.isInstanceOf(FaltaDeAsistencia.class, item);
		FaltaDeAsistencia falta = (FaltaDeAsistencia) item;
		delete(falta);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#findByDateAsignaturaAndTitulo
	 * (java.util.Date, com.app.domainLayer.domainModel.types.Asignatura,
	 * java.lang.String, com.app.domainLayer.domainModel.types.Alumno)
	 */
	public ItemEvaluable findByDateAsignaturaAndTitulo(Date date,
			Asignatura asign, String titulo, Alumno a) {
		ItemEvaluable item = null;
		Collection<FaltaDeAsistencia> faltas = faltaDeAsistenciaRepositorio
				.findByDateAsignaturaTitle(date, asign.getId(), titulo);
		for (FaltaDeAsistencia falta : faltas) {
				if (falta.getAlumno().equals(a)) {
					item = falta;
				}
		}
		Assert.notNull(item);
		return item;
	}

	// Otros métodos de negocio

}
