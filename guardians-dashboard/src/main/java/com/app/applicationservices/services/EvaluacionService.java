/**
 * EvaluacionService.java
 * appEducacional
 * 27/01/2014 11:33:10
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

import com.app.domain.model.types.Evaluacion;
import com.app.domain.repositories.EvaluacionRepository;

@Service
@Transactional
/**
 * @author David Romero Alcaide
 *
 */
public class EvaluacionService {

	/**
	 * Constructor
	 */
	public EvaluacionService() {
		super();

	}

	@Autowired
	/**
	 * Repositorio para interactuar con la base de datos
	 */
	private EvaluacionRepository evaluacionRepositorio;

	// Métodos CRUD

	/**
	 * Crear evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Evaluacion create() {
		return new Evaluacion();
	}

	/**
	 * Buscar todas las evaluaciones
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Evaluacion> findAll() {
		List<Evaluacion> ite;
		ite = evaluacionRepositorio.findAll();

		return ite;
	}

	/**
	 * encontrar una evaluacion por id
	 * 
	 * @author David Romero Alcaide
	 * @param profesorId
	 * @return
	 */
	public Evaluacion findOne(int evId) {
		Evaluacion ev;
		ev = evaluacionRepositorio.findOne(evId);
		return ev;
	}

	/**
	 * guardar una evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void save(Evaluacion ev) {
		evaluacionRepositorio.save(ev);
	}

	/**
	 * Eliminar una evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void delete(Evaluacion evaluacion) {
		Assert.notNull(evaluacion);
		Assert.isTrue(evaluacion.getId()>0);
		evaluacionRepositorio.delete(evaluacion);
	}

	// Otros métodos de negocio

	/**
	 * Busca la evaluacion actual
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Evaluacion findEvaluacionActual() {
		return findByDate(new Date(System.currentTimeMillis()));
	}

	/**
	 * Busca una evaluacion por fecha
	 * 
	 * @author David Romero Alcaide
	 * @param currentMoment
	 * @return
	 */
	public Evaluacion findByDate(Date currentMoment) {
		return evaluacionRepositorio.getEvaluacionByDate(currentMoment);
	}

	public Evaluacion findByIndicador(int indicador) {
		Evaluacion result = null;
		for (Evaluacion ev : evaluacionRepositorio.findAll()) {
			if (ev.getIndicador() == indicador) {
				result = ev;
			}
		}
		return result;
	}
}
