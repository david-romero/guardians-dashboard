/**
 * EventoService.java
 * appEducacional
 * 16/01/2014 11:38:28
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

/**
 * imports
 */
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Evento;
import com.app.domain.model.types.Materia;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.EventoRepository;
import com.google.common.collect.Sets;

@Service
@Transactional
/**
 * @author David Romero Alcaide
 *
 */
public class EventoService {

	@Autowired
	/**
	 * 
	 */
	AlumnoService alumnoService;
	
	@Autowired
	/**
	 * 
	 */
	ProfesorService profesorService;

	@Autowired
	/**
	 * 
	 */
	EventoRepository eventoRepository;

	/**
	 * Constructor
	 */
	public EventoService() {
		super();

	}

	/**
	 * @author David Romero Alcaide
	 * @param eventoJson
	 */
	public void save(Evento eventoJson) {
		Assert.notNull(eventoJson);
		Assert.notNull(eventoJson.getMateria());
		Assert.notNull(eventoJson.getFecha());
		Assert.notNull(eventoJson.getItemsEvaluables());
		Assert.notNull(eventoJson.getProfesor());
		Assert.isTrue(eventoJson.getMateria().getProfesor().equals(eventoJson.getProfesor()));
		eventoRepository.save(eventoJson);
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Evento create() {
		Evento e = new Evento();
		Assert.notNull(e);
		return e;
	}

	public Collection<Evento> findAllProfesor(Profesor profesor) {
		Assert.notNull(profesor);
		return eventoRepository.findEventosProfesorPasados(profesor.getId());
	}
	
	public Collection<Evento> findAllProfesorMateria(Optional<Materia> asignatura) {
		Assert.notNull(asignatura);
		Assert.isTrue(asignatura.isPresent());
		Assert.notNull(asignatura.get().getProfesor());
		Assert.isTrue(asignatura.get().getProfesor().isIdentidadConfirmada());
		return eventoRepository.findAllEventosProfesorMateria(asignatura.get().getProfesor().getId(),asignatura.get().getId());
	}

	public Collection<Evento> findAllProfesorPasadoFuturo(Profesor profesor) {
		Assert.notNull(profesor);
		return eventoRepository.findAllEventosProfesor(profesor.getId());
	}

	/**
	 * @author David Romero Alcaide
	 * @param eventId
	 * @return
	 */
	public Evento findOne(int eventId) {
		Assert.isTrue(eventId > 0);
		return eventoRepository.findOne(eventId);
	}

	public Collection<Evento> findAll() {
		return eventoRepository.findAll();
	}



	/**
	 * @author David Romero Alcaide
	 * @param p
	 * @return
	 */
	public Collection<Evento> findAllTutor(PadreMadreOTutor p) {
		Set<Evento> eventos = Sets.newHashSet();
		for (Alumno alum : p.getTutorandos()){
			Collection<Profesor> profesoresAlumno = alumnoService.getProfesores(alum);
			for (Profesor profesor : profesoresAlumno){
				eventos.addAll(findAllProfesor(profesor));
			}
		}
		return eventos;
	}

}
