/**
 * NotaPorEvaluacionService.java
 * appEducacional
 * 15/02/2014 20:25:28
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.Evaluacion;
import com.app.domain.model.types.NotaPorEvaluacion;
import com.app.domain.repositories.NotaPorEvaluacionRepository;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class NotaPorEvaluacionService {

	/**
	 * Constructor
	 */
	public NotaPorEvaluacionService() {
		super();

	}

	@Autowired
	/**
	 * Repositorio para interactuar con la base de datos
	 */
	private NotaPorEvaluacionRepository notaPorEvaluacionRepositorio;

	// Servicios gestionados

	@Autowired
	private AlumnoService alumnoService;

	@Autowired
	private EvaluacionService evaluacionService;

	// Metrodos CRUD

	public NotaPorEvaluacion create() {
		NotaPorEvaluacion nota = new NotaPorEvaluacion();
		return nota;
	}

	public NotaPorEvaluacion findOne(int notaId) {
		Assert.isTrue(notaId > 0);
		return notaPorEvaluacionRepositorio.findOne(notaId);
	}

	public Collection<NotaPorEvaluacion> findAll() {
		return notaPorEvaluacionRepositorio.findAll();
	}

	public void save(NotaPorEvaluacion nota) {
		Assert.notNull(nota);
		Assert.notNull(nota.getAlumno());
		Assert.notNull(nota.getAsignatura());
		Assert.notNull(nota.getEvaluacion());
		Assert.isTrue(nota.getNotaFinal() >= 0.0);
		notaPorEvaluacionRepositorio.save(nota);
	}

	public void delete(NotaPorEvaluacion nota) {
		Assert.notNull(nota);
		Assert.isTrue(nota.getId() > 0);
		notaPorEvaluacionRepositorio.delete(nota);
	}

	// Other business methods

	public NotaPorEvaluacion findByAlumnoAndEvaluacion(Alumno alum, int ev,
			Asignatura a) {
		Assert.notNull(a);
		Assert.notNull(alum);
		return notaPorEvaluacionRepositorio.findByAlumnoAndEvaluacion(
				alum.getId(), ev, a.getId());
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @param a
	 * @return
	 */
	public List<NotaPorEvaluacion> getNotasPorEvaluacion(Alumno alum,
			Asignatura a) {
		List<NotaPorEvaluacion> notas = Lists.newArrayList();
		for (int i = 1; i <= 3; i++) {
			NotaPorEvaluacion nota1 = findByAlumnoAndEvaluacion(alum, i, a);
			if (nota1 != null) {
				notas.add(nota1);
			}

		}
		return notas;
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @param a
	 */
	public void createNotasPorEvaluacion(Alumno alum, Asignatura a) {
		for (int i = 1; i <= 3; i++) {
			NotaPorEvaluacion nota = create();
			nota.setAlumno(alum);
			Evaluacion evaluacion = evaluacionService.findByIndicador(i);
			nota.setEvaluacion(evaluacion);
			nota.setAsignatura(a);
			nota.setNotaFinal(0.0);
			nota.setAlumno(alum);
			save(nota);
			alum.addNotaPorEvaluacion(nota);
			alumnoService.save(alum);
		}
	}

}
